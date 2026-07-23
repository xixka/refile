package com.webdavrenamer.ui.servers;

/**
 * 服务器列表页 ViewModel（计划 §M1 SubTask 1.4.1）。
 * 
 * - 暴露 [servers] 作为按仓库排序的服务器配置列表（WhileSubscribed(5s) 缓存）。
 * - [deleteServer] 在 viewModelScope 中转发到仓库；UI 层负责删除前二次确认。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000.\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\t\n\u0000\u0008\u0007\u0012\u0001\u0000\u0018\u0000B\u000F\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\n\u0010\u000C2\u0004\u0010\u000E(\u00068\u0005R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u0006H\u0004\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\n\u0010\u000B\u00F2\u0001$\n\u00020\u0001\n\u00020\u0003\n\u00020\t\n\u0006\u0012\u0002\u0018\u00020\u0008\n\u0006\u0012\u0002\u0018\u00030\u0007\n\u00020\r\n\u00020\u000F\u00A8\u0006\u0010"}, d2 = {"Lcom/webdavrenamer/ui/servers/ServerListViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/webdavrenamer/data/repository/ServerRepository;", "<init>", "(Lcom/webdavrenamer/data/repository/ServerRepository;)V", "servers", "Lkotlinx/coroutines/flow/StateFlow;", "", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "getServers", "()Lkotlinx/coroutines/flow/StateFlow;", "deleteServer", "", "id", "", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ServerListViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository repo = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.data.db.ServerConfigEntity>> servers = null;

    @javax.inject.Inject()
    public ServerListViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository repo) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<java.util.List<com.webdavrenamer.data.db.ServerConfigEntity>> getServers() {
        return null;
    }

    public final void deleteServer(long id) {
    }
}
