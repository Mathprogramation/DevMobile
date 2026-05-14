package com.example.ac2.database;
import com.example.ac2.models.Treino;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TreinoDatabase {

    private final FirebaseFirestore db;
    private static final String COLECAO = "treinos";

    public interface Callback {
        void onResult(List<Treino> treinos);
    }

    public TreinoDatabase() {
        db = FirebaseFirestore.getInstance();
    }

    // ── CREATE ──────────────────────────────────────────────
    public void inserir(Treino treino) {
        db.collection(COLECAO)
                .add(treino)
                .addOnSuccessListener(ref -> treino.setId(ref.getId()));
    }

    // ── UPDATE ──────────────────────────────────────────────
    public void atualizar(Treino treino) {
        db.collection(COLECAO)
                .document(treino.getId())
                .set(treino);
    }

    // ── DELETE ──────────────────────────────────────────────
    public void deletar(String id) {
        db.collection(COLECAO)
                .document(id)
                .delete();
    }

    // ── READ com filtros e ordenação ─────────────────────────
    public void listar(String tipoAtividade,
                       boolean treinoConcluido,
                       String ordenacao,
                       Callback callback) {

        Query query = db.collection(COLECAO);

        // Filtro por tipo de atividade
        if (tipoAtividade != null && !tipoAtividade.equals("Todos")) {
            query = query.whereEqualTo("tipoAtividade", tipoAtividade);
        }

        // Filtro por status Treino
        if (treinoConcluido) {
            query = query.whereEqualTo("treinoConcluido", true);
        }

        // Ordenação
        if ("Duracao".equals(ordenacao)) {
            query = query.orderBy("Duracao", Query.Direction.DESCENDING);
        } else if ("Data".equals(ordenacao)) {
            query = query.orderBy("data", Query.Direction.DESCENDING);
        } else {
            query = query.orderBy("nomeTreino", Query.Direction.ASCENDING);
        }

        query.get().addOnSuccessListener(snapshot -> {
            List<Treino> lista = new ArrayList<>();
            for (QueryDocumentSnapshot doc : snapshot) {
                Treino t = doc.toObject(Treino.class);
                t.setId(doc.getId());
                lista.add(t);
            }
            callback.onResult(lista);
        }).addOnFailureListener(e -> callback.onResult(new ArrayList<>()));
    }
}
