
package com.example.ontap1;

import android.content.Context;
import java.util.List;
import java.util.ArrayList;

public class ThaoTacSinhVien {

    private ThaoTacFile thaoTacFile;
    private final String FILE_NAME = "SinhVien.txt";

    // Constructor
    public ThaoTacSinhVien(Context context) {
        thaoTacFile = new ThaoTacFile(context);
    }

    // 1. LẤY TẤT CẢ SINH VIÊN
    public List<SinhVien> layTatCaSinhVien() {
        return thaoTacFile.readFile(FILE_NAME);
    }

    // 2. THÊM SINH VIÊN
    public boolean themSinhVien(SinhVien sv) {

        if (sv == null || sv.getMaSV() == null ||
                sv.getMaSV().trim().isEmpty()) {
            return false;
        }

        List<SinhVien> ds = layTatCaSinhVien();

        // Kiểm tra trùng mã sinh viên
        for (SinhVien item : ds) {
            if (item.getMaSV().trim().equalsIgnoreCase(
                    sv.getMaSV().trim())) {
                return false;
            }
        }

        ds.add(sv);

        return thaoTacFile.writeFile(FILE_NAME, ds);
    }

    // 3. SỬA SINH VIÊN
    public boolean suaSinhVien(SinhVien sv) {

        if (sv == null || sv.getMaSV() == null ||
                sv.getMaSV().trim().isEmpty()) {
            return false;
        }

        List<SinhVien> ds = layTatCaSinhVien();

        for (int i = 0; i < ds.size(); i++) {

            if (ds.get(i).getMaSV().trim().equalsIgnoreCase(
                    sv.getMaSV().trim())) {

                ds.set(i, sv);

                return thaoTacFile.writeFile(FILE_NAME, ds);
            }
        }

        return false;
    }

    // 4. XÓA SINH VIÊN
    public boolean xoaSinhVien(String maSV) {

        if (maSV == null || maSV.trim().isEmpty()) {
            return false;
        }

        List<SinhVien> ds = layTatCaSinhVien();

        for (int i = 0; i < ds.size(); i++) {

            if (ds.get(i).getMaSV().trim().equalsIgnoreCase(
                    maSV.trim())) {

                ds.remove(i);

                return thaoTacFile.writeFile(FILE_NAME, ds);
            }
        }

        return false;
    }
}
