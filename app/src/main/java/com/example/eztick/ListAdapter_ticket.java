package com.example.eztick;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * Adaptador para manejar y mostrar una lista de tickets en un RecyclerView.
 * Permite vincular datos de cada ticket con sus vistas correspondientes y manejar clics en los elementos.
 */
public class ListAdapter_ticket extends RecyclerView.Adapter<ListAdapter_ticket.ViewHolder> {

    // Lista de elementos de tipo ListElementTicket que contienen los datos de los tickets.
    private List<ListElementTicket> mData;
    
    // Inflador de vistas para crear las vistas de los elementos de la lista.
    private LayoutInflater mInflater;
    
    // Contexto de la actividad o aplicación que usa el adaptador.
    private Context context;
    
    // Listener para manejar los clics en los tickets.
    private OnTicketClickListener onTicketClickListener;

    /**
     * Constructor de la clase.
     * Inicializa el adaptador con la lista de tickets, el contexto y el listener de clics.
     * 
     * @param itemList Lista de elementos ListElementTicket a mostrar.
     * @param context Contexto de la aplicación o actividad.
     * @param onTicketClickListener Listener para manejar clics en los tickets.
     */
    public ListAdapter_ticket(List<ListElementTicket> itemList, Context context, OnTicketClickListener onTicketClickListener) {
        this.mInflater = LayoutInflater.from(context); // Inicializa el inflador con el contexto.
        this.context = context;
        this.mData = itemList;
        this.onTicketClickListener = onTicketClickListener;
    }

    /**
     * Interfaz para manejar los clics en los tickets de la lista.
     */
    public interface OnTicketClickListener {
        void onTicketClick(ListElementTicket ticket); // Método llamado cuando un ticket es clickeado.
    }

    /**
     * Devuelve el número de elementos en la lista de tickets.
     * 
     * @return El número de elementos en mData.
     */
    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0; // Si mData es nulo, devuelve 0.
    }

    /**
     * Crea y devuelve un nuevo ViewHolder para un elemento de la lista.
     * 
     * @param parent El contenedor donde se va a añadir la vista.
     * @param viewType El tipo de vista (usualmente no utilizado en este caso).
     * @return Un ViewHolder con la vista inflada.
     */
    @Override
    public ListAdapter_ticket.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Infla la vista para cada elemento de la lista.
        View view = mInflater.inflate(R.layout.ticket_element, null);
        return new ViewHolder(view, onTicketClickListener); // Retorna un nuevo ViewHolder con la vista inflada.
    }

    /**
     * Vincula los datos del ticket con el ViewHolder en la posición dada.
     * 
     * @param holder El ViewHolder que contiene las vistas a las que se deben asignar los datos.
     * @param position La posición en la lista de datos.
     */
    @Override
    public void onBindViewHolder(final ListAdapter_ticket.ViewHolder holder, final int position) {
        // Llama al método bindData() para asignar los datos del ticket a las vistas del ViewHolder.
        holder.bindData(mData.get(position));
    }

    /**
     * Actualiza la lista de tickets con nuevos elementos.
     * 
     * @param items Lista de nuevos elementos ListElementTicket.
     */
    public void setItems(List<ListElementTicket> items) {
        mData = items; // Actualiza la lista de datos.
    }

    /**
     * Clase ViewHolder que mantiene las referencias a las vistas de cada elemento de la lista.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView titulo, descripcion, fecha, lvlPeligro;

        /**
         * Constructor del ViewHolder.
         * 
         * @param itemView La vista inflada que contiene el diseño de un ticket.
         * @param listener El listener para manejar los clics en el item.
         */
        ViewHolder(View itemView, OnTicketClickListener listener) {
            super(itemView);

            // Inicializa las vistas a partir del layout inflado.
            titulo = itemView.findViewById(R.id.tituloTV);
            descripcion = itemView.findViewById(R.id.descripcionTV);
            fecha = itemView.findViewById(R.id.fechaTV);
            lvlPeligro = itemView.findViewById(R.id.lvlPeligroTV);

            // Configura el listener para manejar clics en el elemento de la lista.
            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition(); // Obtiene la posición del item clickeado.
                if (position != RecyclerView.NO_POSITION) {
                    // Si la posición es válida, llama al método onTicketClick() con el ticket correspondiente.
                    listener.onTicketClick((ListElementTicket) itemView.getTag());
                }
            });
        }

        /**
         * Vincula los datos del ticket a las vistas correspondientes.
         * 
         * @param item El objeto ListElementTicket que contiene los datos del ticket.
         */
        void bindData(final ListElementTicket item) {
            // Asocia los datos del ticket con las vistas correspondientes.
            titulo.setText(item.getTitulo());
            descripcion.setText(item.getDescripcion());
            fecha.setText(item.getFecha());
            lvlPeligro.setText(item.getLvlPeligro());

            // Establece el item como la etiqueta del itemView para poder acceder a él en el listener.
            itemView.setTag(item);
        }
    }
}
