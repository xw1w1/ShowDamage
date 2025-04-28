package org.ttlzmc.showdamage

import org.bukkit.scheduler.BukkitRunnable
import org.ttlzmc.showdamage.api.DamageDisplay

class DisplayRemoveRunnable(val display: DamageDisplay): BukkitRunnable() {
    override fun run() {
        try {
            display.remove()
        }catch(e: Exception) {
            ShowDamage.getInstance().slF4JLogger.info("Error while removing $display: ", e)
        }
    }
}