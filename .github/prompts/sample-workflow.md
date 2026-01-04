# Flujo de ejemplo para generar y evolucionar el proyecto

1) Crear proyecto inicial (shell):
   - Copiar el prompt `create-spring-boot-project` en Copilot Chat y rellenar `groupId`, `artifactId`, `dependencies`.
   - Ejecutar el comando curl indicado para descargar el zip.
   - Descomprimir y abrir el proyecto en VS Code.

2) Añadir entidad y endpoint:
   - Abrir `add-hexagonal-endpoint.prompt.md`, rellenar `entityName` y `fields`.
   - Pegar en Copilot Chat y pedir: "Genera los archivos, luego crea tests unitarios para el Service y un integration test para el Controller."

3) Implementar lógica de negocio:
   - Crear un prompt `implement-business-logic` con la descripción del caso de uso.
   - Solicitar que primero genere tests que describan el comportamiento (TDD).

4) Ejecutar pruebas localmente:
```bash
mvn -DskipTests=false test
mvn -DskipTests=false -P integration-test verify
```

5) Iterar:
   - Si un test falla, usar prompts para diagnosticar y proponer la corrección.
   - Aplicar `refactor-to-polymorphism` cuando existan grandes condicionales.

6) Ejemplo de comandos para generar proyecto:
```bash
curl "https://start.spring.io/starter.zip?type=maven-project&language=java&baseDir=demo&groupId=com.example&artifactId=demo&name=demo&packageName=com.example.demo&javaVersion=17&dependencies=web,data-jpa,h2,validation" -o demo.zip
unzip demo.zip -d .
cd demo
mvn -DskipTests=false clean package
```

7) Recomendaciones:
   - Mantén `instructions/clean-code.md` frecuentemente actualizada.
   - Versiona los prompts: añadir encabezado `version: v1` y `lastUpdated`.
   - Para prompts complejos, provee fragmentos de código del repo actual (clases relevantes) como contexto al prompt.