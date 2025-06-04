package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Poligono;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.servicios.interfaces.DegreeResult;
import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/ubicacion")
public final class UbicacionControllerREST {

    private final UbicacionService ubicacionService;

    public UbicacionControllerREST(UbicacionService ubicacionService){
        this.ubicacionService = ubicacionService;
    }


    @GetMapping
    public List<UbicacionDTO> getUbicaciones(@RequestParam(required = false) TipoUbicacion tipo) {
        var ubicaciones = switch (tipo) {
            case SANTUARIO -> ubicacionService.recuperarSantuarios();
            case CEMENTERIO -> ubicacionService.recuperarCementerios();
            case null -> ubicacionService.recuperarTodos();
        };
        return ubicaciones.stream().map(UbicacionDTO::desdeModelo).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UbicacionDTO> getUbicacionById(@PathVariable Long id) {
        Ubicacion ubicacion = ubicacionService.recuperar(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacion));
    }


    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTO>> getEspiritusEn (@PathVariable Long id){
        List<Espiritu> espiritus = ubicacionService.espiritusEn(id);
        List<EspirituDTO> espirituDTOS = espiritus.stream().map(EspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok(espirituDTOS);
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<List<MediumDTO>> getMediumsSinEspiritusEn (@PathVariable Long id){
        List<Medium> mediumsSinEspiritusEn = ubicacionService.mediumsSinEspiritusEn(id);
        List<MediumDTO> mediumDTOS = mediumsSinEspiritusEn.stream().map(MediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(mediumDTOS);
    }

    @PostMapping
    public ResponseEntity<UbicacionDTO> guardarUbicacion(@Valid @RequestBody CreateUbicacionDTO dto) {
        Ubicacion ubicacion = dto.aModelo();
        Ubicacion creada = ubicacionService.guardar(ubicacion, ubicacion.getPoligono());
        URI location = URI.create("/ubicacion/" + creada.getId());
        UbicacionDTO respuesta = UbicacionDTO.desdeModelo(creada);
        return ResponseEntity.created(location).body(respuesta);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UbicacionDTO> actualizarUbicacion(@PathVariable Long id, @Valid @RequestBody UpdateUbicacionDTO dto) {
        Ubicacion ubicacion = ubicacionService.recuperar(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));
        dto.actualizarModelo(ubicacion); // actualiza los atributos de Ubicación
        Ubicacion guardada = ubicacionService.actualizar(ubicacion);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(guardada));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/conexiones")
    public ResponseEntity<List<UbicacionDTO>> getConexiones(@PathVariable Long id) {
        List<Ubicacion> vecinos = ubicacionService.recuperarConexiones(id);

        List<UbicacionDTO> dtoVecinos = vecinos.stream()
                .map(UbicacionDTO::desdeModelo)
                .toList();

        return ResponseEntity.ok(dtoVecinos);
    }

    @PostMapping("/{idOrigen}/conectar/{idDestino}")
    public ResponseEntity<ConexionDTO> conectar(@PathVariable Long idOrigen, @PathVariable Long idDestino) {

        ubicacionService.conectar(idOrigen, idDestino);

        ConexionDTO dto = new ConexionDTO(idOrigen, idDestino, LocalDateTime.now());
        URI location = URI.create("/ubicacion/" + idOrigen + "/conexiones");

        return ResponseEntity.created(location).body(dto);
    }

    @GetMapping("/{idOrigen}/conectada/{idDestino}")
    public ResponseEntity<Map<String, Boolean>> estanConectadas(@PathVariable Long idOrigen, @PathVariable Long idDestino) {

        boolean conectado = ubicacionService.estanConectadas(idOrigen, idDestino);
        Map<String, Boolean> respuesta = Collections.singletonMap("conectadas", conectado);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/sobrecargadas")
    public ResponseEntity<List<UbicacionDTO>> getUbicacionesSobrecargadas(
            @RequestParam(name = "umbral", required = true) Integer umbralDeEnergia) {

        List<Ubicacion> lista = ubicacionService.ubicacionesSobrecargadas(umbralDeEnergia);
        List<UbicacionDTO> dtoLista = lista.stream()
                .map(UbicacionDTO::desdeModelo)
                .toList();

        return ResponseEntity.ok(dtoLista);
    }

    @GetMapping("/{idOrigen}/camino/{idDestino}")
    public ResponseEntity<List<UbicacionDTO>> caminoMasCorto(@PathVariable Long idOrigen, @PathVariable Long idDestino) {

        List<Ubicacion> caminoMasCorto = ubicacionService.caminoMasCorto(idOrigen, idDestino);

        List<UbicacionDTO> dtocaminoMasCorto = caminoMasCorto.stream()
                .map(UbicacionDTO::desdeModelo)
                .toList();

        return ResponseEntity.ok(dtocaminoMasCorto);
    }

    @PostMapping("/closeness")
    public ResponseEntity<List<ClosenessResultDTO>> closenessOf(@RequestBody IdsDTO idsDTO) {
        List<ClosenessResult> closeness = ubicacionService.closenessOf(idsDTO.ids());
        return ResponseEntity.ok(ClosenessResultDTO.desdeModelo(closeness));
    }

    @PostMapping("/degree")
    public ResponseEntity<List<DegreeResultDTO>> getDegreeResult(@RequestBody IdsDTO idsDTO) {
        List<DegreeResult> degree = ubicacionService.degreeOf(idsDTO.ids());
        return ResponseEntity.ok(DegreeResultDTO.desdeModelo(degree));
    }
}
