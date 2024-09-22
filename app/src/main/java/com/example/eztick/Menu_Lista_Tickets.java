package com.example.eztick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

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
        //Referencia lista
        Lista_Tickets LT = new Lista_Tickets();
        getSupportFragmentManager().beginTransaction().replace(R.id.ListaTickets,LT).commit();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // incorporar el menu en la activity
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String usuario = datos.getString("usuario", "");
        if (usuario.equals("MANTENIMIENTO")){
            getMenuInflater().inflate(R.menu.menu_mantenimiento,menu);
        } else if (usuario.equals("RRHH")) {
            getMenuInflater().inflate(R.menu.menu_rrhh,menu);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.CerrarSesion){
            SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor editor = datos.edit();
            editor.remove("usuario");
            editor.apply();

            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        } else if (id==R.id.CrearTicket) {
            Intent i = new Intent(this, activity_crear_ticket.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }
}