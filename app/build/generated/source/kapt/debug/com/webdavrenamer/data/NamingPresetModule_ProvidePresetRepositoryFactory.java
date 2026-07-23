package com.webdavrenamer.data;

import com.webdavrenamer.core.naming.PresetRepository;
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
public final class NamingPresetModule_ProvidePresetRepositoryFactory implements Factory<PresetRepository> {
  @Override
  public PresetRepository get() {
    return providePresetRepository();
  }

  public static NamingPresetModule_ProvidePresetRepositoryFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static PresetRepository providePresetRepository() {
    return Preconditions.checkNotNullFromProvides(NamingPresetModule.INSTANCE.providePresetRepository());
  }

  private static final class InstanceHolder {
    private static final NamingPresetModule_ProvidePresetRepositoryFactory INSTANCE = new NamingPresetModule_ProvidePresetRepositoryFactory();
  }
}
