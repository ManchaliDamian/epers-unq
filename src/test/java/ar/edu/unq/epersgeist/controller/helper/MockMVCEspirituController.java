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


    private Long guardarEspiritu(Espiritu espiritu, HttpStatus expectedStatus) throws Throwable {
        var dto = CreateEspirituDTO.desdeModelo(espiritu);
        var json = objectMapper.writeValueAsString(dto);

        var response = performRequest(MockMvcRequestBuilders.post("/espiritu/all" )
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
                .andReturn().getResponse();

        String responseBody = response.getContentAsString();
        return objectMapper.readValue(responseBody, Espiritu.class).getId();

//        String location = response.getHeader("Location");
//        if (location == null || !location.contains("/")) {
//            throw new IllegalStateException("No se pudo obtener el ID desde la cabecera Location");
//        }
//        return Long.parseLong(location.substring(location.lastIndexOf("/") + 1));
    }


    public Long guardarEspiritu(Espiritu espiritu) throws Throwable {
        return guardarEspiritu(espiritu, HttpStatus.CREATED);
    }

    private String getContentAsString(String url) throws Throwable {
        return performRequest(MockMvcRequestBuilders.get(url))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();
    }


    public Collection<Espiritu> recuperarTodos() throws Throwable{
        var json = getContentAsString("/espiritu/all");

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

    public void conectarAMedium(Long espirituId, Long mediumId) throws Throwable{
        mockMvc.perform(MockMvcRequestBuilders.put("/espiritu/" + espirituId + "/conectar/" + mediumId))
               .andExpect(MockMvcResultMatchers.status().isOk());
    }

    public Collection<Espiritu> getEspiritusDemoniacosPaginados(Direccion dir, int pag, int cantPags) throws Throwable{

        var json = performRequest(
                     MockMvcRequestBuilders
                         .get("/espiritu/espiritusDemoniacos" + pag + "/" + cantPags + "/ " + dir.name()
                     )
                   )
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<EspirituDTO> dtos = objectMapper.readValue(json,
                objectMapper.getTypeFactory().constructCollectionType(List.class,EspirituDTO.class)
        );

        return dtos.stream().map(EspirituDTO::aModelo).collect(Collectors.toList());

    }

}
