package com.kreatidea.transmobile.home;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.kreatidea.transmobile.R;
import com.kreatidea.transmobile.model.Pemakaian;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Dennis on 10/19/2018.
 */
public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(Pemakaian item);
    }

    private static final String TAG = "HomeAdapter";

    private List<Pemakaian> pemakaian;
    private OnItemClickListener listener;

    /* Constructor */
    public HomeAdapter(List<Pemakaian> pemakaian, OnItemClickListener listener) {
        this.pemakaian = pemakaian;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_home, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.bindItem(pemakaian.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return pemakaian.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.pemakaian_list_item_layout) RelativeLayout mPemakaianListItemLayout;
        @BindView(R.id.tujuan_text_view) TextView tujuanTextView;
        @BindView(R.id.nopol_text_view) TextView nopolTextView;
        @BindView(R.id.tgl_pemakaian_text_view) TextView tglPemakaianTextView;
        @BindView(R.id.status_pemakaian_text_view) TextView statusPemakaianTextView;

        private ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bindItem(final Pemakaian pemakaian, final OnItemClickListener listener) {
            tujuanTextView.setText(pemakaian.getTujuan());
            nopolTextView.setText(pemakaian.getNopol());
            tglPemakaianTextView.setText(pemakaian.getTglPemakaian());
            statusPemakaianTextView.setText(pemakaian.getStatusPemakaian());

            /* Set warna dan status pemakaian kendaraan */
            if (pemakaian.getStatusPemakaian().equals("finish"))
                statusPemakaianTextView.setBackgroundResource(R.color.holo_green_light);
            else
                statusPemakaianTextView.setBackgroundResource(R.color.holo_blue_bright);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(pemakaian);
                }
            });
        }
    }
}
