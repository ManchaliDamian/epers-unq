package ar.edu.unq.epersgeist.controller.helper;

import ar.edu.unq.epersgeist.controller.dto.CreateEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
import ar.edu.unq.epersgeist.modelo.Direccion;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MockMVCEspirituController {

    @Autowired private MockMvc mockMvc;

    @Autowired private ObjectMapper objectMapper;

    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
        try {
            return mockMvc.perform(requestBuilder);
        } catch (ServletException e) {
            throw e.getCause();
        }
    }


    private Long guardar(Espiritu espiritu, HttpStatus expectedStatus) throws Throwable {
        var createDto = CreateEspirituDTO.desdeModelo(espiritu);
        var json = objectMapper.writeValueAsString(createDto);

        var response = performRequest(MockMvcRequestBuilders.post("/espiritu")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse();

        String responseBody = response.getContentAsString();
        EspirituDTO responseDto = objectMapper.readValue(responseBody, EspirituDTO.class);
        Espiritu espirituCreado = responseDto.aModelo();

        return espirituCreado.getId();




//        String location = response.getHeader("Location");
//        if (location == null || !location.contains("/")) {
//            throw new IllegalStateException("No se pudo obtener el ID desde la cabecera Location");
//        }
//        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }


    public Long guardar(Espiritu espiritu) throws Throwable {
        return guardar(espiritu, HttpStatus.CREATED);
    }

    private String getContentAsString(String url) throws Throwable {
        return performRequest(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }


    public Collection<Espiritu> recuperarTodos() throws Throwable{
        var json = getContentAsString("/espiritu");

        Collection<EspirituDTO> dtos = objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
                );
    return dtos.stream().map(EspirituDTO::aModelo).toList();
    }

    public Espiritu recuperarEspiritu(Long espirituId) throws Throwable{

        var json = performRequest(MockMvcRequestBuilders.get("/espiritu/" + espirituId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
        var dto = objectMapper.readValue(json,EspirituDTO.class);
        return dto.aModelo();
    }


    public int espiritusDemoniacos(Direccion direccion, int pagina,
                                   int cantidadPorPagina, HttpStatus expectedStatus) throws Throwable {
        return performRequest(
                MockMvcRequestBuilders.get("/espiritu/demoniacos")
                        .param("direccion", direccion.name())
                        .param("pagina",   String.valueOf(pagina))
                        .param("cantidadPorPagina", String.valueOf(cantidadPorPagina))
        )
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn()
                .getResponse()
                .getStatus();
    }

    public void eliminar(Long espirituId) throws Throwable {
        performRequest(MockMvcRequestBuilders.delete("/espiritu/" + espirituId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }


    public void conectarAMedium(Long espirituId, Long mediumId) throws Throwable{
        mockMvc.perform(MockMvcRequestBuilders.put("/espiritu/" + espirituId + "/conectar/" + mediumId))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
