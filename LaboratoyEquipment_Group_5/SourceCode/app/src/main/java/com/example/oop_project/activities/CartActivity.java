package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.adapters.AdapterEquipmentCart;
import com.example.oop_project.databinding.ActivityCartBinding;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class CartActivity extends AppCompatActivity  {
    private ActivityCartBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private AdapterEquipmentCart adapterEquipmentCart;
    private FirebaseAuth firebaseAuth;
    private String equipmentId;
    private String title;
    private String quantityBorrow;
    private ArrayList<Pair<String, String>> listIdChecked;
    private ArrayList<String> listOfKey;
    private ArrayList<String> listOfEquipmentId;
    private ArrayList<String> listOfTitleEquipment;
    private boolean flag = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listIdChecked = new ArrayList<>();
        listOfKey = new ArrayList<>();
        listOfEquipmentId = new ArrayList<>();
        listOfTitleEquipment =  new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CartActivity.this, DashboardUserActivity.class));
                finish();
            }
        });
        LoadCartEquipments();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ACTION_GET_DATA"));

        binding.borrowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                insertDataToFirebase();
                if(listIdChecked.size() == 0){
                    Toast.makeText(CartActivity.this, "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0; i < listIdChecked.size(); i++){
                        if(listIdChecked.get(i).second.equals("0")){
                            flag = false;
                            Toast.makeText(CartActivity.this, "Sản phẩm mượn có số lượng bằng 0", Toast.LENGTH_SHORT).show();
                        }
                    }
                   if(flag == true){
                       Intent intent = new Intent(CartActivity.this, OrderActivity.class);
                       intent.putStringArrayListExtra("listOfKey", listOfKey);
                       intent.putStringArrayListExtra("listOfEquipmentId", listOfEquipmentId);
                       intent.putStringArrayListExtra("listOfTitleEquipment", listOfTitleEquipment);
                       startActivity(intent);
                       finish();
                   }
                }

            }
        });
    }

    private void insertDataToFirebase() {
        long timestamp = System.currentTimeMillis();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        for(int i = 0; i < listIdChecked.size(); i++){
            String key = ref.push().getKey();
            listOfKey.add("HH"+key);
            listOfEquipmentId.add(listIdChecked.get(i).first);
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("equipmentId", listIdChecked.get(i).first);
            hashMap.put("quantityBorrowed", listIdChecked.get(i).second);
            hashMap.put("status", "new");
            hashMap.put("timestamp", ""+timestamp);
            ref.child(firebaseAuth.getUid())
                    .child("Borrowed")
                    .child("HH"+key)
                    .setValue(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CartActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }

    String preIsChecked;
    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            equipmentId = intent.getStringExtra("equipmentId");
            title = intent.getStringExtra("equipmentName");
            quantityBorrow = intent.getStringExtra("quantityBorrowed");
            ModelEquipment model = new ModelEquipment();
            model.setId(equipmentId);
            model.setTitle(title);
            model.setQuantityBorrow(Integer.parseInt(quantityBorrow != null ? quantityBorrow : "0"));
            String isChecked = intent.getStringExtra("isChecked");
            boolean flag = true;
            int index = -1;
            if (isChecked.equals("true")) {
                for (int i = 0; i < listIdChecked.size(); i++) {
                    if (listIdChecked.get(i).first.equals(equipmentId)) {
                        flag = false;
                        index = i;
                        break;
                    }
                }
                if(flag == false){
                    Pair<String, String> p = new Pair<>(equipmentId, quantityBorrow);
                    listIdChecked.set(index, p);
                    listOfTitleEquipment.set(index, title);
                }else{
                    Pair<String, String> p = new Pair<>(equipmentId, quantityBorrow);
                    listIdChecked.add(p);
                    listOfTitleEquipment.add(title);
                }

            }else{
                for (int i = 0; i < listIdChecked.size(); i++) {
                    if (listIdChecked.get(i).first.equals(equipmentId)) {
                        flag = false;
                        index = i;
                        break;
                    }
                }
                if(flag == false){
                    listIdChecked.remove(index);
                    listOfTitleEquipment.remove(index);
                }
            }

        }
    };
    private void LoadCartEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .child("Carts")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        equipmentArrayList.clear();
                        for(DataSnapshot ds: snapshot.getChildren()){
                            String equipmentId = ""+ds.child("equipmentId").getValue();
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
                            reference.child(equipmentId)
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            ModelEquipment model = snapshot.getValue(ModelEquipment.class);
                                            equipmentArrayList.add(model);
                                            adapterEquipmentCart.notifyDataSetChanged();
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                        }
                        adapterEquipmentCart = new AdapterEquipmentCart(CartActivity.this, equipmentArrayList);
                        binding.cartRv.setAdapter(adapterEquipmentCart);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }


}