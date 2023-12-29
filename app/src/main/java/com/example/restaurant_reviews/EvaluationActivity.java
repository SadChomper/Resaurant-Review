package com.example.restaurant_reviews;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EvaluationActivity extends AppCompatActivity {

    private Spinner spinnerRestaurants;
    private EditText editTextAddress, editTextPrice;
    private RadioButton radioQualiteExcellent, radioQualiteBien, radioQualiteMoyen, radioQualiteMediocre;
    private RadioButton radioPlatExcellent, radioPlatBien, radioPlatMoyen, radioPlatMediocre;
    private RatingBar ratingBarExperience;
    private Button BoutonValidee, decreaseButton, increaseButton;
    private String prenom, nom;
    private RestaurantDatabaseHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);

        dbHelper = new RestaurantDatabaseHelper(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            prenom = extras.getString("prenom");
            nom = extras.getString("nom");
        }

        TextView textGreeting = findViewById(R.id.textGreeting);
        textGreeting.setText("Salut " + prenom + " " + nom);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPrice = findViewById(R.id.editTextPrice);
        spinnerRestaurants = findViewById(R.id.spinnerRestaurants);
        radioQualiteExcellent = findViewById(R.id.radioQualiteExcellent);
        radioQualiteBien = findViewById(R.id.radioQualiteBien);
        radioQualiteMoyen = findViewById(R.id.radioQualiteMoyen);
        radioQualiteMediocre = findViewById(R.id.radioQualiteMediocre);

        radioPlatExcellent = findViewById(R.id.radioPlatExcellent);
        radioPlatBien = findViewById(R.id.radioPlatBien);
        radioPlatMoyen = findViewById(R.id.radioPlatMoyen);
        radioPlatMediocre = findViewById(R.id.radioPlatMediocre);

        ratingBarExperience = findViewById(R.id.ratingBarExperience);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.restaurant_names, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRestaurants.setAdapter(adapter);

        BoutonValidee = findViewById(R.id.buttonValidate);
        decreaseButton = findViewById(R.id.decreaseButton);
        increaseButton = findViewById(R.id.increaseButton);

        BoutonValidee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEvaluationToDatabase();
            }
        });

        decreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreasePrice();
            }
        });

        increaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increasePrice();
            }
        });
    }

    private void decreasePrice() {
        String currentPriceString = editTextPrice.getText().toString();
        if (!currentPriceString.isEmpty()) {
            double currentPrice = Double.parseDouble(currentPriceString);
            if (currentPrice > 1) {
                currentPrice -= 1;
                editTextPrice.setText(String.valueOf(currentPrice));
            }
        }
    }

    private void increasePrice() {
        String currentPriceString = editTextPrice.getText().toString();
        double currentPrice;
        if (!currentPriceString.isEmpty()) {
            currentPrice = Double.parseDouble(currentPriceString);
        } else {
            currentPrice = 0;
        }
        currentPrice += 1;
        editTextPrice.setText(String.valueOf(currentPrice));
    }

    private void addEvaluationToDatabase() {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String selectedRestaurant = spinnerRestaurants.getSelectedItem().toString();
        String qualite = getSelectedRadioButtonValue(radioQualiteExcellent, radioQualiteBien, radioQualiteMoyen, radioQualiteMediocre);
        String plat = getSelectedRadioButtonValue(radioPlatExcellent, radioPlatBien, radioPlatMoyen, radioPlatMediocre);
        String address = editTextAddress.getText().toString();
        String price = editTextPrice.getText().toString();
        float experienceRating = ratingBarExperience.getRating();

        if (selectedRestaurant.isEmpty() || qualite.isEmpty() || plat.isEmpty() || address.isEmpty() || experienceRating == 0 || price.isEmpty()) {
            showToast("La revue a échoué ! Assurez-vous de sélectionner des valeurs pour chaque critère.");
        } else {
            ContentValues values = new ContentValues();
            values.put("nomRestaurant", selectedRestaurant);
            values.put("adresseRestaurant", address);
            values.put("qualitePlats", plat);
            values.put("qualiteService", qualite);
            values.put("experience", experienceRating);
            values.put("prix", price);

            long newRowId = db.insert("Restaurants", null, values);

            db.close();

            if (newRowId != -1) {
                showToast("Votre revue a été effectuée avec succès!");
                navigateToListeRestaurantActivity();
            } else {
                showToast("La revue a échoué!");
            }
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
        } else {
            return "";
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void navigateToListeRestaurantActivity() {
        Intent intent = new Intent(this, ListeRestaurantActivity.class);
        startActivity(intent);
        finish();
    }
}