package com.webdavrenamer.data.prefs;

/**
 * 命名可视化选项（Task 3.3 模板编辑器）。
 * 
 * 与 [com.webdavrenamer.core.naming.NamingOptions] 一一对应，但放在 data 层以便 DataStore 持久化。
 * UI 层通过 [toNamingOptions] 转换后传给 [com.webdavrenamer.core.naming.TemplateEngine]。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000:\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000C\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0010\u000B\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B'\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0006\u0008\u0002\u0010\u0004(\u0002\u0012\u0006\u0008\u0002\u0010\u0006(\u0003\u0012\u0006\u0008\u0002\u0010\u0008(\u0004\u00A2\u0006\u0004\u0008\n\u0010\u000BJ\u0004\u0010\u000C8\u0005J\u0007\u0010\u000E8\u0001H\u00C6\u0003J\u0007\u0010\u000F8\u0002H\u00C6\u0003J\u0007\u0010\u00108\u0003H\u00C6\u0003J\u0007\u0010\u00118\u0004H\u00C6\u0003J'\u0010\u00122\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00032\u0006\u0008\u0002\u0010\u0008(\u00048\u0006H\u00C6\u0001J\r\u0010\u00132\u0004\u0010\u0015(\u00088\u0007H\u00D6\u0003J\u0007\u0010\u00168\u0004H\u00D6\u0001J\u0007\u0010\u00178\tH\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001*\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\r\n\u00020\u0000\n\u00020\u0014\n\u0004\u0018\u00010\u0001\n\u00020\u0018\u00A8\u0006\u0019"}, d2 = {"Lcom/webdavrenamer/data/prefs/VisualOptions;", "", "separator", "", "caseMode", "Lcom/webdavrenamer/core/naming/NamingOptions$Casing;", "illegalCharHandling", "Lcom/webdavrenamer/core/naming/NamingOptions$IllegalCharHandling;", "padDigits", "", "<init>", "(CLcom/webdavrenamer/core/naming/NamingOptions$Casing;Lcom/webdavrenamer/core/naming/NamingOptions$IllegalCharHandling;I)V", "toNamingOptions", "Lcom/webdavrenamer/core/naming/NamingOptions;", "component1", "component2", "component3", "component4", "copy", "equals", "", "other", "hashCode", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
public final class VisualOptions {
    private final char separator = '\u0000';

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.naming.NamingOptions.Casing caseMode = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.naming.NamingOptions.IllegalCharHandling illegalCharHandling = null;

    private final int padDigits = 0;

    public VisualOptions() {
        super();
    }

    /**
     * 命名可视化选项（Task 3.3 模板编辑器）。
     * 
     * 与 [com.webdavrenamer.core.naming.NamingOptions] 一一对应，但放在 data 层以便 DataStore 持久化。
     * UI 层通过 [toNamingOptions] 转换后传给 [com.webdavrenamer.core.naming.TemplateEngine]。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.prefs.VisualOptions copy(char separator, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.NamingOptions.Casing caseMode, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.NamingOptions.IllegalCharHandling illegalCharHandling, int padDigits) {
        return null;
    }

    /**
     * 命名可视化选项（Task 3.3 模板编辑器）。
     * 
     * 与 [com.webdavrenamer.core.naming.NamingOptions] 一一对应，但放在 data 层以便 DataStore 持久化。
     * UI 层通过 [toNamingOptions] 转换后传给 [com.webdavrenamer.core.naming.TemplateEngine]。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 命名可视化选项（Task 3.3 模板编辑器）。
     * 
     * 与 [com.webdavrenamer.core.naming.NamingOptions] 一一对应，但放在 data 层以便 DataStore 持久化。
     * UI 层通过 [toNamingOptions] 转换后传给 [com.webdavrenamer.core.naming.TemplateEngine]。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 命名可视化选项（Task 3.3 模板编辑器）。
     * 
     * 与 [com.webdavrenamer.core.naming.NamingOptions] 一一对应，但放在 data 层以便 DataStore 持久化。
     * UI 层通过 [toNamingOptions] 转换后传给 [com.webdavrenamer.core.naming.TemplateEngine]。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public VisualOptions(char separator, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.NamingOptions.Casing caseMode, @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.NamingOptions.IllegalCharHandling illegalCharHandling, int padDigits) {
        super();
    }

    public final char component1() {
        return '\u0000';
    }

    public final char getSeparator() {
        return '\u0000';
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.naming.NamingOptions.Casing component2() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.naming.NamingOptions.Casing getCaseMode() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.naming.NamingOptions.IllegalCharHandling component3() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.naming.NamingOptions.IllegalCharHandling getIllegalCharHandling() {
        return null;
    }

    public final int component4() {
        return 0;
    }

    public final int getPadDigits() {
        return 0;
    }

    /**
     * 转为 core 层 [NamingOptions] 供模板引擎使用。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.core.naming.NamingOptions toNamingOptions() {
        return null;
    }
}
