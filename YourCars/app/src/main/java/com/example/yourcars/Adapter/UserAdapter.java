package com.example.yourcars.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourcars.R;
import com.example.yourcars.model.UserModel;

import java.util.ArrayList;

public abstract class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private final Context context;
    private final ArrayList<UserModel> userList;

    public UserAdapter(Context context, ArrayList<UserModel> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Make fields public so the fragment can access them
    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView Email;
        public TextView ContactNum;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            Email = itemView.findViewById(R.id.userEmail);
            ContactNum = itemView.findViewById(R.id.userContact);
        }
    }
}
