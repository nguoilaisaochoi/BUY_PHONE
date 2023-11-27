package fpoly.edu.duan1.Fragment;

import static android.app.Activity.RESULT_OK;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.util.ArrayList;

import fpoly.edu.duan1.Adapter.HangSanPhamAdapter;
import fpoly.edu.duan1.DAO.HangSpDAO;
import fpoly.edu.duan1.Model.HangSp;
import fpoly.edu.duan1.Model.SanPham;
import fpoly.edu.duan1.R;

public class HangSanPhamFragment extends Fragment {
    ListView lvsp;

    static HangSpDAO dao;




    ArrayList<HangSp> list;

    HangSanPhamAdapter adapter;

    FloatingActionButton fab;

    EditText tvhangsp;

    TextView tvmahang;
    Button them,huy,chonanh;

    HangSp item;

    ImageView anh;

    Dialog dialog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private String imagePath;
    Uri selectedImageUri;

    SearchView searchView;

    private ActivityResultLauncher<Intent> galleryLauncher;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = getLayoutInflater().inflate(R.layout.sanphamadminfragment, container, false);
        lvsp = v.findViewById(R.id.lvsp);
        dao = new HangSpDAO(getActivity());
        fab = v.findViewById(R.id.fab);
        searchView=v.findViewById(R.id.searchViewad);
        capnhatlv();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog(getActivity(), 0);
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHang(newText);
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

    void capnhatlv() {
        list = (ArrayList<HangSp>) dao.getAll();
        adapter = new HangSanPhamAdapter(getActivity(), this, list);
        lvsp.setAdapter(adapter);
    }

    public void xoa(final String ID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Delete");
        builder.setMessage("Muốn xoá?!");
        builder.setCancelable(true);
        builder.setPositiveButton(
                "Xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dao.delete(ID);
                        capnhatlv();
                        dialog.cancel();
                    }
                }
        );
        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        builder.show();
    }

    protected void opendialog(final Context context, final int type){
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_them_hang_sanpham);
        tvmahang=dialog.findViewById(R.id.tv_hmahang);
        tvhangsp=dialog.findViewById(R.id.et_tenhang);
        huy=dialog.findViewById(R.id.btn_hhuy);
        them=dialog.findViewById(R.id.btn_hthem);
        chonanh=dialog.findViewById(R.id.btn_hanh);
        anh=dialog.findViewById(R.id.iv_hanh);

        //
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.copyFrom(dialog.getWindow().getAttributes());
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialog.getWindow().setAttributes(layoutParams);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setAttributes(layoutParams);
        //
        huy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        if (type != 0) {
            tvmahang.setText(String.valueOf(item.getMahang()));
            tvmahang.setVisibility(View.GONE);//mahang bị ẩn
            tvhangsp.setText(item.getTenloai());
            //anh.setImageURI(item.getAnh());
            if (item != null) {
                try {
                    Glide.with(context)
                            .load(item.getAnh())
                            .dontAnimate()
                            .override(240, 240)
                            .skipMemoryCache(false)
                            .error(R.drawable.baseline_image_24) // Add an error drawable
                            .diskCacheStrategy(DiskCacheStrategy.ALL) // Sử dụng cache đĩa
                            .listener(new RequestListener<Drawable>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                    return false;
                                }
                            })
                            .into(anh);
                } catch (Exception e) {
                }
            }
        }
        them.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                item = new HangSp();
                item.setTenloai(String.valueOf(tvhangsp.getText()));
                item.setAnh(selectedImageUri);
                // Lưu đường dẫn ảnh vào cơ sở dữ liệu
                if (type == 0) {
                    if (dao.insert(item) > 0) {
                        Context context = getContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Đã thêm");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();
                        //Toast.makeText(context, "Đã thêm", Toast.LENGTH_SHORT).show();
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
                        //Toast.makeText(context, "Không thêm được", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    item.setMahang(Integer.parseInt(tvmahang.getText().toString()));
                    if (dao.update(item) > 0) {
                        Context context = getContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Đã sửa");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();
                        //Toast.makeText(context, "Đã sửa", Toast.LENGTH_SHORT).show();
                    } else {
                        Context context = getContext();
                        LayoutInflater inflater = getLayoutInflater();
                        View customToastView = inflater.inflate(R.layout.customtoast, null);
                        TextView textView = customToastView.findViewById(R.id.custom_toast_message);
                        textView.setText("Không sửa được");

                        Toast customToast = new Toast(context);
                        customToast.setDuration(Toast.LENGTH_SHORT);
                        customToast.setView(customToastView);
                        customToast.show();
                        //Toast.makeText(context, "Không sửa được", Toast.LENGTH_SHORT).show();
                    }
                }
                capnhatlv();
                dialog.dismiss();
            }
        });
        dialog.show();
        chonanh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
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
                Glide.with(requireContext()).clear(anh);
                // Đặt hình ảnh vào ImageView
                anh.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getActivity(), "Không thể lấy dữ liệu ảnh", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getActivity(), "Không thể lấy đường dẫn ảnh", Toast.LENGTH_SHORT).show();
        }
    }
    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = requireContext().getContentResolver().query(contentUri, projection, null, null, null);

        if (cursor == null) {
            return contentUri.getPath(); // Path directly from URI
        }

        cursor.moveToFirst();
        int columnIndex = cursor.getColumnIndex(projection[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    public boolean suaHangSanPham(int position) {
        item = list.get(position);
        opendialog(getActivity(), 1); // 1 update
        return false;
    }

    private void searchHang(String keyword) { //timkiem
        ArrayList<HangSp> searchResults = dao.searchSpByName(keyword);
        adapter.clear();
        adapter.addAll(searchResults);
        adapter.notifyDataSetChanged();
    }
}
