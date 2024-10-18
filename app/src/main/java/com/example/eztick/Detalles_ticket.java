package com.example.eztick;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Detalles_ticket extends AppCompatActivity {

    private TextView ticketTituloDetail, ticketDescripcionDetail, ticketFechaDetail, ticketPeligroDetail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String ticketId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ticket);

        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");

        Toolbar TB = findViewById(R.id.toolbar_tickets);
        setSupportActionBar(TB);

        ticketTituloDetail = findViewById(R.id.ticketTituloDetail);
        ticketDescripcionDetail = findViewById(R.id.ticketDescripcionDetail);
        ticketFechaDetail = findViewById(R.id.ticketFechaDetail);
        ticketPeligroDetail = findViewById(R.id.ticketPeligroDetail);

        ticketId = getIntent().getStringExtra("ticket_id");

        if (ticketId != null) {
            cargarDetallesTicket(ticketId);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");
        if (usuario.equals("MANTENIMIENTO")){
            getMenuInflater().inflate(R.menu.menu_solo_volver,menu);
        } else if (usuario.equals("RRHH")) {
            getMenuInflater().inflate(R.menu.menu_detalles_ticket,menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.finalizar){
            eliminarTicket(ticketId);
        } else if (id==R.id.atras) {
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
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
        });
    }

    private void eliminarTicket(String ticketId) {
        db.collection("tickets").document(ticketId).delete().addOnSuccessListener(aVoid -> {
            Toast.makeText(this, "Ticket eliminado exitosamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Detalles_ticket.this, Menu_Lista_Tickets.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al eliminar el ticket", Toast.LENGTH_SHORT).show();
        });
    }

}



