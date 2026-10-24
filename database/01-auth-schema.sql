USE bd_quiro_t3;
CREATE TABLE tbl_persona (
    id_persona INT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    email VARCHAR(50) NOT NULL UNIQUE
);
CREATE TABLE tbl_usuario (
    persona_id INT NOT NULL PRIMARY KEY,
    clave VARCHAR(255) NOT NULL,
    cargo VARCHAR(20) NOT NULL,
    CONSTRAINT fk_usuario_persona FOREIGN KEY (persona_id)
        REFERENCES tbl_persona(id_persona)
);

