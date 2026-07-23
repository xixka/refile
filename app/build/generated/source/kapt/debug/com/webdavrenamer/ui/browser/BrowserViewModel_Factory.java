package com.webdavrenamer.ui.browser;

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
public final class BrowserViewModel_Factory implements Factory<BrowserViewModel> {
  private final Provider<ServerRepository> serverRepoProvider;

  private final Provider<KeystoreCrypto> cryptoProvider;

  public BrowserViewModel_Factory(Provider<ServerRepository> serverRepoProvider,
      Provider<KeystoreCrypto> cryptoProvider) {
    this.serverRepoProvider = serverRepoProvider;
    this.cryptoProvider = cryptoProvider;
  }

  @Override
  public BrowserViewModel get() {
    return newInstance(serverRepoProvider.get(), cryptoProvider.get());
  }

  public static BrowserViewModel_Factory create(Provider<ServerRepository> serverRepoProvider,
      Provider<KeystoreCrypto> cryptoProvider) {
    return new BrowserViewModel_Factory(serverRepoProvider, cryptoProvider);
  }

  public static BrowserViewModel newInstance(ServerRepository serverRepo, KeystoreCrypto crypto) {
    return new BrowserViewModel(serverRepo, crypto);
  }
}
