package com.example.eztick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * Actividad que muestra los detalles de un ticket específico.
 * Permite al usuario ver información detallada sobre un ticket y tomar acciones sobre él
 * como finalizar o eliminarlo, dependiendo de su rol.
 */
public class Detalles_ticket_old extends AppCompatActivity {

    private TextView ticketTituloDetail, ticketDescripcionDetail, ticketFechaDetail, ticketPeligroDetail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String ticketId;

    /**
     * Método que se llama cuando la actividad es creada. Configura la interfaz de usuario,
     * obtiene el ticket seleccionado y carga los detalles desde la base de datos.
     *
     * @param savedInstanceState El estado guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_ticket);

        // Obtener el nombre del usuario desde SharedPreferences y mostrar un mensaje de bienvenida.
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");

        // Configurar el Toolbar de la actividad.
        Toolbar TB = findViewById(R.id.toolbar_tickets);
        setSupportActionBar(TB);

        // Inicializar las vistas.
        ticketTituloDetail = findViewById(R.id.ticketTituloDetail);
        ticketDescripcionDetail = findViewById(R.id.ticketDescripcionDetail);
        ticketFechaDetail = findViewById(R.id.ticketFechaDetail);
        ticketPeligroDetail = findViewById(R.id.ticketPeligroDetail);

        // Obtener el ID del ticket desde el Intent.
        ticketId = getIntent().getStringExtra("ticket_id");

        // Si se ha recibido un ID de ticket, cargar los detalles de ese ticket.
        if (ticketId != null) {
            cargarDetallesTicket(ticketId);
        }
    }

    /**
     * Infla el menú de opciones de la actividad basado en el tipo de usuario.
     *
     * @param menu El menú que se va a inflar.
     * @return true para indicar que el menú fue inflado correctamente.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");
        
        // Inflar el menú dependiendo del tipo de usuario (Mantenimiento o RRHH).
        if (usuario.equals("MANTENIMIENTO")) {
            getMenuInflater().inflate(R.menu.menu_solo_volver, menu);
        } else if (usuario.equals("RRHH")) {
            getMenuInflater().inflate(R.menu.menu_detalles_ticket, menu);
        }

        return true;
    }

    /**
     * Maneja las selecciones del menú de opciones.
     * Permite al usuario eliminar un ticket o volver a la lista de tickets.
     *
     * @param item El ítem seleccionado del menú.
     * @return true si el ítem fue manejado, false en caso contrario.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Si el usuario selecciona "finalizar", eliminar el ticket.
        if (id == R.id.finalizar) {
            eliminarTicket(ticketId);
        } else if (id == R.id.atras) {
            // Si el usuario selecciona "volver", regresar a la lista de tickets.
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Carga los detalles del ticket desde Firestore utilizando el ID del ticket.
     *
     * @param ticketId El ID del ticket a cargar.
     */
    private void cargarDetallesTicket(String ticketId) {
        db.collection("tickets").document(ticketId).get().addOnSuccessListener(documentSnapshot -> {
            // Si el documento existe, asignar los datos a las vistas.
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
            // Manejo de errores (vacío por el momento).
        });
    }

    /**
     * Elimina un ticket de la base de datos y muestra un mensaje de éxito o error.
     *
     * @param ticketId El ID del ticket a eliminar.
     */
    private void eliminarTicket(String ticketId) {
        db.collection("tickets").document(ticketId).delete().addOnSuccessListener(aVoid -> {
            // Mostrar un mensaje de éxito y regresar a la lista de tickets.
            Toast.makeText(this, "Ticket eliminado exitosamente", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Detalles_ticket_old.this, Menu_Lista_Tickets.class);
            startActivity(intent);
            finish();
        }).addOnFailureListener(e -> {
            // Mostrar un mensaje de error si falla la eliminación.
            Toast.makeText(this, "Error al eliminar el ticket", Toast.LENGTH_SHORT).show();
        });
    }
}
