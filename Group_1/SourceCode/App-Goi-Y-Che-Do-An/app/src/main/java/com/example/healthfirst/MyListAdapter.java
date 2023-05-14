package com.example.healthfirst;


import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;


public class MyListAdapter extends BaseAdapter implements Filterable
{
    private final Activity context;
    List<FoodItems> foodItems;
    List<FoodItems> full;

    public MyListAdapter(Activity context, List<FoodItems> foodItems)
    {
        this.context = context;
        this.foodItems = foodItems;
        full = new ArrayList<>(foodItems);
    }


    @Override
    public int getCount()
    {
        return foodItems.size();
    }

    @Override
    public Object getItem(int position)
    {
        return foodItems.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {

        View r=convertView;
        ViewHolder viewHolder=null;
        if(r == null)
        {
            LayoutInflater layoutInflater=context.getLayoutInflater();
            r=layoutInflater.inflate(R.layout.listview_layout,null,true);
            viewHolder= new ViewHolder(r);
            r.setTag(viewHolder);
        }
        else
        {
            viewHolder=(ViewHolder)r.getTag();
        }

        viewHolder.ivw.setImageResource(foodItems.get(position).getFoodImageId());
        viewHolder.tv1.setText(foodItems.get(position).getFoodName());
        r.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FoodDetails.class);
                intent.putExtra("key", foodItems.get(position).getFoodNameEng());
                intent.putExtra("foodvn", foodItems.get(position).getFoodName());
                context.startActivity(intent);
            }
        });

        return r;
    }
    class ViewHolder
    {
        TextView tv1;
        TextView tv2;
        ImageView ivw;
        ViewHolder(View v)
        {
            tv1=(TextView)v.findViewById(R.id.FoodItemName);
            ivw=(ImageView)v.findViewById(R.id.imageView);


        }
    }


    Filter listFilter = new Filter()
    {
        @Override
        protected FilterResults performFiltering(CharSequence constraint)
        {
            List<FoodItems> resultData=new ArrayList<>( );

            FilterResults filterResults = new FilterResults();
            if(constraint==null || constraint.length()==0)
            {
                filterResults.count = full.size();
                filterResults.values = full;
            }
            else
            {
                for(int i = 0 ; i < full.size(); i++)
                {
                    if(full.get(i).getFoodName().toLowerCase().startsWith(constraint.toString().toLowerCase()))
                    {
                        resultData.add(full.get(i));
                    }
                }
                filterResults.values = resultData;
            }
            return filterResults;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results)
        {
            foodItems = (List<FoodItems>)results.values;
            notifyDataSetChanged();
        }
    };

    @NonNull
    @Override
    public Filter getFilter()
    {
        return listFilter;

    }
}
