package com.example.oop_project.adapters.admin;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.activities.common.EquipmentDetailActivity;
import com.example.oop_project.activities.admin.EquipmentEditActivity;
import com.example.oop_project.MyApplication;
import com.example.oop_project.databinding.RowEquipmentsAdminBinding;
import com.example.oop_project.filters.admin.FilterEquipment;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdapterEquipmentAdmin extends RecyclerView.Adapter<AdapterEquipmentAdmin.HolderEquipmentAdmin> implements Filterable {
    // context
    private Context context;
    public ArrayList<ModelEquipment> equipmentArraylist, filterList;
    private RowEquipmentsAdminBinding binding;
    private FilterEquipment filter;

    private ProgressDialog progressDialog;

    public AdapterEquipmentAdmin(Context context, ArrayList<ModelEquipment> equipmentArraylist) {
        this.context = context;
        this.filterList = equipmentArraylist;
        this.equipmentArraylist = equipmentArraylist;

        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait!");
        progressDialog.setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public HolderEquipmentAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsAdminBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderEquipmentAdmin(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentAdmin holder, int position) {
        // get date, set data..
        ModelEquipment model = equipmentArraylist.get(position);
        String title = model.getTitle();
        String description = model.getDescription();
        long timestamp = model.getTimestamp();
        String formattedDate = MyApplication.formatTimestamp(timestamp);
        int quantity = model.getQuantity();
        String equipmentImage = model.getEquipmentImage();
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);
        holder.quantityTv.setText(""+quantity);
        loadCategory(model, holder);
        // handle Click, show option 1: Edit, 2 Delete
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreOptionsDialog(model, holder);
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", model.getId());
                if(model.getIsUsedBy().equals("admin")){
                    intent.putExtra("role", model.getIsUsedBy());
                }
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

    private void moreOptionsDialog(ModelEquipment model, HolderEquipmentAdmin holder) {
        // options to show in dialog
        String equipmentId = model.getId();
        String[] options = {"Sửa", "Xóa"};

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Choose Options")
                .setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            // Edit clicked
                            Intent intent = new Intent(context, EquipmentEditActivity.class);
                            intent.putExtra("equipmentId",equipmentId);
                            context.startActivity(intent);
                        } else if (which == 1) {
                            // Delete clicked
                            AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Delete").
                                    setMessage("Are you sure want to delete this equipment?")
                                    .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            // begin delete;
                                            Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                            deleteEquipment(model, holder);
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        }
                    }
                })
                .show();
    }

    private void deleteEquipment(ModelEquipment model, HolderEquipmentAdmin holder) {
        String equipmentId = model.getId();
        String equipmentTitle = model.getTitle();
        progressDialog.setMessage("Deleting...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                progressDialog.dismiss();
                Toast.makeText(context, "Xóa thiết bị thành công!", Toast.LENGTH_SHORT).show();
                snapshot.getRef().child("status").setValue("deleted");
                String equipmentId = "" + snapshot.child("timestamp").getValue();
                DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
                ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if(ds.hasChild("Carts")){
                                DatabaseReference ref2 = ds.child("Carts").getRef();
                                ref2.child(equipmentId).removeValue();
                            }
                            if(ds.hasChild("Favorites")){
                                DatabaseReference ref2 = ds.child("Favorites").getRef();
                                ref2.child(equipmentId).removeValue();
                            }
                        }
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

    private void loadCategory(ModelEquipment model, HolderEquipmentAdmin holder) {
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
    }

    @Override
    public int getItemCount() {
        return equipmentArraylist.size(); // return number of records
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterEquipment(filterList, this);
        }
        return filter;
    }

    // view Holder class
    class HolderEquipmentAdmin extends RecyclerView.ViewHolder {

        // full views
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, dateTv;
        ImageButton moreBtn;
        TextView quantityTv;
        ImageView imageView;
        public HolderEquipmentAdmin(@NonNull View itemView) {
            super(itemView);


            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            moreBtn = binding.moreBtn;
            quantityTv = binding.quantityTv;
            imageView = binding.imageView;
        }
    }
}
