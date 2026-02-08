package com.example.yourcars.Adapter;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yourcars.R;
import com.example.yourcars.helper.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {

    Context context;
    ArrayList<HashMap<String, String>> requestList;
    DBHelper dbHelper;
    private static final int SMS_PERMISSION_CODE = 101;

    public RequestAdapter(Context context, ArrayList<HashMap<String, String>> requestList, DBHelper dbHelper) {
        this.context = context;
        this.requestList = requestList;
        this.dbHelper = dbHelper;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        HashMap<String, String> map = requestList.get(position);

        holder.tvName.setText(map.get("name"));
        holder.tvModel.setText(map.get("brand"));
        holder.tvContact.setText(map.get("contact"));
        holder.tvEmail.setText(map.get("email"));
        holder.tvDateTime.setText(map.get("date") + " " + map.get("time"));
        holder.tvStatus.setText(map.get("status"));

        String status = map.get("status");

        //  Change background color & disable buttons based on status
        if ("Approved".equalsIgnoreCase(status)) {
            holder.itemView.setAlpha(1f);
            holder.tvStatus.setBackgroundResource(R.drawable.status_bg_approved);
            holder.btnApprove.setEnabled(false);
            holder.btnReject.setEnabled(false);
        } else if ("Rejected".equalsIgnoreCase(status)) {
            holder.itemView.setAlpha(0.5f);
            holder.tvStatus.setBackgroundResource(R.drawable.status_bg_rejected);
            holder.btnApprove.setEnabled(false);
            holder.btnReject.setEnabled(false);
        } else {
            holder.itemView.setAlpha(1f);
            holder.tvStatus.setBackgroundResource(R.drawable.status_bg_pending);
            holder.btnApprove.setEnabled(true);
            holder.btnReject.setEnabled(true);
        }

        //  Approve button
        holder.btnApprove.setOnClickListener(v -> {
            int bookingId = Integer.parseInt(map.get("id"));
            dbHelper.updateBookingStatus(bookingId, "Approved");

            map.put("status", "Approved");
            holder.tvStatus.setText("Approved");
            holder.tvStatus.setBackgroundResource(R.drawable.status_bg_approved);
            holder.itemView.setAlpha(1f);
            holder.btnApprove.setEnabled(false);
            holder.btnReject.setEnabled(false);

            sendSMS(map.get("contact"), "✅ Your test drive request has been approved!");
            Toast.makeText(context, "Approved and user notified", Toast.LENGTH_SHORT).show();

            notifyItemChanged(position);
        });

        //  Reject button
        holder.btnReject.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Reject Request")
                    .setMessage("Are you sure you want to reject this test drive request?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        int bookingId = Integer.parseInt(map.get("id"));
                        dbHelper.updateBookingStatus(bookingId, "Rejected");

                        map.put("status", "Rejected");
                        holder.tvStatus.setText("Rejected");
                        holder.tvStatus.setBackgroundResource(R.drawable.status_bg_rejected);
                        holder.itemView.setAlpha(0.5f);
                        holder.btnApprove.setEnabled(false);
                        holder.btnReject.setEnabled(false);

                        sendSMS(map.get("contact"), "❌ Your test drive request has been rejected.");
                        Toast.makeText(context, "Request rejected and user notified", Toast.LENGTH_SHORT).show();

                        notifyItemChanged(position);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvModel, tvDateTime, tvStatus, tvEmail, tvContact;
        Button btnApprove, btnReject;

        public ViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvModel = itemView.findViewById(R.id.tvModel);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            tvEmail = itemView.findViewById(R.id.tvEmail);
            tvContact = itemView.findViewById(R.id.tvContact);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            btnApprove = itemView.findViewById(R.id.btnApprove);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }

    //  Send SMS with permission check
    private void sendSMS(String contact, String message) {
        if (contact == null || contact.isEmpty()) {
            Toast.makeText(context, "Contact not available", Toast.LENGTH_SHORT).show();
            return;
        }

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) context,
                    new String[]{Manifest.permission.SEND_SMS}, SMS_PERMISSION_CODE);
        } else {
            try {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(contact, null, message, null, null);
            } catch (Exception e) {
                Toast.makeText(context, "Failed to send SMS", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }
}
