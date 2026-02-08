package com.example.yourcars.ui.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;

import java.util.ArrayList;
import java.util.Calendar;

public class UserTestDriveBookingActivity extends AppCompatActivity {

    EditText etName, etEmail, etContact, etDate, etTime;
    Spinner modelSpinner;
    TextView tvSelectedCar;
    Button btnSubmit;
    DBHelper dbHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_test_drive_booking);

        // Initialize views
        etName = findViewById(R.id.et_name);
        etEmail = findViewById(R.id.et_email);
        etContact = findViewById(R.id.et_contact);
        etDate = findViewById(R.id.et_date);
        etTime = findViewById(R.id.et_time);
        modelSpinner = findViewById(R.id.brandSpinner);
        btnSubmit = findViewById(R.id.btn_submit);
        tvSelectedCar = findViewById(R.id.tv_selectedCar);
        dbHelper = new DBHelper(this);

        // Load all car brands into spinner
        ArrayList<String> brands = loadCarBrands();

        // Get data from intent
        String selectedBrand = getIntent().getStringExtra("brand");
        String selectedModel = getIntent().getStringExtra("title");

        // Set spinner adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                brands
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        modelSpinner.setAdapter(adapter);

        // If car is passed from CarDetailsActivity
        if (selectedBrand != null && selectedModel != null) {
            tvSelectedCar.setText("Booking Test Drive for " + selectedBrand + " " + selectedModel);

            // Auto-select the brand
            for (int i = 0; i < brands.size(); i++) {
                if (brands.get(i).equalsIgnoreCase(selectedBrand)) {
                    modelSpinner.setSelection(i);
                    break;
                }
            }

            // Disable spinner (lock brand)
            modelSpinner.setEnabled(false);
        } else {
            tvSelectedCar.setText("Select a Car for Test Drive");
            modelSpinner.setEnabled(true);
        }

        // Date Picker
        etDate.setOnClickListener(v -> showDatePicker());

        // Time Picker
        etTime.setOnClickListener(v -> showTimePicker());

        // Submit Button
        btnSubmit.setOnClickListener(v -> submitBooking());
    }

    private ArrayList<String> loadCarBrands() {
        ArrayList<String> carBrands = new ArrayList<>();
        var cursor = dbHelper.getAllCars();
        while (cursor.moveToNext()) {
            String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
            if (!carBrands.contains(brand)) carBrands.add(brand);
        }
        cursor.close();

        if (carBrands.isEmpty()) {
            carBrands.add("BMW");
            carBrands.add("AUDI");
            carBrands.add("TOYOTA");
            carBrands.add("HONDA");
            carBrands.add("CRETA");
            carBrands.add("VERNA");
        }
        return carBrands;
    }

    //datepicker//
    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) ->
                        etDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year),
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        dialog.show();
    }
//Time Picker//
    private void showTimePicker() {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog dialog = new TimePickerDialog(
                this,
                (view, hourOfDay, minute) ->
                        etTime.setText(String.format("%02d:%02d", hourOfDay, minute)),
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                true
        );
        dialog.show();
    }

    private void submitBooking() {
        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String contact = etContact.getText().toString().trim();
        String date = etDate.getText().toString().trim();
        String time = etTime.getText().toString().trim();
        String brand = modelSpinner.getSelectedItem() != null ? modelSpinner.getSelectedItem().toString() : "";

        if (name.isEmpty() || email.isEmpty() || contact.isEmpty() ||
                date.isEmpty() || time.isEmpty() || brand.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        boolean inserted = dbHelper.insertBooking(name, email, contact, brand, date, time);
        if (inserted) {
            Toast.makeText(this, "✅ Test Drive Booked Successfully!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, DashboardActivity.class));
            finish();
        } else {
            Toast.makeText(this, "❌ Booking Failed", Toast.LENGTH_SHORT).show();
        }
    }
}
