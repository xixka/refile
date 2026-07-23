package com.webdavrenamer.data.repository;

import com.webdavrenamer.data.crypto.KeystoreCrypto;
import com.webdavrenamer.data.db.ServerConfigDao;
import com.webdavrenamer.data.prefs.SettingsRepository;
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
public final class ServerRepository_Factory implements Factory<ServerRepository> {
  private final Provider<ServerConfigDao> daoProvider;

  private final Provider<KeystoreCrypto> cryptoProvider;

  private final Provider<SettingsRepository> settingsProvider;

  public ServerRepository_Factory(Provider<ServerConfigDao> daoProvider,
      Provider<KeystoreCrypto> cryptoProvider, Provider<SettingsRepository> settingsProvider) {
    this.daoProvider = daoProvider;
    this.cryptoProvider = cryptoProvider;
    this.settingsProvider = settingsProvider;
  }

  @Override
  public ServerRepository get() {
    return newInstance(daoProvider.get(), cryptoProvider.get(), settingsProvider.get());
  }

  public static ServerRepository_Factory create(Provider<ServerConfigDao> daoProvider,
      Provider<KeystoreCrypto> cryptoProvider, Provider<SettingsRepository> settingsProvider) {
    return new ServerRepository_Factory(daoProvider, cryptoProvider, settingsProvider);
  }

  public static ServerRepository newInstance(ServerConfigDao dao, KeystoreCrypto crypto,
      SettingsRepository settings) {
    return new ServerRepository(dao, crypto, settings);
  }
}
