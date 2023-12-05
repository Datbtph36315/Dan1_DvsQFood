package com.example.DvsQFood.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.foodorder.R;
import com.pro.foodorder.databinding.ActivityForgotPasswordBinding;
import com.pro.foodorder.utils.StringUtil;

public class ForgotPasswordActivity extends BaseActivity {

    private ActivityForgotPasswordBinding mActivityForgotPasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kết nối đối tượng binding với layout
        mActivityForgotPasswordBinding = ActivityForgotPasswordBinding.inflate(getLayoutInflater());
        setContentView(mActivityForgotPasswordBinding.getRoot());

        // Bắt sự kiện khi nút quay lại được nhấn
        mActivityForgotPasswordBinding.imgBack.setOnClickListener(v -> onBackPressed());

        // Bắt sự kiện khi nút Reset Password được nhấn
        mActivityForgotPasswordBinding.btnResetPassword.setOnClickListener(v -> onClickValidateResetPassword());
    }

    // Phương thức xử lý khi nút Reset Password được nhấn
    private void onClickValidateResetPassword() {
        // Lấy địa chỉ email từ EditText
        String strEmail = mActivityForgotPasswordBinding.edtEmail.getText().toString().trim();

        // Kiểm tra tính hợp lệ của email
        if (StringUtil.isEmpty(strEmail)) {
            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.msg_email_require), Toast.LENGTH_SHORT).show();
        } else if (!StringUtil.isValidEmail(strEmail)) {
            Toast.makeText(ForgotPasswordActivity.this, getString(R.string.msg_email_invalid), Toast.LENGTH_SHORT).show();
        } else {
            // Gọi phương thức resetPassword khi email hợp lệ
            resetPassword(strEmail);
        }
    }

    // Phương thức thực hiện reset mật khẩu
    private void resetPassword(String email) {
        // Hiển thị dialog tiến trình đang xử lý
        showProgressDialog(true);

        // Lấy đối tượng FirebaseAuth
        FirebaseAuth auth = FirebaseAuth.getInstance();

        // Gửi email reset mật khẩu đến địa chỉ email
        auth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    // Ẩn dialog tiến trình đang xử lý
                    showProgressDialog(false);

                    // Kiểm tra kết quả gửi email
                    if (task.isSuccessful()) {
                        // Thông báo reset mật khẩu thành công
                        Toast.makeText(ForgotPasswordActivity.this,
                                getString(R.string.msg_reset_password_successfully),
                                Toast.LENGTH_SHORT).show();

                        // Xóa nội dung trường email
                        mActivityForgotPasswordBinding.edtEmail.setText("");
                    }
                });
    }
}
