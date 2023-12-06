package fpoly.edu.duan1.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import fpoly.edu.duan1.Adapter.GioHangAdapter;
import fpoly.edu.duan1.DAO.HoaDonDAO;
import fpoly.edu.duan1.DAO.GioHang_KHDAO;
import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.Model.KhachHang;
import fpoly.edu.duan1.R;

public class GioHangFragment extends Fragment {
    ListView lvsp;

    static GioHang_KHDAO dao;

    static KhachHangDAO khdao;
    ArrayList<GioHang> list;

    GioHangAdapter adapter;

    TextView tongtien,diachi;

    Button mua;
    SharedPreferences nav_name;

    KhachHangDAO khachHangDAO;

    KhachHang item;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.gio_hang, container, false);
        lvsp = v.findViewById(R.id.list_giohang);
        dao = new GioHang_KHDAO(getActivity());
        khdao = new KhachHangDAO(getActivity());
        tongtien = v.findViewById(R.id.tvtongtien);
        mua = v.findViewById(R.id.btnmua);
        diachi=v.findViewById(R.id.tvdiachi);
        capnhatlv();
        nav_name = getActivity().getSharedPreferences("name_Nav", MODE_PRIVATE);
        String id = nav_name.getString("makh", "");
        khachHangDAO=new KhachHangDAO(getContext());
        item=khachHangDAO.getID(id);
        diachi.setText("Địa chỉ: "+item.getDiachi());
        mua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (valiable()>0){
                // Lấy tất cả các mục từ GioHang_KHDAO
                List<GioHang> gioHangList = dao.getAll();
                // Kiểm tra xem danh sách có rỗng không
                if (!gioHangList.isEmpty()) {
                    // Khởi tạo GioHangDAO
                    HoaDonDAO hoaDonDAO = new HoaDonDAO(getActivity());
                    // Lặp qua từng mục trong danh sách và chèn nó vào GioHangDAO
                    for (GioHang gioHang : gioHangList) {
                        // Lấy ngày hiện tại
                        //Date currentDate = getCurrentDateTime();

                        // Set ngày hiện tại cho gioHang
                        gioHang.setNgay(getCurrentDateTime());

                        gioHang.setMakh(id);
                        gioHang.setTinhtrang("Đang giao hàng...");
                        // Chèn mục vào GioHangDAO
                        long result = hoaDonDAO.insert(gioHang, getActivity());

                        // Kiểm tra xem việc chèn có thành công không
                        if (result > 0) {
                            // Nếu thành công, xóa mục khỏi GioHang_KHDAO
                            dao.delete(String.valueOf(gioHang.getMagh()));

                            Intent updateBadgeIntent = new Intent("update_badge");
                            LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(updateBadgeIntent);
                        } else {
                            // Xử lý nếu việc chèn thất bại nếu cần
                            Toast.makeText(getActivity(), "Lỗi", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }

                    // Sau khi chuyển tất cả các mục, cập nhật ListView và tổng số tiền
                    capnhatlv();
                    hienThiTongThanhToan();

                    // Hiển thị thông báo thành công
                    Context context = getContext();
                    LayoutInflater inflater = getLayoutInflater();
                    View customToastView = inflater.inflate(R.layout.customtoast, null);
                    TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                    textView.setText("Mua thành công");

                    Toast customToast = new Toast(context);
                    customToast.setDuration(Toast.LENGTH_SHORT);
                    customToast.setView(customToastView);
                    customToast.show();
                    //Toast.makeText(getActivity(), "Đã mua", Toast.LENGTH_SHORT).show();
                } else {
                    Context context = getContext();
                    LayoutInflater inflater = getLayoutInflater();
                    View customToastView = inflater.inflate(R.layout.customtoast, null);
                    TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                    textView.setText("Không có sản phẩm");

                    Toast customToast = new Toast(context);
                    customToast.setDuration(Toast.LENGTH_SHORT);
                    customToast.setView(customToastView);
                    customToast.show();
                    // Hiển thị thông báo nếu danh sách GioHang_KHDAO trống rỗng
                    //Toast.makeText(getActivity(), "Không có sản phẩm", Toast.LENGTH_SHORT).show();
                }
            }
            }
        });

        return v;
    }
    public int valiable() {
        nav_name = getActivity().getSharedPreferences("name_Nav", MODE_PRIVATE);
        String id = nav_name.getString("makh", "");
        khachHangDAO=new KhachHangDAO(getContext());
        item=khachHangDAO.getID(id);
        int check = 1;
        if (item.getSdt()==0||item.getDiachi().isEmpty()){
            Context context = getContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Hãy điền địa chỉ và số điện thoại tại thông tin tài khoản");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            check =-1;
        }
        return check;
    }
    public void capnhatlv() {
        int firstVisibleItem = lvsp.getFirstVisiblePosition();
        View v = lvsp.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - lvsp.getPaddingTop());
        list = (ArrayList<GioHang>) dao.getAll();
        adapter = new GioHangAdapter(getActivity(), this, list);
        lvsp.setAdapter(adapter);
        hienThiTongThanhToan();
        lvsp.setSelectionFromTop(firstVisibleItem, top);
    }

    public void xoa(final String ID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Muốn xoá?!");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dao.delete(ID);
                        capnhatlv();
                        Intent updateBadgeIntent = new Intent("update_badge");
                        LocalBroadcastManager.getInstance(getActivity()).sendBroadcast(updateBadgeIntent);
                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    public void hienThiTongThanhToan() {
        int tongThanhToan = 0;
        for (GioHang gioHang : list) {
            tongThanhToan += gioHang.getTongtien();
        }
        String formattedAmount = chuyendvtien(tongThanhToan);
        tongtien.setText(formattedAmount);
    }

    public String chuyendvtien(int amount) {
        // Định dạng kiểu tiền tệ Việt Nam
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("###,###,###,### đ");

        // Chuyển đổi giá trị từ int sang chuỗi tiền tệ
        return currencyFormat.format(amount);
    }

    private LocalDateTime getCurrentDateTime() {
        return LocalDateTime.now();
    }

}
