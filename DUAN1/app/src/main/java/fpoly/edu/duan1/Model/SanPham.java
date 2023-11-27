package fpoly.edu.duan1.Model;


import android.net.Uri;

public class SanPham {
    private int masp;
    private String tensp;
    private int gia;
    private int ram;


    private  int dungluong;
    private float manhinh;

    private int mahang;

    private Uri anh;

    public SanPham() {
    }

    public SanPham(int masp, String tensp, int gia, int ram, int dungluong, float manhinh, int mahang, Uri anh) {
        this.masp = masp;
        this.tensp = tensp;
        this.gia = gia;
        this.ram = ram;
        this.dungluong = dungluong;
        this.manhinh = manhinh;
        this.mahang = mahang;
        this.anh = anh;
    }

    public void setManhinh(float manhinh) {
        this.manhinh = manhinh;
    }

    public int getMasp() {
        return masp;
    }

    public void setMasp(int masp) {
        this.masp = masp;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getGia() {
        return gia;
    }

    public void setGia(int gia) {
        this.gia = gia;
    }

    public int getRam() {
        return ram;
    }

    public void setRam(int ram) {
        this.ram = ram;
    }

    public int getDungluong() {
        return dungluong;
    }

    public void setDungluong(int dungluong) {
        this.dungluong = dungluong;
    }

    public float getManhinh() {
        return manhinh;
    }

    public void setManhinh(int manhinh) {
        this.manhinh = manhinh;
    }

    public int getMahang() {
        return mahang;
    }

    public void setMahang(int mahang) {
        this.mahang = mahang;
    }

    public Uri getAnh() {
        return anh;
    }

    public void setAnh(Uri anh) {
        this.anh = anh;
    }
}
