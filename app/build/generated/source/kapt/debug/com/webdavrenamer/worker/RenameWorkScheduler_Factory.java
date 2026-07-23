package com.webdavrenamer.worker;

import android.content.Context;
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
public final class RenameWorkScheduler_Factory implements Factory<RenameWorkScheduler> {
  private final Provider<Context> contextProvider;

  public RenameWorkScheduler_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public RenameWorkScheduler get() {
    return newInstance(contextProvider.get());
  }

  public static RenameWorkScheduler_Factory create(Provider<Context> contextProvider) {
    return new RenameWorkScheduler_Factory(contextProvider);
  }

  public static RenameWorkScheduler newInstance(Context context) {
    return new RenameWorkScheduler(context);
  }
}
