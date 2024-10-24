package com.example.eztick;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder> {

    private final List<Comentario> listaComentarios;

    public ComentariosAdapter(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    @NonNull
    @Override
    public ComentariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ComentariosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComentariosViewHolder holder, int position) {
        Comentario comentario = listaComentarios.get(position);
        holder.textViewRol.setText(comentario.getRol());
        holder.textViewTexto.setText(comentario.getTexto());

        // Formatear la marca de tiempo para mostrar la hora
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String horaEnvio = sdf.format(new Date(comentario.getTimestamp()));
        holder.textViewHora.setText(horaEnvio);
    }

    @Override
    public int getItemCount() {
        return listaComentarios.size();
    }

    public static class ComentariosViewHolder extends RecyclerView.ViewHolder {
        TextView textViewRol, textViewTexto, textViewHora;

        public ComentariosViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewRol = itemView.findViewById(R.id.textViewRol);
            textViewTexto = itemView.findViewById(R.id.textViewTexto);
            textViewHora = itemView.findViewById(R.id.textViewHora);
        }
    }
}
