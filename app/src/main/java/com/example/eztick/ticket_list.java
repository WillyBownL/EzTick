package com.example.eztick;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ticket_list extends Fragment implements ListAdapter_ticket.OnTicketClickListener {
    private RecyclerView recyclerView;
    private ListAdapter_ticket ticketAdapter;
    private List<ListElementTicket> listaTickets;
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference tickets = db.collection("tickets");

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ticket_list, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewTicket);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializar la lista de tickets (asegúrate de no dejarla null)
        listaTickets = new ArrayList<>();

        // Crear y configurar el adaptador
        ticketAdapter = new ListAdapter_ticket(listaTickets, getContext(), this);
        recyclerView.setAdapter(ticketAdapter);

        cargarTickets();

        return view;
    }

    private void cargarTickets() {
        tickets.get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (listaTickets != null) {
                listaTickets.clear();
            }
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                ListElementTicket ticket = documentSnapshot.toObject(ListElementTicket.class);
                assert ticket != null;
                ticket.setId(documentSnapshot.getId());
                listaTickets.add(ticket);
            }

            Collections.sort(listaTickets, (ticket1, ticket2) -> {
                int lvlPeligro1 = Integer.parseInt(ticket1.getLvlPeligro());
                int lvlPeligro2 = Integer.parseInt(ticket2.getLvlPeligro());
                return Integer.compare(lvlPeligro2, lvlPeligro1); // Ordenar por nivel de peligro
            });

            ticketAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Log.e("FirestoreError", "Error al cargar los tickets: " + e.getMessage());
        });
    }

    @Override
    public void onTicketClick(ListElementTicket ticket) {
        Log.d("TicketClick", "Ticket seleccionado: " + ticket.getTitulo());
        Intent intent = new Intent(getContext(), Detalles_ticket.class);
        intent.putExtra("ticket_id", ticket.getId());
        startActivity(intent);
    }
}


