package org.ttlzmc.showdamage

import org.ttlzmc.showdamage.api.DamageDisplay
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.concurrent.LinkedBlockingQueue

class DamageDataCallback(val damageDisplay: DamageDisplay, val queue: LinkedBlockingQueue<DamageDataCallback>): Runnable{
    private val timeout = ShowDamageConfiguration.getDouble("visibility.popup-lifetime")
    private val timeToExit = Instant.now().plus(timeout.toLong(), ChronoUnit.SECONDS)
    override fun run() {
        if (Instant.now().isBefore(timeToExit)) {
            queue.put(this)
        }else {
            damageDisplay.remove();
        }
    }

}