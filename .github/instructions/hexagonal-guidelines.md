# Hexagonal Architecture Guidelines

## Propósito
Estas pautas están diseñadas para garantizar que el proyecto siga los principios de la arquitectura hexagonal, promoviendo la separación de preocupaciones y la independencia del dominio frente a frameworks y adaptadores externos.

## Principios clave
1. **Separación de capas**:
   - **Core domain**: Contiene la lógica de negocio pura (entidades, objetos de valor, excepciones de dominio).
   - **Application / Use Cases**: Orquesta operaciones del dominio; depende de puertos (interfaces), no de frameworks.
   - **Ports**:
     - **In (driven by adapters)**: Interfaces que exponen casos de uso a adaptadores (e.g., `CreateProductUseCase`).
     - **Out (driving adapters)**: Interfaces que representan dependencias externas (e.g., `ProductRepositoryPort`).
   - **Adapters**:
     - **Inbound**: Controllers REST, mappers, traductores de entrada.
     - **Outbound**: Repositorios JPA, clientes HTTP, adaptadores de persistencia/messaging.

2. **Independencia del dominio**:
   - El dominio no debe depender de frameworks, bibliotecas externas o detalles de infraestructura.
   - Las entidades y objetos de valor deben ser inmutables siempre que sea posible.

3. **Uso de puertos e interfaces**:
   - Los casos de uso en `application.service` solo dependen de interfaces en `application.port.out`.
   - Los adaptadores implementan estas interfaces y se inyectan mediante configuración.

4. **Adaptadores**:
   - Los adaptadores inbound (e.g., controllers) solo llaman a puertos `application.port.in`.
   - Los adaptadores outbound (e.g., repositorios) implementan puertos `application.port.out`.

5. **Configuración**:
   - Usar clases `@Configuration` para inyectar dependencias y conectar adaptadores con puertos.

## Reglas específicas
1. Nunca permitir que `adapters.*` dependa de `adapters.*` de otro tipo; solo `application` y `domain` son el núcleo.
2. Crear mappers/translator para convertir entre domain y persistence DTOs; evitar leakage de JPA en domain.
3. Mantener los casos de uso pequeños y enfocados en una sola responsabilidad.
4. Escribir pruebas unitarias para servicios y pruebas de integración para adaptadores.
5. **Extraer records a archivos separados**: Nunca definir records (Request, Response, Command, DTO) dentro de controllers, interfaces o services. Cada record debe tener su propio archivo.
6. Organizar DTOs en paquetes dedicados: `adapters.inbound.rest.dto` para requests/responses REST, `application.port.in.command` para commands de use cases.

## Estructura de paquetes sugerida
- `com.example.demo`
  - `domain`
    - `model`
    - `exception`
  - `application`
    - `port`
      - `in`
      - `out`
    - `service`  (implementaciones de los use-cases)
    - `dto` (opcional: request/response mapping)
  - `adapters`
    - `inbound`
      - `rest` (controllers, dtomappers)
    - `outbound`
      - `persistence` (entities JPA, Spring Data repos, mappers)
      - `httpclient`
  - `config`
  - `boot` (Application main)

## Ejemplo de flujo
1. Crear un caso de uso en `application.service`.
2. Definir puertos `in` y `out` en `application.port`.
3. Implementar adaptadores inbound y outbound.
4. Escribir pruebas unitarias para servicios y pruebas de integración para adaptadores.
5. Iterar y refactorizar según sea necesario.