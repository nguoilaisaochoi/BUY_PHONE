package fpoly.edu.duan1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.Model.KhachHang;

public class Dangki extends AppCompatActivity {

    EditText tk, hoten, mk, rmk;

    KhachHangDAO dao;
    KhachHang item;

    Button btndk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_dangky);
        tk = findViewById(R.id.edt_emailLogin);
        mk = findViewById(R.id.dkmatkhau);
        hoten = findViewById(R.id.hoten);
        rmk = findViewById(R.id.dkllmathau);
        btndk = findViewById(R.id.btn_dangki);
        hideSystemUI();
        dao = new KhachHangDAO(getApplicationContext());
        btndk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valiable() > 0) {
                    item = new KhachHang();
                    item.setMakh(tk.getText().toString());
                    item.setMatKhau(mk.getText().toString());
                    item.setHoTen(hoten.getText().toString());
                    if (dao.insert(item) > 0) {
                        Context context = getApplicationContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Đăng kí thành công");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Context context = getApplicationContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Tài khoản đã tồn tại");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();
                    }
                }
            }
        });
    }

    public int valiable() {
        int check = 1;
        if (tk.getText().length() == 0 || mk.getText().length() == 0 || rmk.getText().length() == 0) {
            Context context = getApplicationContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Không bỏ trống");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            check = -1;
        }

        if (!mk.getText().toString().equals(rmk.getText().toString())) {
            Context context = getApplicationContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Mật khẩu mới không trùng khớp");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            check = -1;
        }
        return check;
    }

    private void hideSystemUI() {
// Đặt cho activity hiển thị toàn màn hình
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}