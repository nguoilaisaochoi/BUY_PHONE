package fpoly.edu.duan1.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import fpoly.edu.duan1.Database.DBHelper;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.Model.SanPham;

@RequiresApi(api = Build.VERSION_CODES.O)
public class HoaDonDAO {
    private SQLiteDatabase db;



    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    public HoaDonDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        db = dbHelper.getWritableDatabase();
    }

    public long insert(GioHang obj, Context context) {
        ContentValues values = new ContentValues();
        values.put("masp", obj.getMasp());
        values.put("mahang", obj.getMahang());
        values.put("soluong", obj.getSoluong());
        values.put("ngay",formatter.format(obj.getNgay()));
        values.put("makh", obj.getMakh());
        values.put("tinhtrang", obj.getTinhtrang());
        SanPhamDAO sanPhamDAO = new SanPhamDAO(context);
        SanPham sanPham = sanPhamDAO.getID(String.valueOf(obj.getMasp()));
        int gia = sanPham.getGia();
        // Tính tổng tiền và lưu vào cơ sở dữ liệu
        int tongtien = gia * obj.getSoluong();
        values.put("tongtien", tongtien);
        return db.insert("Hoadon", null, values);
    }

    public int update(GioHang obj, Context context) {
        ContentValues values = new ContentValues();
        values.put("masp", obj.getMasp());
        values.put("mahang", obj.getMahang());
        values.put("soluong", obj.getSoluong());
        values.put("ngay",formatter.format(obj.getNgay()));
        values.put("makh", obj.getMakh());
        values.put("tinhtrang", obj.getMakh());
        SanPhamDAO sanPhamDAO = new SanPhamDAO(context);
        SanPham sanPham = sanPhamDAO.getID(String.valueOf(obj.getMasp()));
        int gia = sanPham.getGia();
        // Tính tổng tiền và lưu vào cơ sở dữ liệu
        int tongtien = gia * obj.getSoluong();
        values.put("tongtien", tongtien);
        return db.update("Hoadon", values, "magh=?", new String[]{String.valueOf(obj.getMagh())});
    }

    public int updateTinhTrang(String ngay, String newTinhTrang) {
        ContentValues values = new ContentValues();
        values.put("tinhtrang", newTinhTrang);
        return db.update("Hoadon", values, "ngay=?", new String[]{ngay});
    }

    public int delete(String id) {
        return db.delete("Hoadon", "magh=?", new String[]{id});
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
            obj.setMakh(c.getString(c.getColumnIndex("makh")));
            obj.setTongtien(Integer.parseInt(c.getString(c.getColumnIndex("tongtien"))));
            String dateString = c.getString(c.getColumnIndex("ngay"));
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
            LocalDateTime ngay = LocalDateTime.parse(dateString, formatter);
            obj.setTinhtrang(c.getString(c.getColumnIndex("tinhtrang")));
            obj.setNgay(ngay);

            list.add(obj);
        }
        c.close(); // Đóng Cursor khi không sử dụng nữa
        return list;
    }

    public List<GioHang> getAllByMaKH(String makh) {
        String sql = "SELECT * FROM Hoadon WHERE makh=?";
        return getData(sql, makh);
    }
    public List<GioHang> getAll() {
        String sql = "SELECT * FROM Hoadon";
        return getData(sql);
    }

    public GioHang getID(String id) {
        String sql = "SELECT * FROM Hoadon where magh=?";
        List<GioHang> list = getData(sql, id);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    // GioHangDAO.java

    public boolean isMaspExists(int masp) {
        String query = "SELECT * FROM Hoadon WHERE masp = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(masp)});

        boolean maspExists = cursor.getCount() > 0;

        cursor.close();


        return maspExists;
    }
    @SuppressLint("Range")
    public int gettMagh(int masp) {
        String query = "SELECT magh FROM Hoadon WHERE masp = ? ";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(masp)});

        int magh = -1; // Giả sử không tìm thấy

        if (cursor.moveToFirst()) {
            magh = cursor.getInt(cursor.getColumnIndex("magh"));
        }

        cursor.close();

        return magh;
    }



    public int sumTongTienTheoMaKHTheoNgay(String makh, String ngay) {
        int sumTongTien = 0;

        // Lấy danh sách các mục trong giỏ hàng của một khách hàng và theo ngày
        String sql = "SELECT * FROM Hoadon WHERE makh=? AND ngay=?";
        List<GioHang> gioHangList = getData(sql, makh, ngay);

        if (gioHangList != null && !gioHangList.isEmpty()) {
            // Tính tổng tiền bằng cách cộng tổng tiền của từng mục trong giỏ hàng
            for (GioHang gioHang : gioHangList) {
                sumTongTien += gioHang.getTongtien();
            }
        }

        return sumTongTien;
    }

    @SuppressLint("Range")
    public Integer getDoanhThu(String tuNgay, String denNgay) {
        String startTime = " 00:00:00";
        String endTime = " 23:59:59";

        // Append start time to tuNgay
        tuNgay = tuNgay + startTime;

        // Append end time to denNgay
        denNgay = denNgay + endTime;

        String sqlDoanhThu = "SELECT SUM(tongtien) AS doanhthu FROM Hoadon " +
                "WHERE ngay >= '" + tuNgay + "' AND ngay <= '" + denNgay + "';";
        List<Integer> list = new ArrayList<Integer>();
        Cursor c = db.rawQuery(sqlDoanhThu, null);

        while (c.moveToNext()) {
            try {
                list.add(c.getInt(c.getColumnIndex("doanhthu")));
            } catch (Exception e) {
                list.add(0);
            }
        }

        return list.isEmpty() ? 0 : list.get(0);
    }






}
