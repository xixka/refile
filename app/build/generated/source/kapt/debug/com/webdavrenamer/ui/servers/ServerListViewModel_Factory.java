package com.webdavrenamer.ui.servers;

import com.webdavrenamer.data.repository.ServerRepository;
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
public final class ServerListViewModel_Factory implements Factory<ServerListViewModel> {
  private final Provider<ServerRepository> repoProvider;

  public ServerListViewModel_Factory(Provider<ServerRepository> repoProvider) {
    this.repoProvider = repoProvider;
  }

  @Override
  public ServerListViewModel get() {
    return newInstance(repoProvider.get());
  }

  public static ServerListViewModel_Factory create(Provider<ServerRepository> repoProvider) {
    return new ServerListViewModel_Factory(repoProvider);
  }

  public static ServerListViewModel newInstance(ServerRepository repo) {
    return new ServerListViewModel(repo);
  }
}
