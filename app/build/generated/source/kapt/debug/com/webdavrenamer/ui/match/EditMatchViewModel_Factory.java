package com.webdavrenamer.ui.match;

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
public final class EditMatchViewModel_Factory implements Factory<EditMatchViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  public EditMatchViewModel_Factory(Provider<SettingsRepository> settingsProvider) {
    this.settingsProvider = settingsProvider;
  }

  @Override
  public EditMatchViewModel get() {
    return newInstance(settingsProvider.get());
  }

  public static EditMatchViewModel_Factory create(Provider<SettingsRepository> settingsProvider) {
    return new EditMatchViewModel_Factory(settingsProvider);
  }

  public static EditMatchViewModel newInstance(SettingsRepository settings) {
    return new EditMatchViewModel(settings);
  }
}
