package com.webdavrenamer.ui.servers;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000\"\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\u001A%\u0010\u00002\u0004\u0010\u0002(\u00012\u0004\u0010\u0004(\u00032\u0004\u0010\u0006(\u00042\u0006\u0008\u0002\u0010\u0008(\u00058\u0000H\u0007\u00A2\u0006\u0002\u0010\n\u00F2\u0001&\n\u00020\u0001\n\u0004\u0018\u00010\u0003\n\u00020\u0003\n\n\u0012\u0002\u0018\u0002\u0012\u0002\u0018\u00000\u0005\n\u0006\u0012\u0002\u0018\u00000\u0007\n\u00020\t\u00A8\u0006\u000B"}, d2 = {"ServerEditScreen", "", "serverId", "", "onSaved", "Lkotlin/Function1;", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel;", "(Ljava/lang/Long;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function0;Lcom/webdavrenamer/ui/servers/ServerEditViewModel;)V", "app_debug"}, xs= "", pn = "", xi = 48)
public final class ServerEditScreenKt {

    /**
     * 添加/编辑服务器页（计划 §M1 SubTask 1.4.2）。
     * 
     * 表单字段：别名、Base URL、端口、根路径、用户名、密码（PasswordVisualTransformation）、
     * HTTPS 开关、认证方式（auto/basic/digest）。
     * 
     * - 「测试连接」调用 [ServerEditViewModel.testConnection]，结果以彩色文案反馈。
     * - 「保存」调用 [ServerEditViewModel.save]，成功后 [onSaved]；失败展示错误。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void ServerEditScreen(@org.jetbrains.annotations.Nullable() java.lang.Long serverId, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function1<? super java.lang.Long, kotlin.Unit> onSaved, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.servers.ServerEditViewModel viewModel) {
    }
}
