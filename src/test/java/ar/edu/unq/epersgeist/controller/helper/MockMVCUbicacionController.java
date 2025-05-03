package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.CreateUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class MockMVCUbicacionController {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (ServletException e) {
            throw e.getCause();
        }
    }

    private String getContentAsString(String url) throws Throwable {
        return performRequest(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }

    public Collection<Ubicacion> getUbicaciones() throws Throwable {
        var json = getContentAsString("/ubicacion");

        Collection<UbicacionDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, UbicacionDTO.class)
        );

        return dtos.stream().map(UbicacionDTO::aModelo).toList();
    }

    public Ubicacion getUbicacionById(Long ubicacionId) throws Throwable {
        var json = getContentAsString("/ubicacion/" + ubicacionId);

        var dto = objectMapper.readValue(json, UbicacionDTO.class);
        return dto.aModelo();
    }

    /* descomentar cuando aModelo este definido
    public Collection<Espiritu> getEspiritusEn(Long ubicacionId) throws Throwable {
        var json = getContentAsString("/ubicacion/" + ubicacionId + "/espiritus");

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
        );

        return dtos.stream().map(EspirituDTO::aModelo).toList();
    }
    */

    /* descomentar cuando mediumDTO este definido
    public Collection<Medium>  getMediumsSinEspiritusEn (Long ubicacionId) throws Throwable {
        var json = getContentAsString("/ubicacion/" + ubicacionId + "/mediumsSinEspiritus");

        Collection<EspirituDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MediumDTO.class)
        );

        return dtos.stream().map(MediumDTO::aModelo).toList();
    }
    */

    public Ubicacion guardarUbicacion(Ubicacion ubi, HttpStatus expectedStatus) throws Throwable {
        var dto = CreateUbicacionDTO.desdeModelo(ubi);
        var json = objectMapper.writeValueAsString(dto);

        var mvcResult = performRequest(MockMvcRequestBuilders
                .post("/ubicacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, Ubicacion.class);
    }


    public Ubicacion actualizarUbicacion(UbicacionDTO dto, Long ubicacionId, HttpStatus expectedStatus) throws Throwable {
        var json = objectMapper.writeValueAsString(dto);

        var mvcResult = performRequest(MockMvcRequestBuilders
                .put("/ubicacion/" + ubicacionId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, Ubicacion.class);
    }


    public void eliminar(Long ubicacionId) throws Throwable {
        performRequest(MockMvcRequestBuilders.delete("/ubicacion/" + ubicacionId))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
