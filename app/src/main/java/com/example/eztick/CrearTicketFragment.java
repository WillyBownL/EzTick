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

/**
 * Fragmento para la creación de un nuevo ticket.
 * Permite al usuario ingresar información sobre el ticket y guardarla en Firebase Firestore.
 */
public class CrearTicketFragment extends Fragment {

    // Referencia a Firestore para acceder a la colección de tickets.
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference tickets = db.collection("tickets");

    // Vistas del formulario de creación de ticket.
    private EditText Agregar_titulo, Agregar_descripcion, Agregar_fecha;
    private Spinner Agregar_lvlpeligro;
    private Button Btn_agregar;

    /**
     * Método que infla la vista del fragmento y configura las vistas y acciones.
     *
     * @param inflater Inflador de vistas para crear la vista del fragmento.
     * @param container Contenedor al que se va a agregar el fragmento.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista inflada para el fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_crear_ticket, container, false);

        // Inicializar vistas.
        Agregar_titulo = view.findViewById(R.id.agregar_titulo);
        Agregar_descripcion = view.findViewById(R.id.agregar_descripcion);
        Agregar_fecha = view.findViewById(R.id.agregar_fecha);
        Agregar_lvlpeligro = view.findViewById(R.id.agregar_lvlpeligro);
        Btn_agregar = view.findViewById(R.id.btn_agregar);

        // Configurar el spinner con los niveles de peligro.
        crearAdaptador(getContext());

        // Configurar el botón de agregar ticket.
        Btn_agregar.setOnClickListener(v -> crearTicket());

        // Habilitar el menú en el fragmento.
        setHasOptionsMenu(true);

        return view;
    }

    /**
     * Configura el adaptador del spinner para los niveles de peligro.
     *
     * @param context Contexto de la actividad para acceder a los recursos.
     */
    private void crearAdaptador(Context context) {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.niveles_peligro, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Agregar_lvlpeligro.setAdapter(adapter);
    }

    /**
     * Inflar el menú de opciones en este fragmento.
     *
     * @param menu Menú al que se agregan las opciones.
     * @param inflater Inflador de menú para inflar las opciones.
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        menu.clear(); // Limpiar el menú actual.
        inflater.inflate(R.menu.menu_solo_volver, menu); // Inflar el menú de opciones.
    }

    /**
     * Maneja las acciones del menú de opciones.
     *
     * @param item El item seleccionado en el menú.
     * @return Verdadero si la acción fue manejada.
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.atras) {
            // Volver al fragmento anterior.
            requireActivity().getSupportFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Crea un ticket usando la información del formulario y la agrega a Firestore.
     */
    private void crearTicket() {
        // Obtener los datos ingresados por el usuario en el formulario.
        String titulo = Agregar_titulo.getText().toString();
        String descripcion = Agregar_descripcion.getText().toString();
        String fecha = Agregar_fecha.getText().toString();
        String lvlPeligro = Agregar_lvlpeligro.getSelectedItem().toString();

        // Verificar que todos los campos estén completos.
        if (TextUtils.isEmpty(titulo) || TextUtils.isEmpty(descripcion) || TextUtils.isEmpty(fecha)) {
            Toast.makeText(getContext(), "Rellene todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear un objeto Ticket con los datos del formulario.
        ListElementTicket ticket = new ListElementTicket(titulo, descripcion, fecha, lvlPeligro);

        // Llamar al método para agregar el ticket a Firestore.
        agregarTicket(ticket);
    }

    /**
     * Agrega un ticket a la colección de Firestore y maneja el éxito o fracaso de la operación.
     *
     * @param ticket El ticket a agregar a la base de datos.
     */
    private void agregarTicket(ListElementTicket ticket) {
        tickets.add(ticket).addOnSuccessListener(documentReference -> {
            // Mostrar mensaje de éxito y limpiar los campos del formulario.
            Toast.makeText(getContext(), "Ticket agregado exitosamente", Toast.LENGTH_SHORT).show();
            limpiarCampos();
            // Volver a la lista de tickets (fragmento anterior).
            requireActivity().getSupportFragmentManager().popBackStack();
        }).addOnFailureListener(e -> {
            // Mostrar mensaje de error si la operación falla.
            Toast.makeText(getContext(), "Error al agregar el ticket", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Limpia los campos del formulario después de agregar un ticket exitosamente.
     */
    private void limpiarCampos() {
        Agregar_titulo.setText("");
        Agregar_descripcion.setText("");
        Agregar_fecha.setText("");
        Agregar_lvlpeligro.setSelection(0); // Restablecer el spinner al primer valor.
    }
}
