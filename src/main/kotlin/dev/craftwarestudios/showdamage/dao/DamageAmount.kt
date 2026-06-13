package dev.craftwarestudios.showdamage.dao

import java.text.DecimalFormat

/**
 * Represents a single damage value together with it's [mitigated] counterpart.
 *
 * The [dealt] value is used for player-facing display, while the [received] value preserves
 * the original amount of damage for internal calculation and aggregation.
 *
 * @author xw1w1
 * @since 2.0
 */
data class DamageAmount(val dealt: Double, val received: Double) {
    val mitigated: Double = received - dealt

    fun plus(other: DamageAmount): DamageAmount = DamageAmount(dealt + other.dealt, received + other.received)

    fun asDecimalString(): String = DecimalFormat("0.00").format(dealt)

    fun asHeartString(): String {
        val halfHearts = Math.floorMod(dealt.toLong(), 2)
        val fullHearts = (dealt - halfHearts / 2).toInt()

        return buildString {
            this.append("❤x${fullHearts} ")
            if (halfHearts >= 1) append("+ ♥x$halfHearts")
        }
    }

    fun format(hearts: Boolean): String = if (hearts) asHeartString() else asDecimalString()
}