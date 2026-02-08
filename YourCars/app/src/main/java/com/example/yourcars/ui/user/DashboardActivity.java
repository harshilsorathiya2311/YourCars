package com.example.yourcars.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.yourcars.R;
import com.example.yourcars.auth.LoginActivity;
import com.example.yourcars.helper.DBHelper;

public class DashboardActivity extends AppCompatActivity {

    CardView tvTestdrive;
    ImageView logout;
    TextView tvcarModelsCount;
    LinearLayout lvbmw, lvcreata, lvtoyota, lvaudi, lvhonda, lvverna;
    DBHelper dbhelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        dbhelper = new DBHelper(this);

        tvTestdrive = findViewById(R.id.cdTestdrive);
        logout = findViewById(R.id.logout);
        lvbmw = findViewById(R.id.lvBMW);
        lvcreata = findViewById(R.id.lvcreata);
        lvtoyota = findViewById(R.id.lvToyota);
        lvaudi = findViewById(R.id.lvAudi);
        lvhonda = findViewById(R.id.lvHonda);
        lvverna = findViewById(R.id.lvverna);
        tvcarModelsCount = findViewById(R.id.carModelsCount);

        tvcarModelsCount.setText(String.valueOf(dbhelper.getcarModelsCount()));


        // Click listeners for car layouts
        tvTestdrive.setOnClickListener(view ->
                startActivity(new Intent(DashboardActivity.this, UserTestDriveBookingActivity.class))
        );

        lvbmw.setOnClickListener(v -> openCarBrand("BMW"));
        lvcreata.setOnClickListener(v -> openCarBrand("CRETA"));
        lvtoyota.setOnClickListener(v -> openCarBrand("TOYOTA"));
        lvaudi.setOnClickListener(v -> openCarBrand("AUDI"));
        lvhonda.setOnClickListener(v -> openCarBrand("HONDA"));
        lvverna.setOnClickListener(v -> openCarBrand("VERNA"));


        // Logout Button
        logout.setOnClickListener(view -> showLogoutConfirmationDialog());
    }

    private void openCarBrand(String brand) {
        Intent intent = new Intent(DashboardActivity.this, ManageCarByBrandActivity.class);
        intent.putExtra("brand", brand);
        startActivity(intent);
    }


    private void performLogout() {
        // Clear session data if needed
        getSharedPreferences("YourCarsAppPrefs", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Redirect to LoginActivity
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

        // Finish current activity
        finish();
    }

    private void showLogoutConfirmationDialog() {
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> performLogout())
                .setNegativeButton("Cancel", null)
                .show();
    }
}
