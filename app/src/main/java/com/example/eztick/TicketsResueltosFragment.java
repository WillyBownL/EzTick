package com.example.eztick;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragmento que muestra una lista de tickets resueltos en un RecyclerView.
 * Este fragmento se conecta a Firestore para obtener los tickets que tienen
 * el estado "resuelto" y los muestra en una lista ordenada por fecha de resolución.
 */
public class TicketsResueltosFragment extends Fragment {

    private static final String TAG = "TicketsResueltosFragment"; // Etiqueta para el log
    private RecyclerView recyclerView;
    private ListAdapter_ticket ticketAdapter;
    private List<ListElementTicket> listaTicketsResueltos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance(); // Instancia de Firestore
    private CollectionReference ticketsRef = db.collection("tickets"); // Referencia a la colección de tickets

    /**
     * Método que crea la vista del fragmento.
     * Infla el layout y configura el RecyclerView para mostrar los tickets resueltos.
     * 
     * @param inflater Inflador para inflar el layout del fragmento.
     * @param container Contenedor del fragmento.
     * @param savedInstanceState Estado guardado si existe.
     * @return Vista inflada con el layout del fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflar el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_tickets_resueltos, container, false);

        // Configuración del RecyclerView para mostrar los tickets
        recyclerView = view.findViewById(R.id.recyclerViewResueltos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        // Inicializar la lista de tickets resueltos
        listaTicketsResueltos = new ArrayList<>();
        ticketAdapter = new ListAdapter_ticket(listaTicketsResueltos, requireContext(), null);
        recyclerView.setAdapter(ticketAdapter);

        // Cargar los tickets resueltos desde Firestore
        cargarTicketsResueltos();

        // Habilitar el menú de opciones en el fragmento
        setHasOptionsMenu(true);

        return view;
    }

    /**
     * Método que carga los tickets resueltos desde Firestore.
     * Los tickets son filtrados por el estado "resuelto" y ordenados por fecha de resolución.
     */
    private void cargarTicketsResueltos() {
        // Consultar los tickets con estado "resuelto" y ordenarlos por la fecha de resolución
        ticketsRef.whereEqualTo("estado", "resuelto")
                .orderBy("fecha_resolucion", Query.Direction.DESCENDING) // Ordenar de más reciente a más antiguo
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    // Limpiar la lista de tickets resueltos antes de agregar nuevos
                    listaTicketsResueltos.clear();

                    // Verificar si se encontraron documentos
                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "No se encontraron tickets resueltos.");
                        Toast.makeText(getContext(), "No hay tickets resueltos", Toast.LENGTH_SHORT).show();
                    } else {
                        // Añadir los tickets resueltos a la lista
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ListElementTicket ticket = document.toObject(ListElementTicket.class);
                            if (ticket != null) {
                                listaTicketsResueltos.add(ticket);
                                Log.d(TAG, "Ticket resuelto agregado: " + ticket.getTitulo());
                            }
                        }
                        // Notificar al adaptador que se actualizó la lista de tickets
                        ticketAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Total de tickets resueltos cargados: " + listaTicketsResueltos.size());
                    }
                })
                .addOnFailureListener(e -> {
                    // Manejo de errores al intentar cargar los tickets
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error al cargar tickets resueltos", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al cargar tickets resueltos: ", e);
                    }
                });
    }

    /**
     * Método que crea el menú de opciones para el fragmento.
     * En este caso, solo se inflará el menú con la opción para volver al fragmento anterior.
     * 
     * @param menu El menú que se va a crear.
     * @param inflater Inflador que ayuda a inflar el menú.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // Limpiar el menú actual
        inflater.inflate(R.menu.menu_solo_volver, menu); // Inflar el menú con la opción de volver
    }

    /**
     * Método que maneja las interacciones con los elementos del menú.
     * En este caso, maneja la opción para volver al fragmento anterior.
     * 
     * @param item El ítem del menú que fue seleccionado.
     * @return True si la acción se maneja correctamente, false de lo contrario.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.atras) {
            // Volver al fragmento anterior en la pila de fragmentos
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
