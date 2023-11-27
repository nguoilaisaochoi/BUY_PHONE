package fpoly.edu.duan1.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import fpoly.edu.duan1.Fragment.GioHangFragment;
import fpoly.edu.duan1.Model.SanPham;

import fpoly.edu.duan1.Model.HangSp;

import fpoly.edu.duan1.DAO.SanPhamDAO;

import fpoly.edu.duan1.DAO.HangSpDAO;

import fpoly.edu.duan1.Fragment.SanPhamFragment;
import fpoly.edu.duan1.R;
public class SanPhamAdapter extends ArrayAdapter<SanPham> {

    private Context context;

    SanPhamFragment fragment;

    GioHangFragment gioHangFragment;
    private ArrayList<SanPham> lists;


    TextView manhinh,tensp,ram,dungluong,gia,tenhang;

    Button addgiohang;
    ImageView anh;

    SanPhamDAO sanPhamDAO;

    HangSpDAO hangSpDAO;
    public SanPhamAdapter(@NonNull Context context, SanPhamFragment fragment, ArrayList<SanPham> lists) {
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
            v = inflater.inflate(R.layout.one_san_pham, null);
        }
        final SanPham item = lists.get(position);
        gioHangFragment = new GioHangFragment();
        if (item != null) {
            anh = v.findViewById(R.id.anh);
            try {
                Glide.with(context)
                        .load(item.getAnh())
                        .dontAnimate()
                        .override(240,240)
                        .skipMemoryCache(false)
                        .error(R.drawable.noimg) // Add an error drawable
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Sử dụng cache đĩa
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                //Log.e("SanPhamAdminAT", "Glide load failed: " + e.getMessage());
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                return false;
                            }
                        })
                        .into(anh);
            } catch (Exception e) {
                //Log.e("SanPhamAdminATLoi", "Error loading image: " + e.getMessage());
            }
            tensp = v.findViewById(R.id.tensanpham);
            tensp.setText(item.getTensp());
            manhinh = v.findViewById(R.id.manhinh);
            manhinh.setText(item.getManhinh()+" inches");
            ram = v.findViewById(R.id.ram);
            ram.setText(item.getRam()+" GB");
            dungluong = v.findViewById(R.id.dungluong);
            dungluong.setText(item.getDungluong()+" GB");
            hangSpDAO = new HangSpDAO(context);
            HangSp hangSp = hangSpDAO.getID(String.valueOf(item.getMahang()));
            tenhang = v.findViewById(R.id.hangsp);
            tenhang.setText(hangSp.getTenloai());
            String formattedAmount =gioHangFragment.chuyendvtien(item.getGia());
            gia=v.findViewById(R.id.gia);
            gia.setText(formattedAmount);
            addgiohang=v.findViewById(R.id.btnaddgh);
            addgiohang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.addgh(item.getMasp(),item.getMahang());
                }
            });

        }
        return v;
    }
}
