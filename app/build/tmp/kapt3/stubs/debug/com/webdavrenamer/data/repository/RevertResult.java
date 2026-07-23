package com.webdavrenamer.data.repository;

/**
 * 整批撤销结果（计划 §M5 SubTask 5.1.2 / 5.1.4）。
 * 
 * - [Success]：所有可撤销条目均成功反向 MOVE。
 * - [Partial]：部分成功，[failedEntries] 为失败项（含原因）。UI 据此提示「已回滚 N/M 条」。
 * - [Failure]：撤销前置条件不满足（批次不存在 / 服务器已删除 / 已撤销等），未执行任何 MOVE。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001A\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u00086\u0012\u0001\u0000\u0018\u0000:\u0003\u0004\u0005\u0006B\t\u0008\u0004\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u0082\u0001\u0003\u0007\u0008\t\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\n"}, d2 = {"Lcom/webdavrenamer/data/repository/RevertResult;", "", "<init>", "()V", "Success", "Partial", "Failure", "Lcom/webdavrenamer/data/repository/RevertResult$Failure;", "Lcom/webdavrenamer/data/repository/RevertResult$Partial;", "Lcom/webdavrenamer/data/repository/RevertResult$Success;", "app_debug"}, xs= "", pn = "", xi = 48)
public abstract class RevertResult {

    private RevertResult() {
        super();
    }

    /**
     * 全部回滚成功。rolledBack=已回滚条数，total=需回滚条数。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0013\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u00A2\u0006\u0004\u0008\u0005\u0010\u0006J\u0007\u0010\u00078\u0001H\u00C6\u0003J\u0007\u0010\u00088\u0001H\u00C6\u0003J\u0017\u0010\t2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00018\u0002H\u00C6\u0001J\r\u0010\n2\u0004\u0010\u000C(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000E8\u0001H\u00D6\u0001J\u0007\u0010\u000F8\u0005H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\u000B\n\u0004\u0018\u00010\r\n\u00020\u0010\u00A8\u0006\u0011"}, d2 = {"Lcom/webdavrenamer/data/repository/RevertResult$Success;", "Lcom/webdavrenamer/data/repository/RevertResult;", "rolledBack", "", "total", "<init>", "(II)V", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Success extends com.webdavrenamer.data.repository.RevertResult {
        private final int rolledBack = 0;

        private final int total = 0;

        /**
         * 全部回滚成功。rolledBack=已回滚条数，total=需回滚条数。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.repository.RevertResult.Success copy(int rolledBack, int total) {
            return null;
        }

        /**
         * 全部回滚成功。rolledBack=已回滚条数，total=需回滚条数。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 全部回滚成功。rolledBack=已回滚条数，total=需回滚条数。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 全部回滚成功。rolledBack=已回滚条数，total=需回滚条数。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public Success(int rolledBack, int total) {
        }

        public final int component1() {
            return 0;
        }

        public final int getRolledBack() {
            return 0;
        }

        public final int component2() {
            return 0;
        }

        public final int getTotal() {
            return 0;
        }
    }
    /**
     * 部分回滚成功。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0002\u0008\u0002\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0019\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0003\u00A2\u0006\u0004\u0008\u0008\u0010\tJ\u0007\u0010\n8\u0001H\u00C6\u0003J\u0007\u0010\u000B8\u0001H\u00C6\u0003J\u0007\u0010\u000C8\u0003H\u00C6\u0003J\u001F\u0010\r2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00038\u0004H\u00C6\u0001J\r\u0010\u000E2\u0004\u0010\u0010(\u00068\u0005H\u00D6\u0003J\u0007\u0010\u00128\u0001H\u00D6\u0001J\u0007\u0010\u00138\u0007H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0003\u00A2\u0006\u0002\n\u0000\u00F2\u0001&\n\u00020\u0001\n\u00020\u0003\n\u00020\u0007\n\u0006\u0012\u0002\u0018\u00020\u0006\n\u00020\u0000\n\u00020\u000F\n\u0004\u0018\u00010\u0011\n\u00020\u0014\u00A8\u0006\u0015"}, d2 = {"Lcom/webdavrenamer/data/repository/RevertResult$Partial;", "Lcom/webdavrenamer/data/repository/RevertResult;", "rolledBack", "", "total", "failedEntries", "", "Lcom/webdavrenamer/data/repository/FailedEntry;", "<init>", "(IILjava/util/List;)V", "component1", "component2", "component3", "copy", "equals", "", "other", "", "hashCode", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Partial extends com.webdavrenamer.data.repository.RevertResult {
        private final int rolledBack = 0;

        private final int total = 0;

        @org.jetbrains.annotations.NotNull()
        private final java.util.List<com.webdavrenamer.data.repository.FailedEntry> failedEntries = null;

        /**
         * 部分回滚成功。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.repository.RevertResult.Partial copy(int rolledBack, int total, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.repository.FailedEntry> failedEntries) {
            return null;
        }

        /**
         * 部分回滚成功。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 部分回滚成功。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 部分回滚成功。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public Partial(int rolledBack, int total, @org.jetbrains.annotations.NotNull() java.util.List<com.webdavrenamer.data.repository.FailedEntry> failedEntries) {
        }

        public final int component1() {
            return 0;
        }

        public final int getRolledBack() {
            return 0;
        }

        public final int component2() {
            return 0;
        }

        public final int getTotal() {
            return 0;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.data.repository.FailedEntry> component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.util.List<com.webdavrenamer.data.repository.FailedEntry> getFailedEntries() {
            return null;
        }
    }
    /**
     * 未执行任何回滚（前置失败）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\r\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0007\u0010\u00068\u0001H\u00C6\u0003J\u000F\u0010\u00072\u0006\u0008\u0002\u0010\u0002(\u00018\u0002H\u00C6\u0001J\r\u0010\u00082\u0004\u0010\n(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000C8\u0005H\u00D6\u0001J\u0007\u0010\u000E8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\t\n\u0004\u0018\u00010\u000B\n\u00020\r\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/data/repository/RevertResult$Failure;", "Lcom/webdavrenamer/data/repository/RevertResult;", "reason", "", "<init>", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Failure extends com.webdavrenamer.data.repository.RevertResult {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String reason = null;

        /**
         * 未执行任何回滚（前置失败）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.repository.RevertResult.Failure copy(@org.jetbrains.annotations.NotNull() java.lang.String reason) {
            return null;
        }

        /**
         * 未执行任何回滚（前置失败）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 未执行任何回滚（前置失败）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 未执行任何回滚（前置失败）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public Failure(@org.jetbrains.annotations.NotNull() java.lang.String reason) {
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getReason() {
            return null;
        }
    }
}
