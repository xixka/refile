package com.webdavrenamer.ui.history;

/**
 * 历史记录页 ViewModel（计划 §M5 SubTask 5.1.3）。
 * 
 * 状态：
 * - [batches]：所有批次（按 createdAt 倒序，[HistoryRepository.observeBatches] 直供）。
 * - [selectedBatch] / [selectedEntries]：当前选中批次的详情。
 * - [reverting]：撤销进行中标志（驱动 UI 显示 CircularProgressIndicator 并禁用按钮）。
 * - [revertResult]：最近一次撤销结果（成功 N/M 或失败原因），由 UI 弹 Snackbar。
 * 
 * [selectBatch] / [revertBatch] / [clearRevertResult] 为 UI 调用入口。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000`\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0002\u0008\u0004\n\u0002\u0010\u000E\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u0007\u0012\u0001\u0000\u0018\u0000:\u0001)B\u000F\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\n\u0010\u001C2\u0004\u0010\u001E(\u00138\u0012J\u0004\u0010 8\u0012J\n\u0010!2\u0004\u0010\u001E(\u00138\u0012J\u0004\u0010\"8\u0012J\n\u0010#2\u0004\u0010%(\u00158\u0014J\n\u0010&2\u0004\u0010((\u00148\u0016R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0006H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\n\u0010\u000BR\u000C\u0010\u000CH\u0006X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000EH\u0007\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u000F\u0010\u000BR\u000C\u0010\u0010H\nX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0012H\u000B\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0013\u0010\u000BR\u000C\u0010\u0014H\rX\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0016H\u000E\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u0017\u0010\u000BR\u000C\u0010\u0018H\u0010X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u001AH\u0011\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\u001B\u0010\u000B\u00F2\u0001\u008C\u0001\n\u00020\u0001\n\u00020\u0003\n\u00020\t\n\u0006\u0012\u0002\u0018\u00020\u0008\n\u0006\u0012\u0002\u0018\u00030\u0007\n\u0004\u0018\u00010\t\n\u0006\u0012\u0002\u0018\u00050\r\n\u0006\u0012\u0002\u0018\u00050\u0007\n\u00020\u0011\n\u0006\u0012\u0002\u0018\u00080\u0008\n\u0006\u0012\u0002\u0018\t0\r\n\u0006\u0012\u0002\u0018\t0\u0007\n\u00020\u0015\n\u0006\u0012\u0002\u0018\u000C0\r\n\u0006\u0012\u0002\u0018\u000C0\u0007\n\u0004\u0018\u00010\u0019\n\u0006\u0012\u0002\u0018\u000F0\r\n\u0006\u0012\u0002\u0018\u000F0\u0007\n\u00020\u001D\n\u00020\u001F\n\u00020$\n\u00020\u0019\n\u00020'\u00A8\u0006*"}, d2 = {"Lcom/webdavrenamer/ui/history/HistoryViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/webdavrenamer/data/repository/HistoryRepository;", "<init>", "(Lcom/webdavrenamer/data/repository/HistoryRepository;)V", "batches", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/webdavrenamer/data/db/RenameBatchEntity;", "getBatches", "()Lkotlinx/coroutines/flow/StateFlow;", "_selectedBatch", "Lkotlinx/coroutines/flow/MutableStateFlow;", "selectedBatch", "getSelectedBatch", "_selectedEntries", "Lcom/webdavrenamer/data/db/RenameEntryEntity;", "selectedEntries", "getSelectedEntries", "_reverting", "", "reverting", "getReverting", "_revertResult", "Lcom/webdavrenamer/data/repository/RevertResult;", "revertResult", "getRevertResult", "selectBatch", "", "id", "", "clearSelection", "revertBatch", "clearRevertResult", "revertResultMessage", "", "result", "entryStatus", "Lcom/webdavrenamer/ui/history/HistoryViewModel$EntryStatus;", "status", "EntryStatus", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class HistoryViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.HistoryRepository repo = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.data.db.RenameBatchEntity>> batches = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.data.db.RenameBatchEntity> _selectedBatch = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.db.RenameBatchEntity> selectedBatch = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.util.List<com.webdavrenamer.data.db.RenameEntryEntity>> _selectedEntries = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.data.db.RenameEntryEntity>> selectedEntries = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<java.lang.Boolean> _reverting = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> reverting = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.data.repository.RevertResult> _revertResult = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.repository.RevertResult> revertResult = null;

    @javax.inject.Inject()
    public HistoryViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.HistoryRepository repo) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.data.db.RenameBatchEntity>> getBatches() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.db.RenameBatchEntity> getSelectedBatch() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.data.db.RenameEntryEntity>> getSelectedEntries() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.lang.Boolean> getReverting() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.data.repository.RevertResult> getRevertResult() {
        return null;
    }

    /**
     * 选中某批次：加载批次详情与条目列表。
     */
    public final void selectBatch(long id) {
    }

    /**
     * 退出详情视图（清空选中态）。
     */
    public final void clearSelection() {
    }

    /**
     * 撤销整批：调用 [HistoryRepository.revertBatch]，过程中置 [reverting]=true，
     * 结果写入 [revertResult]，并刷新当前选中批次（更新 isReverted 标记）。
     */
    public final void revertBatch(long id) {
    }

    /**
     * 清除一次性撤销结果（Snackbar 消费后调用）。
     */
    public final void clearRevertResult() {
    }

    /**
     * 把 [RevertResult] 转成给用户看的简短文案。
     */
    @org.jetbrains.annotations.NotNull()
    public final java.lang.String revertResultMessage(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.RevertResult result) {
        return null;
    }

    /**
     * 把 [RenameEntryEntity.status] 映射为 UI 状态枚举（驱动状态图标着色）。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.ui.history.HistoryViewModel.EntryStatus entryStatus(@org.jetbrains.annotations.NotNull() java.lang.String status) {
        return null;
    }

    /**
     * 条目状态枚举。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u000C\n\u0002\u0018\u0002\n\u0002\u0010\u0010\n\u0002\u0008\u0007\u0008\u0086\u0081\u0002\u0012\u0001\u0001\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003j\u0002\u0008\u0004j\u0002\u0008\u0005j\u0002\u0008\u0006j\u0002\u0008\u0007\u00F2\u0001\u000C\n\u00020\u0000\n\u0006\u0012\u0002\u0018\u00000\u0001\u00A8\u0006\u0008"}, d2 = {"Lcom/webdavrenamer/ui/history/HistoryViewModel$EntryStatus;", "", "<init>", "(Ljava/lang/String;I)V", "SUCCESS", "PARTIAL", "FAILED", "SKIPPED", "app_debug"}, xs= "", pn = "", xi = 48)
    public static enum EntryStatus {
        SUCCESS,
        PARTIAL,
        FAILED,
        SKIPPED;


        EntryStatus() {
        }

        @org.jetbrains.annotations.NotNull()
        public static kotlin.enums.EnumEntries<com.webdavrenamer.ui.history.HistoryViewModel.EntryStatus> getEntries() {
            return null;
        }
    }
}
