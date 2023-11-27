package fpoly.edu.duan1.Fragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import fpoly.edu.duan1.DAO.HoaDonDAO;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.R;

public class DoanhThuFragment extends Fragment {


    HoaDonDAO dao;
    GioHang item;

    Button btntungay,btndenngay,btnkiemtra;
    EditText ettungay,etdenngay;
    TextView tvdoanhthu;

    int myear,mmonth,mday;

    SimpleDateFormat sdf= new SimpleDateFormat("yyy/MM/dd");
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v=getLayoutInflater().inflate(R.layout.doanh_thu,container,false);
        btntungay=v.findViewById(R.id.btntungay);
        btndenngay=v.findViewById(R.id.btndenngay);
        btnkiemtra=v.findViewById(R.id.kiemtra);
        ettungay=v.findViewById(R.id.ettungay);
        etdenngay=v.findViewById(R.id.etdenngay);
        tvdoanhthu=v.findViewById(R.id.tvdoanhthu);
        dao = new HoaDonDAO(getContext());
        btntungay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                myear=c.get(Calendar.YEAR);
                mmonth=c.get(Calendar.MONTH);
                mday=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d=new DatePickerDialog(getActivity(),0,mdatetungay,myear,mmonth,mday);
                d.show();
            }
        });
        btndenngay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar c=Calendar.getInstance();
                myear=c.get(Calendar.YEAR);
                mmonth=c.get(Calendar.MONTH);
                mday=c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog d=new DatePickerDialog(getActivity(),0,mdatedenngay,myear,mmonth,mday);
                d.show();
            }
        });
        btnkiemtra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tungay=ettungay.getText().toString();
                String denngay=etdenngay.getText().toString();
                HoaDonDAO hoaDonDAO =new HoaDonDAO(getActivity());
                tvdoanhthu.setText(chuyendvtien(hoaDonDAO.getDoanhThu(tungay,denngay)));
            }
        });
        return v;
    }
    DatePickerDialog.OnDateSetListener mdatetungay =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myear=year;
            mmonth=month;
            mday=dayOfMonth;
            GregorianCalendar c=new GregorianCalendar(myear,mmonth,mday);
            ettungay.setText(sdf.format(c.getTime()));
        }
    };
    DatePickerDialog.OnDateSetListener mdatedenngay =new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myear=year;
            mmonth=month;
            mday=dayOfMonth;
            GregorianCalendar c=new GregorianCalendar(myear,mmonth,mday);
            etdenngay.setText(sdf.format(c.getTime()));
        }
    };
    public String chuyendvtien(int amount) {
        // Định dạng kiểu tiền tệ Việt Nam
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("###,###,###,### đ");

        // Chuyển đổi giá trị từ int sang chuỗi tiền tệ
        return currencyFormat.format(amount);
    }
}
