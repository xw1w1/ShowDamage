package ru.xw1w1.showdamage.utils;

public class DamageData {
    private int count = 0;
    private boolean damagedBySword = false;
    private String damageDealt;
    private int size = 0;

    public DamageData(boolean damageBySword, String damageDealt) {
        this.damagedBySword = damageBySword;
        this.damageDealt = damageDealt;

    }
    public DamageData(boolean damagedBySword) {
        assert !damagedBySword : "Damaged by sword set to true but no damage value provided.";
        this.damagedBySword = false;
    }
    public boolean isDamagedBySword() {
        return damagedBySword;
    }
    public String getDamageDealt() {
        return damageDealt;
    }
    public void append() {
        size++;
        count++;
    }
    public void reduce() {
        size--;
    }
    public int count() {
        return count;
    }

    public int size() {
        return size;
    }
}
