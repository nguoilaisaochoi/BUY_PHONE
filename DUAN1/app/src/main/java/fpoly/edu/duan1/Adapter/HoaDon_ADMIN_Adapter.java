package fpoly.edu.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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

    TextView   tenkh,tensp, tongtien,tinhtrang,sdt,diachi;

    HoaDonFragment fragment;
    Button tuchoi,giaohang;

    LinearLayout ttxacnhan;
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
        if (groupItems != null && !groupItems.isEmpty()) {
            TextView headerTextView = v.findViewById(R.id.tvngay);
            headerTextView.setText("Ngày: " + dateKey);
            StringBuilder allsp = new StringBuilder();
            for (GioHang item : groupItems) {
                tenkh=v.findViewById(R.id.tvtenkh);
                KhachHang kh=khachHangDAO.getID(item.getMakh());
                tenkh.setText("Tên khách hàng: "+kh.getHoTen());
                SanPham sanPham = sanPhamDAO.getID(String.valueOf(item.getMasp()));
                allsp.append(sanPham.getTensp());
                allsp.append(" ("+item.getSoluong()+")");
                allsp.append(", ");
                tongtien=v.findViewById(R.id.tvtongtien);
                String formattedAmount = chuyendvtien(hoaDonDAO.sumTongTienTheoMaKHTheoNgay(item.getMakh(), dateKey));
                tongtien.setText("Tổng tiền: " +formattedAmount );
                sdt=v.findViewById(R.id.tvsdt);
                sdt.setText("Số điện thoại: "+kh.getSdt());
                diachi=v.findViewById(R.id.tvdiachi);
                diachi.setText("Địa chỉ: "+kh.getDiachi());
                ttxacnhan=v.findViewById(R.id.xacnhan);
                String ttrang=item.getTinhtrang();
                tinhtrang=v.findViewById(R.id.tvtinhtrang);
                if (ttrang.equals("Chờ xử lí")) {
                    // Nếu tình trạng là "Chờ xử lý", thêm chữ "Tình trạng:" và đặt màu sắc thành vàng
                    tinhtrang.setText(ttrang);
                    tinhtrang.setTextColor(0xFFF39C12);
                    ttxacnhan.setVisibility(View.VISIBLE);
                } else if (ttrang.equals("Hết hàng")) {
                    tinhtrang.setText(ttrang);
                    tinhtrang.setTextColor(0xFFCD1212);
                    ttxacnhan.setVisibility(View.GONE);
                } else if (ttrang.equals("Đã giao")) {
                    tinhtrang.setText(ttrang);
                    tinhtrang.setTextColor(0xFF27AE60);
                    ttxacnhan.setVisibility(View.GONE);
                }
                tuchoi=v.findViewById(R.id.btn_tuchoi);
                tuchoi.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Update the UI to reflect the changes
                        hoaDonDAO.updateTinhTrang(dateKey, "Hết hàng");
                        fragment.capnhatlv();
                    }
                });
                giaohang=v.findViewById(R.id.btn_giao);
                giaohang.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hoaDonDAO.updateTinhTrang(dateKey, "Đã giao");
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
