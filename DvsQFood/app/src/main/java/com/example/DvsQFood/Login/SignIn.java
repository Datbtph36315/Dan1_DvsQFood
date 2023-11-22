package com.example.DvsQFood.Login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.DvsQFood.R;
import com.example.DvsQFood.activity.MainActivity;
import com.example.DvsQFood.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignIn extends AppCompatActivity {

    EditText edt_Phone, edt_Password;
    TextView dangKyText;
    Button btn_DangNhap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edt_Phone = findViewById(R.id.edt_Phone);
        edt_Password = findViewById(R.id.edt_Password);
        btn_DangNhap = findViewById(R.id.btn_dangNhap);
        dangKyText = findViewById(R.id.dangKyText);

        // Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference().child("user");

        btn_DangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String userPhone = edt_Phone.getText().toString().trim();
                final String userPassword = edt_Password.getText().toString().trim();

                if (userPhone.isEmpty() || userPassword.isEmpty()) {
                    Toast.makeText(SignIn.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                final ProgressDialog mdialog = new ProgressDialog(SignIn.this);
                mdialog.setMessage("Vui lòng đợi...");
                mdialog.show();

                table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mdialog.dismiss();

                        // Kiểm tra tính hợp lệ của dữ liệu đăng nhập
                        if (dataSnapshot.child(userPhone).exists()) {
                            User user = dataSnapshot.child(userPhone).getValue(User.class);

                            if (user != null && user.getPassword().equals(userPassword)) {
                                Toast.makeText(SignIn.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                                // Thực hiện các hành động sau khi đăng nhập thành công
                                Intent intent = new Intent(SignIn.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Đóng màn hình đăng nhập sau khi đăng nhập thành công
                            } else {
                                Toast.makeText(SignIn.this, "Sai mật khẩu", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SignIn.this, "Người dùng không tồn tại trong Database", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        mdialog.dismiss();
                        Toast.makeText(SignIn.this, "Đã xảy ra lỗi, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        dangKyText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng ký và đóng màn hình đăng nhập
                Intent intent = new Intent(SignIn.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });
    }


}
