package com.webdavrenamer.data;

import com.webdavrenamer.data.crypto.KeystoreCrypto;
import com.webdavrenamer.data.db.ServerConfigDao;
import com.webdavrenamer.data.prefs.SettingsRepository;
import com.webdavrenamer.data.repository.ServerRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class RepositoryModule_ProvideServerRepositoryFactory implements Factory<ServerRepository> {
  private final Provider<ServerConfigDao> daoProvider;

  private final Provider<KeystoreCrypto> cryptoProvider;

  private final Provider<SettingsRepository> settingsProvider;

  public RepositoryModule_ProvideServerRepositoryFactory(Provider<ServerConfigDao> daoProvider,
      Provider<KeystoreCrypto> cryptoProvider, Provider<SettingsRepository> settingsProvider) {
    this.daoProvider = daoProvider;
    this.cryptoProvider = cryptoProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public ServerRepository get() {
    return provideServerRepository(daoProvider.get(), cryptoProvider.get(), settingsProvider.get());
  }

  public static RepositoryModule_ProvideServerRepositoryFactory create(
      Provider<ServerConfigDao> daoProvider, Provider<KeystoreCrypto> cryptoProvider,
      Provider<SettingsRepository> settingsProvider) {
    return new RepositoryModule_ProvideServerRepositoryFactory(daoProvider, cryptoProvider, settingsProvider);
  }

  public static ServerRepository provideServerRepository(ServerConfigDao dao, KeystoreCrypto crypto,
      SettingsRepository settings) {
    return Preconditions.checkNotNullFromProvides(RepositoryModule.INSTANCE.provideServerRepository(dao, crypto, settings));
  }
}
