//package ar.edu.unq.epersgeist.controller.helper;
//
//import ar.edu.unq.epersgeist.controller.dto.EspirituDTO;
//import ar.edu.unq.epersgeist.controller.dto.UbicacionDTO;
//import ar.edu.unq.epersgeist.controller.dto.CreateUbicacionDTO;
//import ar.edu.unq.epersgeist.controller.dto.UpdateUbicacionDTO;
//import ar.edu.unq.epersgeist.modelo.personajes.Espiritu;
//import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.servlet.ServletException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//import java.util.Collection;
//import java.util.List;
//
//@Component
//public class MockMVCUbicacionController {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    public ResultActions performRequest(MockHttpServletRequestBuilder requestBuilder) throws Throwable {
//        try {
//            return mockMvc.perform(requestBuilder);
//        } catch (ServletException e) {
//            throw e.getCause();
//        }
//    }
//
//    private String getContentAsString(String url) throws Throwable {
//        return performRequest(MockMvcRequestBuilders.get(url))
//                .andExpect(MockMvcResultMatchers.status().isOk())
//                .andReturn().getResponse().getContentAsString();
//    }
//
//    public Collection<Ubicacion> getUbicaciones() throws Throwable {
//        var json = getContentAsString("/ubicacion");
//
//        Collection<UbicacionDTO> dtos = objectMapper.readValue(
//                json,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, UbicacionDTO.class)
//        );
//
//        return dtos.stream().map(UbicacionDTO::aModelo).toList();
//    }
//
//    public Ubicacion getUbicacionById(Long ubicacionId) throws Throwable {
//        var json = getContentAsString("/ubicacion/" + ubicacionId);
//
//        var dto = objectMapper.readValue(json, UbicacionDTO.class);
//        return dto.aModelo();
//    }
//
//
//    public Collection<Espiritu> getEspiritusEn(Long ubicacionId) throws Throwable {
//        var json = getContentAsString("/ubicacion/" + ubicacionId + "/espiritus");
//
//        Collection<EspirituDTO> dtos = objectMapper.readValue(
//                json,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, EspirituDTO.class)
//        );
//
//        return dtos.stream().map(EspirituDTO::aModelo).toList();
//    }
//
//
//    /* descomentar cuando mediumDTO este definido
//    public Collection<Medium>  getMediumsSinEspiritusEn (Long ubicacionId) throws Throwable {
//        var json = getContentAsString("/ubicacion/" + ubicacionId + "/mediumsSinEspiritus");
//
//        Collection<EspirituDTO> dtos = objectMapper.readValue(
//                json,
//                objectMapper.getTypeFactory().constructCollectionType(List.class, MediumDTO.class)
//        );
//
//        return dtos.stream().map(MediumDTO::aModelo).toList();
//    }
//    */
//
//    public <T> T guardarUbicacion(CreateUbicacionDTO dto,  Class<T> cls) throws Throwable {
//        return this.guardarUbicacion(dto, HttpStatus.CREATED, cls);
//    }
//
//    public <T> T guardarUbicacion(CreateUbicacionDTO dto, HttpStatus expectedStatus, Class<T> cls) throws Throwable {
//        var json = objectMapper.writeValueAsString(dto);
//
//        var mvcResult = performRequest(MockMvcRequestBuilders
//                .post("/ubicacion")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
//                .andReturn();
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        return objectMapper.readValue(responseBody, cls);
//    }
//
//    public <T> T actualizarUbicacion(UpdateUbicacionDTO dto, Long ubicacionId, Class<T> cls) throws Throwable {
//        return this.actualizarUbicacion(dto, ubicacionId, HttpStatus.OK, cls);
//    }
//
//    public <T> T actualizarUbicacion(UpdateUbicacionDTO dto, Long ubicacionId, HttpStatus expectedStatus, Class<T> cls) throws Throwable {
//        var json = objectMapper.writeValueAsString(dto);
//
//        var mvcResult = performRequest(MockMvcRequestBuilders
//                .put("/ubicacion/" + ubicacionId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(json))
//                .andExpect(MockMvcResultMatchers.status().is(expectedStatus.value()))
//                .andReturn();
//
//        String responseBody = mvcResult.getResponse().getContentAsString();
//        return objectMapper.readValue(responseBody, cls);
//    }
//
//
//    public void eliminar(Long ubicacionId) throws Throwable {
//        performRequest(MockMvcRequestBuilders.delete("/ubicacion/" + ubicacionId))
//                .andExpect(MockMvcResultMatchers.status().isNoContent());
//    }
//}
