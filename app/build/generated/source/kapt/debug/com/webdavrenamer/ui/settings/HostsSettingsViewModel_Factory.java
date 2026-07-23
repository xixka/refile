package com.webdavrenamer.ui.settings;

import com.webdavrenamer.core.backup.HostsSpeedTest;
import com.webdavrenamer.data.prefs.SettingsRepository;
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
public final class HostsSettingsViewModel_Factory implements Factory<HostsSettingsViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<HostsSpeedTest> speedTestProvider;

  public HostsSettingsViewModel_Factory(Provider<SettingsRepository> settingsProvider,
      Provider<HostsSpeedTest> speedTestProvider) {
    this.settingsProvider = settingsProvider;
    this.speedTestProvider = speedTestProvider;
  }

  @Override
  public HostsSettingsViewModel get() {
    return newInstance(settingsProvider.get(), speedTestProvider.get());
  }

  public static HostsSettingsViewModel_Factory create(Provider<SettingsRepository> settingsProvider,
      Provider<HostsSpeedTest> speedTestProvider) {
    return new HostsSettingsViewModel_Factory(settingsProvider, speedTestProvider);
  }

  public static HostsSettingsViewModel newInstance(SettingsRepository settings,
      HostsSpeedTest speedTest) {
    return new HostsSettingsViewModel(settings, speedTest);
  }
}
