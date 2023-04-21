package com.example.oop_project.adapters.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.example.oop_project.activities.common.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsUserBinding;
import com.example.oop_project.filters.user.FilterEquipmentUser;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class AdapterEquipmentUser extends RecyclerView.Adapter<AdapterEquipmentUser.HolderEquipemntUser> implements Filterable {
    private Context context;
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private FilterEquipmentUser filter;

    private RowEquipmentsUserBinding binding;

    public AdapterEquipmentUser(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        this.filterList = equipmentArrayList;

    }

    @NonNull
    @Override
    public HolderEquipemntUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        binding = RowEquipmentsUserBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipemntUser(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipemntUser holder, int position) {

        ModelEquipment model = equipmentArrayList.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        String categoryId = model.getCategoryId();
        int quantity = model.getQuantity();
        long timestamp = model.getTimestamp();
        int viewed = model.getViewed();
        String equipmentImage = model.getEquipmentImage();

        String date = MyApplication.formatTimestamp(timestamp);

        // set data

        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.quantityTv.setText(""+quantity);
        holder.dateTv.setText(date);

        ModelCategory modelCategory = new ModelCategory();
        modelCategory.getDataFromFireBase(categoryId).addOnCompleteListener(new OnCompleteListener<ModelCategory>() {
            @Override
            public void onComplete(@NonNull Task<ModelCategory> task) {
                if(task.isSuccessful()){
                    ModelCategory newModelCategory = task.getResult();
                    holder.categoryTv.setText(newModelCategory.getTitle());
                }
            }
        });
//        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
//        ref.child(categoryId)
//                .addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        String titleCategory = "" + snapshot.child("title").getValue();
//                        holder.categoryTv.setText(titleCategory);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", model.getId());
                context.startActivity(intent);

            }
        });
        Glide.with(context)
                .load(equipmentImage)
                .centerCrop()
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        binding.progressBar.setVisibility(View.VISIBLE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        binding.imageView.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(binding.imageView);
    }

    @Override
    public int getItemCount() {
        return equipmentArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterEquipmentUser(filterList, this);
        }
        return filter;
    }

    class HolderEquipemntUser extends RecyclerView.ViewHolder{
        TextView titleTv;
        TextView categoryTv;
        TextView quantityTv;
        TextView descriptionTv;
        TextView dateTv;
        ProgressBar progressBar;
        ImageView imageView;

        public HolderEquipemntUser(@NonNull View itemView) {
            super(itemView);
            imageView = binding.imageView;
            titleTv = binding.titleTv;
            categoryTv = binding.categoryTv;
            quantityTv = binding.quantityTv;
            dateTv = binding.dateTv;
            descriptionTv = binding.descriptionTv;
            progressBar = binding.progressBar;
        }
    }
}
