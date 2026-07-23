package com.webdavrenamer.data;

import com.webdavrenamer.data.db.AppDatabase;
import com.webdavrenamer.data.db.RenameBatchDao;
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
public final class DatabaseModule_ProvideRenameBatchDaoFactory implements Factory<RenameBatchDao> {
  private final Provider<AppDatabase> dbProvider;

  public DatabaseModule_ProvideRenameBatchDaoFactory(Provider<AppDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RenameBatchDao get() {
    return provideRenameBatchDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRenameBatchDaoFactory create(
      Provider<AppDatabase> dbProvider) {
    return new DatabaseModule_ProvideRenameBatchDaoFactory(dbProvider);
  }

  public static RenameBatchDao provideRenameBatchDao(AppDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRenameBatchDao(db));
  }
}
