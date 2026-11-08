package com.example.ontap1;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class NhapSinhVienActivity extends AppCompatActivity {

    private EditText edtMaSV, edtHoTen, edtEmail;
    private Spinner spLop, spTrangThai;
    private RadioGroup rgGioiTinh;
    private CheckBox cbDuLich, cbTheThao, cbDocSach;
    private ThaoTacSinhVien thaoTacSinhVien;
    private String maSVDangSua;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_nhap_sinh_vien);

        View headerLayout = findViewById(R.id.headerLayout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            if (headerLayout != null) {
                headerLayout.setPadding(0, systemBars.top, 0, 0);
            }
            v.setPadding(systemBars.left, 0, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Nút quay lại màn hình danh sách
        android.view.View btnBack = findViewById(R.id.btnBack);
        if (btnBack != null) {
            btnBack.setOnClickListener(v -> finish());
        }

        edtMaSV = findViewById(R.id.editTextText);
        edtHoTen = findViewById(R.id.editTextText2);
        edtEmail = findViewById(R.id.edtEmail);
        spLop = findViewById(R.id.spLop);
        spTrangThai = findViewById(R.id.spTrangThai);
        rgGioiTinh = findViewById(R.id.RaGGiotinh);
        cbDuLich = findViewById(R.id.checkBox1);
        cbTheThao = findViewById(R.id.checkBox2);
        cbDocSach = findViewById(R.id.checkBox3);
        Button btnLuu = findViewById(R.id.btnLuu);

        thaoTacSinhVien = new ThaoTacSinhVien(this);
        maSVDangSua = getIntent().getStringExtra("maSV");

        if (maSVDangSua != null) {
            napDuLieuDeSua(maSVDangSua);
        }

        btnLuu.setOnClickListener(v -> luuSinhVien());
    }

    private void napDuLieuDeSua(String maSV) {
        SinhVien sinhVienCanSua = null;
        for (SinhVien sv : thaoTacSinhVien.layTatCaSinhVien()) {
            if (sv.getMaSV().equalsIgnoreCase(maSV)) {
                sinhVienCanSua = sv;
                break;
            }
        }

        if (sinhVienCanSua == null) {
            Toast.makeText(this, "Không tìm thấy sinh viên", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        android.widget.TextView tvTitle = findViewById(R.id.textView);
        if (tvTitle != null) {
            tvTitle.setText("CẬP NHẬT SINH VIÊN");
        }

        edtMaSV.setText(sinhVienCanSua.getMaSV());
        edtMaSV.setEnabled(false); // Mã sinh viên là khóa, không sửa khi cập nhật.
        edtHoTen.setText(sinhVienCanSua.getHoTen());
        edtEmail.setText(sinhVienCanSua.getEmail());
        chonSpinner(spLop, sinhVienCanSua.getLop());
        chonSpinner(spTrangThai, sinhVienCanSua.getTrangThai());

        String gioiTinh = chuanHoa(sinhVienCanSua.getGioiTinh());
        if (gioiTinh.equals("nam")) {
            rgGioiTinh.check(R.id.rabNam);
        } else if (gioiTinh.equals("nu")) {
            rgGioiTinh.check(R.id.rabNu);
        } else {
            rgGioiTinh.check(R.id.rabKhac);
        }

        String soThich = chuanHoa(sinhVienCanSua.getSoThich());
        cbDuLich.setChecked(soThich.contains("du lich"));
        cbTheThao.setChecked(soThich.contains("the thao"));
        cbDocSach.setChecked(soThich.contains("doc sach"));
    }

    private void chonSpinner(Spinner spinner, String giaTri) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (chuanHoa(spinner.getItemAtPosition(i).toString())
                    .equals(chuanHoa(giaTri))) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private String chuanHoa(String value) {
        if (value == null) return "";
        String s = Normalizer.normalize(value.toLowerCase(Locale.ROOT),
                Normalizer.Form.NFD);
        return s.replaceAll("\\p{M}+", "").replace('đ', 'd').replace("-", "").replaceAll("\\s+", "").trim();
    }

    private String layGioiTinh() {
        int id = rgGioiTinh.getCheckedRadioButtonId();
        if (id == R.id.rabNam) return "Nam";
        if (id == R.id.rabNu) return "Nữ";
        if (id == R.id.rabKhac) return "Khác";
        return "";
    }

    private String laySoThich() {
        List<String> ds = new ArrayList<>();
        if (cbDuLich.isChecked()) ds.add("Du lich");
        if (cbTheThao.isChecked()) ds.add("The thao");
        if (cbDocSach.isChecked()) ds.add("Doc sach");
        // Không dùng dấu phẩy vì định dạng SinhVien.txt dùng dấu phẩy phân cột.
        return android.text.TextUtils.join("; ", ds);
    }

    private boolean khongHopLeChoTXT(String chuoi) {
        return chuoi.contains(",") || chuoi.contains("\n") || chuoi.contains("\r");
    }

    private void luuSinhVien() {
        String maSV = edtMaSV.getText().toString().trim();
        String hoTen = edtHoTen.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String lop = spLop.getSelectedItem() == null ? ""
                : spLop.getSelectedItem().toString();
        String trangThai = spTrangThai.getSelectedItem() == null ? ""
                : spTrangThai.getSelectedItem().toString();
        String gioiTinh = layGioiTinh();
        String soThich = laySoThich();

        if (maSV.isEmpty()) {
            edtMaSV.setError("Vui lòng nhập mã sinh viên");
            edtMaSV.requestFocus();
            return;
        }
        if (hoTen.isEmpty()) {
            edtHoTen.setError("Vui lòng nhập họ tên");
            edtHoTen.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không hợp lệ");
            edtEmail.requestFocus();
            return;
        }
        if (lop.isEmpty() || trangThai.isEmpty() || gioiTinh.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ thông tin",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        if (khongHopLeChoTXT(maSV) || khongHopLeChoTXT(hoTen)
                || khongHopLeChoTXT(email) || khongHopLeChoTXT(lop)
                || khongHopLeChoTXT(gioiTinh) || khongHopLeChoTXT(soThich)
                || khongHopLeChoTXT(trangThai)) {
            Toast.makeText(this, "Không nhập dấu phẩy hoặc ký tự xuống dòng",
                    Toast.LENGTH_LONG).show();
            return;
        }

        SinhVien sv = new SinhVien(maSV, hoTen, email, lop,
                gioiTinh, soThich, trangThai);
        boolean thanhCong;
        if (maSVDangSua == null) {
            thanhCong = thaoTacSinhVien.themSinhVien(sv);
        } else {
            thanhCong = thaoTacSinhVien.suaSinhVien(sv);
        }

        if (thanhCong) {
            Toast.makeText(this, "Đã lưu sinh viên", Toast.LENGTH_SHORT).show();
            finish(); // Quay về danh sách; onResume() sẽ tải lại dữ liệu.
        } else {
            Toast.makeText(this,
                    maSVDangSua == null
                            ? "Mã sinh viên đã có hoặc không thể ghi file"
                            : "Không thể cập nhật sinh viên",
                    Toast.LENGTH_LONG).show();
        }
    }
}
