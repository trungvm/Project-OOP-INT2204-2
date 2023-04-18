package com.example.oop_project;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oop_project.activities.EquipmentsBorrowedActivity;
import com.example.oop_project.adapters.AdapterBorrowsAdmin;
import com.example.oop_project.adapters.AdapterEquipmentBorrowed;
import com.example.oop_project.adapters.AdapterEquipmentUser;
import com.example.oop_project.databinding.FragmentEquipmentBorrowedBinding;
import com.example.oop_project.databinding.FragmentEquipmentUserBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EquipmentBorrowedFragment extends Fragment  implements ViewPager.OnPageChangeListener {
    private String categoryId;
    private String title;
    private String uid;
    private Context context;
    private ArrayList<ModelEquipment> equipmentArrayListBorrowing;
    private ArrayList<ModelEquipment> equipmentArrayListBorrowed;
    private AdapterEquipmentBorrowed adapterEquipmentBorrowed;
    private ArrayList<ModelEquipment> equipmentArrayListWaiting;

    private FragmentEquipmentBorrowedBinding binding;
    private FirebaseAuth  firebaseAuth;
    private String preStatus = "";

    //
    public EquipmentBorrowedFragment() {
        // Required empty public constructor
    }


    public static EquipmentBorrowedFragment newInstance(String categoryId, String title, String uid) {
        EquipmentBorrowedFragment fragment = new EquipmentBorrowedFragment();
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
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (title.equals("Đang mượn")) {
                // load report layout + button
                loadBorrowingEquipments();

            } else if (title.equals("Đã mượn")) {
                // hidden report layout + button
                loadBorrowedEquipments();

            }else if(categoryId.equals("03")){
                loadWaitingEquipments();
            }
        } else {
            // Fragment được ẩn đi
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentEquipmentBorrowedBinding.inflate(LayoutInflater.from(getContext()), container, false);
        if (title.equals("Đang mượn")) {
            // load report layout + button
            loadBorrowingEquipments();

        } else if (title.equals("Đã mượn")) {
            // hidden report layout + button
            loadBorrowedEquipments();

        }else if (categoryId.equals("03")){
            loadWaitingEquipments();
        }else if (categoryId.equals("04")){
            loadRefuseEquipments();
        }


        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterEquipmentBorrowed.getFilter().filter(s);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return binding.getRoot();
    }


    private String equipmentId;
    private void loadBorrowingEquipments() {
        firebaseAuth = FirebaseAuth.getInstance();
        equipmentArrayListBorrowing = new ArrayList<>();
        if(equipmentArrayListBorrowing.size() != 0){
            equipmentArrayListBorrowing.clear();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayListBorrowing.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("Borrowed")){
                                String key = ds.getKey();
                                preStatus = "";
                                equipmentId = "" + ds.child("equipmentId").getValue();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
                                reference.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                                        model.setKey(key);
                                        model.setStatus("Borrowed");
                                        if(ds.hasChild("preStatus")){
                                            preStatus = "" + ds.child("preStatus").getValue();
                                        }
                                        model.setPreStatus(preStatus);
                                        equipmentArrayListBorrowing.add(model);
                                        adapterEquipmentBorrowed.notifyDataSetChanged();
                                        binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);
                                        preStatus = "";


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }

                        }
                        if (adapterEquipmentBorrowed == null) {
                            adapterEquipmentBorrowed = new AdapterEquipmentBorrowed(getContext(), equipmentArrayListBorrowing);
                            binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

    }

    private void loadBorrowedEquipments() {
        firebaseAuth = FirebaseAuth.getInstance();
        equipmentArrayListBorrowed = new ArrayList<>();
        if(equipmentArrayListBorrowed.size() != 0 ){
            equipmentArrayListBorrowed.clear();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayListBorrowed.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("History")){
                                String key = ds.getKey();
                                equipmentId = "" + ds.child("equipmentId").getValue();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
                                reference.child(equipmentId).keepSynced(true);
                                reference.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                                        model.setStatus("History");
                                        model.setKey(key);
                                        if(ds.hasChild("preStatus")){
                                            preStatus = "" + ds.child("preStatus").getValue();
                                        }
                                        model.setPreStatus(preStatus);
                                        preStatus = "";
                                        equipmentArrayListBorrowed.add(model);
                                        adapterEquipmentBorrowed.notifyDataSetChanged();
                                        binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }

                        }
                        if (adapterEquipmentBorrowed == null) {
                            adapterEquipmentBorrowed = new AdapterEquipmentBorrowed(getContext(), equipmentArrayListBorrowed);
                            binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);
                            adapterEquipmentBorrowed.notifyDataSetChanged();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });


    }
    private void loadWaitingEquipments(){
        firebaseAuth = FirebaseAuth.getInstance();
        equipmentArrayListWaiting = new ArrayList<>();
        if(equipmentArrayListWaiting.size() != 0){
            equipmentArrayListWaiting.clear();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayListWaiting.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("Waiting")){
                                String key = ds.getKey();
                                equipmentId = "" + ds.child("equipmentId").getValue();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
                                reference.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                                        model.setKey(key);
                                        model.setStatus("Waiting");
                                        equipmentArrayListWaiting.add(model);
                                        adapterEquipmentBorrowed.notifyDataSetChanged();
                                        binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }

                        }
                        if (adapterEquipmentBorrowed == null) {
                            adapterEquipmentBorrowed = new AdapterEquipmentBorrowed(getContext(), equipmentArrayListWaiting);
                            binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });

    }
    private  void loadRefuseEquipments(){
        firebaseAuth = FirebaseAuth.getInstance();
        equipmentArrayListWaiting = new ArrayList<>();
        if(equipmentArrayListWaiting.size() != 0){
            equipmentArrayListWaiting.clear();
        }
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Borrowed")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayListWaiting.clear();
                        for(DataSnapshot ds : snapshot.getChildren()){
                            if((""+ds.child("status").getValue()).equals("Refuse")){
                                String key = ds.getKey();
                                equipmentId = "" + ds.child("equipmentId").getValue();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
                                reference.child(equipmentId).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                                        model.setKey(key);
                                        model.setStatus("Refuse");
                                        equipmentArrayListWaiting.add(model);
                                        adapterEquipmentBorrowed.notifyDataSetChanged();
                                        binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);


                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                            }

                        }
                        if (adapterEquipmentBorrowed == null) {
                            adapterEquipmentBorrowed = new AdapterEquipmentBorrowed(getContext(), equipmentArrayListWaiting);
                            binding.equipmentRv.setAdapter(adapterEquipmentBorrowed);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Toast.makeText(context, "test", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}