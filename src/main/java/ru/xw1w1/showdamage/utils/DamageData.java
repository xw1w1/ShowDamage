package ru.xw1w1.showdamage.utils;


import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;

public class DamageData {
    private final int HEART_VALUE = 2;
    private final long rawDamage;
    private @NotNull String formattedDamage;
    public DamageData(double rawDamage, boolean hearts) {
        this.rawDamage = (long) rawDamage;
        if (hearts) {
            formattedDamage = convertDecimalToHearts();
        }else {
            formattedDamage = (new DecimalFormat("0.00")).format(rawDamage);
        }

    }
    public DamageData preAppend(String string) {
        formattedDamage = string + formattedDamage;
        return this;
    }
    private String convertDecimalToHearts() {
        int smallHearts = Math.floorMod(rawDamage, HEART_VALUE);
        int bigHearts = (int) (rawDamage-smallHearts)/2;
        return "❤"
                .repeat(Math.max(0, bigHearts))
                .concat(
                    "♥".repeat(Math.max(0, smallHearts))
                );

    }
    public String valueOf() {
        return formattedDamage;
    }

    /*
    Add hearts option
        - Present amount of damage dealt and health remaining in heart form
        - Use TEXTDISPLAY's Scale function to make text bigger
    Add alternate visible to all option where only PVP is affected and PVE is still visible to other players.
    Provide a perk option so server owners can monetize this feature
    Animation for when TextDisplays get updated in MC 1.20
    Write projectile damage in ACTION BAR
    Write health left in ACTION BAR?


     */
}
