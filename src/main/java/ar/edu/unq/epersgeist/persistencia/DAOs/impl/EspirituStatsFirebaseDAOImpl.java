package ar.edu.unq.epersgeist.persistencia.DAOs.impl;

import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituStatsFirebaseDAO;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
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
                "jugadas", 0
        );
        // set() sin merge para asegurar que partimos de un estado limpio(sobrescribe o crea)
        doc.set(init).get();
    }
}
