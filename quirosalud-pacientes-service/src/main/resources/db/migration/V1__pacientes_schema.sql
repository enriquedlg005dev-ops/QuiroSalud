CREATE TABLE tb_paciente (
    dni VARCHAR(8) NOT NULL PRIMARY KEY,
    nombres VARCHAR(50) NOT NULL,
    apellidos VARCHAR(50) NOT NULL,
    fecha_nac DATE NOT NULL,
    sexo VARCHAR(1) NOT NULL,
    estado_civil VARCHAR(1) NOT NULL,
    ocupacion VARCHAR(50),
    num_hijos INT,
    telefono VARCHAR(15),
    email VARCHAR(50),
    direccion VARCHAR(100) NOT NULL,
    cod_distrito VARCHAR(5) NOT NULL
);
CREATE TABLE tb_historia_clinica (
    dni VARCHAR(8) NOT NULL PRIMARY KEY,
    motivo_consulta VARCHAR(500),
    escala_dolor INT,
    antecedente_trauma VARCHAR(500),
    antecedente_cirugia VARCHAR(500),
    tiene_asma BOOLEAN NOT NULL DEFAULT FALSE,
    tiene_diabetes BOOLEAN NOT NULL DEFAULT FALSE,
    tiene_alergias BOOLEAN NOT NULL DEFAULT FALSE,
    observaciones VARCHAR(1000),
    CONSTRAINT fk_historia_paciente FOREIGN KEY (dni) REFERENCES tb_paciente(dni) ON DELETE CASCADE
);
