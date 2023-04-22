package com.example.oop_project.filters.admin;

import android.widget.Filter;

import com.example.oop_project.adapters.admin.AdapterScheduleAdmin;
import com.example.oop_project.models.ModelEquipment;

import java.util.ArrayList;

public class FilterScheduleAdmin extends Filter {

    ArrayList<ModelEquipment> filterList;

    AdapterScheduleAdmin adapterScheduleAdmin;

    // constructor

    public FilterScheduleAdmin(ArrayList<ModelEquipment> filterList, AdapterScheduleAdmin adapterScheduleAdmin) {
        this.filterList = filterList;
        this.adapterScheduleAdmin = adapterScheduleAdmin;
    }

    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results = new FilterResults();
        if (constraint != null || constraint.length() > 0) {
            constraint = constraint.toString().toUpperCase();
            ArrayList<ModelEquipment> filterCategories = new ArrayList<>();

            for (int i = 0; i < filterList.size(); i++) {
                if (filterList.get(i).getTitle().toUpperCase().contains(constraint)) {
                    filterCategories.add(filterList.get(i));
                }
            }
            results.count = filterCategories.size();
            results.values = filterCategories;
        } else {
            results.count = filterList.size();
            results.values = filterList;
        }
        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        // apply filter changes
        adapterScheduleAdmin.equipmentArrayList = (ArrayList<ModelEquipment>) results.values;
        adapterScheduleAdmin.notifyDataSetChanged();
    }
}
