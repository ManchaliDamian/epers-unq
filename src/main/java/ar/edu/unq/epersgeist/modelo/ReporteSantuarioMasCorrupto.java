package ar.edu.unq.epersgeist.modelo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReporteSantuarioMasCorrupto {

    private String nombreSantuario;
    private int totalDemonios;
    private int demoniosLibres;
    private Medium mediumMayorDemoniacos;

    public ReporteSantuarioMasCorrupto(String nombreSantuario,int totalDemonios,int demoniosLibres,Medium mediumMayorDemoniacos){
        this.nombreSantuario = nombreSantuario;
        this.totalDemonios = totalDemonios;
        this.demoniosLibres = demoniosLibres;
        this.mediumMayorDemoniacos = mediumMayorDemoniacos;
    }
}
