package ru.xw1w1.lookwhatyoudone.api.renderer;

import net.kyori.adventure.text.Component;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.lookwhatyoudone.data.DamageData;

public interface ShowDamageRenderer {

    void render(final @NotNull DamageData data);

    @NotNull Component dataToComponent(@NotNull DamageData data);

    @NotNull Component renderProjectileChatMessage(@NotNull DamageData data);

}
