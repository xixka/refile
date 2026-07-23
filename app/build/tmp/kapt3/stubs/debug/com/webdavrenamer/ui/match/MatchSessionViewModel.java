package com.webdavrenamer.ui.match;

/**
 * 跨匹配系列页面共享的会话 ViewModel（Task 2.4 导航接入 / Task 2.5 Edit Match 数据桥 / Task 3.4 预览数据桥）。
 * 
 * 在 [com.webdavrenamer.ui.navigation.AppNavHost] 中以 `hiltViewModel()`（Activity 作用域）
 * 创建，浏览器 `onProceedToMatch` 通过 [setFiles] 写入用户选中的视频完整路径，
 * [MatchScreen] 读取 [selectedPaths] 后转交 [MatchViewModel]。
 * 
 * Task 2.5 扩展：[matchedFiles] 作为 MatchScreen ↔ EditMatchScreen 之间的匹配结果桥。
 * Task 3.4 扩展：[matches] 作为 MatchScreen → 预览页的已匹配结果桥，供预览页渲染目标路径。
 * 
 * 用 Activity 作用域而非目的地图作用域，是为了让路径在 servers → browser → match → edit_match → preview
 * 的整个回退栈生命周期内保持，且无需把 List<String> 编码进导航参数（路径含特殊字符）。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000B\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0010\u000E\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0007\n\u0002\u0010\u0008\n\u0002\u0008\u0004\u0008\u0007\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0007\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\n\u0010\u000C2\u0004\u0010\u000E(\u00028\u0005J\n\u0010\u00132\u0004\u0010\u0011(\u00078\u0005J\u0012\u0010\u001B2\u0004\u0010\u001C(\u00072\u0006\u0008\u0002\u0010\u001D(\n8\u0005J\u0010\u0010\u001E2\u0004\u0010\u001F(\r2\u0004\u0010!(\u00068\u0005J\n\u0010\"2\u0004\u0010\u001C(\u00078\u0005J\u0004\u0010#8\u0005R\u000C\u0010\u0004H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0008H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\n\u0010\u000BR\u000C\u0010\u000FH\u0008X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0011H\t\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0012\u0010\u000BR\u000C\u0010\u0014H\u0008X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0015H\t\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0016\u0010\u000BR\u000C\u0010\u0017H\u000BX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0019H\u000C\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001A\u0010\u000B\u00F2\u0001X\n\u00020\u0001\n\u00020\u0007\n\u0006\u0012\u0002\u0018\u00010\u0006\n\u0006\u0012\u0002\u0018\u00020\u0005\n\u0006\u0012\u0002\u0018\u00020\t\n\u00020\r\n\u00020\u0010\n\u0006\u0012\u0002\u0018\u00060\u0006\n\u0006\u0012\u0002\u0018\u00070\u0005\n\u0006\u0012\u0002\u0018\u00070\t\n\u00020\u0018\n\u0006\u0012\u0002\u0018\n0\u0005\n\u0006\u0012\u0002\u0018\n0\t\n\u00020 \u00A8\u0006$"}, d2 = {"Lcom/webdavrenamer/ui/match/MatchSessionViewModel;", "Landroidx/lifecycle/ViewModel;", "<init>", "()V", "_selectedPaths", "Lkotlinx/coroutines/flow/MutableStateFlow;", "", "", "selectedPaths", "Lkotlinx/coroutines/flow/StateFlow;", "getSelectedPaths", "()Lkotlinx/coroutines/flow/StateFlow;", "setFiles", "", "paths", "_matches", "Lcom/webdavrenamer/ui/match/MatchViewModel$FileMatch;", "matches", "getMatches", "setMatches", "_matchedFiles", "matchedFiles", "getMatchedFiles", "_dirty", "", "dirty", "getDirty", "setMatchedFiles", "files", "markDirty", "updateMatchedFile", "index", "", "file", "replaceMatchedFiles", "clearDirty", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class MatchSessionViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<java.lang.String>> _selectedPaths = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> selectedPaths = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch>> _matches = null;

    /**
     * 已匹配（含 TMDB 元数据）的文件列表，由匹配页在跳转预览页前写入。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch>> matches = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch>> _matchedFiles = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch>> matchedFiles = null;

    /**
     * 脏标记：仅当 EditMatch 回写后置 true。MatchScreen 消费后 [clearDirty]。
     * 跳转编辑前的 [setMatchedFiles] 不置脏，避免回退时误触发覆盖。
     */
    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _dirty = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> dirty = null;

    @javax.inject.Inject()
    public MatchSessionViewModel() {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<java.lang.String>> getSelectedPaths() {
        return null;
    }

    /**
     * 浏览器跳转匹配页前写入选中文件完整路径列表。
     */
    public final void setFiles(@org.jetbrains.annotations.NotNull() java.util.List<java.lang.String> paths) {
    }

    /**
     * 已匹配（含 TMDB 元数据）的文件列表，由匹配页在跳转预览页前写入。
     */
    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch>> getMatches() {
        return null;
    }

    /**
     * 匹配页跳转预览页前写入已匹配文件列表，供预览页渲染目标路径。
     */
    public final void setMatches(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> matches) {
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch>> getMatchedFiles() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getDirty() {
        return null;
    }

    /**
     * 跳转 EditMatch 前写入当前匹配结果快照。默认不置脏。
     */
    public final void setMatchedFiles(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> files, boolean markDirty) {
    }

    /**
     * EditMatch 单条保存后按索引回写（Task 2.5.1/2.5.2/2.5.3）。
     */
    public final void updateMatchedFile(int index, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.match.MatchViewModel.FileMatch file) {
    }

    /**
     * EditMatch 线性对齐批量保存后回写整表（Task 2.5.4）。
     */
    public final void replaceMatchedFiles(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.ui.match.MatchViewModel.FileMatch> files) {
    }

    /**
     * MatchScreen 消费完编辑结果后清除脏标记。
     */
    public final void clearDirty() {
    }
}
