package com.webdavrenamer.worker;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class RenameWorker_AssistedFactory_Impl implements RenameWorker_AssistedFactory {
  private final RenameWorker_Factory delegateFactory;

  RenameWorker_AssistedFactory_Impl(RenameWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public RenameWorker create(Context arg0, WorkerParameters arg1) {
    return delegateFactory.get(arg0, arg1);
  }

  public static Provider<RenameWorker_AssistedFactory> create(
      RenameWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RenameWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<RenameWorker_AssistedFactory> createFactoryProvider(
      RenameWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RenameWorker_AssistedFactory_Impl(delegateFactory));
  }
}
