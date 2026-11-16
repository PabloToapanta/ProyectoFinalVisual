CREATE DATABASE IF NOT EXISTS SistemaEstudiantil
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_spanish_ci;

USE SistemaEstudiantil;

CREATE TABLE IF NOT EXISTS estudiante (
    id       INT          NOT NULL AUTO_INCREMENT,
    cedula   VARCHAR(10)  NOT NULL,
    nombres  VARCHAR(100) NOT NULL,
    carrera  VARCHAR(100) NOT NULL,
    estado   ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    PRIMARY KEY (id),
    UNIQUE (cedula)
);

CREATE TABLE IF NOT EXISTS tarjeta (
    id            INT         NOT NULL AUTO_INCREMENT,
    codigo_rfid   VARCHAR(32) NOT NULL,
    id_estudiante INT         NOT NULL,
    estado        ENUM('ACTIVO', 'INACTIVO') NOT NULL DEFAULT 'ACTIVO',
    PRIMARY KEY (id),
    UNIQUE (codigo_rfid),
    INDEX idx_tarjeta_estudiante (id_estudiante),
    CONSTRAINT fk_tarjeta_estudiante
        FOREIGN KEY (id_estudiante) REFERENCES estudiante (id)
);

CREATE TABLE IF NOT EXISTS acceso (
    id            INT         NOT NULL AUTO_INCREMENT,
    id_tarjeta    INT         NULL,
    id_estudiante INT         NULL,
    codigo_leido  VARCHAR(32) NOT NULL,
    fecha_hora    DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resultado     ENUM('AUTENTICADO', 'DENEGADO') NOT NULL,
    PRIMARY KEY (id),
    INDEX idx_acceso_fecha (fecha_hora),
    INDEX idx_acceso_estudiante_fecha (id_estudiante, fecha_hora),
    CONSTRAINT fk_acceso_tarjeta
        FOREIGN KEY (id_tarjeta) REFERENCES tarjeta (id),
    CONSTRAINT fk_acceso_estudiante
        FOREIGN KEY (id_estudiante) REFERENCES estudiante (id)
);

CREATE TABLE IF NOT EXISTS administrador (
    id       INT          NOT NULL AUTO_INCREMENT,
    usuario  VARCHAR(50)  NOT NULL,
    clave    VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE (usuario)
);

-- Cuenta inicial para ingresar al sistema por primera vez.
INSERT IGNORE INTO administrador (usuario, clave)
VALUES ('admin', 'admin123');
