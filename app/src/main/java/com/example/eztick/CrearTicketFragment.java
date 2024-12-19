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
import java.util.HashMap;
import java.util.Map;

/**
 * Fragmento para la creación o edición de un ticket.
 * Permite al usuario ingresar o modificar información sobre el ticket y guardarla en Firebase Firestore.
 */
public class CrearTicketFragment extends Fragment {

    // Referencia a Firestore para acceder a la colección de tickets.
    public FirebaseFirestore db = FirebaseFirestore.getInstance();
    public CollectionReference tickets = db.collection("tickets");

    // Vistas del formulario de ticket.
    private EditText Agregar_titulo, Agregar_descripcion, Agregar_fecha;
    private Spinner Agregar_lvlpeligro;
    private Button Btn_guardar;

    // Variables para distinguir entre crear y editar
    private String ticketId = null;

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
        Btn_guardar = view.findViewById(R.id.btn_agregar);

        // Configurar el spinner con los niveles de peligro.
        crearAdaptador(getContext());

        // Verificar si es edición o creación
        if (getArguments() != null) {
            ticketId = getArguments().getString("ticket_id", null);
            if (ticketId != null) {
                cargarDetallesTicket(ticketId); // Cargar datos para edición
                Btn_guardar.setText("Actualizar Ticket"); // Cambiar texto del botón
            }
        }

        // Configurar el botón para guardar (crear o editar ticket).
        Btn_guardar.setOnClickListener(v -> {
            if (ticketId == null) {
                crearTicket();
            } else {
                editarTicket(ticketId);
            }
        });

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
     * Valida los campos del formulario antes de guardar o editar el ticket.
     *
     * @return true si los campos son válidos, false en caso contrario.
     */
    private boolean validarCampos() {
        String titulo = Agregar_titulo.getText().toString();
        String descripcion = Agregar_descripcion.getText().toString();
        String fecha = Agregar_fecha.getText().toString();

        if (TextUtils.isEmpty(titulo)) {
            Agregar_titulo.setError("El título es obligatorio");
            return false;
        }

        if (TextUtils.isEmpty(descripcion)) {
            Agregar_descripcion.setError("La descripción es obligatoria");
            return false;
        }

        if (!fecha.matches("\\d{2}/\\d{2}/\\d{4}")) {
            Agregar_fecha.setError("La fecha debe tener el formato dd/MM/yyyy");
            return false;
        }

        return true;
    }

    /**
     * Crea un ticket usando la información del formulario y la agrega a Firestore.
     */
    private void crearTicket() {
        if (!validarCampos()) return;

        // Obtener los datos ingresados por el usuario en el formulario.
        String titulo = Agregar_titulo.getText().toString();
        String descripcion = Agregar_descripcion.getText().toString();
        String fecha = Agregar_fecha.getText().toString();
        String lvlPeligro = Agregar_lvlpeligro.getSelectedItem().toString();

        // Crear un objeto Ticket con los datos del formulario.
        ListElementTicket ticket = new ListElementTicket(titulo, descripcion, fecha, lvlPeligro);

        // Llamar al método para agregar el ticket a Firestore.
        agregarTicket(ticket);
    }

    /**
     * Edita un ticket existente usando la información del formulario y actualiza Firestore.
     *
     * @param ticketId El ID del ticket que se va a editar.
     */
    private void editarTicket(String ticketId) {
        if (!validarCampos()) return;

        // Obtener los datos ingresados por el usuario en el formulario.
        String titulo = Agregar_titulo.getText().toString();
        String descripcion = Agregar_descripcion.getText().toString();
        String fecha = Agregar_fecha.getText().toString();
        String lvlPeligro = Agregar_lvlpeligro.getSelectedItem().toString();

        // Crear un mapa con los campos a actualizar.
        Map<String, Object> updateFields = new HashMap<>();
        updateFields.put("titulo", titulo);
        updateFields.put("descripcion", descripcion);
        updateFields.put("fecha", fecha);
        updateFields.put("lvlPeligro", lvlPeligro);

        // Actualizar el ticket en la base de datos.
        tickets.document(ticketId).update(updateFields).addOnSuccessListener(aVoid -> {
            // Mostrar mensaje de éxito y volver al fragmento anterior.
            Toast.makeText(getContext(), "Ticket actualizado exitosamente", Toast.LENGTH_SHORT).show();
            requireActivity().getSupportFragmentManager().popBackStack();
        }).addOnFailureListener(e -> {
            // Mostrar mensaje de error si la operación falla.
            Toast.makeText(getContext(), "Error al actualizar el ticket", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Carga los detalles de un ticket existente para mostrarlos en el formulario.
     *
     * @param ticketId El ID del ticket cuyos detalles se van a cargar.
     */
    private void cargarDetallesTicket(String ticketId) {
        tickets.document(ticketId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                ListElementTicket ticket = documentSnapshot.toObject(ListElementTicket.class);
                if (ticket != null) {
                    Agregar_titulo.setText(ticket.getTitulo());
                    Agregar_descripcion.setText(ticket.getDescripcion());
                    Agregar_fecha.setText(ticket.getFecha());
                    ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) Agregar_lvlpeligro.getAdapter();
                    int position = adapter.getPosition(ticket.getLvlPeligro());
                    Agregar_lvlpeligro.setSelection(position);
                }
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Error al cargar los detalles del ticket", Toast.LENGTH_SHORT).show();
        });
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