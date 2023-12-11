package fpoly.edu.duan1.Database;

public class Data_SQLite {
    public static final String INSERT_KHACH_HANG = "insert into Khachhang(makh,hoten,matkhau) values " +
            "('admin','ADMIN','admin')";

    public static final String INSERT_SANPHAM = "INSERT INTO Sanpham(tensp, gia, manhinh, ram, dungluong, mahang) VALUES" +
            "    ('iPhone 12', '20000000', '6.1', '4', '128', '1')," +
            "    ('iPhone 12 Pro', '30000000', '6.1', '6', '256', '1')," +
            "    ('iPhone 12 Mini', '18000000', '5.4', '4', '64', '1')," +
            "    ('iPhone 13', '25000000', '6.2', '6', '128', '1')," +
            "    ('Samsung Galaxy S21', '22000000', '6.2', '8', '128', '2')," +
            "    ('Samsung Galaxy S20', '18000000', '6.2', '8', '128', '2')," +
            "    ('Xiaomi Mi 11', '18000000', '6.81', '8', '256', '3')," +
            "    ('Xiaomi Redmi Note 10', '5000000', '6.43', '4', '64', '3')," +
            "    ('Xiaomi Poco X3', '7000000', '6.67', '6', '128', '3')," +
            "    ('Xiaomi Mi 10T Pro', '16000000', '6.67', '8', '256', '3')," +
            "    ('Xiaomi Redmi 9A', '3000000', '6.53', '2', '32', '3');";
    public static final String INSERT_HANG_SP = "insert into Hangsp(tenloai) values" +
            "('Apple')," +
            "('Samsung')," +
            "('Xiaomi')";
}
