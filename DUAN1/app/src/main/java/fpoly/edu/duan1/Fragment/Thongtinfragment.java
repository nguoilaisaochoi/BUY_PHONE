package fpoly.edu.duan1.Fragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.io.IOException;

import fpoly.edu.duan1.DAO.KhachHangDAO;
import fpoly.edu.duan1.Model.KhachHang;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;

public class Thongtinfragment extends Fragment {

    static KhachHangDAO dao;

    EditText etten, etsdt, etdiachi;

    TextView tvdiachi, tvsdt;

    Button btndoianh, btnluu;

    ImageView avt;

    KhachHang item;

    SharedPreferences nav_name;
    Uri selectedImageUri;
    private ActivityResultLauncher<Intent> galleryLauncher;
    private static final String PREF_ROLE_KEY = "user_role";

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.thong_tin_fragment, container, false);
        nav_name = getActivity().getSharedPreferences("name_Nav", MODE_PRIVATE);
        String id = nav_name.getString("makh", "");
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String userRole = sharedPreferences.getString(PREF_ROLE_KEY, "");
        dao = new KhachHangDAO(getContext());
        item = dao.getID(id);
        etten = v.findViewById(R.id.etten);
        etten.setText(item.getHoTen());
        tvsdt = v.findViewById(R.id.tv_tt_sdt);
        etsdt = v.findViewById(R.id.etsdt);
        etsdt.setText(String.valueOf(item.getSdt()));
        tvdiachi = v.findViewById(R.id.tv_tt_diachi);
        etdiachi = v.findViewById(R.id.etdiachi);
        etdiachi.setText(item.getDiachi());
        avt = v.findViewById(R.id.imgavt);
        if (userRole.equals("admin")) {
            etsdt.setVisibility(View.GONE);
            etdiachi.setVisibility(View.GONE);
            tvdiachi.setVisibility(View.GONE);
            tvsdt.setVisibility(View.GONE);
        }
        if (item != null) {
            try {
                Glide.with(getContext())
                        .load(item.getAvt())
                        .dontAnimate()
                        .override(290, 290)
                        .skipMemoryCache(false)
                        .error(R.drawable.man) // Add an error drawable
                        .diskCacheStrategy(DiskCacheStrategy.ALL) // Sử dụng cache đĩa
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                selectedImageUri = null;
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                selectedImageUri = Uri.parse(item.getAvt().toString());
                                return false;
                            }
                        })
                        .into(avt);
            } catch (Exception e) {
            }
        }
        btndoianh = v.findViewById(R.id.btndoianh);
        btndoianh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnluu = v.findViewById(R.id.btnluutt);
        btnluu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (valiable() > 0) {
                    item = new KhachHang();
                    item.setMakh(id);
                    item.setHoTen(etten.getText().toString());
                    item.setSdt(etsdt.getText().toString());
                    item.setDiachi(etdiachi.getText().toString());
                    item.setAvt(selectedImageUri);
                    if (dao.updateInfo(item) > 0) {
                        Intent intent = new Intent("update_user_info");
                        intent.putExtra("new_name", etten.getText().toString());
                        if (selectedImageUri != null) {
                            intent.putExtra("new_avatar", selectedImageUri.toString());
                        } else {
                        }
                        LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent);
                        Context context = getContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Đổi thành công");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();

                        //Toast.makeText(getContext(),"Đổi thành công", Toast.LENGTH_SHORT).show();
                    } else {
                    }
                }
            }

        });
        galleryLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        Intent data = result.getData();
                        if (data != null) {
                            Uri imageUri = data.getData();
                            handleImageResult(imageUri);
                        }
                    }
                });
        return v;
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");

        // Sử dụng ActivityResultLauncher để khởi động hoạt động thư viện
        galleryLauncher.launch(intent);
    }

    private void handleImageResult(Uri imageUri) {
        if (imageUri != null) {
            selectedImageUri = imageUri;
            try {
                // Sử dụng ContentResolver để lấy dữ liệu hình ảnh
                ContentResolver resolver = getActivity().getContentResolver();
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, imageUri);

                // Đặt hình ảnh vào ImageView
                Glide.with(requireContext()).clear(avt);
                avt.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Không thể lấy dữ liệu ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Không thể lấy đường dẫn ảnh", Toast.LENGTH_SHORT).show();
        }
    }

    public int valiable() {
        int check = 1;
        if (etten.getText().length() == 0 || etsdt.getText().length() == 0 || etdiachi.getText().length() == 0) {
            Context context = getContext();
            LayoutInflater inflater = getLayoutInflater();
            View customToastView = inflater.inflate(R.layout.customtoast, null);
            TextView textView = customToastView.findViewById(R.id.custom_toast_message);
            textView.setText("Không bỏ trống");

            Toast customToast = new Toast(context);
            customToast.setDuration(Toast.LENGTH_SHORT);
            customToast.setView(customToastView);
            customToast.show();
            check = -1;
        }
        return check;
    }
}
