package ru.xw1w1.lookwhatyoudone.config

import net.kyori.adventure.text.format.TextColor

class Colours(configuration: Configuration) {
    @get:JvmName("maceFirst")
    val firstMaceGradientColour = TextColor.fromHexString(configuration.get().getString("colours.mace.first")!!)!!
    @get:JvmName("maceSecond")
    val secondMaceGradientColour = TextColor.fromHexString(configuration.get().getString("colours.mace.second")!!)!!

    @get:JvmName("maceBracketLeft")
    val leftMaceBracket: String = configuration.get().getString("colours.mace.brackets.left")!!
    @get:JvmName("maceBracketRight")
    val rightMaceBracket: String = configuration.get().getString("colours.mace.brackets.right")!!

    @get:JvmName("defaultFirst")
    val firstDefaultGradientColour = TextColor.fromHexString(configuration.get().getString("colours.default.first")!!)!!
    @get:JvmName("defaultSecond")
    val secondDefaultGradientColour = TextColor.fromHexString(configuration.get().getString("colours.default.second")!!)!!

    @get:JvmName("critFirst")
    val firstCritGradientColour = TextColor.fromHexString(configuration.get().getString("colours.crit.first")!!)!!
    @get:JvmName("critSecond")
    val secondCritGradientColour = TextColor.fromHexString(configuration.get().getString("colours.crit.second")!!)!!
    @get:JvmName("critSign")
    val critSign = configuration.get().getString("colours.crit.sign")!!

    @get:JvmName("accentFirst")
    val firstAccentColour = TextColor.fromHexString(configuration.get().getString("colours.accent.first")!!)!!
    @get:JvmName("accentSecond")
    val secondAccentColour = TextColor.fromHexString(configuration.get().getString("colours.accent.second")!!)!!
    @get:JvmName("accentThird")
    val thirdAccentColour = TextColor.fromHexString(configuration.get().getString("colours.accent.third")!!)!!
}