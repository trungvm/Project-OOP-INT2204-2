package com.example.oop_project.adapters.admin;

import android.annotation.SuppressLint;
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
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.activities.common.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsScheduleAdminBinding;
import com.example.oop_project.filters.admin.FilterScheduleAdmin;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterScheduleAdmin extends RecyclerView.Adapter<AdapterScheduleAdmin.HolderEquipmentsScheduleAdmin> implements Filterable {
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private final Context context;
    private RowEquipmentsScheduleAdminBinding binding;
    private final ArrayList<Boolean> isChecked;
    private final ArrayList<String> startDate;
    private final ArrayList<String> endDate;
    private FilterScheduleAdmin filter;


    public AdapterScheduleAdmin(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        isChecked = new ArrayList<>();
        startDate = new ArrayList<>();
        endDate = new ArrayList<>();
        filterList = equipmentArrayList;
        for (int i = 0; i < 10000; i++) {
            isChecked.add(false);
            startDate.add(" ");
            endDate.add(" ");
        }
    }

    @NonNull
    @Override
    public AdapterScheduleAdmin.HolderEquipmentsScheduleAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsScheduleAdminBinding.inflate(LayoutInflater.from(context), parent, false);
        return new AdapterScheduleAdmin.HolderEquipmentsScheduleAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentsScheduleAdmin holder, @SuppressLint("RecyclerView") int position) {
        ModelEquipment model = equipmentArrayList.get(position);
        String equipmentId = model.getId();
        String key = model.getKey();
        String uid = model.getUid();
        String adminStatus = model.getAdminStatus();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
        ref.child(key)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String startDate = "" + snapshot.child("startDate").getValue();
                        String endDate = "" + snapshot.child("endDate").getValue();
                        String fullName = "" + snapshot.child("fullName").getValue();
                        holder.descriptionTv.setText("Người mượn: " + fullName);
                        holder.startDate.setText(startDate);
                        holder.endDate.setText(endDate);

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
//                                        holder.descriptionTv.setText(description);
                                        String equipmentId = "" + snapshots.child("id").getValue();
                                        model.setId(equipmentId);
                                        String categoryId = "" + snapshots.child("categoryId").getValue();

                                        ModelCategory modelCategory = new ModelCategory();
                                        modelCategory.getDataFromFireBase(categoryId).addOnCompleteListener(new OnCompleteListener<ModelCategory>() {
                                            @Override
                                            public void onComplete(@NonNull Task<ModelCategory> task) {
                                                if (task.isSuccessful()) {
                                                    ModelCategory newModelCategory = task.getResult();
                                                    holder.categoryTv.setText(newModelCategory.getTitle());
                                                }
                                            }
                                        });

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
        holder.checkIsBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = isChecked.get(position);
                flag = !flag;
                isChecked.set(position, flag);
                holder.checkIsBorrow.setChecked(flag);
                equipmentArrayList.set(position, model);
                Intent intent = new Intent("ACTION_GET_DATA");
                intent.putExtra("equipmentId", model.getId());
                intent.putExtra("key", model.getKey());
                intent.putExtra("adminStatus", adminStatus);
                intent.putExtra("timestamp", "" + model.getTimestamp());
                intent.putExtra("isChecked", "" + isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return equipmentArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterScheduleAdmin(filterList, this);
        }
        return filter;
    }

    class HolderEquipmentsScheduleAdmin extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView imageView;
        TextView titleTv, descriptionTv, quantityTv, categoryTv;
        RadioButton checkIsBorrow;
        TextView startDate, endDate;


        public HolderEquipmentsScheduleAdmin(@NonNull View itemView) {
            super(itemView);
            progressBar = binding.progressBar;
            imageView = binding.imageView;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            quantityTv = binding.quantityTv;
            categoryTv = binding.categoryTv;
            checkIsBorrow = binding.checkIsBorrow;
            startDate = binding.startDate;
            endDate = binding.endDate;
        }
    }
}
