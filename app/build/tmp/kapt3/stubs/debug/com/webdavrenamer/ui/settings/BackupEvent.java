package com.webdavrenamer.ui.settings;

/**
 * 一次性 SAF 事件。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0008v\u0012\u0001\u0000\u0018\u0000:\u0002\u0002\u0003\u0082\u0001\u0002\u0004\u0005\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0006"}, d2 = {"Lcom/webdavrenamer/ui/settings/BackupEvent;", "", "PickExportFile", "PickImportFile", "Lcom/webdavrenamer/ui/settings/BackupEvent$PickExportFile;", "Lcom/webdavrenamer/ui/settings/BackupEvent$PickImportFile;", "app_debug"}, xs= "", pn = "", xi = 48)
public abstract interface BackupEvent {
    /**
     * 触发 SAF CreateDocument 选择导出保存位置。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u00C6\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0004"}, d2 = {"Lcom/webdavrenamer/ui/settings/BackupEvent$PickExportFile;", "Lcom/webdavrenamer/ui/settings/BackupEvent;", "<init>", "()V", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class PickExportFile implements com.webdavrenamer.ui.settings.BackupEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.webdavrenamer.ui.settings.BackupEvent.PickExportFile INSTANCE = null;

        private PickExportFile() {
            super();
        }
    }
    /**
     * 触发 SAF OpenDocument 选择导入备份文件。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u00C6\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0004"}, d2 = {"Lcom/webdavrenamer/ui/settings/BackupEvent$PickImportFile;", "Lcom/webdavrenamer/ui/settings/BackupEvent;", "<init>", "()V", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class PickImportFile implements com.webdavrenamer.ui.settings.BackupEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.webdavrenamer.ui.settings.BackupEvent.PickImportFile INSTANCE = null;

        private PickImportFile() {
            super();
        }
    }
}
