package ar.edu.unq.epersgeist.modelo.ubicacion;

public enum TipoUbicacion {
    CEMENTERIO,
    SANTUARIO;

    public boolean equalsIgnoreCase(String tipo) {
        return this.name().equalsIgnoreCase(tipo);
    }
}
