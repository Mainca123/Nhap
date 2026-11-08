package com.example.ontap1;

public class SinhVien {

    private String maSV;
    private String hoTen;
    private String email;
    private String lop;
    private String gioiTinh;
    private String soThich;
    private String trangThai;

    // Constructor
    public SinhVien(String maSV, String hoTen, String email,
                    String lop, String gioiTinh,
                    String soThich, String trangThai) {
        this.maSV = maSV;
        this.hoTen = hoTen;
        this.email = email;
        this.lop = lop;
        this.gioiTinh = gioiTinh;
        this.soThich = soThich;
        this.trangThai = trangThai;
    }

    public String toTXT(){
        return maSV + ',' + hoTen + ',' + email + ',' + lop + ',' + gioiTinh + ',' + soThich + ',' + trangThai;
    }

    // Getter và Setter
    public String getMaSV() {
        return maSV;
    }

    public void setMaSV(String maSV) {
        this.maSV = maSV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getSoThich() {
        return soThich;
    }

    public void setSoThich(String soThich) {
        this.soThich = soThich;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }
}