package com.example.yourcars.ui.admin;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourcars.Adapter.RequestAdapter;
import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class  TestDriveRequestFragment extends Fragment {

    RecyclerView rvRequests;
    DBHelper dbHelper;
    ArrayList<HashMap<String, String>> requestList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test_drive_request, container, false);

        rvRequests = view.findViewById(R.id.rvRequests);
        dbHelper = new DBHelper(getContext());
        requestList = new ArrayList<>();

        loadRequests();

        rvRequests.setLayoutManager(new LinearLayoutManager(getContext()));
        rvRequests.setAdapter(new RequestAdapter(getContext(), requestList, dbHelper));

        return view;
    }

    private void loadRequests() {
        requestList.clear();
        Cursor cursor = dbHelper.getAllBookings();
        while(cursor.moveToNext()){
            HashMap<String, String> map = new HashMap<>();
            map.put("id", cursor.getString(cursor.getColumnIndexOrThrow("id")));
            map.put("name", cursor.getString(cursor.getColumnIndexOrThrow("name")));
            map.put("email", cursor.getString(cursor.getColumnIndexOrThrow("email")));
            map.put("contact", cursor.getString(cursor.getColumnIndexOrThrow("contact")));
            map.put("brand", cursor.getString(cursor.getColumnIndexOrThrow("brand")));
            map.put("date", cursor.getString(cursor.getColumnIndexOrThrow("date")));
            map.put("time", cursor.getString(cursor.getColumnIndexOrThrow("time")));
            map.put("status", cursor.getString(cursor.getColumnIndexOrThrow("status")));
            requestList.add(map);
        }
        cursor.close();
    }


    @Override
    public void onResume() {
        super.onResume();
        loadRequests();
        rvRequests.getAdapter().notifyDataSetChanged();
    }

}
