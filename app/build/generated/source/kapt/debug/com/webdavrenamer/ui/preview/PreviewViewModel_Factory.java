package com.webdavrenamer.ui.preview;

import com.webdavrenamer.core.naming.PresetRepository;
import com.webdavrenamer.data.prefs.SettingsRepository;
import com.webdavrenamer.data.repository.ServerRepository;
import com.webdavrenamer.worker.RenameWorkScheduler;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class PreviewViewModel_Factory implements Factory<PreviewViewModel> {
  private final Provider<ServerRepository> serverRepoProvider;

  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<PresetRepository> presetRepoProvider;

  private final Provider<RenameWorkScheduler> workSchedulerProvider;

  public PreviewViewModel_Factory(Provider<ServerRepository> serverRepoProvider,
      Provider<SettingsRepository> settingsProvider, Provider<PresetRepository> presetRepoProvider,
      Provider<RenameWorkScheduler> workSchedulerProvider) {
    this.serverRepoProvider = serverRepoProvider;
    this.settingsProvider = settingsProvider;
    this.presetRepoProvider = presetRepoProvider;
    this.workSchedulerProvider = workSchedulerProvider;
  }

  @Override
  public PreviewViewModel get() {
    return newInstance(serverRepoProvider.get(), settingsProvider.get(), presetRepoProvider.get(), workSchedulerProvider.get());
  }

  public static PreviewViewModel_Factory create(Provider<ServerRepository> serverRepoProvider,
      Provider<SettingsRepository> settingsProvider, Provider<PresetRepository> presetRepoProvider,
      Provider<RenameWorkScheduler> workSchedulerProvider) {
    return new PreviewViewModel_Factory(serverRepoProvider, settingsProvider, presetRepoProvider, workSchedulerProvider);
  }

  public static PreviewViewModel newInstance(ServerRepository serverRepo,
      SettingsRepository settings, PresetRepository presetRepo, RenameWorkScheduler workScheduler) {
    return new PreviewViewModel(serverRepo, settings, presetRepo, workScheduler);
  }
}
