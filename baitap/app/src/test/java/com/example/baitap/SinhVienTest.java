package com.example.baitap;

import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class SinhVienTest {

    @Test
    public void testTaoSinhVienHopLe() {
        SinhVien sv = new SinhVien("SV01", "Giàng Seo Chính", "CNTT01", "Nam", 8.5);
        assertEquals("SV01", sv.getMa());
        assertEquals("Giàng Seo Chính", sv.getHoTen());
        assertEquals("CNTT01", sv.getLop());
        assertEquals("Nam", sv.getGioiTinh());
        assertEquals(8.5, sv.getDiem(), 0.001);
    }

    @Test
    public void testCapNhatThongTin() {
        SinhVien sv = new SinhVien("SV02", "Nguyễn An", "CNTT02", "Nữ", 7.0);
        sv.setDiem(9.0);
        assertEquals(9.0, sv.getDiem(), 0.001);
        sv.setLop("CNTT03");
        assertEquals("CNTT03", sv.getLop());
    }

    @Test
    public void testLocDanhSachTheoMaVaTen() {
        ArrayList<SinhVien> list = new ArrayList<>();
        list.add(new SinhVien("SV01", "Giàng Seo Chính", "CNTT01", "Nam", 8.5));
        list.add(new SinhVien("SV02", "Nguyễn An", "CNTT02", "Nữ", 7.0));
        list.add(new SinhVien("SV03", "Trần Bình", "CNTT01", "Nam", 9.0));

        // Lọc theo mã "sv02" (không phân biệt hoa thường)
        String queryMa = "sv02".trim().toLowerCase();
        ArrayList<SinhVien> kqMa = new ArrayList<>();
        for (SinhVien sv : list) {
            if (sv.getMa().toLowerCase().contains(queryMa) || sv.getHoTen().toLowerCase().contains(queryMa)) {
                kqMa.add(sv);
            }
        }
        assertEquals(1, kqMa.size());
        assertEquals("SV02", kqMa.get(0).getMa());

        // Lọc theo tên "An"
        String queryTen = "An".trim().toLowerCase();
        ArrayList<SinhVien> kqTen = new ArrayList<>();
        for (SinhVien sv : list) {
            if (sv.getMa().toLowerCase().contains(queryTen) || sv.getHoTen().toLowerCase().contains(queryTen)) {
                kqTen.add(sv);
            }
        }
        assertEquals(1, kqTen.size());
        assertEquals("Nguyễn An", kqTen.get(0).getHoTen());
    }

    @Test
    public void testXoaTheoMaOnDinhKhiDangLoc() {
        ArrayList<SinhVien> listGoc = new ArrayList<>();
        SinhVien sv1 = new SinhVien("SV01", "Giàng Seo Chính", "CNTT01", "Nam", 8.5);
        SinhVien sv2 = new SinhVien("SV02", "Nguyễn An", "CNTT02", "Nữ", 7.0);
        SinhVien sv3 = new SinhVien("SV03", "Trần Bình", "CNTT01", "Nam", 9.0);
        listGoc.add(sv1);
        listGoc.add(sv2);
        listGoc.add(sv3);

        // Đang lọc SV02
        String maXoa = "SV02";
        for (int i = 0; i < listGoc.size(); i++) {
            if (listGoc.get(i).getMa().equalsIgnoreCase(maXoa)) {
                listGoc.remove(i);
                break;
            }
        }

        assertEquals(2, listGoc.size());
        assertFalse(listGoc.contains(sv2));
        assertTrue(listGoc.contains(sv1));
        assertTrue(listGoc.contains(sv3));
    }
}
