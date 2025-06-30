package ar.edu.unq.epersgeist.persistencia.DAOs.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituStatsFirebaseDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;
@Component
public class EspirituStatsFirebaseDAOImpl  implements EspirituStatsFirebaseDAO {

    private static final String COLL = "estadisticas_espiritus";

    private final Firestore firestore;

    @Autowired
    public EspirituStatsFirebaseDAOImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    public void save(Espiritu e) throws InterruptedException, ExecutionException {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(e.getId().toString());

        Map<String, Object> init = Map.of(
                "nombre", e.getNombre(),
                "ganadas", 0,
                "perdidas", 0,
                "jugadas", 0,
                "vida", e.getVida(),
                "ataque", e.getAtaque(),
                "defensa", e.getDefensa()
        );
        // set() sin merge para asegurar que partimos de un estado limpio(sobrescribe o crea)
        doc.set(init).get();
    }
    @Override
    public Espiritu actualizar(Espiritu e) {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(e.getId().toString());

        try {
            DocumentSnapshot snapshot = doc.get().get();

            // Valores actuales
            Long ganadas = snapshot.contains("ganadas") ? snapshot.getLong("ganadas") : 0L;
            Long perdidas = snapshot.contains("perdidas") ? snapshot.getLong("perdidas") : 0L;
            Long jugadas = snapshot.contains("jugadas") ? snapshot.getLong("jugadas") : 0L;


            // Sumar nuevos valores
            Map<String, Object> updates = Map.of(
                    "ganadas", ganadas + e.getBatallasGanadas(),
                    "perdidas", perdidas + e.getBatallasPerdidas(),
                    "jugadas", jugadas + e.getBatallasJugadas(),
                    "vida", e.getVida()
            );

            doc.update(updates).get();
            return e;

        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Error al actualizar estadísticas en Firebase", ex);
        }
    }

    public void eliminar(Long id) {
        try {
            firestore.collection(COLL).document(id.toString()).delete().get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Error al eliminar estadísticas de Firebase", ex);
        }
    }

    public void deleteAll() {
        CollectionReference collection = firestore.collection(COLL);

        try {
            // Obtener todos los documentos de la colección
            ApiFuture<QuerySnapshot> future = collection.get();
            List<QueryDocumentSnapshot> documentos = future.get().getDocuments();

            for (QueryDocumentSnapshot doc : documentos) {
                doc.getReference().delete();
            }

        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Error al eliminar todos los documentos de Firebase", ex);
        }
    }

}
