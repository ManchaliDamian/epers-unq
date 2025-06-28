package ar.edu.unq.epersgeist.persistencia.repositories.impl;

import ar.edu.unq.epersgeist.exception.BadRequest.CoordenadaFueraDeAreaException;
import ar.edu.unq.epersgeist.exception.NotFound.EspirituNoEncontradoException;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituAngelical;
import ar.edu.unq.epersgeist.modelo.personajes.EspirituDemoniaco;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOSQL;
import ar.edu.unq.epersgeist.persistencia.DAOs.EspirituDAOMongo;
import ar.edu.unq.epersgeist.persistencia.DAOs.PoligonoDAO;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituMongoDTO;
import ar.edu.unq.epersgeist.persistencia.DTOs.ubicacion.PoligonoMongoDTO;
import ar.edu.unq.epersgeist.persistencia.repositories.interfaces.EspirituRepository;
import ar.edu.unq.epersgeist.persistencia.DTOs.personajes.EspirituJPADTO;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.EspirituMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.Firestore;

import com.google.cloud.firestore.WriteResult;
import org.hibernate.Hibernate;
import ar.edu.unq.epersgeist.persistencia.repositories.mappers.UbicacionMapper;
import java.util.concurrent.ExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class EspirituRepositoryImpl implements EspirituRepository {

    private EspirituDAOSQL espirituDAOSQL;
    private EspirituDAOMongo espirituDAOMongo;
    private EspirituMapper mapperE;
    private UbicacionMapper mapperU;
    private EspirituMapper mapper;
    private PoligonoDAO poligonoDAOMongo;
    @Autowired
    private Firestore firestore;

    public EspirituRepositoryImpl(EspirituDAOSQL espirituDAOSQL, EspirituDAOMongo espirituDAOMongo, EspirituMapper mapperE, PoligonoDAO poligonoDAOMongo,UbicacionMapper mapperU){
        this.espirituDAOSQL = espirituDAOSQL;
        this.espirituDAOMongo = espirituDAOMongo;
        this.poligonoDAOMongo = poligonoDAOMongo;
        this.mapperE = mapperE;
        this.mapperU = mapperU;
    }

    @Override
    public Espiritu guardar(Espiritu espiritu, Coordenada coordenada) {
        GeoJsonPoint punto = new GeoJsonPoint(coordenada.getLongitud(), coordenada.getLatitud());

        Optional<PoligonoMongoDTO> poligonoOpt = poligonoDAOMongo.findByPoligonoGeoIntersectsAndUbicacionId(punto, espiritu.getUbicacion().getId());
        if (poligonoOpt.isEmpty()) {
            throw new CoordenadaFueraDeAreaException("coordenada no valida");
        }
        EspirituJPADTO jpa = mapperE.toJpa(espiritu);
        jpa = espirituDAOSQL.save(jpa);

        EspirituMongoDTO mongoDTO = mapperE.toMongo(jpa, coordenada);
        espirituDAOMongo.save(mongoDTO);
        try {
            String COLLECTION_NAME = "espiritus";

            // Convertir el ID de Long a String para usarlo en Firestore
            String espirituId = String.valueOf(jpa.getId()); // ¡Modificación aquí!

            // Crea un mapa con los datos a guardar.
            Map<String, Object> dataToSave = new HashMap<>();
            dataToSave.put("nombre", espiritu.getNombre()); // Asume que Espiritu tiene getNombre()
            dataToSave.put("id", espirituId); // Para que el ID esté también dentro del documento

            // Puedes agregar más propiedades aquí si lo deseas
            // dataToSave.put("tipo", espiritu.getTipo());
            // dataToSave.put("fechaCreacion", new java.util.Date()); // Ejemplo de fecha

            // Crea o actualiza un documento en la colección "espiritus" con el ID del espíritu.
            ApiFuture<WriteResult> future = firestore.collection(COLLECTION_NAME)
                    .document(espirituId)
                    .set(dataToSave);

            WriteResult result = future.get(); // Esta línea ahora está dentro del try-catch
            System.out.println("Espíritu guardado en Firestore en: " + result.getUpdateTime());

        } catch (InterruptedException e) {
            // Manejo de la interrupción del hilo (ej. si el servidor se detiene)
            Thread.currentThread().interrupt(); // Restaura el estado de interrupción
            System.err.println("La operación de Firestore fue interrumpida: " + e.getMessage());
            // Considera si quieres que esta interrupción detenga el proceso o simplemente lo loggee.
            // Si la interrupción significa que la operación no se completó, podrías lanzar una RuntimeException
            // para reflejar un fallo en el guardado completo del espíritu.
            // throw new RuntimeException("Error: Operación de Firestore interrumpida", e);
        } catch (ExecutionException e) {
            // Manejo de excepciones que ocurrieron durante la ejecución asíncrona de Firestore
            System.err.println("Error al guardar el espíritu en Firestore: " + e.getCause().getMessage());
            // Es buena práctica envolver y lanzar una excepción más específica de tu dominio
            // o una RuntimeException si este fallo debe impedir la finalización del guardado.
            // throw new RuntimeException("Error al guardar en Firebase: " + e.getCause().getMessage(), e.getCause());
        }
        // --- FIN: Guardar en Firebase Firestore ---
        return mapperE.toDomain(jpa);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu) {
        if (espiritu.getId() == null) {
            throw new IllegalArgumentException("El espiritu debe estar persistido");
        }
        EspirituJPADTO dto = actualizarEspirituJPA(espiritu);
        return mapperE.toDomain(dto);
    }

    @Override
    public Espiritu actualizar(Espiritu espiritu, Coordenada coordenada) {
        if (espiritu.getId() == null) {
            throw new IllegalArgumentException("El espiritu debe estar persistido");
        }
        EspirituJPADTO dto = actualizarEspirituJPA(espiritu);

        espirituDAOMongo.deleteByIdSQL(espiritu.getId());

        EspirituMongoDTO mongoDTO = mapperE.toMongo(dto, coordenada);
        espirituDAOMongo.save(mongoDTO);
        return mapperE.toDomain(dto);
    }

    private EspirituJPADTO actualizarEspirituJPA(Espiritu espiritu) {
        espirituDAOSQL.findById(espiritu.getId())
                .filter(u -> !u.isDeleted())
                .orElseThrow(() -> new EspirituNoEncontradoException(espiritu.getId()));

        EspirituJPADTO dto = mapperE.toJpa(espiritu);
        espirituDAOSQL.save(dto);
        return dto;
    }

    @Override
    public void eliminarFisicoEnMongoSiExiste(Long id) {
        Optional<EspirituMongoDTO> mongoDTO = espirituDAOMongo.findByIdSQL(id);
        mongoDTO.ifPresent(espirituDAOMongo::delete);
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return mapperE.toDomainList(this.espirituDAOSQL.recuperarTodos());
    }

    @Override
    public Optional<Espiritu> recuperar(Long espirituId) {
        return this.espirituDAOSQL.findById(espirituId).map(espirituJPADTO -> {
            EspirituJPADTO realJPA = (EspirituJPADTO) Hibernate.unproxy(espirituJPADTO);
            return mapperE.toDomain(realJPA);
        });

    }

    @Override
    public Optional<Coordenada> recuperarCoordenada(Long espirituId) {
        return espirituDAOMongo.findByIdSQL(espirituId).map(espirituMongoDTO -> mapperE.toCoordenada(espirituMongoDTO));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemonios() {
        return mapperE.toDomainListDemoniaco(this.espirituDAOSQL.recuperarDemonios());
    }

    @Override
    public List<EspirituAngelical> recuperarAngeles() {
        return mapperE.toDomainListAngelical(this.espirituDAOSQL.recuperarAngeles());
    }

    @Override
    public Optional<Espiritu> recuperarEliminado(Long id) {
        return this.espirituDAOSQL.recuperarEliminado(id).map(espirituJPADTO -> mapperE.toDomain(espirituJPADTO));
    }

    @Override
    public List<Espiritu> recuperarTodosLosEliminados() {
        return mapperE.toDomainList(this.espirituDAOSQL.recuperarTodosLosEliminados());
    }

    @Override
    public List<EspirituAngelical> recuperarAngelesDe(Long mediumId) {
        return mapperE.toDomainListAngelical(this.espirituDAOSQL.recuperarAngelesDe(mediumId));
    }

    @Override
    public List<EspirituDemoniaco> recuperarDemoniosDe(Long mediumId) {
        return mapperE.toDomainListDemoniaco(this.espirituDAOSQL.recuperarDemoniosDe(mediumId));
    }

    @Override
    public List<Espiritu> recuperarDemoniacosPaginados(Pageable pageable) {
        return mapperE.toDomainList(this.espirituDAOSQL.recuperarDemoniacosPaginados(pageable));
    }
    @Override
    public Optional<Double> distanciaA(Double longitud, Double latitud, Long idEspirituSQL) {
        return espirituDAOMongo.distanciaA(longitud,latitud,idEspirituSQL);
    }
    @Override
    public void deleteAll(){
        this.espirituDAOSQL.deleteAll();
        this.espirituDAOMongo.deleteAll();
    }
}
