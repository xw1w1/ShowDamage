package ru.xw1w1.lookwhatyoudone.api;

import net.kyori.adventure.text.Component;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.jetbrains.annotations.NotNull;
import ru.xw1w1.lookwhatyoudone.api.renderer.ShowDamageRenderer;

/**
 * The ShowDamage API.
 *
 * <p>The API allows other plugins on the server to override some data,
 * such as the renderer for a custom implementation, change plugin behavior,
 * and integrate ShowDamage into other plugins and systems.</p>
 *
 * <p>This interface represents the base of the API package. All functions are
 * accessed via this interface.</p>
 *
 * <p>To start using the API, you need to obtain an instance of this interface.
 * These are registered by the ShowDamage plugin with it main class.
 * You can obtain an instance from the {@link ShowDamageProvider}.</p>
 *
 * <p>To override the default plugin API, you can use
 * {@link ShowDamageProvider#register(ShowDamageAPI)} mehod.</p>
 */
public interface ShowDamageAPI {

    /**
     * Gets the {@link LuckPerms}, responsible for managing permissions and some metadata.
     *
     * <p>This manager can be used to retrieve {@link User} metadata by uuid
     * or name, or query all loaded users, then look for required values.</p>
     *
     * @return the LuckPerms API
     */
    @NotNull LuckPerms getLuckPerms();

    /**
     * Gets the {@link ShowDamageRenderer}, responsible for building and rendering
     * required {@link Component}s.
     * @return {@link ru.xw1w1.lookwhatyoudone.ShowDamageRendererImpl} by default
     */
    @NotNull ShowDamageRenderer renderer();

}
