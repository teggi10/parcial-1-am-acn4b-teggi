package ar.edu.davinci.fogon;

import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    public final int MIN_PORTIONS = 1;
    public final int MAX_PORTIONS = 48;

    public int portions = BASE_PORTIONS;
    public boolean inShoppingList = false;
    public boolean isFavorite = false;
    public List<Ingredient> ingredients = new ArrayList<>();

    public void goBack(View view) {
        finish();
    }

    public void toggleFavorite(View view) {
        ImageButton button = (ImageButton) view;
        isFavorite = !isFavorite;

        if (isFavorite) {
            button.setImageResource(R.drawable.ic_favorite);
            button.setContentDescription(getString(R.string.favorite_remove));
            Toast.makeText(this, R.string.favorite_added_toast, Toast.LENGTH_SHORT).show();
        } else {
            button.setImageResource(R.drawable.ic_favorite_border);
            button.setContentDescription(getString(R.string.favorite_add));
            Toast.makeText(this, R.string.favorite_removed_toast, Toast.LENGTH_SHORT).show();
        }
    }

    public void toggleShoppingList(View view) {
        Button button = (Button) view;

        if (inShoppingList) {
            inShoppingList = false;
            button.setText(R.string.shopping_add);
            Toast.makeText(this, R.string.shopping_removed, Toast.LENGTH_SHORT).show();
            return;
        }

        int missing = countMissingIngredients();
        if (missing == 0) {
            Toast.makeText(this, R.string.shopping_nothing_missing, Toast.LENGTH_SHORT).show();
            return;
        }

        inShoppingList = true;
        button.setText(R.string.shopping_added);
        String message = getResources().getQuantityString(R.plurals.shopping_added_toast, missing, missing);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private int countMissingIngredients() {
        int missing = 0;
        for (Ingredient ingredient : ingredients) {
            if (!ingredient.isChecked() && !ingredient.isToTaste()) missing++;
        }
        return missing;
    }

    public void decreasePortions(View view) {
        if (portions <= MIN_PORTIONS) return;
        portions--;
        updatePortions();
    }

    public void increasePortions(View view) {
        if (portions >= MAX_PORTIONS) return;
        portions++;
        updatePortions();
    }

    private void updatePortions() {
        TextView portionsCount = findViewById(R.id.portionsCount);
        portionsCount.setText(String.valueOf(portions));

        findViewById(R.id.buttonLessPortions).setEnabled(portions > MIN_PORTIONS);
        findViewById(R.id.buttonMorePortions).setEnabled(portions < MAX_PORTIONS);

        renderIngredients();
    }

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
            checkBox.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_body));
            checkBox.setMinHeight(getResources().getDimensionPixelSize(R.dimen.row_min_height));
            checkBox.setButtonTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.herb)));
            checkBox.setChecked(ingredient.isChecked());
            applyCheckedStyle(checkBox, ingredient.isChecked());
            checkBox.setOnCheckedChangeListener((button, isChecked) -> {
                ingredient.setChecked(isChecked);
                applyCheckedStyle(checkBox, isChecked);
            });
            container.addView(checkBox);
        }
    }

    private void applyCheckedStyle(CheckBox checkBox, boolean isChecked) {
        int flags = checkBox.getPaintFlags();
        if (isChecked) {
            checkBox.setPaintFlags(flags | Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.muted));
        } else {
            checkBox.setPaintFlags(flags & ~Paint.STRIKE_THRU_TEXT_FLAG);
            checkBox.setTextColor(ContextCompat.getColor(this, R.color.ink));
        }
    }

    private void renderSteps() {
        LinearLayout container = findViewById(R.id.stepsContainer);
        String[] steps = getResources().getStringArray(R.array.recipe_steps);
        int padding = getResources().getDimensionPixelSize(R.dimen.card_padding);
        int numberSize = getResources().getDimensionPixelSize(R.dimen.step_number_size);
        int gap = getResources().getDimensionPixelSize(R.dimen.step_gap);

        for (int i = 0; i < steps.length; i++) {
            LinearLayout row = new LinearLayout(this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(padding, padding, padding, padding);

            TextView number = new TextView(this);
            number.setText(String.valueOf(i + 1));
            number.setGravity(Gravity.CENTER);
            number.setTypeface(Typeface.DEFAULT_BOLD);
            number.setTextColor(ContextCompat.getColor(this, R.color.herb_dark));
            number.setBackgroundResource(R.drawable.bg_step_number);
            row.addView(number, new LinearLayout.LayoutParams(numberSize, numberSize));

            TextView text = new TextView(this);
            text.setText(steps[i]);
            text.setTextColor(ContextCompat.getColor(this, R.color.ink));
            text.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.text_body));
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(
                    0, LinearLayout.LayoutParams.WRAP_CONTENT, 1
            );
            textParams.setMarginStart(gap);
            row.addView(text, textParams);

            container.addView(row);
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
        renderSteps();
    }
}
