package com.webdavrenamer.ui.navigation;

/**
 * 应用导航图（计划 §M1 SubTask 1.4 导航接入 / §M2 Task 2.4 匹配流程接入 / §M3 Task 3.4 预览接入）。
 * 
 * 起始目的地为服务器列表。编辑路由用可选 query 参数 `id`（<=0 或缺省表示新增）。
 * 浏览器路由接 [com.webdavrenamer.ui.browser.BrowserScreen]；匹配路由接 [MatchScreen]；
 * 预览路由接 [PreviewScreen]；进度路由为占位（Task 4.3 实现完整页面后替换）。
 * 
 * selectedPaths 传递：浏览器 `onProceedToMatch` 把选中视频完整路径写入 Activity 作用域的
 * [MatchSessionViewModel]，匹配页读取后转交 [com.webdavrenamer.ui.match.MatchViewModel]。
 * 避免把含特殊字符的 List<String> 编码进导航参数。
 * 
 * matches 传递（Task 3.4）：匹配页 `onProceedToPreview` 前把已匹配结果写入会话 VM 的
 * `matches`，预览页读取后渲染目标路径，同样避免把复杂对象编码进导航参数。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000$\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0013\n\u0002\u0010\t\n\u0002\u0008\u0006\n\u0002\u0010\u0008\n\u0002\u0008\t\u0008\u00C6\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\u000F\u0010\u00172\u0004\u0010\u0018(\u00028\u0001\u00A2\u0006\u0002\u0010\u001AJ\n\u0010\u001B2\u0004\u0010\u001C(\u00038\u0001J\n\u0010\u001D2\u0004\u0010\u001C(\u00038\u0001J\n\u0010\u001E2\u0004\u0010\u001F(\u00048\u0001J\n\u0010!2\u0004\u0010\u001C(\u00038\u0001J\n\u0010\"2\u0004\u0010#(\u00018\u0001J\u0004\u0010$8\u0001J\u0004\u0010%8\u0001J\u0004\u0010&8\u0001J\u0004\u0010'8\u0001J\u0004\u0010(8\u0001R\u000C\u0010\u0004H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0006H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0007H\u0001X\u0082T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\tH\u0001X\u0082T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\nH\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000BH\u0001X\u0082T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000CH\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\rH\u0001X\u0082T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000EH\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000FH\u0001X\u0082T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0010H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0011H\u0001X\u0082T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0012H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0013H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0014H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0015H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0016H\u0001X\u0086T\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u0016\n\u00020\u0001\n\u00020\u0005\n\u0004\u0018\u00010\u0019\n\u00020\u0019\n\u00020 \u00A8\u0006)"}, d2 = {"Lcom/webdavrenamer/ui/navigation/Routes;", "", "<init>", "()V", "SERVERS", "", "SERVER_EDIT_ROUTE", "SERVER_EDIT_BASE", "BROWSER_ROUTE", "BROWSER_BASE", "MATCH", "MATCH_BASE", "EDIT_MATCH", "EDIT_MATCH_BASE", "PREVIEW", "PREVIEW_BASE", "PROGRESS", "PROGRESS_BASE", "TEMPLATE_EDITOR", "HOSTS_SETTINGS", "HISTORY", "BACKUP", "SETTINGS", "serverEdit", "id", "", "(Ljava/lang/Long;)Ljava/lang/String;", "browser", "serverId", "match", "editMatch", "matchIndex", "", "preview", "progress", "workId", "templateEditor", "hostsSettings", "history", "backup", "settings", "app_debug"}, xs= "", pn = "", xi = 48)
public final class Routes {
    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.ui.navigation.Routes INSTANCE = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SERVERS = "servers";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SERVER_EDIT_ROUTE = "servers/edit?id={id}";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String SERVER_EDIT_BASE = "servers/edit";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BROWSER_ROUTE = "browser/{serverId}";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String BROWSER_BASE = "browser";

    /**
     * 匹配页路由（Task 2.4）。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String MATCH = "match/{serverId}";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String MATCH_BASE = "match";

    /**
     * Edit Match 路由（Task 2.5）：`edit_match/{matchIndex}`，索引指向会话 VM 的 matchedFiles。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String EDIT_MATCH = "edit_match/{matchIndex}";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String EDIT_MATCH_BASE = "edit_match";

    /**
     * 预览页路由（Task 3.4 接入）。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PREVIEW = "preview/{serverId}";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PREVIEW_BASE = "preview";

    /**
     * 执行进度页路由（Task 4.3 实现完整页面，此处先占位接通预览页跳转）。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PROGRESS = "progress/{workId}";

    @org.jetbrains.annotations.NotNull()
    private static final java.lang.String PROGRESS_BASE = "progress";

    /**
     * 模板编辑器路由（Task 3.3）。从设置页跳转。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String TEMPLATE_EDITOR = "template_editor";

    /**
     * Hosts 设置路由（Task 5.3.4/5.3.5）。从设置页跳转。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HOSTS_SETTINGS = "hosts_settings";

    /**
     * 历史记录路由（Task 5.1.3）。单页承载列表 + 详情展开/折叠。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String HISTORY = "history";

    /**
     * 备份与恢复路由（Task 5.2）。从服务器列表页跳转。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String BACKUP = "backup";

    /**
     * 设置中心路由（Task 5.4）。作为所有子设置功能的统一入口，从服务器列表页齿轮图标跳转。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String SETTINGS = "settings";

    private Routes() {
        super();
    }

    /**
     * 编辑页跳转串。id 为 null 或 <=0 表示新增。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String serverEdit(@org.jetbrains.annotations.Nullable() java.lang.Long id) {
        return null;
    }

    /**
     * 文件浏览器跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String browser(long serverId) {
        return null;
    }

    /**
     * 匹配页跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String match(long serverId) {
        return null;
    }

    /**
     * Edit Match 跳转串（Task 2.5）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String editMatch(int matchIndex) {
        return null;
    }

    /**
     * 预览页跳转串（Task 3.4 接入）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String preview(long serverId) {
        return null;
    }

    /**
     * 执行进度页跳转串（Task 3.4 预览页入队后跳转；workId 为 WorkManager UUID 字符串）。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String progress(@org.jetbrains.annotations.NotNull() java.lang.String workId) {
        return null;
    }

    /**
     * 模板编辑器跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String templateEditor() {
        return null;
    }

    /**
     * Hosts 设置页跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String hostsSettings() {
        return null;
    }

    /**
     * 历史记录页跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String history() {
        return null;
    }

    /**
     * 备份与恢复页跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String backup() {
        return null;
    }

    /**
     * 设置中心页跳转串。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String settings() {
        return null;
    }
}
