package com.example.baitap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Objects;

public class SinhVien implements Serializable {
    private String ma;
    private String hoTen;
    private String lop;
    private String gioiTinh;
    private double diem;

    public SinhVien() {
    }

    public SinhVien(String ma, String hoTen, String lop, String gioiTinh, double diem) {
        this.ma = ma;
        this.hoTen = hoTen;
        this.lop = lop;
        this.gioiTinh = gioiTinh;
        this.diem = diem;
    }

    public String getMa() {
        return ma;
    }

    public void setMa(String ma) {
        this.ma = ma;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getLop() {
        return lop;
    }

    public void setLop(String lop) {
        this.lop = lop;
    }

    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }

    public double getDiem() {
        return diem;
    }

    public void setDiem(double diem) {
        this.diem = diem;
    }

    public JSONObject toJsonObject() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("ma", ma);
        json.put("hoTen", hoTen);
        json.put("lop", lop);
        json.put("gioiTinh", gioiTinh);
        json.put("diem", diem);
        return json;
    }

    public static SinhVien fromJsonObject(JSONObject json) throws JSONException, IllegalArgumentException {
        if (!json.has("ma") || !json.has("hoTen") || !json.has("lop") || !json.has("gioiTinh") || !json.has("diem")) {
            throw new IllegalArgumentException("Thiếu trường dữ liệu bắt buộc trong JSON");
        }

        String ma = json.getString("ma").trim().toUpperCase();
        String hoTen = json.getString("hoTen").trim();
        String lop = json.getString("lop").trim();
        String gioiTinh = json.getString("gioiTinh").trim();
        double diem = json.getDouble("diem");

        if (ma.isEmpty()) {
            throw new IllegalArgumentException("Mã sinh viên không được rỗng");
        }
        if (hoTen.isEmpty()) {
            throw new IllegalArgumentException("Họ tên không được rỗng");
        }
        if (Double.isNaN(diem) || Double.isInfinite(diem) || diem < 0.0 || diem > 10.0) {
            throw new IllegalArgumentException("Điểm không hợp lệ: " + diem);
        }

        return new SinhVien(ma, hoTen, lop, gioiTinh, diem);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SinhVien sinhVien = (SinhVien) o;
        return Objects.equals(ma, sinhVien.ma);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ma);
    }

    @Override
    public String toString() {
        return ma + " - " + hoTen + " - " + lop + " - " + gioiTinh + " - " + diem;
    }
}
