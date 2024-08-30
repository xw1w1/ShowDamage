package ru.xw1w1.lookwhatyoudone.config

import com.google.errorprone.annotations.CanIgnoreReturnValue
import net.luckperms.api.model.user.User
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.entity.Player
import ru.xw1w1.lookwhatyoudone.ShowDamage
import ru.xw1w1.lookwhatyoudone.api.ShowDamageProvider

object Configuration {

    const val GLOBAL_NAMESPACE = "showdamage"
    const val VISIBILITY_META_KEY = "showdamage.visibility"

    private lateinit var configuration: FileConfiguration
    private lateinit var colours: Colours

    @JvmStatic
    @CanIgnoreReturnValue
    fun init(): FileConfiguration {
        this.configuration = ShowDamage.getInstance().config
        this.colours = Colours(this)
        return this.configuration
    }

    @JvmStatic
    fun get(): FileConfiguration {
        return this.configuration
    }

    @JvmStatic
    fun colours(): Colours {
        return this.colours
    }

    @JvmStatic
    fun loadDefaults() {
        ShowDamage.getInstance().saveDefaultConfig()
    }

    @JvmStatic
    @JvmName("textVisibleFor")
    fun checkShowDamageVisibilityForPlayer(player: Player): Boolean {
        return ShowDamageProvider.get().luckPerms.userManager
            .getUser(player.uniqueId)?.let(::checkShowDamageVisibilityForUser) ?: false
    }

    @JvmStatic
    fun checkShowDamageVisibilityForUser(user: User): Boolean {
        return user.cachedData.metaData.getMetaValue(VISIBILITY_META_KEY).toBoolean()
    }

    @JvmStatic
    fun chatMessagesEnabled(): Boolean {
        return get().getBoolean("messages.projectile-damage-messages")
    }

    @JvmStatic
    fun hotbarHeartsEnabled(): Boolean {
        return get().getBoolean("messages.projectile-hearts-in-hotbar")
    }

}