package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CreateUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.UpdateUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.modelo.exception.UbicacionNoEncontradaException;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
//import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    public List<UbicacionDTO> getUbicaciones(@RequestParam(required = false) String tipo) {
        Stream<Ubicacion> stream = ubicacionService.recuperarTodos().stream();

        if (tipo != null) {
            stream = stream.filter(ubicacion -> ubicacion.getTipo().equalsIgnoreCase(tipo));
        }

        return stream
                .map(UbicacionDTO::desdeModelo)
                .collect(Collectors.toList());
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
/*
    @GetMapping("/{id}/mediumsSinEspiritus")
    public ResponseEntity<List<MediumDTO>> getMediumsSinEspiritusEn (@PathVariable Long id){
        List<Medium> mediumsSinEspiritusEn = ubicacionService.mediumsSinEspiritusEn(id);
        List<MediumDTO> mediumDTOS = mediumsSinEspiritusEn.stream().map(MediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(mediumDTOS);
    }
*/
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

        Optional<Ubicacion> opt = ubicacionService.recuperar(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build(); // 404

        Ubicacion ubicacion = opt.get();
        dto.actualizarModelo(ubicacion); // actualiza los atributos

        Ubicacion guardada = ubicacionService.actualizar(ubicacion);

        return ResponseEntity.ok(UbicacionDTO.desdeModelo(guardada));
    }


    //DELETE handlers
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        ubicacionService.recuperar(id).orElseThrow(() -> new UbicacionNoEncontradaException(id));

        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
