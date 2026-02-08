package com.example.yourcars.ui.admin;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddUpdateCarModelsActivity extends Fragment {

    TextView texttitle;

    private static final int PICK_IMAGE = 2001;  //for upload img
    DBHelper dbHelper;

    EditText title, description, price;
    TextView date;

    ImageView imageView;

    Button uploadimage, save, cancel;
    Spinner brandSpinner;

    int userId = -1;
    byte[] selectedimage = null;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_add_fragment, container, false);

        dbHelper = new DBHelper(getActivity());
        brandSpinner = view.findViewById(R.id.brandSpinner);
        title = view.findViewById(R.id.title);
        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        imageView = view.findViewById(R.id.imageView);
        uploadimage = view.findViewById(R.id.uploadimage);
        save = view.findViewById(R.id.SAVE);
        cancel = view.findViewById(R.id.CANCEL);
        texttitle = view.findViewById(R.id.texttitle);
        price = view.findViewById(R.id.price);

        date.setOnClickListener(v -> showDatePicker());

        // -------------------------------
        // ✅ PRICE FORMATTER (AUTO 12,00,000)
        // -------------------------------
        price.addTextChangedListener(new TextWatcher() {

            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {

                if (!s.toString().equals(current)) {

                    price.removeTextChangedListener(this);

                    String clean = s.toString().replaceAll(",", "");

                    if (!clean.equals("")) {
                        try {
                            long value = Long.parseLong(clean);

                            String formatted = NumberFormat
                                    .getNumberInstance(new Locale("en", "IN"))
                                    .format(value);

                            current = formatted;
                            price.setText(formatted);
                            price.setSelection(formatted.length());

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    price.addTextChangedListener(this);
                }
            }
        });

        // Setup dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_spinner_item,
                new String[]{"BMW", "AUDI", "TOYOTA", "HONDA", "CRETA", "VERNA"});
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        brandSpinner.setAdapter(adapter);

        // Handle edit mode
        if (getArguments() != null) {
            userId = getArguments().getInt("id", -1);
            String carTitle = getArguments().getString("title", "");
            String cardes = getArguments().getString("description", "");
            String lunchdate = getArguments().getString("date", "");
            byte[] CarImg = getArguments().getByteArray("image");
            String carBrand = getArguments().getString("brand", "");
            String carPrice = getArguments().getString("price", "");

            // Restore data
            title.setText(carTitle);
            description.setText(cardes);
            date.setText(lunchdate);

            // format price if loaded from db
            try {
                long val = Long.parseLong(carPrice.replace(",", ""));
                String formatted = NumberFormat.getNumberInstance(new Locale("en", "IN")).format(val);
                price.setText(formatted);
            } catch (Exception e) {
                price.setText(carPrice);
            }

            if (CarImg != null) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(CarImg, 0, CarImg.length);
                imageView.setImageBitmap(bitmap);
                selectedimage = CarImg;
            }

            int position = adapter.getPosition(carBrand);
            if (position >= 0) brandSpinner.setSelection(position);

            // Update UI
            save.setText("Update");
            texttitle.setText("UPDATE CAR");
        } else {
            save.setText("Save");
            texttitle.setText("ADD CAR");
        }

        //Button listeners
        uploadimage.setOnClickListener(v -> chooseimage());
        save.setOnClickListener(v -> saveData());
        cancel.setOnClickListener(v -> getActivity().onBackPressed());

        return view;
    }

    private void chooseimage() {
        startActivityForResult(new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI), PICK_IMAGE);
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(),
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    date.setText(selectedDate);
                },
                year, month, day
        );
        datePickerDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imageView.setImageBitmap(bitmap);
                selectedimage = getResizedBytes(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //for image resize
    private byte[] getResizedBytes(Bitmap bitmap) {
        int maxSize = 500;
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float scale = Math.min((float) maxSize / width, (float) maxSize / height);

        Bitmap resized = Bitmap.createScaledBitmap(bitmap,
                Math.round(width * scale), Math.round(height * scale), true);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        resized.compress(Bitmap.CompressFormat.JPEG, 70, stream);
        return stream.toByteArray();
    }

    private void saveData() {
        String car_title = title.getText().toString().trim();
        String car_date = date.getText().toString().trim();
        String car_des = description.getText().toString().trim();

        // remove commas before saving (store clean number)
        String car_price = price.getText().toString().trim().replace(",", "");

        if (car_title.isEmpty()) {
            Toast.makeText(getActivity(), "Enter a title", Toast.LENGTH_SHORT).show();
            return;
        }
        if (car_date.isEmpty()) {
            Toast.makeText(getActivity(), "Enter a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (car_des.isEmpty()) {
            Toast.makeText(getActivity(), "Enter a description", Toast.LENGTH_SHORT).show();
            return;
        }
        if (car_price.isEmpty()) {
            Toast.makeText(getActivity(), "Enter price", Toast.LENGTH_SHORT).show();
            return;
        }

        String dropdownValue = brandSpinner.getSelectedItem().toString();

        boolean success;
        if (userId == -1) {
            success = dbHelper.insertCarData(selectedimage, dropdownValue, car_title, car_price, car_date, car_des);
        } else {
            success = dbHelper.updateCarData(userId, selectedimage, dropdownValue, car_title, car_price, car_date, car_des);
        }

        if (success) {
            Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
            getActivity().getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_container, new Home_Fragment())
                    .commit();
            clearForm();
        } else {
            Toast.makeText(getActivity(), "Error saving", Toast.LENGTH_SHORT).show();
        }

    }

    private void clearForm() {
        userId = -1;

        title.setText("");
        description.setText("");
        price.setText("");
        date.setText("");

        imageView.setImageResource(R.drawable.ic_launcher_foreground);
        selectedimage = null;

        brandSpinner.setSelection(0);

        texttitle.setText("ADD CAR");
        save.setText("Save");
    }
}
