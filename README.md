# Conexión Ambato — APE 4 · Fase 2

Red social dinámica implementada como **multilista** con nodos propios, sin `HashMap`, `HashSet`, `ArrayList` ni `LinkedList`.

**Asignatura:** Estructura de Datos — Universidad Técnica de Ambato (FISEI), Ingeniería en Software, Tercero B
**Docente:** Ing. José Rubén Caiza Caizabuano, PhD.
**Autor:** Andres Llamuca

## Descripción

La Fase 1 definió una red de 6 usuarios y 5 amistades. La Fase 2 conserva esa red como estado inicial y agrega operaciones de actualización y relaciones dirigidas de seguimiento.

- **Vértices:** `P01` Lucas, `P02` Sofía, `P03` Mateo, `P04` Valeria, `P05` Daniel, `P06` Camila.
- **AMISTAD:** recíproca, se guarda como dos aristas (una en cada vértice).
- **SEGUIR:** dirigida, se guarda como una sola arista desde quien sigue hacia el seguido.
- No se permiten lazos ni relaciones duplicadas del mismo tipo.

## Estructura

```
src/
├── modelo/
│   ├── TipoRelacion.java   # enum AMISTAD, SEGUIR
│   ├── Persona.java        # usuario y nombre
│   ├── Vertice.java        # nodo de la lista de personas (con su lista de aristas)
│   └── Arista.java         # nodo de la lista de relaciones
├── negocio/
│   └── RedSocial.java      # operaciones y validaciones
└── app/
    └── Main.java           # demostración de la secuencia de comprobación y menú (-i)
test/
└── pruebas/
    └── PruebasRedSocial.java   # pruebas automatizadas (55 verificaciones)
.github/workflows/ci.yml        # compila y ejecuta las pruebas en cada push
```

## Requisitos

JDK 11 o superior (probado con Java 21; el flujo de GitHub usa Java 17).

## Compilar y ejecutar

**Linux / macOS**
```bash
mkdir -p out
javac -encoding UTF-8 -d out $(find src test -name "*.java")
java -cp out app.Main                      # demostración
java -cp out app.Main -i                   # además abre el menú interactivo
java -cp out pruebas.PruebasRedSocial      # pruebas automatizadas
```

**Windows (PowerShell)**
```powershell
mkdir out
javac -encoding UTF-8 -d out (Get-ChildItem -Recurse src,test -Filter *.java).FullName
java -cp out app.Main
java -cp out pruebas.PruebasRedSocial
```

> Si en consola salen mal las tildes: `java -Dfile.encoding=UTF-8 -cp out app.Main`

## Operaciones de `RedSocial`

| Operación | Resultado | Regla principal |
|---|---|---|
| `registrarPersona(u, n)` | `true/false` | Rechaza usuario vacío, repetido, con espacios o comas |
| `eliminarPersona(u)` | `true/false` | Elimina el vértice y todas las aristas entrantes |
| `agregarAmistad(a, b)` | `true/false` | Dos aristas AMISTAD; sin lazos ni duplicados |
| `seguir(a, b)` | `true/false` | Una arista dirigida SEGUIR |
| `eliminarAmistad(a, b)` | `true/false` | Quita ambas aristas AMISTAD |
| `dejarDeSeguir(a, b)` | `true/false` | Quita solo la arista SEGUIR |
| `listarAmigos` / `listarSeguidos` / `listarSeguidores` | texto o `null` | `null` si el usuario no existe |
| `contarAmigos(u)` | entero | `-1` si el usuario no existe |
| `mostrarRed()` | texto | Todos los vértices con sus aristas |

El identificador se normaliza a minúsculas: `P01` y `p01` son la misma cuenta.

## Complejidad

`V` es el número de personas y `A` el de aristas almacenadas (cada amistad aporta dos).

| Operación | Costo |
|---|---|
| buscar / registrar | O(V) |
| agregarAmistad, seguir, eliminarAmistad, dejarDeSeguir, listarAmigos, listarSeguidos, contarAmigos | O(V + grado) |
| eliminarPersona, listarSeguidores, mostrarRed | O(V + A) |

## Resultado esperado de la demostración

```
registrarPersona(P01, Lucas) -> true
...
Personas: 6 | Amistades: 5 | Aristas internas: 10
Grados (P01..P06): 3, 2, 2, 2, 1, 0
listarAmigos(P01) -> Valeria, Mateo, Sofía
listarSeguidores(P01) -> Camila, Mateo
eliminarPersona(P01) -> true
registrarPersona(P02, otro nombre) -> false
agregarAmistad(P03, P03) -> false
```

Como cada arista se inserta al comienzo de su lista, los listados muestran primero las relaciones más recientes.
