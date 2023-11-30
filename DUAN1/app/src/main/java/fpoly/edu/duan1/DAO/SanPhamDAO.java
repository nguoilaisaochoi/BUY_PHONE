package fpoly.edu.duan1.DAO;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import fpoly.edu.duan1.Database.DBHelper;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.Model.Top;

public class SanPhamDAO {

    private SQLiteDatabase db;

    private Context context;
    public SanPhamDAO(Context context) {
        DBHelper dbHelper =new DBHelper(context);
        db=dbHelper.getWritableDatabase();
        this.context = context;
    }
    public long insert(SanPham obj){
        ContentValues values=new ContentValues();
        values.put("tensp",obj.getTensp());
        values.put("gia",obj.getGia());
        values.put("manhinh",obj.getManhinh());
        values.put("ram",obj.getRam());
        values.put("dungluong",obj.getDungluong());
        values.put("mahang",obj.getMahang());
        values.put("anh", String.valueOf(obj.getAnh()));

        return db.insert("Sanpham",null,values);
    }

    public  int update(SanPham obj){
        ContentValues values=new ContentValues();
        values.put("tensp",obj.getTensp());
        values.put("gia",obj.getGia());
        values.put("manhinh",obj.getManhinh());
        values.put("ram",obj.getRam());
        values.put("dungluong",obj.getDungluong());
        values.put("mahang",obj.getMahang());
        values.put("anh", String.valueOf(obj.getAnh()));
        return  db.update("Sanpham",values,"masp=?",new String[]{String.valueOf(obj.getMasp())});
    }
    public int delete(String id){
        return db.delete("Sanpham","masp=?",new String[]{id});
    }


    @SuppressLint("Range")
    public List<SanPham> getData(String sql, String... selectionArgs) {
        List<SanPham> list = new ArrayList<SanPham>();
        Cursor c = db.rawQuery(sql, selectionArgs);
        while (c.moveToNext()) {
            SanPham obj = new SanPham();
            Integer mahang = Integer.parseInt(c.getString(c.getColumnIndex("mahang")));
            try {
                obj.setMahang(mahang);
            } catch (IllegalArgumentException e) {
                obj.setMahang(0);
            }
            Integer masp = Integer.parseInt(c.getString(c.getColumnIndex("masp")));
            try {
                obj.setMasp(masp);
            } catch (IllegalArgumentException e) {
                obj.setMahang(0);
            }
            String tensp = c.getString(c.getColumnIndex("tensp"));
            try {
                obj.setTensp(tensp);
            } catch (IllegalArgumentException e) {
                obj.setTensp("Sản phẩm đã bị xoá");
            }
            Float manhinh = Float.parseFloat(c.getString(c.getColumnIndex("manhinh")));
            try {
                obj.setManhinh(manhinh);
            } catch (IllegalArgumentException e) {
                obj.setMahang(0);
            }
            Integer gia = Integer.parseInt(c.getString(c.getColumnIndex("gia")));
            try {
                obj.setGia(gia);
            } catch (IllegalArgumentException e) {
                obj.setMahang(0);
            }
            Integer ram = Integer.parseInt(c.getString(c.getColumnIndex("ram")));
            try {
                obj.setRam(ram);
            } catch (IllegalArgumentException e) {
                obj.setMahang(0);
            }
            Integer dungluong = Integer.parseInt(c.getString(c.getColumnIndex("dungluong")));
            try {
                obj.setRam(dungluong);
            } catch (IllegalArgumentException e) {
                obj.setMahang(0);
            }
            // Check if "anh" column is not null before parsing the URI
            String anhString = c.getString(c.getColumnIndex("anh"));
            if (anhString != null) {
                try {
                    obj.setAnh(Uri.parse(anhString));
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

    public List<SanPham> getAll(){
        String sql="SELECT * FROM Sanpham";
        return  getData(sql);
    }
    public SanPham getID(String id){
        String sql="select * from Sanpham where masp=?";
        List<SanPham> list=getData(sql,id);
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            // Handle the case where the list is empty
            return null;  // Or some other appropriate value
        }
    }



    public ArrayList<SanPham> searchSpByName(String keyword) {
        ArrayList<SanPham> sanPhams = new ArrayList<>();
        String query = "SELECT * FROM Sanpham WHERE tensp LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyword + "%"};
        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                // Check if the column "maloai" exists in the cursor
                int maspIndex = cursor.getColumnIndex("masp");
                int maSp = (maspIndex != -1) ? cursor.getInt(maspIndex) : -1;

                // Check if the column "tenloai" exists in the cursor
                int tenspIndex = cursor.getColumnIndex("tensp");
                String tenLoai = (tenspIndex != -1) ? cursor.getString(tenspIndex) : "";

                int giaspindex = cursor.getColumnIndex("gia");
                int giasp = (giaspindex != -1) ? cursor.getInt(giaspindex) : -1;

                int manhinhindex = cursor.getColumnIndex("manhinh");
                float manhinhsp = (manhinhindex != -1) ? cursor.getFloat(manhinhindex) : -1.0f;


                int ram = cursor.getColumnIndex("ram");
                int ramsp = (ram != -1) ? cursor.getInt(ram) : -1;

                int dungluong = cursor.getColumnIndex("dungluong");
                int dungluongsp = (dungluong != -1) ? cursor.getInt(dungluong) : -1;

                int mahang = cursor.getColumnIndex("mahang");
                int mahangsp = (mahang != -1) ? cursor.getInt(mahang) : -1;

                Uri anhsp=null;
                int anh = cursor.getColumnIndex("anh");
                String anhString = (anh != -1) ? cursor.getString(anh) : null;
                if (anhString != null && !anhString.isEmpty()) {
                    anhsp = Uri.parse(anhString);
                    // Continue processing Uri as needed
                } else {
                    anhsp= Uri.parse("/a");
                    Log.d("SanPhamDAO", "anhString is null or empty. No further action is taken.");
                }
                // Only add to the list if maLoai is valid
                if (maSp != -1) {
                    SanPham sanPham = new SanPham(maSp, tenLoai, giasp, ramsp, dungluongsp, manhinhsp, mahangsp, anhsp);
                    sanPhams.add(sanPham);
                }
            } while (cursor.moveToNext());

            cursor.close(); // Close the cursor when done
        }

        return sanPhams;
   }

}
