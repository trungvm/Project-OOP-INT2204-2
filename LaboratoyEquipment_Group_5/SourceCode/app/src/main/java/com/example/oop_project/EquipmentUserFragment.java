package com.example.oop_project;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oop_project.adapters.AdapterEquipmentUser;
import com.example.oop_project.databinding.FragmentEquipmentUserBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EquipmentUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EquipmentUserFragment extends Fragment {
    private String categoryId;
    private String title;
    private String uid;

    private ArrayList<ModelEquipment> equipmentArrayList;
    private AdapterEquipmentUser adapterEquipmentUser;

    private FragmentEquipmentUserBinding binding;

    public EquipmentUserFragment() {
        // Required empty public constructor
    }

    public static EquipmentUserFragment newInstance(String categoryId, String title, String uid) {
        EquipmentUserFragment fragment = new EquipmentUserFragment();
        Bundle args = new Bundle();
        args.putString("categoryId", categoryId);
        args.putString("categoryTitle", title);
        args.putString("uid", uid);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            categoryId = getArguments().getString("categoryId");
            title = getArguments().getString("categoryTitle");
            uid = getArguments().getString("uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentEquipmentUserBinding.inflate(LayoutInflater.from(getContext()), container, false);
        if(title.equals("All")){
            // load add equipments
            loadAllEquipments();
        }else if(title.equals("Most Viewed")){
            loadMostViewedEquipments();
        }else if(title.equals("Most Borrowed")){
            loadMostBorrowedEquipments();
        }else{
            loadCategorizedEquipemnts();
        }

        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterEquipmentUser.getFilter().filter(s);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }

    private void loadCategorizedEquipemnts() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.orderByChild("categoryId").equalTo(categoryId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayList.clear();
                        for (DataSnapshot ds : snapshot.getChildren()) {
                            if(ds.hasChild("status")){
                                if((""+ds.child("status").getValue()).equals("use")){
                                    ModelEquipment model = ds.getValue(ModelEquipment.class);

                                    equipmentArrayList.add(model);
                                }
                            }else{
                                ds.getRef().child("status").setValue("use");
                            }

                        }
                        adapterEquipmentUser = new AdapterEquipmentUser(getContext(), equipmentArrayList);
                        binding.equipmentRv.setAdapter(adapterEquipmentUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void loadMostBorrowedEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.orderByChild("numberOfBorrowings").limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            if(ds.hasChild("status")){
                                if((""+ds.child("status").getValue()).equals("use")){
                                    ModelEquipment model = ds.getValue(ModelEquipment.class);

                                    equipmentArrayList.add(model);
                                }
                            }else{
                                ds.getRef().child("status").setValue("use");
                            }
                        }

                        adapterEquipmentUser = new AdapterEquipmentUser(getContext(), equipmentArrayList);
                        binding.equipmentRv.setAdapter(adapterEquipmentUser);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    
    }

    private void loadMostViewedEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");
        ref.orderByChild("viewed").limitToLast(10)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equipmentArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.hasChild("status")){
                        if((""+ds.child("status").getValue()).equals("use")){
                            ModelEquipment model = ds.getValue(ModelEquipment.class);

                            equipmentArrayList.add(model);
                        }
                    }else{
                        ds.getRef().child("status").setValue("use");
                    }
                }

                adapterEquipmentUser = new AdapterEquipmentUser(getContext(), equipmentArrayList);
                binding.equipmentRv.setAdapter(adapterEquipmentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void loadAllEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Equipments");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equipmentArrayList.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if(ds.hasChild("status")){
                        if((""+ds.child("status").getValue()).equals("use")){
                            ModelEquipment model = ds.getValue(ModelEquipment.class);

                            equipmentArrayList.add(model);
                        }
                    }else{
                        ds.getRef().child("status").setValue("use");
                    }
                }

                adapterEquipmentUser = new AdapterEquipmentUser(getContext(), equipmentArrayList);
                binding.equipmentRv.setAdapter(adapterEquipmentUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}