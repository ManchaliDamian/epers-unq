package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.EspirituControllerREST;
//import ar.edu.unq.epersgeist.controller.MediumControllerREST;
import ar.edu.unq.epersgeist.controller.UbicacionControllerREST;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@Configuration
@ComponentScan(basePackages = {"ar.edu.unq.epersgeist"})
public class ControllerTestConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean // pasarle como parámetro mediumControllerRest luego
    // Spring va a intentar hacer autowiring de toda dependencia que declaremos como parametro en el metodo del Bean.
    public MockMvc mockMvc(EspirituControllerREST espirituControllerREST, UbicacionControllerREST ubicacionControllerREST) {
        return MockMvcBuilders.standaloneSetup(espirituControllerREST, ubicacionControllerREST).build();
    }
}