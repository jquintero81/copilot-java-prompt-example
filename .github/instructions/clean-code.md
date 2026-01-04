# Clean Code Rules (Proyecto)

## General rules
1. Follow standard conventions.
2. Keep it simple stupid. Simpler is always better.
3. Boy scout rule. Leave the campground cleaner than you found it.
4. Always find root cause.

## Design rules
1. Keep configurable data at high levels.
2. Prefer polymorphism to if/else or switch/case.
3. Separate multi-threading code.
4. Prevent over-configurability.
5. Use dependency injection.
6. Follow Law of Demeter.

## Understandability tips
1. Be consistent.
2. Use explanatory variables.
3. Encapsulate boundary conditions.
4. Prefer dedicated value objects to primitives.
5. Avoid logical dependency.
6. Avoid negative conditionals.

## Names rules
1. Choose descriptive names.
2. Make meaningful distinction.
3. Use pronounceable names.
4. Use searchable names.
5. Replace magic numbers with constants.
6. Avoid encodings.

## Functions rules
1. Small.
2. Do one thing.
3. Use descriptive names.
4. Prefer fewer arguments.
5. Have no side effects.
6. Don't use flag arguments.

## Comments rules
1. Explain yourself in code first.
2. Don't be redundant.
3. Don't add obvious noise.
4. Use comments for intent, clarification, or warnings.
5. Remove commented-out code.

## Source code structure
1. Separate concepts vertically.
2. Related code should appear vertically dense.
3. Declare variables close to usage.
4. Dependent functions should be close.
5. Keep lines short and clear.

## Objects and data structures
1. Hide internal structure.
2. Prefer explicit data structures.
3. Keep objects small and focused.
4. Extract records to separate files - never define records inside controllers, services, or interfaces.
5. Create dedicated DTO/Request/Response classes in their own files for better maintainability.

## Tests
1. One assert per test.
2. Readable.
3. Fast.
4. Independent.
5. Repeatable.

## Code smells
List common smells: rigidity, fragility, needless complexity, repetition, opacity.