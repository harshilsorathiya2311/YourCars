package com.example.yourcars.ui.admin;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.yourcars.R;
import com.example.yourcars.auth.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FragmentManagmentActivity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    Fragment homeFragment = new Home_Fragment();
    Fragment addFragment = new AddUpdateCarModelsActivity();
    Fragment usersFragment = new usersFragment();
    Fragment testDrive_request = new TestDriveRequestFragment();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_fragment_managment);

        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Load default fragment (Home)
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, homeFragment)
                    .commit();
        }
// Handle bottom navigation clicks
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            if (item.getItemId() == R.id.nav_home) {
                selectedFragment = homeFragment;
            } else if (item.getItemId() == R.id.nav_add) {
                selectedFragment = addFragment;
            }else if (item.getItemId() == R.id.nav_user) {
                selectedFragment = usersFragment;
            } else if (item.getItemId() == R.id.nav_test_drives_request) {
                selectedFragment = testDrive_request;
            } else if(item.getItemId() == R.id.nav_logout){
                showLogoutConfirmationDialog();
                return true; // No fragment transaction needed
            }
            if (selectedFragment != null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame_container, selectedFragment)
                        .commit();
            }

            return true;
        });

    }

    private void performLogout() {
        // Clear session data (if you're using SharedPreferences)
        getSharedPreferences("isLoggedIn", MODE_PRIVATE)
                .edit()
                .clear()
                .apply();

        // Redirect to LoginActivity (replace with your login screen class)
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