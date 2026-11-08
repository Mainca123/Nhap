package com.example.baitap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.Toolbar;

import java.util.Locale;

public class ChiTietActivity extends AppCompatActivity {

    public static final String EXTRA_SINH_VIEN = "EXTRA_SINH_VIEN";
    public static final String EXTRA_YEU_CAU_SUA = "YEU_CAU_SUA";

    private SinhVien sinhVien;
    private TextView txtChiTiet;
    private AppCompatButton btnSua, btnChiaSe, btnVe;
    private TextView btnTuyChon;
    private ImageView ivAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet);

        initViews();
        readDataFromIntent();
        setupEvents();
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtChiTiet = findViewById(R.id.txtChiTiet);
        btnSua = findViewById(R.id.btnSua);
        btnChiaSe = findViewById(R.id.btnChiaSe);
        btnVe = findViewById(R.id.btnVe);
        btnTuyChon = findViewById(R.id.btnTuyChon);
        ivAvatar = findViewById(R.id.ivAvatar);
    }

    private void readDataFromIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra(EXTRA_SINH_VIEN)) {
                sinhVien = (SinhVien) intent.getSerializableExtra(EXTRA_SINH_VIEN);
            } else if (intent.hasExtra(NhapActivity.EXTRA_MA_SV)) {
                String ma = intent.getStringExtra(NhapActivity.EXTRA_MA_SV);
                String hoTen = intent.getStringExtra(NhapActivity.EXTRA_HO_TEN);
                String lop = intent.getStringExtra(NhapActivity.EXTRA_LOP);
                String gioiTinh = intent.getStringExtra(NhapActivity.EXTRA_GIOI_TINH);
                double diem = intent.getDoubleExtra(NhapActivity.EXTRA_DIEM, 0.0);
                sinhVien = new SinhVien(ma, hoTen, lop, gioiTinh, diem);
            }
        }

        if (sinhVien == null) {
            Toast.makeText(this, "Không có dữ liệu chi tiết sinh viên!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        hienThiThongTin();
    }

    private void hienThiThongTin() {
        String diemStr = String.format(Locale.US, "%.1f", sinhVien.getDiem());
        String info = "Mã sinh viên: " + sinhVien.getMa() + "\n"
                + "Họ tên: " + sinhVien.getHoTen() + "\n"
                + "Lớp: " + sinhVien.getLop() + "\n"
                + "Giới tính: " + sinhVien.getGioiTinh() + "\n"
                + "Điểm trung bình: " + diemStr;
        txtChiTiet.setText(info);
    }

    private void setupEvents() {
        // Nút Sửa: trả mã và yêu cầu sửa về MainActivity
        btnSua.setOnClickListener(v -> {
            Intent result = new Intent();
            result.putExtra(EXTRA_YEU_CAU_SUA, true);
            result.putExtra(NhapActivity.EXTRA_MA_SV, sinhVien.getMa());
            setResult(RESULT_OK, result);
            finish();
        });

        // Nút Chia sẻ
        btnChiaSe.setOnClickListener(v -> thucHienChiaSe());

        // Nút Quay lại
        btnVe.setOnClickListener(v -> finish());

        // Nút Popup tùy chọn (neo PopupMenu)
        btnTuyChon.setOnClickListener(this::showPopupMenu);
    }

    private void showPopupMenu(View anchor) {
        PopupMenu popupMenu = new PopupMenu(this, anchor);
        popupMenu.getMenuInflater().inflate(R.menu.menu_popup, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(item -> {
            int id = item.getItemId();
            if (id == R.id.menu_popup_chia_se) {
                thucHienChiaSe();
                return true;
            } else if (id == R.id.menu_popup_mo_website) {
                thucHienMoWebsite();
                return true;
            }
            return false;
        });
        popupMenu.show();
    }

    private void thucHienChiaSe() {
        if (sinhVien == null) return;
        String content = "Hồ sơ sinh viên:\n"
                + "- Mã SV: " + sinhVien.getMa() + "\n"
                + "- Họ tên: " + sinhVien.getHoTen() + "\n"
                + "- Lớp: " + sinhVien.getLop() + "\n"
                + "- Giới tính: " + sinhVien.getGioiTinh() + "\n"
                + "- Điểm TB: " + String.format(Locale.US, "%.1f", sinhVien.getDiem());

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Thông tin sinh viên " + sinhVien.getHoTen());
        intent.putExtra(Intent.EXTRA_TEXT, content);

        try {
            startActivity(Intent.createChooser(intent, "Chia sẻ thông tin sinh viên"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Không tìm thấy ứng dụng phù hợp để chia sẻ!", Toast.LENGTH_SHORT).show();
        }
    }

    private void thucHienMoWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"));
        try {
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Không tìm thấy trình duyệt web trên thiết bị!", Toast.LENGTH_SHORT).show();
        }
    }
}
