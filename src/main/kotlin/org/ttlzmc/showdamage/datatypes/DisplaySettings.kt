package org.ttlzmc.showdamage.datatypes

import org.ttlzmc.showdamage.DamageDisplay
import org.ttlzmc.showdamage.ShowDamageConfiguration
import org.ttlzmc.showdamage.util.JsonConfiguration

/**
 * Wrapper class for values from the configuration at the time of creating a new [DamageDisplay] instance.
 * @see ShowDamageConfiguration
 * @see JsonConfiguration
 *
 * @since 1.4
 * @author Turbotaliz
 */
data class DisplaySettings(
    val isSeeThrough: Boolean,
    val visibleToEveryone: Boolean,
    val visibilityRadius: Double,
    val backgroundColor: Int,
    val popupLifetime: Double,

    val damageType: DamageType,
    val isCritical: Boolean
)
