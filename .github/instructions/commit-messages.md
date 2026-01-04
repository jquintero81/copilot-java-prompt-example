# Instrucciones para Mensajes de Commit

Cuando se te solicite generar un mensaje de commit, **SIEMPRE** debes seguir la convención de Conventional Commits con el siguiente formato:

## Formato Requerido

```
<type>(<optional scope>): <description>

<optional body>

<optional footer>
```

## Types (Tipos de Commit)

Usa **SOLO** uno de estos tipos según el cambio realizado:

- **feat**: Agregar, ajustar o eliminar una característica de la API o UI
- **fix**: Corregir un bug de la API o UI
- **refactor**: Reescribir o reestructurar código sin alterar el comportamiento
- **perf**: Commits de refactor que mejoran el rendimiento
- **style**: Cambios de formato de código (espacios, formato, punto y coma) sin afectar el comportamiento
- **test**: Agregar o corregir tests
- **docs**: Cambios que afectan exclusivamente la documentación
- **build**: Cambios que afectan componentes de build (dependencias, versión del proyecto, herramientas)
- **ops**: Cambios en infraestructura, CI/CD, deployment, monitoring, backups
- **chore**: Tareas como commit inicial, modificar `.gitignore`, etc.

## Scope (Alcance)

- El scope es **OPCIONAL**
- Proporciona contexto adicional sobre el área del proyecto afectada
- Ejemplos: `(api)`, `(shopping-cart)`, `(auth)`, `(database)`
- **NO** uses identificadores de issues como scopes

## Description (Descripción)

- Es **OBLIGATORIA**
- Usa tiempo presente imperativo: "add" no "added" ni "adds"
- Piensa en: "Este commit va a..."
- **NO** capitalices la primera letra
- **NO** termines con un punto (`.`)
- Máximo 100 caracteres

## Breaking Changes (Cambios Incompatibles)

- Si el commit introduce cambios incompatibles, agregar `!` antes de `:` 
- Ejemplo: `feat(api)!: remove status endpoint`
- Describir los cambios en el footer con `BREAKING CHANGE:`

## Body (Cuerpo)

- Es **OPCIONAL**
- Explica la motivación del cambio
- Usa tiempo presente imperativo
- Contrasta con el comportamiento anterior si es relevante

## Footer (Pie de Página)

- Es **OPCIONAL** (excepto para breaking changes)
- Incluye referencias a issues: `Closes #123`, `Fixes JIRA-456`
- Para breaking changes: `BREAKING CHANGE: descripción detallada`

## Ejemplos de Buenos Mensajes

### Ejemplo Simple
```
feat: add email notifications on new direct messages
```

### Con Scope
```
feat(shopping-cart): add the amazing button
```

### Con Breaking Change
```
feat!: remove ticket list endpoint

refers to JIRA-1337

BREAKING CHANGE: ticket endpoints no longer supports list all entities.
```

### Fix
```
fix(api): fix wrong calculation of request body checksum
```

### Con Body
```
fix: add missing parameter to service call

The error occurred due to incorrect parameter mapping in the service layer.
```

### Performance
```
perf: decrease memory footprint for determine unique visitors by using HyperLogLog
```

### Build
```
build: update dependencies
```

### Refactor
```
refactor: implement fibonacci number calculation as recursion
```

## Casos Especiales

### Commit Inicial
```
chore: init
```

### Merge Commits
```
Merge branch '<branch name>'
```
(Sigue el formato por defecto de git merge)

### Revert Commits
```
Revert "<reverted commit subject line>"
```
(Sigue el formato por defecto de git revert)

## Recordatorios Importantes

1. ✅ **Siempre** usa tiempo presente imperativo
2. ✅ **Nunca** capitalices la primera letra de la descripción
3. ✅ **Nunca** termines la descripción con punto
4. ✅ **Elige** el tipo correcto según el cambio realizado
5. ✅ **Usa** `!` para indicar breaking changes
6. ✅ **Limita** la descripción a 100 caracteres
7. ✅ **Mantén** el scope entre 1-20 caracteres si lo usas

## Cuando Generar Mensajes de Commit

Cuando el usuario pida:
- "genera un mensaje de commit"
- "crea un commit message"
- "sugiéreme un commit"
- "qué mensaje de commit uso"
- O cualquier variación similar

**SIEMPRE** genera el mensaje siguiendo EXACTAMENTE este formato sin excepciones.
