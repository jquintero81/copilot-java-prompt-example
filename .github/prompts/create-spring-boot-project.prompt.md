name: create-spring-boot-project
purpose: Crear un proyecto Spring Boot inicial (Maven) con dependencias comunes y estructura limpia.
inputs:
  - groupId: com.example
  - artifactId: demo
  - packageName: com.example.demo
  - javaVersion: 17
  - dependencies: web,data-jpa,h2,validation
constraints:
  - Proyecto Maven, Java 17 (o 21 según preferencia).
  - No usar lombok por defecto; generar código explícito.
  - Seguir reglas de clean-code definidas en `.github/instructions/clean-code.md`.
instructions:
  1. Genera la llamada a Spring Initializr (URL) para descargar el zip del proyecto.
  2. Describe la estructura de carpetas y archivos principales (`src/main/java`, `src/test/java`, `pom.xml`, `application.yml`).
  3. Crea un `Application` bootstrap y un ejemplo de `README.md` con comandos para construir y ejecutar.
  4. Añade configuración mínima de `application.yml` con H2 y datasource en memoria.
examples:
  - curl "https://start.spring.io/starter.zip?type=maven-project&language=java&bootVersion=3.1.6&baseDir=${artifactId}&groupId=${groupId}&artifactId=${artifactId}&name=${artifactId}&packageName=${packageName}&javaVersion=${javaVersion}&dependencies=${dependencies}" -o ${artifactId}.zip
  - mvn -f ${artifactId}/pom.xml clean package
notes:
  - Si el usuario prefiere Gradle, proveer comando equivalente.