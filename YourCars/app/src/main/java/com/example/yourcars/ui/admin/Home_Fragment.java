package com.example.yourcars.ui.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.yourcars.Adapter.CarAdapter;
import com.example.yourcars.Adapter.CarHorizontalAdapter;
import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;
import com.example.yourcars.model.CarModel;

import java.util.ArrayList;

public class Home_Fragment extends Fragment {

    ListView listView;
//    RecyclerView recyclerView;
    DBHelper dbHelper;
    ArrayList<CarModel> carList;
    CarAdapter carAdapter;  //for list view
    CarHorizontalAdapter carHorizontalAdapter;  //for recycler view


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_home_fragment, container, false);

        // initialize views
        listView = view.findViewById(R.id.listView);
//        recyclerView = view.findViewById(R.id.recyclerViewUsers);


        dbHelper = new DBHelper(getActivity());
        carList = new ArrayList<>();

        carAdapter = new CarAdapter(getActivity(), carList);
        listView.setAdapter(carAdapter);

        // RecyclerView adapter (new, horizontal)
        carHorizontalAdapter = new CarHorizontalAdapter(getActivity(), carList, carModel -> {
            showChooseActionDialog(carModel);

        });
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
//        recyclerView.setAdapter(carHorizontalAdapter);

        // load data into both
        loadCars();

        // listview item click
        listView.setOnItemClickListener((parent, v, position, id) -> {
            CarModel carModel = carList.get(position);
            showChooseActionDialog(carModel);
        });

        return view;
    }
    private void loadCars() {
        carList.clear();
        Cursor cursor = dbHelper.getAllCars();
        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                byte[] image = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));
                byte[] thumb = dbHelper.getThumbnail(image, 100, 100);
                String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
                String date = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                String brand = cursor.getString(cursor.getColumnIndexOrThrow("brand"));
                String price = cursor.getString(cursor.getColumnIndexOrThrow("price"));
                String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));


                carList.add(new CarModel(id,thumb,brand,title,price, date,description));
            } while (cursor.moveToNext());
        }
        cursor.close();

        // notify both adapters
        if (carAdapter != null) carAdapter.notifyDataSetChanged();
        if (carHorizontalAdapter != null) carHorizontalAdapter.notifyDataSetChanged();
    }

    /** Custom Choose Action Dialog */
    private void showChooseActionDialog(CarModel carmodel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_choose_action, null);
        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnEdit = dialogView.findViewById(R.id.btnEdit);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnEdit.setOnClickListener(v -> {
            dialog.dismiss();
            openEditUser(carmodel);
        });

        btnDelete.setOnClickListener(v -> {
            dialog.dismiss();
            showFancyDeleteDialog(carmodel);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;
    }

    /** Open AddFragment with user data for editing */
    private void openEditUser(CarModel carModel) {
        CarModel allcars = dbHelper.getCarById(carModel.getId());

        Bundle args = new Bundle();
        args.putInt("id", allcars.getId());
        args.putByteArray("image", allcars.getImage());
        args.putString("brand", allcars.getBrandValue());
        args.putString("title", allcars.getTitle());
        args.putString("date", allcars.getDate());
        args.putString("price", allcars.getPrice());
        args.putString("description", allcars.getDescription());

        AddUpdateCarModelsActivity fragment = new AddUpdateCarModelsActivity();
        fragment.setArguments(args);

        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_container, fragment)
                .addToBackStack(null)
                .commit();
    }

    /** Fancy delete dialog */
    private void showFancyDeleteDialog(CarModel carmodel) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_confirm_delete_fancy, null);
        builder.setView(dialogView);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})  TextView tvMessage = dialogView.findViewById(R.id.tvDialogMessage);
        tvMessage.setText("Are you sure you want to delete \"" + carmodel.getTitle() + "\"?");

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})  Button btnConfirm = dialogView.findViewById(R.id.btnConfirm);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"})  Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnConfirm.setOnClickListener(v -> {
            if (dbHelper.deleteCar(carmodel.getId())) {
                Toast.makeText(getActivity(), "Deleted", Toast.LENGTH_SHORT).show();
                loadCars();
            } else {
                Toast.makeText(getActivity(), "Error deleting", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        });

        dialog.show();
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogSlideAnimation;
    }
}