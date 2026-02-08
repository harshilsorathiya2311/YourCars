package com.example.yourcars.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;
import com.example.yourcars.model.CarModel;

import java.text.NumberFormat;
import java.util.Locale;

public class CarDetailsActivity extends AppCompatActivity {

    ImageView imgCar;
    TextView tvCarTitle, tvCarBrand, tvCarDate, tvCarDescription, tvCarPriceDetail;
    Button btnTestDrive;
    DBHelper dbHelper;

    private String formatIndianPrice(long price) {
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "IN"));
        return nf.format(price);
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_car_detail);

        dbHelper = new DBHelper(this);

        imgCar = findViewById(R.id.imgCarDetail);
        tvCarTitle = findViewById(R.id.tvCarTitleDetail);
        tvCarBrand = findViewById(R.id.tvCarBrandDetail);
        tvCarDate = findViewById(R.id.tvCarDateDetail);
        tvCarDescription = findViewById(R.id.tvCarDescriptionDetail);
        tvCarPriceDetail = findViewById(R.id.tvCarPriceDetail);
        btnTestDrive = findViewById(R.id.btnTestDrive);

        int carId = getIntent().getIntExtra("id", -1);
        if (carId == -1) {
            Toast.makeText(this, "Car not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        CarModel car = dbHelper.getCarById(carId);

        if (car != null) {

            tvCarTitle.setText(car.getTitle());
            tvCarBrand.setText(car.getBrandValue());
            tvCarDate.setText(car.getDate());
            tvCarDescription.setText(car.getDescription());

            // FORMAT PRICE (MAIN FIX)
            long priceValue = Long.parseLong(car.getPrice());
            String formattedPrice = formatIndianPrice(priceValue);
            tvCarPriceDetail.setText("₹" + formattedPrice);

            byte[] imageData = car.getImage();
            if (imageData != null) {
                Bitmap bmp = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
                imgCar.setImageBitmap(bmp);
            } else {
                imgCar.setImageResource(R.drawable.ic_launcher_foreground);
            }

            btnTestDrive.setOnClickListener(v -> {
                Intent intent = new Intent(CarDetailsActivity.this, UserTestDriveBookingActivity.class);
                intent.putExtra("brand", car.getBrandValue());
                intent.putExtra("title", car.getTitle());
                startActivity(intent);
            });

        } else {
            Toast.makeText(this, "Car details not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHelper != null) dbHelper.close();
    }
}
