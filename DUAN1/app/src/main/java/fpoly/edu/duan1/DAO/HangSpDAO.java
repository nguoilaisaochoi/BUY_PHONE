package fpoly.edu.duan1.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import fpoly.edu.duan1.Database.DBHelper;
import fpoly.edu.duan1.Model.HangSp;
import fpoly.edu.duan1.Model.SanPham;

public class HangSpDAO {
    private SQLiteDatabase db;

    public HangSpDAO(Context context) {
        DBHelper dbHelper =new DBHelper(context);
        db=dbHelper.getWritableDatabase();
    }

    public long insert(HangSp obj) {
        ContentValues values = new ContentValues();
        values.put("tenloai", obj.getTenloai());
        values.put("anhloai", String.valueOf(obj.getAnh()));
        return db.insert("Hangsp", null, values);
    }

    public int update(HangSp obj) {
        ContentValues values = new ContentValues();
        values.put("tenloai", obj.getTenloai());
        values.put("anhloai", String.valueOf(obj.getAnh()));
        return db.update("Hangsp", values, "mahang=?", new String[]{String.valueOf(obj.getMahang())});
    }

    public int delete(String id) {
        return db.delete("Hangsp", "mahang=?", new String[]{id});
    }
    @SuppressLint("Range")
    public List<HangSp> getData(String sql, String... selectionArgs) {
        List<HangSp> list = new ArrayList<>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while (c.moveToNext()) {
            HangSp obj = new HangSp();
            obj.setMahang(c.getInt(c.getColumnIndex("mahang")));
            obj.setTenloai(c.getString(c.getColumnIndex("tenloai")));
            String anhString = c.getString(c.getColumnIndex("anhloai"));
            if (anhString != null) {
                try {
                    obj.setAnh(Uri.parse(anhString));
                } catch (IllegalArgumentException e) {
                    // Handle the exception, e.g., log a message or provide a default URI
                    Log.e("HangDAO", "Error parsing URI for 'anh'", e);
                    // You might want to set a default URI or handle the error in some way
                    // obj.setAnh(defaultUri);
                }
            } else {
                // Handle the case where "anh" column is null
                Log.e("HangDAO", "'anh' column is null");
                // You might want to set a default URI or handle the error in some way
                // obj.setAnh(defaultUri);
            }
            list.add(obj);
        }
        c.close(); // Đóng Cursor khi không sử dụng nữa
        return list;
    }

    public List<HangSp> getAll() {
        String sql = "SELECT * FROM Hangsp";
        return getData(sql);
    }

    public HangSp getID(String id) {
        String sql = "SELECT * FROM Hangsp where mahang=?";
        List<HangSp> list = getData(sql, id);
        if (!list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }

    public ArrayList<HangSp> searchSpByName(String keyword) {
        ArrayList<HangSp> hangsanpham = new ArrayList<>();
        String query = "SELECT * FROM Hangsp WHERE tenloai LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyword + "%"};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int mahangindex = cursor.getColumnIndex("mahang");
                int mahang = (mahangindex != -1) ? cursor.getInt(mahangindex) : -1;

                int tenloaiIndex = cursor.getColumnIndex("tenloai");
                String tenLoai = (tenloaiIndex != -1) ? cursor.getString(tenloaiIndex) : "";

                Uri anhsp = null;
                int anh = cursor.getColumnIndex("anhloai");
                String anhString = (anh != -1) ? cursor.getString(anh) : null;
                if (anhString != null && !anhString.isEmpty()) {
                    anhsp = Uri.parse(anhString);
                    // Continue processing Uri as needed
                } else {
                    anhsp = Uri.parse("/a");
                    Log.d("HangSpDAO", "anhString is null or empty. No further action is taken.");
                }

                // Only add to the list if mahang is valid
                if (mahang != -1) {
                    HangSp hangSp = new HangSp(mahang, tenLoai, anhsp);
                    hangsanpham.add(hangSp);
                }
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor when done
        }

        return hangsanpham;
    }

}
