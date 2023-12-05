package com.example.DvsQFood.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.R;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.databinding.ItemAdminOrderBinding;
import com.pro.foodorder.model.Order;
import com.pro.foodorder.utils.DateTimeUtils;

import java.util.List;

public class AdminOrderAdapter extends RecyclerView.Adapter<AdminOrderAdapter.AdminOrderViewHolder> {

    private Context mContext;
    private final List<Order> mListOrder;
    private final IUpdateStatusListener mIUpdateStatusListener;

    // Giao diện để cập nhật trạng thái đơn hàng
    public interface IUpdateStatusListener {
        void updateStatus(Order order);
    }

    public AdminOrderAdapter(Context mContext, List<Order> mListOrder, IUpdateStatusListener listener) {
        this.mContext = mContext;
        this.mListOrder = mListOrder;
        this.mIUpdateStatusListener = listener;
    }

    @NonNull
    @Override
    public AdminOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng data binding để tạo ViewHolder từ layout XML
        ItemAdminOrderBinding itemAdminOrderBinding = ItemAdminOrderBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new AdminOrderViewHolder(itemAdminOrderBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull AdminOrderViewHolder holder, int position) {
        // Lấy dữ liệu đơn hàng từ danh sách
        Order order = mListOrder.get(position);
        if (order == null) {
            return;
        }

        // Thiết lập màu nền cho item dựa vào trạng thái đơn hàng
        if (order.isCompleted()) {
            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.black_overlay));
        } else {
            holder.mItemAdminOrderBinding.layoutItem.setBackgroundColor(mContext.getResources().getColor(R.color.white));
        }

        // Thiết lập các thông tin đơn hàng vào giao diện
        holder.mItemAdminOrderBinding.chbStatus.setChecked(order.isCompleted());
        holder.mItemAdminOrderBinding.tvId.setText(String.valueOf(order.getId()));
        holder.mItemAdminOrderBinding.tvEmail.setText(order.getEmail());
        holder.mItemAdminOrderBinding.tvName.setText(order.getName());
        holder.mItemAdminOrderBinding.tvPhone.setText(order.getPhone());
        holder.mItemAdminOrderBinding.tvAddress.setText(order.getAddress());
        holder.mItemAdminOrderBinding.tvMenu.setText(order.getFoods());
        holder.mItemAdminOrderBinding.tvDate.setText(DateTimeUtils.convertTimeStampToDate(order.getId()));

        // Hiển thị tổng số tiền của đơn hàng
        String strAmount = order.getAmount() + Constant.CURRENCY;
        holder.mItemAdminOrderBinding.tvTotalAmount.setText(strAmount);

        // Xác định phương thức thanh toán và hiển thị
        String paymentMethod = "";
        if (Constant.TYPE_PAYMENT_CASH == order.getPayment()) {
            paymentMethod = Constant.PAYMENT_METHOD_CASH;
        }
        holder.mItemAdminOrderBinding.tvPayment.setText(paymentMethod);

        // Xử lý sự kiện khi người dùng thay đổi trạng thái đơn hàng
        holder.mItemAdminOrderBinding.chbStatus.setOnClickListener(
                v -> mIUpdateStatusListener.updateStatus(order));
    }

    @Override
    public int getItemCount() {
        if (mListOrder != null) {
            return mListOrder.size();
        }
        return 0;
    }

    // Phương thức giải phóng tài nguyên khi không cần thiết
    public void release() {
        mContext = null;
    }

    // ViewHolder để giữ các thành phần giao diện của mỗi item trong RecyclerView
    public static class AdminOrderViewHolder extends RecyclerView.ViewHolder {

        private final ItemAdminOrderBinding mItemAdminOrderBinding;

        public AdminOrderViewHolder(@NonNull ItemAdminOrderBinding itemAdminOrderBinding) {
            super(itemAdminOrderBinding.getRoot());
            this.mItemAdminOrderBinding = itemAdminOrderBinding;
        }
    }
}
