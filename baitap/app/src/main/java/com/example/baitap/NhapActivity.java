package com.example.baitap;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

public class NhapActivity extends AppCompatActivity {

    public static final String EXTRA_CHE_DO = "CHE_DO";
    public static final String EXTRA_MA_SV = "MA_SV";
    public static final String EXTRA_HO_TEN = "HO_TEN";
    public static final String EXTRA_LOP = "LOP";
    public static final String EXTRA_GIOI_TINH = "GIOI_TINH";
    public static final String EXTRA_DIEM = "DIEM";

    public static final int CHE_DO_THEM = 1;
    public static final int CHE_DO_SUA = 2;

    private EditText edtMa, edtTen, edtDiem;
    private Spinner spLop;
    private RadioGroup rgGioiTinh;
    private RadioButton rbNam, rbNu, rbKhac;
    private TextView txtTomTat;
    private CheckBox chkXacNhan;
    private AppCompatButton btnLuu, btnHuy;

    private int cheDo = CHE_DO_THEM;
    private String originalMa = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap);

        initViews();
        setupSpinner();
        setupEvents();

        if (savedInstanceState != null) {
            // Khôi phục trạng thái khi xoay màn hình
            restoreInstanceState(savedInstanceState);
        } else {
            // Đọc dữ liệu từ Intent ban đầu
            readIntentData();
        }

        capNhatTomTat();
        btnLuu.setEnabled(chkXacNhan.isChecked());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtMa = findViewById(R.id.edtMa);
        edtTen = findViewById(R.id.edtTen);
        spLop = findViewById(R.id.spLop);
        rgGioiTinh = findViewById(R.id.rgGioiTinh);
        rbNam = findViewById(R.id.rbNam);
        rbNu = findViewById(R.id.rbNu);
        rbKhac = findViewById(R.id.rbKhac);
        txtTomTat = findViewById(R.id.txtTomTat);
        edtDiem = findViewById(R.id.edtDiem);
        chkXacNhan = findViewById(R.id.chkXacNhan);
        btnLuu = findViewById(R.id.btnLuu);
        btnHuy = findViewById(R.id.btnHuy);
    }

    private void setupSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.array_lop,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spLop.setAdapter(adapter);
    }

    private void setupEvents() {
        // Cập nhật dòng tóm tắt khi thay đổi Spinner
        spLop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                capNhatTomTat();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // Cập nhật dòng tóm tắt khi thay đổi giới tính
        rgGioiTinh.setOnCheckedChangeListener((group, checkedId) -> capNhatTomTat());

        // Bỏ xác nhận thì vô hiệu hóa nút Lưu
        chkXacNhan.setOnCheckedChangeListener((buttonView, isChecked) -> btnLuu.setEnabled(isChecked));

        // Nút Hủy
        btnHuy.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // Nút Lưu
        btnLuu.setOnClickListener(v -> xuLyLuu());
    }

    private void readIntentData() {
        Intent intent = getIntent();
        if (intent != null) {
            cheDo = intent.getIntExtra(EXTRA_CHE_DO, CHE_DO_THEM);
            if (cheDo == CHE_DO_SUA) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.title_sua);
                }
                originalMa = intent.getStringExtra(EXTRA_MA_SV);
                if (originalMa == null) originalMa = "";

                edtMa.setText(originalMa);
                edtMa.setEnabled(false); // Mã sinh viên chỉ đọc khi sửa
                edtMa.setFocusable(false);

                String hoTen = intent.getStringExtra(EXTRA_HO_TEN);
                if (hoTen != null) edtTen.setText(hoTen);

                String lop = intent.getStringExtra(EXTRA_LOP);
                if (lop != null) {
                    String[] lopArray = getResources().getStringArray(R.array.array_lop);
                    for (int i = 0; i < lopArray.length; i++) {
                        if (lopArray[i].equalsIgnoreCase(lop)) {
                            spLop.setSelection(i);
                            break;
                        }
                    }
                }

                String gioiTinh = intent.getStringExtra(EXTRA_GIOI_TINH);
                if (gioiTinh != null) {
                    if (gioiTinh.equalsIgnoreCase("Nam")) {
                        rbNam.setChecked(true);
                    } else if (gioiTinh.equalsIgnoreCase("Nữ")) {
                        rbNu.setChecked(true);
                    } else {
                        rbKhac.setChecked(true);
                    }
                }

                double diem = intent.getDoubleExtra(EXTRA_DIEM, -1);
                if (diem >= 0) {
                    edtDiem.setText(String.valueOf(diem));
                }
            } else {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setTitle(R.string.title_nhap);
                }
                edtMa.setEnabled(true);
                edtMa.setFocusableInTouchMode(true);
            }
        }
    }

    private void restoreInstanceState(@NonNull Bundle savedInstanceState) {
        cheDo = savedInstanceState.getInt(EXTRA_CHE_DO, CHE_DO_THEM);
        originalMa = savedInstanceState.getString("ORIGINAL_MA", "");

        if (cheDo == CHE_DO_SUA) {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_sua);
            }
            edtMa.setEnabled(false);
            edtMa.setFocusable(false);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.title_nhap);
            }
            edtMa.setEnabled(true);
            edtMa.setFocusableInTouchMode(true);
        }

        edtMa.setText(savedInstanceState.getString("SAVED_MA", ""));
        edtTen.setText(savedInstanceState.getString("SAVED_TEN", ""));
        spLop.setSelection(savedInstanceState.getInt("SAVED_LOP_POS", 0));

        int genderId = savedInstanceState.getInt("SAVED_GENDER_ID", R.id.rbNam);
        rgGioiTinh.check(genderId);

        edtDiem.setText(savedInstanceState.getString("SAVED_DIEM", ""));
        boolean xacNhan = savedInstanceState.getBoolean("SAVED_XAC_NHAN", false);
        chkXacNhan.setChecked(xacNhan);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(EXTRA_CHE_DO, cheDo);
        outState.putString("ORIGINAL_MA", originalMa);
        outState.putString("SAVED_MA", edtMa.getText().toString());
        outState.putString("SAVED_TEN", edtTen.getText().toString());
        outState.putInt("SAVED_LOP_POS", spLop.getSelectedItemPosition());
        outState.putInt("SAVED_GENDER_ID", rgGioiTinh.getCheckedRadioButtonId());
        outState.putString("SAVED_DIEM", edtDiem.getText().toString());
        outState.putBoolean("SAVED_XAC_NHAN", chkXacNhan.isChecked());
    }

    private void capNhatTomTat() {
        String lop = spLop.getSelectedItem() != null ? spLop.getSelectedItem().toString() : "";
        String gioiTinh = layGioiTinhDaChon();
        txtTomTat.setText("Tóm tắt: Lớp " + lop + " | Giới tính: " + gioiTinh);
    }

    private String layGioiTinhDaChon() {
        int checkedId = rgGioiTinh.getCheckedRadioButtonId();
        if (checkedId == R.id.rbNu) {
            return "Nữ";
        } else if (checkedId == R.id.rbKhac) {
            return "Khác";
        } else {
            return "Nam";
        }
    }

    private void xuLyLuu() {
        // Kiểm tra CheckBox xác nhận
        if (!chkXacNhan.isChecked()) {
            Toast.makeText(this, "Vui lòng chọn xác nhận thông tin trước khi lưu!", Toast.LENGTH_SHORT).show();
            return;
        }

        String ma = edtMa.getText().toString().trim().toUpperCase();
        String hoTen = edtTen.getText().toString().trim();
        String diemStr = edtDiem.getText().toString().trim();

        // 1. Kiểm tra mã sinh viên
        if (ma.isEmpty()) {
            edtMa.setError("Mã sinh viên không được để trống!");
            edtMa.requestFocus();
            return;
        }

        // 2. Kiểm tra họ tên
        if (hoTen.isEmpty()) {
            edtTen.setError("Họ tên không được để trống!");
            edtTen.requestFocus();
            return;
        }

        // 3. Kiểm tra điểm
        if (diemStr.isEmpty()) {
            edtDiem.setError("Điểm trung bình không được để trống!");
            edtDiem.requestFocus();
            return;
        }

        double diem;
        try {
            diem = Double.parseDouble(diemStr);
        } catch (NumberFormatException e) {
            edtDiem.setError("Điểm phải là số thực hợp lệ (dùng dấu chấm)!");
            edtDiem.requestFocus();
            return;
        }

        if (Double.isNaN(diem) || Double.isInfinite(diem) || diem < 0.0 || diem > 10.0) {
            edtDiem.setError("Điểm trung bình phải từ 0.0 đến 10.0!");
            edtDiem.requestFocus();
            return;
        }

        String lop = spLop.getSelectedItem() != null ? spLop.getSelectedItem().toString() : "CNTT01";
        String gioiTinh = layGioiTinhDaChon();

        // Trả kết quả về cho MainActivity
        Intent resultIntent = new Intent();
        resultIntent.putExtra(EXTRA_CHE_DO, cheDo);
        resultIntent.putExtra(EXTRA_MA_SV, ma);
        resultIntent.putExtra("ORIGINAL_MA", originalMa);
        resultIntent.putExtra(EXTRA_HO_TEN, hoTen);
        resultIntent.putExtra(EXTRA_LOP, lop);
        resultIntent.putExtra(EXTRA_GIOI_TINH, gioiTinh);
        resultIntent.putExtra(EXTRA_DIEM, diem);

        setResult(RESULT_OK, resultIntent);
        finish();
    }
}
