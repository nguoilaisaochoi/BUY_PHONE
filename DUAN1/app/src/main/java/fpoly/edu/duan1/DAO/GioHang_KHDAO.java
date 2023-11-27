package fpoly.edu.duan1.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import fpoly.edu.duan1.Database.DBHelper;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.Model.SanPham;

public class GioHang_KHDAO {
    private SQLiteDatabase db;
    private String tableName;

    SharedPreferences nav_name;

    public GioHang_KHDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();
        //SharedPreferences pref = context.getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
        //String user = pref.getString("USERNAME", "");
        nav_name = context.getSharedPreferences("name_Nav", Context.MODE_PRIVATE);
        String hoten = nav_name.getString("makh", "");
        tableName = "Giohang_" + hoten;
    }

    public long insert(GioHang obj, Context context) {
        ContentValues values = new ContentValues();
        values.put("masp", obj.getMasp());
        values.put("mahang", obj.getMahang());
        values.put("soluong", obj.getSoluong());
        SanPhamDAO sanPhamDAO = new SanPhamDAO(context);
        SanPham sanPham = sanPhamDAO.getID(String.valueOf(obj.getMasp()));
        int gia = sanPham.getGia();
        // Tính tổng tiền và lưu vào cơ sở dữ liệu
        int tongtien = gia * obj.getSoluong();
        values.put("tongtien", tongtien);
        return db.insert(tableName, null, values);
    }

    public int update(GioHang obj, Context context) {
        ContentValues values = new ContentValues();
        values.put("masp", obj.getMasp());
        values.put("mahang", obj.getMahang());
        values.put("soluong", obj.getSoluong());
        SanPhamDAO sanPhamDAO = new SanPhamDAO(context);
        SanPham sanPham = sanPhamDAO.getID(String.valueOf(obj.getMasp()));
        int gia = sanPham.getGia();
        // Tính tổng tiền và lưu vào cơ sở dữ liệu
        int tongtien = gia * obj.getSoluong();
        values.put("tongtien", tongtien);
        return db.update(tableName, values, "magh=?", new String[]{String.valueOf(obj.getMagh())});
    }

    public int delete(String id) {
        return db.delete(tableName, "magh=?", new String[]{id});
    }

    @SuppressLint("Range")
    public List<GioHang> getData(String sql, String... selectionArgs) {
        List<GioHang> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while (c.moveToNext()) {
            GioHang obj = new GioHang();
            obj.setMagh(Integer.parseInt(c.getString(c.getColumnIndex("magh"))));
            obj.setMahang(Integer.parseInt((c.getString(c.getColumnIndex("mahang")))));
            obj.setMasp(Integer.parseInt(c.getString(c.getColumnIndex("masp"))));
            obj.setSoluong(Integer.parseInt(c.getString(c.getColumnIndex("soluong"))));
            obj.setTongtien(Integer.parseInt(c.getString(c.getColumnIndex("tongtien"))));
            list.add(obj);
        }
        c.close(); // Đóng Cursor khi không sử dụng nữa
        return list;
    }

    public List<GioHang> getAll() {
        String sql = "SELECT * FROM " + tableName;
        return getData(sql);
    }

    public GioHang getID(String id) {
        String sql = "SELECT * FROM " + tableName + " where magh=?";
        List<GioHang> list = getData(sql, id);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    // GioHangDAO.java

    // GioHangDAO.java

    public boolean isMaspExists(int masp) {
        String query = "SELECT * FROM " + tableName + " WHERE masp = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(masp)});

        boolean maspExists = cursor.getCount() > 0;

        cursor.close();


        return maspExists;
    }
    @SuppressLint("Range")
    public int gettMagh(int masp) {
        String query = "SELECT magh FROM " + tableName + " WHERE masp = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(masp)});

        int magh = -1; // Giả sử không tìm thấy

        if (cursor.moveToFirst()) {
            magh = cursor.getInt(cursor.getColumnIndex("magh"));
        }

        cursor.close();

        return magh;
    }

    @SuppressLint("Range")
    public int gettsoluong(int magh) {
        String query = "SELECT soluong FROM " + tableName + " WHERE magh = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(magh)});

        int soluong = -1; // Giả sử không tìm thấy

        if (cursor.moveToFirst()) {
            soluong = cursor.getInt(cursor.getColumnIndex("soluong"));
        }

        cursor.close();

        return soluong;
    }

    public int tangsl(int magh,Context context) {
        // Lấy số lượng hiện tại từ cơ sở dữ liệu
        int currentQuantity = gettsoluong(magh);

        // Kiểm tra nếu tìm thấy magh và có số lượng hiện tại hợp lệ
        if (currentQuantity != -1) {
            // Tăng số lượng
            int newQuantity = currentQuantity + 1;

            // Lấy giá từ cơ sở dữ liệu dựa trên masp
            GioHang gioHang = getID(String.valueOf(magh));
            SanPhamDAO sanPhamDAO = new SanPhamDAO(context);
            SanPham sanPham = sanPhamDAO.getID(String.valueOf(gioHang.getMasp()));
            int gia = sanPham.getGia();

            // Tính tổng tiền
            int tongtien = gia * newQuantity;

            // Cập nhật số lượng mới và tổng tiền vào cơ sở dữ liệu
            ContentValues values = new ContentValues();
            values.put("soluong", newQuantity);
            values.put("tongtien", tongtien);

            // Thực hiện cập nhật
            int rowsAffected = db.update(tableName, values, "magh=?", new String[]{String.valueOf(magh)});

            // Trả về số lượng mới sau khi tăng
            return (rowsAffected > 0) ? newQuantity : currentQuantity;
        }

        // Trường hợp không tìm thấy magh
        return -1;
    }

    public int giamsl(int magh,Context context) {
        // Lấy số lượng hiện tại từ cơ sở dữ liệu
        int currentQuantity = gettsoluong(magh);

        // Kiểm tra nếu tìm thấy magh và có số lượng hiện tại hợp lệ
        if (currentQuantity != -1 && currentQuantity > 1) {
            // Giảm số lượng
            int newQuantity = currentQuantity - 1;

            // Lấy giá từ cơ sở dữ liệu dựa trên masp
            GioHang gioHang = getID(String.valueOf(magh));
            SanPhamDAO sanPhamDAO = new SanPhamDAO(context);
            SanPham sanPham = sanPhamDAO.getID(String.valueOf(gioHang.getMasp()));
            int gia = sanPham.getGia();

            // Tính tổng tiền
            int tongtien = gia * newQuantity;

            // Cập nhật số lượng mới và tổng tiền vào cơ sở dữ liệu
            ContentValues values = new ContentValues();
            values.put("soluong", newQuantity);
            values.put("tongtien", tongtien);

            // Thực hiện cập nhật
            int rowsAffected = db.update(tableName, values, "magh=?", new String[]{String.valueOf(magh)});

            // Trả về số lượng mới sau khi giảm
            return (rowsAffected > 0) ? newQuantity : currentQuantity;
        }

        // Trường hợp không tìm thấy magh hoặc số lượng hiện tại đã là 0
        return -1;
    }
    public int getSoLuongMasp() {
        // Thực hiện truy vấn SQLite để lấy số lượng masp
        // Lưu ý: Bạn cần thay thế tên bảng và cột phù hợp với dữ liệu của bạn
        String query = "SELECT COUNT(DISTINCT masp) FROM "+tableName;
        Cursor cursor = db.rawQuery(query, null);
        int count = 0;
        if (cursor != null) {
            cursor.moveToFirst();
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

}
