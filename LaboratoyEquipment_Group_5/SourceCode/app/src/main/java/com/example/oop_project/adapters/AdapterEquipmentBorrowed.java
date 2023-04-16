package com.example.oop_project.adapters;

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
import com.example.oop_project.MyApplication;
import com.example.oop_project.activities.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsBorrowedBinding;
import com.example.oop_project.filters.FilterEquipmentBorrowed;
import com.example.oop_project.filters.FilterEquipmentUser;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterEquipmentBorrowed extends  RecyclerView.Adapter<AdapterEquipmentBorrowed.HolderEquipmentBorrowed> implements Filterable {
    private Context context;
    public ArrayList<ModelEquipment> equipmentArrayList, filterList;
    private RowEquipmentsBorrowedBinding binding;
    private FirebaseAuth firebaseAuth;
    private FilterEquipmentBorrowed filter;
    private ArrayList<Boolean> isChecked;

    public AdapterEquipmentBorrowed(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        firebaseAuth = FirebaseAuth.getInstance();
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        filterList = equipmentArrayList;
        isChecked = new ArrayList<>();
        for(int i = 0; i < 1000; i++){
            isChecked.add(false);
        }


    }

    public void clear() {
        equipmentArrayList.clear();
        notifyDataSetChanged();
    }
    public int checkSize(){
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
        if(model.getStatus().equals("Borrowed")){
            binding.checkBox.setVisibility(View.VISIBLE);
            binding.categoryTv.setVisibility(View.GONE);
        }else if(model.getStatus().equals("History") || model.getStatus().equals("Waiting")){
            binding.checkBox.setVisibility(View.GONE);
            binding.categoryTv.setVisibility(View.GONE);
        }
        binding.textDate.setText("Thời gian mượn: ");
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.child(firebaseAuth.getUid())
                    .child("Borrowed")
                            .child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String time = "" + snapshot.child("timestamp").getValue();
                        String quantityBorrowed  = "" + snapshot.child("quantityBorrowed").getValue();
                        String date = MyApplication.formatTimestampToDetailTime(Long.parseLong(time));

                        holder.quantityTv.setText(quantityBorrowed);
                        holder.dateTv.setText(date);

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
                intent.putExtra("uid", ""+firebaseAuth.getUid());
                intent.putExtra("key", equipmentArrayList.get(position).getKey());

                intent.putExtra("isChecked", ""+isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(equipmentArrayList == null){
            return 0;
        }else return equipmentArrayList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterEquipmentBorrowed(filterList, this);
        }
        return filter;
    }

    class HolderEquipmentBorrowed extends RecyclerView.ViewHolder{
        TextView titleTv, categoryTv, quantityTv, descriptionTv, dateTv;
        ProgressBar progressBar;
        ImageView imageView;
        RadioButton checkIs;
        public HolderEquipmentBorrowed(@NonNull View itemView) {
            super(itemView);
            titleTv = binding.titleTv;
            categoryTv = binding.categoryTv;
            quantityTv = binding.quantityTv;
            descriptionTv = binding.descriptionTv;
            dateTv = binding.dateTv;
            imageView = binding.imageView;
            progressBar = binding.progressBar;
            checkIs = binding.checkIs;
        }
    }
}
