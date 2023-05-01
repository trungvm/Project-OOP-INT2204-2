package com.example.oop_project.adapters.user;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.MyApplication;
import com.example.oop_project.activities.common.EquipmentDetailActivity;
import com.example.oop_project.databinding.RowEquipmentsCartBinding;
import com.example.oop_project.models.ModelCategory;
import com.example.oop_project.models.ModelEquipment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.ArrayList;

public class AdapterEquipmentCart extends RecyclerView.Adapter<AdapterEquipmentCart.HolderEquipmentCart> {
    private final Context context;
    private final ArrayList<ModelEquipment> equipmentArrayList;
    private RowEquipmentsCartBinding binding;
    private final ArrayList<Integer> quantityBorrow;
    private final ArrayList<Boolean> isChecked;
    private ArrayList<Boolean> isCallbackHandled;


    public AdapterEquipmentCart(Context context, ArrayList<ModelEquipment> equipmentArrayList) {
        this.context = context;
        this.equipmentArrayList = equipmentArrayList;
        quantityBorrow = new ArrayList<>();
        isChecked = new ArrayList<>();
        isCallbackHandled = new ArrayList<>();

        for (int i = 0; i < 1000; i++) {
            quantityBorrow.add(0);
            isChecked.add(false);
            isCallbackHandled.add(false);
        }
    }

    @NonNull
    @Override
    public HolderEquipmentCart onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = RowEquipmentsCartBinding.inflate(LayoutInflater.from(context), parent, false);
        return new HolderEquipmentCart(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull HolderEquipmentCart holder, @SuppressLint("RecyclerView") int position) {
        ModelEquipment modelEquipment = equipmentArrayList.get(position);
        loadEquipmentDetails(modelEquipment, holder, position);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EquipmentDetailActivity.class);
                intent.putExtra("equipmentId", modelEquipment.getId());
                context.startActivity(intent);
            }
        });
        holder.removeCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Delete").
                        setMessage("Are you sure want to remove equipment from cart?")
                        .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // begin delete;
                                Toast.makeText(context, "Removing...", Toast.LENGTH_SHORT).show();
                                MyApplication.removeFromCart(context, modelEquipment.getId());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();

            }
        });
        holder.minus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityBorrow.get(position) == 0) {
                    quantityBorrow.set(position, 0);
                } else {
                    int x = quantityBorrow.get(position);
                    x--;
                    quantityBorrow.set(position, x);
                }
                holder.quantity_text_choose.setText("" + quantityBorrow.get(position));
                modelEquipment.setQuantityBorrow(quantityBorrow.get(position));
                long timestamp = System.currentTimeMillis();
                Intent intent = new Intent("ACTION_GET_DATA");
                intent.putExtra("equipmentId", modelEquipment.getId());
                intent.putExtra("quantityBorrowed", "" + quantityBorrow.get(position));
                intent.putExtra("equipmentName", modelEquipment.getTitle());
                intent.putExtra("timestamp", "" + timestamp);
                intent.putExtra("isChecked", "" + isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
        holder.plus_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (quantityBorrow.get(position) < modelEquipment.getQuantity()) {
                    int x = quantityBorrow.get(position);
                    x++;
                    quantityBorrow.set(position, x);
                } else {
                    quantityBorrow.set(position, modelEquipment.getQuantity());
                }
                holder.quantity_text_choose.setText("" + quantityBorrow.get(position));
                modelEquipment.setQuantityBorrow(quantityBorrow.get(position));
                long timestamp = System.currentTimeMillis();
                Intent intent = new Intent("ACTION_GET_DATA");
                intent.putExtra("equipmentId", modelEquipment.getId());
                intent.putExtra("quantityBorrowed", "" + quantityBorrow.get(position));
                intent.putExtra("equipmentName", modelEquipment.getTitle());
                intent.putExtra("timestamp", "" + timestamp);
                intent.putExtra("isChecked", "" + isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });

        holder.checkIsBorrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean flag = isChecked.get(position);
                flag = !flag;
                isChecked.set(position, flag);
                holder.checkIsBorrow.setChecked(flag);
                equipmentArrayList.set(position, modelEquipment);
                long timestamp = System.currentTimeMillis();
                Intent intent = new Intent("ACTION_GET_DATA");
                intent.putExtra("equipmentId", modelEquipment.getId());
                intent.putExtra("quantityBorrowed", "" + quantityBorrow.get(position));
                intent.putExtra("equipmentName", modelEquipment.getTitle());
                intent.putExtra("timestamp", "" + timestamp);
                intent.putExtra("isChecked", "" + isChecked.get(position));
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });


    }

    private void loadEquipmentDetails(ModelEquipment model, HolderEquipmentCart holder, int position) {
        String equipmentId = model.getId();
        String title = model.getTitle();
        String description = model.getDescription();
        String equipmentImage = model.getEquipmentImage();
        long timestamp = model.getTimestamp();
        String date = MyApplication.formatTimestamp(timestamp);
        int quantity = model.getQuantity();
        holder.dateTv.setText(date);
        holder.titleTv.setText(title);
        holder.descriptionTv.setText(description);
        holder.quantityTv.setText("" + quantity);
        String categoryId = model.getCategoryId();
        holder.quantity_text_choose.setText("" + 0);

        ModelCategory modelCategory = new ModelCategory();
        modelCategory.getDataFromFireBase(categoryId).addOnCompleteListener(new OnCompleteListener<ModelCategory>() {
            @Override
            public void onComplete(@NonNull Task<ModelCategory> task) {
                if (task.isSuccessful()) {
                    ModelCategory newModelCategory = task.getResult();
                    holder.categoryTv.setText(newModelCategory.getTitle());
                }
            }
        });

       if(equipmentImage.equals("null") || equipmentImage.equals("")){

       }else{
           Handler handler = new Handler(Looper.getMainLooper());
           Glide.with(context)
                   .load(equipmentImage)
                   .centerCrop()
                   .listener(new RequestListener<Drawable>() {
                       @Override
                       public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                           if (!isCallbackHandled.get(position)) { // Kiểm tra nếu chưa xử lý callback
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       holder.progressBar.setVisibility(View.VISIBLE);
                                       holder.imageView.setVisibility(View.GONE);
                                   }
                               });

                               // Đánh dấu là đã xử lý callback
                               isCallbackHandled.set(position, true);
                           }

                           // Trả về false để cho phép Glide xử lý callback tiếp
                           return false;
                       }

                       @Override
                       public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                           if (!isCallbackHandled.get(position)) { // Kiểm tra nếu chưa xử lý callback
                               handler.post(new Runnable() {
                                   @Override
                                   public void run() {
                                       holder.imageView.setVisibility(View.VISIBLE);

                                       holder.progressBar.setVisibility(View.GONE);
                                   }
                               });

                               // Đánh dấu là đã xử lý callback
                               isCallbackHandled.set(position, true);
                           }

                           // Trả về false để cho phép Glide xử lý callback tiếp
                           return false;
                       }
                   })
                   .into(holder.imageView);
       }
    }

    @Override
    public int getItemCount() {
        return equipmentArrayList.size();
    }


    class HolderEquipmentCart extends RecyclerView.ViewHolder {
        ProgressBar progressBar;
        ImageView imageView;
        TextView titleTv, descriptionTv, quantityTv, dateTv, categoryTv;
        ImageButton removeCartBtn;
        TextView quantity_text_choose;
        AppCompatButton plus_button, minus_button;
        RadioButton checkIsBorrow;


        public HolderEquipmentCart(@NonNull View itemView) {
            super(itemView);
            progressBar = binding.progressBar;
            imageView = binding.imageView;
            titleTv = binding.titleTv;
            descriptionTv = binding.descriptionTv;
            quantityTv = binding.quantityTv;
            dateTv = binding.dateTv;
            categoryTv = binding.categoryTv;
            removeCartBtn = binding.removeCartBtn;
            quantity_text_choose = binding.quantityTextChoose;
            plus_button = binding.plusButton;
            minus_button = binding.minusButton;
            checkIsBorrow = binding.checkIsBorrow;
        }
    }
}
