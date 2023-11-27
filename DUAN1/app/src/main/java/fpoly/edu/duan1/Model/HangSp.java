package fpoly.edu.duan1.Model;

import android.net.Uri;

public class HangSp {
    private int mahang;
    private String tenloai;

    private Uri anh;

    public HangSp() {
    }

    public HangSp(int mahang, String tenloai, Uri anh) {
        this.mahang = mahang;
        this.tenloai = tenloai;
        this.anh = anh;
    }

    public Uri getAnh() {
        return anh;
    }

    public void setAnh(Uri anh) {
        this.anh = anh;
    }

    public int getMahang() {
        return mahang;
    }

    public void setMahang(int mahang) {
        this.mahang = mahang;
    }

    public String getTenloai() {
        return tenloai;
    }

    public void setTenloai(String tenloai) {
        this.tenloai = tenloai;
    }
}
