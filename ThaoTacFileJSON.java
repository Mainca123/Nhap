package com.example.ontap1;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Đọc, ghi danh sách sinh viên dưới dạng mảng JSON trong bộ nhớ nội bộ. */
public class ThaoTacFileJSON {

    private final Context context;

    public ThaoTacFileJSON(Context context) {
        this.context = context;
        taoDuLieuMau("SinhVien.json");
    }

    // 1. TẠO DỮ LIỆU MẪU: chỉ tạo khi file chưa tồn tại.
    private void taoDuLieuMau(String fileName) {
        if (context.getFileStreamPath(fileName).exists()) {
            return;
        }

        List<SinhVien> dsSinhVien = new ArrayList<>();
        dsSinhVien.add(new SinhVien("SV001", "Nguyen Van A", "a@gmail.com",
                "CNTT-1", "Nam", "Doc sach", "Đang học"));
        dsSinhVien.add(new SinhVien("SV002", "Tran Thi B", "b@gmail.com",
                "CNTT-2", "Nữ", "Du lich", "Đang học"));
        dsSinhVien.add(new SinhVien("SV003", "Le Van C", "c@gmail.com",
                "CNTT-1", "Nam", "The thao", "Đang học"));
        dsSinhVien.add(new SinhVien("SV004", "Pham Thi D", "d@gmail.com",
                "CNTT-3", "Nữ", "Doc sach", "Tạm nghỉ học"));
        dsSinhVien.add(new SinhVien("SV005", "Hoang Van E", "e@gmail.com",
                "CNTT-2", "Nam", "Du lich", "Thôi học"));
        writeFile(fileName, dsSinhVien);
    }

    // 2. GHI FILE: ghi đè toàn bộ danh sách, danh sách rỗng được lưu thành [].
    public Boolean writeFile(String fileName, List<SinhVien> dsSinhVien) {
        try {
            JSONArray array = new JSONArray();
            for (SinhVien sv : dsSinhVien) {
                JSONObject object = new JSONObject();
                object.put("maSV", giaTriJSON(sv.getMaSV()));
                object.put("hoTen", giaTriJSON(sv.getHoTen()));
                object.put("email", giaTriJSON(sv.getEmail()));
                object.put("lop", giaTriJSON(sv.getLop()));
                object.put("gioiTinh", giaTriJSON(sv.getGioiTinh()));
                object.put("soThich", giaTriJSON(sv.getSoThich()));
                object.put("trangThai", giaTriJSON(sv.getTrangThai()));
                array.put(object);
            }

            // Chuyển thành JSON trước khi mở file để tránh xóa dữ liệu khi chuyển đổi lỗi.
            String json = array.toString(4);
            try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    context.openFileOutput(fileName, Context.MODE_PRIVATE),
                    StandardCharsets.UTF_8))) {
                writer.write(json);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. ĐỌC FILE: trả về danh sách rỗng nếu file chưa tồn tại hoặc không hợp lệ.
    public List<SinhVien> readFile(String fileName) {
        List<SinhVien> dsSinhVien = new ArrayList<>();
        if (!context.getFileStreamPath(fileName).exists()) {
            return dsSinhVien;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                context.openFileInput(fileName), StandardCharsets.UTF_8))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                json.append(line).append('\n');
            }

            JSONArray array = new JSONArray(json.toString());
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.getJSONObject(i);
                dsSinhVien.add(new SinhVien(
                        docChuoi(object, "maSV"),
                        docChuoi(object, "hoTen"),
                        docChuoi(object, "email"),
                        docChuoi(object, "lop"),
                        docChuoi(object, "gioiTinh"),
                        docChuoi(object, "soThich"),
                        docChuoi(object, "trangThai")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
            dsSinhVien.clear();
        }
        return dsSinhVien;
    }

    // 4. XÓA FILE.
    public Boolean deleteFile(String fileName) {
        return context.deleteFile(fileName);
    }

    private Object giaTriJSON(String value) {
        return value == null ? JSONObject.NULL : value;
    }

    private String docChuoi(JSONObject object, String key) throws JSONException {
        Object value = object.get(key);
        if (value == JSONObject.NULL) {
            return null;
        }
        if (!(value instanceof String)) {
            throw new JSONException("Trường " + key + " phải là chuỗi");
        }
        return (String) value;
    }
}
