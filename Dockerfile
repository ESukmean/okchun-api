FROM arm64v8/openjdk:25-jdk

# gradle을 실행하기 위해서는 findutils 필요
RUN microdnf install findutils

WORKDIR /app

# Copy only Gradle wrapper and build scripts to enable caching
COPY gradlew gradlew
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Pre-warm Gradle (downloads dependencies once and caches)
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew --no-daemon build -x test || true


COPY src src
RUN --mount=type=cache,target=/root/.gradle \
    ./gradlew bootJar --no-daemon


RUN cp ./build/libs/*.jar /app/web.jar

# Default environment variables
ENV PORT=5000
ENV JAVA_OPTS=""

# Start the application with dynamic runtime arguments
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=$PORT -jar /app/web.jar"]