package com.example.eztick;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragmento que muestra una lista de comentarios relacionados con un ticket.
 * Permite agregar nuevos comentarios y visualizar los existentes.
 */
public class ComentariosFragment extends Fragment {

    // Vista de RecyclerView donde se muestran los comentarios.
    private RecyclerView recyclerViewComentarios;

    // Adaptador para el RecyclerView que maneja los comentarios.
    private ComentariosAdapter comentariosAdapter;

    // Lista que contiene los comentarios cargados desde Firestore.
    private List<Comentario> listaComentarios;

    // Referencia a la base de datos de Firestore.
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // Referencia a la colección de comentarios de un ticket específico.
    private CollectionReference comentariosRef;

    // EditText para que el usuario escriba un nuevo comentario.
    private EditText editTextComentario;

    // Botón para agregar un nuevo comentario.
    private Button btnAgregarComentario;

    // ID del ticket actual para asociar los comentarios.
    private String ticketId;

    /**
     * Método que infla el layout del fragmento y configura las vistas.
     *
     * @param inflater Inflador de vistas para crear la vista del fragmento.
     * @param container Contenedor al que se va a agregar el fragmento.
     * @param savedInstanceState Estado guardado del fragmento.
     * @return La vista inflada para el fragmento.
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Infla la vista del fragmento.
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);

        // Obtener el ID del ticket desde los argumentos del fragmento.
        if (getArguments() != null) {
            ticketId = getArguments().getString("ticket_id");
            comentariosRef = db.collection("tickets").document(ticketId).collection("comentarios");
        }

        // Inicialización de vistas.
        recyclerViewComentarios = view.findViewById(R.id.recyclerViewComentarios);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true); // Configura para que los comentarios nuevos aparezcan al final.
        recyclerViewComentarios.setLayoutManager(layoutManager);

        // Lista de comentarios que se mostrarán.
        listaComentarios = new ArrayList<>();
        comentariosAdapter = new ComentariosAdapter(listaComentarios);
        recyclerViewComentarios.setAdapter(comentariosAdapter);

        // Inicialización del campo de texto para los comentarios.
        editTextComentario = view.findViewById(R.id.editTextComentario);
        btnAgregarComentario = view.findViewById(R.id.btnAgregarComentario);

        // Configuración del botón para agregar un comentario.
        btnAgregarComentario.setOnClickListener(v -> agregarComentario());

        // Cargar los comentarios existentes al inicializar el fragmento.
        cargarComentarios();

        // Habilitar el menú en este fragmento.
        setHasOptionsMenu(true);

        return view;
    }

    /**
     * Carga los comentarios existentes desde Firestore, ordenados por el timestamp.
     */
    private void cargarComentarios() {
        comentariosRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    listaComentarios.clear(); // Limpiar la lista antes de agregar nuevos comentarios.
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        // Convertir cada documento en un objeto Comentario.
                        Comentario comentario = document.toObject(Comentario.class);
                        listaComentarios.add(comentario);
                    }
                    comentariosAdapter.notifyDataSetChanged(); // Notificar al adaptador para actualizar la vista.
                    // Desplazarse al último comentario.
                    recyclerViewComentarios.scrollToPosition(listaComentarios.size() - 1);
                }).addOnFailureListener(e -> {
                    if (isAdded()) {
                        // Mostrar un mensaje de error si la carga falla.
                        Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * Agrega un nuevo comentario a Firestore.
     * Toma el texto del comentario, obtiene el rol del usuario desde SharedPreferences,
     * y lo agrega a la colección de comentarios en Firestore.
     */
    private void agregarComentario() {
        String textoComentario = editTextComentario.getText().toString();
        if (TextUtils.isEmpty(textoComentario)) {
            // Mostrar un mensaje si el comentario está vacío.
            Toast.makeText(getContext(), "Escribe un comentario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el rol del usuario desde SharedPreferences.
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String rol = datos.getString("usuario", "");

        // Crear un mapa con los datos del nuevo comentario.
        Map<String, Object> nuevoComentario = new HashMap<>();
        nuevoComentario.put("rol", rol);
        nuevoComentario.put("texto", textoComentario);
        nuevoComentario.put("timestamp", System.currentTimeMillis()); // Añadir la marca de tiempo.

        // Agregar el comentario a la base de datos.
        comentariosRef.add(nuevoComentario).addOnSuccessListener(documentReference -> {
            // Mostrar mensaje de éxito y recargar la lista de comentarios.
            Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
            editTextComentario.setText(""); // Limpiar el campo de texto.
            cargarComentarios(); // Recargar los comentarios.
        }).addOnFailureListener(e -> {
            if (isAdded()) {
                // Mostrar mensaje de error si no se pudo agregar el comentario.
                Toast.makeText(getContext(), "Error al agregar comentario", Toast.LENGTH_SHORT).show();
            }
        });
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
}
