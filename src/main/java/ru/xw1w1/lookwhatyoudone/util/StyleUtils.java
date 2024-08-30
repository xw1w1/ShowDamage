package ru.xw1w1.lookwhatyoudone.util;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.JoinConfiguration;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Objects;

public class StyleUtils {

    @Contract("_ -> new")
    public static @NotNull Component single(@NotNull Component... components) {
        return Component.join(JoinConfiguration.noSeparators(), components);
    }

    @Contract("_ -> new")
    public static @NotNull Component spaced(@NotNull Component... components) {
        return Component.join(JoinConfiguration.spaces(), components);
    }

    @Contract("_ -> new")
    public static @NotNull Component component(@NotNull Object object) {
        return object instanceof Component c ? c : Component.text(String.valueOf(object));
    }

    @Contract("_ -> new")
    public static @NotNull Component @NotNull [] components(@Nullable Object @NotNull ... objects) {
        return Arrays.stream(objects).filter(Objects::nonNull).map(StyleUtils::component).toArray(Component[]::new);
    }

    @Contract("_ -> new")
    public static @NotNull Component newlined(@NotNull Object... objects) {
        return Component.join(JoinConfiguration.newlines(), components(objects));
    }

    public static @NotNull Component newline() {
        return Component.newline();
    }

    public static @NotNull Component empty() {
        return Component.empty();
    }

    @Contract("_ -> new")
    public static @NotNull String toString(@NotNull Object... objects) {
        return PlainTextComponentSerializer.plainText().serialize(single(components(objects)));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Component gradient(@NotNull String firstColor, @NotNull String secondColor, Object @NotNull ... objects) {
        return MiniMessage.miniMessage().deserialize("<gradient:#" + firstColor + ":#" + secondColor + "><content></gradient>",
                Placeholder.component("content", single(components(objects))));
    }

    @Contract("_, _, _ -> new")
    public static @NotNull Component gradient(@NotNull TextColor first, @NotNull TextColor second, Object @NotNull... objects) {
        return MiniMessage.miniMessage().deserialize("<gradient:" + first.asHexString() + ":" + second.asHexString() + ">" +
                "<content></gradient>", Placeholder.component("content", single(components(objects))));
    }

    @Contract("_, _ -> new")
    public static @NotNull Component hex(@NotNull String color, @Nullable Object @NotNull ... objects) {
        return single(components(objects)).colorIfAbsent(TextColor.fromHexString(color));
    }

    @Contract("_, _ -> new")
    public static @NotNull Component hex(@NotNull TextColor color, @Nullable Object @NotNull... objects) {
        return single(components(objects)).colorIfAbsent(color);
    }

    @Contract("_ -> new")
    public static @NotNull Component bold(@Nullable Object... objects) {
        return single(components(objects)).decorate(TextDecoration.BOLD);
    }

    @Contract("_ -> new")
    public static @NotNull Component reset(@Nullable Object... objects) {
        return single(components(objects)).decoration(TextDecoration.ITALIC, false).decoration(TextDecoration.BOLD, false).decoration(TextDecoration.UNDERLINED, false)
                .decoration(TextDecoration.STRIKETHROUGH, false).decoration(TextDecoration.OBFUSCATED, false);
    }

    @Contract("_ -> new")
    public static @NotNull Component italic(@Nullable Object... objects) {
        return single(components(objects)).decorate(TextDecoration.ITALIC);
    }

    @Contract("_ -> new")
    public static @NotNull Component strikethrough(@Nullable Object... objects) {
        return single(components(objects)).decorate(TextDecoration.STRIKETHROUGH);
    }

    @Contract("_ -> new")
    public static @NotNull Component underlined(@Nullable Object... objects) {
        return single(components(objects)).decorate(TextDecoration.UNDERLINED);
    }

    @Contract("_ -> new")
    public static @NotNull Component obfuscated(@Nullable Object... objects) {
        return single(components(objects)).decorate(TextDecoration.OBFUSCATED);
    }

    @Contract("_, _ -> new")
    public static @NotNull Component hover(@NotNull Component component, @Nullable Object... objects) {
        return component.hoverEvent(HoverEvent.showText(single(components(objects))));
    }

    @Contract("_, _ -> new")
    public static @NotNull Component openUrl(@NotNull Component component, @NotNull String link) {
        return component.clickEvent(ClickEvent.openUrl(link));
    }

    @Contract("_, _ -> new")
    public static @NotNull Component suggestCommand(@NotNull Component component, @NotNull String command) {
        return component.clickEvent(ClickEvent.suggestCommand(command));
    }

    @Contract("_, _ -> new")
    public static @NotNull Component runCommand(@NotNull Component component, @NotNull String command) {
        return component.clickEvent(ClickEvent.runCommand(command));
    }

    @Contract("_ -> new")
    public static @NotNull Component red(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.RED);
    }

    @Contract("_ -> new")
    public static @NotNull Component darkRed(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.DARK_RED);
    }

    @Contract("_ -> new")
    public static @NotNull Component gold(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.GOLD);
    }

    @Contract("_ -> new")
    public static @NotNull Component yellow(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.YELLOW);
    }

    @Contract("_ -> new")
    public static @NotNull Component green(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.GREEN);
    }

    @Contract("_ -> new")
    public static @NotNull Component darkGreen(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.DARK_GREEN);
    }

    @Contract("_ -> new")
    public static @NotNull Component aqua(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.AQUA);
    }

    @Contract("_ -> new")
    public static @NotNull Component darkAqua(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.DARK_AQUA);
    }

    @Contract("_ -> new")
    public static @NotNull Component blue(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.BLUE);
    }

    @Contract("_ -> new")
    public static @NotNull Component darkBlue(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.DARK_BLUE);
    }

    @Contract("_ -> new")
    public static @NotNull Component lightPurple(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.LIGHT_PURPLE);
    }

    @Contract("_ -> new")
    public static @NotNull Component darkPurple(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.DARK_PURPLE);
    }

    @Contract("_ -> new")
    public static @NotNull Component gray(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.GRAY);
    }

    @Contract("_ -> new")
    public static @NotNull Component darkGray(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.DARK_GRAY);
    }

    @Contract("_ -> new")
    public static @NotNull Component black(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.BLACK);
    }

    @Contract("_ -> new")
    public static @NotNull Component white(@Nullable Object... objects) {
        return single(components(objects)).colorIfAbsent(NamedTextColor.WHITE);
    }

    @Contract("_ -> new")
    public static @NotNull Component text(@Nullable Object... objects) {
        return single(components(objects));
    }

}
