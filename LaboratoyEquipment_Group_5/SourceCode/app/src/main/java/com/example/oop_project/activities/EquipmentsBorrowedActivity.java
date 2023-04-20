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
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.oop_project.EquipmentBorrowedFragment;
import com.example.oop_project.databinding.ActivityEquipmentsBorrowedBinding;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.example.oop_project.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class EquipmentsBorrowedActivity extends AppCompatActivity{
    public ArrayList<ModelCategory> categoryArrayList;
    public EquipmentsBorrowedActivity.ViewPagerAdapter viewPagerAdapter;
    private ActivityEquipmentsBorrowedBinding binding;
    private FirebaseAuth firebaseAuth;
    private ArrayList<String> equipmentKeyArrayList;
    private long timestamp;
    private String reportHistory;
    private String titleEquipment = "";
    private ArrayList<String> equipmentIdArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentsBorrowedBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        firebaseAuth = FirebaseAuth.getInstance();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter("ACTION_GET_DATA"));
        equipmentKeyArrayList = new ArrayList<>();
        equipmentIdArrayList = new ArrayList<>();



        setupViewPagerAdapter(binding.viewPagerBorrowed);
        binding.tabLayout.setupWithViewPager(binding.viewPagerBorrowed);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();

            }
        });
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                switch (position){
                    case 0:
                        binding.reportLayout.setVisibility(View.VISIBLE);
                        binding.submitBtn.setVisibility(View.VISIBLE);
                        int heightInPx = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 450, getResources().getDisplayMetrics());
                        binding.viewPagerBorrowed.getLayoutParams().height = heightInPx;
                        binding.viewPagerBorrowed.requestLayout();
                        break;
                    case 1:
                        binding.reportLayout.setVisibility(View.GONE);
                        binding.submitBtn.setVisibility(View.GONE);
                        int heightInPxt = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 1000, getResources().getDisplayMetrics());
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
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
                sendMail();
                startActivity(new Intent(EquipmentsBorrowedActivity.this, SuccessActivity.class));
                finish();
            }
        });



    }



    private void validateData() {
        // cài đặt lại logic, chọn cái nào thì update cái đó trong database và reload lại thiết bị
        if(equipmentKeyArrayList.size() == 0){
            Toast.makeText(this, "Vui lòng chọn thiết bị!", Toast.LENGTH_SHORT).show();
        }else{
            timestamp = System.currentTimeMillis();
            reportHistory = binding.reportEt.getText().toString().trim();
            for(int i = 0; i < equipmentKeyArrayList.size(); i++){
                    String key = equipmentKeyArrayList.get(i);
                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                    ref.child(firebaseAuth.getUid())
                            .child("Borrowed")
                            .child(key)
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    DatabaseReference reference = snapshot.child("status").getRef();
                                    reference.setValue("History");
                                    String equipmentId = "" + snapshot.child("equipmentId").getValue();
                                    equipmentIdArrayList.add(equipmentId);
                                    int quantityBorrowed = Integer.parseInt(""+snapshot.child("quantityBorrowed").getValue());
                                    DatabaseReference refE = FirebaseDatabase.getInstance().getReference("Equipments");
                                    refE.child(equipmentId)
                                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    int quantity = Integer.parseInt("" + snapshot.child("quantity").getValue());
                                                    DatabaseReference refQ = snapshot.child("quantity").getRef();
                                                    refQ.setValue(quantity + quantityBorrowed);
                                                    String title = "" + snapshot.child("title").getValue();
                                                    titleEquipment += title + "\n";


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
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("reportHistory", reportHistory);
                    hashMap.put("timestampReturn", timestamp);
                    hashMap.put("status", "History");
                    DatabaseReference refEquipmentBorrowed = FirebaseDatabase.getInstance().getReference("EquipmentsBorrowed");
                    refEquipmentBorrowed.child(key)
                            .updateChildren(hashMap)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {


                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
            }

            }
    }
    private void sendMail(){
        ModelEquipment model = new ModelEquipment();
        for(int i = 0; i < equipmentIdArrayList.size(); i++){
            model.getTitle(equipmentIdArrayList.get(i)).addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if(task.isSuccessful()){
                        titleEquipment += task.getResult();
                    }else{
                        Exception exception = task.getException();
                    }
                }
            });
        }
        User user = new User();
        user.getFullName(firebaseAuth.getUid()).addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {
                if (task.isSuccessful()) {
                    String  fullName = task.getResult();
                    String subject = "Trả thiết bị thành công!";
                    user.setFullName(fullName);
                    String message = "Chúc mừng " + fullName + " đã trả thành công thiết bị từ chúng tôi!" +
                            "\n" + "Danh sách thiết bị gồm: "+ "\n"
                            + titleEquipment  + "Báo cáo về thiết bị lúc trả: " + reportHistory
                            +"\n" + "Chúc bạn hoàn thành tốt công việc";
                    user.sendMail(EquipmentsBorrowedActivity.this, firebaseAuth.getUid(),subject, message);
                } else {
                    Exception exception = task.getException();
                    // Xử lý lỗi ở đây
                }
            }
        });
    }

    public BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
           String equipmentId = intent.getStringExtra("equipmentId");
           String uid = intent.getStringExtra("uid");
           String isChecked = intent.getStringExtra("isChecked");
           String key = intent.getStringExtra("key");
           if(isChecked.equals("true")){
                equipmentKeyArrayList.add(key);
           }else{
               int index = equipmentKeyArrayList.indexOf(key);
               if(index != -1){
                   equipmentKeyArrayList.remove(index);

               }
           }


        }
    };
    private void setupViewPagerAdapter(ViewPager viewPager){

        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        categoryArrayList = new ArrayList<>();

        categoryArrayList.clear();
        ModelCategory modelBorrowing = new ModelCategory("01", "Đang mượn", "", 1);
        ModelCategory modelBorrowed = new ModelCategory("02", "Đã mượn", "", 1);
        ModelCategory modelWaiting = new ModelCategory("03", "Đang xử lý", "", 1);
        ModelCategory modelRefuse = new ModelCategory("04", "Đã bị hủy", "", 1);

        categoryArrayList.add(modelBorrowing);
        categoryArrayList.add(modelBorrowed);
        categoryArrayList.add(modelWaiting);
        categoryArrayList.add(modelRefuse);
        viewPagerAdapter.addFragment(EquipmentBorrowedFragment.newInstance(
                "" + modelBorrowing.getId(),
                ""+ modelBorrowing.getTitle(),
                "" + modelBorrowing.getUid()
        ), modelBorrowing.getTitle());

        viewPagerAdapter.addFragment(EquipmentBorrowedFragment.newInstance(
                "" + modelBorrowed.getId(),
                ""+ modelBorrowed.getTitle(),
                "" + modelBorrowed.getUid()
        ), modelBorrowed.getTitle());
        viewPagerAdapter.addFragment(EquipmentBorrowedFragment.newInstance(
                "" + modelWaiting.getId(),
                ""+ modelWaiting.getTitle(),
                "" + modelWaiting.getUid()
        ), modelWaiting.getTitle());
        viewPagerAdapter.addFragment(EquipmentBorrowedFragment.newInstance(
                "" + modelRefuse.getId(),
                ""+ modelRefuse.getTitle(),
                "" + modelRefuse.getUid()
        ), modelRefuse.getTitle());

        // refresh list
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(viewPagerAdapter);

    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<EquipmentBorrowedFragment> fragmentList = new ArrayList<>();
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
        private void addFragment(EquipmentBorrowedFragment fragment, String title){
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
