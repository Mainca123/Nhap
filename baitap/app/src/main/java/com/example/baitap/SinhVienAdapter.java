package com.example.baitap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Locale;

public class SinhVienAdapter extends BaseAdapter {

    private final Context context;
    private List<SinhVien> danhSach;
    private final LayoutInflater inflater;

    public SinhVienAdapter(Context context, List<SinhVien> danhSach) {
        this.context = context;
        this.danhSach = danhSach;
        this.inflater = LayoutInflater.from(context);
    }

    public void setDanhSach(List<SinhVien> danhSach) {
        this.danhSach = danhSach;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return danhSach != null ? danhSach.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return danhSach != null && position >= 0 && position < danhSach.size() ? danhSach.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    static class ViewHolder {
        TextView txtMa;
        TextView txtTen;
        TextView txtLop;
        TextView txtDiem;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_sinh_vien, parent, false);
            holder = new ViewHolder();
            holder.txtMa = convertView.findViewById(R.id.txtMa);
            holder.txtTen = convertView.findViewById(R.id.txtTen);
            holder.txtLop = convertView.findViewById(R.id.txtLop);
            holder.txtDiem = convertView.findViewById(R.id.txtDiem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SinhVien sv = (SinhVien) getItem(position);
        if (sv != null) {
            holder.txtMa.setText(sv.getMa());
            holder.txtTen.setText(sv.getHoTen());
            holder.txtLop.setText(sv.getLop());
            // Định dạng điểm gọn gàng (ví dụ: 8.5 hoặc 7.0)
            String diemStr = (sv.getDiem() == (long) sv.getDiem())
                    ? String.format(Locale.US, "%.1f", sv.getDiem())
                    : String.format(Locale.US, "%.1f", sv.getDiem());
            holder.txtDiem.setText(diemStr);
        }

        return convertView;
    }
}
