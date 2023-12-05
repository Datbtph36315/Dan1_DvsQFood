package com.example.DvsQFood.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.adapter.OrderAdapter;
import com.pro.foodorder.databinding.ActivityOrderHistoryBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.prefs.DataStoreManager;

import java.util.ArrayList;
import java.util.List;

public class OrderHistoryActivity extends BaseActivity {

    private ActivityOrderHistoryBinding mActivityOrderHistoryBinding;
    private List<Order> mListOrder;
    private OrderAdapter mOrderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kết nối đối tượng binding với layout
        mActivityOrderHistoryBinding = ActivityOrderHistoryBinding.inflate(getLayoutInflater());
        setContentView(mActivityOrderHistoryBinding.getRoot());

        // Khởi tạo thanh công cụ (toolbar)
        initToolbar();

        // Khởi tạo giao diện người dùng
        initView();

        // Lấy danh sách đơn hàng từ Firebase
        getListOrders();
    }

    // Phương thức khởi tạo thanh công cụ (toolbar)
    private void initToolbar() {
        // Hiển thị nút quay lại và ẩn biểu tượng giỏ hàng
        mActivityOrderHistoryBinding.toolbar.imgBack.setVisibility(View.VISIBLE);
        mActivityOrderHistoryBinding.toolbar.imgCart.setVisibility(View.GONE);

        // Đặt tiêu đề cho thanh công cụ
        mActivityOrderHistoryBinding.toolbar.tvTitle.setText(getString(R.string.order_history));

        // Bắt sự kiện khi người dùng nhấn nút quay lại
        mActivityOrderHistoryBinding.toolbar.imgBack.setOnClickListener(v -> onBackPressed());
    }

    // Phương thức khởi tạo giao diện người dùng
    private void initView() {
        // Thiết lập quản lý layout cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mActivityOrderHistoryBinding.rcvOrderHistory.setLayoutManager(linearLayoutManager);
    }

    // Phương thức lấy danh sách đơn hàng từ Firebase
    public void getListOrders() {
        ControllerApplication.get(this).getBookingDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Kiểm tra và xử lý dữ liệu từ Firebase
                        processFirebaseData(snapshot);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi xảy ra trong quá trình truy cập Firebase
                    }
                });
    }

    // Phương thức xử lý dữ liệu từ Firebase
    private void processFirebaseData(DataSnapshot snapshot) {
        // Khởi tạo danh sách đơn hàng nếu chưa tồn tại
        if (mListOrder == null) {
            mListOrder = new ArrayList<>();
        } else {
            mListOrder.clear();
        }

        // Lặp qua danh sách các dữ liệu từ Firebase
        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
            // Chuyển dữ liệu từ Firebase thành đối tượng Order
            Order order = dataSnapshot.getValue(Order.class);

            // Kiểm tra và thêm đơn hàng vào danh sách nếu email khớp
            if (order != null) {
                String strEmail = DataStoreManager.getUser().getEmail();
                if (strEmail.equalsIgnoreCase(order.getEmail())) {
                    mListOrder.add(0, order);
                }
            }
        }

        // Khởi tạo và thiết lập Adapter cho RecyclerView
        mOrderAdapter = new OrderAdapter(OrderHistoryActivity.this, mListOrder);
        mActivityOrderHistoryBinding.rcvOrderHistory.setAdapter(mOrderAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Giải phóng tài nguyên khi activity bị hủy
        if (mOrderAdapter != null) {
            mOrderAdapter.release();
        }
    }
}
