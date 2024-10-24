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

public class TicketsResueltosFragment extends Fragment {

    private static final String TAG = "TicketsResueltosFragment";
    private RecyclerView recyclerView;
    private ListAdapter_ticket ticketAdapter;
    private List<ListElementTicket> listaTicketsResueltos;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ticketsRef = db.collection("tickets");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tickets_resueltos, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewResueltos);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        listaTicketsResueltos = new ArrayList<>();
        ticketAdapter = new ListAdapter_ticket(listaTicketsResueltos, requireContext(), null);
        recyclerView.setAdapter(ticketAdapter);

        cargarTicketsResueltos();

        // Habilitar el menú en el fragmento
        setHasOptionsMenu(true);

        return view;
    }

    private void cargarTicketsResueltos() {
        ticketsRef.whereEqualTo("estado", "resuelto")
                .orderBy("fecha_resolucion", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    listaTicketsResueltos.clear();

                    if (queryDocumentSnapshots.isEmpty()) {
                        Log.d(TAG, "No se encontraron tickets resueltos.");
                        Toast.makeText(getContext(), "No hay tickets resueltos", Toast.LENGTH_SHORT).show();
                    } else {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            ListElementTicket ticket = document.toObject(ListElementTicket.class);

                            if (ticket != null) {
                                listaTicketsResueltos.add(ticket);
                                Log.d(TAG, "Ticket resuelto agregado: " + ticket.getTitulo());
                            }
                        }
                        ticketAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Total de tickets resueltos cargados: " + listaTicketsResueltos.size());
                    }
                })
                .addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error al cargar tickets resueltos", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error al cargar tickets resueltos: ", e);
                    }
                });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // Limpiar el menú actual
        inflater.inflate(R.menu.menu_solo_volver, menu); // Inflar el menú
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.atras) {
            requireActivity().getSupportFragmentManager().popBackStack(); // Volver al fragmento anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
