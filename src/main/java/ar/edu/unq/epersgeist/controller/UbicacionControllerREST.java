package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
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
        Optional<Ubicacion> ubicacion = ubicacionService.recuperar(id);
        return ubicacion.map(ubi -> ResponseEntity.ok(UbicacionDTO.desdeModelo(ubi)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTO>> getEspiritusEn (@PathVariable Long id){
        List<Espiritu> espiritus = ubicacionService.espiritusEn(id);
        List<EspirituDTO> espirituDTOS = espiritus.stream().map(EspirituDTO::desdeModelo).toList();
        return ResponseEntity.ok(espirituDTOS);
    }
/*
    @GetMapping("/{id}/mediumSinEspiritus")
    public ResponseEntity<List<MediumDTO>> getMediumsSinEspiritusEn (@PathVariable Long id){
        List<Medium> mediumsSinEspiritusEn = ubicacionService.mediumsSinEspiritusEn(id);
        List<MediumDTO> mediumDTOS = mediumsSinEspiritusEn.stream().map(MediumDTO::desdeModelo).toList();
        return ResponseEntity.ok(mediumDTOS);
    }
*/
    //POST handlers
@PostMapping
public ResponseEntity<Void> guardarUbicacion(@Valid @RequestBody UbicacionDTO dto) {
    Ubicacion ubicacion = dto.aModelo();
    Ubicacion creada = ubicacionService.guardar(ubicacion);
    URI location = URI.create("/ubicacion/" + creada.getId());
    return ResponseEntity.created(location).build();
}

    //PUT handlers
    @PutMapping("/{id}")
    public ResponseEntity<Void> actualizarUbicacion(@PathVariable Long id, @Valid @RequestBody UbicacionDTO dto) {
        if (!id.equals(dto.id())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Ubicacion> existente = ubicacionService.recuperar(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Ubicacion ubicacion = dto.aModelo();
        ubicacionService.guardar(ubicacion);

        return ResponseEntity.ok().build();
    }

    //DELETE handlers
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id){
        Optional<Ubicacion> existente = ubicacionService.recuperar(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        ubicacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
