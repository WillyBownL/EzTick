package com.example.eztick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ListAdapter_ticket extends RecyclerView.Adapter<ListAdapter_ticket.ViewHolder> {
    private List<ListElementTicket> mData;
    private LayoutInflater mInflater;
    private Context context;
    private OnTicketClickListener onTicketClickListener;

    public ListAdapter_ticket(List<ListElementTicket> itemList, Context context, OnTicketClickListener onTicketClickListener) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.onTicketClickListener = onTicketClickListener;
    }

    public interface OnTicketClickListener {
        void onTicketClick(ListElementTicket ticket);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    @Override
    public ListAdapter_ticket.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.ticket_element, null);
        return new ViewHolder(view, onTicketClickListener);
    }

    @Override
    public void onBindViewHolder(final ListAdapter_ticket.ViewHolder holder, final int position) {
        holder.bindData(mData.get(position));
    }

    public void setItems(List<ListElementTicket> items) {
        mData = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo, descripcion, fecha, lvlPeligro;

        ViewHolder(View itemView, OnTicketClickListener listener) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloTV);
            descripcion = itemView.findViewById(R.id.descripcionTV);
            fecha = itemView.findViewById(R.id.fechaTV);
            lvlPeligro = itemView.findViewById(R.id.lvlPeligroTV);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onTicketClick((ListElementTicket) itemView.getTag());
                }
            });
        }

        void bindData(final ListElementTicket item) {
            titulo.setText(item.getTitulo());
            descripcion.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            lvlPeligro.setText(item.getLvlPeligro());
            itemView.setTag(item);
        }
    }
}