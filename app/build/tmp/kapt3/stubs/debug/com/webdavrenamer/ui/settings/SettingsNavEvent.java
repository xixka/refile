package com.webdavrenamer.ui.settings;

/**
 * 设置中心一次性导航事件。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001A\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0008v\u0012\u0001\u0000\u0018\u0000:\u0003\u0002\u0003\u0004\u0082\u0001\u0003\u0005\u0006\u0007\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/ui/settings/SettingsNavEvent;", "", "OpenTemplateEditor", "OpenBackup", "OpenHostsSettings", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent$OpenBackup;", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent$OpenHostsSettings;", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent$OpenTemplateEditor;", "app_debug"}, xs= "", pn = "", xi = 48)
public abstract interface SettingsNavEvent {
    /**
     * 跳转模板编辑器。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u00C6\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0004"}, d2 = {"Lcom/webdavrenamer/ui/settings/SettingsNavEvent$OpenTemplateEditor;", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent;", "<init>", "()V", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class OpenTemplateEditor implements com.webdavrenamer.ui.settings.SettingsNavEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.webdavrenamer.ui.settings.SettingsNavEvent.OpenTemplateEditor INSTANCE = null;

        private OpenTemplateEditor() {
            super();
        }
    }
    /**
     * 跳转备份与恢复。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u00C6\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0004"}, d2 = {"Lcom/webdavrenamer/ui/settings/SettingsNavEvent$OpenBackup;", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent;", "<init>", "()V", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class OpenBackup implements com.webdavrenamer.ui.settings.SettingsNavEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.webdavrenamer.ui.settings.SettingsNavEvent.OpenBackup INSTANCE = null;

        private OpenBackup() {
            super();
        }
    }
    /**
     * 跳转 Hosts 设置。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u00C6\u0002\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0004"}, d2 = {"Lcom/webdavrenamer/ui/settings/SettingsNavEvent$OpenHostsSettings;", "Lcom/webdavrenamer/ui/settings/SettingsNavEvent;", "<init>", "()V", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class OpenHostsSettings implements com.webdavrenamer.ui.settings.SettingsNavEvent {
        @org.jetbrains.annotations.NotNull()
        public static final com.webdavrenamer.ui.settings.SettingsNavEvent.OpenHostsSettings INSTANCE = null;

        private OpenHostsSettings() {
            super();
        }
    }
}
