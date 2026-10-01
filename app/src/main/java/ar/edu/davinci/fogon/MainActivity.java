package ar.edu.davinci.fogon;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public final int BASE_PORTIONS = 12;

    public int portions = BASE_PORTIONS;
    public List<Ingredient> ingredients = new ArrayList<>();

    private void loadIngredients() {
        ingredients.add(new Ingredient(12, R.plurals.ingredient_dough));
        ingredients.add(new Ingredient(500, R.plurals.ingredient_beef));
        ingredients.add(new Ingredient(2, R.plurals.ingredient_onion));
        ingredients.add(new Ingredient(1, R.plurals.ingredient_pepper));
        ingredients.add(new Ingredient(2, R.plurals.ingredient_potato));
        ingredients.add(new Ingredient(3, R.plurals.ingredient_egg));
        ingredients.add(new Ingredient(3, R.plurals.ingredient_green_onion));
        ingredients.add(new Ingredient(2, R.plurals.ingredient_lard));
        ingredients.add(new Ingredient(1, R.plurals.ingredient_cumin));
        ingredients.add(new Ingredient(1, R.plurals.ingredient_paprika));
        ingredients.add(new Ingredient(0, R.plurals.ingredient_salt));
    }

    private String formatQuantity(double value) {
        if (value >= 10) return String.valueOf(Math.round(value));

        double rounded = Math.max(0.5, Math.round(value * 2) / 2.0);
        int whole = (int) Math.floor(rounded);
        boolean hasHalf = rounded - whole > 0;

        if (!hasHalf) return String.valueOf(whole);
        if (whole == 0) return getString(R.string.symbol_half);
        return getString(R.string.quantity_with_half, whole);
    }

    private String describeIngredient(Ingredient ingredient) {
        if (ingredient.isToTaste()) {
            return getResources().getQuantityString(ingredient.getNameRes(), 2);
        }
        double quantity = ingredient.getQuantityFor(portions, BASE_PORTIONS);
        int pluralCount = quantity <= 1 ? 1 : 2;
        return getResources().getQuantityString(
                ingredient.getNameRes(), pluralCount, formatQuantity(quantity)
        );
    }

    private void renderIngredients() {
        LinearLayout container = findViewById(R.id.ingredientsContainer);
        container.removeAllViews();

        for (Ingredient ingredient : ingredients) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(describeIngredient(ingredient));
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.ink));
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_body));
            checkBox.setMinHeight(getResources().getDimensionPixelSize(R.dimen.row_min_height));
            checkBox.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.herb)));
            checkBox.setChecked(ingredient.isChecked());
            container.addView(checkBox);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loadIngredients();
        renderIngredients();
    }
}
