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

import fpoly.edu.duan1.DAO.HangSpDAO;
import fpoly.edu.duan1.Fragment.HangSanPhamFragment;
import fpoly.edu.duan1.Model.HangSp;
import fpoly.edu.duan1.R;

public class HangSanPhamAdapter extends ArrayAdapter<HangSp> {

    private Context context;

    HangSanPhamFragment fragment;
    private ArrayList<HangSp> lists;

    TextView mahang,tenhang;

    Button sua;
    ImageView anh;


    HangSpDAO hangSpDAO;
    public HangSanPhamAdapter(@NonNull Context context, HangSanPhamFragment fragment, ArrayList<HangSp> lists) {
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
            v = inflater.inflate(R.layout.loai_sanpham_1, null);
        }
        final HangSp item = lists.get(position);
        if (item != null) {
            anh = v.findViewById(R.id.anhloai);
            try {
                Glide.with(context)
                        .load(item.getAnh())
                        .dontAnimate()
                        .override(290,290)
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
            mahang = v.findViewById(R.id.mahang);
            mahang.setText("Mã hãng: "+item.getMahang());
            tenhang = v.findViewById(R.id.tenhang);
            tenhang.setText("Tên: "+item.getTenloai());
            Log.d("HangSanPhamAdapter", "TenLoai value: " + item.getTenloai());
            sua =v.findViewById(R.id.btn_sua);
            sua.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fragment.suaHangSanPham(position);
                }
            });

        }
        return v;
    }
}
