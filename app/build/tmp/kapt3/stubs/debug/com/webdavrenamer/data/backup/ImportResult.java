package com.webdavrenamer.data.backup;

/**
 * 导入结果。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u00086\u0012\u0001\u0000\u0018\u0000:\u0002\u0004\u0005B\t\u0008\u0004\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u0082\u0001\u0002\u0006\u0007\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/data/backup/ImportResult;", "", "<init>", "()V", "Preview", "Failure", "Lcom/webdavrenamer/data/backup/ImportResult$Failure;", "Lcom/webdavrenamer/data/backup/ImportResult$Preview;", "app_debug"}, xs= "", pn = "", xi = 48)
public abstract class ImportResult {

    private ImportResult() {
        super();
    }

    /**
     * 解析/解密成功，等待用户确认落库。
     * @param payload 已解密/解析出的数据载荷，落库时直接使用。
     * @param changes 相对当前本地数据的变更预览。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0006\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0000\n\u0002\u0010\u000E\n\u0000\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u0013\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\u0007\u0010\u00088\u0001H\u00C6\u0003J\u0007\u0010\t8\u0002H\u00C6\u0003J\u0017\u0010\n2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00028\u0003H\u00C6\u0001J\r\u0010\u000B2\u0004\u0010\r(\u00058\u0004H\u00D6\u0003J\u0007\u0010\u000F8\u0006H\u00D6\u0001J\u0007\u0010\u00118\u0007H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000\u00F2\u0001\"\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0000\n\u00020\u000C\n\u0004\u0018\u00010\u000E\n\u00020\u0010\n\u00020\u0012\u00A8\u0006\u0013"}, d2 = {"Lcom/webdavrenamer/data/backup/ImportResult$Preview;", "Lcom/webdavrenamer/data/backup/ImportResult;", "payload", "Lcom/webdavrenamer/data/backup/BackupPayload;", "changes", "Lcom/webdavrenamer/data/backup/ImportChanges;", "<init>", "(Lcom/webdavrenamer/data/backup/BackupPayload;Lcom/webdavrenamer/data/backup/ImportChanges;)V", "component1", "component2", "copy", "equals", "", "other", "", "hashCode", "", "toString", "", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Preview extends com.webdavrenamer.data.backup.ImportResult {
        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.data.backup.BackupPayload payload = null;

        @org.jetbrains.annotations.NotNull()
        private final com.webdavrenamer.data.backup.ImportChanges changes = null;

        /**
         * 解析/解密成功，等待用户确认落库。
         * @param payload 已解密/解析出的数据载荷，落库时直接使用。
         * @param changes 相对当前本地数据的变更预览。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.ImportResult.Preview copy(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.BackupPayload payload, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.ImportChanges changes) {
            return null;
        }

        /**
         * 解析/解密成功，等待用户确认落库。
         * @param payload 已解密/解析出的数据载荷，落库时直接使用。
         * @param changes 相对当前本地数据的变更预览。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 解析/解密成功，等待用户确认落库。
         * @param payload 已解密/解析出的数据载荷，落库时直接使用。
         * @param changes 相对当前本地数据的变更预览。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 解析/解密成功，等待用户确认落库。
         * @param payload 已解密/解析出的数据载荷，落库时直接使用。
         * @param changes 相对当前本地数据的变更预览。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public Preview(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.BackupPayload payload, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.ImportChanges changes) {
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.BackupPayload component1() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.BackupPayload getPayload() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.ImportChanges component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.ImportChanges getChanges() {
            return null;
        }
    }
    /**
     * 导入失败，[reason] 为可展示原因，现有配置保持不变。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\r\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0007\u0010\u00068\u0001H\u00C6\u0003J\u000F\u0010\u00072\u0006\u0008\u0002\u0010\u0002(\u00018\u0002H\u00C6\u0001J\r\u0010\u00082\u0004\u0010\n(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000C8\u0005H\u00D6\u0001J\u0007\u0010\u000E8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\t\n\u0004\u0018\u00010\u000B\n\u00020\r\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/data/backup/ImportResult$Failure;", "Lcom/webdavrenamer/data/backup/ImportResult;", "reason", "", "<init>", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class Failure extends com.webdavrenamer.data.backup.ImportResult {
        @org.jetbrains.annotations.NotNull()
        private final java.lang.String reason = null;

        /**
         * 导入失败，[reason] 为可展示原因，现有配置保持不变。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.data.backup.ImportResult.Failure copy(@org.jetbrains.annotations.NotNull() java.lang.String reason) {
            return null;
        }

        /**
         * 导入失败，[reason] 为可展示原因，现有配置保持不变。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 导入失败，[reason] 为可展示原因，现有配置保持不变。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 导入失败，[reason] 为可展示原因，现有配置保持不变。
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
