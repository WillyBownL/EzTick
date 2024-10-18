package com.example.eztick;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void login(View v) {

        EditText campoUsuario = this.findViewById(R.id.usuario);
        String usuario = campoUsuario.getText().toString();
        EditText campoContrasenia = this.findViewById(R.id.contrasenia);
        String contrasenia = campoContrasenia.getText().toString();

        if (usuario.equals("RRHH") && contrasenia.equals(("123"))) {
            SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = datos.edit();
            editor.putString("usuario", usuario);
            editor.apply();

            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        } else if (usuario.equals("MANTENIMIENTO") && contrasenia.equals(("123"))) {
            SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = datos.edit();
            editor.putString("usuario", usuario);
            editor.apply();

            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        } else if (usuario.isEmpty() || contrasenia.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        String usuario = datos.getString("usuario","");
        if(!usuario.isEmpty()){
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        }

    }
}