package com.example.ontap1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class QuanLySinhvienActivity extends AppCompatActivity {

    private ThaoTacSinhVien thaoTacSinhVien;
    private final List<SinhVien> danhSach = new ArrayList<>();
    private SinhVienAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quan_ly_sinhvien);

        View headerLayout = findViewById(R.id.headerLayout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            if (headerLayout != null) {
                headerLayout.setPadding(0, systemBars.top, 0, 0);
            }
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nút quay lại màn hình chính
        View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        thaoTacSinhVien = new ThaoTacSinhVien(this);
        ListView listSV = findViewById(R.id.ListSV);
        Button btnThem = findViewById(R.id.btnThem);

        adapter = new SinhVienAdapter(this, danhSach);
        listSV.setAdapter(adapter);

        // Nút Menu 3 chấm mở menu tùy chọn
        android.view.View btnMenu = findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                PopupMenu popup = new PopupMenu(this, v);
                popup.getMenuInflater().inflate(R.menu.menu_lua_chon, popup.getMenu());
                popup.setOnMenuItemClickListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.menu_cai_dat) {
                        Toast.makeText(this, "Chức năng Cài đặt", Toast.LENGTH_SHORT).show();
                        return true;
                    } else if (itemId == R.id.menu_gioi_thieu) {
                        new AlertDialog.Builder(this)
                                .setTitle("Giới thiệu")
                                .setMessage("Ứng dụng Quản lý Sinh viên\nPhiên bản: 1.0\nHọc phần: Lập trình Android")
                                .setPositiveButton("Đóng", null)
                                .show();
                        return true;
                    } else if (itemId == R.id.menu_thoat) {
                        finish();
                        return true;
                    }
                    return false;
                });
                popup.show();
            });
        }

        // Nhấn Thêm để mở màn hình nhập mới.
        btnThem.setOnClickListener(v -> {
            startActivity(new Intent(this, NhapSinhVienActivity.class));
        });

        // Nhấn vào một sinh viên để sửa.
        listSV.setOnItemClickListener((parent, view, position, id) -> {
            SinhVien sv = danhSach.get(position);
            Intent intent = new Intent(this, NhapSinhVienActivity.class);
            intent.putExtra("maSV", sv.getMaSV());
            startActivity(intent);
        });

        // Nhấn giữ để mở menu Xóa trong menu_xoa.xml.
        listSV.setOnItemLongClickListener((parent, view, position, id) -> {
            SinhVien sv = danhSach.get(position);
            PopupMenu popup = new PopupMenu(this, view);
            popup.getMenuInflater().inflate(R.menu.menu_xoa, popup.getMenu());
            popup.setOnMenuItemClickListener(item -> {
                if (item.getItemId() == R.id.menu_xoa) {
                    xacNhanXoa(sv);
                    return true;
                }
                return false;
            });
            popup.show();
            return true;
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        hienThiDanhSach();
    }

    private void hienThiDanhSach() {
        if (adapter == null || thaoTacSinhVien == null) return;
        danhSach.clear();
        danhSach.addAll(thaoTacSinhVien.layTatCaSinhVien());
        adapter.notifyDataSetChanged();
    }

    private void xacNhanXoa(SinhVien sv) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Xóa sinh viên " + sv.getMaSV() + " - "
                        + sv.getHoTen() + "?")
                .setNegativeButton("Hủy", null)
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean thanhCong = thaoTacSinhVien.xoaSinhVien(sv.getMaSV());
                    if (thanhCong) hienThiDanhSach();
                    Toast.makeText(this,
                            thanhCong ? "Đã xóa sinh viên" : "Không thể xóa sinh viên",
                            Toast.LENGTH_SHORT).show();
                })
                .show();
    }
}
