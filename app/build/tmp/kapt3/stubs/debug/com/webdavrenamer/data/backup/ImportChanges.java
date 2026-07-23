package com.webdavrenamer.data.backup;

/**
 * 变更预览（Task 5.2.3）。描述导入将造成的差异。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000 \n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u000F\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B+\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0001\u0012\u0004\u0010\u0006(\u0002\u0012\u0004\u0010\u0008(\u0001\u0012\u0004\u0010\t(\u0002\u00A2\u0006\u0004\u0008\n\u0010\u000BJ\u0007\u0010\u000C8\u0001H\u00C6\u0003J\u0007\u0010\r8\u0001H\u00C6\u0003J\u0007\u0010\u000E8\u0001H\u00C6\u0003J\u0007\u0010\u000F8\u0002H\u00C6\u0003J\u0007\u0010\u00108\u0001H\u00C6\u0003J\u0007\u0010\u00118\u0002H\u00C6\u0003J7\u0010\u00122\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00012\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0008(\u00012\u0006\u0008\u0002\u0010\t(\u00028\u0003H\u00C6\u0001J\r\u0010\u00132\u0004\u0010\u0014(\u00048\u0002H\u00D6\u0003J\u0007\u0010\u00158\u0001H\u00D6\u0001J\u0007\u0010\u00168\u0005H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0002\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0007\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u0017\u00A8\u0006\u0018"}, d2 = {"Lcom/webdavrenamer/data/backup/ImportChanges;", "", "newServers", "", "overwrittenServers", "removedServers", "settingsChanged", "", "templatesCount", "hostsChanged", "<init>", "(IIIZIZ)V", "component1", "component2", "component3", "component4", "component5", "component6", "copy", "equals", "other", "hashCode", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
public final class ImportChanges {
    /**
     * 将新增的服务器数量（备份中存在、本地按 name 不存在的）。
     */
    private final int newServers = 0;

    /**
     * 将覆盖的服务器数量（按 name 匹配已存在的）。
     */
    private final int overwrittenServers = 0;

    /**
     * 将删除的本地服务器数量（备份中不存在、本地存在的）。
     */
    private final int removedServers = 0;

    /**
     * 设置是否将发生变化。
     */
    private final boolean settingsChanged = false;

    /**
     * 自定义模板数量。
     */
    private final int templatesCount = 0;

    /**
     * Hosts 配置是否将发生变化。
     */
    private final boolean hostsChanged = false;

    /**
     * 变更预览（Task 5.2.3）。描述导入将造成的差异。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.ImportChanges copy(int newServers, int overwrittenServers, int removedServers, boolean settingsChanged, int templatesCount, boolean hostsChanged) {
        return null;
    }

    /**
     * 变更预览（Task 5.2.3）。描述导入将造成的差异。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 变更预览（Task 5.2.3）。描述导入将造成的差异。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 变更预览（Task 5.2.3）。描述导入将造成的差异。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public ImportChanges(int newServers, int overwrittenServers, int removedServers, boolean settingsChanged, int templatesCount, boolean hostsChanged) {
        super();
    }

    /**
     * 将新增的服务器数量（备份中存在、本地按 name 不存在的）。
     */
    public final int component1() {
        return 0;
    }

    /**
     * 将新增的服务器数量（备份中存在、本地按 name 不存在的）。
     */
    public final int getNewServers() {
        return 0;
    }

    /**
     * 将覆盖的服务器数量（按 name 匹配已存在的）。
     */
    public final int component2() {
        return 0;
    }

    /**
     * 将覆盖的服务器数量（按 name 匹配已存在的）。
     */
    public final int getOverwrittenServers() {
        return 0;
    }

    /**
     * 将删除的本地服务器数量（备份中不存在、本地存在的）。
     */
    public final int component3() {
        return 0;
    }

    /**
     * 将删除的本地服务器数量（备份中不存在、本地存在的）。
     */
    public final int getRemovedServers() {
        return 0;
    }

    /**
     * 设置是否将发生变化。
     */
    public final boolean component4() {
        return false;
    }

    /**
     * 设置是否将发生变化。
     */
    public final boolean getSettingsChanged() {
        return false;
    }

    /**
     * 自定义模板数量。
     */
    public final int component5() {
        return 0;
    }

    /**
     * 自定义模板数量。
     */
    public final int getTemplatesCount() {
        return 0;
    }

    /**
     * Hosts 配置是否将发生变化。
     */
    public final boolean component6() {
        return false;
    }

    /**
     * Hosts 配置是否将发生变化。
     */
    public final boolean getHostsChanged() {
        return false;
    }
}
