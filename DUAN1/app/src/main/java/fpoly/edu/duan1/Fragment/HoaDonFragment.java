package fpoly.edu.duan1.Fragment;

import static android.content.Context.MODE_PRIVATE;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

import fpoly.edu.duan1.Adapter.HoaDonAdapter;
import fpoly.edu.duan1.Adapter.HoaDon_ADMIN_Adapter;
import fpoly.edu.duan1.DAO.HoaDonDAO;
import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.DAO.SanPhamDAO;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;

public class HoaDonFragment extends Fragment {
    ListView lvsp;

    static HoaDonDAO dao;

    ArrayList<GioHang> list;

    HoaDonAdapter adapter;

    HoaDon_ADMIN_Adapter admin_adapter;
    KhachHangDAO khachHangDAO;

    SanPhamDAO sanPhamDAO;

    SharedPreferences nav_name;

    private static final String PREF_ROLE_KEY = "user_role";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.hoadonfragment, container, false);
        lvsp = v.findViewById(R.id.lvhd);
        dao = new HoaDonDAO(getActivity());
        khachHangDAO = new KhachHangDAO(getContext());
        sanPhamDAO = new SanPhamDAO(getContext());
        capnhatlv();
        return v;
    }

    public void capnhatlv() {
        int firstVisibleItem = lvsp.getFirstVisiblePosition();
        View v = lvsp.getChildAt(0);
        int top = (v == null) ? 0 : (v.getTop() - lvsp.getPaddingTop());
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString(PREF_ROLE_KEY, "");
        nav_name = getActivity().getSharedPreferences("name_Nav", MODE_PRIVATE);
        if (userRole.equals("admin")) {
            list = (ArrayList<GioHang>) dao.getAll();
            admin_adapter = new HoaDon_ADMIN_Adapter(getActivity(), this, list);
            lvsp.setAdapter(admin_adapter);
        } else {
            String tenDangNhap = nav_name.getString("makh", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                list = (ArrayList<GioHang>) dao.getAllByMaKH(tenDangNhap);
            }
            adapter = new HoaDonAdapter(getActivity(), this, list);
            lvsp.setAdapter(adapter);
        }
        lvsp.setSelectionFromTop(firstVisibleItem, top);
    }
    // Trong phương thức animateListView

}
