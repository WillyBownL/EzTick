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


public class Menu_Lista_Tickets extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        //Para recordarme que usuario esta logeado
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");
        Toast.makeText(this, "Ingresaste como " + usuario, Toast.LENGTH_SHORT).show();

        //Referencia a la vista de la lista de tickets
        setContentView(R.layout.activity_ticket_list);
        //Referencia al Toolbar
        Toolbar TB = findViewById(R.id.toolbar_tickets);
        setSupportActionBar(TB);

        loadFragment(new TicketListFragment());

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();

        // Obtener el usuario actual de SharedPreferences
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");

        // Inflar el menú basado en el usuario
        if (usuario.equals("MANTENIMIENTO")) {
            inflater.inflate(R.menu.menu_mantenimiento, menu);
        } else if (usuario.equals("RRHH")) {
            inflater.inflate(R.menu.menu_rrhh, menu);
        }
        return true;
    }


    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();

        // Llamar a invalidateOptionsMenu() para actualizar el menú
        invalidateOptionsMenu();
    }


    @Override
    public boolean onPrepareOptionsMenu(@NonNull Menu menu) {
        return true;
    }


}