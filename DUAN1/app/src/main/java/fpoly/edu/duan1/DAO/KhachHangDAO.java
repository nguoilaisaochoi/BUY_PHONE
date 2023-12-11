package fpoly.edu.duan1.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fpoly.edu.duan1.Database.DBHelper;

import fpoly.edu.duan1.Model.KhachHang;
public class KhachHangDAO {
    private SQLiteDatabase db;

    SharedPreferences nav_name;
    Context c;
    public KhachHangDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        db = dbHelper.getReadableDatabase();
        c=context;
    }

    public long insert(KhachHang obj){
        ContentValues values=new ContentValues();
        values.put("makh",obj.getMakh());
        values.put("hoten",obj.getHoTen());
        values.put("matKhau",obj.getMatKhau());
        values.put("sdt",obj.getSdt());
        values.put("diachi",obj.getDiachi());
        values.put("avt", String.valueOf(obj.getAvt()));
        return db.insert("Khachhang",null,values);
    };

    public int updatepass(KhachHang obj){
        ContentValues values=new ContentValues();
        values.put("hoten",obj.getHoTen());
        values.put("matKhau",obj.getMatKhau());
        return db.update("Khachhang",values,"makh=?",new String[]{obj.getMakh()});
    }
    public int updateInfo(KhachHang obj){
        ContentValues values=new ContentValues();
        values.put("hoten", obj.getHoTen());
        values.put("sdt", obj.getSdt());
        values.put("diachi", obj.getDiachi());
        values.put("avt", String.valueOf(obj.getAvt()));
        return db.update("Khachhang", values, "makh=?", new String[]{obj.getMakh()});
    }

    public int delete(String id){
        return db.delete("Khachhang","makh",new String[]{id});
    }
    @SuppressLint("Range")
    public List<KhachHang> getData(String sql, String...selectionArgs){
        List<KhachHang> list=new ArrayList<KhachHang>();
        Cursor c=db.rawQuery(sql,selectionArgs);
        while (c.moveToNext()){
            KhachHang obj=new KhachHang();
            obj.setMakh(c.getString(c.getColumnIndex("makh")));
            obj.setHoTen(c.getString(c.getColumnIndex("hoten")));
            obj.setMatKhau(c.getString(c.getColumnIndex("matKhau")));
            String sdtString = c.getString(c.getColumnIndex("sdt"));
            obj.setDiachi(c.getString(c.getColumnIndex("diachi")));
            if (sdtString != null && !sdtString.isEmpty()) {
                obj.setSdt("0"+sdtString);
            } else {
                // Xử lý khi "sdt" rỗng hoặc null
                obj.setSdt(""); // Hoặc giá trị mặc định khác bạn mong muốn
            }
            String daichiString = c.getString(c.getColumnIndex("diachi"));
            if (daichiString != null && !daichiString.isEmpty()) {
                obj.setDiachi(daichiString);
            } else {
                // Xử lý khi "daichi" rỗng hoặc null
                obj.setDiachi(""); // Hoặc giá trị mặc định khác bạn mong muốn
            }
            String anhString = c.getString(c.getColumnIndex("avt"));
            if (anhString != null) {
                try {
                    obj.setAvt(Uri.parse(anhString));
                } catch (IllegalArgumentException e) {
                    // Handle the exception, e.g., log a message or provide a default URI
                    Log.e("SanPhamDAO", "Error parsing URI for 'anh'", e);
                    // You might want to set a default URI or handle the error in some way
                    // obj.setAnh(defaultUri);
                }
            } else {
                // Handle the case where "anh" column is null
                Log.e("SanPhamDAO", "'anh' column is null");
                // You might want to set a default URI or handle the error in some way
                // obj.setAnh(defaultUri);
            }
            list.add(obj);
        }
        return list;
    }
    public List<KhachHang> getAll(){
        String sql="SELECT * FROM Khachhang";
        return  getData(sql);
    }
    public KhachHang getID(String id){
        String sql="select * from Khachhang where makh=?";
        List<KhachHang> list=getData(sql,id);
        return list.get(0);
    }

    public int checkLogin(String id, String password) {
        String sql = "SELECT * FROM Khachhang WHERE makh=? AND matKhau=?";
        List<KhachHang> list = getData(sql, id, password);
        if (list.size() == 0)
            return -1;
        nav_name = c.getSharedPreferences("name_Nav", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = nav_name.edit();
        editor.putString("makh", id);
        editor.commit();
        return 1;
    }

    public String getHotenByMatt(String makh) {
        String hoten = null;

        String query = "SELECT hoten FROM Khachhang WHERE makh = ?";
        Cursor cursor = db.rawQuery(query, new String[]{makh});

        if (cursor != null && cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex("hoten");
            if (columnIndex >= 0) {
                hoten = cursor.getString(columnIndex);
            }
            cursor.close();
        }

        return hoten;
    }


    @SuppressLint("Range")
    public String getMaKhachHangByTenDangNhap(String hoten) {
        String maKhachHang = null;

        Cursor cursor = null;

        try {
            String query = "SELECT makh FROM Khachhang WHERE hoten = ?";
            cursor = db.rawQuery(query, new String[]{hoten});

            if (cursor.moveToFirst()) {
                maKhachHang = cursor.getString(cursor.getColumnIndex("makh"));
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return maKhachHang;
    }

    }


