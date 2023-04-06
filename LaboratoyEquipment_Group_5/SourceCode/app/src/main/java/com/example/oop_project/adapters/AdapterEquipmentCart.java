package com.example.oop_project.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

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
import com.example.oop_project.activities.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsCartBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterEquipmentCart extends RecyclerView.Adapter<AdapterEquipmentCart.HolderEquipmentCart> {
    private Context context;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private RowEquipmentsCartBinding binding;
    private int quantityBorrow = 0;
    private boolean isChecked = false;

    public AdapterEquipmentCart(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
    }

    @NonNull
    @Override
    public HolderEquipmentCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipmentCart(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentCart holder, int position) {
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
        holder.removeCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyApplication.removeFromCart(context, modelEquipment.getId());
            }
        });
        holder.minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityBorrow == 0){
                    quantityBorrow = 0;
                }else{
                    quantityBorrow--;
                }
                holder.quantity_text_choose.setText(""+quantityBorrow);
                modelEquipment.setQuantityBorrow(quantityBorrow);
            }
        });
        holder.plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(quantityBorrow < modelEquipment.getQuantity()){
                    quantityBorrow++;
                }else{
                    quantityBorrow = modelEquipment.getQuantity();
                }
                holder.quantity_text_choose.setText(""+quantityBorrow);
                modelEquipment.setQuantityBorrow(quantityBorrow);
            }
        });
        holder.checkIsBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isChecked = ! isChecked;
                holder.checkIsBorrow.setChecked(isChecked);
            }
        });

    }

    private void loadEquipmentDetails(ModelEquipment model, HolderEquipmentCart holder) {
        String equipmentId = model.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(equipmentId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // get equipment information
                        String title = "" + snapshot.child("title").getValue();
                        String description = "" + snapshot.child("description").getValue();
                        String quantity = "" + snapshot.child("quantity").getValue();
                        String categoryId = "" + snapshot.child("categoryId").getValue();
                        String timestamp = "" + snapshot.child("timestamp").getValue();
                        String viewed = "" + snapshot.child("viewed").getValue();
                        String uid = "" + snapshot.child("uid").getValue();
                        String equipmentImage = "" + snapshot.child("equipmentImage").getValue();



                        // set to model
                        model.setTitle(title);
                        model.setDescription(description);
                        model.setTimestamp(Long.parseLong(timestamp));
                        model.setCategoryId(categoryId);
                        model.setUid(uid);
                        model.setQuantity(Integer.parseInt(quantity));
                        model.setQuantityBorrow(quantityBorrow);

                        String date = MyApplication.formatTimestamp(Long.parseLong(timestamp));


                        // set to view
                        holder.quantity_text_choose.setText(""+quantityBorrow);
                        holder.titleTv.setText(title);
                        holder.descriptionTv.setText(description);
                        holder.dateTv.setText(date);
                        holder.quantityTv.setText(quantity);
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


    class HolderEquipmentCart extends RecyclerView.ViewHolder{
        ProgressBar progressBar;
        ImageView imageView;
        TextView titleTv, descriptionTv, quantityTv, dateTv, categoryTv;
        ImageButton removeCartBtn;
        TextView quantity_text_choose;
        AppCompatButton plus_button, minus_button;
        RadioButton checkIsBorrow;

        public HolderEquipmentCart(@NonNull View itemView) {
            super(itemView);
            progressBar = binding.progressBar;
            imageView = binding.imageView;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            quantityTv = binding.quantityTv;
            dateTv = binding.dateTv;
            categoryTv = binding.categoryTv;
            removeCartBtn = binding.removeCartBtn;
            quantity_text_choose = binding.quantityTextChoose;
            plus_button = binding.plusButton;
            minus_button = binding.minusButton;
            checkIsBorrow = binding.checkIsBorrow;
        }
    }
}
