package com.webdavrenamer.ui.match;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class MatchSessionViewModel_Factory implements Factory<MatchSessionViewModel> {
  @Override
  public MatchSessionViewModel get() {
    return newInstance();
  }

  public static MatchSessionViewModel_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static MatchSessionViewModel newInstance() {
    return new MatchSessionViewModel();
  }

  private static final class InstanceHolder {
    private static final MatchSessionViewModel_Factory INSTANCE = new MatchSessionViewModel_Factory();
  }
}
