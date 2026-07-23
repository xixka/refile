package com.webdavrenamer.data.backup;

/**
 * 自定义模板快照。
 */
@kotlin.Metadata(k = 1, mv = {2, 0, 0}, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u000E\n\u0002\u0008\u0003\n\u0002\u0010\u000B\n\u0002\u0008\n\n\u0002\u0010\u0008\n\u0002\u0008\u0004\u0008\u0087\u0008\u0012\u0001\u0000\u0018\u0000 \u0015:\u0002\u0014\u0015B\u001F\u0012\u0004\u0010\u0002(\u0001\u0012\u0004\u0010\u0004(\u0001\u0012\u0004\u0010\u0005(\u0001\u0012\u0004\u0010\u0006(\u0002\u00A2\u0006\u0004\u0008\u0008\u0010\tJ\u0007\u0010\n8\u0001H\u00C6\u0003J\u0007\u0010\u000B8\u0001H\u00C6\u0003J\u0007\u0010\u000C8\u0001H\u00C6\u0003J\u0007\u0010\r8\u0002H\u00C6\u0003J'\u0010\u000E2\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0006\u0008\u0002\u0010\u0005(\u00012\u0006\u0008\u0002\u0010\u0006(\u00028\u0003H\u00C6\u0001J\r\u0010\u000F2\u0004\u0010\u0010(\u00048\u0002H\u00D6\u0003J\u0007\u0010\u00118\u0005H\u00D6\u0001J\u0007\u0010\u00138\u0001H\u00D6\u0001R\t\u0010\u0002H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0004H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0005H\u0001\u00A2\u0006\u0002\n\u0000R\t\u0010\u0006H\u0002\u00A2\u0006\u0002\n\u0000\u00F2\u0001\u001A\n\u00020\u0001\n\u00020\u0003\n\u00020\u0007\n\u00020\u0000\n\u0004\u0018\u00010\u0001\n\u00020\u0012\u00A8\u0006\u0016"}, d2 = {"Lcom/webdavrenamer/data/backup/TemplateSnapshot;", "", "id", "", "name", "templateString", "isCustom", "", "<init>", "(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Z)V", "component1", "component2", "component3", "component4", "copy", "equals", "other", "hashCode", "", "toString", "$serializer", "Companion", "app_debug"}, xs= "", pn = "", xi = 48)
@kotlinx.serialization.Serializable()
public final class TemplateSnapshot {
    @org.jetbrains.annotations.NotNull()
    private final java.lang.String id = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String name = null;

    @org.jetbrains.annotations.NotNull()
    private final java.lang.String templateString = null;

    private final boolean isCustom = false;

    /**
     * 自定义模板快照。
     */
    @org.jetbrains.annotations.NotNull()
    public final com.webdavrenamer.data.backup.TemplateSnapshot copy(@org.jetbrains.annotations.NotNull() java.lang.String id, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String templateString, boolean isCustom) {
        return null;
    }

    /**
     * 自定义模板快照。
     */
    public boolean equals(@org.jetbrains.annotations.Nullable() java.lang.Object other) {
        return false;
    }

    /**
     * 自定义模板快照。
     */
    public int hashCode() {
        return 0;
    }

    /**
     * 自定义模板快照。
     */
    @org.jetbrains.annotations.NotNull()
    public java.lang.String toString() {
        return null;
    }

    public TemplateSnapshot(@org.jetbrains.annotations.NotNull() java.lang.String id, @org.jetbrains.annotations.NotNull() java.lang.String name, @org.jetbrains.annotations.NotNull() java.lang.String templateString, boolean isCustom) {
        super();
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String component1() {
        return null;
    }

    @org.jetbrains.annotations.NotNull()
    public final java.lang.String getId() {
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
    public final java.lang.String getTemplateString() {
        return null;
    }

    public final boolean component4() {
        return false;
    }

    public final boolean isCustom() {
        return false;
    }

    public static final class Companion {

        private Companion() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.KSerializer<com.webdavrenamer.data.backup.TemplateSnapshot> serializer() {
            return null;
        }
    }
    @kotlin.Deprecated(message = "This synthesized declaration should not be used directly", level = kotlin.DeprecationLevel.HIDDEN)
    @java.lang.Deprecated
    public static final class $serializer implements kotlinx.serialization.internal.GeneratedSerializer<com.webdavrenamer.data.backup.TemplateSnapshot> {
        @org.jetbrains.annotations.NotNull()
        @java.lang.Deprecated
        public static final com.webdavrenamer.data.backup.TemplateSnapshot.$serializer INSTANCE = null;

        @org.jetbrains.annotations.NotNull()
        private static final kotlinx.serialization.descriptors.SerialDescriptor descriptor = null;

        private $serializer() {
            super();
        }

        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public final kotlinx.serialization.KSerializer<?>[] childSerializers() {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        @java.lang.Override()
        public final com.webdavrenamer.data.backup.TemplateSnapshot deserialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Decoder decoder) {
            return null;
        }

        @org.jetbrains.annotations.NotNull()
        public final kotlinx.serialization.descriptors.SerialDescriptor getDescriptor() {
            return null;
        }

        @java.lang.Override()
        public final void serialize(@org.jetbrains.annotations.NotNull() kotlinx.serialization.encoding.Encoder encoder, @org.jetbrains.annotations.NotNull() com.webdavrenamer.data.backup.TemplateSnapshot value) {
        }
    }
}
