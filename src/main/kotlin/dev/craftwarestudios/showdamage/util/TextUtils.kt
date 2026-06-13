package dev.craftwarestudios.showdamage.util

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.JoinConfiguration
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.minimessage.MiniMessage
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

/**
 * A class for more efficient and convenient work with [Component]s.
 *
 * l_MrBoom_l, спасибо за то, что привил мне любовь к кодингу своей личностью и в том числе сервером.
 *
 * @author xw1w1
 * @since 2.0
 */
object TextUtils {
    fun single(vararg components: Component): Component = Component.join(JoinConfiguration.noSeparators(), components.toList())

    fun single(config: JoinConfiguration, vararg components: Component): Component = Component.join(config, components.toList())

    fun component(value: Any): Component = when (value) {
        is Component -> value
        else -> Component.text(value.toString())
    }

    fun components(vararg values: Any?): Array<Component> = values.filterNotNull().map(::component).toTypedArray()

    fun newline(): Component = Component.newline()

    fun empty(): Component = Component.empty()

    fun gradient(firstColor: String, secondColor: String, vararg values: Any?): Component {
        return MiniMessage.miniMessage().deserialize(
            "<gradient:#$firstColor:#$secondColor><content></gradient>",
            Placeholder.component("content", single(*components(*values)))
        )
    }

    fun hex(color: String, vararg values: Any?): Component {
        return single(*components(*values)).colorIfAbsent(TextColor.fromHexString("#$color") ?: NamedTextColor.WHITE)
    }

    fun bold(vararg values: Any?): Component = single(*components(*values)).decorate(TextDecoration.BOLD)

    fun destyle(vararg values: Any?): Component = single(*components(*values))
        .decoration(TextDecoration.ITALIC, false)
        .decoration(TextDecoration.BOLD, false)
        .decoration(TextDecoration.UNDERLINED, false)
        .decoration(TextDecoration.STRIKETHROUGH, false)
        .decoration(TextDecoration.OBFUSCATED, false)

    fun italic(vararg values: Any?): Component = single(*components(*values)).decorate(TextDecoration.ITALIC)

    fun strikethrough(vararg values: Any?): Component = single(*components(*values)).decorate(TextDecoration.STRIKETHROUGH)

    fun underlined(vararg values: Any?): Component = single(*components(*values)).decorate(TextDecoration.UNDERLINED)

    fun obfuscated(vararg values: Any?): Component = single(*components(*values)).decorate(TextDecoration.OBFUSCATED)

    fun hover(component: Component, vararg values: Any?): Component = component.hoverEvent(HoverEvent.showText(single(*components(*values))))

    fun openUrl(component: Component, link: String): Component = component.clickEvent(ClickEvent.openUrl(link))

    fun suggestCommand(component: Component, command: String): Component = component.clickEvent(ClickEvent.suggestCommand(command))

    fun runCommand(component: Component, command: String): Component = component.clickEvent(ClickEvent.runCommand(command))

    fun red(vararg values: Any?): Component = single(*components(*values)).colorIfAbsent(NamedTextColor.RED)

    fun green(vararg values: Any?): Component = single(*components(*values)).colorIfAbsent(NamedTextColor.GREEN)

    fun gold(vararg values: Any?): Component = single(*components(*values)).colorIfAbsent(NamedTextColor.GOLD)

    fun gray(vararg values: Any?): Component = single(*components(*values)).colorIfAbsent(NamedTextColor.GRAY)
}
