package com.webdavrenamer.data.repository;

import com.webdavrenamer.data.db.TmdbCacheDao;
import com.webdavrenamer.data.prefs.SettingsRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class TmdbCacheRepository_Factory implements Factory<TmdbCacheRepository> {
  private final Provider<TmdbCacheDao> daoProvider;

  private final Provider<SettingsRepository> settingsProvider;

  public TmdbCacheRepository_Factory(Provider<TmdbCacheDao> daoProvider,
      Provider<SettingsRepository> settingsProvider) {
    this.daoProvider = daoProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public TmdbCacheRepository get() {
    return newInstance(daoProvider.get(), settingsProvider.get());
  }

  public static TmdbCacheRepository_Factory create(Provider<TmdbCacheDao> daoProvider,
      Provider<SettingsRepository> settingsProvider) {
    return new TmdbCacheRepository_Factory(daoProvider, settingsProvider);
  }

  public static TmdbCacheRepository newInstance(TmdbCacheDao dao, SettingsRepository settings) {
    return new TmdbCacheRepository(dao, settings);
  }
}
