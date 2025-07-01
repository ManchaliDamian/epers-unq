package ar.edu.unq.epersgeist.persistencia.DAOs.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOFirestore;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.util.List;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.function.Consumer;

@Component
public class EspirituDAOFirestoreImpl implements EspirituDAOFirestore {

    private static final String COLL = "estadisticas_espiritus";

    private final Firestore firestore;

    public EspirituDAOFirestoreImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    @Override
    public void crear(Espiritu e) {
        try {
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

        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error al guardar en Firestore (interrumpido)", ex);
        }

        catch (ExecutionException ex) {
            throw new RuntimeException("Error al guardar en Firestore", ex);
        }
    }

    @Override
    public Espiritu actualizar(Espiritu e) {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(e.getId().toString());

        try {
            DocumentSnapshot snapshot = doc.get().get();

            // Valores actuales
            Long ganadas  = this.getLongOrDefault(snapshot, "ganadas");
            Long perdidas = this.getLongOrDefault(snapshot, "perdidas");
            Long jugadas  = this.getLongOrDefault(snapshot, "jugadas");


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

    private Long getLongOrDefault(DocumentSnapshot snapshot, String key) {
        Long val = snapshot.getLong(key);
        return val != null ? val : 0L;
    }

    @Override
    public void eliminar(Long id) {
        try {
            firestore.collection(COLL).document(id.toString()).delete().get();
        } catch (InterruptedException | ExecutionException ex) {
            throw new RuntimeException("Error al eliminar estadísticas de Firebase", ex);
        }
    }

    @Override
    public void enriquecer(Espiritu espiritu) {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(espiritu.getId().toString());

        try {
            DocumentSnapshot snapshot = doc.get().get();

            if (!snapshot.exists()) return;

            Map<String, Consumer<Integer>> setters = Map.of(
                    "vida",     espiritu::setVida,
                    "ganadas",  espiritu::setBatallasGanadas,
                    "perdidas", espiritu::setBatallasPerdidas,
                    "jugadas",  espiritu::setBatallasJugadas,
                    "ataque",   espiritu::setAtaque,
                    "defensa",  espiritu::setDefensa
            );

            for (var entry : setters.entrySet()) {
                if (snapshot.contains(entry.getKey())) {
                    Long val = snapshot.getLong(entry.getKey());
                    if (val != null) entry.getValue().accept(val.intValue());
                }
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al enriquecer desde Firebase", e);
        }
    }

    @Override
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
