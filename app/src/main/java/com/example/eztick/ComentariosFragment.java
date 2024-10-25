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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ComentariosFragment extends Fragment {

    private RecyclerView recyclerViewComentarios;
    private ComentariosAdapter comentariosAdapter;
    private List<Comentario> listaComentarios;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference comentariosRef;
    private EditText editTextComentario;
    private Button btnAgregarComentario;
    private String ticketId;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comentarios, container, false);

        // Obtener el ID del ticket desde los argumentos
        if (getArguments() != null) {
            ticketId = getArguments().getString("ticket_id");
            comentariosRef = db.collection("tickets").document(ticketId).collection("comentarios");
        }

        // Inicializar vistas
        recyclerViewComentarios = view.findViewById(R.id.recyclerViewComentarios);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true); // Configuración para que el RecyclerView comience desde abajo
        recyclerViewComentarios.setLayoutManager(layoutManager);
        listaComentarios = new ArrayList<>();
        comentariosAdapter = new ComentariosAdapter(listaComentarios);
        recyclerViewComentarios.setAdapter(comentariosAdapter);

        editTextComentario = view.findViewById(R.id.editTextComentario);
        btnAgregarComentario = view.findViewById(R.id.btnAgregarComentario);

        // Configurar el botón para agregar un comentario
        btnAgregarComentario.setOnClickListener(v -> agregarComentario());

        // Cargar comentarios existentes
        cargarComentarios();

        // Habilitar el menú en el fragmento
        setHasOptionsMenu(true);

        return view;
    }

    private void cargarComentarios() {
        comentariosRef.orderBy("timestamp", Query.Direction.ASCENDING)
                .get().addOnSuccessListener(queryDocumentSnapshots -> {
                    listaComentarios.clear();
                    for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                        Comentario comentario = document.toObject(Comentario.class);
                        listaComentarios.add(comentario);
                    }
                    comentariosAdapter.notifyDataSetChanged();
                    // Desplazarse al último comentario
                    recyclerViewComentarios.scrollToPosition(listaComentarios.size() - 1);
                }).addOnFailureListener(e -> {
                    if (isAdded()) {
                        Toast.makeText(getContext(), "Error al cargar comentarios", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void agregarComentario() {
        String textoComentario = editTextComentario.getText().toString();
        if (TextUtils.isEmpty(textoComentario)) {
            Toast.makeText(getContext(), "Escribe un comentario", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el rol del usuario desde SharedPreferences
        SharedPreferences datos = PreferenceManager.getDefaultSharedPreferences(requireContext());
        String rol = datos.getString("usuario", "");

        Map<String, Object> nuevoComentario = new HashMap<>();
        nuevoComentario.put("rol", rol);
        nuevoComentario.put("texto", textoComentario);
        nuevoComentario.put("timestamp", System.currentTimeMillis()); // Añadir marca de tiempo

        comentariosRef.add(nuevoComentario).addOnSuccessListener(documentReference -> {
            Toast.makeText(getContext(), "Comentario agregado", Toast.LENGTH_SHORT).show();
            editTextComentario.setText("");
            cargarComentarios(); // Recargar la lista de comentarios
        }).addOnFailureListener(e -> {
            if (isAdded()) {
                Toast.makeText(getContext(), "Error al agregar comentario", Toast.LENGTH_SHORT).show();
            }
        });
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
}
