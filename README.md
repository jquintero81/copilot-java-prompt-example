# Supermarket Management System

Sistema de gestión de supermercados construido con Spring Boot siguiendo arquitectura hexagonal y principios de Clean Code.

## 📋 Descripción

Sistema para gestionar clientes, productos, pedidos y stock de un supermercado. Implementa arquitectura hexagonal (puertos y adaptadores) para mantener la lógica de negocio independiente de los detalles de implementación.

## 🏗️ Arquitectura

```
src/main/java/com/example/
├── domain/                    # Capa de dominio (lógica de negocio pura)
│   ├── model/                 # Entidades de dominio
│   │   ├── Product.java
│   │   ├── Customer.java
│   │   ├── Order.java
│   │   ├── OrderItem.java
│   │   └── OrderStatus.java
│   └── exception/             # Excepciones de dominio
│       ├── ProductNotFoundException.java
│       ├── CustomerNotFoundException.java
│       └── InsufficientStockException.java
│
├── application/               # Capa de aplicación (casos de uso)
│   ├── port/
│   │   ├── in/               # Puertos de entrada (use cases)
│   │   │   ├── CreateProductUseCase.java
│   │   │   ├── GetProductUseCase.java
│   │   │   ├── CreateCustomerUseCase.java
│   │   │   └── CreateOrderUseCase.java
│   │   └── out/              # Puertos de salida (repositorios)
│   │       ├── ProductRepositoryPort.java
│   │       ├── CustomerRepositoryPort.java
│   │       └── OrderRepositoryPort.java
│   └── service/              # Implementación de casos de uso
│       ├── ProductService.java
│       ├── CustomerService.java
│       └── OrderService.java
│
├── adapters/                  # Adaptadores (implementaciones concretas)
│   ├── inbound/
│   │   └── rest/             # Adaptadores de entrada (REST API)
│   │       ├── ProductController.java
│   │       ├── CustomerController.java
│   │       ├── OrderController.java
│   │       └── GlobalExceptionHandler.java
│   └── outbound/
│       └── persistence/       # Adaptadores de salida (persistencia)
│           ├── ProductEntity.java
│           ├── CustomerEntity.java
│           ├── OrderEntity.java
│           ├── OrderItemEntity.java
│           ├── SpringDataProductRepository.java
│           ├── SpringDataCustomerRepository.java
│           ├── SpringDataOrderRepository.java
│           ├── ProductRepositoryAdapter.java
│           ├── CustomerRepositoryAdapter.java
│           └── OrderRepositoryAdapter.java
│
├── config/                    # Configuración de beans
│   └── AdapterConfiguration.java
└── boot/                      # Punto de entrada
    └── Application.java
```

## 🚀 Tecnologías

- Java 17
- Spring Boot 3.2.1
- Spring Data JPA
- H2 Database (en memoria)
- Maven


## ⚙️ Configuración

### Requisitos previos

- Java 17 (ubicado en: `c:/Users/<user_home>/.sdkman/candidates/java/17.0.12-oracle`)
- Maven 3.6+

### Compilar el proyecto

Usando el script de PowerShell:

```powershell
.\build.ps1
```

O manualmente:

```powershell
$env:JAVA_HOME = "c:/Users/<user_home>/.sdkman/candidates/java/17.0.12-oracle"
mvn clean package
```

### Ejecutar la aplicación

Usando el script de PowerShell:

```powershell
.\run.ps1
```

O manualmente:

```powershell
$env:JAVA_HOME = "c:/Users/<user_home>/.sdkman/candidates/java/17.0.12-oracle"
mvn spring-boot:run
```

O ejecutar el JAR directamente:

```powershell
java -jar target/copilot-java-prompt-example-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## 🔍 Consola H2

Accede a la consola H2 en: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:mem:supermarketdb`
- Username: `sa`
- Password: (dejar vacío)

## 📡 API Endpoints

### Products

#### Crear producto
```bash
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{
    "sku": "PROD-001",
    "name": "Leche Entera",
    "description": "Leche entera 1L",
    "price": 1.50,
    "stockQuantity": 100
  }'
```

#### Obtener producto por ID
```bash
curl http://localhost:8080/api/v1/products/1
```

#### Obtener producto por SKU
```bash
curl http://localhost:8080/api/v1/products/sku/PROD-001
```

#### Listar todos los productos
```bash
curl http://localhost:8080/api/v1/products
```

#### Actualizar stock
```bash
curl -X PATCH http://localhost:8080/api/v1/products/1/stock?quantity=50
```

### Customers

#### Crear cliente
```bash
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{
    "email": "juan.perez@example.com",
    "firstName": "Juan",
    "lastName": "Pérez",
    "phone": "+34600123456",
    "address": "Calle Mayor 123, Madrid"
  }'
```

#### Obtener cliente por ID
```bash
curl http://localhost:8080/api/v1/customers/1
```

#### Obtener cliente por email
```bash
curl http://localhost:8080/api/v1/customers/email/juan.perez@example.com
```

#### Listar todos los clientes
```bash
curl http://localhost:8080/api/v1/customers
```

### Orders

#### Crear pedido
```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {
        "productId": 1,
        "quantity": 2
      },
      {
        "productId": 2,
        "quantity": 1
      }
    ]
  }'
```

#### Obtener pedido por ID
```bash
curl http://localhost:8080/api/v1/orders/1
```

#### Listar pedidos de un cliente
```bash
curl http://localhost:8080/api/v1/orders/customer/1
```

#### Actualizar estado del pedido
```bash
# Confirmar
curl -X PATCH http://localhost:8080/api/v1/orders/1/confirm

# Enviar
curl -X PATCH http://localhost:8080/api/v1/orders/1/ship

# Entregar
curl -X PATCH http://localhost:8080/api/v1/orders/1/deliver

# Cancelar
curl -X PATCH http://localhost:8080/api/v1/orders/1/cancel
```

## 🧪 Ejemplo completo de flujo

```bash
# 1. Crear productos
curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"PROD-001","name":"Leche","description":"Leche entera 1L","price":1.50,"stockQuantity":100}'

curl -X POST http://localhost:8080/api/v1/products \
  -H "Content-Type: application/json" \
  -d '{"sku":"PROD-002","name":"Pan","description":"Pan de molde","price":2.00,"stockQuantity":50}'

# 2. Crear cliente
curl -X POST http://localhost:8080/api/v1/customers \
  -H "Content-Type: application/json" \
  -d '{"email":"maria@example.com","firstName":"María","lastName":"García","phone":"+34600111222","address":"Calle Sol 45, Madrid"}'

# 3. Crear pedido
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{"customerId":1,"items":[{"productId":1,"quantity":3},{"productId":2,"quantity":2}]}'

# 4. Confirmar pedido
curl -X PATCH http://localhost:8080/api/v1/orders/1/confirm

# 5. Verificar stock actualizado
curl http://localhost:8080/api/v1/products/1
```

## 📚 Principios aplicados

### Clean Code

- **Nombres descriptivos**: Variables, métodos y clases con nombres claros
- **Funciones pequeñas**: Cada método hace una sola cosa
- **Sin side effects**: Métodos predecibles y sin efectos secundarios ocultos
- **Objetos de valor**: BigDecimal para precios, LocalDateTime para fechas
- **Encapsulación**: Validaciones en el dominio

### Arquitectura Hexagonal

- **Independencia del dominio**: La lógica de negocio no depende de frameworks
- **Puertos e interfaces**: Abstracciones claras entre capas
- **Adaptadores intercambiables**: Fácil cambio de implementaciones (BD, API, etc.)
- **Inversión de dependencias**: El dominio no conoce los adaptadores

### Diseño

- **Inyección de dependencias**: Uso de Spring para wiring
- **Separation of concerns**: Capas bien definidas
- **Law of Demeter**: Cada clase conoce solo sus dependencias directas

## 🎯 Próximos pasos

Usa los prompts en `.github/prompts/` para:

- Añadir nuevas entidades (ej: Shipment, Invoice)
- Implementar nuevos casos de uso (ej: gestión de devoluciones)
- Añadir validaciones adicionales
- Crear tests unitarios e integración
- Refactorizar condicionales a polimorfismo

## 📝 Notas

- La base de datos H2 es en memoria y se reinicia con cada ejecución
- Los datos iniciales se pierden al detener la aplicación
- Para persistencia real, cambiar a PostgreSQL/MySQL en `application.yml`

```
  "timestamp": "2025-12-28T10:30:00"
```

## Siguientes pasos

Para extender el proyecto, puedes usar los prompts en `.github/prompts/`:

1. **Añadir nuevos casos de uso**: usar `implement-business-logic.prompt.md`
2. **Añadir tests**: usar `write-tests.prompt.md`
3. **Refactorizar código**: usar `refactor-to-polymorphism.prompt.md`

## Guías de desarrollo

Ver `.github/instructions/` para:
- Reglas de Clean Code
- Principios de arquitectura hexagonal
- Convenciones del proyecto

## Licencia

Este proyecto es un ejemplo educativo.
