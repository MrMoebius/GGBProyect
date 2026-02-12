# ============================================
# Stage 1: Build con Maven
# ============================================
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# Instalar dos2unix para fix line endings Windows->Linux
RUN apk add --no-cache dos2unix

# Copiar Maven wrapper y pom primero (cache de dependencias)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Fix CRLF y dar permisos de ejecucion
RUN dos2unix mvnw && chmod +x mvnw

# Descargar dependencias (capa cacheada si pom.xml no cambia)
RUN ./mvnw dependency:go-offline -B

# Copiar codigo fuente y compilar
COPY src/ src/
RUN ./mvnw package -DskipTests -B

# ============================================
# Stage 2: Runtime con JRE
# ============================================
FROM eclipse-temurin:17-jre-alpine AS runtime
WORKDIR /app

# Crear usuario no-root
RUN addgroup -g 1001 -S ggb && adduser -u 1001 -S ggb -G ggb

# Crear directorio de uploads con permisos
RUN mkdir -p /app/uploads/games && chown -R ggb:ggb /app/uploads

# Copiar imagenes de juegos existentes (311 JPGs, ~8.4MB)
COPY --chown=ggb:ggb uploads/games/ /app/uploads/games/

# Copiar JAR desde stage de build
COPY --from=build /app/target/GGBProyect-0.0.1-SNAPSHOT.jar app.jar
RUN chown ggb:ggb app.jar

USER ggb

EXPOSE 8080

ENV UPLOAD_GAMES_DIR=/app/uploads/games

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java", "-jar", "app.jar"]
