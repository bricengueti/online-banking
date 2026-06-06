# ============================================================
# Stage 1 – Build common (library partagée)
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS common-builder
WORKDIR /workspace

# Copie et build de la lib common
COPY common/pom.xml common/pom.xml
COPY common/src common/src
COPY common/.mvn/ common/.mvn/
COPY common/mvnw common/mvnw
RUN chmod +x common/mvnw

WORKDIR /workspace/common
RUN ./mvnw install -DskipTests -B

# ============================================================
# Stage 2 – Build authService
# ============================================================
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /workspace

# Récupère le common installé dans le repo Maven local
COPY --from=common-builder /root/.m2 /root/.m2

COPY authService/pom.xml .
COPY authService/.mvn/ .mvn/
COPY authService/mvnw .
RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

COPY authService/src ./src
RUN ./mvnw package -DskipTests -B && \
    java -Djarmode=layertools -jar target/*.jar extract --destination target/extracted

# ============================================================
# Stage 3 – Runtime
# ============================================================
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

WORKDIR /app

# Dossier pour les logs
RUN mkdir -p /var/log/auth && chown -R appuser:appgroup /app /var/log/auth

USER appuser

COPY --from=builder /workspace/target/extracted/dependencies/ ./
COPY --from=builder /workspace/target/extracted/spring-boot-loader/ ./
COPY --from=builder /workspace/target/extracted/snapshot-dependencies/ ./
COPY --from=builder /workspace/target/extracted/application/ ./

EXPOSE 8081

HEALTHCHECK --interval=30s --timeout=5s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8081/api/auth/actuator/health || exit 1

ENV JAVA_OPTS="-XX:+UseContainerSupport -XX:MaxRAMPercentage=75.0 -Djava.security.egd=file:/dev/./urandom"

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS org.springframework.boot.loader.launch.JarLauncher"]