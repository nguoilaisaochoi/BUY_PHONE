package fpoly.edu.duan1.Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GioHang {
    private int magh;

    private int masp;
    private int tongtien;

    private int soluong;
    private int mahang;

    private LocalDateTime ngay;

    private String makh;

    private String tinhtrang;

    private String diachi;

    private String tuchoi;

    public GioHang() {
    }

    public GioHang(int magh, int masp, int tongtien, int soluong, int mahang, LocalDateTime ngay, String makh) {
        this.magh = magh;
        this.masp = masp;
        this.tongtien = tongtien;
        this.soluong = soluong;
        this.mahang = mahang;
        this.ngay = ngay;
        this.makh = makh;
    }

    public GioHang(int magh, int masp, int tongtien, int soluong, int mahang) {
        this.magh = magh;
        this.masp = masp;
        this.tongtien = tongtien;
        this.soluong = soluong;
        this.mahang = mahang;
    }

    public String getTuchoi() {
        return tuchoi;
    }

    public void setTuchoi(String tuchoi) {
        this.tuchoi = tuchoi;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getTinhtrang() {
        return tinhtrang;
    }

    public void setTinhtrang(String tinhtrang) {
        this.tinhtrang = tinhtrang;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    public LocalDateTime getNgay() {
        return ngay;
    }

    public void setNgay(LocalDateTime ngay) {
        this.ngay = ngay;
    }

    public int getMagh() {
        return magh;
    }

    public void setMagh(int magh) {
        this.magh = magh;
    }


    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public int getTongtien() {
        return tongtien;
    }

    public void setTongtien(int tongtien) {
        this.tongtien = tongtien;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }

    public int getMahang() {
        return mahang;
    }

    public void setMahang(int mahang) {
        this.mahang = mahang;
    }
}
