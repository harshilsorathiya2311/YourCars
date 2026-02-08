package com.example.yourcars.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.yourcars.R;
import com.example.yourcars.model.CarModel;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;

public class CarHorizontalAdapter extends RecyclerView.Adapter<CarHorizontalAdapter.UserViewHolder> {

    private final Context context;
    private final ArrayList<CarModel> carList;
    private final OnUserClickListener listener;

    public interface OnUserClickListener {
        void onUserClick(CarModel carModel);
    }

    public CarHorizontalAdapter(Context context, ArrayList<CarModel> carList, OnUserClickListener listener) {
        this.context = context;
        this.carList = carList;
        this.listener = listener;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car_horizontal, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        CarModel carModel = carList.get(position);

        holder.modelName.setText(carModel.getTitle());

        if (carModel.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(carModel.getImage(), 0, carModel.getImage().length);
            holder.userImage.setImageBitmap(bitmap);
        } else {
            holder.userImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onUserClick(carModel);
        });
    }

    @Override
    public int getItemCount() {
        return carList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        TextView modelName;

        public UserViewHolder(View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.userImage);
            modelName = itemView.findViewById(R.id.modelName);
        }
    }
}
