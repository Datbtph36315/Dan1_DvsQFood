package com.example.DvsQFood.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.pro.foodorder.databinding.ItemFeedbackBinding;
import com.pro.foodorder.model.Feedback;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {

    private final List<Feedback> mListFeedback;

    // Constructor của Adapter, nhận danh sách Feedback từ Activity hoặc Fragment
    public FeedbackAdapter(List<Feedback> mListFeedback) {
        this.mListFeedback = mListFeedback;
    }

    // Phương thức được gọi khi RecyclerView cần tạo một ViewHolder mới
    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng data binding để tạo ViewHolder từ layout XML
        ItemFeedbackBinding itemFeedbackBinding = ItemFeedbackBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false);
        return new FeedbackViewHolder(itemFeedbackBinding);
    }

    // Phương thức được gọi để hiển thị dữ liệu tại một vị trí cụ thể
    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, int position) {
        Feedback feedback = mListFeedback.get(position);
        if (feedback == null) {
            return;
        }

        // Thiết lập dữ liệu của mỗi item vào giao diện
        holder.mItemFeedbackBinding.tvEmail.setText(feedback.getEmail());
        holder.mItemFeedbackBinding.tvFeedback.setText(feedback.getComment());
    }

    // Phương thức trả về số lượng item trong danh sách
    @Override
    public int getItemCount() {
        if (mListFeedback != null) {
            return mListFeedback.size();
        }
        return 0;
    }

    // ViewHolder để giữ các thành phần giao diện của mỗi item trong RecyclerView
    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        private final ItemFeedbackBinding mItemFeedbackBinding;

        // Constructor của ViewHolder, khởi tạo các thành phần giao diện
        public FeedbackViewHolder(@NonNull ItemFeedbackBinding itemFeedbackBinding) {
            super(itemFeedbackBinding.getRoot());
            this.mItemFeedbackBinding = itemFeedbackBinding;
        }
    }
}
