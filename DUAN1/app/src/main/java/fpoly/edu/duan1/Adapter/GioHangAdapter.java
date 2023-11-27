package fpoly.edu.duan1.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

import fpoly.edu.duan1.DAO.GioHang_KHDAO;
import fpoly.edu.duan1.DAO.HangSpDAO;
import fpoly.edu.duan1.DAO.SanPhamDAO;
import fpoly.edu.duan1.Fragment.GioHangFragment;
import fpoly.edu.duan1.Model.GioHang;
import fpoly.edu.duan1.Model.HangSp;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;

public class GioHangAdapter extends ArrayAdapter<GioHang> {

    private Context context;

    GioHangFragment fragment;
    private ArrayList<GioHang> lists;

    TextView tengh,giagh,slgiohang,sltang,slgiam,manhinhgh,ramgh,dungluonggh,hangspgh;

    ImageView anh;

    Button huygh;
    HangSpDAO hangSpDAO;
    SanPhamDAO sanPhamDAO;

   GioHang_KHDAO gioHang_KHDAO;
    public GioHangAdapter(@NonNull Context context, GioHangFragment fragment, ArrayList<GioHang> lists) {
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
            v = inflater.inflate(R.layout.one_sp_thanh_toan, null);
        }
        final GioHang item = lists.get(position);
        sanPhamDAO = new SanPhamDAO(context);
        gioHang_KHDAO = new GioHang_KHDAO(context);
        SanPham sanPham = sanPhamDAO.getID(String.valueOf(item.getMasp()));
        if (sanPham != null) {
            anh = v.findViewById(R.id.gh_anh);
            try {
                Glide.with(context)
                        .load(sanPham.getAnh())
                        .dontAnimate()
                        .override(240,240)
                        .skipMemoryCache(false)
                        .error(R.drawable.noimg) // Add an error drawable
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Sử dụng cache đĩa
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
            tengh = v.findViewById(R.id.gh_tensp);
            tengh.setText(sanPham.getTensp());
            hangSpDAO = new HangSpDAO(context);
            HangSp hangSp = hangSpDAO.getID(String.valueOf(item.getMahang()));
            manhinhgh=v.findViewById(R.id.manhinhgh);
            manhinhgh.setText(sanPham.getManhinh()+" inches");
            ramgh=v.findViewById(R.id.ramgh);
            ramgh.setText(sanPham.getRam()+"GB");
            dungluonggh=v.findViewById(R.id.dungluonggh);
            dungluonggh.setText(sanPham.getDungluong()+"GB");
            hangspgh=v.findViewById(R.id.hangspgh);
            hangspgh.setText(hangSp.getTenloai());
            giagh = v.findViewById(R.id.gh_gia);
            String formattedAmount =fragment.chuyendvtien(sanPham.getGia());
            giagh.setText(formattedAmount);
            slgiohang = v.findViewById(R.id.gh_soluong);
            slgiohang.setText(item.getSoluong()+"");
            sltang=v.findViewById(R.id.sl_tang);
            sltang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        gioHang_KHDAO.tangsl(item.getMagh(),context);
                        fragment.capnhatlv();
                }
            });
            slgiam=v.findViewById(R.id.sl_giam);
            slgiam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gioHang_KHDAO.giamsl(item.getMagh(),context);
                    fragment.capnhatlv();
                }
            });
            huygh=v.findViewById(R.id.btnhuygh);
            huygh.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.xoa(String.valueOf(item.getMagh()));
                }
            });

        }
        return v;
    }


}
