name: add-hexagonal-endpoint
purpose: Añadir un endpoint REST con DTOs, Controller, Service, Repository y tests en arquitectura hexagonal.
inputs:
  - entityName: e.g., Product
  - fields: e.g., name:String, price:BigDecimal, sku:String
  - packageBase: com.example.demo
constraints:
  - Seguir patrones DTO -> Service -> Repository.
  - Usar puertos (ports) definidos en `application.port`.
  - Escribir pruebas unitarias para Service y pruebas de integración para Controller.
  - Aplicar validaciones y manejo global de errores.
instructions:
  1. Crear el modelo de dominio en `domain.model`.
  2. Crear puertos `in` y `out` en `application.port`.
  3. Implementar el caso de uso en `application.service`.
  4. Crear adaptadores outbound en `adapters.outbound.persistence` con `@Entity` y repositorios Spring Data.
  5. Crear adaptadores inbound en `adapters.inbound.rest` con controllers y mapeadores DTO.
  6. Escribir pruebas unitarias para el servicio y pruebas de integración para el controlador.
examples:
  - Input: entityName=Product, fields=name:String,price:BigDecimal,sku:String
  - Expected: Archivos creados bajo `packageBase`: entidad, DTOs, puertos, servicio, controlador, pruebas.