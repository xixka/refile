package com.webdavrenamer.data;

import com.webdavrenamer.core.backup.HostsSpeedTest;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast",
    "deprecation"
})
public final class HostsModule_ProvideHostsSpeedTestFactory implements Factory<HostsSpeedTest> {
  @Override
  public HostsSpeedTest get() {
    return provideHostsSpeedTest();
  }

  public static HostsModule_ProvideHostsSpeedTestFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static HostsSpeedTest provideHostsSpeedTest() {
    return Preconditions.checkNotNullFromProvides(HostsModule.INSTANCE.provideHostsSpeedTest());
  }

  private static final class InstanceHolder {
    private static final HostsModule_ProvideHostsSpeedTestFactory INSTANCE = new HostsModule_ProvideHostsSpeedTestFactory();
  }
}
