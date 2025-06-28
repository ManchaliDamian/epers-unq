package ar.edu.unq.epersgeist.persistencia.DAOs.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituFirebaseDAO;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
@Component
public class EspirituFirebaseDAOImpl implements EspirituFirebaseDAO {

    private static final String COLLECTION_NAME = "espiritus";
    @Autowired
    private Firestore firestore;
    @Override
    public void save(Espiritu espiritu) throws InterruptedException, ExecutionException {
        String documentId = String.valueOf(espiritu.getId());

        if (documentId == null || documentId.isEmpty()) {
            // Si el ID es nulo o vacío, Firestore generará uno automáticamente.
            // Sin embargo, para mantener coherencia con otras BDs, es mejor tener un ID definido.
            // Podrías generar un UUID aquí si no tienes un ID de SQL/Mongo para el Espiritu de dominio.
            // O, idealmente, el servicio le pasa el ID de SQL/JPA después de guardarlo allí.
            throw new IllegalArgumentException("El ID del espíritu no puede ser nulo o vacío para guardar en Firestore.");
        }


        Map<String, Object> dataToSave = new HashMap<>();
        dataToSave.put("id", documentId);
        dataToSave.put("nombre", espiritu.getNombre()); // Asegúrate de que Espiritu.getNombre() exista
        // Agrega otras propiedades del espíritu que quieras guardar en Firestore
        // dataToSave.put("tipo", espiritu.getTipo());
        // dataToSave.put("descripcion", espiritu.getDescripcion());

        ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                .document(documentId)
                .set(dataToSave); // .set() sobrescribe o crea

        // El .get() lanza InterruptedException y ExecutionException, así que el llamador debe manejarlas.
        // Aquí simplemente lanzamos las excepciones hacia arriba.
        future.get();
    }
}
