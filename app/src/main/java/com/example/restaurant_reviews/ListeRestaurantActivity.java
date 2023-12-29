package com.example.restaurant_reviews;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListeRestaurantActivity extends AppCompatActivity {

    private static RecyclerView recyclerView;
    private RestaurantAdapter adapter;
    private static RestaurantDatabaseHelper dbHelper;
    private static final int REQUEST_CODE_UPDATE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_liste_restaurant);
        dbHelper = new RestaurantDatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewRestaurants);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String[]> restaurantList = dbHelper.getAllRestaurants();
        adapter = new RestaurantAdapter(this, restaurantList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_UPDATE && resultCode == RESULT_OK) {
            List<String[]> restaurantList = dbHelper.getAllRestaurants();
            adapter.setRestaurants(restaurantList);
            adapter.notifyDataSetChanged();
        }
    }

    private static class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.ViewHolder> {
        private final LayoutInflater inflater;
        private List<String[]> restaurants;
        private final Context context;

        RestaurantAdapter(Context context, List<String[]> restaurants) {
            this.inflater = LayoutInflater.from(context);
            this.restaurants = restaurants;
            this.context = context;
        }

        public void setRestaurants(List<String[]> restaurants) {
            this.restaurants = restaurants;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.restaurant_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final String[] restaurant = restaurants.get(position);
            holder.nameTextView.setText(restaurant[1]);
            holder.imageView.setImageResource(getImageResourceId(context, restaurant[1]));
            holder.adresseTextView.setText(restaurant[2]);
            holder.qualitePlatTextView.setText(restaurant[3]);
            holder.qualiteServiceTextView.setText(restaurant[4]);
            holder.experienceTextView.setText(restaurant[5]);
            holder.prixTextView.setText(restaurant[6]);

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean isVisible = holder.detailsLayout.getVisibility() == View.VISIBLE;
                    holder.detailsLayout.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                    holder.deleteButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                    holder.updateButton.setVisibility(isVisible ? View.GONE : View.VISIBLE);
                    // If the details are being made visible, scroll to this position
                    if (!isVisible) {
                        recyclerView.scrollToPosition(position);
                    }

                }
            });

            holder.updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, UpdateActivity.class);
                    intent.putExtra("RESTAURANT_ID", restaurant[0]);
                    ((Activity) context).startActivityForResult(intent, REQUEST_CODE_UPDATE);
                }
            });

            holder.deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(context)
                            .setTitle("Confirm Delete")
                            .setMessage("Are you sure you want to delete this restaurant?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    if (dbHelper.deleteRestaurant(restaurant[0])) {
                                        restaurants.remove(position);
                                        notifyDataSetChanged();
                                    }
                                }
                            })
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }

        private int getImageResourceId(Context context, String imageName) {
            String formattedName = imageName.toLowerCase().replaceAll("\\s+", "").replaceAll("[^a-z0-9]", "");
            int resourceId = context.getResources().getIdentifier(formattedName, "drawable", context.getPackageName());
            return resourceId != 0 ? resourceId : R.drawable.placeholder;
        }

        @Override
        public int getItemCount() {
            return restaurants.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;
            TextView nameTextView, adresseTextView, qualitePlatTextView, qualiteServiceTextView, experienceTextView, prixTextView;
            View detailsLayout, deleteButton, updateButton;

            ViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.restaurantImage);
                nameTextView = itemView.findViewById(R.id.nameTextView);
                adresseTextView = itemView.findViewById(R.id.adresseTextView);
                qualitePlatTextView = itemView.findViewById(R.id.qualitePlatTextView);
                qualiteServiceTextView = itemView.findViewById(R.id.qualiteServiceTextView);
                experienceTextView = itemView.findViewById(R.id.experienceTextView);
                prixTextView = itemView.findViewById(R.id.prixTextView);
                detailsLayout = itemView.findViewById(R.id.detailsLayout);
                deleteButton = itemView.findViewById(R.id.deleteButton);
                updateButton = itemView.findViewById(R.id.updateButton);
            }
        }
    }
}