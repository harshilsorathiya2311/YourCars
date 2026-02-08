package com.example.yourcars.auth;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;
import com.example.yourcars.databinding.ActivityRegisterBinding;

public class RegisterActivity extends AppCompatActivity {
    ActivityRegisterBinding binding;
    DBHelper databaseHelper;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        databaseHelper = new DBHelper(this);

        binding.registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = binding.registeremail.getText().toString().trim();
                String cotact_number = binding.registerContactnum.getText().toString().trim();
                String password = binding.registerpassword.getText().toString().trim();
                String confirmpassword = binding.registerconfirmpassword.getText().toString().trim();

                if (email.equals("")||cotact_number.equals("")||password.equals("")||confirmpassword.equals(""))
                    Toast.makeText(RegisterActivity.this, "All field are mandatory", Toast.LENGTH_SHORT).show();
                else{
                    if (password.equals(confirmpassword)){
                        Boolean CheckUserEmail = databaseHelper.checkEmail(email);
                        if (CheckUserEmail == false){
                            Boolean insert = databaseHelper.insertUserData(email,cotact_number,password);
                            if (insert == true){
                                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                startActivity(intent);
                            }else{
                                Toast.makeText(RegisterActivity.this, "Register failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "User alredy exists! please login", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(RegisterActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        binding.registerRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        binding.registerpassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Index for drawableEnd

            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.registerpassword.getRight()
                        - binding.registerpassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    // Toggle password visibility
                    if (binding.registerpassword.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT
                            | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {

                        // Show password
                        binding.registerpassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                                | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.registerpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    } else {
                        // Hide password
                        binding.registerpassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                                | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.registerpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    }

                    // Move cursor to end
                    binding.registerpassword.setSelection(binding.registerpassword.length());

                    return true;
                }
            }
            return false;
        });

        binding.registerconfirmpassword.setOnTouchListener((v, event) -> {
            final int DRAWABLE_END = 2; // Index for drawableEnd

            if (event.getAction() == android.view.MotionEvent.ACTION_UP) {
                if (event.getRawX() >= (binding.registerconfirmpassword.getRight()
                        - binding.registerconfirmpassword.getCompoundDrawables()[DRAWABLE_END].getBounds().width())) {

                    // Toggle password visibility
                    if (binding.registerconfirmpassword.getInputType() == (android.text.InputType.TYPE_CLASS_TEXT
                            | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD)) {

                        // Show password
                        binding.registerconfirmpassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                                | android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                        binding.registerconfirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility, 0);
                    } else {
                        // Hide password
                        binding.registerconfirmpassword.setInputType(android.text.InputType.TYPE_CLASS_TEXT
                                | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        binding.registerconfirmpassword.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_visibility_off, 0);
                    }

                    // Move cursor to end
                    binding.registerconfirmpassword.setSelection(binding.registerconfirmpassword.length());

                    return true;
                }
            }
            return false;
        });

    }
}