package com.example.eztick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class TicketListFragment extends Fragment implements ListAdapter_ticket.OnTicketClickListener {
    private RecyclerView recyclerView;
    private ListAdapter_ticket ticketAdapter;
    private List<ListElementTicket> listaTickets;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference tickets = db.collection("tickets");

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true); // Permitir que el fragmento maneje el menú
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        listaTickets = new ArrayList<>();
        ticketAdapter = new ListAdapter_ticket(listaTickets, getContext(), this);
        recyclerView.setAdapter(ticketAdapter);

        cargarTickets();

        return view;
    }

    private void cargarTickets() {
        // Filtrar los tickets no resueltos directamente en Firestore
        tickets.whereEqualTo("estado", "pendiente")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (listaTickets != null) {
                        listaTickets.clear();
                    }

                    // Añadir los tickets con estado "pendiente"
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        ListElementTicket ticket = documentSnapshot.toObject(ListElementTicket.class);
                        if (ticket != null) {
                            ticket.setId(documentSnapshot.getId());
                            listaTickets.add(ticket);
                        }
                    }

                    // Realizar otra consulta para tickets sin campo "estado"
                    tickets.whereEqualTo("estado", null)
                            .get()
                            .addOnSuccessListener(queryDocumentSnapshots2 -> {
                                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots2) {
                                    ListElementTicket ticket = documentSnapshot.toObject(ListElementTicket.class);
                                    if (ticket != null) {
                                        ticket.setId(documentSnapshot.getId());
                                        listaTickets.add(ticket);
                                    }
                                }

                                // Ordenar por nivel de peligro
                                if (!listaTickets.isEmpty()) {
                                    listaTickets.sort((ticket1, ticket2) -> {
                                        int lvlPeligro1 = Integer.parseInt(ticket1.getLvlPeligro());
                                        int lvlPeligro2 = Integer.parseInt(ticket2.getLvlPeligro());
                                        return Integer.compare(lvlPeligro2, lvlPeligro1);
                                    });
                                }

                                // Notificar al adaptador para actualizar la vista
                                ticketAdapter.notifyDataSetChanged();

                                if (listaTickets.isEmpty()) {
                                    Log.d("TicketListFragment", "No se encontraron tickets pendientes.");
                                    Toast.makeText(getContext(), "No hay tickets pendientes", Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d("TicketListFragment", "Total de tickets pendientes cargados: " + listaTickets.size());
                                }
                            })
                            .addOnFailureListener(e -> {
                                Log.e("FirestoreError", "Error al cargar tickets sin estado: " + e.getMessage());
                            });
                })
                .addOnFailureListener(e -> {
                    Log.e("FirestoreError", "Error al cargar tickets pendientes: " + e.getMessage());
                });
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        // Limpiar el menú actual
        menu.clear();

        // Obtener el usuario actual de SharedPreferences
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String usuario = datos.getString("usuario", "");

        // Inflar el menú basado en el usuario
        if (usuario.equals("MANTENIMIENTO")) {
            inflater.inflate(R.menu.menu_mantenimiento, menu);
        } else if (usuario.equals("RRHH")) {
            inflater.inflate(R.menu.menu_rrhh, menu);
        }
    }

    @Override
    public void onTicketClick(ListElementTicket ticket) {
        Log.d("TicketClick", "Ticket seleccionado: " + ticket.getTitulo());

        // Limpiar el menú de la actividad antes de navegar
        requireActivity().invalidateOptionsMenu();

        DetallesTicketFragment detallesFragment = new DetallesTicketFragment();

        // Crear un Bundle para pasar los datos del ticket
        Bundle bundle = new Bundle();
        bundle.putString("ticket_id", ticket.getId());
        detallesFragment.setArguments(bundle);

        // Reemplazar el fragmento actual
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, detallesFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String usuario = datos.getString("usuario", "");

        if (id == R.id.CerrarSesion) {
            SharedPreferences.Editor editor = datos.edit();
            editor.remove("usuario");
            editor.apply();

            Intent i = new Intent(requireContext(), MainActivity.class);
            startActivity(i);
            requireActivity().finish(); // Finalizar la actividad actual para evitar volver a ella
            return true;

        } else if (id == R.id.CrearTicket) {
            CrearTicketFragment crearTicketFragment = new CrearTicketFragment();


            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, crearTicketFragment)
                    .addToBackStack(null)
                    .commit();
            return true;
        }else if (id == R.id.verTicketsResueltos) {
            TicketsResueltosFragment ticketsResueltosFragment = new TicketsResueltosFragment();

            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, ticketsResueltosFragment)
                    .addToBackStack(null)
                    .commit();
            return true;

        }

        return super.onOptionsItemSelected(item);
    }



}
