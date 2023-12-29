package com.example.restaurant_reviews;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class UpdateActivity extends AppCompatActivity {

    private EditText editTextAddress, editTextPrice;
    private RadioButton radioQualiteExcellent, radioQualiteBien, radioQualiteMoyen, radioQualiteMediocre;
    private RadioButton radioPlatExcellent, radioPlatBien, radioPlatMoyen, radioPlatMediocre;
    private RatingBar ratingBarExperience;
    private Button buttonUpdate;

    private RestaurantDatabaseHelper dbHelper;
    private String restaurantId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        dbHelper = new RestaurantDatabaseHelper(this);
        restaurantId = getIntent().getStringExtra("RESTAURANT_ID");

        editTextPrice = findViewById(R.id.editTextPrice1);
        radioQualiteExcellent = findViewById(R.id.RQualiteExcellent);
        radioQualiteBien = findViewById(R.id.RQualiteBien);
        radioQualiteMoyen = findViewById(R.id.RQualiteMoyen);
        radioQualiteMediocre = findViewById(R.id.RQualiteMediocre);
        radioPlatExcellent = findViewById(R.id.RPlatExcellent);
        radioPlatBien = findViewById(R.id.RPlatBien);
        radioPlatMoyen = findViewById(R.id.RPlatMoyen);
        radioPlatMediocre = findViewById(R.id.RPlatMediocre);
        ratingBarExperience = findViewById(R.id.ratingBarExperience1);
        buttonUpdate = findViewById(R.id.buttonUpdate);

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRestaurantInDatabase();
            }
        });
    }

    private void updateRestaurantInDatabase() {
        String address = editTextAddress.getText().toString();
        String price = editTextPrice.getText().toString();
        float experienceRating = ratingBarExperience.getRating();
        String qualiteService = getSelectedRadioButtonValue(radioQualiteExcellent, radioQualiteBien, radioQualiteMoyen, radioQualiteMediocre);
        String qualitePlats = getSelectedRadioButtonValue(radioPlatExcellent, radioPlatBien, radioPlatMoyen, radioPlatMediocre);

        if (!address.isEmpty() && !price.isEmpty() && experienceRating > 0 && !qualiteService.isEmpty() && !qualitePlats.isEmpty()) {
            boolean isUpdated = dbHelper.updateRestaurant(
                    restaurantId, address, qualiteService, qualitePlats, experienceRating, price
            );
            if (isUpdated) {
                Toast.makeText(this, "Updated successfully!", Toast.LENGTH_SHORT).show();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "Update failed.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Please make sure all fields are filled out correctly.", Toast.LENGTH_SHORT).show();
        }
    }

    private String getSelectedRadioButtonValue(RadioButton excellent, RadioButton bien, RadioButton moyen, RadioButton mediocre) {
        if (excellent.isChecked()) {
            return "Excellent";
        } else if (bien.isChecked()) {
            return "Bien";
        } else if (moyen.isChecked()) {
            return "Moyen";
        } else if (mediocre.isChecked()) {
            return "Médiocre";
        }
        return "";
    }
}