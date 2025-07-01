package ar.edu.unq.epersgeist.persistencia.DAOs.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOFirestore;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituFirestoreDTO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import java.util.List;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ExecutionException;
@Component
public class EspirituDAOFirestoreImpl implements EspirituDAOFirestore {

    private static final String COLL = "estadisticas_espiritus";

    private final Firestore firestore;

    public EspirituDAOFirestoreImpl(Firestore firestore) {
        this.firestore = firestore;
    }

    public void save(EspirituFirestoreDTO e) throws InterruptedException, ExecutionException {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(e.getIdSQL().toString());

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
    public EspirituFirestoreDTO actualizar(EspirituFirestoreDTO e) {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(e.getIdSQL().toString());

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

    @Override
    public void enriquecer(Espiritu espiritu) {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(espiritu.getId().toString());

        try {
            DocumentSnapshot snapshot = doc.get().get();

            if (!snapshot.exists()) return;

            if (snapshot.contains("vida")) {
                espiritu.setVida(snapshot.getLong("vida").intValue());
            }
            if (snapshot.contains("ganadas")) {
                espiritu.setBatallasGanadas(snapshot.getLong("ganadas").intValue());
            }
            if (snapshot.contains("perdidas")) {
                espiritu.setBatallasPerdidas(snapshot.getLong("perdidas").intValue());
            }
            if (snapshot.contains("jugadas")) {
                espiritu.setBatallasJugadas(snapshot.getLong("jugadas").intValue());
            }
            if (snapshot.contains("ataque")) {
                espiritu.setAtaque(snapshot.getLong("ataque").intValue());
            }
            if (snapshot.contains("defensa")) {
                espiritu.setDefensa(snapshot.getLong("defensa").intValue());
            }

        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error al enriquecer desde Firebase", e);
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
