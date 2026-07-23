package com.webdavrenamer.ui.theme;

@kotlin.Metadata(k = 2, mv = {2, 0, 0}, d1 = {"\u0000\u001A\n\u0000\n\u0002\u0010\u0002\n\u0000\n\u0002\u0010\u000B\n\u0002\u0008\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\u001A\u001C\u0010\u00002\u0006\u0008\u0002\u0010\u0002(\u00012\u0006\u0008\u0002\u0010\u0004(\u00012\u0004\u0010\u0005(\u00028\u0000H\u0007\u00F2\u0001\u0015\n\u00020\u0001\n\u00020\u0003\n\u000B\u0012\u0002\u0018\u00000\u0006\u00A2\u0006\u0002\u0008\u0007\u00A8\u0006\u0008"}, d2 = {"WebDavRenamerTheme", "", "darkTheme", "", "dynamicColor", "content", "Lkotlin/Function0;", "Landroidx/compose/runtime/Composable;", "app_debug"}, xs= "", pn = "", xi = 48)
public final class ThemeKt {

    /**
     * App-wide Compose theme.
     * 
     * Dark-only by design: the [darkTheme] parameter is accepted for API symmetry
     * but the app always renders with [DarkColorScheme] regardless of the value.
     * Dynamic color (Material You) is disabled — the cinema palette is fixed.
     * 
     * Status bar / system bars are handled by `enableEdgeToEdge()` in
     * [com.webdavrenamer.MainActivity]; no extra WindowCompat manipulation is
     * needed here.
     */
    @androidx.compose.runtime.Composable()
    public static final void WebDavRenamerTheme(boolean darkTheme, boolean dynamicColor, @org.jetbrains.annotations.NotNull() kotlin.jvm.functions.Function0<kotlin.Unit> content) {
    }
}
