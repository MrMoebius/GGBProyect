-- ==============================================================================
-- ESQUEMA DE BASE DE DATOS: GIBER BAR
-- Optimizado para TiDB Cloud / MySQL 8
-- ==============================================================================

-- Desactivar chequeo de FKs para permitir borrado y creación en cualquier orden
SET FOREIGN_KEY_CHECKS = 0;

-- ------------------------------------------------------------------------------
-- LIMPIEZA (DROP TABLES)
-- ------------------------------------------------------------------------------
DROP TABLE IF EXISTS pagos_mesa;
DROP TABLE IF EXISTS peticiones_pago;
DROP TABLE IF EXISTS lineas_comanda;
DROP TABLE IF EXISTS comandas;
DROP TABLE IF EXISTS productos;
DROP TABLE IF EXISTS ludoteca_sesiones;
DROP TABLE IF EXISTS tarifas_ludoteca;
DROP TABLE IF EXISTS reservas_juegos;
DROP TABLE IF EXISTS juegos_copias;
DROP TABLE IF EXISTS juegos;
DROP TABLE IF EXISTS sesiones_mesa;
DROP TABLE IF EXISTS reservas_mesa;
DROP TABLE IF EXISTS mesas;
DROP TABLE IF EXISTS clientes;
DROP TABLE IF EXISTS empleados;
DROP TABLE IF EXISTS roles_empleado;

-- ------------------------------------------------------------------------------
-- CREACIÓN DE BASE DE DATOS
-- ------------------------------------------------------------------------------
CREATE DATABASE IF NOT EXISTS giber_bar
  CHARACTER SET utf8mb4
  COLLATE utf8mb4_general_ci;

USE giber_bar;

-- ==============================================================================
-- MÓDULO 1: USUARIOS Y CLIENTES
-- ==============================================================================

CREATE TABLE roles_empleado (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE empleados (
    id_empleado INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    telefono VARCHAR(20),
    password VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    fecha_ingreso DATE,
    estado VARCHAR(50) DEFAULT 'ACTIVO',
    CONSTRAINT fk_empleado_rol FOREIGN KEY (id_rol)
        REFERENCES roles_empleado(id_rol)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_empleados_email ON empleados(email);

CREATE TABLE clientes (
    id_cliente INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    email VARCHAR(150) UNIQUE,
    telefono VARCHAR(20),
    password VARCHAR(255),
    fecha_alta DATE,
    notas TEXT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_clientes_email ON clientes(email);

-- ==============================================================================
-- MÓDULO 2: GESTIÓN DE SALA
-- ==============================================================================

CREATE TABLE mesas (
    id_mesa INT AUTO_INCREMENT PRIMARY KEY,
    numero_mesa INT NOT NULL,
    capacidad INT NOT NULL,
    zona VARCHAR(50),
    estado VARCHAR(30) DEFAULT 'LIBRE',
    UNIQUE KEY uk_mesas_numero (numero_mesa)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE reservas_mesa (
    id_reserva INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT,
    id_mesa INT,
    fecha_hora_inicio DATETIME NOT NULL,
    fecha_hora_fin DATETIME,
    num_personas INT,
    id_juego_deseado INT,
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    notas TEXT,
    CONSTRAINT fk_reserva_cliente FOREIGN KEY (id_cliente)
        REFERENCES clientes(id_cliente)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_reserva_mesa FOREIGN KEY (id_mesa)
        REFERENCES mesas(id_mesa)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_reservas_inicio ON reservas_mesa(fecha_hora_inicio);
CREATE INDEX idx_reservas_estado ON reservas_mesa(estado);

CREATE TABLE sesiones_mesa (
    id_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_mesa INT NOT NULL,
    id_reserva INT,
    id_empleado_apertura INT,
    fecha_hora_apertura DATETIME NOT NULL,
    fecha_hora_cierre DATETIME,
    estado VARCHAR(30) DEFAULT 'ABIERTA',
    CONSTRAINT fk_sesion_mesa FOREIGN KEY (id_mesa)
        REFERENCES mesas(id_mesa)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,
    CONSTRAINT fk_sesion_reserva FOREIGN KEY (id_reserva)
        REFERENCES reservas_mesa(id_reserva)
        ON UPDATE CASCADE
        ON DELETE SET NULL,
    CONSTRAINT fk_sesion_empleado FOREIGN KEY (id_empleado_apertura)
        REFERENCES empleados(id_empleado)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_sesion_mesa_estado ON sesiones_mesa(id_mesa, estado);

-- ==============================================================================
-- MÓDULO 3: LUDOTECA
-- ==============================================================================

CREATE TABLE juegos (
    id_juego INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    min_jugadores INT,
    max_jugadores INT,
    duracion_media_min INT,
    complejidad VARCHAR(50),
    genero VARCHAR(100),
    idioma VARCHAR(50),
    descripcion TEXT,
    ubicacion VARCHAR(100),
    recomendado_dos_jugadores BOOLEAN DEFAULT FALSE,
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- FK Circular: Reservas -> Juegos
ALTER TABLE reservas_mesa
ADD CONSTRAINT fk_reserva_juego_deseado FOREIGN KEY (id_juego_deseado)
REFERENCES juegos(id_juego) ON UPDATE CASCADE ON DELETE SET NULL;

CREATE TABLE juegos_copias (
    id_copia INT AUTO_INCREMENT PRIMARY KEY,
    id_juego INT NOT NULL,
    codigo_interno VARCHAR(50) UNIQUE,
    estado VARCHAR(30) DEFAULT 'DISPONIBLE',
    CONSTRAINT fk_copia_juego FOREIGN KEY (id_juego)
        REFERENCES juegos(id_juego)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_copias_juego ON juegos_copias(id_juego);

-- ==============================================================================
-- MÓDULO 4: OPERATIVA JUEGOS Y TARIFAS
-- ==============================================================================

CREATE TABLE reservas_juegos (
    id_reserva_juego INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT NOT NULL,
    id_copia INT NOT NULL,
    hora_inicio DATETIME,
    hora_fin DATETIME,
    estado VARCHAR(30) DEFAULT 'ACTIVA',
    CONSTRAINT fk_resjuego_sesion FOREIGN KEY (id_sesion)
        REFERENCES sesiones_mesa(id_sesion)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_resjuego_copia FOREIGN KEY (id_copia)
        REFERENCES juegos_copias(id_copia)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_resjuego_sesion ON reservas_juegos(id_sesion);

CREATE TABLE tarifas_ludoteca (
    id_tarifa INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tramo VARCHAR(50) NOT NULL,
    edad_min INT NOT NULL DEFAULT 0,
    edad_max INT NOT NULL DEFAULT 99,
    precio DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    descripcion VARCHAR(255)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE TABLE ludoteca_sesiones (
    id_ludoteca_sesion INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT NOT NULL,
    num_adultos INT DEFAULT 0,
    num_ninos_6_13 INT DEFAULT 0,
    num_ninos_0_5 INT DEFAULT 0,
    importe_total DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    id_comanda_ludoteca INT DEFAULT NULL,
    fecha_calculo TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    notas TEXT,
    CONSTRAINT fk_ludoteca_sesion FOREIGN KEY (id_sesion)
        REFERENCES sesiones_mesa(id_sesion)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    UNIQUE KEY uk_ludoteca_sesion_unica (id_sesion)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_ludoteca_fecha ON ludoteca_sesiones(fecha_calculo);

-- ==============================================================================
-- MÓDULO 5: RESTAURACIÓN
-- ==============================================================================

CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    categoria VARCHAR(50) NOT NULL,
    precio DECIMAL(10,2) NOT NULL,
    activo BOOLEAN DEFAULT TRUE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_productos_categoria ON productos(categoria);

CREATE TABLE comandas (
    id_comanda INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT NOT NULL,
    id_empleado INT,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    estado VARCHAR(30) DEFAULT 'ENVIADA',
    notas TEXT,
    CONSTRAINT fk_comanda_sesion FOREIGN KEY (id_sesion)
        REFERENCES sesiones_mesa(id_sesion)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_comanda_empleado FOREIGN KEY (id_empleado)
        REFERENCES empleados(id_empleado)
        ON UPDATE CASCADE
        ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_comandas_sesion ON comandas(id_sesion);

CREATE TABLE lineas_comanda (
    id_linea INT AUTO_INCREMENT PRIMARY KEY,
    id_comanda INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL DEFAULT 1,
    precio_unitario_historico DECIMAL(10,2) NOT NULL,
    estado_preparacion VARCHAR(30) DEFAULT 'PENDIENTE',
    notas_chef VARCHAR(255),
    CONSTRAINT fk_linea_comanda FOREIGN KEY (id_comanda)
        REFERENCES comandas(id_comanda)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    CONSTRAINT fk_linea_producto FOREIGN KEY (id_producto)
        REFERENCES productos(id_producto)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_lineas_estado ON lineas_comanda(estado_preparacion);

-- ==============================================================================
-- MÓDULO 6: PAGOS
-- ==============================================================================

CREATE TABLE peticiones_pago (
    id_peticion INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT NOT NULL,
    metodo_preferido VARCHAR(20),
    atendida BOOLEAN DEFAULT FALSE,
    fecha_peticion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_peticion_sesion FOREIGN KEY (id_sesion)
        REFERENCES sesiones_mesa(id_sesion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_peticion_sesion_atendida ON peticiones_pago(id_sesion, atendida);

CREATE TABLE pagos_mesa (
    id_pago INT AUTO_INCREMENT PRIMARY KEY,
    id_sesion INT NOT NULL,
    fecha_hora DATETIME NOT NULL,
    importe DECIMAL(10,2) NOT NULL,
    metodo_pago VARCHAR(50),
    estado VARCHAR(30) DEFAULT 'PENDIENTE',
    CONSTRAINT fk_pago_sesion FOREIGN KEY (id_sesion)
        REFERENCES sesiones_mesa(id_sesion)
        ON UPDATE CASCADE
        ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

CREATE INDEX idx_pagos_sesion ON pagos_mesa(id_sesion);
CREATE INDEX idx_pagos_estado ON pagos_mesa(estado);

-- ==============================================================================
-- AJUSTES FINALES Y DATOS
-- ==============================================================================

-- FK Circular: Ludoteca -> Comandas
ALTER TABLE ludoteca_sesiones
ADD CONSTRAINT fk_ludoteca_comanda FOREIGN KEY (id_comanda_ludoteca)
    REFERENCES comandas(id_comanda)
    ON UPDATE CASCADE
    ON DELETE SET NULL;

-- Datos Iniciales: Tarifas
INSERT INTO tarifas_ludoteca (nombre_tramo, edad_min, edad_max, precio, descripcion) VALUES
('ADULTO', 14, 150, 2.50, 'Tarifa estándar para adultos y mayores de 13 años'),
('NINO_6_13', 6, 13, 1.50, 'Tarifa reducida para niños de 6 a 13 años'),
('NINO_0_5', 0, 5, 0.00, 'Gratis para menores de 6 años');

-- Reactivar chequeo de FKs
SET FOREIGN_KEY_CHECKS = 1;
