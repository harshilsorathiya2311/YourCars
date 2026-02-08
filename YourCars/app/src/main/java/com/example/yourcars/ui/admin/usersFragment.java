package com.example.yourcars.ui.admin;

import android.annotation.SuppressLint;
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

import com.example.yourcars.Adapter.UserAdapter;
import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;
import com.example.yourcars.model.UserModel;

import java.util.ArrayList;

public class usersFragment extends Fragment {

    RecyclerView recyclerView;
    DBHelper dbHelper;
    ArrayList<UserModel> userList;
    UserAdapter userAdapter;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_users_fragment, container, false);

        recyclerView = view.findViewById(R.id.userRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dbHelper = new DBHelper(getActivity());
        userList = new ArrayList<>();

        // Initialize adapter
        userAdapter = new UserAdapter(getActivity(), userList) {
            @Override
            public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
                UserModel user = userList.get(position);
                holder.Email.setText(user.getEmail());
                holder.ContactNum.setText(user.getContactnumber()); // ensure this matches your model
            }
        };

        recyclerView.setAdapter(userAdapter);
        loadUsers();

        return view;
    }

    private void loadUsers() {
        userList.clear();
        Cursor cursor = dbHelper.getAllUsers(); // ✅ Must exist in DBHelper
        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String email = cursor.getString(cursor.getColumnIndex("email"));
                @SuppressLint("Range") String contact = cursor.getString(cursor.getColumnIndex("contact_num"));
                userList.add(new UserModel(email, contact));
            } while (cursor.moveToNext());
        }
        cursor.close();

        userAdapter.notifyDataSetChanged();
    }
}
