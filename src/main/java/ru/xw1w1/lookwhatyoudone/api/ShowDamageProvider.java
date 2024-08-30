package ru.xw1w1.lookwhatyoudone.api;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

public final class ShowDamageProvider {

    private static ShowDamageAPI instance = null;

    public static @NotNull ShowDamageAPI get() {
        ShowDamageAPI instance = ShowDamageProvider.instance;
        if (instance == null) throw new IllegalStateException("ShowDamageAPI instance not found!");
        return instance;
    }

    @ApiStatus.Internal
    public static void register(@NotNull ShowDamageAPI instance) {
        ShowDamageProvider.instance = instance;
    }

    @ApiStatus.Internal
    public static void unregister() {
        ShowDamageProvider.instance = null;
    }

    private ShowDamageProvider() {
        throw new UnsupportedOperationException(
                this.getClass().getName() + " cannot be instantiated!"
        );
    }

}
