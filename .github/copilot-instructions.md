# GitHub Copilot Instructions - Supermarket Management System

Este documento centraliza todas las instrucciones y guías para trabajar con GitHub Copilot en este proyecto. Actúa como punto de referencia único para garantizar consistencia en la generación y modificación de código.

## 📋 Tabla de Contenidos

1. [Instrucciones Generales](#instrucciones-generales)
2. [Arquitectura y Diseño](#arquitectura-y-diseño)
3. [Estándares de Código](#estándares-de-código)
4. [Convenciones de Commits](#convenciones-de-commits)
5. [Prompts Disponibles](#prompts-disponibles)
6. [Flujo de Trabajo Recomendado](#flujo-de-trabajo-recomendado)

---

## Instrucciones Generales

### Objetivo del Proyecto

Este es un **Sistema de Gestión de Supermercados** construido con:
- **Java 17** + **Spring Boot 3.2.1**
- **Arquitectura Hexagonal** (puertos y adaptadores)
- **Principios de Clean Code**
- **Base de datos H2** (en memoria)

### Principios Fundamentales

Toda modificación o creación de código debe respetar estos principios:

1. **Simplicidad**: KISS (Keep It Simple, Stupid)
2. **Responsabilidad única**: Cada clase/método hace una cosa
3. **Separación de capas**: Dominio, aplicación, adaptadores
4. **Testabilidad**: Todo código debe ser testeable
5. **Mantenibilidad**: Código legible y autodocumentado

---

## Arquitectura y Diseño

### Referencias Detalladas

👉 Ver: [`.github/instructions/hexagonal-guidelines.instructions.md`](.github/instructions/hexagonal-guidelines.instructions.md)

### Estructura de Capas

```
src/main/java/com/example/
├── domain/              # Lógica de negocio pura (sin dependencias externas)
│   ├── model/           # Entidades, objetos de valor
│   └── exception/       # Excepciones de dominio
├── application/         # Casos de uso (orquestación)
│   ├── port/
│   │   ├── in/          # Puertos de entrada (interfaces de casos de uso)
│   │   └── out/         # Puertos de salida (interfaces de dependencias)
│   └── service/         # Implementaciones de casos de uso
├── adapters/            # Implementaciones concretas
│   ├── inbound/         # REST controllers, mappers
│   └── outbound/        # JPA entities, repositorios
├── config/              # Configuración de Spring (beans, DI)
└── boot/                # Punto de entrada (main)
```

### Reglas Clave de Arquitectura

1. **El dominio no conoce nada externo** (sin Spring, sin JPA, sin HTTP)
2. **Los casos de uso dependen de puertos, no de implementaciones**
3. **Los adaptadores inbound llaman a puertos `in`**
4. **Los adaptadores outbound implementan puertos `out`**
5. **Records, DTOs y Commands en archivos separados** (nunca dentro de controllers/services)
6. **Mappers/Adapters para convertir entre capas** (evitar leakage de JPA)

---

## Estándares de Código

### Referencias Detalladas

👉 Ver: [`.github/instructions/clean-code.instructions.md`](.github/instructions/clean-code.instructions.md)

### Checklist de Calidad

Al escribir código, asegúrate de:

- ✅ **Nombres descriptivos**: Variables, métodos, clases con nombres claros
- ✅ **Funciones pequeñas**: Un método = una responsabilidad
- ✅ **Sin efectos secundarios**: Métodos predecibles
- ✅ **Encapsulación**: Detalles de implementación ocultos
- ✅ **Constantes vs magic numbers**: `BigDecimal.TEN` no `10`
- ✅ **Inyección de dependencias**: Constructor injection, no `new` directo
- ✅ **Polimorfismo sobre if/else**: Estrategias, no condicionales largos
- ✅ **Tests significativos**: Un assert por test, nombres claros
- ✅ **Comentarios valiosos**: Solo si explican el "por qué", no el "qué"

### Estructura de Métodos

```java
// ❌ NO recomendado
public void process(User u) {
    if (u != null && u.getStatus() == 1 && u.getAge() > 18) {
        // ... 20 líneas de lógica
    }
}

// ✅ Recomendado
public void processAdultActiveUser(User user) {
    validateUser(user);
    if (!isAdultAndActive(user)) {
        return;
    }
    executeBusinessLogic(user);
}

private void validateUser(User user) {
    if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
    }
}

private boolean isAdultAndActive(User user) {
    return user.isAdult() && user.isActive();
}
```

### Validaciones

Todas las validaciones pertenecen a **la capa de dominio**:

```java
public class Product {
    public void reduceStock(Integer quantity) {
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        if (this.stockQuantity < quantity) {
            throw new InsufficientStockException(this.name, quantity, this.stockQuantity);
        }
        this.stockQuantity -= quantity;
    }
}
```

### Tests

**Una aserción por test**, estructura Given-When-Then:

```java
@Test
@DisplayName("givenValidCustomerId_whenCreateOrder_thenOrderIsSaved")
void givenValidCustomerId_whenCreateOrder_thenOrderIsSaved() {
    // Given
    given(customerRepository.findById(1L)).willReturn(Optional.of(validCustomer));
    given(orderRepository.save(any(Order.class))).willReturn(validOrder);

    // When
    Order result = orderService.execute(validCommand);

    // Then
    assertThat(result).isNotNull();
}
```

---

## Convenciones de Commits

### Referencias Detalladas

👉 Ver: [`.github/instructions/commit-messages.instructions.md`](.github/instructions/commit-messages.instructions.md)

### Formato Obligatorio: Conventional Commits

```
<type>(<scope>): <description>

<optional body>

<optional footer>
```

### Tipos Permitidos

| Tipo | Descripción | Ejemplo |
|------|-------------|---------|
| `feat` | Nueva característica | `feat(order): add order cancellation` |
| `fix` | Corrección de bug | `fix(product): fix stock calculation` |
| `refactor` | Cambio de estructura sin alterar comportamiento | `refactor(service): simplify validation logic` |
| `perf` | Mejora de rendimiento | `perf(api): reduce query complexity` |
| `test` | Agregar o corregir tests | `test(order): add edge case tests` |
| `docs` | Cambios en documentación | `docs: update API endpoints` |
| `build` | Cambios en dependencias | `build: upgrade Spring Boot version` |
| `chore` | Tareas varias | `chore: init` |

### Ejemplos

```
✅ feat(order): add order status transitions

implement state machine for order lifecycle:
- PENDING -> CONFIRMED -> SHIPPED -> DELIVERED
- Add validation to prevent invalid transitions

Closes #123
```

```
✅ fix(product): prevent negative stock updates

The stock reduction should validate availability before updating.
Added InsufficientStockException.
```

```
✅ refactor: extract validation to separate service

Move repetitive validations from ProductService to dedicated 
ProductValidationService following SRP.
```

---

## Prompts Disponibles

Estos prompts automatizan tareas comunes. Cópialos al chat de Copilot y reemplaza los placeholders.

### 1. **Crear Proyecto Spring Boot**
📄 [`.github/prompts/create-spring-boot-project.prompt.md`](.github/prompts/create-spring-boot-project.prompt.md)

Uso: Generar un nuevo proyecto Maven desde cero.

```bash
# Copiar el contenido del prompt y ejecutar:
curl "https://start.spring.io/starter.zip?type=maven-project&language=java&..." -o myproject.zip
```

### 2. **Añadir Hexagonal Endpoint**
📄 [`.github/prompts/add-hexagonal-endpoint.prompt.md`](.github/prompts/add-hexagonal-endpoint.prompt.md)

Uso: Crear un endpoint REST completo (Controller → Service → Repository) siguiendo arquitectura hexagonal.

**Entrada**:
- `entityName`: ej. `Invoice`
- `fields`: ej. `invoiceNumber:String, amount:BigDecimal, customerId:Long`

**Salida**: 
- Modelo de dominio
- DTOs (Request/Response)
- Puertos (in/out)
- Servicio
- Controller + global exception handler
- Tests unitarios e integración

### 3. **Implementar Lógica de Negocio**
📄 [`.github/prompts/implement-business-logic.prompt.md`](.github/prompts/implement-business-logic.prompt.md)

Uso: Agregar reglas de negocio complejas, validaciones, flujos de trabajo.

**Entrada**:
- `useCaseDescription`: ej. "Cuando se cancela una orden, restaurar el stock de todos los productos"
- `entityContext`: entidades implicadas

**Salida**:
- Métodos en service
- Validaciones en dominio
- Tests TDD (tests primero)

### 4. **Escribir Tests**
📄 [`.github/prompts/write-tests.prompt.md`](.github/prompts/write-tests.prompt.md)

Uso: Generar tests unitarios e integración.

**Entrada**:
- `target`: `Service` | `Controller` | `Repository`
- `framework`: `junit5`, `mockito`, `spring-test`

**Salida**:
- Tests siguiendo estructura Given-When-Then
- Cobertura >= 80%
- Un assert por test

### 5. **Refactorizar a Polimorfismo**
📄 [`.github/prompts/refactor-to-polymorphism.prompt.md`](.github/prompts/refactor-to-polymorphism.prompt.md)

Uso: Convertir condicionales (if/switch) en soluciones orientadas a objetos.

**Entrada**:
- `filePath`: ruta del archivo
- `behaviorSummary`: resumen de comportamientos

**Salida**:
- Interfaz común
- Implementaciones concretas
- Factory o Strategy
- Tests actualizados

---

## Flujo de Trabajo Recomendado

### Scenario 1: Crear un Nuevo Endpoint

```
1. Abre `.github/prompts/add-hexagonal-endpoint.prompt.md`
2. Reemplaza `entityName` y `fields` con tu contexto
3. Pega en Copilot Chat
4. Pide: "Genera los archivos listados. Luego crea tests unitarios 
          para el Service y integration tests para el Controller"
5. Ejecuta: mvn clean test
6. Revisa cobertura y commits con: git log --oneline
```

### Scenario 2: Implementar Lógica Compleja

```
1. Describe el requisito en `.github/prompts/implement-business-logic.prompt.md`
2. Pega en Copilot Chat
3. Pide: "Primero escribe los tests que describan el comportamiento 
          esperado (TDD), luego implementa"
4. Ejecuta tests: mvn test
5. Valida con: mvn verify (incluye coverage)
```

### Scenario 3: Refactorizar Condicionales

```
1. Identifica un if/switch largo en tu código
2. Abre `.github/prompts/refactor-to-polymorphism.prompt.md`
3. Proporciona el código actual y estructura esperada
4. Copilot genera interfaces, implementaciones y Strategy
5. Actualiza tests y valida: mvn clean test
```

### Scenario 4: Generalista - Seguir Directrices

```
Cuando no hay un prompt específico, menciona en Copilot:

"Por favor, sigue estas directrices:
- Arquitectura hexagonal (domain → application → adapters)
- Clean Code (funciones pequeñas, un assert por test)
- Estructura: Given-When-Then en tests
- Un record por archivo (DTO, Request, Response, Command)
- Commits: Conventional Commits format
- Ver .github/instructions/ para más detalles"
```

---

## Checklist para Code Review

Antes de hacer commit, verifica:

- [ ] ¿El código sigue arquitectura hexagonal?
- [ ] ¿Las entidades de dominio no dependen de Spring/JPA?
- [ ] ¿Los DTOs están en archivos separados?
- [ ] ¿Hay validaciones en el dominio (no en controllers)?
- [ ] ¿Los tests tienen nombres descriptivos?
- [ ] ¿Un assert por test?
- [ ] ¿Cobertura >= 80%?
- [ ] ¿Funciones pequeñas (<20 líneas)?
- [ ] ¿Sin condicionales largos (if/switch)?
- [ ] ¿Commit message sigue Conventional Commits?

---

## Referencias Rápidas

### Archivos de Instrucciones

| Archivo | Propósito |
|---------|-----------|
| [`.github/instructions/clean-code.instructions.md`](.github/instructions/clean-code.instructions.md) | Reglas de Clean Code |
| [`.github/instructions/hexagonal-guidelines.instructions.md`](.github/instructions/hexagonal-guidelines.instructions.md) | Guía de Arquitectura Hexagonal |
| [`.github/instructions/commit-messages.instructions.md`](.github/instructions/commit-messages.instructions.md) | Convención de commits |

### Archivos de Prompts

| Archivo | Uso |
|---------|-----|
| [`.github/prompts/create-spring-boot-project.prompt.md`](.github/prompts/create-spring-boot-project.prompt.md) | Crear proyecto nuevo |
| [`.github/prompts/add-hexagonal-endpoint.prompt.md`](.github/prompts/add-hexagonal-endpoint.prompt.md) | Agregar endpoint |
| [`.github/prompts/implement-business-logic.prompt.md`](.github/prompts/implement-business-logic.prompt.md) | Lógica de negocio |
| [`.github/prompts/write-tests.prompt.md`](.github/prompts/write-tests.prompt.md) | Tests |
| [`.github/prompts/refactor-to-polymorphism.prompt.md`](.github/prompts/refactor-to-polymorphism.prompt.md) | Refactorización |

---

## Contacto y Actualización

Este documento está centralizado en `copilot-instructions.md`. 

**Cuando actualices instrucciones**:
1. Modifica el archivo correspondiente en `.github/instructions/` o `.github/prompts/`
2. Actualiza las referencias aquí si es necesario
3. Commit con: `docs: update copilot guidelines`
