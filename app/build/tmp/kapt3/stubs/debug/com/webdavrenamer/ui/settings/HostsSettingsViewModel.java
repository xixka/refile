package com.webdavrenamer.ui.settings;

/**
 * Hosts 设置 ViewModel（spec §5.3.3–5.3.5）。
 * 
 * 状态：
 * - [hostsConfig]：从 [SettingsRepository.hostsConfig] 派生的可观察配置（开关 + 条目）。
 * - [testing]：测速进行中标志，UI 用以禁用按钮/显示进度。
 * - [testResults]：每个 hostname 的测速结果列表（hostname → 各 IP 结果）。
 * 
 * 方法覆盖 spec §5.3.4 UI 的所有操作：
 * - [toggleEnabled]：总开关持久化（§5.3.5）。
 * - [addHost]/[editHost]/[removeHost]：条目 CRUD。
 * - [testConnection]：测指定 hostname 全部 IP。
 * - [testAllConnections]：测所有 hostname，并行。
 * - [autoPickFastest]：测速后选延迟最低可用 IP 设为该 hostname 唯一 ips（§5.3.3）。
 * - [applyPreset]：把预设域名填入条目（ips 留空待用户测速）。
 * 
 * Hosts 写入全部经 [SettingsRepository.setHostsConfig] 落盘，OkHttpClient 在使用方
 * （[com.webdavrenamer.core.tmdb.TmdbClient] 与 [com.webdavrenamer.data.repository.ServerRepository]）
 * 构造时读取该 Flow 应用 [com.webdavrenamer.core.backup.HostsDns]。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010\u000B\n\u0002\u0008\u0003\n\u0002\u0010$\n\u0002\u0010\u000E\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0002\u0008\u000F\n\u0002\u0018\u0002\n\u0002\u0008\u0004\u0008\u0007\u0012\u0001\u0000\u0018\u0000 1:\u000201B\u0015\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\n\u0010\u001D2\u0004\u0010\u001F(\u00058\u0010J\u0010\u0010 2\u0004\u0010!(\u00082\u0004\u0010\"(\u00118\u0010J\u0010\u0010#2\u0004\u0010!(\u00082\u0004\u0010$(\u00118\u0010J\n\u0010%2\u0004\u0010!(\u00088\u0010J\n\u0010&2\u0004\u0010!(\u00088\u0010J\u0004\u0010'8\u0010J\n\u0010(2\u0004\u0010!(\u00088\u0010J\n\u0010)2\u0004\u0010*(\u00088\u0010J\u0012\u0010+2\u0004\u0010!(\u00088\u0011H\u0082@\u00A2\u0006\u0002\u0010,J\u0018\u0010-2\u0004\u0010/(\u00132\u0004\u0010!(\u00082\u0004\u0010\"(\u00118\u0013H\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0008H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000B\u0010\u000CR\u000C\u0010\rH\u0006X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0010H\u0007\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0011\u0010\u000CR\u000C\u0010\u0012H\u000CX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0017H\r\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0018\u0010\u000CR\u000F\u0010\u0019H\u000F\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001B\u0010\u001C\u00F2\u0001|\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\n\n\u0006\u0012\u0002\u0018\u00030\t\n\u00020\u000F\n\u0006\u0012\u0002\u0018\u00050\u000E\n\u0006\u0012\u0002\u0018\u00050\t\n\u00020\u0014\n\u00020\u0016\n\u0006\u0012\u0002\u0018\t0\u0015\n\n\u0012\u0002\u0018\u0008\u0012\u0002\u0018\n0\u0013\n\u0006\u0012\u0002\u0018\u000B0\u000E\n\u0006\u0012\u0002\u0018\u000B0\t\n\u00020\u001A\n\u0006\u0012\u0002\u0018\u000E0\u0015\n\u00020\u001E\n\u0006\u0012\u0002\u0018\u00080\u0015\n\u00020.\n\u0006\u0012\u0002\u0018\u00120\u0015\u00A8\u00062"}, d2 = {"Lcom/webdavrenamer/ui/settings/HostsSettingsViewModel;", "Landroidx/lifecycle/ViewModel;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "speedTest", "Lcom/webdavrenamer/core/backup/HostsSpeedTest;", "<init>", "(Lcom/webdavrenamer/data/prefs/SettingsRepository;Lcom/webdavrenamer/core/backup/HostsSpeedTest;)V", "hostsConfig", "Lkotlinx/coroutines/flow/StateFlow;", "Lcom/webdavrenamer/core/backup/HostsConfig;", "getHostsConfig", "()Lkotlinx/coroutines/flow/StateFlow;", "_testing", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "testing", "getTesting", "_testResults", "", "", "", "Lcom/webdavrenamer/core/backup/HostsSpeedTest$IpSpeedTestResult;", "testResults", "getTestResults", "presetOptions", "Lcom/webdavrenamer/ui/settings/HostsSettingsViewModel$PresetOption;", "getPresetOptions", "()Ljava/util/List;", "toggleEnabled", "", "enabled", "addHost", "hostname", "ips", "editHost", "newIps", "removeHost", "testConnection", "testAllConnections", "autoPickFastest", "applyPreset", "presetName", "currentIpsFor", "(Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "upsertEntry", "Lcom/webdavrenamer/core/backup/HostEntry;", "entries", "PresetOption", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HostsSettingsViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.backup.HostsSpeedTest speedTest = null;

    /**
     * 当前 hosts 配置（开关 + 条目）。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.core.backup.HostsConfig> hostsConfig = null;

    /**
     * 是否正在测速。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _testing = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> testing = null;

    /**
     * 各 hostname 的测速结果。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.Map<java.lang.String, java.util.List<com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult>>> _testResults = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult>>> testResults = null;

    /**
     * 预设列表，供 UI 渲染按钮行。
     */
    @org.jetbrains.annotations.NotNull()
    private final java.util.List<com.webdavrenamer.ui.settings.HostsSettingsViewModel.PresetOption> presetOptions = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.ui.settings.HostsSettingsViewModel.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRESET_TMDB_API = "TMDB_API";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRESET_TMDB_IMAGE = "TMDB_IMAGE";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRESET_DEFAULT_CANDIDATES = "DEFAULT_CANDIDATES";

    @javax.inject.Inject()
    public HostsSettingsViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.backup.HostsSpeedTest speedTest) {
        super();
    }

    /**
     * 当前 hosts 配置（开关 + 条目）。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.core.backup.HostsConfig> getHostsConfig() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getTesting() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.Map<java.lang.String, java.util.List<com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult>>> getTestResults() {
        return null;
    }

    /**
     * 预设列表，供 UI 渲染按钮行。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.ui.settings.HostsSettingsViewModel.PresetOption> getPresetOptions() {
        return null;
    }

    /**
     * 切换总开关（§5.3.5）。
     */
    public final void toggleEnabled(boolean enabled) {
    }

    /**
     * 新增 hostname（若已存在则覆盖 ips）。
     */
    public final void addHost(@org.jetbrains.annotations.NotNull() java.lang.String hostname, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> ips) {
    }

    /**
     * 编辑指定 hostname 的 ips 列表。
     */
    public final void editHost(@org.jetbrains.annotations.NotNull() java.lang.String hostname, @org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> newIps) {
    }

    /**
     * 删除指定 hostname 条目并清除其测速结果。
     */
    public final void removeHost(@org.jetbrains.annotations.NotNull() java.lang.String hostname) {
    }

    /**
     * 测指定 hostname 的所有 IP（§5.3.4 连接测试按钮）。
     */
    public final void testConnection(@org.jetbrains.annotations.NotNull() java.lang.String hostname) {
    }

    /**
     * 测所有 hostname 的所有 IP，并行（§5.3.4 测试所有连接）。
     */
    public final void testAllConnections() {
    }

    /**
     * 测速后选延迟最低可用 IP 设为该 hostname 唯一 ips（§5.3.3 自动测速）。
     */
    public final void autoPickFastest(@org.jetbrains.annotations.NotNull() java.lang.String hostname) {
    }

    /**
     * 应用预设（添加对应 hostname 条目，ips 留空待测速）。
     */
    public final void applyPreset(@org.jetbrains.annotations.NotNull() java.lang.String presetName) {
    }

    /**
     * 取当前 hostname 的 ips 列表。
     */
    private final java.lang.Object currentIpsFor(java.lang.String hostname, kotlin.coroutines.Continuation<? super java.util.List<java.lang.String>> $completion) {
        return null;
    }

    /**
     * 插入或更新条目（按 hostname 忽略大小写匹配）。
     */
    private final java.util.List<com.webdavrenamer.core.backup.HostEntry> upsertEntry(java.util.List<com.webdavrenamer.core.backup.HostEntry> entries, java.lang.String hostname, java.util.List<java.lang.String> ips) {
        return null;
    }

    /**
     * 预设按钮展示用：name → 标签。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0013\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u00A2\u0006\u0004\u0008\u0005\u0010\u0006J\u0007\u0010\u00078\u0001H\u00C6\u0003J\u0007\u0010\u00088\u0001H\u00C6\u0003J\u0017\u0010\t2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00018\u0002H\u00C6\u0001J\r\u0010\n2\u0004\u0010\u000C(\u00048\u0003H\u00D6\u0003J\u0007\u0010\r8\u0005H\u00D6\u0001J\u0007\u0010\u000F8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\u000B\n\u0004\u0018\u00010\u0001\n\u00020\u000E\u00A8\u0006\u0010"}, d2 = {"Lcom/webdavrenamer/ui/settings/HostsSettingsViewModel$PresetOption;", "", "name", "", "label", "<init>", "(Ljava/lang/String;Ljava/lang/String;)V", "component1", "component2", "copy", "equals", "", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class PresetOption {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String name = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String label = null;

        /**
         * 预设按钮展示用：name → 标签。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.settings.HostsSettingsViewModel.PresetOption copy(@org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String label) {
            return null;
        }

        /**
         * 预设按钮展示用：name → 标签。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 预设按钮展示用：name → 标签。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 预设按钮展示用：name → 标签。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public PresetOption(@org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String label) {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getName() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getLabel() {
            return null;
        }
    }
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0014\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0003\u0008\u0082\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0001X\u0086TR\u0007\u0010\u0007H\u0001X\u0086T\u00F2\u0001\u0008\n\u00020\u0001\n\u00020\u0005\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/ui/settings/HostsSettingsViewModel$Companion;", "", "<init>", "()V", "PRESET_TMDB_API", "", "PRESET_TMDB_IMAGE", "PRESET_DEFAULT_CANDIDATES", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class Companion {

        private Companion() {
            super();
        }
    }
}
