package com.example.DvsQFood.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.ChangePasswordActivity;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.activity.OrderHistoryActivity;
import com.pro.foodorder.activity.SignInActivity;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAccountBinding;
import com.pro.foodorder.prefs.DataStoreManager;

public class AccountFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Sử dụng Data Binding để gắn kết layout
        FragmentAccountBinding fragmentAccountBinding = FragmentAccountBinding.inflate(inflater, container, false);

        // Hiển thị địa chỉ email của người dùng
        fragmentAccountBinding.tvEmail.setText(DataStoreManager.getUser().getEmail());

        // Gán các sự kiện cho các View trong layout
        fragmentAccountBinding.layoutSignOut.setOnClickListener(v -> onClickSignOut());
        fragmentAccountBinding.layoutChangePassword.setOnClickListener(v -> onClickChangePassword());
        fragmentAccountBinding.layoutOrderHistory.setOnClickListener(v -> onClickOrderHistory());

        return fragmentAccountBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        // Thiết lập thanh toolbar nếu có
        if (getActivity() != null) {
            ((MainActivity) getActivity()).setToolBar(false, getString(R.string.account));
        }
    }

    private void onClickOrderHistory() {
        // Chuyển đến màn hình xem lịch sử đơn hàng
        GlobalFunction.startActivity(getActivity(), OrderHistoryActivity.class);
    }

    private void onClickChangePassword() {
        // Chuyển đến màn hình đổi mật khẩu
        GlobalFunction.startActivity(getActivity(), ChangePasswordActivity.class);
    }

    private void onClickSignOut() {
        // Đăng xuất khỏi tài khoản và chuyển về màn hình đăng nhập
        if (getActivity() == null) {
            return;
        }
        FirebaseAuth.getInstance().signOut();
        DataStoreManager.setUser(null);
        GlobalFunction.startActivity(getActivity(), SignInActivity.class);
        getActivity().finishAffinity();
    }
}
