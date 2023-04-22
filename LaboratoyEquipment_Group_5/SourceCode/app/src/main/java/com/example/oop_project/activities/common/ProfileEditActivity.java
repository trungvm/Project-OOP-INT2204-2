package com.example.oop_project.activities.common;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.oop_project.R;
import com.example.oop_project.databinding.ActivityProfileEditBinding;
import com.example.oop_project.models.Person;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {
    private ActivityProfileEditBinding binding;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private Uri imageUri = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileEditBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait!");
        progressDialog.setCanceledOnTouchOutside(false);
        firebaseAuth = FirebaseAuth.getInstance();
        binding.birthdayEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int month = calendar.get(Calendar.MONTH);
                int year = calendar.get(Calendar.YEAR);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ProfileEditActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                String dateOfBirth = dayOfMonth + "/" + (monthOfYear + 1) + "/" + year;
                                binding.birthdayEt.setText(dateOfBirth);
                            }
                        },
                        year, month, day
                );
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        loadUserInformation();
        binding.profileTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showImageAttachMenu();
            }
        });

        // handle click, update profile;
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateData();
            }
        });
    }
    private String fullName = "", mobile = "", address = "", otherInformation = "", birthday = "";
    private int gender = 0;
    private void validateData() {
        fullName = binding.nameEt.getText().toString().trim();
        mobile = binding.mobileEt.getText().toString().trim();
        address = binding.addressEt.getText().toString().trim();
        otherInformation = binding.otherInforEt.getText().toString().trim();
        birthday = binding.birthdayEt.getText().toString().trim();
        int id = binding.genderRadioGroup.getCheckedRadioButtonId();
        if(id == R.id.male_radio_button){
            gender = 1;
        }else if(id == R.id.female_radio_button){
            gender = 2;
        }
        if(TextUtils.isEmpty(fullName)){
            Toast.makeText(this, "Enter name...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(mobile)){
            Toast.makeText(this, "Enter phone number...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(address)){
            Toast.makeText(this, "Enter address...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(otherInformation)){
            Toast.makeText(this, "Enter other information...", Toast.LENGTH_SHORT).show();
        }else if(TextUtils.isEmpty(birthday)){
            Toast.makeText(this, "Enter birthday", Toast.LENGTH_SHORT).show();
        }else if(gender == 0){
            Toast.makeText(this, "Choose gender...", Toast.LENGTH_SHORT).show();
        }else{
            if(imageUri == null){
                // need to update without image
                updateProfile("");
            }else{
                uploadImage();
            }
        }
    }
    private void uploadImage(){
        progressDialog.setMessage("Updating profile image");
        progressDialog.show();

        String filePathAndName =  "ProfileImages/" + firebaseAuth.getUid();
        StorageReference ref = FirebaseStorage.getInstance().getReference(filePathAndName);
        ref.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        String uploadedImageUrl = String.valueOf(uriTask.getResult());
                        updateProfile(uploadedImageUrl);

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this, "Failed to upload image due to", Toast.LENGTH_SHORT).show();

                    }
                });
    }
    private void updateProfile(String imageUrl){
        progressDialog.setMessage("Updating user profile");
        progressDialog.show();

        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fullName", fullName);
        hashMap.put("mobile", mobile);
        hashMap.put("birthday", birthday);
        hashMap.put("address", address);
        hashMap.put("gender", gender);
        hashMap.put("otherInfor", otherInformation);
        if(imageUri != null){
            hashMap.put("profileImage", ""+imageUrl);
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
        ref.child(firebaseAuth.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this, "Sửa thông tin thành công...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                     public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(ProfileEditActivity.this, "Failed to update profile...", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showImageAttachMenu() {
        PopupMenu popupMenu = new PopupMenu(this, binding.profileTv);
        popupMenu.getMenu().add(Menu.NONE, 0, 0, "Camera");
        popupMenu.getMenu().add(Menu.NONE, 1, 1, "Gallery");
        popupMenu.show();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                // get id of item clicked
                int which = item.getItemId();
                if(which == 0){
                    // camera clicked
                    pickImageCamera();
                }else if(which == 1){
                    // gallery clicked
                    pickImageGallery();
                }
                return false;
            }
        });
    }

    private void pickImageGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        galleryActivityResultLauncher.launch(intent);
    }

    private void pickImageCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Pick");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Sample Image Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        cameraActivityResultLauncher.launch(intent);
    }
    private ActivityResultLauncher<Intent> cameraActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        binding.profileTv.setImageURI(imageUri);

                    }else{
                        Toast.makeText(ProfileEditActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }
                }
            }
    );
    private ActivityResultLauncher<Intent> galleryActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        imageUri = data.getData();
                        binding.profileTv.setImageURI(imageUri);

                    }else{
                        Toast.makeText(ProfileEditActivity.this, "Cancelled", Toast.LENGTH_SHORT).show();
                    }

                }
            }
    );

    private void loadUserInformation() {
        Person person = new Person("Users");
        person.getDataFromFireBase(firebaseAuth.getUid())
                .addOnCompleteListener(new OnCompleteListener<Person>() {
                    @Override
                    public void onComplete(@NonNull Task<Person> task) {
                        if(task.isSuccessful()){
                            Person newPerson = task.getResult();
                            String sex = "N/A";
                            if(newPerson.getGender() == 1){
                                sex = "Nam";
                            }else if(newPerson.getGender() == 2){
                                sex = "Nữ";
                            }
                            Glide.with(ProfileEditActivity.this)
                                    .load(newPerson.getProfileImage())
                                    .centerCrop()
                                    .listener(new RequestListener<Drawable>() {
                                        @Override
                                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                            binding.profileTv.setVisibility(View.VISIBLE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                                            binding.profileTv.setVisibility(View.VISIBLE);
                                            return false;
                                        }
                                    })
                                    .into(binding.profileTv);
//                            String formattedDate = MyApplication.formatTimestamp(Long.parseLong(timestamp));
                            // set data to ui
                            binding.nameEt.setText(newPerson.getFullName().equals("null") ? "" : newPerson.getFullName());
                            binding.mobileEt.setText(newPerson.getMobile().equals("null")? "" : newPerson.getMobile());
                            binding.addressEt.setText(newPerson.getAddress().equals("null") ? "" :  newPerson.getAddress());
                            binding.otherInforEt.setText(newPerson.getOtherInfor().equals("null") ? "" : newPerson.getOtherInfor());
                            binding.birthdayEt.setText(newPerson.getBirthday().equals("null") ? "" : newPerson.getBirthday());
                            if(sex.equals("Nam")){
                                binding.maleRadioButton.setChecked(true);
                            }else if(sex.equals("Nữ")){
                                binding.femaleRadioButton.setChecked(true);
                            }

                        }
                    }
                });
    }
}