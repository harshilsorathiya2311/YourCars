package com.example.yourcars.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourcars.R;
import com.example.yourcars.model.CarModel;

import java.util.ArrayList;

public class CarAdapter  extends BaseAdapter {

    private final Context context;
    private final ArrayList<CarModel> carList;
    public CarAdapter(Context context, ArrayList<CarModel> carList) {
        this.context = context;
        this.carList = carList;
    }
    @Override
    public int getCount() {
        return carList.size();
    }

    @Override
    public Object getItem(int position) {
        return carList.get(position);
    }
    @Override
    public long getItemId(int position) {

        return carList.get(position).getId();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.car_item, parent, false);
        }
        ImageView carImage = convertView.findViewById(R.id.userImage);
        TextView modelName = convertView.findViewById(R.id.modelName);
        TextView title = convertView.findViewById(R.id.title);
        TextView date = convertView.findViewById(R.id.date);


        CarModel car = carList.get(position);

        modelName.setText(car.getBrandValue());
        title.setText(car.getTitle());
        date.setText(car.getDate());

        //set profile//
        if (car.getImage() != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(car.getImage(), 0, car.getImage().length);
            carImage.setImageBitmap(bitmap);
        } else {
            carImage.setImageResource(R.drawable.ic_launcher_foreground);
        }

        // --- Fancy fade+slide animation ---
        convertView.clearAnimation();
        convertView.setAlpha(0f);
        convertView.setTranslationY(50f);
        convertView.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(400)
                .setStartDelay(position * 100) // staggered effect
                .start();

        return convertView;
    }

}
