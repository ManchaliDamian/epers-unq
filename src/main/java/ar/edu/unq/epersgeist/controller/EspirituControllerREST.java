package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/espiritu")
public final  class EspirituControllerREST {

    private final EspirituService espirituService;
    private final UbicacionService ubicacionService;

    public EspirituControllerREST(EspirituService espirituService, UbicacionService ubicacionService){
        this.espirituService = espirituService;
        this.ubicacionService = ubicacionService;
    }

    @PostMapping
    public ResponseEntity<EspirituDTO> createEspiritu(@Valid @RequestBody CreateEspirituDTO dto) {
        Ubicacion ubicacion = ubicacionService.recuperar(dto.ubicacionId())
                .orElseThrow(() -> new IllegalArgumentException("Ubicación no encontrada"));

        Espiritu espiritu = dto.aModelo(ubicacion);
        Espiritu creado = espirituService.guardar(espiritu);

        return ResponseEntity
                .created(URI.create("/espiritu/" + creado.getId()))
                .body(EspirituDTO.desdeModelo(creado));
    }

    @GetMapping("/all")
    public List<EspirituDTO> getAllEspiritus(){
        return espirituService.recuperarTodos().stream()
                 .map(EspirituDTO::desdeModelo).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> getEspirituById(@PathVariable Long id){
        Optional<Espiritu> espiritu = espirituService.recuperar(id);
        return espiritu.map(e -> ResponseEntity.ok(EspirituDTO.desdeModelo(e)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEspiritu(@PathVariable Long id){
        espirituService.eliminar(id);
        return ResponseEntity.ok().build();
    }

//    @PostMapping
//    public ResponseEntity<EspirituDTO>guardarEspiritu(@Valid @RequestBody EspirituDTO espirituDTO){
//        Espiritu espiritu = espirituDTO.aModelo();
//        espirituService.guardar(espiritu);
//        URI location = URI.create("/espiritu/" + espiritu.getId());
//        EspirituDTO respuesta = EspirituDTO.desdeModelo(espiritu);
//        return ResponseEntity.created(location).body(respuesta);
//    }


//Descomentar luego de tener a MediumDTO hecho
//    @GetMapping("/{idEspiritu}/conectar/{idMedium}")
//    public ResponseEntity<EspirituDTO> conectarEspiritu(@PathVariable Long idEspiritu,@PathVariable Long idMedium){
//        Medium medium = espirituService.conectar(idEspiritu,idMedium);
//        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
//    }
}
