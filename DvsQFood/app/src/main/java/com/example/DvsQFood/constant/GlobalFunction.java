package com.example.DvsQFood.constant;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import com.pro.foodorder.activity.AdminMainActivity;
import com.pro.foodorder.activity.MainActivity;
import com.pro.foodorder.listener.IGetDateListener;
import com.pro.foodorder.prefs.DataStoreManager;
import com.pro.foodorder.utils.StringUtil;

import java.text.Normalizer;
import java.util.Calendar;
import java.util.regex.Pattern;

public class GlobalFunction {

    // Khởi tạo một Intent để mở màn hình mới
    public static void startActivity(Context context, Class<?> clz) {
        Intent intent = new Intent(context, clz);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Khởi tạo một Intent để mở màn hình mới với dữ liệu Bundle
    public static void startActivity(Context context, Class<?> clz, Bundle bundle) {
        Intent intent = new Intent(context, clz);
        intent.putExtras(bundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    // Chuyển đến màn hình chính dựa trên quyền hạn người dùng
    public static void gotoMainActivity(Context context) {
        if (DataStoreManager.getUser().isAdmin()) {
            GlobalFunction.startActivity(context, AdminMainActivity.class);
        } else {
            GlobalFunction.startActivity(context, MainActivity.class);
        }
    }

    // Ẩn bàn phím ảo trên màn hình
    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.
                    getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    // Hiển thị thông báo lỗi người dùng
    public static void showMessageError(Activity activity) {
        Toast.makeText(activity, Constant.GENERIC_ERROR, Toast.LENGTH_SHORT).show();
    }

    // Mở ứng dụng Gmail để gửi email
    public static void onClickOpenGmail(Context context) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", Constant.GMAIL, null));
        context.startActivity(Intent.createChooser(emailIntent, "Send Email"));
    }

    // Mở ứng dụng Skype để bắt đầu cuộc trò chuyện
    public static void onClickOpenSkype(Context context) {
        try {
            Uri skypeUri = Uri.parse("skype:" + Constant.SKYPE_ID + "?chat");
            context.getPackageManager().getPackageInfo("com.skype.raider", 0);
            Intent skypeIntent = new Intent(Intent.ACTION_VIEW, skypeUri);
            skypeIntent.setComponent(new ComponentName("com.skype.raider", "com.skype.raider.Main"));
            context.startActivity(skypeIntent);
        } catch (Exception e) {
            openSkypeWebview(context);
        }
    }

    // Mở Skype qua trình duyệt nếu ứng dụng không được cài đặt
    private static void openSkypeWebview(Context context) {
        try {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("skype:" + Constant.SKYPE_ID + "?chat")));
        } catch (Exception exception) {
            String skypePackageName = "com.skype.raider";
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + skypePackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + skypePackageName)));
            }
        }
    }

    // Mở ứng dụng Facebook hoặc trình duyệt web
    public static void onClickOpenFacebook(Context context) {
        Intent intent;
        try {
            String urlFacebook = Constant.PAGE_FACEBOOK;
            PackageManager packageManager = context.getPackageManager();
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) {
                urlFacebook = "fb://facewebmodal/f?href=" + Constant.LINK_FACEBOOK;
            }
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlFacebook));
        } catch (Exception e) {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.LINK_FACEBOOK));
        }
        context.startActivity(intent);
    }

    // Mở kênh YouTube trong trình duyệt
    public static void onClickOpenYoutubeChannel(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.LINK_YOUTUBE)));
    }

    // Mở ứng dụng Zalo hoặc trình duyệt web
    public static void onClickOpenZalo(Context context) {
        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constant.ZALO_LINK)));
    }

    // Gọi điện thoại đến số điện thoại cố định
    public static void callPhoneNumber(Activity activity) {
        try {
            if (Build.VERSION.SDK_INT > 22) {
                if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 101);
                    return;
                }

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Constant.PHONE_NUMBER));
                activity.startActivity(callIntent);

            } else {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:" + Constant.PHONE_NUMBER));
                activity.startActivity(callIntent);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    // Hiển thị thông báo nhỏ với nội dung được đưa vào
    public static void showToastMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    // Loại bỏ dấu tiếng Việt trong chuỗi để tìm kiếm không dấu
    public static String getTextSearch(String input) {
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    // Hiển thị hộp thoại chọn ngày
    public static void showDatePicker(Context context, String currentDate, final IGetDateListener getDateListener) {
        Calendar mCalendar = Calendar.getInstance();
        int currentDay = mCalendar.get(Calendar.DATE);
        int currentMonth = mCalendar.get(Calendar.MONTH);
        int currentYear = mCalendar.get(Calendar.YEAR);
        mCalendar.set(currentYear, currentMonth, currentDay);

        if (!StringUtil.isEmpty(currentDate)) {
            String[] split = currentDate.split("/");
            currentDay = Integer.parseInt(split[0]);
            currentMonth = Integer.parseInt(split[1]);
            currentYear = Integer.parseInt(split[2]);
            mCalendar.set(currentYear, currentMonth - 1, currentDay);
        }

        DatePickerDialog.OnDateSetListener callBack = (view, year, monthOfYear, dayOfMonth) -> {
            String date = StringUtil.getDoubleNumber(dayOfMonth) + "/" +
                    StringUtil.getDoubleNumber(monthOfYear + 1) + "/" + year;
            getDateListener.getDate(date);
        };
        DatePickerDialog datePicker = new DatePickerDialog(context,
                callBack, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                mCalendar.get(Calendar.DATE));
        datePicker.show();
    }
}
