package com.example.eztick;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class CrearTicketFragment extends Fragment {

    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference tickets = db.collection("tickets");

    private EditText Agregar_titulo, Agregar_descripcion, Agregar_fecha;
    private Spinner Agregar_lvlpeligro;
    private Button Btn_agregar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_ticket, container, false);

        // Inicializar vistas
        Agregar_titulo = view.findViewById(R.id.agregar_titulo);
        Agregar_descripcion = view.findViewById(R.id.agregar_descripcion);
        Agregar_fecha = view.findViewById(R.id.agregar_fecha);
        Agregar_lvlpeligro = view.findViewById(R.id.agregar_lvlpeligro);
        Btn_agregar = view.findViewById(R.id.btn_agregar);

        // Configurar el spinner
        crearAdaptador(getContext());

        // Configurar el botón
        Btn_agregar.setOnClickListener(v -> crearTicket());

        // Habilitar el menú en el fragmento
        setHasOptionsMenu(true);

        return view;
    }

    private void crearAdaptador(Context context) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.niveles_peligro, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Agregar_lvlpeligro.setAdapter(adapter);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // Limpiar el menú actual
        inflater.inflate(R.menu.menu_solo_volver, menu); // Inflar el menú
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.atras) {
            requireActivity().getSupportFragmentManager().popBackStack(); // Volver al fragmento anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void crearTicket() {
        String titulo = Agregar_titulo.getText().toString();
        String descripcion = Agregar_descripcion.getText().toString();
        String fecha = Agregar_fecha.getText().toString();
        String lvlPeligro = Agregar_lvlpeligro.getSelectedItem().toString();

        if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(fecha)) {
            Toast.makeText(getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        ListElementTicket ticket = new ListElementTicket(titulo, descripcion, fecha, lvlPeligro);
        agregarTicket(ticket);
    }

    private void agregarTicket(ListElementTicket ticket) {
        tickets.add(ticket).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Ticket agregado exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            requireActivity().getSupportFragmentManager().popBackStack(); // Volver a la lista de tickets
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error al agregar el ticket", Toast.LENGTH_SHORT).show();
        });
    }

    private void limpiarCampos() {
        Agregar_titulo.setText("");
        Agregar_descripcion.setText("");
        Agregar_fecha.setText("");
        Agregar_lvlpeligro.setSelection(0);
    }
}
