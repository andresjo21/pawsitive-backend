# Usa una imagen base de OpenJDK 17
FROM amd64/openjdk:17

# Copia el archivo JAR de la aplicación al contenedor
COPY ./build/libs/pawsitive-0.0.3-SNAPSHOT.jar /app/pawsitive-0.0.3-SNAPSHOT.jar

# Establece el directorio de trabajo dentro del contenedor
WORKDIR /app

# Comando a ejecutar cuando se inicie el contenedor
CMD ["java", "-jar", "pawsitive-0.0.3-SNAPSHOT.jar"]