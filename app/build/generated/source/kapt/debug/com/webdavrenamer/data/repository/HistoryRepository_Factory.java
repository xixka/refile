package com.webdavrenamer.data.repository;

import com.webdavrenamer.data.db.RenameBatchDao;
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
public final class HistoryRepository_Factory implements Factory<HistoryRepository> {
  private final Provider<RenameBatchDao> daoProvider;

  private final Provider<ServerRepository> serverRepositoryProvider;

  public HistoryRepository_Factory(Provider<RenameBatchDao> daoProvider,
      Provider<ServerRepository> serverRepositoryProvider) {
    this.daoProvider = daoProvider;
    this.serverRepositoryProvider = serverRepositoryProvider;
  }

  @Override
  public HistoryRepository get() {
    return newInstance(daoProvider.get(), serverRepositoryProvider.get());
  }

  public static HistoryRepository_Factory create(Provider<RenameBatchDao> daoProvider,
      Provider<ServerRepository> serverRepositoryProvider) {
    return new HistoryRepository_Factory(daoProvider, serverRepositoryProvider);
  }

  public static HistoryRepository newInstance(RenameBatchDao dao,
      ServerRepository serverRepository) {
    return new HistoryRepository(dao, serverRepository);
  }
}
