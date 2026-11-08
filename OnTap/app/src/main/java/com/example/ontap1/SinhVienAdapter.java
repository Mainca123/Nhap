package com.example.ontap1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class SinhVienAdapter extends ArrayAdapter<SinhVien> {

    private final LayoutInflater inflater;

    public SinhVienAdapter(Context context, List<SinhVien> danhSach) {
        super(context, 0, danhSach);
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sinh_vien, parent, false);
            holder = new ViewHolder();
            holder.txtMaSV = convertView.findViewById(R.id.textMSV);
            holder.txtHoTen = convertView.findViewById(R.id.textHoTen);
            holder.txtEmail = convertView.findViewById(R.id.textEmail);
            holder.txtLop = convertView.findViewById(R.id.textLop);
            holder.txtTrangThai = convertView.findViewById(R.id.textTrangThai);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = getItem(position);
        if (sv != null) {
            holder.txtMaSV.setText(sv.getMaSV());
            holder.txtHoTen.setText(sv.getHoTen());
            holder.txtEmail.setText(sv.getEmail());
            holder.txtLop.setText(sv.getLop());
            holder.txtTrangThai.setText(sv.getTrangThai());
        }

        return convertView;
    }

    private static class ViewHolder {
        TextView txtMaSV;
        TextView txtHoTen;
        TextView txtEmail;
        TextView txtLop;
        TextView txtTrangThai;
    }
}
