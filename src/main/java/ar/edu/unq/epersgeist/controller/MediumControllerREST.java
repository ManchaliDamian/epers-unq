package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mediums")
public class MediumControllerREST {

    private final MediumService mediumService;

    public MediumControllerREST(MediumService mediumService) {
        this.mediumService = mediumService;
    }

    @GetMapping
    public List<MediumDTO> recuperarTodos() {
        return mediumService.recuperarTodos()
                .stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> recuperar(@PathVariable Long id) {
        return mediumService.recuperar(id)
                .map(MediumDTO::desdeModelo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/espiritus")
    public ResponseEntity<List<EspirituDTO>> recuperarEspiritus(@PathVariable Long id, @RequestParam(required = false) String tipo) {
        return mediumService.recuperar(id)
                .map(m -> m.getEspiritus().stream()
                        .filter(e -> tipo == null || e.getTipo().equalsIgnoreCase(tipo))
                        .map(EspirituDTO::desdeModelo)
                        .collect(Collectors.toList()))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<MediumDTO> actualizar(@PathVariable Long id, @RequestBody MediumDTO mediumDTO) {
        if (!id.equals(mediumDTO.id())) {
            return ResponseEntity.badRequest().build();
        }

        Optional<Medium> existente = mediumService.recuperar(id);

        if (existente.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Medium medium = mediumDTO.aModelo();
        mediumService.guardar(medium);

        Medium mediumActualizado = mediumService.guardar(mediumDTO.aModelo());
        MediumDTO respuesta = MediumDTO.desdeModelo(mediumActualizado);

        return ResponseEntity.ok(respuesta);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        mediumService.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping
    public MediumDTO guardar(@RequestBody MediumDTO mediumDTO) {
        Medium guardado = mediumService.guardar(mediumDTO.aModelo());
        return MediumDTO.desdeModelo(guardado);
    }

    @PostMapping("/{id}/exorcizar/{mediumId}")
    public void exorcizar(@PathVariable Long id, @PathVariable Long mediumId) {
        mediumService.exorcizar(id, mediumId);
    }

    @PostMapping("/{id}/descansar")
    public void descansar(@PathVariable Long id) {
        mediumService.descansar(id);
    }

    @PostMapping("/{id}/invocar/{espirituId}")
    public void invocar(@PathVariable Long id, @PathVariable Long espirituId) {
        mediumService.invocar(id, espirituId);
    }

    @PostMapping("/{id}/mover/{ubicacionId}")
    public void mover(@PathVariable Long id, @PathVariable Long ubicacionId) {
        mediumService.mover(id, ubicacionId);
    }
}
