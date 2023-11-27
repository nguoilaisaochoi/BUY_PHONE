package fpoly.edu.duan1.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import java.util.ArrayList;

import fpoly.edu.duan1.DAO.GioHang_KHDAO;
import fpoly.edu.duan1.DAO.HangSpDAO;
import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.R;
import fpoly.edu.duan1.DAO.SanPhamDAO;
import fpoly.edu.duan1.Adapter.SanPhamAdapter;
import fpoly.edu.duan1.Model.SanPham;

public class SanPhamFragment extends Fragment {
    ListView lvsp;

    static SanPhamDAO dao;

    static GioHang_KHDAO daogh_kh;


    ArrayList<SanPham> list;

    SanPhamAdapter adapter;


    GioHang item;

    GioHang item2;
    KhachHangDAO khachHangDAO;
    SanPhamDAO sanPhamDAO;

    HangSpDAO hangSpDAO;
    SearchView searchView;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.san_pham_user, container, false);
        lvsp = v.findViewById(R.id.lvsp);
        dao = new SanPhamDAO(getActivity());
        daogh_kh = new GioHang_KHDAO(getActivity());
        searchView = v.findViewById(R.id.search_sp);
        searchView.setFocusable(false);
        capnhatlv();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchSanpham(newText);
                return false;
            }
        });
        lvsp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                searchView.clearFocus();
                return false;
            }
        });
        return v;
    }

    void capnhatlv() {
        list = (ArrayList<SanPham>) dao.getAll();
        adapter = new SanPhamAdapter(getActivity(), this, list);
        lvsp.setAdapter(adapter);
    }

    public void addgh(int masp, int mahang) {

        boolean maspExists = daogh_kh.isMaspExists(masp);
        item = new GioHang();
        item.setMasp(masp);
        item.setMahang(mahang);
        if (maspExists) {
            item.setMagh(daogh_kh.gettMagh(masp));
            int currentSoluong = daogh_kh.gettsoluong(item.getMagh()) + 1;
            item.setSoluong(currentSoluong);
            // Cập nhật lại trong cơ sở dữ liệu
            if (daogh_kh.update(item, getContext()) > 0) {
                Context context = getContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Đã thêm");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();
                //Toast.makeText(getContext(), "Đã thêm", Toast.LENGTH_SHORT).show();
            } else {
                Context context = getContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Khônh thêm được");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();
                //Toast.makeText(getContext(), "Không thêm được", Toast.LENGTH_SHORT).show();
            }
        } else {
            // Lưu đường dẫn ảnh vào cơ sở dữ liệu
            item.setSoluong(1);
            if (daogh_kh.insert(item, getContext()) > 0) {
                Context context = getContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Đã thêm");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();
                //Toast.makeText(getContext(), "Đã thêm", Toast.LENGTH_SHORT).show();
            } else {
                Context context = getContext();
                LayoutInflater inflater = getLayoutInflater();
                View customToastView = inflater.inflate(R.layout.customtoast, null);
                TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                textView.setText("Không thêm được");

                Toast customToast = new Toast(context);
                customToast.setDuration(Toast.LENGTH_SHORT);
                customToast.setView(customToastView);
                customToast.show();
                //Toast.makeText(getContext(), "Không thêm được", Toast.LENGTH_SHORT).show();
            }
        }
        LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent("update_badge"));
    }

    private void searchSanpham(String keyword) { //timkiem
        ArrayList<SanPham> searchResults = dao.searchSpByName(keyword);
        adapter.clear();
        adapter.addAll(searchResults);
        adapter.notifyDataSetChanged();
    }
}
