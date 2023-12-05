package com.example.DvsQFood.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemRevenueBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.DateTimeUtils;

import java.util.List;

public class RevenueAdapter extends RecyclerView.Adapter<RevenueAdapter.RevenueViewHolder> {

    private final List<Order> mListOrder;

    // Constructor, nhận danh sách các đơn đặt hàng
    public RevenueAdapter(List<Order> mListOrder) {
        this.mListOrder = mListOrder;
    }

    // Phương thức này được gọi khi RecyclerView cần tạo một ViewHolder mới
    @NonNull
    @Override
    public RevenueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng data binding để tạo ViewHolder từ layout XML
        ItemRevenueBinding itemRevenueBinding = ItemRevenueBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new RevenueViewHolder(itemRevenueBinding);
    }

    // Phương thức này được gọi để hiển thị dữ liệu tại một vị trí cụ thể
    @Override
    public void onBindViewHolder(@NonNull RevenueViewHolder holder, int position) {
        // Lấy dữ liệu đơn đặt hàng từ danh sách
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }

        // Hiển thị thông tin đơn đặt hàng trong giao diện
        holder.mItemRevenueBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemRevenueBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate_2(order.getId()));

        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemRevenueBinding.tvTotalAmount.setText(strAmount);
    }

    // Phương thức trả về số lượng item trong danh sách
    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    // ViewHolder để giữ các thành phần giao diện của mỗi item trong RecyclerView
    public static class RevenueViewHolder extends RecyclerView.ViewHolder {

        private final ItemRevenueBinding mItemRevenueBinding;

        // Constructor của ViewHolder, khởi tạo các thành phần giao diện
        public RevenueViewHolder(@NonNull ItemRevenueBinding itemRevenueBinding) {
            super(itemRevenueBinding.getRoot());
            this.mItemRevenueBinding = itemRevenueBinding;
        }
    }
}
