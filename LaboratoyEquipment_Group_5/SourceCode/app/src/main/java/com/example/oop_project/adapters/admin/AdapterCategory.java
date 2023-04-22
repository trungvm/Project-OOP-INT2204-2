package com.example.oop_project.adapters.admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.oop_project.activities.admin.EquipmentListAdminActivity;
import com.example.oop_project.databinding.RowCategoryBinding;
import com.example.oop_project.filters.admin.FilterCategory;
import com.example.oop_project.models.ModelCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.HolderCategory> implements Filterable {

    public ArrayList<ModelCategory> categoryArrayList, filterList;
    private final Context context;
    // view binding
    private RowCategoryBinding binding;
    private FilterCategory filter;

    public AdapterCategory(Context context, ArrayList<ModelCategory> categoryArrayList) {
        this.context = context;
        this.categoryArrayList = categoryArrayList;
        this.filterList = categoryArrayList;
    }

    @NonNull
    @Override
    public HolderCategory onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowCategoryBinding.inflate(LayoutInflater.from(context), parent, false);

        return new HolderCategory(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderCategory holder, int position) {
        // getdata
        ModelCategory category = categoryArrayList.get(position);
        String id = category.getId();
        String title = category.getTitle();
        String uid = category.getUid();
        long timestamp = category.getTimestamp();


        //set data
        holder.categoryTv.setText(title);

        // holder click delete category
        holder.deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete").
                        setMessage("Are you sure want to delete this category?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // begin delete;
                                Toast.makeText(context, "Deleting...", Toast.LENGTH_SHORT).show();
                                deleteCategory(category, holder);
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
        });
        // handle item click, go ListEquipmentAdmin
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentListAdminActivity.class);
                intent.putExtra("categoryId", id);
                intent.putExtra("categoryTitle", title);
                context.startActivity(intent);
            }
        });
    }

    private void deleteCategory(ModelCategory category, HolderCategory holder) {
        String id = category.getId();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(id)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        snapshot.getRef().child("status").setValue("deleted");
                        Toast.makeText(context, "Xóa thành công", Toast.LENGTH_SHORT).show();
                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
                        reference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot ds : snapshot.getChildren()) {
                                    if (("" + ds.child("categoryId").getValue()).equals(id)) {
                                        ds.getRef().child("status").setValue("deleted");
                                        String equipmentId = "" + ds.child("timestamp").getValue();
                                        DatabaseReference ref1 = FirebaseDatabase.getInstance().getReference("Users");
                                        ref1.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                for (DataSnapshot ds : snapshot.getChildren()) {
                                                    if (ds.hasChild("Carts")) {
                                                        DatabaseReference ref2 = ds.child("Carts").getRef();
                                                        ref2.child(equipmentId).removeValue();
                                                    }
                                                    if (ds.hasChild("Favorites")) {
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

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show();
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
        return categoryArrayList != null ? categoryArrayList.size() : 0;
    }

    @Override
    public Filter getFilter() {
        if (filter == null) {
            filter = new FilterCategory(filterList, this);
        }
        return filter;
    }

    // View holder class to hold UI views for row_category
    class HolderCategory extends RecyclerView.ViewHolder {

        // ui views of row_category.xml
        TextView categoryTv;
        ImageButton deleteBtn;

        public HolderCategory(@NonNull View itemView) {
            super(itemView);
            categoryTv = binding.categoryTv;
            deleteBtn = binding.deleteBtn;

        }
    }
}
