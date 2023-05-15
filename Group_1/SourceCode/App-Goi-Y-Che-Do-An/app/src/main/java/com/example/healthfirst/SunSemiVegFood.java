package com.example.healthfirst;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class SunSemiVegFood extends AppCompatActivity {
    public RecyclerView recyclerView;
    public ArrayList<Modal> datalist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sun_semi_veg_food);
        recyclerView=findViewById(R.id.rv);

        datalist=new ArrayList<>();

        datalist.add(new Modal(R.drawable.pineapple1,"Dứa"));
        datalist.add(new Modal(R.drawable.passion1,"Chanh dây"));
        datalist.add(new Modal(R.drawable.pomegranate1,"Thạch lựu"));
        datalist.add(new Modal(R.drawable.watermelon1,"Dưa hấu"));
        datalist.add(new Modal(R.drawable.turkey1,"Gà tây"));
        datalist.add(new Modal(R.drawable.shrimp1,"Tôm sú"));


        //datalist.add(new Modal(R.drawable.mobile));




        RecyclerViewAdapter adapter=new RecyclerViewAdapter(this,datalist);
        GridLayoutManager layoutManager=new GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }
}