package com.example.restaurant_reviews;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private EditText editPrenom, editNom, editNumTel, editEmail;
    private Button bouton1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editPrenom = findViewById(R.id.prenom);
        editNom = findViewById(R.id.nom);
        editNumTel = findViewById(R.id.numTel);
        editEmail = findViewById(R.id.email);
        bouton1 = findViewById(R.id.bouton1);

        bouton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Retrieve values from EditText fields
                String prenom = editPrenom.getText().toString();
                String nom = editNom.getText().toString();
                String numTel = editNumTel.getText().toString();
                String email = editEmail.getText().toString();

                // Check if any field is empty
                if (prenom.isEmpty() || nom.isEmpty() || numTel.isEmpty() || email.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Veuillez remplir tous les champs.", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, EvaluationActivity.class);
                    intent.putExtra("prenom", prenom);
                    intent.putExtra("nom", nom);
                    startActivity(intent);
                }
            }
        });
    }
}