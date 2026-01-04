name: implement-business-logic
purpose: Implementar reglas de negocio específicas de una entidad o caso de uso en arquitectura hexagonal.
inputs:
  - useCaseDescription: descripción natural del requisito de negocio
  - entityContext: qué entidades están implicadas y sus relaciones
constraints:
  - Buscar la raíz del problema si hay ambigüedad.
  - Producir tests que definan el comportamiento esperado (TDD-friendly).
  - Mantener separación de responsabilidades y aplicar inyección de dependencias.
instructions:
  1. Analiza la descripción y extrae invariantes y reglas de negocio.
  2. Propón una API de servicio (métodos públicos) que exprese esas reglas.
  3. Implementa la lógica en el `application.service` y crea objetos de valor si procede.
  4. Añade puertos `in` y `out` si es necesario para interactuar con adaptadores.
  5. Escribe pruebas unitarias que cubran casos felices y errores (una aserción por test).
  6. Propón escenarios de edge cases y cómo manejarlos (ej. condiciones de concurrencia, validaciones).
examples:
  - "Cuando el stock de un producto es menor a 0 al reservar, lanzar `OutOfStockException` y retornar 409 en la API."