package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
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

    public EspirituControllerREST(EspirituService espirituService){
        this.espirituService = espirituService;
    }

    @GetMapping("/all")
    public List<EspirituDTO> getAllEspiritus(){
        return espirituService.recuperarTodos().stream()
                 .map(EspirituDTO::desdeModelo).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> getEspirituById(@PathVariable Long id){
        Optional<Espiritu> espiritu = espirituService.recuperar(id);
        //No sé qué tan bien estaría esto, pero no rompe.
        return espiritu.map(value -> ResponseEntity.ok(EspirituDTO.desdeModelo(value))).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/espirituEliminado/{id}")
    public ResponseEntity<String> deleteEspiritu(@PathVariable Long id){
        espirituService.eliminar(id);
        return ResponseEntity.ok().body("El espiritu ha sido eliminado con exito");
    }

//    @PostMapping
//    public ResponseEntity<EspirituDTO>guardarEspiritu(@Valid @RequestBody EspirituDTO espirituDTO){
//        Espiritu espiritu = espirituDTO.aModelo();
//        espirituService.guardar(espiritu);
//        URI location = URI.create("/espiritu/" + espiritu.getId());
//        EspirituDTO respuesta = EspirituDTO.desdeModelo(espiritu);
//        return ResponseEntity.created(location).body(respuesta);
//    }


    @PostMapping
    public void createEspiritu(@RequestBody EspirituDTO espirituDTO ){
        espirituService.guardar(espirituDTO.aModelo());
    }

//Descomentar luego de tener a MediumDTO hecho
//    @GetMapping("/{idEspiritu}/conectar/{idMedium}")
//    public ResponseEntity<EspirituDTO> conectarEspiritu(@PathVariable Long idEspiritu,@PathVariable Long idMedium){
//        Medium medium = espirituService.conectar(idEspiritu,idMedium);
//        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
//    }
}
