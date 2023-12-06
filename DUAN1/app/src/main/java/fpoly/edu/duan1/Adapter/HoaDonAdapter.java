package fpoly.edu.duan1.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import fpoly.edu.duan1.DAO.HoaDonDAO;
import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.DAO.SanPhamDAO;
import fpoly.edu.duan1.Model.KhachHang;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import fpoly.edu.duan1.Fragment.HoaDonFragment;
import fpoly.edu.duan1.Model.GioHang;



public class HoaDonAdapter extends ArrayAdapter<GioHang> {
    private Context context;
    private ArrayList<GioHang> lists;
    private HashMap<String, List<GioHang>> groupedItems = new HashMap<>();
    private List<String> groupDates = new ArrayList<>();
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private KhachHangDAO khachHangDAO;
    private HoaDonDAO hoaDonDAO;

    private SanPhamDAO sanPhamDAO;

    TextView   tensp, tongtien,tinhtrang,lydotc;


    HoaDonFragment fragment;

    Integer check=0;
    public HoaDonAdapter(@NonNull Context context, HoaDonFragment hoaDonKHFragment, ArrayList<GioHang> lists) {
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
            v = inflater.inflate(R.layout.hoa_don_one, null);
        }

        String dateKey = groupDates.get(position);
        List<GioHang> groupItems = groupedItems.get(dateKey);
        if (groupItems != null && !groupItems.isEmpty()) {
            TextView headerTextView = v.findViewById(R.id.tvngay);
            headerTextView.setText("Ngày: " + dateKey);
            StringBuilder allsp = new StringBuilder();
            for (GioHang item : groupItems) {
                SanPham sanPham = sanPhamDAO.getID(String.valueOf(item.getMasp()));
                if (sanPham!=null){
                    allsp.append(sanPham.getTensp());
                }else{
                    allsp.append("Sản phẩm đã bị xoá");
                    check=-1;
                }
                allsp.append(" ("+item.getSoluong()+")");
                allsp.append(", ");
                tongtien=v.findViewById(R.id.tvtongtien);
                String formattedAmount = chuyendvtien(hoaDonDAO.sumTongTienTheoMaKHTheoNgay(item.getMakh(), dateKey));
                tongtien.setText("Tổng tiền: " +formattedAmount );
                tinhtrang=v.findViewById(R.id.tvtinhtrang);
                String ttrang=item.getTinhtrang();
                lydotc=v.findViewById(R.id.tvlydotc);
                if (check>=0){
                    if (ttrang.equals("Đang giao hàng...")) {
                        tinhtrang.setText(ttrang);
                        tinhtrang.setTextColor(0xFFF39C12);
                        lydotc.setVisibility(View.GONE);
                    } else if (ttrang.equals("Cửa hàng từ chối giao hàng")) {
                        tinhtrang.setText(ttrang);
                        lydotc.setText("Lý do từ chối: "+item.getTuchoi());
                        tinhtrang.setTextColor(0xFFCD1212);
                    } else if (ttrang.equals("Đã giao")) {
                        tinhtrang.setText(ttrang);
                        tinhtrang.setTextColor(0xFF27AE60);
                        lydotc.setVisibility(View.GONE);
                    }
                }else{
                    String Del="Bị huỷ do có sản phảm không tồn tại";
                    tinhtrang.setText(Del);
                    tinhtrang.setTextColor(0xFFCD1212);
                }
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
        check=0;
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

}
