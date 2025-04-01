CREATE TABLE IF NOT EXISTS espiritu (
    id SERIAL PRIMARY KEY,
    tipo varchar(128) NOT NULL,
    nivel_de_conexion int NOT NULL DEFAULT 0 CHECK (nivel_de_conexion >= 0 AND nivel_de_conexion <= 100),
    nombre varchar(128) NOT NULL
);
