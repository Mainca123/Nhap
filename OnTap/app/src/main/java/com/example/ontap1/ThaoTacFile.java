
package com.example.ontap1;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ThaoTacFile {

    private final Context context;

    public ThaoTacFile(Context context) {
        this.context = context;
        taoDuLieuMau("SinhVien.txt");
    }

    // 1. TẠO DỮ LIỆU MẪU
    private void taoDuLieuMau(String fileName) {

        // Kiểm tra file đã tồn tại chưa
        if (context.getFileStreamPath(fileName).exists()) {
            return;
        }

        ArrayList<SinhVien> dsSinhVien = new ArrayList<>();

        dsSinhVien.add(new SinhVien(
                "SV001", "Nguyen Van A", "a@gmail.com",
                "CNTT-1", "Nam", "Doc sach", "Đang học"
        ));

        dsSinhVien.add(new SinhVien(
                "SV002", "Tran Thi B", "b@gmail.com",
                "CNTT-2", "Nữ", "Du lich", "Đang học"
        ));

        dsSinhVien.add(new SinhVien(
                "SV003", "Le Van C", "c@gmail.com",
                "CNTT-1", "Nam", "The thao", "Đang học"
        ));

        dsSinhVien.add(new SinhVien(
                "SV004", "Pham Thi D", "d@gmail.com",
                "CNTT-3", "Nữ", "Doc sach", "Tạm nghỉ học"
        ));

        dsSinhVien.add(new SinhVien(
                "SV005", "Hoang Van E", "e@gmail.com",
                "CNTT-2", "Nam", "Du lich", "Thôi học"
        ));

        writeFile(fileName, dsSinhVien);
    }

    // 2. GHI FILE
    public Boolean writeFile(String fileName, List<SinhVien> dsSinhVien) {

        try (BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(
                        context.openFileOutput(
                                fileName, Context.MODE_PRIVATE),
                        StandardCharsets.UTF_8))) {

            for (SinhVien sv : dsSinhVien) {
                writer.write(sv.toTXT());
                writer.newLine();
            }

            return true;

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 3. ĐỌC FILE
    public List<SinhVien> readFile(String fileName) {

        List<SinhVien> dsSinhVien = new ArrayList<>();

        // Kiểm tra file tồn tại
        if (!context.getFileStreamPath(fileName).exists()) {
            return dsSinhVien;
        }

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        context.openFileInput(fileName),
                        StandardCharsets.UTF_8))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",", -1);

                if (data.length == 7) {

                    SinhVien sv = new SinhVien(
                            data[0].trim(),
                            data[1].trim(),
                            data[2].trim(),
                            data[3].trim(),
                            data[4].trim(),
                            data[5].trim(),
                            data[6].trim()
                    );

                    dsSinhVien.add(sv);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return dsSinhVien;
    }

    // 4. XÓA FILE
    public Boolean deleteFile(String tenFile) {
        return context.deleteFile(tenFile);
    }
}
