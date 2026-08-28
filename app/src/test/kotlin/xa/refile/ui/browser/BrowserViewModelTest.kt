package xa.refile.ui.browser

import com.google.common.truth.Truth.assertThat
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import xa.refile.core.webdav.FileClient
import xa.refile.core.webdav.WebDavEntry
import xa.refile.data.db.ServerConfigEntity
import xa.refile.data.repository.ServerRepository

/**
 * BrowserViewModel 单元测试：多选区间选择（Shift 语义长按连选）。
 *
 * 经 mock [ServerRepository]/[FileClient] 驱动 [BrowserViewModel.init] 加载根目录列表后，
 * 断言 [BrowserViewModel.selectRangeTo] 的区间端点包含、不可选项跳过、反向区间、
 * 非多选模式回退与锚点更新语义。
 *
 * 根目录列表（加载后按「目录在前、文件按名排序」）：
 * dir/、e01.mkv、e02.ass（字幕，不可选）、e02.mkv、e03.mkv。
 */
class BrowserViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var serverRepo: ServerRepository
    private lateinit var client: FileClient
    private val entity = ServerConfigEntity(
        id = 1L,
        name = "test",
        type = "webdav",
        baseUrl = "https://dav.example.com",
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        serverRepo = mockk()
        client = mockk()
        // 具体值打桩（避免 any() 与可空默认参数的匹配问题）：
        // init 调用 clientFor(entity) 时第二参数编译为 null，具体值等值匹配必然命中。
        coEvery { serverRepo.getServer(1L) } returns entity
        coEvery { serverRepo.clientFor(entity) } returns client
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `selectRangeTo selects all selectable items between anchor and target`() = runTest(testDispatcher) {
        val vm = newVm()

        // 点选 e01 进入多选（锚点 = e01）
        vm.enterMultiSelect("/e01.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv")

        // 长按 e03：选中 e01..e03 之间全部（含端点）；中间的字幕不可选被跳过
        vm.selectRangeTo("/e03.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv", "/e02.mkv", "/e03.mkv")
    }

    @Test
    fun `selectRangeTo supports reverse direction and is idempotent for selected range`() = runTest(testDispatcher) {
        val vm = newVm()

        vm.enterMultiSelect("/e03.mkv", isCollection = false)
        // 反向区间：锚点 e03 → 目标 e01，同样选中 e01..e03 全部
        vm.selectRangeTo("/e01.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv", "/e02.mkv", "/e03.mkv")

        // 锚点已更新为 e01，再长按 e02（已在选集内）→ 幂等，选集不变
        vm.selectRangeTo("/e02.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv", "/e02.mkv", "/e03.mkv")
    }

    @Test
    fun `selectRangeTo ignores non-selectable target`() = runTest(testDispatcher) {
        val vm = newVm()

        vm.enterMultiSelect("/e01.mkv", isCollection = false)
        // 字幕不可选：长按字幕不改变选集
        vm.selectRangeTo("/e02.ass", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv")
    }

    @Test
    fun `selectRangeTo outside multi select enters multi select with target`() = runTest(testDispatcher) {
        val vm = newVm()
        assertThat(vm.uiState.value.multiSelectMode).isFalse()

        vm.selectRangeTo("/e02.mkv", isCollection = false)
        assertThat(vm.uiState.value.multiSelectMode).isTrue()
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e02.mkv")
    }

    @Test
    fun `selectRangeTo includes directories inside the range`() = runTest(testDispatcher) {
        val vm = newVm()

        // 锚点取目录（列表首位），长按 e01 → 选中 dir + e01
        vm.enterMultiSelect("/dir", isCollection = true)
        vm.selectRangeTo("/e01.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/dir", "/e01.mkv")
    }

    @Test
    fun `toggleSelected updates anchor for subsequent range select`() = runTest(testDispatcher) {
        val vm = newVm()

        vm.enterMultiSelect("/e01.mkv", isCollection = false)
        // 点选 e02：选中集为 {e01, e02}，锚点更新为 e02
        vm.toggleSelected("/e02.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv", "/e02.mkv")

        // 长按 e03：区间为锚点 e02..e03，与现有选集合并 → e01..e03 全选
        vm.selectRangeTo("/e03.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectedPaths).containsExactly("/e01.mkv", "/e02.mkv", "/e03.mkv")
    }

    @Test
    fun `exitMultiSelect clears selection and anchor`() = runTest(testDispatcher) {
        val vm = newVm()

        vm.enterMultiSelect("/e01.mkv", isCollection = false)
        vm.selectRangeTo("/e03.mkv", isCollection = false)
        assertThat(vm.uiState.value.selectionAnchor).isEqualTo("/e03.mkv")

        vm.exitMultiSelect()
        assertThat(vm.uiState.value.multiSelectMode).isFalse()
        assertThat(vm.uiState.value.selectedPaths).isEmpty()
        assertThat(vm.uiState.value.selectionAnchor).isNull()
    }

    // ---- 辅助构造 ----

    /** 根目录 PROPFIND 结果：首项为目录自身（会被 drop(1) 过滤）。 */
    private fun rootEntries(): List<WebDavEntry> = listOf(
        WebDavEntry(href = "/", displayName = "/", isCollection = true),
        WebDavEntry(href = "/dir/", displayName = "dir", isCollection = true),
        WebDavEntry(href = "/e01.mkv", displayName = "e01.mkv", contentLength = 1L),
        WebDavEntry(href = "/e02.mkv", displayName = "e02.mkv", contentLength = 2L),
        WebDavEntry(href = "/e02.ass", displayName = "e02.ass", contentLength = 4L),
        WebDavEntry(href = "/e03.mkv", displayName = "e03.mkv", contentLength = 3L),
    )

    /** 已加载根目录列表的 VM。 */
    private suspend fun TestScope.newVm(): BrowserViewModel {
        coEvery { client.propfind("/", 1) } returns rootEntries()
        val vm = BrowserViewModel(serverRepo)
        vm.init(1L)
        advanceUntilIdle()
        return vm
    }
}
