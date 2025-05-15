package ar.edu.unq.epersgeist.modelo.enums;

public enum TipoUbicacion {
    CEMENTERIO,
    SANTUARIO;

    public boolean equalsIgnoreCase(String tipo) {
        return this.name().equalsIgnoreCase(tipo);
    }
}
