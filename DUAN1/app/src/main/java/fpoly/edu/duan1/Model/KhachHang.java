package fpoly.edu.duan1.Model;

import android.net.Uri;

public class KhachHang {
    private String makh;
    private String hoTen;
    private Integer sdt;
    private String matKhau;

    private String diachi;

    private Uri avt;

    public KhachHang() {
    }

    public KhachHang(String makh, String hoTen, Integer sdt, String matKhau) {
        this.makh = makh;
        this.hoTen = hoTen;
        this.sdt = sdt;
        this.matKhau = matKhau;
    }

    public Uri getAvt() {
        return avt;
    }

    public void setAvt(Uri avt) {
        this.avt = avt;
    }

    public String getDiachi() {
        return diachi;
    }

    public void setDiachi(String diachi) {
        this.diachi = diachi;
    }

    public String getMakh() {
        return makh;
    }

    public void setMakh(String makh) {
        this.makh = makh;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public Integer getSdt() {
        return sdt;
    }

    public void setSdt(Integer sdt) {
        this.sdt = sdt;
    }

    public String getMatKhau() {
        return matKhau;
    }

    public void setMatKhau(String matKhau) {
        this.matKhau = matKhau;
    }
}
