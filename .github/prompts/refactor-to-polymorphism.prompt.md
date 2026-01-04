name: refactor-to-polymorphism
purpose: Transformar condicionales (switch/if) a una solución basada en polimorfismo.
inputs:
  - filePath: ruta del archivo con condicional
  - behaviorSummary: breve resumen de los comportamientos distintos
constraints:
  - Mantener tests existentes; actualizar o añadir nuevos tests.
  - No introducir breaking changes en la API pública.
instructions:
  1. Identificar las ramas del condicional y proponer una interfaz común.
  2. Crear implementaciones concretas para cada caso.
  3. Usar un `Factory` o `Strategy` inyectado por DI para seleccionar la implementación.
  4. Mover lógica a clases específicas y reducir el tamaño del método original.
  5. Actualizar/añadir tests para cada implementación.
examples:
  - Convertir `if (type.equals("A")) ... else if (type.equals("B")) ...` en `StrategyA`, `StrategyB` y `StrategyFactory`.