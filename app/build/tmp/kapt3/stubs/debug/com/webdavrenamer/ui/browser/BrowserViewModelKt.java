package com.webdavrenamer.ui.browser;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000\u001E\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0002\u0008\u0004\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0005\u001A\u000C\u0010\u00002\u0004\u0010\u0002(\u00008\u0000H\u0000\u001A\u0012\u0010\u00032\u0004\u0010\u0004(\u00002\u0004\u0010\u0005(\u00008\u0000H\u0000\u001A\u0012\u0010\u00062\u0004\u0010\u0008(\u00002\u0004\u0010\t(\u00008\u0001H\u0000\u001A\u0012\u0010\n2\u0004\u0010\u0008(\u00002\u0004\u0010\t(\u00008\u0000H\u0000\u001A\u0012\u0010\u000B2\u0004\u0010\u0008(\u00002\u0004\u0010\t(\u00008\u0003H\u0000\u001A\u000C\u0010\u000E2\u0004\u0010\u000F(\u00008\u0000H\u0000\u001A\u000C\u0010\u00102\u0004\u0010\u0011(\u00008\u0000H\u0000\u00F2\u0001\u001C\n\u00020\u0001\n\u00020\u0007\n\n\u0012\u0002\u0018\u0000\u0012\u0002\u0018\u00000\r\n\u0006\u0012\u0002\u0018\u00020\u000C\u00A8\u0006\u0012"}, d2 = {"normalizePath", "", "p", "joinPath", "parent", "child", "isRoot", "", "current", "root", "parentPath", "breadcrumbs", "", "Lkotlin/Pair;", "fileNameOf", "path", "nameFromHref", "href", "app_debug"}, xs= "", pn = "", xi = 48)
public final class BrowserViewModelKt {

    /**
     * 计算面包屑各级：返回 (label, path) 列表，首项为根。
     * 根标签取 rootPath 末段，根为 "/" 时显示 "/"。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.util.List<kotlin.Pair<java.lang.String, java.lang.String>> breadcrumbs(@org.jetbrains.annotations.NotNull() java.lang.String current, @org.jetbrains.annotations.NotNull() java.lang.String root) {
        return null;
    }

    /**
     * 从完整路径取末段文件名。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String fileNameOf(@org.jetbrains.annotations.NotNull() java.lang.String path) {
        return null;
    }

    /**
     * 当前路径是否即根路径。
     */
    public static final boolean isRoot(@org.jetbrains.annotations.NotNull() java.lang.String current, @org.jetbrains.annotations.NotNull() java.lang.String root) {
        return false;
    }

    /**
     * 拼接父路径与子段，结果规范化。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String joinPath(@org.jetbrains.annotations.NotNull() java.lang.String parent, @org.jetbrains.annotations.NotNull() java.lang.String child) {
        return null;
    }

    /**
     * 从 WebDAV href 取末段并做最小 %20 解码（仅当 displayName 缺失时回退用）。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String nameFromHref(@org.jetbrains.annotations.NotNull() java.lang.String href) {
        return null;
    }

    /**
     * 规范化路径：保证以 "/" 开头，去除多余末尾斜杠（根 "/" 保留）。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String normalizePath(@org.jetbrains.annotations.NotNull() java.lang.String p) {
        return null;
    }

    /**
     * 返回上一级路径，且不低于根。
     */
    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String parentPath(@org.jetbrains.annotations.NotNull() java.lang.String current, @org.jetbrains.annotations.NotNull() java.lang.String root) {
        return null;
    }
}
