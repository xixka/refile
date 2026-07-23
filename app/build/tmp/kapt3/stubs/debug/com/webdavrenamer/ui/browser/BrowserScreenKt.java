package com.webdavrenamer.ui.browser;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000X\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0010\u0008\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0008\u0006\u001A \u0010\u00032\u0004\u0010\u0005(\u00022\u0004\u0010\u0007(\u00032\u0004\u0010\t(\u00072\u0006\u0008\u0002\u0010\u0010(\u00088\u0001H\u0007\u001A$\u0010\u00122\u0004\u0010\u0013(\t2\u0004\u0010\u0015(\u00032\u0004\u0010\u0016(\u00032\u0004\u0010\u0017(\u00032\u0004\u0010\u0018(\u00038\u0001H\u0003\u001A8\u0010\u00192\u0004\u0010\u001A(\n2\u0004\u0010\u000C(\u00052\u0004\u0010\u001C(\u000B2\u0004\u0010\u001E(\u000B2\u0004\u0010\u001F(\u00032\u0004\u0010 (\u00032\u0004\u0010!(\u00032\u0006\u0008\u0002\u0010\"(\u000C8\u0001H\u0003\u001A\u0011\u0010$2\u0004\u0010%(\r8\u0005H\u0002\u00A2\u0006\u0002\u0010&\u001A\u000C\u0010'2\u0004\u0010((\u000E8\u0005H\u0002\"\u000E\u0010\u0000H\u0000X\u0082\u0004\u00A2\u0006\u0004\n\u0002\u0010\u0002\u00F2\u0001r\n\u00020\u0001\n\u00020\u0004\n\u00020\u0006\n\u0006\u0012\u0002\u0018\u00010\u0008\n\u00110\u0006\u00A2\u0006\u000C\u0008\u000B\u0012\u0008\u0008\u000C\u0012\u0004\u0008\u0008(\u0005\n\u00020\u000E\n\u0015\u0012\u0002\u0018\u00050\r\u00A2\u0006\u000C\u0008\u000B\u0012\u0008\u0008\u000C\u0012\u0004\u0008\u0008(\u000F\n\u000E\u0012\u0002\u0018\u0004\u0012\u0002\u0018\u0006\u0012\u0002\u0018\u00010\n\n\u00020\u0011\n\u00020\u0014\n\u00020\u001B\n\u00020\u001D\n\u00020#\n\u0004\u0018\u00010\u0006\n\u0004\u0018\u00010\u000E\u00A8\u0006)"}, d2 = {"AmberColor", "Landroidx/compose/ui/graphics/Color;", "J", "BrowserScreen", "", "serverId", "", "onBack", "Lkotlin/Function0;", "onProceedToMatch", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "", "", "selectedPaths", "viewModel", "Lcom/webdavrenamer/ui/browser/BrowserViewModel;", "MultiSelectBottomBar", "selectedCount", "", "onSelectAll", "onInvert", "onExit", "onProceed", "BrowserEntryRow", "entry", "Lcom/webdavrenamer/core/webdav/WebDavEntry;", "multiSelectMode", "", "isSelected", "onClick", "onLongClick", "onToggle", "modifier", "Landroidx/compose/ui/Modifier;", "formatSize", "bytes", "(Ljava/lang/Long;)Ljava/lang/String;", "formatDate", "raw", "app_debug"}, xs= "", pn = "", xi = 48)
public final class BrowserScreenKt {
    /**
     * 目录图标用琥珀色，对齐 MiXplorer 视觉。
     */
    private static final long AmberColor = 0L;

    /**
     * 单条浏览器项。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    private static final void BrowserEntryRow(com.webdavrenamer.core.webdav.WebDavEntry entry, java.lang.String name, boolean multiSelectMode, boolean isSelected, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onLongClick, kotlin.jvm.functions.Function0<kotlin.Unit> onToggle, androidx.compose.ui.Modifier modifier) {
    }

    /**
     * WebDAV 文件浏览器（计划 §M1 SubTask 1.5）。
     * 
     * - 顶部 TopAppBar：返回 + 可点击面包屑 + 刷新/排序菜单。
     * - 列表：每行图标（目录/视频/字幕/其它）+ 名称 + 大小 + 修改日期；iso 仅显示并置灰。
     * - 选择规则：所有类型都显示；仅 [MediaFileTypes.isSelectableVideo] 显示复选框且可勾选；
     *   非视频（目录/字幕/nfo/图片/iso）置灰、无复选框，目录点击进入。
     * - 多选：长按视频进入，底栏显示计数 + 全选/反选 + 「下一步：匹配」。
     * - 下拉刷新（material3 [PullToRefreshBox]）；空目录居中提示；加载中转圈。
     * - 系统返回键：多选先退出，否则逐级回退，根目录回退到上一屏。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class, androidx.compose.foundation.ExperimentalFoundationApi.class})
    @androidx.compose.runtime.Composable()
    public static final void BrowserScreen(long serverId, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function2<? super java.lang.Long, ? super java.util.List<java.lang.String>, kotlin.Unit> onProceedToMatch, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.browser.BrowserViewModel viewModel) {
    }

    /**
     * 多选模式底部栏：退出 + 已选计数 + 全选/反选 + 下一步（仅当选中>0）。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    private static final void MultiSelectBottomBar(int selectedCount, kotlin.jvm.functions.Function0<kotlin.Unit> onSelectAll, kotlin.jvm.functions.Function0<kotlin.Unit> onInvert, kotlin.jvm.functions.Function0<kotlin.Unit> onExit, kotlin.jvm.functions.Function0<kotlin.Unit> onProceed) {
    }

    /**
     * 截取 RFC1123 修改时间的日期部分（`dd MMM yyyy`）；格式不符时回退到首段。
     */
    private static final java.lang.String formatDate(java.lang.String raw) {
        return null;
    }

    /**
     * 人性化字节大小，如 `1.2 GB`。null 或目录返回 `—`。
     */
    private static final java.lang.String formatSize(java.lang.Long bytes) {
        return null;
    }
}
