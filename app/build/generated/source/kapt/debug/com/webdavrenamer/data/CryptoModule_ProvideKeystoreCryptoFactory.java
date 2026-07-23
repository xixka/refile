package com.webdavrenamer.data;

import android.content.Context;
import com.webdavrenamer.data.crypto.KeystoreCrypto;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class CryptoModule_ProvideKeystoreCryptoFactory implements Factory<KeystoreCrypto> {
  private final Provider<Context> contextProvider;

  public CryptoModule_ProvideKeystoreCryptoFactory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public KeystoreCrypto get() {
    return provideKeystoreCrypto(contextProvider.get());
  }

  public static CryptoModule_ProvideKeystoreCryptoFactory create(
      Provider<Context> contextProvider) {
    return new CryptoModule_ProvideKeystoreCryptoFactory(contextProvider);
  }

  public static KeystoreCrypto provideKeystoreCrypto(Context context) {
    return Preconditions.checkNotNullFromProvides(CryptoModule.INSTANCE.provideKeystoreCrypto(context));
  }
}
