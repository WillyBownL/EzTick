package com.example.eztick;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class activity_crear_ticket extends AppCompatActivity {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference tickets = db.collection("tickets");

    private EditText Agregar_titulo, Agregar_descripcion, Agregar_fecha;
    private Spinner Agregar_lvlpeligro;
    private Button Btn_agregar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_ticket);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Agrego la toolbar
        Toolbar TB = findViewById(R.id.toolbar_tickets);
        setSupportActionBar(TB);

        //Inicializo Firebase
        db = FirebaseFirestore.getInstance();

        //Referencio los elementos del layout
        Agregar_titulo = findViewById(R.id.agregar_titulo);
        Agregar_descripcion = findViewById(R.id.agregar_descripcion);
        Agregar_fecha = findViewById(R.id.agregar_fecha);
        Agregar_lvlpeligro = findViewById(R.id.agregar_lvlpeligro);
        Btn_agregar = findViewById(R.id.btn_agregar);

        //Configuro el spinner para dar las opciones de nivel de peligro
        crearAdaptador(this);

        //Configurar boton
        Btn_agregar.setOnClickListener(v -> crearTicket());

    }



    public void crearAdaptador(Context context) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.niveles_peligro,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Agregar_lvlpeligro.setAdapter(adapter);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_solo_volver,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id==R.id.atras) {
            limpiarCampos();
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    private void crearTicket(){
        String titulo = Agregar_titulo.getText().toString();
        String descripcion = Agregar_descripcion.getText().toString();
        String fecha = Agregar_fecha.getText().toString();
        String lvlPeligro = Agregar_lvlpeligro.getSelectedItem().toString();

        if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(fecha)) {
            Toast.makeText(this, "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ListElementTicket ticket = new ListElementTicket(titulo, descripcion, fecha, lvlPeligro);
        agregarTicket(ticket);
    }

    private void agregarTicket(ListElementTicket ticket) {
        tickets.add(ticket).addOnSuccessListener(documentReference -> {
            // Ticket agregado exitosamente
            Toast.makeText(this, "Ticket agregado exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            Intent i = new Intent(this, Menu_Lista_Tickets.class);
            startActivity(i);
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Error al agregar el ticket", Toast.LENGTH_SHORT).show();
        });
    }
    private void limpiarCampos() {
        Agregar_titulo.setText("");
        Agregar_descripcion.setText("");
        Agregar_fecha.setText("");
        Agregar_lvlpeligro.setSelection(0);
    }



}