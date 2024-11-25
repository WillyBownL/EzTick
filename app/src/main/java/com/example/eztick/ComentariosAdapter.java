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

/**
 * Adaptador de RecyclerView para mostrar una lista de comentarios.
 * Cada comentario se muestra con el rol de la persona que lo hizo, el texto del comentario
 * y la hora en que se realizó el comentario, formateada.
 */
public class ComentariosAdapter extends RecyclerView.Adapter<ComentariosAdapter.ComentariosViewHolder> {

    // Lista de comentarios a mostrar en el RecyclerView.
    private final List<Comentario> listaComentarios;

    /**
     * Constructor de la clase ComentariosAdapter.
     * 
     * @param listaComentarios Lista de comentarios a mostrar en el RecyclerView.
     */
    public ComentariosAdapter(List<Comentario> listaComentarios) {
        this.listaComentarios = listaComentarios;
    }

    /**
     * Crea y devuelve un nuevo ViewHolder para un comentario.
     * Este método infla el layout del item de comentario.
     * 
     * @param parent El contenedor donde se va a agregar el item.
     * @param viewType El tipo de vista (no utilizado en este caso).
     * @return Un nuevo ViewHolder para el comentario.
     */
    @NonNull
    @Override
    public ComentariosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Infla el layout para el item de comentario.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comentario, parent, false);
        return new ComentariosViewHolder(view); // Retorna un nuevo ViewHolder con la vista inflada.
    }

    /**
     * Vincula los datos de un comentario a las vistas del ViewHolder en la posición indicada.
     * 
     * @param holder El ViewHolder que contiene las vistas a las que se deben asignar los datos.
     * @param position La posición del comentario en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull ComentariosViewHolder holder, int position) {
        // Obtiene el comentario en la posición indicada.
        Comentario comentario = listaComentarios.get(position);

        // Asigna el rol, texto y hora al ViewHolder.
        holder.textViewRol.setText(comentario.getRol());
        holder.textViewTexto.setText(comentario.getTexto());

        // Formatea el timestamp del comentario a hora y minuto (HH:mm).
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String horaEnvio = sdf.format(new Date(comentario.getTimestamp()));
        holder.textViewHora.setText(horaEnvio);
    }

    /**
     * Devuelve el número de comentarios en la lista.
     * 
     * @return El número de comentarios.
     */
    @Override
    public int getItemCount() {
        return listaComentarios.size(); // Devuelve el tamaño de la lista de comentarios.
    }

    /**
     * ViewHolder que mantiene las referencias a las vistas de cada item de comentario.
     */
    public static class ComentariosViewHolder extends RecyclerView.ViewHolder {

        // Vistas para mostrar el rol, texto y hora del comentario.
        TextView textViewRol, textViewTexto, textViewHora;

        /**
         * Constructor del ViewHolder.
         * 
         * @param itemView La vista inflada que contiene los elementos del comentario.
         */
        public ComentariosViewHolder(@NonNull View itemView) {
            super(itemView);

            // Inicializa las vistas a partir del layout.
            textViewRol = itemView.findViewById(R.id.textViewRol);
            textViewTexto = itemView.findViewById(R.id.textViewTexto);
            textViewHora = itemView.findViewById(R.id.textViewHora);
        }
    }
}