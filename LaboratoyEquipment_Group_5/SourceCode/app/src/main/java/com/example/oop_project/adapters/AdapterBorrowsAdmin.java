package com.example.oop_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.activities.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowBorrowsAdminBinding;
import com.example.oop_project.filters.FilterBorrowsAdmin;
import com.example.oop_project.filters.FilterEquipmentBorrowed;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterBorrowsAdmin extends RecyclerView.Adapter<AdapterBorrowsAdmin.HolderBorrowsAdmin> implements Filterable {
    private Context context;
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private FilterBorrowsAdmin filter;
    private RowBorrowsAdminBinding binding;


    public AdapterBorrowsAdmin(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        filterList = equipmentArrayList;

    }

    @NonNull
    @Override
    public HolderBorrowsAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowBorrowsAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderBorrowsAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderBorrowsAdmin holder, int position) {
        ModelEquipment model = equipmentArrayList.get(position);
        String equipmentId = model.getId();
        String key = model.getKey();
        binding.textDate.setText("Thời gian mượn: ");
        binding.categoryTv.setVisibility(View.GONE);
        String uid = model.getUid();
        String preStatus = model.getPreStatus();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
        ref.child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(timestamp));
                        holder.dateTv.setText(date);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(uid)
                        .child("Borrowed").child(key)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        String equipmentId = "" + snapshot.child("equipmentId").getValue();
                                        String quantityBorrowed = "" + snapshot.child("quantityBorrowed").getValue();
                                        holder.quantityTv.setText(quantityBorrowed);
                                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Equipments");
                                        ref1.child(equipmentId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot snapshots) {
                                                        String title = "" + snapshots.child("title").getValue();
                                                        String description = "" + snapshots.child("description").getValue();
                                                        holder.titleTv.setText(title);
                                                        holder.descriptionTv.setText(description);
                                                        String equipmentId = "" + snapshots.child("id").getValue();
                                                        model.setId(equipmentId);

                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", model.getId());
                intent.putExtra("status", model.getStatus());
                intent.putExtra("key", model.getKey());
                intent.putExtra("role", "admin");
                intent.putExtra("personI4", "admin");
                intent.putExtra("preStatus", preStatus);
                context.startActivity(intent);
            }
        });
        DatabaseReference refImage = FirebaseDatabase.getInstance().getReference("Equipments");
        refImage.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String equipmentImage = "" + snapshot.child("equipmentImage").getValue();
                Log.d("equipmentImage", equipmentImage);
                Glide.with(context)
                            .load(equipmentImage)
                            .centerCrop()
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                    binding.imageView.setVisibility(View.VISIBLE);
                                    return false;
                                }
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    binding.progressBar.setVisibility(View.VISIBLE);
                                    return false;
                                }

                            })
                            .into(binding.imageView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return equipmentArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterBorrowsAdmin(filterList, this);
        }
        return filter;
    }

    class HolderBorrowsAdmin extends RecyclerView.ViewHolder{
        TextView titleTv, categoryTv, quantityTv, descriptionTv, dateTv;
        ProgressBar progressBar;
        ImageView imageView;
        public HolderBorrowsAdmin(@NonNull View itemView) {
            super(itemView);
            titleTv = binding.titleTv;
            categoryTv = binding.categoryTv;
            quantityTv = binding.quantityTv;
            descriptionTv = binding.descriptionTv;
            dateTv = binding.dateTv;
            imageView = binding.imageView;
            progressBar = binding.progressBar;
        }
    }
}
