package com.example.healthfirst;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telecom.TelecomManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity3 extends AppCompatActivity
{
    ListView lv;
    Context context;
    ArrayList progList;
    MyListAdapter adapter;
    static public boolean active = false;

    public  static  String[] progNames=
            {
                    "Hạnh nhân",
                    "Táo",
                    "Quả mơ",
                    "Atisô",
                    "Rau cải lông",
                    "Bơ",
                    "Hoa chuối",
                    "Chuối",
                    "Củ cải đường",
                    "Ớt chuông",
                    "Cải chíp",
                    "Bông cải xanh",
                    "Bắp cải",
                    "Cà rốt",
                    "Súp lơ trắng",
                    "Quả anh đào",
                    "Ớt",
                    "Dừa",
                    "Ngô",
                    "Dưa chuột",
                    "Thanh long",
                    "Sầu riêng",
                    "Cà tím",
                    "Quả sung",
                    "Tỏi",
                    "Gừng",
                    "Bạch quả",
                    "Nho",
                    "Trái ổi",
                    "Cải xoăn",
                    "Quả kiwi",
                    "Tỏi tây",
                    "Chanh vàng",
                    "Rau xà lách",
                    "Mắc ca",
                    "Mache",
                    "Quả xoài",
                    "Sữa",
                    "Nấm",
                    "Đậu bắp",
                    "Quả ô liu",
                    "Củ hành",
                    "Quả cam",
                    "Đu đủ",
                    "Mùi tây",
                    "Chanh dây",
                    "Quả đào",
                    "Đậu phụng",
                    "Quả lê",
                    "Đậu Hà Lan",
                    "Hạt tiêu",
                    "Quả dứa",
                    "Mận",
                    "Thạch lựu",
                    "Khoai tây",
                    "Quả bí ngô",
                    "Củ cải",
                    "Dâu rừng",
                    "Rau chân vịt",
                    "Trái khế",
                    "Quả dâu",
                    "Cà chua",
                    "Cây củ cải",
                    "Quả óc chó",
                    "Cải xoong nước",
                    "Dưa hấu",
            };

    public  static  Integer[] progImages=
            {
                    R.drawable.almond1,
                    R.drawable.apple1,
                    R.drawable.apricot1,
                    R.drawable.artichoke,
                    R.drawable.arugula,
                    R.drawable.avocado1,
                    R.drawable.bananaflower,
                    R.drawable.bananas1,
                    R.drawable.beet,
                    R.drawable.bellpepper,
                    R.drawable.bokchoy,
                    R.drawable.broccoli,
                    R.drawable.cabbage2,
                    R.drawable.carrot1,
                    R.drawable.cauliflower1,
                    R.drawable.cherry1,
                    R.drawable.chili,
                    R.drawable.coconut1,
                    R.drawable.corn1,
                    R.drawable.cucumber1,
                    R.drawable.dragon1,
                    R.drawable.durian1,
                    R.drawable.eggplant1,
                    R.drawable.fig1,
                    R.drawable.garlic1,
                    R.drawable.ginger,
                    R.drawable.ginkgo2,
                    R.drawable.grape1,
                    R.drawable.guava1,
                    R.drawable.kale,
                    R.drawable.kiwi1,
                    R.drawable.leek,
                    R.drawable.lemon1,
                    R.drawable.lettuce1,
                    R.drawable.macadamianut,
                    R.drawable.mache,
                    R.drawable.mango1,
                    R.drawable.milk,
                    R.drawable.mushrooms1,
                    R.drawable.okra,
                    R.drawable.olives,
                    R.drawable.onion1,
                    R.drawable.orange2,
                    R.drawable.papaya1,
                    R.drawable.parsley,
                    R.drawable.passion1,
                    R.drawable.peach1,
                    R.drawable.peanut,
                    R.drawable.pear1,
                    R.drawable.peas1,
                    R.drawable.pepper,
                    R.drawable.pineapple1,
                    R.drawable.plum1,
                    R.drawable.pomegranate1,
                    R.drawable.potato1,
                    R.drawable.pumpkin1,
                    R.drawable.radish1,
                    R.drawable.raspberry1,
                    R.drawable.spinach1,
                    R.drawable.star1,
                    R.drawable.strawberry1,
                    R.drawable.tomato1,
                    R.drawable.turnip1,
                    R.drawable.walnut,
                    R.drawable.watercress,
                    R.drawable.watermelon1,
            };


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu,menu);
        MenuItem menuItem=menu.findItem(R.id.searchview);
        SearchView searchView=(SearchView) menuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query)
            {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText)
            {
                adapter.getFilter().filter(newText);
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item)
    {
        int id=item.getItemId();
        if(id== R.id.searchview)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        active = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        active = false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen

        List<FoodItems> foodItems = new ArrayList<>();

        for(int i = 0 ; i < progNames.length; i++)
        {
            foodItems.add(new FoodItems(progNames[i],progImages[i]));
        }

        adapter=new MyListAdapter(this,foodItems);
        lv=(ListView)findViewById(R.id.listview2);
        lv.setAdapter(adapter);



    }
}