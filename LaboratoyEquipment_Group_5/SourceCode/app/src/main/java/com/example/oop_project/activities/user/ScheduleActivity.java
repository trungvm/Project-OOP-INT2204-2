package com.example.oop_project.activities.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.adapters.admin.AdapterSchedule;
import com.example.oop_project.databinding.ActivityScheduleBinding;
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

public class ScheduleActivity extends AppCompatActivity {
    private ActivityScheduleBinding binding;
    private ArrayList<ModelEquipment> equipmentArrayList;
    private FirebaseAuth firebaseAuth;
    private AdapterSchedule adapterSchedule;
    private ArrayList<Pair<String, String>> listIdChecked;
    private ArrayList<String> listOfKey;
    private ArrayList<String> listOfEquipmentId;
    private ArrayList<String> listOfTitleEquipment;
    private String equipmentId;
    private String title;
    private String quantityBorrow;
    private boolean flag = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        firebaseAuth = FirebaseAuth.getInstance();
        listIdChecked = new ArrayList<>();
        listOfKey = new ArrayList<>();
        listOfEquipmentId = new ArrayList<>();
        listOfTitleEquipment =  new ArrayList<>();
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadListEquipments();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ACTION_GET_DATASCHEDULE"));
        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                    adapterSchedule.getFilter().filter(s);
                }catch (Exception e){

                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                insertDataToFirebase();
                if(listIdChecked.size() == 0){
                    Toast.makeText(ScheduleActivity.this, "Vui lòng chọn sản phẩm!", Toast.LENGTH_SHORT).show();
                }else{
                    for(int i = 0; i < listIdChecked.size(); i++){
                        if(listIdChecked.get(i).second.equals("0")){
                            flag = false;
                            Toast.makeText(ScheduleActivity.this, "Sản phẩm mượn có số lượng bằng 0", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if(flag == true){
                        Intent intent = new Intent(ScheduleActivity.this, OrderScheduleActivity.class);
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
                            Toast.makeText(ScheduleActivity.this, "Có lỗi xảy ra!", Toast.LENGTH_SHORT).show();

                        }
                    });

        }

    }

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

    private void loadListEquipments() {
        equipmentArrayList = new ArrayList<>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Equipments");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds: snapshot.getChildren()){
                   if(("" + ds.child("status").getValue()).equals("use")){
                       String title = "" + ds.child("title").getValue();
                       String categoryId = "" + ds.child("categoryId").getValue();
                       String equipmentId = "" + ds.child("id").getValue();
                       String timestamp = "" + ds.child("timestamp").getValue();
                       String manual = "" + ds.child("manual").getValue();
                       String description = "" + ds.child("description").getValue();
                       String quantity = "" + ds.child("quantity").getValue();
                       String equipmentImage = "" + ds.child("equipmentImage").getValue();
                       ModelEquipment model = new ModelEquipment();

                       model.setTitle(title);
                       model.setEquipmentImage(equipmentImage);
                       model.setCategoryId(categoryId);
                       model.setId(equipmentId);
                       model.setTimestamp(Long.parseLong(timestamp));
                       model.setDescription(description);
                       model.setManual(manual);
                       model.setQuantity(Integer.parseInt(quantity));
                       equipmentArrayList.add(model);
                   }
                }
                adapterSchedule = new AdapterSchedule(ScheduleActivity.this, equipmentArrayList);
                binding.equipmentRv.setAdapter(adapterSchedule);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}