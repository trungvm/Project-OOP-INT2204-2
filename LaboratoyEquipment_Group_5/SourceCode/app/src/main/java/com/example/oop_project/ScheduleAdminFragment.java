package com.example.oop_project;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.oop_project.adapters.user.AdapterScheduleAdmin;
import com.example.oop_project.databinding.FragmentScheduleAdminBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScheduleAdminFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScheduleAdminFragment extends Fragment {

    private String categoryId;
    private String title;
    private String uid;
    private Context context;
    private FragmentScheduleAdminBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayListAccept;
    private ArrayList<ModelEquipment> equipmentArrayListRefuse;
    private AdapterScheduleAdmin adapterScheduleAdmin;
    public ScheduleAdminFragment() {
        // Required empty public constructor
    }

    public static ScheduleAdminFragment newInstance(String categoryId, String title, String uid) {
        ScheduleAdminFragment fragment = new ScheduleAdminFragment();
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
        binding = FragmentScheduleAdminBinding.inflate(LayoutInflater.from(getContext()), container, false);
        if (categoryId.equals("02")) {
            // load report layout + button
            loadAcceptEquipments();

        } else if (categoryId.equals("01")) {
            // hidden report layout + button
            loadRefuseEquipments();

        }
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterScheduleAdmin.getFilter().filter(s);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }

    public void loadAcceptEquipments() {
        equipmentArrayListAccept = new ArrayList<>();
        if(equipmentArrayListAccept.size() != 0){
            equipmentArrayListAccept.clear();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equipmentArrayListAccept.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if((""+ds.child("status").getValue()).equals("Waiting")){
                        String key = ds.getKey();
                        String uid = "" + ds.child("uid").getValue();
                        ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                        String equipmentId = "" + ds.child("equipmentId").getValue();
                        String title = "" + ds.child("title").getValue();
                        String timestamp = "" + ds.child("timestamp").getValue();

                        model.setTitle(title);
                        model.setTimestamp(Long.parseLong(timestamp));
                        model.setId(equipmentId);
                        model.setKey(key);
                        model.setUid(uid);
                        model.setAdminStatus("Accept");
                        model.setStatus("Waiting");
                        equipmentArrayListAccept.add(model);

                    }
                }
                Collections.sort(equipmentArrayListAccept, new Comparator<ModelEquipment>() {
                    @Override
                    public int compare(ModelEquipment o1, ModelEquipment o2) {
                        // Sử dụng trường "ti" (timestamp) để so sánh
                        long ti1 = o1.getTimestamp();
                        long ti2 = o2.getTimestamp();
                        return Long.compare(ti1, ti2);
                    }
                });

                adapterScheduleAdmin = new AdapterScheduleAdmin(getContext(), equipmentArrayListAccept);
                adapterScheduleAdmin.notifyDataSetChanged();
                binding.equipmentRv.setAdapter(adapterScheduleAdmin);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    public void loadRefuseEquipments() {
        equipmentArrayListRefuse = new ArrayList<>();
        if(equipmentArrayListRefuse.size() != 0){
            equipmentArrayListRefuse.clear();
        }
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                equipmentArrayListRefuse.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    if((""+ds.child("status").getValue()).equals("Waiting")){
                        String key = ds.getKey();
                        String uid = "" + ds.child("uid").getValue();
                        ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                        String equipmentId = "" + ds.child("equipmentId").getValue();
                        String title = "" + ds.child("title").getValue();
                        model.setTitle(title);
                        String timestamp = "" + ds.child("timestamp").getValue();
                        model.setTimestamp(Long.parseLong(timestamp));
                        model.setId(equipmentId);
                        model.setKey(key);
                        model.setUid(uid);
                        model.setStatus("Waiting");
                        model.setAdminStatus("Refuse");
                        equipmentArrayListRefuse.add(model);



                    }
                }
                Collections.sort(equipmentArrayListRefuse, new Comparator<ModelEquipment>() {
                    @Override
                    public int compare(ModelEquipment o1, ModelEquipment o2) {
                        // Sử dụng trường "ti" (timestamp) để so sánh
                        long ti1 = o1.getTimestamp();
                        long ti2 = o2.getTimestamp();
                        return Long.compare(ti1, ti2);
                    }
                });

                adapterScheduleAdmin = new AdapterScheduleAdmin(getContext(), equipmentArrayListRefuse);
                adapterScheduleAdmin.notifyDataSetChanged();
                binding.equipmentRv.setAdapter(adapterScheduleAdmin);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}