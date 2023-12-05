package com.example.DvsQFood.activity;

import android.os.Bundle;
import android.view.View;

import androidx.viewpager2.widget.ViewPager2;

import com.afollestad.materialdialogs.MaterialDialog;
import com.pro.foodorder.R;
import com.pro.foodorder.adapter.AdminViewPagerAdapter;
import com.pro.foodorder.databinding.ActivityAdminMainBinding;

public class AdminMainActivity extends BaseActivity {

    private ActivityAdminMainBinding mActivityAdminMainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivityAdminMainBinding = ActivityAdminMainBinding.inflate(getLayoutInflater());
        setContentView(mActivityAdminMainBinding.getRoot());

        // Khởi tạo và cấu hình ViewPager
        mActivityAdminMainBinding.viewpager2.setUserInputEnabled(false);
        AdminViewPagerAdapter adminViewPagerAdapter = new AdminViewPagerAdapter(this);
        mActivityAdminMainBinding.viewpager2.setAdapter(adminViewPagerAdapter);

        // Thiết lập lắng nghe sự kiện khi trang của ViewPager thay đổi
        mActivityAdminMainBinding.viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // Cập nhật trạng thái của BottomNavigationView khi trang thay đổi
                switch (position) {
                    case 0:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_home).setChecked(true);
                        break;

                    case 1:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_feedback).setChecked(true);
                        break;

                    case 2:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_order).setChecked(true);
                        break;

                    case 3:
                        mActivityAdminMainBinding.bottomNavigation.getMenu().findItem(R.id.nav_account).setChecked(true);
                        break;
                }
            }
        });

        // Thiết lập lắng nghe sự kiện khi chọn mục trên BottomNavigationView
        mActivityAdminMainBinding.bottomNavigation.setOnNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(0);
            } else if (id == R.id.nav_feedback) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(1);
            } else if (id == R.id.nav_order) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(2);
            }  else if (id == R.id.nav_account) {
                mActivityAdminMainBinding.viewpager2.setCurrentItem(3);
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        // Hiển thị hộp thoại xác nhận khi nhấn nút quay lại
        showConfirmExitApp();
    }

    private void showConfirmExitApp() {
        // Hiển thị hộp thoại xác nhận để đảm bảo người dùng muốn thoát ứng dụng
        new MaterialDialog.Builder(this)
                .title(getString(R.string.app_name))
                .content(getString(R.string.msg_exit_app))
                .positiveText(getString(R.string.action_ok))
                .onPositive((dialog, which) -> finishAffinity())
                .negativeText(getString(R.string.action_cancel))
                .cancelable(false)
                .show();
    }

    public void setToolBar(String title) {
        // Thiết lập thanh toolbar với tiêu đề được chuyển đến
        mActivityAdminMainBinding.toolbar.layoutToolbar.setVisibility(View.VISIBLE);
        mActivityAdminMainBinding.toolbar.tvTitle.setText(title);
    }
}
