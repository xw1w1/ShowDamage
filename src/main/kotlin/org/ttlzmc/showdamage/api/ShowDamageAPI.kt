package org.ttlzmc.showdamage.api

import org.jetbrains.annotations.ApiStatus
import java.util.Objects.isNull

interface ShowDamageAPI {

    @ApiStatus.Internal
    fun defaultProvider() = ShowDamageAPIProvider

    companion object {
        private var INSTANCE: ShowDamageAPI? = null

        fun get(): ShowDamageAPI {
            val instance: ShowDamageAPI? = INSTANCE
            if (isNull(INSTANCE)) throw NullPointerException("ShowDamageAPI is not initialized.")
            return instance!!
        }

        @ApiStatus.NonExtendable
        fun register(instance: ShowDamageAPI) {
            if (isNull(INSTANCE)) INSTANCE = instance
            else throw RuntimeException("ShowDamageAPI instance is already set. You are trying to set API instance twice?")
        }
    }
}