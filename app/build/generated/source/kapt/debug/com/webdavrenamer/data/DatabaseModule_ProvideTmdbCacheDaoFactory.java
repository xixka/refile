package com.webdavrenamer.data;

import com.webdavrenamer.data.db.AppDatabase;
import com.webdavrenamer.data.db.TmdbCacheDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideTmdbCacheDaoFactory implements Factory<TmdbCacheDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideTmdbCacheDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public TmdbCacheDao get() {
    return provideTmdbCacheDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideTmdbCacheDaoFactory create(Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideTmdbCacheDaoFactory(dbProvider);
  }

  public static TmdbCacheDao provideTmdbCacheDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideTmdbCacheDao(db));
  }
}
