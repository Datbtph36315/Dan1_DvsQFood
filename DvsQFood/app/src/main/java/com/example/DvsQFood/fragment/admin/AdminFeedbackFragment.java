package com.example.DvsQFood.fragment.admin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.AdminMainActivity;
import com.pro.foodorder.adapter.FeedbackAdapter;
import com.pro.foodorder.databinding.FragmentAdminFeedbackBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.model.Feedback;

import java.util.ArrayList;
import java.util.List;

public class AdminFeedbackFragment extends BaseFragment {

    private FragmentAdminFeedbackBinding mFragmentAdminFeedbackBinding;
    private List<Feedback> mListFeedback;
    private FeedbackAdapter mFeedbackAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminFeedbackBinding = FragmentAdminFeedbackBinding.inflate(inflater, container, false);

        initView();
        getListFeedback();
        return mFragmentAdminFeedbackBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.feedback));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        // Thiết lập LinearLayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminFeedbackBinding.rcvFeedback.setLayoutManager(linearLayoutManager);
    }

    // Phương thức để lấy danh sách phản hồi từ Firebase
    public void getListFeedback() {
        if (getActivity() == null) {
            return;
        }
        // Thực hiện truy vấn dữ liệu từ Firebase
        ControllerApplication.get(getActivity()).getFeedbackDatabaseReference()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        // Xử lý dữ liệu khi có sự thay đổi
                        if (mListFeedback != null) {
                            mListFeedback.clear();
                        } else {
                            mListFeedback = new ArrayList<>();
                        }
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Feedback feedback = dataSnapshot.getValue(Feedback.class);
                            if (feedback != null) {
                                // Thêm phản hồi vào danh sách, đảm bảo thứ tự mới nhất được hiển thị đầu tiên
                                mListFeedback.add(0, feedback);
                            }
                        }
                        // Khởi tạo và cập nhật Adapter cho RecyclerView
                        mFeedbackAdapter = new FeedbackAdapter(mListFeedback);
                        mFragmentAdminFeedbackBinding.rcvFeedback.setAdapter(mFeedbackAdapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Xử lý khi có lỗi truy vấn
                    }
                });
    }
}
