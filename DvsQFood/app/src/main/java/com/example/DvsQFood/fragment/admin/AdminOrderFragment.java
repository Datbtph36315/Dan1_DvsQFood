package com.example.DvsQFood.fragment.admin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.AdminMainActivity;
import com.pro.foodorder.adapter.AdminOrderAdapter;
import com.pro.foodorder.databinding.FragmentAdminOrderBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AdminOrderFragment extends BaseFragment {

    private FragmentAdminOrderBinding mFragmentAdminOrderBinding;
    private List<Order> mListOrder;
    private AdminOrderAdapter mAdminOrderAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mFragmentAdminOrderBinding = FragmentAdminOrderBinding.inflate(inflater, container, false);
        initView();
        getListOrders();
        return mFragmentAdminOrderBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.order));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        // Thiết lập LinearLayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminOrderBinding.rcvOrder.setLayoutManager(linearLayoutManager);
        // Khởi tạo danh sách đơn đặt hàng và Adapter
        mListOrder = new ArrayList<>();
        mAdminOrderAdapter = new AdminOrderAdapter(getActivity(), mListOrder,
                this::handleUpdateStatusOrder);
        // Gán Adapter cho RecyclerView
        mFragmentAdminOrderBinding.rcvOrder.setAdapter(mAdminOrderAdapter);
    }

    public void getListOrders() {
        // Lấy danh sách đơn đặt hàng từ Firebase
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        // Xử lý khi có thêm một đơn đặt hàng mới được thêm vào Firebase
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null || mAdminOrderAdapter == null) {
                            return;
                        }
                        // Thêm đơn đặt hàng vào danh sách
                        mListOrder.add(0, order);
                        // Cập nhật Adapter để hiển thị dữ liệu mới
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        // Xử lý khi có thông tin đơn đặt hàng được chỉnh sửa
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mAdminOrderAdapter == null) {
                            return;
                        }
                        // Tìm và cập nhật thông tin đơn đặt hàng trong danh sách
                        for (int i = 0; i < mListOrder.size(); i++) {
                            if (order.getId() == mListOrder.get(i).getId()) {
                                mListOrder.set(i, order);
                                break;
                            }
                        }
                        // Cập nhật Adapter để hiển thị dữ liệu mới
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        // Xử lý khi một đơn đặt hàng bị xóa
                        Order order = dataSnapshot.getValue(Order.class);
                        if (order == null || mListOrder == null
                                || mListOrder.isEmpty() || mAdminOrderAdapter == null) {
                            return;
                        }
                        // Tìm và xóa đơn đặt hàng khỏi danh sách
                        for (Order orderObject : mListOrder) {
                            if (order.getId() == orderObject.getId()) {
                                mListOrder.remove(orderObject);
                                break;
                            }
                        }
                        // Cập nhật Adapter để hiển thị dữ liệu mới
                        mAdminOrderAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                        // Xử lý khi có sự di chuyển của một đơn đặt hàng (chưa sử dụng trong ứng dụng này)
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi truy vấn dữ liệu
                    }
                });
    }

    private void handleUpdateStatusOrder(Order order) {
        // Cập nhật trạng thái của đơn đặt hàng
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getBookingDatabaseReference()
                .child(String.valueOf(order.getId())).child("completed").setValue(!order.isCompleted());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Giải phóng tài nguyên khi Fragment bị hủy
        if (mAdminOrderAdapter != null) {
            mAdminOrderAdapter.release();
        }
    }
}
