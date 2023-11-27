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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.Model.KhachHang;


public class Login extends AppCompatActivity {

    TextView dk;
    KhachHangDAO dao;
    EditText edtk,edmk;
    Button btnlogin;
    SharedPreferences sharedPreferences;
    private static final String PREF_ROLE_KEY = "user_role";
    String user, pass;

    CheckBox remember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_login);
        dk = findViewById(R.id.dangki);
        edtk = findViewById(R.id.taikhoan);
        edmk = findViewById(R.id.matkhau);
        btnlogin = findViewById(R.id.btn_login);
        remember = findViewById(R.id.chkghinho);
        dao = new KhachHangDAO(this);
        hideSystemUI();
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        // doc user,pass tring sharđded
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        String user = pref.getString("USERNAME", "");
        String pass = pref.getString("PASSWORD", "");
        boolean rem = pref.getBoolean("REMEMBER", false);

        edtk.setText(user);
        edmk.setText(pass);
        remember.setChecked(rem);
        dk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Dangki.class);
                startActivity(intent);
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
        //toast

    }


    public void checkLogin() {
        user = edtk.getText().toString();
        pass = edmk.getText().toString();
        if (user.isEmpty() || pass.isEmpty()) {
            Context context = getApplicationContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Không được bỏ trống");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            //Toast.makeText(getApplicationContext(), "Không được bỏ trống", Toast.LENGTH_SHORT).show();
        } else {
            if (dao.checkLogin(user, pass) >= 0) {
                rememberuser(user, pass, remember.isChecked());
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(PREF_ROLE_KEY, user.equals("admin") ? "admin" : "user");
                editor.apply();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("user", user);
                startActivity(intent);
                finish();
            } else {
                Context context = getApplicationContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Tài khoản không đúng");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();

                //Toast.makeText(getApplicationContext(),"Tài khoản không đúng",Toast.LENGTH_SHORT).show();

            }
        }
    }

    public void rememberuser(String u, String p, boolean status) {
        SharedPreferences pref = getSharedPreferences("USER_FILE", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        if (!status) {
            //xoa tinh nang luu truoc do
            editor.clear();
        } else {
            //luu du lieu
            editor.putString("USERNAME", u);
            editor.putString("PASSWORD", p);
            editor.putBoolean("REMEMBER", status);
        }
        //luu lai toan bo
        editor.commit();
    }

    private void hideSystemUI() {
// Đặt cho activity hiển thị toàn màn hình
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    }
}