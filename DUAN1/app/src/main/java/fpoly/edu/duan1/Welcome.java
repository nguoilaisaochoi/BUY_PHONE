package fpoly.edu.duan1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;


public class Welcome extends AppCompatActivity {
    private static final int REQUEST_PERMISSION_CODE = 10;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.manhinh_chao);
        hideSystemUI();
        ycquyen();
    }



    public void ycquyen(){
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(Welcome.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }, 1500);
        }else {
            String[] quyen={Manifest.permission.READ_EXTERNAL_STORAGE};
            requestPermissions(quyen,REQUEST_PERMISSION_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode ==REQUEST_PERMISSION_CODE){
        if (grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(this,"Đã cấp quyền",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);
            finish();
        }else{
            //Toast.makeText(this,"Đã cấp quyền",Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Welcome.this, Login.class);
            startActivity(intent);
            finish();
        }
    }
    }
    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }
}