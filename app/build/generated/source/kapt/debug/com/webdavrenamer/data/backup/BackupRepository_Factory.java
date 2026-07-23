package com.webdavrenamer.data.backup;

import com.webdavrenamer.core.naming.PresetRepository;
import com.webdavrenamer.data.crypto.KeystoreCrypto;
import com.webdavrenamer.data.prefs.SettingsRepository;
import com.webdavrenamer.data.repository.ServerRepository;
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
public final class BackupRepository_Factory implements Factory<BackupRepository> {
  private final Provider<ServerRepository> serverRepositoryProvider;

  private final Provider<SettingsRepository> settingsProvider;

  private final Provider<PresetRepository> presetsProvider;

  private final Provider<KeystoreCrypto> cryptoProvider;

  public BackupRepository_Factory(Provider<ServerRepository> serverRepositoryProvider,
      Provider<SettingsRepository> settingsProvider, Provider<PresetRepository> presetsProvider,
      Provider<KeystoreCrypto> cryptoProvider) {
    this.serverRepositoryProvider = serverRepositoryProvider;
    this.settingsProvider = settingsProvider;
    this.presetsProvider = presetsProvider;
    this.cryptoProvider = cryptoProvider;
  }

  @Override
  public BackupRepository get() {
    return newInstance(serverRepositoryProvider.get(), settingsProvider.get(), presetsProvider.get(), cryptoProvider.get());
  }

  public static BackupRepository_Factory create(Provider<ServerRepository> serverRepositoryProvider,
      Provider<SettingsRepository> settingsProvider, Provider<PresetRepository> presetsProvider,
      Provider<KeystoreCrypto> cryptoProvider) {
    return new BackupRepository_Factory(serverRepositoryProvider, settingsProvider, presetsProvider, cryptoProvider);
  }

  public static BackupRepository newInstance(ServerRepository serverRepository,
      SettingsRepository settings, PresetRepository presets, KeystoreCrypto crypto) {
    return new BackupRepository(serverRepository, settings, presets, crypto);
  }
}
