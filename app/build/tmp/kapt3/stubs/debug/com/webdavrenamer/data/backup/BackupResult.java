package com.webdavrenamer.data.backup;

/**
 * 导出结果。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u00086\u0012\u0001\u0000\u0018\u0000:\u0002\u0004\u0005B\t\u0008\u0004\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupResult;", "", "<init>", "()V", "Success", "Failure", "Lcom/webdavrenamer/data/backup/BackupResult$Failure;", "Lcom/webdavrenamer/data/backup/BackupResult$Success;", "app_debug"}, xs= "", pn = "", xi = 48)
public abstract class BackupResult {

    private BackupResult() {
        super();
    }

    /**
     * 导出成功，[json] 为最终 JSON 文本（已含可能的密文）。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\r\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0007\u0010\u00068\u0001H\u00C6\u0003J\u000F\u0010\u00072\u0006\u0008\u0002\u0010\u0002(\u00018\u0002H\u00C6\u0001J\r\u0010\u00082\u0004\u0010\n(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000C8\u0005H\u00D6\u0001J\u0007\u0010\u000E8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\t\n\u0004\u0018\u00010\u000B\n\u00020\r\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupResult$Success;", "Lcom/webdavrenamer/data/backup/BackupResult;", "json", "", "<init>", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Success extends com.webdavrenamer.data.backup.BackupResult {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String json = null;

        /**
         * 导出成功，[json] 为最终 JSON 文本（已含可能的密文）。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.BackupResult.Success copy(@org.jetbrains.annotations.NotNull() java.lang.String json) {
            return null;
        }

        /**
         * 导出成功，[json] 为最终 JSON 文本（已含可能的密文）。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 导出成功，[json] 为最终 JSON 文本（已含可能的密文）。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 导出成功，[json] 为最终 JSON 文本（已含可能的密文）。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public Success(@org.jetbrains.annotations.NotNull() java.lang.String json) {
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getJson() {
            return null;
        }
    }
    /**
     * 导出失败，[reason] 为可展示原因。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\r\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0007\u0010\u00068\u0001H\u00C6\u0003J\u000F\u0010\u00072\u0006\u0008\u0002\u0010\u0002(\u00018\u0002H\u00C6\u0001J\r\u0010\u00082\u0004\u0010\n(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000C8\u0005H\u00D6\u0001J\u0007\u0010\u000E8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\t\n\u0004\u0018\u00010\u000B\n\u00020\r\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupResult$Failure;", "Lcom/webdavrenamer/data/backup/BackupResult;", "reason", "", "<init>", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Failure extends com.webdavrenamer.data.backup.BackupResult {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String reason = null;

        /**
         * 导出失败，[reason] 为可展示原因。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.BackupResult.Failure copy(@org.jetbrains.annotations.NotNull() java.lang.String reason) {
            return null;
        }

        /**
         * 导出失败，[reason] 为可展示原因。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 导出失败，[reason] 为可展示原因。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 导出失败，[reason] 为可展示原因。
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
