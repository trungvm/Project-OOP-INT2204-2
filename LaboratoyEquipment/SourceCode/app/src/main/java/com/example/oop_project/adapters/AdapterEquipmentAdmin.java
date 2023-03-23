package com.example.oop_project.adapters;

import android.view.LayoutInflater;
import android.view.View;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import com.example.oop_project.MyApplication;
import com.example.oop_project.databinding.RowEquipmentsBinding;
import com.example.oop_project.filters.FilterEquipment;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AdapterEquipmentAdmin extends RecyclerView.Adapter<AdapterEquipmentAdmin.HolderEquipmentAdmin> implements Filterable {
    // context
    private Context context;
    public ArrayList<ModelEquipment>  equipmentArraylist, filterList;
    private RowEquipmentsBinding binding;
    private FilterEquipment filter;
    public AdapterEquipmentAdmin(Context context, ArrayList<ModelEquipment> equipmentArraylist) {
        this.context = context;
        this.filterList = equipmentArraylist;
        this.equipmentArraylist = equipmentArraylist;
    }

    @NonNull
    @Override
    public HolderEquipmentAdmin onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsBinding.inflate(LayoutInflater.from(context), parent, false);

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

        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.dateTv.setText(formattedDate);

        loadCategory(model, holder);
    }

    private void loadCategory(ModelEquipment model, HolderEquipmentAdmin holder) {
        String categoryId = model.getCategoryId();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.child(categoryId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String category = ""+snapshot.child("title").getValue();

                        holder.categoryTv.setText(category);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return equipmentArraylist.size(); // return number of records
    }

    @Override
    public Filter getFilter() {
        if(filter == null){
            filter = new FilterEquipment(filterList, this);
        }
        return filter;
    }

    // view Holder class
    class HolderEquipmentAdmin extends RecyclerView.ViewHolder{

        // full views
        ProgressBar progressBar;
        TextView titleTv, descriptionTv, categoryTv, dateTv;
        ImageButton moreBtn;
        public HolderEquipmentAdmin(@NonNull View itemView) {
            super(itemView);

            progressBar = binding.progressBar;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            categoryTv = binding.categoryTv;
            dateTv = binding.dateTv;
            moreBtn = binding.moreBtn;
        }
    }
}
