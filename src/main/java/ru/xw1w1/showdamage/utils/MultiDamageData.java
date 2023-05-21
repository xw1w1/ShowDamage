package ru.xw1w1.showdamage.utils;

public class MultiDamageData {
    private int count = 0;
    private boolean damagedBySword = false;
    private DamageData damageDealt;
    private int size = 0;

    public MultiDamageData(boolean damageBySword, DamageData damageDealt) {
        this.damagedBySword = damageBySword;
        this.damageDealt = damageDealt;

    }
    public MultiDamageData(boolean damagedBySword) {
        assert !damagedBySword : "Damaged by sword set to true but no damage value provided.";
        this.damagedBySword = false;
    }

    public boolean isDamagedBySword() {
        return damagedBySword;
    }
    public DamageData getDamageDealt() {
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
