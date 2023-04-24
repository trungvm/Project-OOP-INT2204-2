package com.oop.iotapp;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.slider.Slider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CurtainsActivity extends AppCompatActivity{

    private ArrayAdapter<String> adapter = null;
    private List<String> listDevices = new ArrayList<>();
    private DatabaseReference myRef;

    private AutoCompleteTextView actv_devices;
    private MaterialButton bt_addDevice, bt_deleteDevice, bt_statusOn, bt_statusOff, bt_percentConfirm, bt_autoOn, bt_autoOff;
    private Slider sl_percent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curtains);

        initViews();

        myRef = FirebaseDatabase.getInstance("https://ntiot-741e0-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("Curtains");

        getListCurtains();

        curtainsTasks();
    }

    private void curtainsTasks() {
        Log.e(TAG, "curtainsTasks: Start");

        selectDevice();

        addDevice();

        deleteDevice();

    }

    private void deleteDevice() {
        bt_deleteDevice.setOnClickListener(e -> {

            String selected = actv_devices.getText().toString();
            MaterialAlertDialogBuilder alert = new MaterialAlertDialogBuilder(this);
            alert.setTitle("Xóa thiết bị");
            alert.setMessage("Bạn có chắc bạn muốn xóa " + selected + " chứ?\n" + "Nếu bấm YES bạn sẽ không thể khôi phục!");
            alert.setPositiveButton("YES", (dialog, which) -> {
                deleteNode(selected);
            });
            alert.setNegativeButton("No", (dialog, which) -> {

            });
            alert.show();
        });
    }

    private void deleteNode(String selected) {
        myRef.child(selected).removeValue();
        //TODO: bug
    }



    private void addDevice() {
        bt_addDevice.setOnClickListener(e -> {
            //TODO: added simple dialog to enter device information
        });
    }

    private void selectDevice() {

        String selectedDevice = actv_devices.getText().toString();

        if (selectedDevice.equals("")){
            bt_deleteDevice.setBackgroundColor(Color.rgb(63,72,84));
            bt_deleteDevice.setClickable(false);

            bt_statusOn.setBackgroundColor(Color.rgb(63,72,84));
            bt_statusOn.setClickable(false);

            bt_statusOff.setBackgroundColor(Color.rgb(63, 72, 84));
            bt_statusOff.setClickable(false);

            bt_percentConfirm.setBackgroundColor(Color.rgb(63, 72, 84));
            bt_percentConfirm.setClickable(false);

            bt_autoOff.setBackgroundColor(Color.rgb(63, 72, 84));
            bt_autoOff.setClickable(false);

            bt_autoOn.setBackgroundColor(Color.rgb(63, 72, 84));
            bt_autoOn.setClickable(false);

            Toast.makeText(this, "Vui lòng chọn thiết bị hoặc chế độ tự động!", Toast.LENGTH_SHORT).show();

        }
        actv_devices.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Query query = myRef.child(actv_devices.getText().toString());
                CurtainsData cd;
                query.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        CurtainsData curtainsData = snapshot.getValue(CurtainsData.class);
                        Toast.makeText(CurtainsActivity.this, "Selected " + curtainsData.getName(), Toast.LENGTH_SHORT).show();

                        bt_deleteDevice.setBackgroundColor(Color.rgb(34, 40, 49));
                        bt_deleteDevice.setClickable(true);
                        //Dieu chinh PERCENT
                        bt_percentConfirm.setBackgroundColor(Color.rgb(34, 40, 49));
                        bt_percentConfirm.setClickable(true);
                        //Xet trang thai cua STATUS de dieu chinh nut bam
                        if (curtainsData.getStatus() == 0L){
                            bt_statusOff.setBackgroundColor(Color.rgb(63,72,84));
                            bt_statusOff.setClickable(false);

                            bt_statusOn.setBackgroundColor(Color.rgb(34, 40, 49));
                            bt_statusOn.setClickable(true);
                        }
                        else {
                            bt_statusOff.setBackgroundColor(Color.rgb(34, 40, 49));
                            bt_statusOff.setClickable(true);

                            bt_statusOn.setBackgroundColor(Color.rgb(63,72,84));
                            bt_statusOn.setClickable(false);
                        }
                        //Xet trang thai cua AUTOMODE de dieu chinh nut bam
                        if (curtainsData.getAutoMode() == 0L){
                            bt_autoOff.setBackgroundColor(Color.rgb(63,72,84));
                            bt_autoOff.setClickable(false);

                            bt_autoOn.setBackgroundColor(Color.rgb(34, 40, 49));
                            bt_autoOn.setClickable(true);
                        }
                        else{
                            bt_autoOff.setBackgroundColor(Color.rgb(34, 40, 49));
                            bt_autoOff.setClickable(true);

                            bt_autoOn.setBackgroundColor(Color.rgb(63,72,84));
                            bt_autoOn.setClickable(false);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(CurtainsActivity.this, "Không thể lấy dữ liệu", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void getListCurtains() {
        Log.e(TAG, "getListCurtains: Start");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    String curtainsData = dataSnapshot.getKey();
                    listDevices.add(curtainsData);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(CurtainsActivity.this, "Không thể lấy dữ liệu từ server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initViews() {
        Log.e(TAG, "initViews: Start");

        actv_devices = findViewById(R.id.autocompletetextview_devices_curtains);
        bt_addDevice = findViewById(R.id.button_add_device_curtains);
        bt_deleteDevice = findViewById(R.id.button_delete_device_curtains);
        bt_statusOn = findViewById(R.id.button_status_on_curtains);
        bt_statusOff = findViewById(R.id.button_status_off_curtains);
        bt_percentConfirm = findViewById(R.id.button_percent_confirm_curtains);
        bt_autoOn = findViewById(R.id.button_autoMode_on_curtains);
        bt_autoOff = findViewById(R.id.button_autoMode_off_curtains);
        sl_percent = findViewById(R.id.slider_percent_curtains);

        adapter = new ArrayAdapter<>(this, R.layout.dropdown_item, listDevices);
        actv_devices.setAdapter(adapter);
    }
}