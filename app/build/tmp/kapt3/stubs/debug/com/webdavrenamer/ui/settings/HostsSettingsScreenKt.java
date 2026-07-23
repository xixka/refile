package com.webdavrenamer.ui.settings;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000B\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0008\n\u0002\u0010\u000E\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0005\u001A\u0014\u0010\u00002\u0004\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0000H\u0007\u001A0\u0010\u00062\u0004\u0010\u0007(\u00032\u0004\u0010\t(\u00042\u0004\u0010\u000B(\u00062\u0004\u0010\u000E(\u00012\u0004\u0010\u000F(\u00012\u0004\u0010\u0010(\u00012\u0004\u0010\u0011(\u00018\u0000H\u0003\u001A\u000C\u0010\u00122\u0004\u0010\u0013(\u00058\u0000H\u0003\u001A*\u0010\u00142\u0004\u0010\u0015(\u00072\u0004\u0010\u0017(\u00072\u0004\u0010\u0018(\u00072\u0004\u0010\u0019(\u00042\u0004\u0010\u001A(\n2\u0004\u0010 (\u00018\u0000H\u0003\u00F2\u0001d\n\u00020\u0001\n\u0006\u0012\u0002\u0018\u00000\u0003\n\u00020\u0005\n\u00020\u0008\n\u00020\n\n\u00020\r\n\u0008\u0012\u0002\u0018\u0005\u0018\u00010\u000C\n\u00020\u0016\n\u00110\u0016\u00A2\u0006\u000C\u0008\u001C\u0012\u0008\u0008\u001D\u0012\u0004\u0008\u0008(\u001E\n\u0015\u0012\u0002\u0018\u00070\u000C\u00A2\u0006\u000C\u0008\u001C\u0012\u0008\u0008\u001D\u0012\u0004\u0008\u0008(\u001F\n\u000E\u0012\u0002\u0018\u0008\u0012\u0002\u0018\t\u0012\u0002\u0018\u00000\u001B\u00A8\u0006!"}, d2 = {"HostsSettingsScreen", "", "onBack", "Lkotlin/Function0;", "viewModel", "Lcom/webdavrenamer/ui/settings/HostsSettingsViewModel;", "HostEntryCard", "entry", "Lcom/webdavrenamer/core/backup/HostEntry;", "testing", "", "results", "", "Lcom/webdavrenamer/core/backup/HostsSpeedTest$IpSpeedTestResult;", "onTest", "onAutoPick", "onEdit", "onRemove", "IpResultRow", "result", "HostEditDialog", "initialHostname", "", "initialIps", "title", "hostnameEditable", "onConfirm", "Lkotlin/Function2;", "Lkotlin/ParameterName;", "name", "hostname", "ips", "onDismiss", "app_debug"}, xs= "", pn = "", xi = 48)
public final class HostsSettingsScreenKt {

    /**
     * 新增/编辑对话框。hostname 在新增时可编辑，编辑时只读。
     */
    @androidx.compose.runtime.Composable()
    private static final void HostEditDialog(java.lang.String initialHostname, java.lang.String initialIps, java.lang.String title, boolean hostnameEditable, kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.util.List<java.lang.String>, kotlin.Unit> onConfirm, kotlin.jvm.functions.Function0<kotlin.Unit> onDismiss) {
    }

    /**
     * 单条 hostname 卡片：标题 + IP 列表 + 测速结果 + 操作按钮。
     */
    @androidx.compose.runtime.Composable()
    private static final void HostEntryCard(com.webdavrenamer.core.backup.HostEntry entry, boolean testing, java.util.List<com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult> results, kotlin.jvm.functions.Function0<kotlin.Unit> onTest, kotlin.jvm.functions.Function0<kotlin.Unit> onAutoPick, kotlin.jvm.functions.Function0<kotlin.Unit> onEdit, kotlin.jvm.functions.Function0<kotlin.Unit> onRemove) {
    }

    /**
     * Hosts 设置页（spec §5.3.4 连接测试按钮 UI + §5.3.5 总开关 UI）。
     * 
     * 布局：
     * - TopAppBar「Hosts 设置」+ 返回。
     * - 总开关 [Switch]（启用/禁用 [com.webdavrenamer.core.backup.HostsDns]）。
     * - 预设按钮行：TMDB API / TMDB Image / 默认候选，点击填入对应 hostname（ips 留空待测速）。
     * - 「新增 Host」按钮 → 弹出编辑对话框。
     * - hostname 列表（LazyColumn）：每行 hostname + IP 列表 + 测试/自动选优/编辑/删除按钮 + 测速结果。
     * - 测速中显示 [CircularProgressIndicator]。
     * - 底部「测试所有连接」按钮。
     */
    @kotlin.OptIn(markerClass = {androidx.compose.material3.ExperimentalMaterial3Api.class})
    @androidx.compose.runtime.Composable()
    public static final void HostsSettingsScreen(@org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> onBack, @org.jetbrains.annotations.NotNull() com.webdavrenamer.ui.settings.HostsSettingsViewModel viewModel) {
    }

    /**
     * 单个 IP 测速结果行：延迟（颜色编码）+ 状态码 + 错误信息。
     */
    @androidx.compose.runtime.Composable()
    private static final void IpResultRow(com.webdavrenamer.core.backup.HostsSpeedTest.IpSpeedTestResult result) {
    }
}
