package fpoly.edu.duan1.Adapter;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fpoly.edu.duan1.DAO.HoaDonDAO;
import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.DAO.SanPhamDAO;
import fpoly.edu.duan1.Fragment.HoaDonFragment;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.Model.KhachHang;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;


public class HoaDon_ADMIN_Adapter extends ArrayAdapter<GioHang> {
    private Context context;
    private ArrayList<GioHang> lists;
    private HashMap<String, List<GioHang>> groupedItems = new HashMap<>();
    private List<String> groupDates = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private KhachHangDAO khachHangDAO;
    private HoaDonDAO hoaDonDAO;

    private SanPhamDAO sanPhamDAO;

    TextView tenkh, tensp, tongtien, sdt, diachi, lydotc;


    HoaDonFragment fragment;
    Button tuchoi, giaohang, xacnhan, huy;

    CardView cardView;
    LinearLayout ttxacnhan;

    Integer check = 0;

    public HoaDon_ADMIN_Adapter(@NonNull Context context, HoaDonFragment hoaDonKHFragment, ArrayList<GioHang> lists) {
        super(context, 0, lists);
        this.context = context;
        this.lists = lists;
        this.groupDataByDate();
        khachHangDAO = new KhachHangDAO(context);
        sanPhamDAO = new SanPhamDAO(context);
        this.hoaDonDAO = new HoaDonDAO(context);
        this.fragment = hoaDonKHFragment; // Set the fragment variable
    }

    private void groupDataByDate() {
        groupedItems.clear();
        groupDates.clear();

        Collections.sort(lists, new Comparator<GioHang>() {
            @Override
            public int compare(GioHang item1, GioHang item2) {
                // Compare the dates in reverse order
                return item2.getNgay().compareTo(item1.getNgay());
            }
        });
        for (GioHang item : lists) {
            String dateKey = formatter.format(item.getNgay());

            if (groupedItems.containsKey(dateKey)) {
                groupedItems.get(dateKey).add(item);
            } else {
                List<GioHang> newGroup = new ArrayList<>();
                newGroup.add(item);
                groupedItems.put(dateKey, newGroup);
                groupDates.add(dateKey);
            }
        }
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.hoa_don_admin_one, null);
        }

        String dateKey = groupDates.get(position);
        List<GioHang> groupItems = groupedItems.get(dateKey);
        LinearLayout ld = v.findViewById(R.id.ld);
        EditText  lydo = v.findViewById(R.id.etlydo);
        if (groupItems != null && !groupItems.isEmpty()) {
            TextView headerTextView = v.findViewById(R.id.tvngay);
            headerTextView.setText("Ngày: " + dateKey);
            StringBuilder allsp = new StringBuilder();
            for (GioHang item : groupItems) {
                //tinh trang
                if (item.isLdVisible()) {
                    ld.setVisibility(View.VISIBLE);
                } else {
                    ld.setVisibility(View.GONE);
                }
                TextView tinhtrang = v.findViewById(R.id.ad_tvtinhtrang);
                String ttrang = item.getTinhtrang();
                ttxacnhan = v.findViewById(R.id.xacnhan);
                xacnhan = v.findViewById(R.id.btn_xacnhan);
                huy = v.findViewById(R.id.btn_huy);
                cardView=v.findViewById(R.id.cardview);
                tenkh = v.findViewById(R.id.tvtenkh);
                KhachHang kh = khachHangDAO.getID(item.getMakh());
                tenkh.setText("Tên khách hàng: " + kh.getHoTen());
                SanPham sanPham = sanPhamDAO.getID(String.valueOf(item.getMasp()));
                if (sanPham != null) {
                    allsp.append(sanPham.getTensp());
                } else {
                    allsp.append("Sản phẩm đã bị xoá");
                    check = -1;
                }
                allsp.append(" (" + item.getSoluong() + ")");
                allsp.append(", ");
                tongtien = v.findViewById(R.id.tvtongtien);
                String formattedAmount = chuyendvtien(hoaDonDAO.sumTongTienTheoMaKHTheoNgay(item.getMakh(), dateKey));
                tongtien.setText("Tổng tiền: " + formattedAmount);
                sdt = v.findViewById(R.id.tvsdt);
                sdt.setText("Số điện thoại: " + kh.getSdt());
                diachi = v.findViewById(R.id.tvdiachi);
                diachi.setText("Địa chỉ: " + kh.getDiachi());
                lydotc = v.findViewById(R.id.tvlydotuchoi);
                if (check >= 0) {
                    if (ttrang.equals("Đang giao hàng...")) {
                        String trangthai = "Chờ giao hàng";
                        tinhtrang.setText(trangthai);
                        tinhtrang.setTextColor(0xFFF39C12);
                        ttxacnhan.setVisibility(View.VISIBLE);
                        lydotc.setVisibility(View.GONE);
                    } else if (ttrang.equals("Cửa hàng từ chối giao hàng")) {
                        String trangthai = "Đã từ chối";
                        tinhtrang.setText(trangthai);
                        tinhtrang.setTextColor(0xFFCD1212);
                        ttxacnhan.setVisibility(View.GONE);
                        lydotc.setText("Lý do từ chối: " + item.getTuchoi());
                    } else if (ttrang.equals("Đã giao")) {
                        tinhtrang.setText(ttrang);
                        tinhtrang.setTextColor(0xFF27AE60);
                        ttxacnhan.setVisibility(View.GONE);
                        lydotc.setVisibility(View.GONE);
                    }
                } else {
                    String Del = "Bị huỷ do có sản phảm không tồn tại";
                    tinhtrang.setText(Del);
                    tinhtrang.setTextColor(0xFFCD1212);
                    ttxacnhan.setVisibility(View.GONE);
                }

                tuchoi = v.findViewById(R.id.btn_tuchoi);
                tuchoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set trạng thái isLdVisible cho mục hiện tại khi nhấn nút "tuchoi"
                        item.setLdVisible(!item.isLdVisible());
                        notifyDataSetChanged(); // Cập nhật Adapter để hiển thị thay đổi
                        //thu hồi bàn phím ảo
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                });
                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Set trạng thái isLdVisible cho mục hiện tại khi nhấn nút "tuchoi"
                        item.setLdVisible(false);
                        notifyDataSetChanged(); // Cập nhật Adapter để hiển thị thay đổi
                        //thu hồi bàn phím ảo
                        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                });

                xacnhan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        lydo.requestFocus();
                        Log.d("YourTag", "lydo: " + lydo.getText().toString());
                        String tuchoi = lydo.getText().toString();
                        if (checklydo(tuchoi) > 0) {
                            hoaDonDAO.updateTinhTrang(dateKey, "Cửa hàng từ chối giao hàng", tuchoi);
                            ld.setVisibility(View.GONE);
                            fragment.capnhatlv();
                        }
                    }
                });

                giaohang = v.findViewById(R.id.btn_giao);
                giaohang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hoaDonDAO.updateTinhTrang(dateKey, "Đã giao", "");
                        fragment.capnhatlv();
                    }
                });
            }
            tensp = v.findViewById(R.id.tvtensp);
            String tensp = allsp.toString();
            if (tensp.endsWith(",")) {
                //Trả về chuỗi con bắt đầu từ vị trí 0 (đầu chuỗi) đến vị trí
                // tensp.length() - 1 (trừ đi một ký tự cuối cùng).
                tensp = tensp.substring(0, tensp.length() - 1);
            }
            this.tensp.setText("Sản phẩm đã mua: \n" + tensp);
        }
        check = 0;
        return v;
    }

    @Override
    public int getCount() {
        return groupDates.size();
    }

    public String chuyendvtien(int amount) {
        // Định dạng kiểu tiền tệ Việt Nam
        java.text.DecimalFormat currencyFormat = new java.text.DecimalFormat("###,###,###,### đ");

        // Chuyển đổi giá trị từ int sang chuỗi tiền tệ
        return currencyFormat.format(amount);
    }

    public int checklydo(String txtlydo) {
        int checkld = 1;
        if (txtlydo.length() == 0) {
            Context context = getContext();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Lý do từ chối trống");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            checkld = -1;
        }
        return checkld;
    }
}
