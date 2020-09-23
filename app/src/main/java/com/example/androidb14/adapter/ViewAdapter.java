package com.example.androidb14.adapter;

import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.androidb14.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewAdapter extends RecyclerView.Adapter<ViewAdapter.HolderVIew> {

    private File[] files;
    private LayoutInflater layoutInflater;
    private OnClickItemListener listener;

    public ViewAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    public void setFiles(File[] files) {
        this.files = files;
        notifyDataSetChanged();
    }

    public void setListener(OnClickItemListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public HolderVIew onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_view, parent, false);
        return new HolderVIew(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderVIew holder, int position) {
        final File file = files[position];
        holder.bindView(file);
        if (listener != null) {
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listener.onClickItem(file);
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return files.length;
    }

    public class HolderVIew extends RecyclerView.ViewHolder {

        private TextView tvName, tvTime, tvSize;
        private ImageView ivIcon;

        public HolderVIew(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvTime = itemView.findViewById(R.id.tv_time_up);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvSize = itemView.findViewById(R.id.tv_size);
        }

        private void bindView(File file) {
            Date date = new Date(file.lastModified());
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
            tvName.setText(file.getName());
            tvTime.setText(dateFormat.format(date));
            if (file.isDirectory()) {
                Glide.with(itemView).load(R.drawable.ic_folder).into(ivIcon);
            } else {
                tvSize.setText(Formatter.formatFileSize(tvName.getContext(), file.length()));
                Glide.with(itemView).load(R.drawable.ic_file).into(ivIcon);
            }
        }
    }

    public interface OnClickItemListener {
        void onClickItem(File file);
    }

}
