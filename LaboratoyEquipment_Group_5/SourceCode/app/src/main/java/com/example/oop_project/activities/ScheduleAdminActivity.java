package com.example.oop_project.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.View;
import android.widget.Toast;

import com.example.oop_project.BorrowsAdminFragment;
import com.example.oop_project.ScheduleAdminFragment;
import com.example.oop_project.adapters.AdapterBorrowsAdmin;
import com.example.oop_project.adapters.AdapterSchedule;
import com.example.oop_project.adapters.AdapterScheduleAdmin;
import com.example.oop_project.databinding.ActivityScheduleAdminBinding;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class ScheduleAdminActivity extends AppCompatActivity {
    private ActivityScheduleAdminBinding binding;
    public ArrayList<ModelCategory> categoryArrayList;
    public ViewPagerAdapter viewPagerAdapter;
    private ArrayList<Pair<Pair<String, String>, String>> listIdChecked;
    private int cntRefuse;
    private int cntAccpet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listIdChecked = new ArrayList<>();
        setupViewPagerAdapter(binding.viewPagerBorrowed);
        binding.tabLayout.setupWithViewPager(binding.viewPagerBorrowed);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ACTION_GET_DATA"));
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScheduleAdminActivity.this, DashboardAdminActivity.class));
                finish();
            }
        });
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = true;
                counting();
                validateRefuseData();
                if(flag == true){
                    Toast.makeText(ScheduleAdminActivity.this, "Xác nhận hủy thành công", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(ScheduleAdminActivity.this, SuccessActivity.class);
                    intent.putExtra("status", "ScheduleRefuse");
                    startActivity(intent);
                    finish();
                }
            }
        });

        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flag = true;
                                counting();
                                validateRefuseData();
                                if(flag == true){
                                    Toast.makeText(ScheduleAdminActivity.this, "Xác nhận hủy thành công", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ScheduleAdminActivity.this, SuccessActivity.class);
                                    intent.putExtra("status", "ScheduleRefuse");
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                        binding.reportLayout.setVisibility(View.VISIBLE);
                        binding.submitBtn.setVisibility(View.VISIBLE);
                        int heightInPx = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                        binding.viewPagerBorrowed.getLayoutParams().height = heightInPx;
                        binding.viewPagerBorrowed.requestLayout();
                        break;
                    case 1:
                        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                flag = true;
                                counting();
                                validateAcceptData();
                                if(flag == true){
                                    Toast.makeText(ScheduleAdminActivity.this, "Xác nhận thành công!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(ScheduleAdminActivity.this, SuccessActivity.class);
                                    intent.putExtra("status", "ScheduleAccept");
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        });
                        binding.reportLayout.setVisibility(View.GONE);
                        binding.submitBtn.setVisibility(View.VISIBLE);
                        int heightInPxt = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 550, getResources().getDisplayMetrics());
                        binding.viewPagerBorrowed.getLayoutParams().height = heightInPxt;
                        binding.viewPagerBorrowed.requestLayout();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    String reportRefuse;
    boolean flag = true;
    private void counting(){
        cntRefuse = 0;
        cntAccpet = 0;
        for(int i =0 ; i < listIdChecked.size(); i++){
            String status = listIdChecked.get(i).second;
            if(status.equals("Refuse")){
                cntRefuse ++;
            }else cntAccpet ++;
        }
    }
    private void validateRefuseData() {
        reportRefuse = binding.reportEt.getText().toString().trim();
        if(cntRefuse == 0 ){
            Toast.makeText(this, "Vui lòng chọn thiết bị", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else if(TextUtils.isEmpty(reportRefuse)){
            Toast.makeText(ScheduleAdminActivity.this, "Điền lý do!", Toast.LENGTH_SHORT).show();
            flag = false;
        }else{
            Long timestamp = System.currentTimeMillis();
            for(int i = 0; i < listIdChecked.size(); i++){
                String status =  listIdChecked.get(i).second;
                if(status.equals("Refuse")){
                    String equipmentId = listIdChecked.get(i).first.first;
                    String key = listIdChecked.get(i).first.second;
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                    ref.child(key)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.hasChild("reportRefuse")){
                                        snapshot.getRef().child("reportRefuse").setValue(reportRefuse);
                                    }else{
                                        HashMap<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("reportRefuse", reportRefuse);
                                        snapshot.getRef().updateChildren(childUpdates);
                                    }
                                    if(snapshot.hasChild("timestampAdminSchedue")){
                                        snapshot.getRef().child("timestampAdminSchedue").setValue(timestamp);
                                    }else{
                                        HashMap<String, Object> childUpdates = new HashMap<>();
                                        childUpdates.put("timestampAdminSchedue", timestamp);
                                        snapshot.getRef().updateChildren(childUpdates);
                                    };
                                    snapshot.getRef().child("status").setValue("Refuse");
                                    String uid =  "" + snapshot.child("uid").getValue();
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                    reference.child(uid).child("Borrowed").child(key)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    snapshot.getRef().child("status").setValue("Refuse");
                                                    if(snapshot.hasChild("timestampAdminSchedue")){
                                                        snapshot.getRef().child("timestampAdminSchedue").setValue(timestamp);
                                                    }else{
                                                        HashMap<String, Object> childUpdates = new HashMap<>();
                                                        childUpdates.put("timestampAdminSchedue", timestamp);
                                                        snapshot.getRef().updateChildren(childUpdates);
                                                    }
                                                    int quantityBorrowed = Integer.parseInt("" + snapshot.child("quantityBorrowed").getValue());
                                                    DatabaseReference ref1 =  FirebaseDatabase.getInstance().getReference("Equipments");
                                                    ref1.child(equipmentId)
                                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                                    int x = Integer.parseInt(""+ snapshot.child("quantity").getValue());
                                                                    x = x + quantityBorrowed;
                                                                    snapshot.getRef().child("quantity").setValue(x);
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
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                }
            }
        }
    }
    private void validateAcceptData(){
        if(cntAccpet == 0 ){
            Toast.makeText(this, "Vui lòng chọn thiết bị", Toast.LENGTH_SHORT).show();
            flag = false;
        }
        else{
        Long timestamp = System.currentTimeMillis();
        for(int i = 0; i < listIdChecked.size(); i++) {
            String status = listIdChecked.get(i).second;
            if (status.equals("Accept")) {
                String equipmentId = listIdChecked.get(i).first.first;
                String key = listIdChecked.get(i).first.second;
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                ref.child(key)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.hasChild("timestampAdminSchedue")) {
                                    snapshot.getRef().child("timestampAdminSchedue").setValue(timestamp);
                                } else {
                                    HashMap<String, Object> childUpdates = new HashMap<>();
                                    childUpdates.put("timestampAdminSchedue", timestamp);
                                    snapshot.getRef().updateChildren(childUpdates);
                                }
                                snapshot.getRef().child("status").setValue("Borrowed");
                                String uid = "" + snapshot.child("uid").getValue();
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
                                reference.child(uid).child("Borrowed").child(key)
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                snapshot.getRef().child("status").setValue("Borrowed");
                                                if (snapshot.hasChild("timestampAdminSchedue")) {
                                                    snapshot.getRef().child("timestampAdminSchedue").setValue(timestamp);
                                                } else {
                                                    HashMap<String, Object> childUpdates = new HashMap<>();
                                                    childUpdates.put("timestampAdminSchedue", timestamp);
                                                    snapshot.getRef().updateChildren(childUpdates);
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
        }
        }

    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String equipmentId = intent.getStringExtra("equipmentId");
            String timestamp = intent.getStringExtra("timestamp");
            String key = intent.getStringExtra("key");
            String adminStatus = intent.getStringExtra("adminStatus");
            ModelEquipment model = new ModelEquipment();
            model.setId(equipmentId);
            model.setTimestamp(Long.parseLong(timestamp));
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
                    Pair<String, String> p = new Pair<>(equipmentId, key);
                    Pair<Pair<String, String>, String> p1 =  new Pair<>(p, adminStatus);
                    listIdChecked.set(index, p1);
                }else{
                    Pair<String, String> p = new Pair<>(equipmentId, key);
                    Pair<Pair<String, String>, String> p1 =  new Pair<>(p, adminStatus);
                    listIdChecked.add(p1);
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
                }
            }

        }
    };

    private void setupViewPagerAdapter(ViewPager viewPager){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        categoryArrayList = new ArrayList<>();

        categoryArrayList.clear();
        ModelCategory modelBorrowing = new ModelCategory("01", "Hủy", "", 1);
        ModelCategory modelBorrowed = new ModelCategory("02", "Đồng ý", "", 1);

        categoryArrayList.add(modelBorrowing);
        categoryArrayList.add(modelBorrowed);
        viewPagerAdapter.addFragment(ScheduleAdminFragment.newInstance(
                "" + modelBorrowing.getId(),
                ""+ modelBorrowing.getTitle(),
                "" + modelBorrowing.getUid()
        ), modelBorrowing.getTitle());

        viewPagerAdapter.addFragment(ScheduleAdminFragment.newInstance(
                "" + modelBorrowed.getId(),
                ""+ modelBorrowed.getTitle(),
                "" + modelBorrowed.getUid()
        ), modelBorrowed.getTitle());

        // refresh list
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();


    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<ScheduleAdminFragment> fragmentList = new ArrayList<>();
        private ArrayList<String> fragmentTitleList = new ArrayList<>();
        private Context context;


        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
            super(fm);
        }

        public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Context context) {
            super(fm, behavior);
            this.context = context;
        }


        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
        private void addFragment(ScheduleAdminFragment fragment, String title){
            fragmentList.add(fragment);
            fragmentTitleList.add(title);

        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}
