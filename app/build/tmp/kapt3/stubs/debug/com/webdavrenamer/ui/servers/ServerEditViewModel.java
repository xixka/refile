package com.webdavrenamer.ui.servers;

/**
 * 添加/编辑服务器页 ViewModel（计划 §M1 SubTask 1.4.2）。
 * 
 * 持有表单的可变 UI 状态，负责：
 * - [load]：编辑模式预填（密码字段留空表示"保留原密码"，明文密码绝不回显，红线）。
 * - [testConnection]：用当前输入构造临时 [ServerConfigEntity] 调 [ServerRepository.testConnection]，
 *   把 [ConnectionResult] 映射为可读文案。
 * - [save]：新增调 [ServerRepository.addServer]，编辑调 [ServerRepository.updateServer]；返回 id 或抛异常。
 * 
 * 说明：[ServerRepository.testConnection] 会对 [ServerConfigEntity.encryptedPassword] 解密后使用，
 * 因此为了用"当前输入的明文密码"测试连接，需先经 [KeystoreCrypto] 加密形成临时 entity
 * （加密→仓库内解密为同一明文，round-trip），否则无法对尚未保存的新密码做连通性测试。
 * [KeystoreCrypto] 仅用于此 round-trip，不在 UI 层持久化任何密文。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\\\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0010\t\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0005\u0008\u0007\u0012\u0001\u0000\u0018\u0000:\u0002)*B\u0015\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u00A2\u0006\u0004\u0008\u0006\u0010\u0007J\n\u0010\u00112\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u00152\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u00162\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u00172\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u00182\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u00192\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u001A2\u0004\u0010\u0013(\u00088\u0007J\n\u0010\u001B2\u0004\u0010\u0013(\t8\u0007J\u0012\u0010\u001D2\u0004\u0010\u001E(\n8\u0007H\u0086@\u00A2\u0006\u0002\u0010 J\u0006\u0010!8\u000BH\u0002J\u0004\u0010\"8\u0007J\u000C\u0010#2\u0004\u0010%(\r8\u000CH\u0002J\u000C\u0010'8\u000EH\u0086@\u00A2\u0006\u0002\u0010(R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000F\u0010\u000BH\u0005\u00A2\u0006\u0008\n\u0000\u001A\u0004\u0008\r\u0010\u000ER\u000C\u0010\u000FH\u0006X\u0082\u000E\u00A2\u0006\u0002\n\u0000\u00F2\u0001H\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\n\n\u0006\u0012\u0002\u0018\u00030\t\n\u0006\u0012\u0002\u0018\u00030\u000C\n\u0004\u0018\u00010\u0010\n\u00020\u0012\n\u00020\u0014\n\u00020\u001C\n\u0004\u0018\u00010\u001F\n\u00020\u0010\n\u00020$\n\u00020&\n\u00020\u001F\u00A8\u0006+"}, d2 = {"Lcom/webdavrenamer/ui/servers/ServerEditViewModel;", "Landroidx/lifecycle/ViewModel;", "repo", "Lcom/webdavrenamer/data/repository/ServerRepository;", "crypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "<init>", "(Lcom/webdavrenamer/data/repository/ServerRepository;Lcom/webdavrenamer/data/crypto/KeystoreCrypto;)V", "_uiState", "Lkotlinx/coroutines/flow/MutableStateFlow;", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$UiState;", "uiState", "Lkotlinx/coroutines/flow/StateFlow;", "getUiState", "()Lkotlinx/coroutines/flow/StateFlow;", "loadedEntity", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "updateName", "", "value", "", "updateBaseUrl", "updatePort", "updateRootPath", "updateUsername", "updatePassword", "updateAuthType", "updateHttps", "", "load", "id", "", "(Ljava/lang/Long;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "buildTempEntity", "testConnection", "mapResult", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi;", "result", "Lcom/webdavrenamer/core/webdav/ConnectionResult;", "save", "(Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "TestResultUi", "UiState", "app_debug"}, xs= "", pn = "", xi = 48)
@dagger.hilt.android.lifecycle.HiltViewModel()
public final class ServerEditViewModel extends androidx.lifecycle.ViewModel {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository repo = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.crypto.KeystoreCrypto crypto = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.MutableStateFlow<com.webdavrenamer.ui.servers.ServerEditViewModel.UiState> _uiState = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.servers.ServerEditViewModel.UiState> uiState = null;

    /**
     * 编辑模式预填时缓存的原始实体，用于"密码留空=保留原密文"分支。
     */
    @org.jetbrains.annotations.Nullable()
    private com.webdavrenamer.data.db.ServerConfigEntity loadedEntity = null;

    @javax.inject.Inject()
    public ServerEditViewModel(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository repo, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.crypto.KeystoreCrypto crypto) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final kotlinx.coroutines.flow.StateFlow<com.webdavrenamer.ui.servers.ServerEditViewModel.UiState> getUiState() {
        return null;
    }

    public final void updateName(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updateBaseUrl(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updatePort(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updateRootPath(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updateUsername(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updatePassword(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updateAuthType(@org.jetbrains.annotations.NotNull() java.lang.String value) {
    }

    public final void updateHttps(boolean value) {
    }

    /**
     * 编辑模式预填。id 为 null 或 <=0 视为新增。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object load(@org.jetbrains.annotations.Nullable() java.lang.Long id, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super kotlin.Unit> $completion) {
        return null;
    }

    /**
     * 构造用于"测试连接"的临时实体。密码用当前输入（加密后 round-trip），留空则沿用原密文。
     */
    private final com.webdavrenamer.data.db.ServerConfigEntity buildTempEntity() {
        return null;
    }

    /**
     * 用当前输入测试连接，结果写入 [uiState.testResult]。
     */
    public final void testConnection() {
    }

    private final com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi mapResult(com.webdavrenamer.core.webdav.ConnectionResult result) {
        return null;
    }

    /**
     * 新增或更新。返回 id；校验失败或仓库异常时抛出，由 UI 捕获展示。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object save(@org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super java.lang.Long> $completion) {
        return null;
    }

    /**
     * 测试连接结果的 UI 投影。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u0016\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u0008v\u0012\u0001\u0000\u0018\u0000:\u0002\u0002\u0003\u0082\u0001\u0002\u0004\u0005\u00F2\u0001\u0004\n\u00020\u0001\u00A8\u0006\u0006"}, d2 = {"Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi;", "", "Success", "Error", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi$Error;", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi$Success;", "app_debug"}, xs= "", pn = "", xi = 48)
    public static abstract interface TestResultUi {
        @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\u000F\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0007\u0010\u00068\u0001H\u00C6\u0003J\u000F\u0010\u00072\u0006\u0008\u0002\u0010\u0002(\u00018\u0002H\u00C6\u0001J\r\u0010\u00082\u0004\u0010\n(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000C8\u0005H\u00D6\u0001J\u0007\u0010\u000E8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\t\n\u0004\u0018\u00010\u000B\n\u00020\r\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi$Success;", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi;", "message", "", "<init>", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
        public static final class Success implements com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;

            public Success() {
                super();
            }

            @org.jetbrains.annotations.NotNull()
            public final com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi.Success copy(@org.jetbrains.annotations.NotNull() java.lang.String message) {
                return null;
            }

            public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }

            public Success(@org.jetbrains.annotations.NotNull() java.lang.String message) {
                super();
            }

            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }

            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
        }
        @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000&\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0005\n\u0002\u0010\u000B\n\u0000\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000B\r\u0012\u0004\u0010\u0002(\u0001\u00A2\u0006\u0004\u0008\u0004\u0010\u0005J\u0007\u0010\u00068\u0001H\u00C6\u0003J\u000F\u0010\u00072\u0006\u0008\u0002\u0010\u0002(\u00018\u0002H\u00C6\u0001J\r\u0010\u00082\u0004\u0010\n(\u00048\u0003H\u00D6\u0003J\u0007\u0010\u000C8\u0005H\u00D6\u0001J\u0007\u0010\u000E8\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0000\n\u00020\t\n\u0004\u0018\u00010\u000B\n\u00020\r\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi$Error;", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi;", "message", "", "<init>", "(Ljava/lang/String;)V", "component1", "copy", "equals", "", "other", "", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
        public static final class Error implements com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi {
            @org.jetbrains.annotations.NotNull()
            private final java.lang.String message = null;

            @org.jetbrains.annotations.NotNull()
            public final com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi.Error copy(@org.jetbrains.annotations.NotNull() java.lang.String message) {
                return null;
            }

            public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
                return false;
            }

            public int hashCode() {
                return 0;
            }

            @org.jetbrains.annotations.NotNull()
            public java.lang.String toString() {
                return null;
            }

            public Error(@org.jetbrains.annotations.NotNull() java.lang.String message) {
                super();
            }

            @org.jetbrains.annotations.NotNull()
            public final java.lang.String component1() {
                return null;
            }

            @org.jetbrains.annotations.NotNull()
            public final java.lang.String getMessage() {
                return null;
            }
        }
    }
    /**
     * 表单 UI 状态。
     */
    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0007\n\u0002\u0010\u000B\n\u0002\u0008\u0004\n\u0002\u0018\u0002\n\u0002\u0008\u0014\n\u0002\u0010\u0008\n\u0002\u0008\u0002\u0008\u0086\u0008\u0012\u0001\u0000\u0018\u0000Bo\u0012\u0006\u0008\u0002\u0010\u0002(\u0001\u0012\u0006\u0008\u0002\u0010\u0004(\u0002\u0012\u0006\u0008\u0002\u0010\u0006(\u0002\u0012\u0006\u0008\u0002\u0010\u0007(\u0002\u0012\u0006\u0008\u0002\u0010\u0008(\u0002\u0012\u0006\u0008\u0002\u0010\t(\u0002\u0012\u0006\u0008\u0002\u0010\n(\u0002\u0012\u0006\u0008\u0002\u0010\u000B(\u0002\u0012\u0006\u0008\u0002\u0010\u000C(\u0003\u0012\u0006\u0008\u0002\u0010\u000E(\u0003\u0012\u0006\u0008\u0002\u0010\u000F(\u0003\u0012\u0006\u0008\u0002\u0010\u0010(\u0003\u0012\u0006\u0008\u0002\u0010\u0011(\u0004\u00A2\u0006\u0004\u0008\u0013\u0010\u0014J\u0007\u0010\u00168\u0001H\u00C6\u0003J\u0007\u0010\u00178\u0002H\u00C6\u0003J\u0007\u0010\u00188\u0002H\u00C6\u0003J\u0007\u0010\u00198\u0002H\u00C6\u0003J\u0007\u0010\u001A8\u0002H\u00C6\u0003J\u0007\u0010\u001B8\u0002H\u00C6\u0003J\u0007\u0010\u001C8\u0002H\u00C6\u0003J\u0007\u0010\u001D8\u0002H\u00C6\u0003J\u0007\u0010\u001E8\u0003H\u00C6\u0003J\u0007\u0010\u001F8\u0003H\u00C6\u0003J\u0007\u0010 8\u0003H\u00C6\u0003J\u0007\u0010!8\u0003H\u00C6\u0003J\u0007\u0010\"8\u0004H\u00C6\u0003Jo\u0010#2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00022\u0006\u0008\u0002\u0010\u0006(\u00022\u0006\u0008\u0002\u0010\u0007(\u00022\u0006\u0008\u0002\u0010\u0008(\u00022\u0006\u0008\u0002\u0010\t(\u00022\u0006\u0008\u0002\u0010\n(\u00022\u0006\u0008\u0002\u0010\u000B(\u00022\u0006\u0008\u0002\u0010\u000C(\u00032\u0006\u0008\u0002\u0010\u000E(\u00032\u0006\u0008\u0002\u0010\u000F(\u00032\u0006\u0008\u0002\u0010\u0010(\u00032\u0006\u0008\u0002\u0010\u0011(\u00048\u0005H\u00C6\u0001J\r\u0010$2\u0004\u0010%(\u00068\u0003H\u00D6\u0003J\u0007\u0010&8\u0007H\u00D6\u0001J\u0007\u0010(8\u0002H\u00D6\u0001R\u000B\u0010\u0002H\u0001\u00A2\u0006\u0004\n\u0002\u0010\u0015R\t\u0010\u0004H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0007H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u0008H\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\tH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\nH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u000BH\u0002\u00A2\u0006\u0002\n\u0000R\t\u0010\u000CH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u000EH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u000FH\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0010H\u0003\u00A2\u0006\u0002\n\u0000R\t\u0010\u0011H\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001&\n\u00020\u0001\n\u0004\u0018\u00010\u0003\n\u00020\u0005\n\u00020\r\n\u0004\u0018\u00010\u0012\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020'\u00A8\u0006)"}, d2 = {"Lcom/webdavrenamer/ui/servers/ServerEditViewModel$UiState;", "", "id", "", "name", "", "baseUrl", "port", "rootPath", "username", "password", "authType", "https", "", "isEditing", "isTesting", "isSaving", "testResult", "Lcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi;", "<init>", "(Ljava/lang/Long;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;ZZZZLcom/webdavrenamer/ui/servers/ServerEditViewModel$TestResultUi;)V", "Ljava/lang/Long;", "component1", "component2", "component3", "component4", "component5", "component6", "component7", "component8", "component9", "component10", "component11", "component12", "component13", "copy", "equals", "other", "hashCode", "", "toString", "app_debug"}, xs= "", pn = "", xi = 48)
    public static final class UiState {
        @org.jetbrains.annotations.Nullable()
        private final java.lang.Long id = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String name = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String baseUrl = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String port = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String rootPath = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String username = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String password = null;

        @org.jetbrains.annotations.NotNull()
        private final java.lang.String authType = null;

        private final boolean https = false;

        private final boolean isEditing = false;

        private final boolean isTesting = false;

        private final boolean isSaving = false;

        @org.jetbrains.annotations.Nullable()
        private final com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi testResult = null;

        public UiState() {
            super();
        }

        /**
         * 表单 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public final com.webdavrenamer.ui.servers.ServerEditViewModel.UiState copy(@org.jetbrains.annotations.Nullable() java.lang.Long id, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.NotNull() java.lang.String port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.NotNull() java.lang.String username, @org.jetbrains.annotations.NotNull() java.lang.String password, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https, boolean isEditing, boolean isTesting, boolean isSaving, @org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi testResult) {
            return null;
        }

        /**
         * 表单 UI 状态。
         */
        public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
            return false;
        }

        /**
         * 表单 UI 状态。
         */
        public int hashCode() {
            return 0;
        }

        /**
         * 表单 UI 状态。
         */
        @org.jetbrains.annotations.NotNull()
        public java.lang.String toString() {
            return null;
        }

        public UiState(@org.jetbrains.annotations.Nullable() java.lang.Long id, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String baseUrl, @org.jetbrains.annotations.NotNull() java.lang.String port, @org.jetbrains.annotations.NotNull() java.lang.String rootPath, @org.jetbrains.annotations.NotNull() java.lang.String username, @org.jetbrains.annotations.NotNull() java.lang.String password, @org.jetbrains.annotations.NotNull() java.lang.String authType, boolean https, boolean isEditing, boolean isTesting, boolean isSaving, @org.jetbrains.annotations.Nullable() com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi testResult) {
            super();
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long component1() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final java.lang.Long getId() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component2() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getName() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component3() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getBaseUrl() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component4() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getPort() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component5() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getRootPath() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component6() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getUsername() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component7() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getPassword() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String component8() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final java.lang.String getAuthType() {
            return null;
        }

        public final boolean component9() {
            return false;
        }

        public final boolean getHttps() {
            return false;
        }

        public final boolean component10() {
            return false;
        }

        public final boolean isEditing() {
            return false;
        }

        public final boolean component11() {
            return false;
        }

        public final boolean isTesting() {
            return false;
        }

        public final boolean component12() {
            return false;
        }

        public final boolean isSaving() {
            return false;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi component13() {
            return null;
        }

        @org.jetbrains.annotations.Nullable()
        public final com.webdavrenamer.ui.servers.ServerEditViewModel.TestResultUi getTestResult() {
            return null;
        }
    }
}
