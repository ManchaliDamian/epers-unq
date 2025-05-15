package ar.edu.unq.epersgeist.modelo.enums;

public enum TipoEspiritu {
    ANGELICAL,
    DEMONIACO;

    public boolean equalsIgnoreCase(String tipo) {
        return this.name().equalsIgnoreCase(tipo);
    }
}
