package com.example.oop_project.adapters.user;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.activities.common.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsBorrowedBinding;
import com.example.oop_project.filters.user.FilterEquipmentBorrowed;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterEquipmentBorrowed extends RecyclerView.Adapter<AdapterEquipmentBorrowed.HolderEquipmentBorrowed> implements Filterable {
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private final Context context;
    private RowEquipmentsBorrowedBinding binding;
    private final FirebaseAuth firebaseAuth;
    private FilterEquipmentBorrowed filter;
    private final ArrayList<Boolean> isChecked;
    private ArrayList<Boolean> isCallbackHandled;

    public AdapterEquipmentBorrowed(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        filterList = equipmentArrayList;
        isChecked = new ArrayList<>();
        isCallbackHandled = new ArrayList<>();
        for (int i = 0; i < 1000; i++) {
            isChecked.add(false);
            isCallbackHandled.add(false);
        }


    }

    public void clear() {
        equipmentArrayList.clear();
        notifyDataSetChanged();
    }

    public int checkSize() {
        return equipmentArrayList.size();
    }

    @NonNull
    @Override
    public HolderEquipmentBorrowed onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsBorrowedBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipmentBorrowed(binding.getRoot());
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentBorrowed holder, int position) {
        ModelEquipment model = equipmentArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        String equipmentImage = model.getEquipmentImage();
        String key = model.getKey();
        String preStatus = model.getPreStatus();
        if (model.getStatus().equals("Borrowed")) {
            binding.checkBox.setVisibility(View.VISIBLE);
        } else if (model.getStatus().equals("History") || model.getStatus().equals("Waiting") || model.getStatus().equals("Refuse")) {
            binding.checkBox.setVisibility(View.GONE);
        }
        binding.textDate.setText("Thời gian mượn: ");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
        ref.child(key).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String timestamp = "" + snapshot.child("timestamp").getValue();
                String date = "";
                if(timestamp.equals("null") || timestamp.equals("")){

                }else{
                    date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                }
                holder.dateTv.setText(date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                .child("Borrowed")
                .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String quantityBorrowed = "" + snapshot.child("quantityBorrowed").getValue();

                        holder.quantityTv.setText(quantityBorrowed);


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", model.getId());
                intent.putExtra("status", model.getStatus());
                intent.putExtra("key", model.getKey());
                intent.putExtra("preStatus", preStatus);
                context.startActivity(intent);
            }
        });
       if(equipmentImage.equals("") || equipmentImage.equals("null")){

       }else{
           Handler handler = new Handler(Looper.getMainLooper());
           Glide.with(context)
                   .load(equipmentImage)
                   .centerCrop()
                   .listener(new RequestListener<Drawable>() {
                       @Override
                       public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                           if (!isCallbackHandled.get(position)) { // Kiểm tra nếu chưa xử lý callback
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       holder.progressBar.setVisibility(View.VISIBLE);
                                       holder.imageView.setVisibility(View.GONE);
                                   }
                               });

                               // Đánh dấu là đã xử lý callback
                               isCallbackHandled.set(position, true);
                           }

                           // Trả về false để cho phép Glide xử lý callback tiếp
                           return false;
                       }

                       @Override
                       public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                           if (!isCallbackHandled.get(position)) { // Kiểm tra nếu chưa xử lý callback
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       holder.imageView.setVisibility(View.VISIBLE);

                                       holder.progressBar.setVisibility(View.GONE);
                                   }
                               });

                               // Đánh dấu là đã xử lý callback
                               isCallbackHandled.set(position, true);
                           }

                           // Trả về false để cho phép Glide xử lý callback tiếp
                           return false;
                       }
                   })
                   .into(holder.imageView);
       }
        holder.checkIs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = isChecked.get(position);
                flag = !flag;
                isChecked.set(position, flag);
                holder.checkIs.setChecked(flag);
                equipmentArrayList.set(position, model);
                Intent intent = new Intent("ACTION_GET_DATA");
                intent.putExtra("equipmentId", model.getId());
                intent.putExtra("uid", "" + firebaseAuth.getUid());
                intent.putExtra("key", equipmentArrayList.get(position).getKey());

                intent.putExtra("isChecked", "" + isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (equipmentArrayList == null) {
            return 0;
        } else return equipmentArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterEquipmentBorrowed(filterList, this);
        }
        return filter;
    }

    class HolderEquipmentBorrowed extends RecyclerView.ViewHolder {
        TextView titleTv, categoryTv, quantityTv, descriptionTv, dateTv;
        ProgressBar progressBar;
        ImageView imageView;
        RadioButton checkIs;

        public HolderEquipmentBorrowed(@NonNull View itemView) {
            super(itemView);
            titleTv = binding.titleTv;
            quantityTv = binding.quantityTv;
            descriptionTv = binding.descriptionTv;
            dateTv = binding.dateTv;
            imageView = binding.imageView;
            progressBar = binding.progressBar;
            checkIs = binding.checkIs;
        }
    }
}
