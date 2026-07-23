package com.webdavrenamer.ui.servers;

import com.webdavrenamer.data.crypto.KeystoreCrypto;
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
public final class ServerEditViewModel_Factory implements Factory<ServerEditViewModel> {
  private final Provider<ServerRepository> repoProvider;

  private final Provider<KeystoreCrypto> cryptoProvider;

  public ServerEditViewModel_Factory(Provider<ServerRepository> repoProvider,
      Provider<KeystoreCrypto> cryptoProvider) {
    this.repoProvider = repoProvider;
    this.cryptoProvider = cryptoProvider;
  }

  @Override
  public ServerEditViewModel get() {
    return newInstance(repoProvider.get(), cryptoProvider.get());
  }

  public static ServerEditViewModel_Factory create(Provider<ServerRepository> repoProvider,
      Provider<KeystoreCrypto> cryptoProvider) {
    return new ServerEditViewModel_Factory(repoProvider, cryptoProvider);
  }

  public static ServerEditViewModel newInstance(ServerRepository repo, KeystoreCrypto crypto) {
    return new ServerEditViewModel(repo, crypto);
  }
}
