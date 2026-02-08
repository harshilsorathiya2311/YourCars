package com.example.yourcars.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;

public class CarListAdapter extends CursorAdapter {

    public CarListAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.itemuser_side_carlist, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView imgCar = view.findViewById(R.id.imgCar);
        TextView tvTitle = view.findViewById(R.id.tvCarTitle);
        TextView tvDescription = view.findViewById(R.id.tvCarDescription);

        // get data
        String title = cursor.getString(cursor.getColumnIndexOrThrow("title"));
        String description = cursor.getString(cursor.getColumnIndexOrThrow("description"));
        byte[] imageData = cursor.getBlob(cursor.getColumnIndexOrThrow("image"));

        tvTitle.setText(title);
        tvDescription.setText(description);

        if (imageData != null) {
            byte[] thumb = DBHelper.getThumbnail(imageData, 120, 120);
            Bitmap bmp = BitmapFactory.decodeByteArray(thumb, 0, thumb.length);
            imgCar.setImageBitmap(bmp);
        } else {
            imgCar.setImageResource(R.drawable.ic_launcher_foreground);
        }
    }
}
