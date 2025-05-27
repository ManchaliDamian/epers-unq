package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.enums.TipoUbicacion;
import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.servicios.interfaces.ClosenessResult;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

import java.net.URI;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/ubicacion")
public final class UbicacionControllerREST {

    private final UbicacionService ubicacionService;

    public UbicacionControllerREST(UbicacionService ubicacionService){
        this.ubicacionService = ubicacionService;
    }


    // GET handlers
    @GetMapping
    public List<UbicacionDTO> getUbicaciones(@RequestParam(required = false) TipoUbicacion tipo) {
        var ubicaciones = switch (tipo) {
            case SANTUARIO -> ubicacionService.recuperarSantuarios();
            case CEMENTERIO -> ubicacionService.recuperarCementerios();
            case null -> ubicacionService.recuperarTodos();
        };
        return ubicaciones.stream().map(UbicacionDTO::desdeModelo).toList();
    }

    @GetMapping("/closeness")
    public ResponseEntity<List<ClosenessResult>> closenessOf(@RequestBody List<Long> ids) {
        return ResponseEntity.ok(ubicacionService.closenessOf(ids));
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

    //POST handlers
    @PostMapping
    public ResponseEntity<UbicacionDTO> guardarUbicacion(@Valid @RequestBody CreateUbicacionDTO dto) {
        Ubicacion ubicacion = dto.aModelo();
        Ubicacion creada = ubicacionService.guardar(ubicacion);
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


    //DELETE handlers
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
