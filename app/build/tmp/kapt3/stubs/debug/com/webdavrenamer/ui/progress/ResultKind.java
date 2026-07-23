package com.webdavrenamer.ui.progress;

/**
 * 结果态分类，决定大图标与文案。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0008\u0008\u0082\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006j\u0002\u0008\u0007j\u0002\u0008\u0008\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\t"}, d2 = {"Lcom/webdavrenamer/ui/progress/ResultKind;", "", "<init>", "(Ljava/lang/String;I)V", "ALL_SUCCESS", "PARTIAL_FAILURE", "ALL_FAILURE", "CANCELLED", "ERROR", "app_debug"}, xs= "", pn = "", xi = 48)
enum ResultKind {
    ALL_SUCCESS,
    PARTIAL_FAILURE,
    ALL_FAILURE,
    CANCELLED,
    ERROR;


    ResultKind() {
    }

    @org.jetbrains.annotations.NotNull()
    public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.progress.ResultKind> getEntries() {
        return null;
    }
}
