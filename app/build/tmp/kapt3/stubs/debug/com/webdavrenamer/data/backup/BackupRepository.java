package com.webdavrenamer.data.backup;

/**
 * 备份与恢复仓库（计划 §M5 SubTask 5.2.1–5.2.3）。
 * 
 * 职责：
 * - [export]：收集服务器/设置/模板/Hosts 构造 [BackupFile]；口令非空时整体 AES-GCM 加密。
 * - [import]：解析 + schema 校验 + 版本兼容性校验 + 解密（若加密），返回 [ImportResult.Preview]。
 * - [applyImport]：全量校验通过后落库（servers 全量替换、settings/templates/hosts 覆盖）。
 * 
 * 红线：历史与缓存不纳入备份；服务器密码默认置空，仅口令加密时才包含解密后的明文并整体加密。
 * 
 * 注：servers 落库采用「清空再插入」简化策略（Task 5.2.3 允许），不做跨表事务回滚；
 * 解析/解密/版本校验失败均不触碰现有配置。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u008C\u0001\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000E\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0005\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u0012\n\u0000\n\u0002\u0018\u0002\n\u0002\u0008\t\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0008\u0003\u0008\u0007\u0012\u0001\u0000\u0018\u0000 <:\u0001<B!\u0008\u0007\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0002\u0012\u0004\u0010\u0006(\u0003\u0012\u0004\u0010\u0008(\u0004\u00A2\u0006\u0004\u0008\n\u0010\u000BJ\u0018\u0010\u000E2\u0004\u0010\u0010(\u00072\u0004\u0010\u0012(\u00088\u0006H\u0086@\u00A2\u0006\u0002\u0010\u0014J\u0018\u0010\u00152\u0004\u0010\u0017(\n2\u0004\u0010\u0010(\u00078\tH\u0086@\u00A2\u0006\u0002\u0010\u0018J\u0012\u0010\u00192\u0004\u0010\u001B(\u000C8\u000BH\u0086@\u00A2\u0006\u0002\u0010\u001DJ\u0012\u0010\u001E2\u0004\u0010\u001F(\u00088\u000CH\u0082@\u00A2\u0006\u0002\u0010 J\u000E\u0010!2\u0004\u0010\u001F(\u00088\r@\u000EH\u0002J\u0012\u0010$2\u0004\u0010&(\n2\u0004\u0010'(\n8\u000FH\u0002J\u0012\u0010(2\u0004\u0010\u001B(\u000C8\u0010H\u0082@\u00A2\u0006\u0002\u0010\u001DJ\u0012\u0010*2\u0004\u0010\u0010(\n2\u0004\u0010,(\u00128\u0011H\u0002J\u0012\u0010.2\u0004\u00100(\u00112\u0004\u00101(\u00128\u0013H\u0002J\u0018\u001022\u0004\u00100(\u00112\u0004\u00103(\u00122\u0004\u00104(\u00128\u0012H\u0002J\u000C\u001052\u0004\u00106(\u00128\nH\u0002J\u000C\u001072\u0004\u00108(\n8\u0012H\u0002J\u0008\u0010!8\u0014@\u0015H\u0002J\u0008\u0010;8\u0015@\u0014H\u0002R\u000C\u0010\u0002H\u0001X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0004H\u0002X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0006H\u0003X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u0008H\u0004X\u0082\u0004\u00A2\u0006\u0002\n\u0000R\u000C\u0010\u000CH\u0005X\u0082\u0004\u00A2\u0006\u0002\n\u0000\u00F2\u0001b\n\u00020\u0001\n\u00020\u0003\n\u00020\u0005\n\u00020\u0007\n\u00020\t\n\u00020\r\n\u00020\u000F\n\u0004\u0018\u00010\u0011\n\u00020\u0013\n\u00020\u0016\n\u00020\u0011\n\u00020\u001A\n\u00020\u001C\n\u00020\"\n\u00020#\n\u00020%\n\u00020)\n\u00020+\n\u00020-\n\n\u0012\u0002\u0018\u0012\u0012\u0002\u0018\u00120/\n\u000209\n\u00020:\u00A8\u0006="}, d2 = {"Lcom/webdavrenamer/data/backup/BackupRepository;", "", "serverRepository", "Lcom/webdavrenamer/data/repository/ServerRepository;", "settings", "Lcom/webdavrenamer/data/prefs/SettingsRepository;", "presets", "Lcom/webdavrenamer/core/naming/PresetRepository;", "crypto", "Lcom/webdavrenamer/data/crypto/KeystoreCrypto;", "<init>", "(Lcom/webdavrenamer/data/repository/ServerRepository;Lcom/webdavrenamer/data/prefs/SettingsRepository;Lcom/webdavrenamer/core/naming/PresetRepository;Lcom/webdavrenamer/data/crypto/KeystoreCrypto;)V", "json", "Lkotlinx/serialization/json/Json;", "export", "Lcom/webdavrenamer/data/backup/BackupResult;", "passphrase", "", "includePasswords", "", "(Ljava/lang/String;ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "import", "Lcom/webdavrenamer/data/backup/ImportResult;", "jsonText", "(Ljava/lang/String;Ljava/lang/String;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "applyImport", "Lcom/webdavrenamer/data/backup/ApplyResult;", "payload", "Lcom/webdavrenamer/data/backup/BackupPayload;", "(Lcom/webdavrenamer/data/backup/BackupPayload;Lkotlin/coroutines/Continuation;)Ljava/lang/Object;", "collectPayload", "withPasswords", "(ZLkotlin/coroutines/Continuation;)Ljava/lang/Object;", "toSnapshot", "Lcom/webdavrenamer/data/backup/ServerSnapshot;", "Lcom/webdavrenamer/data/db/ServerConfigEntity;", "buildTemplateSnapshot", "Lcom/webdavrenamer/data/backup/TemplateSnapshot;", "presetId", "templateString", "buildChanges", "Lcom/webdavrenamer/data/backup/ImportChanges;", "deriveKey", "Ljavax/crypto/SecretKey;", "salt", "", "encryptGcm", "Lkotlin/Pair;", "key", "plain", "decryptGcm", "iv", "cipherText", "b64", "bytes", "b64Decode", "s", "Lcom/webdavrenamer/data/backup/VisualOptionsSnapshot;", "Lcom/webdavrenamer/data/prefs/VisualOptions;", "toVisualOptions", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@javax.inject.Singleton()
public final class BackupRepository {
    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.repository.ServerRepository serverRepository = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.prefs.SettingsRepository settings = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.core.naming.PresetRepository presets = null;

    @org.jetbrains.annotations.NotNull()
    private final com.webdavrenamer.data.crypto.KeystoreCrypto crypto = null;

    @org.jetbrains.annotations.NotNull()
    private final kotlinx.serialization.json.Json json = null;

    @org.jetbrains.annotations.NotNull()
    private static final com.webdavrenamer.data.backup.BackupRepository.Companion Companion = null;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String APP_VERSION = "1.0.0";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PRESET_CUSTOM = "CUSTOM";

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256";

    public static final int PBKDF2_ITERATIONS = 100000;

    public static final int KEY_LENGTH_BITS = 256;

    public static final int SALT_SIZE_BYTES = 16;

    @org.jetbrains.annotations.NotNull()
    public static final java.lang.String GCM_TRANSFORMATION = "AES/GCM/NoPadding";

    public static final int GCM_IV_SIZE_BYTES = 12;

    public static final int GCM_TAG_LENGTH_BITS = 128;

    @javax.inject.Inject()
    public BackupRepository(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.repository.ServerRepository serverRepository, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.SettingsRepository settings, @kotlin.Suppress(names = {"unused"}) @org.jetbrains.annotations.NotNull() com.webdavrenamer.core.naming.PresetRepository presets, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.crypto.KeystoreCrypto crypto) {
        super();
    }

    /**
     * 导出当前全部配置为 JSON 文本。
     * 
     * @param passphrase 口令；非空白则用 PBKDF2 派生密钥并整体 AES-GCM 加密。
     * @param includePasswords 是否在口令加密时包含服务器明文密码（需 [passphrase] 非空才有意义）。
     * @return [BackupResult.Success] 含 JSON 文本，或 [BackupResult.Failure]。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object export(@org.jetbrains.annotations.Nullable() java.lang.String passphrase, boolean includePasswords, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.backup.BackupResult> $completion) {
        return null;
    }


    /**
     * 应用已预览的导入载荷：全量覆盖落库。
     * 
     * 简化策略：servers 清空再插入（按 name 全量替换），settings/templates/hosts 直接覆盖。
     * 落库前数据已在 [import] 阶段完成校验；此处的写操作为简单 CRUD，失败返回 [ApplyResult.Failure]。
     */
    @org.jetbrains.annotations.Nullable()
    public final java.lang.Object applyImport(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.BackupPayload payload, @org.jetbrains.annotations.NotNull() kotlin.coroutines.Continuation<? super com.webdavrenamer.data.backup.ApplyResult> $completion) {
        return null;
    }

    /**
     * 收集当前全部配置为 [BackupPayload]。[withPasswords]=true 时填入解密后的明文密码。
     */
    private final java.lang.Object collectPayload(boolean withPasswords, kotlin.coroutines.Continuation<? super com.webdavrenamer.data.backup.BackupPayload> $completion) {
        return null;
    }

    /**
     * 服务器实体 → 快照。[withPasswords]=true 时填入解密后的明文密码，否则置空串。
     */
    private final com.webdavrenamer.data.backup.ServerSnapshot toSnapshot(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.db.ServerConfigEntity $this$toSnapshot, boolean withPasswords) {
        return null;
    }

    /**
     * 根据预设 id 与模板串构造单条模板快照。
     */
    private final com.webdavrenamer.data.backup.TemplateSnapshot buildTemplateSnapshot(java.lang.String presetId, java.lang.String templateString) {
        return null;
    }

    /**
     * 构造变更预览：对比备份载荷与当前本地数据。
     */
    private final java.lang.Object buildChanges(com.webdavrenamer.data.backup.BackupPayload payload, kotlin.coroutines.Continuation<? super com.webdavrenamer.data.backup.ImportChanges> $completion) {
        return null;
    }

    /**
     * PBKDF2 派生 256 位 AES 密钥。
     */
    private final javax.crypto.SecretKey deriveKey(java.lang.String passphrase, byte[] salt) {
        return null;
    }

    /**
     * AES-GCM 加密，返回 (iv, cipherText)。
     */
    private final kotlin.Pair<byte[], byte[]> encryptGcm(javax.crypto.SecretKey key, byte[] plain) {
        return null;
    }

    /**
     * AES-GCM 解密。口令错误会抛 [AEADBadTagException]。
     */
    private final byte[] decryptGcm(javax.crypto.SecretKey key, byte[] iv, byte[] cipherText) {
        return null;
    }

    private final java.lang.String b64(byte[] bytes) {
        return null;
    }

    private final byte[] b64Decode(java.lang.String s) {
        return null;
    }

    private final com.webdavrenamer.data.backup.VisualOptionsSnapshot toSnapshot(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.prefs.VisualOptions $this$toSnapshot) {
        return null;
    }

    private final com.webdavrenamer.data.prefs.VisualOptions toVisualOptions(@org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.VisualOptionsSnapshot $this$toVisualOptions) {
        return null;
    }

    @kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\u001C\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\u0008\u0003\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0010\u0008\n\u0002\u0008\u0006\u0008\u0082\u0003\u0012\u0001\u0000\u0018\u0000B\t\u0008\u0002\u00A2\u0006\u0004\u0008\u0002\u0010\u0003R\u0007\u0010\u0004H\u0001X\u0086TR\u0007\u0010\u0006H\u0001X\u0086TR\u0007\u0010\u0007H\u0001X\u0086TR\u0007\u0010\u0008H\u0002X\u0086TR\u0007\u0010\nH\u0002X\u0086TR\u0007\u0010\u000BH\u0002X\u0086TR\u0007\u0010\u000CH\u0001X\u0086TR\u0007\u0010\rH\u0002X\u0086TR\u0007\u0010\u000EH\u0002X\u0086T\u00F2\u0001\u000C\n\u00020\u0001\n\u00020\u0005\n\u00020\t\u00A8\u0006\u000F"}, d2 = {"Lcom/webdavrenamer/data/backup/BackupRepository$Companion;", "", "<init>", "()V", "APP_VERSION", "", "PRESET_CUSTOM", "PBKDF2_ALGORITHM", "PBKDF2_ITERATIONS", "", "KEY_LENGTH_BITS", "SALT_SIZE_BYTES", "GCM_TRANSFORMATION", "GCM_IV_SIZE_BYTES", "GCM_TAG_LENGTH_BITS", "app_debug"}, xs= "", pn = "", xi = 48)
    private static final class Companion {

        private Companion() {
            super();
        }
    }
}
