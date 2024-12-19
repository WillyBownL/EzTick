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

/**
 * Actividad principal que gestiona el inicio de sesión del usuario en la aplicación.
 * Valida las credenciales y redirige al usuario a la pantalla de lista de tickets
 * según el tipo de usuario. La actividad también maneja la persistencia del usuario
 * a través de SharedPreferences.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * Método que se ejecuta cuando se crea la actividad.
     * Configura el modo de pantalla completa y ajusta el contenido de la vista
     * en función de las barras de sistema (como las de estado y navegación).
     * 
     * @param savedInstanceState Estado de la instancia guardado, si existe.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Ajuste de los insets para garantizar que el contenido no se solape con las barras de sistema.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Método que maneja el inicio de sesión del usuario. Verifica si las credenciales son correctas
     * y, si lo son, guarda el usuario en SharedPreferences y redirige a la pantalla de lista de tickets.
     * 
     * @param v La vista (botón) que ejecuta este método.
     */
    public void login(View v) {
        EditText campoUsuario = this.findViewById(R.id.usuario);
        String usuario = campoUsuario.getText().toString();
        EditText campoContrasenia = this.findViewById(R.id.contrasenia);
        String contrasenia = campoContrasenia.getText().toString();

        // Validación de las credenciales
        if (usuario.equals("RRHH") && contrasenia.equals(("123"))) {
            SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = datos.edit();
            editor.putString("usuario", usuario);
            editor.apply();

            // Redirigir al usuario a la pantalla de tickets
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        } else if (usuario.equals("MANTENIMIENTO") && contrasenia.equals(("123"))) {
            SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
            SharedPreferences.Editor editor = datos.edit();
            editor.putString("usuario", usuario);
            editor.apply();

            // Redirigir al usuario a la pantalla de tickets
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        } else if (usuario.isEmpty() || contrasenia.isEmpty()) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error en las credenciales", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Este método se ejecuta cuando la actividad vuelve a primer plano.
     * Verifica si un usuario ya está guardado en SharedPreferences y, si es así,
     * redirige automáticamente a la pantalla de lista de tickets.
     */
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(this);
        String usuario = datos.getString("usuario","");

        // Si ya existe un usuario, redirige al listado de tickets
        if(!usuario.isEmpty()){
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        }
    }
}
