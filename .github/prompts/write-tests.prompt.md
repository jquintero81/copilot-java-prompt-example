name: write-tests
purpose: Generar tests unitarios e integración siguiendo las reglas de tests del repo.
inputs:
  - target: Service|Controller|Repository
  - framework: junit5,mockito,spring-test
constraints:
  - Un assert por test.
  - Tests rápidos e independientes.
  - Usar nombres descriptivos: `givenInitialConditions_whenTestCase_thenExpectedResult`.
  - Tenemos que usar estrcutura Given-When-Then.
  - Seguir convenciones de mocks y fixtures del proyecto.
  - Preferible usar mockito BDD style.
  - Cobertura de al menos un 80%.
instructions:
  1. Si `target=Service`, mockear dependencias externas y probar lógica.
  2. Si `target=Controller`, usar `MockMvc` o `@WebMvcTest` para verificar rutas y códigos HTTP.
  3. Añadir casos de error y validación.
  4. Añadir configuración para `@TestConfiguration` si se necesita inyectar beans de prueba.
examples:
  - Unit test para `ProductService.reserveStock()` que lanza `OutOfStockException`.