package com.webdavrenamer.data.repository;

/**
 * [com.webdavrenamer.core.rename.CompanionRename] 列表 ↔ JSON 的简易编解码器。
 * 
 * :app 模块不直接依赖 kotlinx-serialization-json（:core 内为 implementation 不传递），
 * 故用极简的手写 JSON 编解码：仅处理 List<CompanionRename> 这种 [{s,t},{s,t}] 形态。
 * 字符串值按 JSON 规范转义（引号/反斜杠/控制字符）。
 * 
 * 仅用于历史落库，非热路径，性能可接受。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001E\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0006\u0008\u00C2\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003J\n\u0010\u00042\u0004\u0010\u0006(\u00038\u0001J\n\u0010\t2\u0004\u0010\n(\u00018\u0003J\u000C\u0010\u000B2\u0004\u0010\u000C(\u00018\u0001H\u0002J\u000C\u0010\r2\u0004\u0010\u000C(\u00018\u0001H\u0002\u00F2\u0001\u0014\n\u00020\u0001\n\u00020\u0005\n\u00020\u0008\n\u0006\u0012\u0002\u0018\u00020\u0007\u00A8\u0006\u000E"}, d2 = {"Lcom/webdavrenamer/data/repository/CompanionJson;", "", "<init>", "()V", "encode", "", "list", "", "Lcom/webdavrenamer/core/rename/CompanionRename;", "decode", "json", "quote", "s", "unescape", "app_debug"}, xs= "", pn = "", xi = 48)
final class CompanionJson {
    @org.jetbrains.annotations.NotNull()
    public static final com.webdavrenamer.data.repository.CompanionJson INSTANCE = null;

    private CompanionJson() {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String encode(@org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.core.rename.CompanionRename> list) {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.util.List<com.webdavrenamer.core.rename.CompanionRename> decode(@org.jetbrains.annotations.NotNull() java.lang.String json) {
        return null;
    }

    private final java.lang.String quote(java.lang.String s) {
        return null;
    }

    private final java.lang.String unescape(java.lang.String s) {
        return null;
    }
}
