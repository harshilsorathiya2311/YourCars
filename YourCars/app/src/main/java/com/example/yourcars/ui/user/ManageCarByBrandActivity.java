package com.example.yourcars.ui.user;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourcars.Adapter.CarListAdapter;
import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;

public class ManageCarByBrandActivity extends AppCompatActivity {

    TextView tvBrandTitle;
    ListView listCars;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_car_by_brand);

        dbHelper = new DBHelper(this);
        tvBrandTitle = findViewById(R.id.tvBrandTitle);
        listCars = findViewById(R.id.listCars);

        // Get brand name from Intent
        String brand = getIntent().getStringExtra("brand");

        if (brand == null || brand.trim().isEmpty()) {
            Toast.makeText(this, "No brand selected", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        tvBrandTitle.setText(brand + " Cars");

        // Load and display cars for the selected brand
        loadCarsByBrand(brand);

        // Click event for car item
        listCars.setOnItemClickListener((adapterView, view, position, id) -> {
            Intent intent = new Intent(ManageCarByBrandActivity.this, CarDetailsActivity.class);
            intent.putExtra("id", (int) id);
            startActivity(intent);
        });
    }

    private void loadCarsByBrand(String brand) {
        Cursor cursor = dbHelper.getCarsByBrand(brand);

        if (cursor != null && cursor.getCount() > 0) {
            CarListAdapter adapter = new CarListAdapter(this, cursor);
            listCars.setAdapter(adapter);
        } else {
            Toast.makeText(this, "No cars found for " + brand, Toast.LENGTH_SHORT).show();
            tvBrandTitle.setText("No cars found for " + brand);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Close DB when done to prevent leaks
        if (dbHelper != null) dbHelper.close();
    }
}
