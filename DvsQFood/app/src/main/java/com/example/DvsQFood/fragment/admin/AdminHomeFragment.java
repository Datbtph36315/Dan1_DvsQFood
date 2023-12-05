package com.example.DvsQFood.fragment.admin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.pro.foodorder.ControllerApplication;
import com.pro.foodorder.R;
import com.pro.foodorder.activity.AddFoodActivity;
import com.pro.foodorder.activity.AdminMainActivity;
import com.pro.foodorder.adapter.AdminFoodAdapter;
import com.pro.foodorder.constant.Constant;
import com.pro.foodorder.constant.GlobalFunction;
import com.pro.foodorder.databinding.FragmentAdminHomeBinding;
import com.pro.foodorder.fragment.BaseFragment;
import com.pro.foodorder.listener.IOnManagerFoodListener;
import com.pro.foodorder.model.Food;
import com.pro.foodorder.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class AdminHomeFragment extends BaseFragment {

    private FragmentAdminHomeBinding mFragmentAdminHomeBinding;
    private List<Food> mListFood;
    private AdminFoodAdapter mAdminFoodAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentAdminHomeBinding = FragmentAdminHomeBinding.inflate(inflater, container, false);

        initView();
        initListener();
        getListFood("");
        return mFragmentAdminHomeBinding.getRoot();
    }

    @Override
    protected void initToolbar() {
        if (getActivity() != null) {
            ((AdminMainActivity) getActivity()).setToolBar(getString(R.string.home));
        }
    }

    private void initView() {
        if (getActivity() == null) {
            return;
        }
        // Thiết lập LinearLayoutManager cho RecyclerView
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mFragmentAdminHomeBinding.rcvFood.setLayoutManager(linearLayoutManager);
        // Khởi tạo danh sách thức ăn và Adapter
        mListFood = new ArrayList<>();
        mAdminFoodAdapter = new AdminFoodAdapter(mListFood, new IOnManagerFoodListener() {
            @Override
            public void onClickUpdateFood(Food food) {
                onClickEditFood(food);
            }

            @Override
            public void onClickDeleteFood(Food food) {
                deleteFoodItem(food);
            }
        });
        // Gán Adapter cho RecyclerView
        mFragmentAdminHomeBinding.rcvFood.setAdapter(mAdminFoodAdapter);
    }

    private void initListener() {
        // Xử lý sự kiện khi click vào nút thêm mới thức ăn
        mFragmentAdminHomeBinding.btnAddFood.setOnClickListener(v -> onClickAddFood());

        // Xử lý sự kiện khi click vào nút tìm kiếm
        mFragmentAdminHomeBinding.imgSearch.setOnClickListener(view1 -> searchFood());

        // Xử lý sự kiện khi người dùng nhập xong từ khóa tìm kiếm
        mFragmentAdminHomeBinding.edtSearchName.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchFood();
                return true;
            }
            return false;
        });

        // Xử lý sự kiện khi người dùng thay đổi nội dung trong ô tìm kiếm
        mFragmentAdminHomeBinding.edtSearchName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // Xử lý sự kiện khi nội dung của ô tìm kiếm thay đổi
                String strKey = s.toString().trim();
                if (strKey.equals("") || strKey.length() == 0) {
                    searchFood();
                }
            }
        });
    }

    private void onClickAddFood() {
        // Chuyển đến màn hình thêm mới thức ăn
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class);
    }

    private void onClickEditFood(Food food) {
        // Chuyển đến màn hình chỉnh sửa thông tin thức ăn
        Bundle bundle = new Bundle();
        bundle.putSerializable(Constant.KEY_INTENT_FOOD_OBJECT, food);
        GlobalFunction.startActivity(getActivity(), AddFoodActivity.class, bundle);
    }

    private void deleteFoodItem(Food food) {
        // Xác nhận xóa món ăn
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.msg_delete_title))
                .setMessage(getString(R.string.msg_confirm_delete))
                .setPositiveButton(getString(R.string.action_ok), (dialogInterface, i) -> {
                    // Xác nhận xóa và thực hiện xóa dữ liệu từ Firebase
                    if (getActivity() == null) {
                        return;
                    }
                    ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                            .child(String.valueOf(food.getId())).removeValue((error, ref) ->
                                    Toast.makeText(getActivity(),
                                            getString(R.string.msg_delete_movie_successfully), Toast.LENGTH_SHORT).show());
                })
                .setNegativeButton(getString(R.string.action_cancel), null)
                .show();
    }

    private void searchFood() {
        // Tìm kiếm thức ăn dựa trên từ khóa
        String strKey = mFragmentAdminHomeBinding.edtSearchName.getText().toString().trim();
        if (mListFood != null) {
            mListFood.clear();
        } else {
            mListFood = new ArrayList<>();
        }
        // Gọi phương thức để lấy danh sách thức ăn từ Firebase
        getListFood(strKey);
        // Ẩn bàn phím ảo sau khi tìm kiếm
        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    public void getListFood(String keyword) {
        // Lấy danh sách thức ăn từ Firebase
        if (getActivity() == null) {
            return;
        }
        ControllerApplication.get(getActivity()).getFoodDatabaseReference()
                .addChildEventListener(new ChildEventListener() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, String s) {
                        // Xử lý khi có thêm một món ăn mới được thêm vào Firebase
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null || mListFood == null || mAdminFoodAdapter == null) {
                            return;
                        }
                        // Kiểm tra xem nếu từ khóa tìm kiếm không tồn tại thì thêm vào danh sách
                        if (StringUtil.isEmpty(keyword)) {
                            mListFood.add(0, food);
                        } else {
                            // Nếu có từ khóa, kiểm tra xem tên món ăn có chứa từ khóa hay không
                            if (GlobalFunction.getTextSearch(food.getName()).toLowerCase().trim()
                                    .contains(GlobalFunction.getTextSearch(keyword).toLowerCase().trim())) {
                                // Nếu chứa, thêm vào danh sách
                                mListFood.add(0, food);
                            }
                        }
                        // Cập nhật Adapter để hiển thị dữ liệu mới
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, String s) {
                        // Xử lý khi có thông tin món ăn được chỉnh sửa
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null || mListFood == null
                                || mListFood.isEmpty() || mAdminFoodAdapter == null) {
                            return;
                        }
                        // Tìm và cập nhật thông tin món ăn trong danh sách
                        for (int i = 0; i < mListFood.size(); i++) {
                            if (food.getId() == mListFood.get(i).getId()) {
                                mListFood.set(i, food);
                                break;
                            }
                        }
                        // Cập nhật Adapter để hiển thị dữ liệu mới
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        // Xử lý khi một món ăn bị xóa
                        Food food = dataSnapshot.getValue(Food.class);
                        if (food == null || mListFood == null
                                || mListFood.isEmpty() || mAdminFoodAdapter == null) {
                            return;
                        }
                        // Tìm và xóa món ăn khỏi danh sách
                        for (Food foodObject : mListFood) {
                            if (food.getId() == foodObject.getId()) {
                                mListFood.remove(foodObject);
                                break;
                            }
                        }
                        // Cập nhật Adapter để hiển thị dữ liệu mới
                        mAdminFoodAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, String s) {
                        // Xử lý khi có sự di chuyển của một món ăn (chưa sử dụng trong ứng dụng này)
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Xử lý khi có lỗi truy vấn dữ liệu
                    }
                });
    }
}
