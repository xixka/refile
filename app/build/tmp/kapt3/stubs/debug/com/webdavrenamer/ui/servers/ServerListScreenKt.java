package com.webdavrenamer.ui.servers;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000*\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\t\n\u0002\u0008\u0006\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0004\u001A@\u0010\u00002\u0004\u0010\u0002(\u00012\u0004\u0010\u0004(\u00032\u0004\u0010\u0007(\u00032\u0006\u0008\u0002\u0010\u0008(\u00012\u0006\u0008\u0002\u0010\t(\u00012\u0006\u0008\u0002\u0010\n(\u00012\u0006\u0008\u0002\u0010\u000B(\u00012\u0006\u0008\u0002\u0010\u000C(\u00048\u0000H\u0007\u001A\u001E\u0010\u000E2\u0004\u0010\u000F(\u00052\u0004\u0010\u0011(\u00012\u0004\u0010\u0012(\u00012\u0004\u0010\u0013(\u00018\u0000H\u0003\u00F2\u0001$\n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\u0006\n\n\u0012\u0002\u0018\u0002\u0012\u0002\u0018\u00000\u0005\n\u00020\r\n\u00020\u0010\u00A8\u0006\u0014"}, d2 = {"ServerListScreen", "", "onAddServer", "Lkotlin/Function0;", "onEditServer", "Lkotlin/Function1;", "", "onOpenBrowser", "onOpenHistory", "onOpenHosts", "onOpenBackup", "onOpenSettings", "viewModel", "Lcom/webdavrenamer/ui/servers/ServerListViewModel;", "ServerRow", "server", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "onClick", "onEdit", "onDelete", "app_debug"}, xs= "", pn = "", xi = 48)
public final class ServerListScreenKt {

    /**
     * 服务器列表页（计划 §M1 SubTask 1.4.1）。
     * 
     * - 顶部栏标题"服务器" + 右上角添加按钮。
     * - 每项一张卡片：名称、Base URL、用户名、密码（仅显示 ••••••，不回显明文，红线）。
     * - 卡片点击进入文件浏览器；左滑露出编辑/删除操作。
     * - 删除前用 [AlertDialog] 二次确认。
     * - 空状态居中提示。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ServerListScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onAddServer, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onEditServer, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onOpenBrowser, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHistory, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenHosts, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenBackup, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onOpenSettings, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.servers.ServerListViewModel viewModel) {
    }

    /**
     * 单条服务器卡片，支持左滑露出编辑/删除操作。
     */
    @androidx.compose.runtime.Composable()
    private static final void ServerRow(com.webdavrenamer.data.db.ServerConfigEntity server, kotlin.jvm.functions.Function0<kotlin.Unit> onClick, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onDelete) {
    }
}
