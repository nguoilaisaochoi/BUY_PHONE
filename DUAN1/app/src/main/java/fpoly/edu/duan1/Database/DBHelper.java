package fpoly.edu.duan1.Database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "BUYPHONE";
    private static final int DB_VERSION = 1;



    static final String CREATE_TABLE_KHACH_HANG = " CREATE TABLE Khachhang (" +
            "makh    TEXT PRIMARY KEY," +
            "hoten   TEXT NOT NULL," +
            "sdt     INTEGER ," +
            "diachi     TEXT ," +
            "avt     TEXT ," +
            "matKhau TEXT NOT NULL" +
            ")";

    static final String CREATE_TABLE_SANPHAM = "CREATE TABLE Sanpham (" +
            "masp  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "tensp TEXT    NOT NULL," +
            "gia INTEGER NOT NULL," +
            "manhinh FLOAT    NOT NULL," +
            "ram INTEGER    NOT NULL," +
            "dungluong INTEGER    NOT NULL," +
            "mahang  INTEGER REFERENCES LoaiSach (Hangsp)," +
            "anh TEXT" +
            ")";

    static final String CREATE_TABLE_HANG_SP = "CREATE TABLE Hangsp (" +
            "mahang  INTEGER PRIMARY KEY AUTOINCREMENT," +
            "tenloai TEXT    NOT NULL," +
            "anhloai TEXT" +
            ")";



    static  final  String CREATE_TABLE_Hoadon = "CREATE TABLE Hoadon (" +
            "magh     INTEGER PRIMARY KEY AUTOINCREMENT," +
            "masp   INTEGER REFERENCES Sanpham (masp)," +
            "makh   TEXT REFERENCES Khachhang (makh)," +
            "mahang   INTEGER REFERENCES Hangsp (mahang)," +
            "tongtien INTEGER NOT NULL," +
            "ngay     DATE," +
            "tinhtrang   TEXT," +
            "soluong INTEGER NOT NULL" +
            ")";

    public void createNewTableFromThanhVien(String user) {
        // Tạo tên bảng dựa trên tên người dùng
        String tableName = "Giohang_" + user;
        // Thực hiện tạo bảng mới
        String createTableSQL = "CREATE TABLE " + tableName + " (" +
                "magh     INTEGER PRIMARY KEY AUTOINCREMENT," +
                "makh   TEXT REFERENCES Khachhang (makh)," +
                "masp   INTEGER REFERENCES Sanpham (masp)," +
                "mahang   INTEGER REFERENCES Hangsp (mahang)," +
                "tongtien INTEGER NOT NULL," +
                "soluong INTEGER NOT NULL" +
                ")";
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(createTableSQL);
    }

    public boolean isTableExists(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                new String[]{"table", tableName}
        );
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }
    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_KHACH_HANG);
        db.execSQL(CREATE_TABLE_SANPHAM);
        db.execSQL(CREATE_TABLE_HANG_SP);
        db.execSQL(CREATE_TABLE_Hoadon);
        db.execSQL(Data_SQLite.INSERT_KHACH_HANG);
        db.execSQL(Data_SQLite.INSERT_SANPHAM);
        db.execSQL(Data_SQLite.INSERT_HANG_SP);
        //db.execSQL(Data_SQLite.INSERT_THANHVIEN);
        //db.execSQL(Data_SQLite.INSERT_PHIEUMUON);
        //db.execSQL(ADD_COLUMN_MA_TT_THANH_VIEN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTableLoaiKhachhang = "drop table if exists Khachhang";
        db.execSQL(dropTableLoaiKhachhang);
        String dropTableLoaiSanpham = "drop table if exists Sanpham";
        db.execSQL(dropTableLoaiSanpham);
        String dropTableHangsp= "drop table if exists Hangsp";
        db.execSQL(dropTableHangsp);

        onCreate(db);
    }
}
