package com.example.oop_project.activities.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.oop_project.EquipmentBorrowedFragment;
import com.example.oop_project.EquipmentUserFragment;
import com.example.oop_project.databinding.ActivityEquipmentUserShowBinding;
import com.example.oop_project.models.ModelCategory;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class EquipmentUserShowActivity extends AppCompatActivity {

    // to show in tabs
    public ArrayList<ModelCategory> categoryArrayList;
    public ViewPagerAdapter viewPagerAdapter;
    private ActivityEquipmentUserShowBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEquipmentUserShowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        setupViewPagerAdapter(binding.viewPager);
        binding.tabLayout.setupWithViewPager(binding.viewPager);
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }
    private void setupViewPagerAdapter(ViewPager viewPager){
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, this);
        categoryArrayList = new ArrayList<>();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Categories");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                categoryArrayList.clear();

                ModelCategory modelAll = new ModelCategory("01", "All", "", 1);
                ModelCategory modelMostViewed = new ModelCategory("02", "Most Viewed", "", 1);
                ModelCategory modelMostBorrowed = new ModelCategory("03", "Most Borrowed", "", 1);

                categoryArrayList.add(modelAll);
                categoryArrayList.add(modelMostViewed);
                categoryArrayList.add(modelMostBorrowed);

                viewPagerAdapter.addFragment(EquipmentUserFragment.newInstance(
                        "" + modelAll.getId(),
                        ""+ modelAll.getTitle(),
                        "" + modelAll.getUid()
                ), modelAll.getTitle());

                viewPagerAdapter.addFragment(EquipmentUserFragment.newInstance(
                        "" + modelMostViewed.getId(),
                        ""+ modelMostViewed.getTitle(),
                        "" + modelMostViewed.getUid()
                ), modelMostViewed.getTitle());

                viewPagerAdapter.addFragment(EquipmentUserFragment.newInstance(
                        "" + modelMostBorrowed.getId(),
                        ""+ modelMostBorrowed.getTitle(),
                        "" + modelMostBorrowed.getUid()
                ), modelMostBorrowed.getTitle());

                // refresh list
                viewPagerAdapter.notifyDataSetChanged();

                for(DataSnapshot ds: snapshot.getChildren()){
                    if(("" + ds.child("status").getValue()).equals("use")){
                        ModelCategory model = ds.getValue(ModelCategory.class);
                        categoryArrayList.add(model);

                        viewPagerAdapter.addFragment(EquipmentUserFragment.newInstance(
                                ""+model.getId(),
                                ""+ model.getTitle(),
                                ""+model.getUid()), model.getTitle());
                        viewPagerAdapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        viewPager.setAdapter(viewPagerAdapter);

    }
    public static class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<EquipmentUserFragment> fragmentList = new ArrayList<>();
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
        private void addFragment(EquipmentUserFragment fragment, String title){
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
