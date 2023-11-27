package fpoly.edu.duan1.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.Model.KhachHang;
import fpoly.edu.duan1.R;

public class DoimkFM extends Fragment {


    static KhachHangDAO dao;



    EditText mkc,mkm,nlmk;

    Button luumk;

    KhachHang item;
    String matkhaucu;

    SharedPreferences nav_name;

    KhachHangDAO khachHangDAO;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=getLayoutInflater().inflate(R.layout.doi_mat_khau,container,false);
        dao = new KhachHangDAO(getActivity());
        mkc=v.findViewById(R.id.mkcu);
        mkm=v.findViewById(R.id.makmoi);
        nlmk=v.findViewById(R.id.mkll);
        luumk=v.findViewById(R.id.luumk);
        luumk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SharedPreferences pref=getActivity().getSharedPreferences("USER_FILE", Context.MODE_PRIVATE);
                //String user=pref.getString("USERNAME","");
                nav_name = getActivity().getSharedPreferences("name_Nav", MODE_PRIVATE);
                String hoten = nav_name.getString("makh", "");
                khachHangDAO = new KhachHangDAO(getContext());
                KhachHang khachhang=dao.getID(hoten);
                matkhaucu=khachhang.getMatKhau();
                if(valiable()>0){
                    khachhang.setMatKhau(mkm.getText().toString());
                    dao.updatepass(khachhang);
                    if (dao.updatepass(khachhang)>0){
                        Context context = getContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Đổi thành công");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();
                        //Toast.makeText(getContext(),"Đổi thành công", Toast.LENGTH_SHORT).show();
                        mkc.setText("");
                        mkm.setText("");
                        nlmk.setText("");
                    }else {}
                }
            }
        });
        return v;
    }
    public int valiable(){
        int check=1;
        if(mkc.getText().length()==0||mkm.getText().length()==0||nlmk.getText().length()==0){
            Context context = getContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Không bỏ trống");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            //Toast.makeText(getContext(),"Không bỏ trống", Toast.LENGTH_SHORT).show();
            check=-1;
        }else{
            String mkmoi=mkm.getText().toString();
            String mknhaplai=nlmk.getText().toString();
            if (!matkhaucu.equals(mkc.getText().toString())){
                Context context = getContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Mật khẩu cũ sai");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();
                //Toast.makeText(getContext(),"Mật khẩu cũ sai", Toast.LENGTH_SHORT).show();
                check=-1;
            }
            if(!mkmoi.equals(mknhaplai)){
                Context context = getContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Mật khẩu không trùng khớp");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();
                //Toast.makeText(getContext(),"Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                check=-1;
            }
        }
        return check;
    }

}
