package ar.edu.unq.epersgeist.controller.helper;
import ar.edu.unq.epersgeist.controller.dto.*;
import ar.edu.unq.epersgeist.modelo.Espiritu;
import ar.edu.unq.epersgeist.modelo.Medium;
import ar.edu.unq.epersgeist.modelo.Ubicacion;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import java.util.Collection;
import java.util.List;


@Component
public class MockMVCMediumController {

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

    public <T> T guardarUbicacion(CreateUbicacionDTO dto, Class<T> cls) throws Throwable {
        var json = objectMapper.writeValueAsString(dto);

        var mvcResult = performRequest(MockMvcRequestBuilders
                .post("/ubicacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, cls);
    }

    public <T> T guardarMedium(CreateMediumDTO dto, Class<T> cls) throws Throwable {
        var json = objectMapper.writeValueAsString(dto);

        var mvcResult = performRequest(MockMvcRequestBuilders
                .post("/medium")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        return objectMapper.readValue(responseBody, cls);
    }

    public Collection<Medium> recuperarTodos() throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/medium"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        Collection<MediumDTO> dtos = objectMapper.readValue(
                json,
                objectMapper.getTypeFactory().constructCollectionType(List.class, MediumDTO.class)
        );

        return dtos.stream().map(MediumDTO::aModelo).toList();
    }

    public void eliminar(Long mediumId) throws Throwable {
        performRequest(MockMvcRequestBuilders.delete("/medium/" + mediumId))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    public Medium actualizarMedium(Long mediumId, UpdateMediumDTO dto) throws Throwable {
        var json = objectMapper.writeValueAsString(dto);

        var mvcResult = performRequest(MockMvcRequestBuilders
                .put("/medium/" + mediumId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString();
        var mediumDTO = objectMapper.readValue(responseBody, MediumDTO.class);
        return mediumDTO.aModelo();
    }

    public int exorcizar(Long mediumEmisorId, Long mediumReceptorId) throws Throwable {
        performRequest(MockMvcRequestBuilders.post("/medium/" + mediumEmisorId + "/exorcizar/" + mediumReceptorId))
                .andExpect(MockMvcResultMatchers.status().isOk());
        return HttpStatus.OK.value();
    }

    public int descansar(Long mediumId) throws Throwable {
        performRequest(MockMvcRequestBuilders.post("/medium/" + mediumId + "/descansar"))
                .andExpect(MockMvcResultMatchers.status().isOk());
        return HttpStatus.OK.value();
    }

    public Medium getMediumById(Long mediumId) throws Throwable {
        var json = performRequest(MockMvcRequestBuilders.get("/medium/" + mediumId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        var dto = objectMapper.readValue(json, MediumDTO.class);
        return dto.aModelo();
    }
}
