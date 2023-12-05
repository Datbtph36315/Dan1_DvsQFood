package com.example.DvsQFood.activity;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pro.foodorder.R;
import com.pro.foodorder.databinding.ActivityChangePasswordBinding;
import com.pro.foodorder.model.User;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.StringUtil;

public class ChangePasswordActivity extends BaseActivity {

    private ActivityChangePasswordBinding mActivityChangePasswordBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityChangePasswordBinding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(mActivityChangePasswordBinding.getRoot());

        // Bắt sự kiện khi nút quay lại được nhấn
        mActivityChangePasswordBinding.imgBack.setOnClickListener(v -> onBackPressed());

        // Bắt sự kiện khi nút "Thay đổi mật khẩu" được nhấn
        mActivityChangePasswordBinding.btnChangePassword.setOnClickListener(v -> onClickValidateChangePassword());
    }

    private void onClickValidateChangePassword() {
        // Xử lý khi nút "Thay đổi mật khẩu" được nhấn
        String strOldPassword = mActivityChangePasswordBinding.edtOldPassword.getText().toString().trim();
        String strNewPassword = mActivityChangePasswordBinding.edtNewPassword.getText().toString().trim();
        String strConfirmPassword = mActivityChangePasswordBinding.edtConfirmPassword.getText().toString().trim();

        // Kiểm tra và hiển thị thông báo lỗi nếu có
        if (StringUtil.isEmpty(strOldPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_old_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_new_password_require), Toast.LENGTH_SHORT).show();
        } else if (StringUtil.isEmpty(strConfirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_confirm_password_require), Toast.LENGTH_SHORT).show();
        } else if (!DataStoreManager.getUser().getPassword().equals(strOldPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_old_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (!strNewPassword.equals(strConfirmPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_confirm_password_invalid), Toast.LENGTH_SHORT).show();
        } else if (strOldPassword.equals(strNewPassword)) {
            Toast.makeText(ChangePasswordActivity.this, getString(R.string.msg_new_password_invalid), Toast.LENGTH_SHORT).show();
        } else {
            // Gọi phương thức thay đổi mật khẩu
            changePassword(strNewPassword);
        }
    }

    private void changePassword(String newPassword) {
        // Xử lý thay đổi mật khẩu trên Firebase
        showProgressDialog(true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            return;
        }
        user.updatePassword(newPassword)
                .addOnCompleteListener(task -> {
                    showProgressDialog(false);
                    if (task.isSuccessful()) {
                        // Thông báo thành công và cập nhật thông tin người dùng trong DataStoreManager
                        Toast.makeText(ChangePasswordActivity.this,
                                getString(R.string.msg_change_password_successfully), Toast.LENGTH_SHORT).show();
                        User userLogin = DataStoreManager.getUser();
                        userLogin.setPassword(newPassword);
                        DataStoreManager.setUser(userLogin);

                        // Xóa dữ liệu trên giao diện
                        mActivityChangePasswordBinding.edtOldPassword.setText("");
                        mActivityChangePasswordBinding.edtNewPassword.setText("");
                        mActivityChangePasswordBinding.edtConfirmPassword.setText("");
                    }
                });
    }
}
