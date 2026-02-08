package com.example.yourcars.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourcars.R;
import com.example.yourcars.ui.admin.FragmentManagmentActivity;
import com.example.yourcars.helper.DBHelper;
import com.example.yourcars.ui.admin.TestDriveRequestFragment;
import com.example.yourcars.ui.user.DashboardActivity;
import com.example.yourcars.databinding.ActivityLoginBinding;
import com.example.yourcars.ui.user.UserTestDriveBookingActivity;

public class LoginActivity extends AppCompatActivity {
    ActivityLoginBinding binding;

    DBHelper dbhelper;


    public  static final String PREFS_NAME= "YourCarsAppPrefs";

    public  static final String LOGIN_KEY= "isLoggedIn";

    public  static final String USER_EMAIL_KEY= "LoggedInEmail";

    //Fix Admin Credentials//
    String ADMIN_USERNAME="admin";
    String ADMIN_PASSWORD="admin";



    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences prefs = getSharedPreferences(PREFS_NAME,MODE_PRIVATE);
        boolean isLoggedIn = prefs.getBoolean(LOGIN_KEY,false);
        if (isLoggedIn){
            //skip login ,and ,go to main activity
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
            finish();
            return;
        }

        //LayoutInflater in Android is a fundamental class responsible for converting XML layout
        // files into their corresponding View objects in memory.

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        dbhelper = new DBHelper(this);

        binding.loginbutton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                String email = binding.loginEmail.getText().toString().trim();
                String password = binding.loginPassword.getText().toString().trim();
                if (email.equals("") || password.equals(""))
                {
                    Toast.makeText(LoginActivity.this, "All fields are mendatory", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Boolean checkcredentials = dbhelper.checkEmailPassword(email,password);
                    if (checkcredentials)
                    {
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.putBoolean(LOGIN_KEY,true);
                        editor.putString(USER_EMAIL_KEY,email);
                        editor.apply();

                        Toast.makeText(LoginActivity.this,"Login Sucessfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }else if (email.equals(ADMIN_USERNAME) && password.equals(ADMIN_PASSWORD)){
                        Toast.makeText(LoginActivity.this,"Login Sucessfully!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), FragmentManagmentActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Invalid Credentials", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        binding.registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        binding.loginPassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Index for drawableEnd

            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.loginPassword.getRight()
                        - binding.loginPassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    // Toggle password visibility
                    if (binding.loginPassword.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT
                            | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {

                        // Show password
                        binding.loginPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                                | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.loginPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    } else {
                        // Hide password
                        binding.loginPassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                                | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.loginPassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    }

                    // Move cursor to end
                    binding.loginPassword.setSelection(binding.loginPassword.length());

                    return true;
                }
            }
            return false;
        });

    }
}