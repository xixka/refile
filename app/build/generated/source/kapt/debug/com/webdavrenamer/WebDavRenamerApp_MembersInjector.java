package com.webdavrenamer;

import androidx.hilt.work.HiltWorkerFactory;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class WebDavRenamerApp_MembersInjector implements MembersInjector<WebDavRenamerApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public WebDavRenamerApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<WebDavRenamerApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new WebDavRenamerApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(WebDavRenamerApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.webdavrenamer.WebDavRenamerApp.workerFactory")
  public static void injectWorkerFactory(WebDavRenamerApp instance,
      HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
