package com.example.oop_project.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.oop_project.databinding.RowEquipmentsFavoriteBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterEquipmentFavorite extends RecyclerView.Adapter<AdapterEquipmentFavorite.HolderEquipmentFavorite>{
    private Context context;
    private ArrayList<ModelEquipment> equipmentArrayList;

    // view binding
    private RowEquipmentsFavoriteBinding binding;

    public AdapterEquipmentFavorite(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
    }

    @NonNull
    @Override
    public HolderEquipmentFavorite onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsFavoriteBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipmentFavorite(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentFavorite holder, int position) {
        ModelEquipment model = equipmentArrayList.get(position);
        loadEquipmentDetails(model, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", model.getId());
                context.startActivity(intent);
            }
        });

        holder.removeFavoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.removeFromFavorite(context, model.getId());

            }
        });


    }

    private void loadEquipmentDetails(ModelEquipment model, AdapterEquipmentFavorite.HolderEquipmentFavorite holder) {
        String title = model.getTitle();
        String description = model.getDescription();
        String equipmentImage = model.getEquipmentImage();
        long timestamp = model.getTimestamp();
        String date = MyApplication.formatTimestamp(timestamp);
        int quantity = model.getQuantity();
        holder.dateTv.setText(date);
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.quantityTv.setText(""+quantity);
        String categoryId = model.getCategoryId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String titleCategory = "" + snapshot.child("title").getValue();
                        holder.categoryTv.setText(titleCategory);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

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

    class HolderEquipmentFavorite extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        TextView titleTv;
        TextView descriptionTv;
        TextView quantityTv;
        TextView dateTv;
        TextView categoryTv;
        ImageButton removeFavoriteBtn;
        ImageView imageView;

        public HolderEquipmentFavorite(@NonNull View itemView) {
            super(itemView);

            // init ui views of row_equipment_favorite
            imageView = binding.imageView;
            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            quantityTv = binding.quantityTv;
            dateTv = binding.dateTv;
            categoryTv = binding.categoryTv;
            removeFavoriteBtn = binding.removeFavoriteBtn;

        }
    }
}
