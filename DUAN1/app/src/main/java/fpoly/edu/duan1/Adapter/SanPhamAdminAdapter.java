package fpoly.edu.duan1.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import fpoly.edu.duan1.DAO.HangSpDAO;
import fpoly.edu.duan1.Fragment.GioHangFragment;
import fpoly.edu.duan1.Fragment.SanPhamAdminFragment;
import fpoly.edu.duan1.Model.HangSp;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;

public class SanPhamAdminAdapter extends ArrayAdapter<SanPham>{


    private Context context;

    SanPhamAdminFragment fragment;
    private ArrayList<SanPham> lists;

    TextView manhinh, tensp, ram, dungluong, gia, tenhang, masp;

    Button xoa, sua;
    ImageView anh;

    HangSpDAO hangSpDAO;
    GioHangFragment gioHangFragment;
    public SanPhamAdminAdapter(@NonNull Context context, SanPhamAdminFragment fragment, ArrayList<SanPham> lists) {
        super(context, 0, lists);
        this.context = context;
        this.lists = lists;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater inflater = (LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.ql_san_pham_one, null);
        }
         SanPham item = lists.get(position);
        gioHangFragment = new GioHangFragment();
        anh = v.findViewById(R.id.adanh);
        try {
            Glide.with(context)
                    .load(item.getAnh())
                    .dontAnimate()
                    .override(240,240)
                    .skipMemoryCache(false) // Cho phép sử dụng cache trong bộ nhớ
                    .diskCacheStrategy(DiskCacheStrategy.ALL) // Sử dụng cache đĩa
                    .error(R.drawable.noimg) // Add an error drawable
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            Log.e("SanPhamAdminAT", "Glide load failed: " + e.getMessage());
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(anh);
        } catch (Exception e) {
            Log.e("SanPhamAdminATLoi", "Error loading image: " + e.getMessage());
        }
        tensp = v.findViewById(R.id.adtensp);
        tensp.setText(item.getTensp());
        manhinh = v.findViewById(R.id.admanhinh);
        manhinh.setText(item.getManhinh() + " inches");
        ram = v.findViewById(R.id.adram);
        ram.setText(item.getRam() + " GB");
        dungluong = v.findViewById(R.id.addungluong);
        dungluong.setText(item.getDungluong() + " GB");
        hangSpDAO = new HangSpDAO(context);
        HangSp hangSp = hangSpDAO.getID(String.valueOf(item.getMahang()));
        tenhang = v.findViewById(R.id.adhangsp);
        tenhang.setText(hangSp.getTenloai());
        gia = v.findViewById(R.id.adgia);
        String formattedAmount =gioHangFragment.chuyendvtien(item.getGia());
        gia.setText(formattedAmount);
        xoa = v.findViewById(R.id.btnxoa);
        xoa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.xoa(String.valueOf(item.getMasp()));
            }
        });
        sua = v.findViewById(R.id.btnsua);
        sua.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment.suaSanPham(position);

            }
        });
        return v;
    }


}
