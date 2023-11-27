package fpoly.edu.duan1.Model;

public class Top {
    private  String tensp;
    private  int soluong;

    public Top() {
    }

    public Top(String tensp, int soluong) {
        this.tensp = tensp;
        this.soluong = soluong;
    }

    public String getTensp() {
        return tensp;
    }

    public void setTensp(String tensp) {
        this.tensp = tensp;
    }

    public int getSoluong() {
        return soluong;
    }

    public void setSoluong(int soluong) {
        this.soluong = soluong;
    }
}
