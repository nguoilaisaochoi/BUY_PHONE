package fpoly.edu.duan1.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import fpoly.edu.duan1.Model.HangSp;
import fpoly.edu.duan1.R;

import java.util.ArrayList;

public class HangspSpinnerAdapter extends ArrayAdapter<HangSp> {
    Context context;
    ArrayList<HangSp> lists;
    TextView tvmahang, tvtenhang;
    public HangspSpinnerAdapter(@NonNull Context context, ArrayList<HangSp> lists) {
        super(context, 0,lists);
        this.context = context;
        this.lists = lists;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if(v==null){
            LayoutInflater inflater=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.hangsp_item_spinner,null);
        }
        final HangSp item= lists.get(position);
        if(item!=null){
            tvtenhang =v.findViewById(R.id.tvtenhang);
            tvtenhang.setText(item.getTenloai());
        }
        return v;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v=convertView;
        if(v==null){
            LayoutInflater inflater=(LayoutInflater)
                    context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v=inflater.inflate(R.layout.hangsp_item_spinner,null);
        }
        final HangSp item= lists.get(position);
        if(item!=null){
            tvtenhang =v.findViewById(R.id.tvtenhang);
            tvtenhang.setText(item.getTenloai());
        }
        return  v;

    }
}
