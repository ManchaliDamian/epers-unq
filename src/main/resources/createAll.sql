CREATE TABLE IF NOT EXISTS espiritu (
    id SERIAL PRIMARY KEY,
    tipo varchar(128) NOT NULL,
    nivel_de_conexion int NOT NULL DEFAULT 0,
    nombre varchar(128) NOT NULL
);
