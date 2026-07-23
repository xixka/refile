package com.webdavrenamer.ui.match;

import android.content.Context;
import com.webdavrenamer.data.prefs.SettingsRepository;
import com.webdavrenamer.data.repository.TmdbCacheRepository;
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
public final class MatchViewModel_Factory implements Factory<MatchViewModel> {
  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<TmdbCacheRepository> tmdbCacheProvider;

  private final Provider<Context> contextProvider;

  public MatchViewModel_Factory(Provider<SettingsRepository> settingsProvider,
      Provider<TmdbCacheRepository> tmdbCacheProvider, Provider<Context> contextProvider) {
    this.settingsProvider = settingsProvider;
    this.tmdbCacheProvider = tmdbCacheProvider;
    this.contextProvider = contextProvider;
  }

  @Override
  public MatchViewModel get() {
    return newInstance(settingsProvider.get(), tmdbCacheProvider.get(), contextProvider.get());
  }

  public static MatchViewModel_Factory create(Provider<SettingsRepository> settingsProvider,
      Provider<TmdbCacheRepository> tmdbCacheProvider, Provider<Context> contextProvider) {
    return new MatchViewModel_Factory(settingsProvider, tmdbCacheProvider, contextProvider);
  }

  public static MatchViewModel newInstance(SettingsRepository settings,
      TmdbCacheRepository tmdbCache, Context context) {
    return new MatchViewModel(settings, tmdbCache, context);
  }
}
