package com.webdavrenamer.ui.settings;

import android.content.Context;
import com.webdavrenamer.data.backup.BackupRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class BackupViewModel_Factory implements Factory<BackupViewModel> {
  private final Provider<Context> contextProvider;

  private final Provider<BackupRepository> repositoryProvider;

  public BackupViewModel_Factory(Provider<Context> contextProvider,
      Provider<BackupRepository> repositoryProvider) {
    this.contextProvider = contextProvider;
    this.repositoryProvider = repositoryProvider;
  }

  @Override
  public BackupViewModel get() {
    return newInstance(contextProvider.get(), repositoryProvider.get());
  }

  public static BackupViewModel_Factory create(Provider<Context> contextProvider,
      Provider<BackupRepository> repositoryProvider) {
    return new BackupViewModel_Factory(contextProvider, repositoryProvider);
  }

  public static BackupViewModel newInstance(Context context, BackupRepository repository) {
    return new BackupViewModel(context, repository);
  }
}
