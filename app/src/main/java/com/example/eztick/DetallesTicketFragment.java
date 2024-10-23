package com.example.eztick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.FirebaseFirestore;

public class DetallesTicketFragment extends Fragment {

    private TextView ticketTituloDetail, ticketDescripcionDetail, ticketFechaDetail, ticketPeligroDetail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String ticketId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detalles_ticket, container, false);

        // Habilitar el menú en el fragmento
        setHasOptionsMenu(true);

        // Inicializar vistas y cargar datos
        ticketTituloDetail = view.findViewById(R.id.ticketTituloDetail);
        ticketDescripcionDetail = view.findViewById(R.id.ticketDescripcionDetail);
        ticketFechaDetail = view.findViewById(R.id.ticketFechaDetail);
        ticketPeligroDetail = view.findViewById(R.id.ticketPeligroDetail);

        if (getArguments() != null) {
            ticketId = getArguments().getString("ticket_id");
            cargarDetallesTicket(ticketId);
        }

        return view;
    }


    private void cargarDetallesTicket(String ticketId) {
        db.collection("tickets").document(ticketId).get().addOnSuccessListener(documentSnapshot -> {
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
            Toast.makeText(getContext(), "Error al cargar los detalles del ticket", Toast.LENGTH_SHORT).show();
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
            inflater.inflate(R.menu.menu_solo_volver, menu);
        } else if (usuario.equals("RRHH")) {
            inflater.inflate(R.menu.menu_detalles_ticket, menu);
        }
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.finalizar) {
            eliminarTicket(ticketId);
            return true;
        } else if (id == R.id.atras) {
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void eliminarTicket(String ticketId) {
        db.collection("tickets").document(ticketId).delete().addOnSuccessListener(aVoid -> {
            Toast.makeText(getContext(), "Ticket eliminado exitosamente", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error al eliminar el ticket", Toast.LENGTH_SHORT).show();
        });
    }
}
