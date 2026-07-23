package com.webdavrenamer.ui.settings;

import com.webdavrenamer.core.naming.PresetRepository;
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
public final class TemplateEditorViewModel_Factory implements Factory<TemplateEditorViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<PresetRepository> presetsProvider;

  public TemplateEditorViewModel_Factory(Provider<SettingsRepository> settingsProvider,
      Provider<PresetRepository> presetsProvider) {
    this.settingsProvider = settingsProvider;
    this.presetsProvider = presetsProvider;
  }

  @Override
  public TemplateEditorViewModel get() {
    return newInstance(settingsProvider.get(), presetsProvider.get());
  }

  public static TemplateEditorViewModel_Factory create(
      Provider<SettingsRepository> settingsProvider, Provider<PresetRepository> presetsProvider) {
    return new TemplateEditorViewModel_Factory(settingsProvider, presetsProvider);
  }

  public static TemplateEditorViewModel newInstance(SettingsRepository settings,
      PresetRepository presets) {
    return new TemplateEditorViewModel(settings, presets);
  }
}
