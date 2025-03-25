CREATE TABLE IF NOT EXISTS espiritu (
    id SERIAL PRIMARY KEY,
    tipo varchar(128),
    nivel_de_conexion int,
    nombre varchar(128),
);