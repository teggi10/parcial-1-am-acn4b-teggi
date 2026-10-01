package ar.edu.davinci.fogon;

import androidx.annotation.PluralsRes;

public class Ingredient {
    private final double baseQuantity;
    private final int nameRes;
    private boolean checked;

    public Ingredient(double baseQuantity, @PluralsRes int nameRes) {
        this.baseQuantity = baseQuantity;
        this.nameRes = nameRes;
    }

    public double getQuantityFor(int portions, int basePortions) {
        return baseQuantity * portions / basePortions;
    }

    public boolean isToTaste() {
        return baseQuantity == 0;
    }

    public int getNameRes() {
        return nameRes;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }
}
