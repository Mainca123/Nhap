package com.example.baitap;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class SinhVienFileStore {

    public static final String FILE_JSON = "sinhvien.json";
    public static final String FILE_LICHSU = "lichsu.txt";

    /**
     * Kiểm tra xem file sinhvien.json có tồn tại trong internal storage không.
     */
    public static boolean hasJsonFile(Context context) {
        File file = new File(context.getFilesDir(), FILE_JSON);
        return file.exists() && file.length() > 0;
    }

    /**
     * Đọc danh sách sinh viên từ file sinhvien.json (MODE_PRIVATE, UTF-8).
     * Kiểm tra cấu trúc, trường bắt buộc, miền điểm và mã trùng.
     * Trả về danh sách sinh viên hợp lệ.
     */
    public static ArrayList<SinhVien> docDanhSach(Context context) throws IOException, JSONException, IllegalArgumentException {
        File file = new File(context.getFilesDir(), FILE_JSON);
        if (!file.exists()) {
            throw new FileNotFoundException("File " + FILE_JSON + " chưa tồn tại.");
        }

        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(FILE_JSON);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }

        String jsonStr = sb.toString().trim();
        if (jsonStr.isEmpty()) {
            return new ArrayList<>();
        }

        JSONArray jsonArray = new JSONArray(jsonStr);
        ArrayList<SinhVien> danhSach = new ArrayList<>();
        Set<String> maSet = new HashSet<>();

        for (int i = 0; i < jsonArray.length(); i++) {
            SinhVien sv = SinhVien.fromJsonObject(jsonArray.getJSONObject(i));
            if (maSet.contains(sv.getMa())) {
                throw new IllegalArgumentException("Dữ liệu trong file có mã trùng: " + sv.getMa());
            }
            maSet.add(sv.getMa());
            danhSach.add(sv);
        }

        return danhSach;
    }

    /**
     * Lưu toàn bộ danh sách sinh viên thành mảng JSON hoàn chỉnh (MODE_PRIVATE, UTF-8).
     */
    public static void luuDanhSach(Context context, List<SinhVien> danhSach) throws IOException, JSONException {
        JSONArray jsonArray = new JSONArray();
        for (SinhVien sv : danhSach) {
            jsonArray.put(sv.toJsonObject());
        }

        String jsonString = jsonArray.toString(2); // Indent 2 cho đẹp và chuẩn UTF-8

        try (FileOutputStream fos = context.openFileOutput(FILE_JSON, Context.MODE_PRIVATE);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write(jsonString);
            osw.flush();
        }
    }

    /**
     * Ghi nối một dòng vào file lichsu.txt (MODE_APPEND, UTF-8).
     * Định dạng: "dd/MM/yyyy HH:mm | HANH_DONG | MA_SV"
     */
    public static void ghiLichSu(Context context, String hanhDong, String maSV) throws IOException {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
        String timestamp = sdf.format(new Date());
        String line = timestamp + " | " + hanhDong + " | " + maSV + "\n";

        try (FileOutputStream fos = context.openFileOutput(FILE_LICHSU, Context.MODE_APPEND);
             OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8)) {
            osw.write(line);
            osw.flush();
        }
    }

    /**
     * Đọc toàn bộ nội dung file lichsu.txt.
     */
    public static String docLichSu(Context context) throws IOException {
        File file = new File(context.getFilesDir(), FILE_LICHSU);
        if (!file.exists() || file.length() == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        try (FileInputStream fis = context.openFileInput(FILE_LICHSU);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader reader = new BufferedReader(isr)) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
        }
        return sb.toString().trim();
    }

    /**
     * Xóa file lichsu.txt.
     */
    public static boolean xoaLichSu(Context context) {
        return context.deleteFile(FILE_LICHSU);
    }
}
