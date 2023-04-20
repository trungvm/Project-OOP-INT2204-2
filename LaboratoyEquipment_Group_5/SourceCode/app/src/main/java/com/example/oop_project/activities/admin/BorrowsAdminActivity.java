package com.example.oop_project.activities.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.oop_project.BorrowsAdminFragment;
import com.example.oop_project.databinding.ActivityBorrowsAdminBinding;
import com.example.oop_project.models.ModelCategory;

import java.util.ArrayList;

public class BorrowsAdminActivity extends AppCompatActivity {
    public ArrayList<ModelCategory> categoryArrayList;
    public ViewPagerAdapter viewPagerAdapter;
    private ActivityBorrowsAdminBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBorrowsAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupViewPagerAdapter(binding.viewPagerBorrowed);
        binding.tabLayout.setupWithViewPager(binding.viewPagerBorrowed);
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

        categoryArrayList.clear();
        ModelCategory modelBorrowing = new ModelCategory("01", "Đang mượn", "", 1);
        ModelCategory modelBorrowed = new ModelCategory("02", "Đã mượn", "", 1);
        ModelCategory modelRefuse = new ModelCategory("03", "Đã hủy", "", 1);

        categoryArrayList.add(modelBorrowing);
        categoryArrayList.add(modelBorrowed);
        categoryArrayList.add(modelRefuse);
        viewPagerAdapter.addFragment(BorrowsAdminFragment.newInstance(
                "" + modelBorrowing.getId(),
                ""+ modelBorrowing.getTitle(),
                "" + modelBorrowing.getUid()
        ), modelBorrowing.getTitle());

        viewPagerAdapter.addFragment(BorrowsAdminFragment.newInstance(
                "" + modelBorrowed.getId(),
                ""+ modelBorrowed.getTitle(),
                "" + modelBorrowed.getUid()
        ), modelBorrowed.getTitle());
        viewPagerAdapter.addFragment(BorrowsAdminFragment.newInstance(
                "" + modelRefuse.getId(),
                ""+ modelRefuse.getTitle(),
                "" + modelRefuse.getUid()
        ), modelRefuse.getTitle());

        // refresh list
        viewPagerAdapter.notifyDataSetChanged();

        viewPager.setAdapter(viewPagerAdapter);
        viewPagerAdapter.notifyDataSetChanged();


    }


    public class ViewPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<BorrowsAdminFragment> fragmentList = new ArrayList<>();
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
        private void addFragment(BorrowsAdminFragment fragment, String title){
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
