package ar.edu.unq.epersgeist.persistencia.DAOs.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOFirestore;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.Callable;
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
    public void crear(Espiritu espiritu) {
        Map<String,Object> data = this.buildEspirituData(espiritu);
        this.runBlocking(() -> firestore.collection(COLL)
                .document(espiritu.getId().toString())
                .set(data)); // set() sin merge para partir de cero
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu) {
        Map<String, Object> data = this.buildEspirituData(espiritu);

        this.runBlocking(() ->
            firestore.collection(COLL)
                    .document(espiritu.getId().toString())
                    .set(data)
        );

        return espiritu;
    }

    @Override
    public void eliminar(Long id) {
        this.runBlocking(() ->
                firestore.collection(COLL).document(id.toString()).delete()
        );
    }

    @Override
    public void enriquecer(Espiritu espiritu) {
        DocumentReference doc = firestore
                .collection(COLL)
                .document(espiritu.getId().toString());

        DocumentSnapshot snapshot = runBlocking(doc::get);
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

    }

    @Override
    public void deleteAll() {
        CollectionReference collection = firestore.collection(COLL);
        List<QueryDocumentSnapshot> docs = runBlocking(collection::get).getDocuments();
        WriteBatch batch = firestore.batch();
        docs.forEach(d -> batch.delete(d.getReference()));
        runBlocking(batch::commit);
    }

    @Override
    public List<Espiritu> recuperarTodosMayorVida(int vida) {
        // Construye la consulta: colección, ordenar por vida, filtrar por vida > vidaMinima
        Query query = firestore.collection(COLL)
                .whereGreaterThan("vida", vida)
                .orderBy("vida", Query.Direction.DESCENDING);

        QuerySnapshot querySnapshot = runBlocking(() -> query.get());
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();


        List<Espiritu> espiritusEncontrados = new ArrayList<>();
        for (DocumentSnapshot document : documents) {
            // ... (resto de la lógica para reconstruir el objeto Espiritu) ...
            String tipoEspirituStr = document.getString("tipo");
            String nombre = document.getString("nombre");
            Long id = Long.valueOf(document.getId());

            Espiritu espiritu = null;
            if ("ANGELICAL".equals(tipoEspirituStr)) {
                espiritu = new EspirituAngelical(nombre, new Santuario("Dummy", 10) {
                });
            } else if ("DEMONIACO".equals(tipoEspirituStr)) {
                espiritu = new EspirituDemoniaco(nombre, new Santuario("Dummy", 10));
            }

            if (espiritu != null) {
                espiritu.setId(id);
                this.enriquecer(espiritu);
                espiritusEncontrados.add(espiritu);
            }
        }
        return espiritusEncontrados;
    }

    // -- HELPERS --

    private Map<String, Object> buildEspirituData(Espiritu espiritu) {
        Map<String, Object> data = new HashMap<>();
        data.put("nombre",   espiritu.getNombre());
        data.put("ganadas",  espiritu.getBatallasGanadas());
        data.put("perdidas", espiritu.getBatallasPerdidas());
        data.put("jugadas",  espiritu.getBatallasJugadas());
        data.put("vida",     espiritu.getVida());
        data.put("ataque",   espiritu.getAtaque());
        data.put("defensa",  espiritu.getDefensa());
        data.put("tipo", espiritu.getTipo());
        return data;
    }

    private <T> T runBlocking(Callable<ApiFuture<T>> action) {
        try {
            return action.call().get(); // bloquea y espera resultado
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Interrupción al acceder a Firestore", ie);
        } catch (Exception e) {
            throw new RuntimeException("Error al acceder a Firestore", e);
        }
    }
}
