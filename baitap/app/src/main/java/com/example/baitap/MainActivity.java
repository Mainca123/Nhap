package com.example.baitap;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText edtTim;
    private TextView txtSoLuong;
    private ListView lvSinhVien;

    private ArrayList<SinhVien> danhSachGoc = new ArrayList<>();
    private ArrayList<SinhVien> danhSachHienThi = new ArrayList<>();
    private SinhVienAdapter adapter;

    private boolean hasUnsavedChanges = false;

    private ActivityResultLauncher<Intent> launcherNhap;
    private ActivityResultLauncher<Intent> launcherChiTiet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupActivityLaunchers();
        setupListView();
        setupSearch();
        setupBackPressHandler();

        if (savedInstanceState != null) {
            // Khôi phục trạng thái khi xoay màn hình
            @SuppressWarnings("unchecked")
            ArrayList<SinhVien> savedList = (ArrayList<SinhVien>) savedInstanceState.getSerializable("DANH_SACH_GOC");
            if (savedList != null) {
                danhSachGoc = savedList;
            }
            hasUnsavedChanges = savedInstanceState.getBoolean("HAS_UNSAVED_CHANGES", false);
        } else {
            // Mở lần đầu: đọc file nếu có sẵn, nếu chưa có tạo 10 sinh viên mẫu
            if (SinhVienFileStore.hasJsonFile(this)) {
                try {
                    danhSachGoc = SinhVienFileStore.docDanhSach(this);
                    hasUnsavedChanges = false;
                    
                    if (danhSachGoc.isEmpty()) {
                        // File có tồn tại nhưng trống (hoặc chứa []), tiến hành xóa file
                        deleteFile(SinhVienFileStore.FILE_JSON);
                        khoiTaoDuLieuMau();
                    }
                } catch (Exception e) {
                    danhSachGoc = new ArrayList<>();
                    Toast.makeText(this, "Lỗi đọc file ban đầu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    // Nếu lỗi file thì cũng xóa và tạo mẫu lại
                    deleteFile(SinhVienFileStore.FILE_JSON);
                    khoiTaoDuLieuMau();
                }
            } else {
                khoiTaoDuLieuMau();
            }
        }

        capNhatHienThi();
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        edtTim = findViewById(R.id.edtTim);
        txtSoLuong = findViewById(R.id.txtSoLuong);
        lvSinhVien = findViewById(R.id.lvSinhVien);

        // Đăng ký Context Menu cho ListView
        registerForContextMenu(lvSinhVien);
    }

    private void setupActivityLaunchers() {
        // Launcher cho màn hình Nhập/Sửa
        launcherNhap = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        int cheDo = data.getIntExtra(NhapActivity.EXTRA_CHE_DO, NhapActivity.CHE_DO_THEM);
                        String ma = data.getStringExtra(NhapActivity.EXTRA_MA_SV);
                        String originalMa = data.getStringExtra("ORIGINAL_MA");
                        String ten = data.getStringExtra(NhapActivity.EXTRA_HO_TEN);
                        String lop = data.getStringExtra(NhapActivity.EXTRA_LOP);
                        String gioiTinh = data.getStringExtra(NhapActivity.EXTRA_GIOI_TINH);
                        double diem = data.getDoubleExtra(NhapActivity.EXTRA_DIEM, 0.0);

                        if (ma == null || ma.isEmpty()) return;

                        if (cheDo == NhapActivity.CHE_DO_THEM) {
                            // Kiểm tra trùng mã sinh viên
                            boolean daTonTai = false;
                            for (SinhVien sv : danhSachGoc) {
                                if (sv.getMa().equalsIgnoreCase(ma)) {
                                    daTonTai = true;
                                    break;
                                }
                            }

                            if (daTonTai) {
                                Toast.makeText(this, "Mã sinh viên " + ma + " đã tồn tại trong danh sách! Không thể thêm.", Toast.LENGTH_LONG).show();
                                return;
                            }

                            SinhVien svMoi = new SinhVien(ma, ten, lop, gioiTinh, diem);
                            danhSachGoc.add(svMoi);
                            hasUnsavedChanges = true;

                            // Ghi lịch sử thao tác THEM
                            try {
                                SinhVienFileStore.ghiLichSu(this, "THEM", ma);
                            } catch (IOException e) {
                                Toast.makeText(this, "Lỗi ghi lịch sử: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            capNhatHienThi();
                            Toast.makeText(this, "Đã thêm sinh viên " + ma + " thành công!", Toast.LENGTH_SHORT).show();

                        } else if (cheDo == NhapActivity.CHE_DO_SUA) {
                            String targetMa = (originalMa != null && !originalMa.isEmpty()) ? originalMa : ma;
                            int index = -1;
                            for (int i = 0; i < danhSachGoc.size(); i++) {
                                if (danhSachGoc.get(i).getMa().equalsIgnoreCase(targetMa)) {
                                    index = i;
                                    break;
                                }
                            }

                            if (index != -1) {
                                SinhVien sv = danhSachGoc.get(index);
                                sv.setHoTen(ten);
                                sv.setLop(lop);
                                sv.setGioiTinh(gioiTinh);
                                sv.setDiem(diem);
                                hasUnsavedChanges = true;

                                // Ghi lịch sử thao tác SUA
                                try {
                                SinhVienFileStore.ghiLichSu(this, "SUA", targetMa);
                                } catch (IOException e) {
                                    Toast.makeText(this, "Lỗi ghi lịch sử: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                }

                                capNhatHienThi();
                                Toast.makeText(this, "Cập nhật sinh viên " + targetMa + " thành công!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
        );

        // Launcher cho màn hình Chi tiết
        launcherChiTiet = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Intent data = result.getData();
                        boolean yeuCauSua = data.getBooleanExtra(ChiTietActivity.EXTRA_YEU_CAU_SUA, false);
                        String ma = data.getStringExtra(NhapActivity.EXTRA_MA_SV);

                        if (yeuCauSua && ma != null) {
                            SinhVien svCanSua = timSinhVienTheoMa(ma);
                            if (svCanSua != null) {
                                moManHinhSua(svCanSua);
                            }
                        }
                    }
                }
        );
    }

    private void setupListView() {
        adapter = new SinhVienAdapter(this, danhSachHienThi);
        lvSinhVien.setAdapter(adapter);

        // Chạm một hàng để xem chi tiết
        lvSinhVien.setOnItemClickListener((parent, view, position, id) -> {
            if (position >= 0 && position < danhSachHienThi.size()) {
                SinhVien sv = danhSachHienThi.get(position);
                Intent intent = new Intent(MainActivity.this, ChiTietActivity.class);
                intent.putExtra(ChiTietActivity.EXTRA_SINH_VIEN, sv);
                intent.putExtra(NhapActivity.EXTRA_MA_SV, sv.getMa());
                intent.putExtra(NhapActivity.EXTRA_HO_TEN, sv.getHoTen());
                intent.putExtra(NhapActivity.EXTRA_LOP, sv.getLop());
                intent.putExtra(NhapActivity.EXTRA_GIOI_TINH, sv.getGioiTinh());
                intent.putExtra(NhapActivity.EXTRA_DIEM, sv.getDiem());
                launcherChiTiet.launch(intent);
            }
        });
    }

    private void setupSearch() {
        edtTim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                locDanhSach(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void setupBackPressHandler() {
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (hasUnsavedChanges) {
                    hienThiDialogChuaLuu(MainActivity.this::finish);
                } else {
                    setEnabled(false);
                    getOnBackPressedDispatcher().onBackPressed();
                }
            }
        });
    }

    private void locDanhSach(String query) {
        danhSachHienThi.clear();
        String q = query != null ? query.trim().toLowerCase() : "";

        if (q.isEmpty()) {
            danhSachHienThi.addAll(danhSachGoc);
        } else {
            for (SinhVien sv : danhSachGoc) {
                if (sv.getMa().toLowerCase().contains(q) || sv.getHoTen().toLowerCase().contains(q)) {
                    danhSachHienThi.add(sv);
                }
            }
        }

        adapter.notifyDataSetChanged();
        capNhatSoLuong();
    }

    private void capNhatHienThi() {
        String query = edtTim.getText() != null ? edtTim.getText().toString() : "";
        locDanhSach(query);
    }

    private void khoiTaoDuLieuMau() {
        danhSachGoc.add(new SinhVien("SV001", "Nguyễn Văn An", "KTPM01", "Nam", 8.5));
        danhSachGoc.add(new SinhVien("SV002", "Trần Thị Bình", "KTPM01", "Nữ", 9.0));
        danhSachGoc.add(new SinhVien("SV003", "Lê Hoàng Cường", "KTPM02", "Nam", 7.5));
        danhSachGoc.add(new SinhVien("SV004", "Phạm Thị Dung", "KTPM02", "Nữ", 8.0));
        danhSachGoc.add(new SinhVien("SV005", "Hoàng Văn Em", "KTPM03", "Nam", 6.5));
        danhSachGoc.add(new SinhVien("SV006", "Vũ Thị Hoa", "KTPM03", "Nữ", 9.5));
        danhSachGoc.add(new SinhVien("SV007", "Đặng Minh Giang", "KTPM04", "Nam", 7.0));
        danhSachGoc.add(new SinhVien("SV008", "Bùi Thu Hà", "KTPM04", "Nữ", 8.2));
        danhSachGoc.add(new SinhVien("SV009", "Đỗ Văn Long", "KTPM01", "Nam", 7.8));
        danhSachGoc.add(new SinhVien("SV010", "Ngô Thị Mai", "KTPM02", "Nữ", 8.9));
        
        hasUnsavedChanges = true;
        try {
            SinhVienFileStore.luuDanhSach(this, danhSachGoc);
            hasUnsavedChanges = false;
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lưu dữ liệu mẫu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void capNhatSoLuong() {
        int soLuong = danhSachHienThi.size();
        txtSoLuong.setText(soLuong + " sinh viên");
    }

    private SinhVien timSinhVienTheoMa(String ma) {
        if (ma == null) return null;
        for (SinhVien sv : danhSachGoc) {
            if (sv.getMa().equalsIgnoreCase(ma)) {
                return sv;
            }
        }
        return null;
    }

    private void moManHinhSua(SinhVien sv) {
        Intent intent = new Intent(MainActivity.this, NhapActivity.class);
        intent.putExtra(NhapActivity.EXTRA_CHE_DO, NhapActivity.CHE_DO_SUA);
        intent.putExtra(NhapActivity.EXTRA_MA_SV, sv.getMa());
        intent.putExtra(NhapActivity.EXTRA_HO_TEN, sv.getHoTen());
        intent.putExtra(NhapActivity.EXTRA_LOP, sv.getLop());
        intent.putExtra(NhapActivity.EXTRA_GIOI_TINH, sv.getGioiTinh());
        intent.putExtra(NhapActivity.EXTRA_DIEM, sv.getDiem());
        launcherNhap.launch(intent);
    }

    // ==================== OPTIONS MENU ====================
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_them) {
            Intent intent = new Intent(MainActivity.this, NhapActivity.class);
            intent.putExtra(NhapActivity.EXTRA_CHE_DO, NhapActivity.CHE_DO_THEM);
            launcherNhap.launch(intent);
            return true;
        } else if (id == R.id.menu_luu) {
            thucHienLuuFile();
            return true;
        } else if (id == R.id.menu_doc) {
            if (hasUnsavedChanges) {
                hienThiDialogChuaLuu(this::thucHienDocFile);
            } else {
                thucHienDocFile();
            }
            return true;
        } else if (id == R.id.menu_gioi_thieu) {
            hienThiDialogGioiThieu();
            return true;
        } else if (id == R.id.menu_mo_website) {
            thucHienMoWebsite();
            return true;
        } else if (id == R.id.menu_xem_lich_su) {
            thucHienXemLichSu();
            return true;
        } else if (id == R.id.menu_xoa_lich_su) {
            thucHienXacNhanXoaLichSu();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // ==================== CONTEXT MENU ====================
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.menu_context, menu);
        menu.setHeaderTitle("Thao tác sinh viên");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        if (info == null || info.position < 0 || info.position >= danhSachHienThi.size()) {
            return super.onContextItemSelected(item);
        }

        SinhVien svChon = danhSachHienThi.get(info.position);
        int itemId = item.getItemId();

        if (itemId == R.id.menu_context_sua) {
            moManHinhSua(svChon);
            return true;
        } else if (itemId == R.id.menu_context_xoa) {
            hienThiDialogXacNhanXoa(svChon);
            return true;
        }

        return super.onContextItemSelected(item);
    }

    private void hienThiDialogXacNhanXoa(SinhVien sv) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc chắn muốn xóa sinh viên " + sv.getMa() + " - " + sv.getHoTen() + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    String maXoa = sv.getMa();
                    // Xóa theo mã ổn định trong danhSachGoc
                    boolean daXoa = false;
                    for (int i = 0; i < danhSachGoc.size(); i++) {
                        if (danhSachGoc.get(i).getMa().equalsIgnoreCase(maXoa)) {
                            danhSachGoc.remove(i);
                            daXoa = true;
                            break;
                        }
                    }

                    if (daXoa) {
                        hasUnsavedChanges = true;
                        try {
                            SinhVienFileStore.ghiLichSu(MainActivity.this, "XOA", maXoa);
                        } catch (IOException e) {
                            Toast.makeText(MainActivity.this, "Lỗi ghi lịch sử: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                        capNhatHienThi();
                        Toast.makeText(MainActivity.this, "Đã xóa sinh viên " + maXoa + " thành công!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    // ==================== CÁC CHỨC NĂNG FILE & DIALOG ====================

    private void thucHienLuuFile() {
        try {
            SinhVienFileStore.luuDanhSach(this, danhSachGoc);
            hasUnsavedChanges = false;
            Toast.makeText(this, "Đã lưu " + danhSachGoc.size() + " sinh viên vào " + SinhVienFileStore.FILE_JSON, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi khi lưu file: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void thucHienDocFile() {
        try {
            ArrayList<SinhVien> dsMoi = SinhVienFileStore.docDanhSach(this);
            danhSachGoc.clear();
            danhSachGoc.addAll(dsMoi);
            hasUnsavedChanges = false;
            capNhatHienThi();
            Toast.makeText(this, "Đọc file thành công (" + dsMoi.size() + " sinh viên)!", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            Toast.makeText(this, "File " + SinhVienFileStore.FILE_JSON + " chưa tồn tại!", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Lỗi đọc file: " + e.getMessage() + "\nDanh sách hiện tại được giữ nguyên.", Toast.LENGTH_LONG).show();
        }
    }

    private void hienThiDialogChuaLuu(Runnable onContinue) {
        new AlertDialog.Builder(this)
                .setTitle("Thay đổi chưa lưu")
                .setMessage("Bạn có thay đổi chưa được lưu vào file. Bạn muốn xử lý thế nào?")
                .setPositiveButton("Lưu", (dialog, which) -> {
                    try {
                        SinhVienFileStore.luuDanhSach(MainActivity.this, danhSachGoc);
                        hasUnsavedChanges = false;
                        Toast.makeText(MainActivity.this, "Đã lưu dữ liệu thành công!", Toast.LENGTH_SHORT).show();
                        if (onContinue != null) onContinue.run();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Lưu file thất bại: " + e.getMessage() + "\nGiữ nguyên màn hình hiện tại.", Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Bỏ thay đổi", (dialog, which) -> {
                    if (onContinue != null) onContinue.run();
                })
                .setNeutralButton("Hủy", null)
                .show();
    }

    private void hienThiDialogGioiThieu() {
        new AlertDialog.Builder(this)
                .setTitle("Giới thiệu ứng dụng")
                .setMessage("Ứng dụng: Quản lý sinh viên\n"
                        + "Phạm vi kiến thức: Bài 1 đến Bài 8 Android\n\n"
                        + "• Bài 1: Activity, Manifest, tài nguyên, vòng đời\n"
                        + "• Bài 2: LinearLayout, ConstraintLayout, ScrollView, Views\n"
                        + "• Bài 3: Event Listeners, Input validation, Toast\n"
                        + "• Bài 4: Custom Adapter, ListView cột bảng, Tìm kiếm\n"
                        + "• Bài 5: Options Menu, Context Menu, PopupMenu, Intent\n"
                        + "• Bài 6: Explicit & Implicit Intent, Result API, Share, Web\n"
                        + "• Bài 7: Lưu trữ Internal Storage, UTF-8, IO Streams\n"
                        + "• Bài 8: JSON ghi đè, TXT ghi nối, phục hồi & toàn vẹn")
                .setPositiveButton("Đóng", null)
                .show();
    }

    private void thucHienMoWebsite() {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://developer.android.com"));
        try {
            startActivity(browserIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "Không tìm thấy trình duyệt web trên thiết bị!", Toast.LENGTH_SHORT).show();
        }
    }

    private void thucHienXemLichSu() {
        try {
            String lichSu = SinhVienFileStore.docLichSu(this);
            if (lichSu.isEmpty()) {
                Toast.makeText(this, "Chưa có lịch sử thao tác.", Toast.LENGTH_SHORT).show();
                return;
            }

            ScrollView sv = new ScrollView(this);
            TextView tv = new TextView(this);
            tv.setText(lichSu);
            tv.setPadding(32, 24, 32, 24);
            tv.setTextSize(14f);
            tv.setTextColor(getResources().getColor(R.color.text_primary));
            sv.addView(tv);

            new AlertDialog.Builder(this)
                    .setTitle("Lịch sử thao tác")
                    .setView(sv)
                    .setPositiveButton("Đóng", null)
                    .show();

        } catch (IOException e) {
            Toast.makeText(this, "Lỗi đọc lịch sử: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void thucHienXacNhanXoaLichSu() {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa lịch sử")
                .setMessage("Bạn có chắc chắn muốn xóa toàn bộ lịch sử thao tác không?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    boolean xoaThanhCong = SinhVienFileStore.xoaLichSu(this);
                    if (xoaThanhCong) {
                        Toast.makeText(this, "Đã xóa file lịch sử thành công!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Chưa có file lịch sử hoặc không thể xóa!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Hủy", null)
                .show();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("DANH_SACH_GOC", danhSachGoc);
        outState.putBoolean("HAS_UNSAVED_CHANGES", hasUnsavedChanges);
    }
}
