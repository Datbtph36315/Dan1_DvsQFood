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
import com.example.DvsQFood.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUp extends AppCompatActivity {

    EditText edt_Phone, edt_User, edt_Password;
    TextView dangNhapText;
    Button btn_DangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edt_Phone = findViewById(R.id.edt_Phone);
        edt_User = findViewById(R.id.edt_User);
        edt_Password = findViewById(R.id.edt_Password);
        dangNhapText = findViewById(R.id.dangNhapText);
        btn_DangKy = findViewById(R.id.btn_dangKy);

        // Khởi tạo DB
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference().child("user");

        // ... (phần code khác không đổi)

        btn_DangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mdialog = new ProgressDialog(SignUp.this);
                mdialog.setMessage("Vui lòng đợi...");
                mdialog.show();

                final String phoneNumber = edt_Phone.getText().toString().trim();
                final String username = edt_User.getText().toString().trim();
                final String password = edt_Password.getText().toString().trim();

                // Kiểm tra tính hợp lệ của dữ liệu
                if (phoneNumber.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    mdialog.dismiss();
                    Toast.makeText(SignUp.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (phoneNumber.length() != 10) {
                    mdialog.dismiss();
                    Toast.makeText(SignUp.this, "Số điện thoại phải có 10 chữ số", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    mdialog.dismiss();
                    Toast.makeText(SignUp.this, "Mật khẩu phải có ít nhất 6 ký tự", Toast.LENGTH_SHORT).show();
                } else {
                    table_user.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // Kiểm tra số điện thoại đã đăng ký chưa
                            if (dataSnapshot.child(phoneNumber).exists()) {
                                mdialog.dismiss();
                                Toast.makeText(SignUp.this, "Số điện thoại đã đăng ký rồi", Toast.LENGTH_SHORT).show();
                            } else {
                                // Đăng ký thành công
                                mdialog.dismiss();
                                User user = new User(username, password);
                                table_user.child(phoneNumber).setValue(user);
                                Toast.makeText(SignUp.this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();

                                // Reset các ô người dùng đã nhập
                                edt_Phone.setText("");
                                edt_User.setText("");
                                edt_Password.setText("");
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            mdialog.dismiss();
                            Toast.makeText(SignUp.this, "Đã xảy ra lỗi, vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

// ... (phần code khác không đổi)


        dangNhapText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển đến màn hình đăng nhập và đóng màn hình đăng ký
                Intent intent = new Intent(SignUp.this, SignIn.class);
                startActivity(intent);
                finish();
            }
        });
    }

}