package com.example.oop_project.adapters.user;

import android.annotation.SuppressLint;
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
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.activities.common.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsScheduleBinding;
import com.example.oop_project.filters.user.FilterSchedule;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class AdapterSchedule extends  RecyclerView.Adapter<AdapterSchedule.HolderEquipmentsSchedule> implements Filterable {

    private Context context;
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private RowEquipmentsScheduleBinding binding;
    private FilterSchedule filter;
    private ArrayList<Integer> quantityBorrow;
    private ArrayList<Boolean> isChecked;

    public AdapterSchedule(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        this.filterList = equipmentArrayList;
        quantityBorrow = new ArrayList<>();
        isChecked = new ArrayList<>();
        for(int i = 0; i < 10000; i++){
            quantityBorrow.add(0);
            isChecked.add(false);
        }
    }

    @NonNull
    @Override
    public HolderEquipmentsSchedule onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsScheduleBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipmentsSchedule(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentsSchedule holder, @SuppressLint("RecyclerView") int position) {
        ModelEquipment modelEquipment = equipmentArrayList.get(position);
        loadEquipmentDetails(modelEquipment, holder);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", modelEquipment.getId());
                context.startActivity(intent);
            }
        });
        holder.minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityBorrow.get(position) == 0){
                    quantityBorrow.set(position, 0);
                }else{
                    int x = quantityBorrow.get(position);
                    x--;
                    quantityBorrow.set(position, x);
                }
                holder.quantity_text_choose.setText(""+quantityBorrow.get(position));
                modelEquipment.setQuantityBorrow(quantityBorrow.get(position));
                long timestamp = System.currentTimeMillis();
                Intent intent = new Intent("ACTION_GET_DATASCHEDULE");
                intent.putExtra("equipmentId", modelEquipment.getId());
                intent.putExtra("quantityBorrowed", ""+quantityBorrow.get(position));
                intent.putExtra("equipmentName", modelEquipment.getTitle());
                intent.putExtra("timestamp", ""+timestamp);
                intent.putExtra("isChecked", ""+isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityBorrow.get(position) < modelEquipment.getQuantity()){
                    int x = quantityBorrow.get(position);
                    x++;
                    quantityBorrow.set(position, x);
                }else{
                    quantityBorrow.set(position, modelEquipment.getQuantity());
                }
                holder.quantity_text_choose.setText(""+quantityBorrow.get(position));
                modelEquipment.setQuantityBorrow(quantityBorrow.get(position));
                long timestamp = System.currentTimeMillis();
                Intent intent = new Intent("ACTION_GET_DATASCHEDULE");
                intent.putExtra("equipmentId", modelEquipment.getId());
                intent.putExtra("quantityBorrowed", ""+quantityBorrow.get(position));
                intent.putExtra("equipmentName", modelEquipment.getTitle());
                intent.putExtra("timestamp", ""+timestamp);
                intent.putExtra("isChecked", ""+isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        holder.checkIsBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = isChecked.get(position);
                flag = !flag;
                isChecked.set(position, flag);
                holder.checkIsBorrow.setChecked(flag);
                equipmentArrayList.set(position, modelEquipment);
                long timestamp = System.currentTimeMillis();
                Intent intent = new Intent("ACTION_GET_DATASCHEDULE");
                intent.putExtra("equipmentId", modelEquipment.getId());
                intent.putExtra("quantityBorrowed", ""+quantityBorrow.get(position));
                intent.putExtra("equipmentName", modelEquipment.getTitle());
                intent.putExtra("timestamp", ""+timestamp);
                intent.putExtra("isChecked", ""+isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

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
        holder.quantity_text_choose.setText("0");
        String categoryId = model.getCategoryId();


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
