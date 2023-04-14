package com.example.oop_project.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.databinding.RowEquipmentsScheduleBinding;
import com.example.oop_project.filters.FilterBorrowsAdmin;
import com.example.oop_project.filters.FilterSchedule;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterSchedule extends  RecyclerView.Adapter<AdapterSchedule.HolderEquipmentsSchedule> implements Filterable {

    private Context context;
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private RowEquipmentsScheduleBinding binding;
    private FilterSchedule filter;

    public AdapterSchedule(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        this.filterList = equipmentArrayList;
    }

    @NonNull
    @Override
    public HolderEquipmentsSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsScheduleBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipmentsSchedule(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentsSchedule holder, int position) {
        ModelEquipment modelEquipment = equipmentArrayList.get(position);
        loadEquipmentDetails(modelEquipment, holder);

    }
    private void loadEquipmentDetails(ModelEquipment model, HolderEquipmentsSchedule holder) {
        String equipmentId = model.getId();
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

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterSchedule(filterList, this);
        }
        return filter;
    }

    class HolderEquipmentsSchedule extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        ImageView imageView;
        TextView titleTv, descriptionTv, quantityTv, dateTv, categoryTv;
        RadioButton checkIsBorrow;
        TextView quantity_text_choose;
        AppCompatButton plus_button, minus_button;

        public HolderEquipmentsSchedule(@NonNull View itemView) {
            super(itemView);
            progressBar = binding.progressBar;
            imageView = binding.imageView;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            quantityTv = binding.quantityTv;
            dateTv = binding.dateTv;
            categoryTv = binding.categoryTv;
            checkIsBorrow = binding.checkIsBorrow;
            quantity_text_choose = binding.quantityTextChoose;
            plus_button = binding.plusButton;
            minus_button = binding.minusButton;
        }
    }
}
