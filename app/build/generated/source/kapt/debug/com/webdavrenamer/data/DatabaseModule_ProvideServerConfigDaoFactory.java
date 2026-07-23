package com.webdavrenamer.data;

import com.webdavrenamer.data.db.AppDatabase;
import com.webdavrenamer.data.db.ServerConfigDao;
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
public final class DatabaseModule_ProvideServerConfigDaoFactory implements Factory<ServerConfigDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideServerConfigDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ServerConfigDao get() {
    return provideServerConfigDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideServerConfigDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideServerConfigDaoFactory(dbProvider);
  }

  public static ServerConfigDao provideServerConfigDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideServerConfigDao(db));
  }
}
