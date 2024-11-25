package com.example.eztick;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

/**
 * Fragmento que muestra los detalles de un ticket, permitiendo a los usuarios ver la información
 * y tomar acciones como marcar el ticket como resuelto o agregar comentarios.
 */
public class DetallesTicketFragment extends Fragment {

    private TextView ticketTituloDetail, ticketDescripcionDetail, ticketFechaDetail, ticketPeligroDetail;
    private Button btnComentarios; // Botón para abrir los comentarios del ticket
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String ticketId;

    /**
     * Método que se llama cuando el fragmento es creado.
     * Configura la vista del fragmento, inicializa las vistas y carga los datos del ticket.
     *
     * @param inflater Inflador de la vista.
     * @param container Contenedor donde el fragmento debe ser insertado.
     * @param savedInstanceState Estado guardado del fragmento (si existe).
     * @return La vista del fragmento.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar la vista del fragmento
        View view = inflater.inflate(R.layout.fragment_detalles_ticket, container, false);

        // Habilitar el menú en este fragmento
        setHasOptionsMenu(true);

        // Inicializar las vistas
        ticketTituloDetail = view.findViewById(R.id.ticketTituloDetail);
        ticketDescripcionDetail = view.findViewById(R.id.ticketDescripcionDetail);
        ticketFechaDetail = view.findViewById(R.id.ticketFechaDetail);
        ticketPeligroDetail = view.findViewById(R.id.ticketPeligroDetail);
        btnComentarios = view.findViewById(R.id.btnComentarios);

        // Obtener el ID del ticket pasado desde el argumento del fragmento
        if (getArguments() != null) {
            ticketId = getArguments().getString("ticket_id");
            cargarDetallesTicket(ticketId); // Cargar los detalles del ticket
        }

        // Configurar el botón para abrir el fragmento de comentarios
        btnComentarios.setOnClickListener(v -> abrirComentarios());

        return view;
    }

    /**
     * Carga los detalles del ticket desde Firestore usando el ID del ticket.
     *
     * @param ticketId El ID del ticket a cargar.
     */
    private void cargarDetallesTicket(String ticketId) {
        db.collection("tickets").document(ticketId).get().addOnSuccessListener(documentSnapshot -> {
            // Si el ticket existe en la base de datos, cargar sus detalles
            if (documentSnapshot.exists()) {
                ListElementTicket ticket = documentSnapshot.toObject(ListElementTicket.class);
                if (ticket != null) {
                    ticketTituloDetail.setText(ticket.getTitulo());
                    ticketDescripcionDetail.setText(ticket.getDescripcion());
                    ticketFechaDetail.setText(ticket.getFecha());
                    ticketPeligroDetail.setText(ticket.getLvlPeligro());
                }
            }
        }).addOnFailureListener(e -> {
            // Mostrar un mensaje de error si ocurre un fallo al cargar los datos
            Toast.makeText(getContext(), "Error al cargar los detalles del ticket", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Abre el fragmento de comentarios, pasando el ID del ticket para asociar los comentarios.
     */
    private void abrirComentarios() {
        // Crear un nuevo fragmento de comentarios
        ComentariosFragment comentariosFragment = new ComentariosFragment();
        Bundle bundle = new Bundle();
        bundle.putString("ticket_id", ticketId); // Pasar el ID del ticket al fragmento
        comentariosFragment.setArguments(bundle);

        // Reemplazar el fragmento actual por el de comentarios
        requireActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, comentariosFragment) // Reemplazar el fragmento
                .addToBackStack(null) // Agregar a la pila de retroceso
                .commit();
    }

    /**
     * Inflar el menú de opciones basado en el rol del usuario.
     *
     * @param menu El menú que se va a inflar.
     * @param inflater Inflador del menú.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Limpiar el menú actual antes de inflar uno nuevo
        menu.clear();

        // Obtener el usuario actual desde SharedPreferences
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String usuario = datos.getString("usuario", "");

        // Inflar el menú basado en el rol del usuario
        if (usuario.equals("MANTENIMIENTO")) {
            inflater.inflate(R.menu.menu_solo_volver, menu); // Menú con solo opción de volver
        } else if (usuario.equals("RRHH")) {
            inflater.inflate(R.menu.menu_detalles_ticket, menu); // Menú con opciones para finalizar ticket
        }
    }

    /**
     * Maneja la selección de los ítems del menú.
     * Permite al usuario finalizar un ticket o volver al fragmento anterior.
     *
     * @param item El ítem del menú que se ha seleccionado.
     * @return true si el ítem fue manejado correctamente.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Si el usuario selecciona "finalizar", marcar el ticket como resuelto
        if (id == R.id.finalizar) {
            eliminarTicket(ticketId);
            return true;
        } else if (id == R.id.atras) {
            // Si selecciona "volver", regresar al fragmento anterior
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Marca el ticket como resuelto y lo actualiza en la base de datos.
     *
     * @param ticketId El ID del ticket que se va a marcar como resuelto.
     */
    private void eliminarTicket(String ticketId) {
        // Crear un mapa con los campos a actualizar en la base de datos
        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("estado", "resuelto"); // Marcar el estado como "resuelto"
        updateFields.put("fecha_resolucion", System.currentTimeMillis()); // Establecer la fecha de resolución

        // Actualizar el ticket en la base de datos
        db.collection("tickets").document(ticketId).update(updateFields).addOnSuccessListener(aVoid -> {
            // Mostrar un mensaje de éxito
            Toast.makeText(getContext(), "Ticket marcado como resuelto", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack(); // Volver al fragmento anterior
        }).addOnFailureListener(e -> {
            // Mostrar un mensaje de error en caso de fallo
            Toast.makeText(getContext(), "Error al marcar el ticket como resuelto", Toast.LENGTH_SHORT).show();
        });
    }

}
