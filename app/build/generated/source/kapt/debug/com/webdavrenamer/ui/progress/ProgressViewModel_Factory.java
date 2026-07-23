package com.webdavrenamer.ui.progress;

import androidx.lifecycle.SavedStateHandle;
import androidx.work.WorkManager;
import com.webdavrenamer.worker.RenameWorkScheduler;
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
public final class ProgressViewModel_Factory implements Factory<ProgressViewModel> {
  private final Provider<RenameWorkScheduler> schedulerProvider;

  private final Provider<WorkManager> workManagerProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  public ProgressViewModel_Factory(Provider<RenameWorkScheduler> schedulerProvider,
      Provider<WorkManager> workManagerProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    this.schedulerProvider = schedulerProvider;
    this.workManagerProvider = workManagerProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
  }

  @Override
  public ProgressViewModel get() {
    return newInstance(schedulerProvider.get(), workManagerProvider.get(), savedStateHandleProvider.get());
  }

  public static ProgressViewModel_Factory create(Provider<RenameWorkScheduler> schedulerProvider,
      Provider<WorkManager> workManagerProvider,
      Provider<SavedStateHandle> savedStateHandleProvider) {
    return new ProgressViewModel_Factory(schedulerProvider, workManagerProvider, savedStateHandleProvider);
  }

  public static ProgressViewModel newInstance(RenameWorkScheduler scheduler,
      WorkManager workManager, SavedStateHandle savedStateHandle) {
    return new ProgressViewModel(scheduler, workManager, savedStateHandle);
  }
}
