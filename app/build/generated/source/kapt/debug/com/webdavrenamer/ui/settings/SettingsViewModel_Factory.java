package com.webdavrenamer.ui.settings;

import android.content.Context;
import com.webdavrenamer.data.prefs.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class SettingsViewModel_Factory implements Factory<SettingsViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<SettingsRepository> settingsProvider;

  public SettingsViewModel_Factory(Provider<Context> contextProvider,
      Provider<SettingsRepository> settingsProvider) {
    this.contextProvider = contextProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public SettingsViewModel get() {
    return newInstance(contextProvider.get(), settingsProvider.get());
  }

  public static SettingsViewModel_Factory create(Provider<Context> contextProvider,
      Provider<SettingsRepository> settingsProvider) {
    return new SettingsViewModel_Factory(contextProvider, settingsProvider);
  }

  public static SettingsViewModel newInstance(Context context, SettingsRepository settings) {
    return new SettingsViewModel(context, settings);
  }
}
