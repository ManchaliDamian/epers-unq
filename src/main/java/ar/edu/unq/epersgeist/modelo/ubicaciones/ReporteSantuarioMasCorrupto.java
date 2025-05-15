package ar.edu.unq.epersgeist.modelo.ubicaciones;

import ar.edu.unq.epersgeist.modelo.personajes.Medium;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReporteSantuarioMasCorrupto {

    private String nombreSantuario;
    private int totalDemonios;
    private int cantDemoniosLibres;
    private Medium mediumMayorDemoniacos;

    public ReporteSantuarioMasCorrupto(String nombreSantuario,int totalDemonios,int cantDemoniosLibres,Medium mediumMayorDemoniacos){
        this.nombreSantuario = nombreSantuario;
        this.totalDemonios = totalDemonios;
        this.cantDemoniosLibres = cantDemoniosLibres;
        this.mediumMayorDemoniacos = mediumMayorDemoniacos;
    }
}
