package com.oop.iotapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class RegisterActivity extends AppCompatActivity {
    //    List<User> users;
    private EditText et_username;
    private EditText et_email;
    private EditText et_password;
    private EditText et_repassword;
    private Button bt_register;
    private TextView tv_loginForward;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();

        bt_register.setOnClickListener(e -> {
            registerClick();
        });

        tv_loginForward.setOnClickListener(e -> {
            loginIntent();
        });
    }

    private void initViews() {
        et_email = findViewById(R.id.edittext_email_register);
        et_username = findViewById(R.id.edittext_username);
        et_password = findViewById(R.id.edittext_password_register);
        et_repassword = findViewById(R.id.edittext_repassword_register);
        bt_register = findViewById(R.id.button_register);
        tv_loginForward = findViewById(R.id.textview_login_forward);

        mAuth = FirebaseAuth.getInstance();
    }

    private void loginIntent() {
        Intent login = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(login);
    }

    private void registerClick() {
        String username = et_username.getText().toString();
        String email = et_email.getText().toString();
        String password = et_password.getText().toString();
        String repassword = et_repassword.getText().toString();

        if (username.equals("") || email.equals("") || password.equals("") || repassword.equals("")) {
            Toast.makeText(this, "Không được để trống trường nào!", Toast.LENGTH_SHORT).show();
        } else if (password.length() < 8) {
            Toast.makeText(this, "Password cần nhiều hơn hoặc bằng 8 ký tự", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(repassword)) {
            Toast.makeText(this, "Mật khẩu nhập lại không đúng!", Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if (!(c >= ' ' && c <= '~')) {
                    Toast.makeText(this, "Bạn chỉ được nhập các ký tự cho phép", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(RegisterActivity.this, "Register success", Toast.LENGTH_SHORT).show();
                                loginIntent();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(RegisterActivity.this, "Register fail", Toast.LENGTH_SHORT).show();
                                et_email.setText("");
                                et_password.setText("");
                                et_repassword.setText("");
                                et_username.setText("");
                            }
                        }
                    });
        }
    }
}