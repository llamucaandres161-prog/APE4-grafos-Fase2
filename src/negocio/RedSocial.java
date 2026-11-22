package negocio;

import modelo.Arista;
import modelo.Persona;
import modelo.TipoRelacion;
import modelo.Vertice;

/**
 * Red social "Conexión Ambato" como multilista: una lista enlazada de vértices (personas)
 * y, por cada vértice, una lista enlazada de aristas (AMISTAD o SEGUIR).
 * No usa HashMap, HashSet, ArrayList ni LinkedList: solo nodos propios.
 */
public class RedSocial {

    private Vertice cabeza;

    // ---------------------------------------------------------------- búsqueda y auxiliares

    /** Busca un vértice por su identificador normalizado (minúsculas, sin espacios en los extremos). */
    private Vertice buscar(String usuario) {
        if (usuario == null) return null;
        String clave = usuario.trim().toLowerCase();
        for (Vertice v = cabeza; v != null; v = v.sig)
            if (v.persona.usuario.equals(clave)) return v;
        return null;
    }

    /** Inserta una arista al comienzo de la lista de origen. */
    private void insertar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        origen.aristas = new Arista(destino, tipo, origen.aristas);
    }

    /** Indica si origen ya tiene una arista de ese tipo hacia destino. */
    private boolean existe(Vertice origen, Vertice destino, TipoRelacion tipo) {
        for (Arista a = origen.aristas; a != null; a = a.sig)
            if (a.destino == destino && a.tipo == tipo) return true;
        return false;
    }

    /** Quita de la lista de origen la arista de ese tipo hacia destino. */
    private boolean quitar(Vertice origen, Vertice destino, TipoRelacion tipo) {
        Arista anterior = null;
        for (Arista a = origen.aristas; a != null; anterior = a, a = a.sig) {
            if (a.destino == destino && a.tipo == tipo) {
                if (anterior == null) origen.aristas = a.sig;
                else anterior.sig = a.sig;
                return true;
            }
        }
        return false;
    }

    /** Retira de w toda arista (de cualquier tipo) que apunte a objetivo. */
    private void quitarHacia(Vertice w, Vertice objetivo) {
        Arista anterior = null, a = w.aristas;
        while (a != null) {
            if (a.destino == objetivo) {
                if (anterior == null) w.aristas = a.sig;
                else anterior.sig = a.sig;
            } else {
                anterior = a;
            }
            a = a.sig;
        }
    }

    private int contar(Vertice v, TipoRelacion tipo) {
        int n = 0;
        for (Arista a = v.aristas; a != null; a = a.sig)
            if (a.tipo == tipo) n++;
        return n;
    }

    private String salientes(Vertice v, TipoRelacion tipo) {
        StringBuilder sb = new StringBuilder();
        for (Arista a = v.aristas; a != null; a = a.sig) {
            if (a.tipo != tipo) continue;
            if (sb.length() > 0) sb.append(", ");
            sb.append(a.destino.persona.nombre);
        }
        return sb.length() == 0 ? "Ninguno" : sb.toString();
    }

    // ---------------------------------------------------------------- personas

    /** Rechaza usuario vacío, repetido, con espacios o con comas. */
    public boolean registrarPersona(String usuario, String nombre) {
        if (usuario == null || nombre == null) return false;
        String clave = usuario.trim().toLowerCase();
        if (clave.isEmpty() || nombre.trim().isEmpty()) return false;
        if (clave.indexOf(' ') >= 0 || clave.indexOf(',') >= 0) return false;
        if (buscar(clave) != null) return false;
        Vertice nuevo = new Vertice(new Persona(clave, nombre.trim()));
        nuevo.sig = cabeza;      // inserción al inicio de la lista de vértices
        cabeza = nuevo;
        return true;
    }

    /** Elimina el vértice y todas las aristas que llegan a él. */
    public boolean eliminarPersona(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) return false;
        for (Vertice w = cabeza; w != null; w = w.sig)
            quitarHacia(w, objetivo);               // retira toda arista hacia objetivo
        Vertice anterior = null, actual = cabeza;
        while (actual != objetivo) {
            anterior = actual;
            actual = actual.sig;
        }
        if (anterior == null) cabeza = objetivo.sig;
        else anterior.sig = objetivo.sig;
        return true;
    }

    // ---------------------------------------------------------------- relaciones

    /** La amistad es recíproca: se crean dos aristas. Sin lazos ni duplicados. */
    public boolean agregarAmistad(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb || existe(va, vb, TipoRelacion.AMISTAD)) return false;
        insertar(va, vb, TipoRelacion.AMISTAD);
        insertar(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    /** Seguir es dirigido: se crea una sola arista de a hacia b. */
    public boolean seguir(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb || existe(va, vb, TipoRelacion.SEGUIR)) return false;
        insertar(va, vb, TipoRelacion.SEGUIR);
        return true;
    }

    /** Quita ambas aristas AMISTAD. */
    public boolean eliminarAmistad(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb || !existe(va, vb, TipoRelacion.AMISTAD)) return false;
        quitar(va, vb, TipoRelacion.AMISTAD);
        quitar(vb, va, TipoRelacion.AMISTAD);
        return true;
    }

    /** Quita solo la arista SEGUIR de a hacia b. */
    public boolean dejarDeSeguir(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        if (va == null || vb == null || va == vb) return false;
        return quitar(va, vb, TipoRelacion.SEGUIR);
    }

    public boolean sonAmigos(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        return va != null && vb != null && existe(va, vb, TipoRelacion.AMISTAD);
    }

    public boolean sigue(String a, String b) {
        Vertice va = buscar(a), vb = buscar(b);
        return va != null && vb != null && existe(va, vb, TipoRelacion.SEGUIR);
    }

    // ---------------------------------------------------------------- consultas

    /** Nombres de los amigos, o null si el usuario no existe. */
    public String listarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : salientes(v, TipoRelacion.AMISTAD);
    }

    /** Nombres de las personas a las que sigue, o null si el usuario no existe. */
    public String listarSeguidos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? null : salientes(v, TipoRelacion.SEGUIR);
    }

    /** Nombres de quienes siguen al usuario (recorre toda la red), o null si no existe. */
    public String listarSeguidores(String usuario) {
        Vertice objetivo = buscar(usuario);
        if (objetivo == null) return null;
        StringBuilder sb = new StringBuilder();
        for (Vertice w = cabeza; w != null; w = w.sig) {
            for (Arista a = w.aristas; a != null; a = a.sig) {
                if (a.destino == objetivo && a.tipo == TipoRelacion.SEGUIR) {
                    if (sb.length() > 0) sb.append(", ");
                    sb.append(w.persona.nombre);
                }
            }
        }
        return sb.length() == 0 ? "Ninguno" : sb.toString();
    }

    /** Cantidad de amigos, o -1 si el usuario no existe. */
    public int contarAmigos(String usuario) {
        Vertice v = buscar(usuario);
        return v == null ? -1 : contar(v, TipoRelacion.AMISTAD);
    }

    public int contarPersonas() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.sig) n++;
        return n;
    }

    /** Cada amistad se guarda como dos aristas, por eso se divide para 2. */
    public int contarAmistades() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.sig) n += contar(v, TipoRelacion.AMISTAD);
        return n / 2;
    }

    /** Total de aristas almacenadas (AMISTAD cuenta dos veces). */
    public int contarAristas() {
        int n = 0;
        for (Vertice v = cabeza; v != null; v = v.sig)
            for (Arista a = v.aristas; a != null; a = a.sig) n++;
        return n;
    }

    /** Texto con todos los vértices y sus aristas, en el orden de la lista. */
    public String mostrarRed() {
        if (cabeza == null) return "(red vacía)\n";
        StringBuilder sb = new StringBuilder();
        for (Vertice v = cabeza; v != null; v = v.sig) {
            sb.append("[").append(v.persona.usuario.toUpperCase()).append("] ")
              .append(v.persona.nombre)
              .append(" -> AMISTAD: ").append(salientes(v, TipoRelacion.AMISTAD))
              .append(" | SEGUIR: ").append(salientes(v, TipoRelacion.SEGUIR))
              .append("\n");
        }
        return sb.toString();
    }
}
