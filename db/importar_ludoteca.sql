-- ==============================================================================
-- SCRIPT DE IMPORTACIÓN DE INVENTARIO DE JUEGOS
-- ==============================================================================

-- Desactivar chequeo de FKs para permitir limpieza
SET FOREIGN_KEY_CHECKS = 0;

-- Limpiar tablas de juegos para evitar duplicados
-- Usamos WHERE 1=1 para evitar advertencias de seguridad "Unsafe Query" en IntelliJ
DELETE FROM juegos_copias WHERE 1=1;
DELETE FROM juegos WHERE 1=1;

ALTER TABLE juegos AUTO_INCREMENT = 1;
ALTER TABLE juegos_copias AUTO_INCREMENT = 1;

-- Reactivar chequeo
SET FOREIGN_KEY_CHECKS = 1;

-- Inicio de transacción
START TRANSACTION;

-- ------------------------------------------------------------------------------
-- Procesamiento fila a fila del Excel
-- ------------------------------------------------------------------------------

-- 1. ¡De qué vas! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('¡De qué vas!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 2. ¡Ding! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('¡Ding!', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 3. ¡TETO! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('¡TETO!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 4. ¡Toma 6! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('¡Toma 6!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 5. 100 km Mario Kart (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('100 km Mario Kart', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 6. 5 Minute dungeon (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('5 Minute dungeon', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 7. 5 Torres (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('5 Torres', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 8. 7 Wonders Architects (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('7 Wonders Architects', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 9. 7 Wonders Dice (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('7 Wonders Dice', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 10. 7 Wonders Duel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('7 Wonders Duel', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 11. Adaptoid 24 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Adaptoid 24', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 12. Agrícola (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Agrícola', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 13. Alcachofas ¡No, gracias! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Alcachofas ¡No, gracias!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 14. All Aboard! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('All Aboard!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 15. Amigos de Mierda 2 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Amigos de Mierda 2', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 16. Ankh (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ankh', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 17. Anne Bonny's Malediction (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Anne Bonnys Malediction', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 18. Arcs (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Arcs', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 19. Arkham Noir (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Arkham Noir', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 20. Arshmallows (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Arshmallows', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 21. Ascension (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ascension', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 22. Aventureros al tren el tren fantasma (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Aventureros al tren el tren fantasma', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 23. Aventureros al tren Europa (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Aventureros al tren Europa', 'VERDE', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 24. Aventureros al tren Londres (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Aventureros al tren Londres', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 25. Aztec (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Aztec', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 26. Azul (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Azul', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 27. Azul Duel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Azul Duel', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 28. Bang (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Bang', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 29. Bang!  El Juego de Dados (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Bang!  El Juego de Dados', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 30. Baraja de Poker (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Baraja de Poker', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 31. Baraja Española (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Baraja Española', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 32. Beer Mug Dice (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Beer Mug Dice', 'VERDE', FALSE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 33. Betty Botter Bought Some Butter (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Betty Botter Bought Some Butter', 'VERDE', FALSE, 'PASILLO', 'JUEGO EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 34. Big Two (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Big Two', 'AMARILLO', FALSE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 35. Billabong Race (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Billabong Race', 'AMARILLO', FALSE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 36. Black Party "Cabaret Mortal" (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Black Party "Cabaret Mortal"', 'AMARILLO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 37. Black Party "Descansa en paz, Sherlock" (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Black Party "Descansa en paz, Sherlock"', 'AMARILLO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 38. Black Party "La muerte de El Padrino" (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Black Party "La muerte de El Padrino"', 'AMARILLO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 39. Blind Business (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Blind Business', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 40. Blokus (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Blokus', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 41. Blood on the Clocktower (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Blood on the Clocktower', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 42. Camel Up: Juego de Cartas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Camel Up: Juego de Cartas', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 43. Campeones (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Campeones', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 44. Captain Flip (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Captain Flip', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 45. Carcassone (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Carcassone', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 46. Carcassonne básico (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Carcassonne básico', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 47. Cascadia (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cascadia', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 48. Castillos de Borgoña (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Castillos de Borgoña', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 49. Castle Combo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Castle Combo', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 50. Catan (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Catan', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 51. Catan Plus (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Catan Plus', 'AMARILLO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 52. Caverna (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Caverna', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 53. Century Ruta especias (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Century Ruta especias', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 54. Cerveceros (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cerveceros', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 55. Ciudad de Puntos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ciudad de Puntos', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 56. Ciudadelas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ciudadelas', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 57. Ciudadelas Nueva Versión (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ciudadelas Nueva Versión', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 58. Clank! y Expansiones (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Clank! y Expansiones', 'AMARILLO', TRUE, 'PASILLO', 'UNA CON EXPANSIÓN Y OTRA SIN');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 59. Claro (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Claro', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 60. CloudAge (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('CloudAge', 'AMARILLO', FALSE, 'PASILLO', 'INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 61. Codex Naturalis (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Codex Naturalis', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 62. Código Secreto (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Código Secreto', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 63. Código secreto - Imágenes (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Código secreto - Imágenes', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 64. Código secreto Duel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Código secreto Duel', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 65. Colt Express (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Colt Express', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 66. Cookies Battle (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cookies Battle', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 67. Copa Grial (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Copa Grial', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 68. Cortex Challenge (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cortex Challenge', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 69. Coup X (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Coup X', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 70. Crisis (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Crisis', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 71. Crown of Emara (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Crown of Emara', 'ROJO', FALSE, 'SALÓN', 'INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 72. Cubirds (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cubirds', 'VERDE', TRUE, 'ENTRADA', 'INDEPENDIENTE IDIOMA PERO REGLAS INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 73. Cursed? (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cursed?', 'VERDE', FALSE, 'ENTRADA', 'SOLITARIO, SIN CAJA, EN BOLSA');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 74. Cyclades (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cyclades', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 75. Dados Poker (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dados Poker', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 76. Damas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Damas', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 77. Danger (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Danger', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 78. Darwins Journey (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Darwins Journey', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 79. Deck 52 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Deck 52', 'VERDE', FALSE, 'ENTRADA', 'SOLITARIO, SIN CAJA, EN BOLSA');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 80. Días de Radio (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Días de Radio', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 81. Dice Hospital (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dice Hospital', 'AMARILLO', TRUE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 82. Dice throne 1 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dice throne 1', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 83. Dice throne 2 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dice throne 2', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 84. Dice throne 3 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dice throne 3', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 85. Dice throne 4 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dice throne 4', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 86. Dino Fiesta (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dino Fiesta', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 87. Dioses + Expa (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dioses + Expa', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 88. Dixit (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dixit', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 89. Dobble (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dobble', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 90. Dobble One Piece (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dobble One Piece', 'VERDE', TRUE, 'ENTRADA', 'INSTRUCCIONES EN JAPONÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 91. Dobble Star Wars Mandalorian (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dobble Star Wars Mandalorian', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 92. Dominion, 2ª ed. (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dominion, 2ª ed.', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 93. Dominó (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dominó', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 94. Dorfromantik: Sakura (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dorfromantik: Sakura', 'AMARILLO', TRUE, 'NOVEDAD', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 95. Dos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dos', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 96. Dungeon Fighter (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dungeon Fighter', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 97. Dungeon Riders (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dungeon Riders', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 98. Dungeon Roll (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dungeon Roll', 'VERDE', TRUE, 'ENTRADA', 'ITALIANO');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 99. Dungeons & Dragons "Comienza la aventura" (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Dungeons & Dragons "Comienza la aventura"', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 100. El Embustero (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('El Embustero', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 101. El Espía (Que se perdió) (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('El Espía (Que se perdió)', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 102. El Señor de los Anillos: Juego de Bazas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('El Señor de los Anillos: Juego de Bazas', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 103. El Símbolo Arcano (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('El Símbolo Arcano', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 104. El susurro de las hojas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('El susurro de las hojas', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 105. Emotify (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Emotify', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 106. Ensalada de Puntos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ensalada de Puntos', 'VERDE', FALSE, 'ENTRADA', 'CAJA NEGRA, SIN INSTRUCCIONES');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 107. Estimator (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Estimator', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 108. Exploding Kittens (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Exploding Kittens', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 109. Faraway (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Faraway', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 110. Faraway Expansión Subterráneos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Faraway Expansión Subterráneos', 'AMARILLO', FALSE, 'ENTRADA', 'DENTRO DEL FARAWAY');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 111. Fertility (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Fertility', 'AMARILLO', TRUE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 112. Festival (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Festival', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 113. First to Worst (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('First to Worst', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 114. Flamecraft (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Flamecraft', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 115. Fliptown (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Fliptown', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 116. Friegas tú (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Friegas tú', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 117. Galaxy Trucker (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Galaxy Trucker', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 118. Gestrudis (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Gestrudis', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 119. Gloria mundi (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Gloria mundi', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 120. Great Western Trail: El Paso (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Great Western Trail: El Paso', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 121. Hanabi (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Hanabi', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 122. Happy Mochi (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Happy Mochi', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 123. Harmonies (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Harmonies', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 124. Heat + Expansión (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Heat + Expansión', 'AMARILLO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 125. Heroes de Tenefyr + Expansión (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Heroes de Tenefyr + Expansión', 'AMARILLO', TRUE, 'PASILLO', 'COGER TAPETE QUE VIENE APARTE');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 126. High Society (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('High Society', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 127. Hilo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Hilo', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 128. Hitster (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Hitster', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 129. Hitster Bingo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Hitster Bingo', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 130. Holawakas de Oromo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Holawakas de Oromo', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 131. Hombres Lobo de Castronegro (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Hombres Lobo de Castronegro', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 132. ikonikus (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('ikonikus', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 133. Incomodos invitados 2 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Incomodos invitados 2', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 134. INTELECT (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('INTELECT', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 135. Isla Calavera (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Isla Calavera', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 136. Its a Wonderful World + Expansión (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Its a Wonderful World + Expansión', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 137. Jalape-No! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jalape-No!', 'VERDE', FALSE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 138. Jamaica (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jamaica', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 139. Jenga (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jenga', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 140. Jenga (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jenga', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 141. Jenga (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jenga', 'VERDE', TRUE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 142. Jungo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jungo', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 143. Kaboom Universe (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Kaboom Universe', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 144. Kharnage + Expansión (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Kharnage + Expansión', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 145. King of Tokyo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('King of Tokyo', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 146. La Cima (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('La Cima', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 147. La Isla Prohibida (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('La Isla Prohibida', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 148. La tripulación (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('La tripulación', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 149. La Tripulación 2: Misión Mar Profundo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('La Tripulación 2: Misión Mar Profundo', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 150. Lanterns (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Lanterns', 'VERDE', FALSE, 'PASILLO', 'INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 151. Lanza y Pasa (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Lanza y Pasa', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 152. Libro de Hechizos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Libro de Hechizos', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 153. Linkto (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Linkto', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 154. Listos, Listas y Listakens - Clásico (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Listos, Listas y Listakens - Clásico', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 155. Listos, Listas y Listakens - Junior (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Listos, Listas y Listakens - Junior', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 156. Listos, Listas y Listakens - Party (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Listos, Listas y Listakens - Party', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 157. Llama Kadabra (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Llama Kadabra', 'VERDE', FALSE, 'ENTRADA', 'CAJA BLANCA, COGER CARTÓN DE ABAJO');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 158. Llama Party (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Llama Party', 'VERDE', FALSE, 'ENTRADA', 'CAJA LUIGI, SIN INSTRUCCIONES');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 159. Lost Cities (Roll & Write) (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Lost Cities (Roll & Write)', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 160. Love Letter (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Love Letter', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 161. Love Letter Marvel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Love Letter Marvel', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 162. Luna de Miel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Luna de Miel', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 163. M: Cumplida (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('M: Cumplida', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 164. M: Rescate (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('M: Rescate', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 165. Magic the Gathering : Assasins Creed Starter Kit (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Magic the Gathering : Assasins Creed Starter Kit', 'AMARILLO', TRUE, 'ENTRADA', 'CAJA VERDE, SIN INSTRUCCIONES');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 166. Magic the Gathering : Final Fantasy Starter Kit (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Magic the Gathering : Final Fantasy Starter Kit', 'AMARILLO', TRUE, 'ENTRADA', 'CAJA BLANCA, INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 167. Maldita Palabrita (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Maldita Palabrita', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 168. Mariposas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mariposas', 'AMARILLO', FALSE, 'PASILLO', 'INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 169. Marshalls (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Marshalls', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 170. Marvel Remix (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Marvel Remix', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 171. Marvel United (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Marvel United', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 172. Marvel Villainous: Infinite Power (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Marvel Villainous: Infinite Power', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 173. Marvel Zombies (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Marvel Zombies', 'AMARILLO', TRUE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 174. Mazescape: Ariadne (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mazescape: Ariadne', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 175. Mensaje Final (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mensaje Final', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 176. Mesozooic (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mesozooic', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 177. Mind Bug (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mind Bug', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 178. Mind Up! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mind Up!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 179. Mineros del imperio (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mineros del imperio', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 180. Mini Rogue (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mini Rogue', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 181. Mino Dice (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mino Dice', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 182. Misión Secreta (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Misión Secreta', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 183. MismaMente (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('MismaMente', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 184. MLEM Agencia Espacial (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('MLEM Agencia Espacial', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 185. Moirai (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Moirai', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 186. Momiji (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Momiji', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 187. Monolith Arena (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monolith Arena', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 188. Monopoly Fallout (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monopoly Fallout', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 189. Monopoly Fornite (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monopoly Fornite', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 190. Monopoly Knockout (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monopoly Knockout', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 191. Monopoly: Dungeons and Dragons (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monopoly: Dungeons and Dragons', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 192. Monster Flip (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monster Flip', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 193. Monster Lands (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Monster Lands', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 194. Morada Maldita (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Morada Maldita', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 195. Not Alone (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Not Alone', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 196. Oak (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Oak', 'ROJO', FALSE, 'SALÓN', 'INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 197. Oceanos de papel + Expansion (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Oceanos de papel + Expansion', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 198. Oceanos de papel Expansion Papelillos a la Mar (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Oceanos de papel Expansion Papelillos a la Mar', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 199. Oh Captain! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Oh Captain!', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 200. One Deck Dungeon (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('One Deck Dungeon', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 201. One Deck Dungeon: Bosque Sombrío (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('One Deck Dungeon: Bosque Sombrío', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 202. ONIX (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('ONIX', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 203. Ouch (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ouch', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 204. Palacio de Jabba (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Palacio de Jabba', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 205. Pan Am (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pan Am', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 206. Pandemic (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pandemic', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 207. Parchís + Ajedrez (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Parchís + Ajedrez', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 208. Paris: La Cité de la Lumière (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Paris: La Cité de la Lumière', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 209. Patchwork San Valentin (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Patchwork San Valentin', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 210. Pelo Pata Popó (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pelo Pata Popó', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 211. Pelusas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pelusas', 'VERDE', FALSE, 'ENTRADA', 'CAJA NEGRA, SIN INSTRUCCIONES');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 212. Pelusas Revolution (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pelusas Revolution', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 213. Pescacartas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pescacartas', 'VERDE', FALSE, 'ENTRADA', 'CAJA BLANCA');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 214. Piko Piko (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Piko Piko', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 215. Piña Coladice (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Piña Coladice', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 216. Pixel Tactics (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pixel Tactics', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 217. Pixies (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pixies', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 218. Pixies Expansión Flower Power (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pixies Expansión Flower Power', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 219. Plata (3 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Plata', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-3'), 'DISPONIBLE');

-- 220. Plenus (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Plenus', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 221. Pocket Invaders (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pocket Invaders', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 222. Pokémon TCG Mazos Inicio (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Pokémon TCG Mazos Inicio', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 223. Póker a Voces (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Póker a Voces', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 224. Póker de Villanos Batman El juego de cartas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Póker de Villanos Batman El juego de cartas', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 225. Póker Set (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Póker Set', 'VERDE', FALSE, 'PASILLO', 'ARRIBA DEL MUEBLE');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 226. Polilla Tramposa (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Polilla Tramposa', 'VERDE', FALSE, 'ENTRADA', 'CAJA VERDE');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 227. PoPCoM (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('PoPCoM', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 228. Port Royal Deluxe (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Port Royal Deluxe', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 229. Radlands (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Radlands', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 230. Rage (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Rage', 'VERDE', FALSE, 'ENTRADA', 'CAJA VERDE');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 231. Ragemore (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ragemore', 'AMARILLO', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 232. Ramen! Ramen! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ramen! Ramen!', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 233. Red 7 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Red 7', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 234. Red Cathedral (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Red Cathedral', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 235. Reinos Rodados (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Reinos Rodados', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 236. Res arcana (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Res arcana', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 237. Rhino Hero (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Rhino Hero', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 238. Ringmaster ¡Pasen y Vean! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ringmaster ¡Pasen y Vean!', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 239. Riverboat (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Riverboat', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 240. Rivet Wars (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Rivet Wars', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 241. Roll to the Top (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Roll to the Top', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 242. Rummy PRO (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Rummy PRO', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 243. Run, Mule, Run! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Run, Mule, Run!', 'VERDE', FALSE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 244. Saber Ancestral (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Saber Ancestral', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 245. Saboteur (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Saboteur', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 246. Sagrada (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Sagrada', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 247. Scope Stalingrad (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Scope Stalingrad', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 248. Scotland Yard (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Scotland Yard', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 249. Semevá Labola (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Semevá Labola', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 250. Sequence Junior (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Sequence Junior', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 251. Sequence Travel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Sequence Travel', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 252. Shadows: Amsterdam (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Shadows: Amsterdam', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 253. Silly Cow (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Silly Cow', 'VERDE', FALSE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 254. Skull King 2023 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Skull King 2023', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 255. Slide Quest (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Slide Quest', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 256. Smart 10 (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Smart 10', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 257. Smart 10 Expansión Música (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Smart 10 Expansión Música', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 258. Smart 10 Expansión Superhéroes (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Smart 10 Expansión Superhéroes', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 259. Smart 10 Expansión Videojuegos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Smart 10 Expansión Videojuegos', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 260. Splash (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Splash', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 261. Splendor Duel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Splendor Duel', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 262. Splendor Marvel (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Splendor Marvel', 'VERDE', TRUE, 'ENTRADA', 'SIN CAJA, EN BOLSA');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 263. Spoilers: Travel Edition (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Spoilers: Travel Edition', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 264. Star Wars Bounty (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Star Wars Bounty', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 265. Star Wars Deckbuilding Clone Wars (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Star Wars Deckbuilding Clone Wars', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 266. Story Island (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Story Island', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 267. Strike Harry Potter (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Strike Harry Potter', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 268. Stubborn Mules (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Stubborn Mules', 'VERDE', TRUE, 'PASILLO', 'INSTRUCCIONES EN INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 269. Sunrise Sunset (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Sunrise Sunset', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 270. Super Zings (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Super Zings', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 271. Survive the Island (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Survive the Island', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 272. Sushi Go Party! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Sushi Go Party!', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 273. Taco Gato Cabra Pizza (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Taco Gato Cabra Pizza', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 274. Taco, Gato Waterproof (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Taco, Gato Waterproof', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 275. Talisman (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Talisman', 'AMARILLO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 276. Ten (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Ten', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 277. Terraforming Dados (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Terraforming Dados', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 278. The Last Lighthouse (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('The Last Lighthouse', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 279. The Lie (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('The Lie', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 280. The Mind (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('The Mind', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 281. The White Castle (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('The White Castle', 'ROJO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 282. This War of Mine (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('This War of Mine', 'ROJO', TRUE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 283. Tichu (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Tichu', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 284. Tier List (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Tier List', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 285. Timeline: Música & Cine (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Timeline: Música & Cine', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 286. Tira del Hilo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Tira del Hilo', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 287. Torre Ciberdiabólica (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Torre Ciberdiabólica', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 288. Tortilla de Patatas (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Tortilla de Patatas', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 289. Toy Battle (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Toy Battle', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 290. Tranvía Mortal (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Tranvía Mortal', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 291. Trio (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Trio', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 292. UFO Galaxies (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('UFO Galaxies', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 293. Unánimo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Unánimo', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 294. Unánimo Party (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Unánimo Party', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 295. Under Evil (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Under Evil', 'ROJO', FALSE, 'SALÓN', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 296. Unmatched: Battle of Legends (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Unmatched: Battle of Legends', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 297. UNO (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('UNO', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 298. UNO Flex (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('UNO Flex', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 299. UNO No Mercy (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('UNO No Mercy', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 300. UNO: Master of the Universe (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('UNO: Master of the Universe', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 301. Unrest (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Unrest', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 302. Upples (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Upples', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 303. Valle de la Eternidad (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Valle de la Eternidad', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 304. Vegetable stock (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Vegetable stock', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 305. Vertellis (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Vertellis', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 306. Virus Marvel (2 copias)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Virus Marvel', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-2'), 'DISPONIBLE');

-- 307. Virus Roll and Write (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Virus Roll and Write', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 308. Virus! + Expansión (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Virus! + Expansión', 'VERDE', FALSE, 'ENTRADA', 'CARTAS SEPARADAS EN LAS CAJAS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 309. Walking on the Moon (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Walking on the Moon', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 310. Welcome To coleccionista (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Welcome To coleccionista', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 311. Wendigo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Wendigo', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 312. Whale Riders (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Whale Riders', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 313. Whistle Mountain (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Whistle Mountain', 'ROJO', FALSE, 'SALÓN', 'INGLÉS');
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 314. Wild Space (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Wild Space', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 315. Winter (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Winter', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 316. Wok And Roll (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Wok And Roll', 'VERDE', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 317. Wonderful Kingdom (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Wonderful Kingdom', 'AMARILLO', TRUE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 318. Yokai Pagoda (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Yokai Pagoda', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 319. Yokai Sketch (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Yokai Sketch', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 320. Zombie Kittens (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Zombie Kittens', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 321. Las Vegas Royale (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Las Vegas Royale', 'VERDE', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 322. La Cuenta (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('La Cuenta', 'VERDE', FALSE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 323. Panda Spin (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Panda Spin', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 324. Maravillas del Mundo (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Maravillas del Mundo', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 325. Munchkin (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Munchkin', 'AMARILLO', FALSE, 'PASILLO', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 326. Jaipur (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Jaipur', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 327. Can't Stop (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Cant Stop', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 328. Guerra de Calderos (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Guerra de Calderos', 'VERDE', TRUE, 'ENTRADA', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 329. Mysterium Park (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Mysterium Park', 'VERDE', FALSE, 'NOVEDAD', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 330. Everest Go! (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Everest Go!', 'VERDE', FALSE, 'NOVEDAD', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

-- 331. Speed Monsters (1 copia)
INSERT INTO juegos (nombre, complejidad, recomendado_dos_jugadores, ubicacion, descripcion) VALUES ('Speed Monsters', 'VERDE', TRUE, 'NOVEDAD', NULL);
SET @id = LAST_INSERT_ID();
INSERT INTO juegos_copias (id_juego, codigo_interno, estado) VALUES (@id, CONCAT('JD-', @id, '-1'), 'DISPONIBLE');

COMMIT;
