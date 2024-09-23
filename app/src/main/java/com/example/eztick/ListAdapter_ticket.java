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

    public ListAdapter_ticket(List<ListElementTicket> itemList, Context context){
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount()   { return mData.size();}

    @Override
    public ListAdapter_ticket.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = mInflater.inflate(R.layout.ticket_element,null);
        return new ListAdapter_ticket.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ListAdapter_ticket.ViewHolder holder,final int position){
        holder.bindData(mData.get(position));
    }
    public void setItems(List<ListElementTicket> items) {  mData = items; }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        private TextView titulo,descripcion,fecha,lvlPeligro;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.tituloTV);
            descripcion = itemView.findViewById(R.id.descripcionTV);
            fecha = itemView.findViewById(R.id.fechaTV);
            lvlPeligro = itemView.findViewById(R.id.lvlPeligroTV);
        }

        void bindData(final ListElementTicket item){
            titulo.setText(item.getTitulo());
            descripcion.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            lvlPeligro.setText(item.getLvlPeligro());
        }
    }
}
