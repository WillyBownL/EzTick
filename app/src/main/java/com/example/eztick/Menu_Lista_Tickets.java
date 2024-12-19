package com.example.eztick;

import static androidx.core.content.ContentProviderCompat.requireContext;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Actividad principal que muestra la lista de tickets y gestiona la navegación entre fragmentos.
 * También maneja la interfaz de usuario basada en el tipo de usuario (Mantenimiento, RRHH).
 */
public class Menu_Lista_Tickets extends AppCompatActivity {

    /**
     * Método que se llama cuando la actividad se crea. Inicializa la interfaz de usuario
     * y muestra el fragmento correspondiente según el tipo de usuario.
     *
     * @param savedInstanceState El estado guardado de la actividad, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this); // Habilitar la funcionalidad de pantalla completa (Edge to Edge).

        // Obtener el nombre de usuario desde SharedPreferences y mostrar un mensaje de bienvenida.
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");
        Toast.makeText(this, "Ingresaste como " + usuario, Toast.LENGTH_SHORT).show();

        // Establecer el layout de la actividad con la lista de tickets.
        setContentView(R.layout.activity_ticket_list);

        // Configurar el Toolbar para la actividad.
        Toolbar TB = findViewById(R.id.toolbar_tickets);
        setSupportActionBar(TB);

        // Cargar el fragmento que contiene la lista de tickets.
        loadFragment(new TicketListFragment());
    }

    /**
     * Infla el menú de opciones de la actividad dependiendo del tipo de usuario.
     *
     * @param menu El menú que se va a inflar.
     * @return true para indicar que el menú fue inflado correctamente.
     */
    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // Obtener el tipo de usuario desde SharedPreferences.
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");

        // Inflar el menú correspondiente según el tipo de usuario (Mantenimiento o RRHH).
        if (usuario.equals("MANTENIMIENTO")) {
            inflater.inflate(R.menu.menu_mantenimiento, menu);
        } else if (usuario.equals("RRHH")) {
            inflater.inflate(R.menu.menu_rrhh, menu);
        }
        return true;
    }

    /**
     * Método que permite cargar un fragmento en el contenedor de la actividad.
     *
     * @param fragment El fragmento que se desea cargar.
     */
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment); // Reemplazar el fragmento actual.
        transaction.addToBackStack(null); // Añadir la transacción al backstack para poder regresar.
        transaction.commit(); // Ejecutar la transacción.

        // Invalidar el menú para que se actualice si es necesario.
        invalidateOptionsMenu();
    }

    /**
     * Prepara el menú de opciones antes de mostrarlo, permitiendo realizar cambios si es necesario.
     *
     * @param menu El menú de opciones que se va a preparar.
     * @return true para indicar que el menú está listo.
     */
    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        return true;
    }
}
