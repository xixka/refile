package com.webdavrenamer.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.webdavrenamer.data.crypto.KeystoreCrypto;
import com.webdavrenamer.data.repository.HistoryRepository;
import com.webdavrenamer.data.repository.ServerRepository;
import dagger.internal.DaggerGenerated;
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
public final class RenameWorker_Factory {
  private final Provider<ServerRepository> serverRepoProvider;

  private final Provider<KeystoreCrypto> cryptoProvider;

  private final Provider<HistoryRepository> historyRepoProvider;

  public RenameWorker_Factory(Provider<ServerRepository> serverRepoProvider,
      Provider<KeystoreCrypto> cryptoProvider, Provider<HistoryRepository> historyRepoProvider) {
    this.serverRepoProvider = serverRepoProvider;
    this.cryptoProvider = cryptoProvider;
    this.historyRepoProvider = historyRepoProvider;
  }

  public RenameWorker get(Context appContext, WorkerParameters params) {
    return newInstance(appContext, params, serverRepoProvider.get(), cryptoProvider.get(), historyRepoProvider.get());
  }

  public static RenameWorker_Factory create(Provider<ServerRepository> serverRepoProvider,
      Provider<KeystoreCrypto> cryptoProvider, Provider<HistoryRepository> historyRepoProvider) {
    return new RenameWorker_Factory(serverRepoProvider, cryptoProvider, historyRepoProvider);
  }

  public static RenameWorker newInstance(Context appContext, WorkerParameters params,
      ServerRepository serverRepo, KeystoreCrypto crypto, HistoryRepository historyRepo) {
    return new RenameWorker(appContext, params, serverRepo, crypto, historyRepo);
  }
}
