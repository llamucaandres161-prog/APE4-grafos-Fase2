package pruebas;

import negocio.RedSocial;

/** Pruebas automatizadas sin librerías externas. Termina con código 1 si alguna falla. */
public class PruebasRedSocial {

    static int total = 0, fallidas = 0;

    static void verificar(String descripcion, boolean condicion) {
        total++;
        if (!condicion) fallidas++;
        System.out.println((condicion ? "[OK]    " : "[FALLA] ") + descripcion);
    }

    static boolean igual(String a, String b) { return a == null ? b == null : a.equals(b); }

    /** Red inicial de la Fase 1: 6 personas y 5 amistades. */
    static RedSocial redInicial() {
        RedSocial r = new RedSocial();
        r.registrarPersona("P01", "Lucas");  r.registrarPersona("P02", "Sofía");
        r.registrarPersona("P03", "Mateo");  r.registrarPersona("P04", "Valeria");
        r.registrarPersona("P05", "Daniel"); r.registrarPersona("P06", "Camila");
        r.agregarAmistad("P01", "P02"); r.agregarAmistad("P02", "P03"); r.agregarAmistad("P03", "P01");
        r.agregarAmistad("P01", "P04"); r.agregarAmistad("P04", "P05");
        return r;
    }

    public static void main(String[] args) {
        // ---- registrarPersona
        RedSocial r = new RedSocial();
        verificar("registrarPersona válido devuelve true", r.registrarPersona("P01", "Lucas"));
        verificar("registrarPersona rechaza usuario repetido", !r.registrarPersona("P01", "Otro"));
        verificar("registrarPersona rechaza p01 (P01 ya existe, sin distinguir mayúsculas)", !r.registrarPersona("p01", "Otro"));
        verificar("registrarPersona rechaza usuario vacío", !r.registrarPersona("", "Ana"));
        verificar("registrarPersona rechaza usuario con espacios", !r.registrarPersona("P 07", "Ana"));
        verificar("registrarPersona rechaza usuario con comas", !r.registrarPersona("P,07", "Ana"));
        verificar("registrarPersona rechaza null", !r.registrarPersona(null, "Ana"));
        verificar("contarPersonas = 1 tras los rechazos", r.contarPersonas() == 1);

        // ---- red inicial de la Fase 1
        r = redInicial();
        verificar("red inicial: 6 personas", r.contarPersonas() == 6);
        verificar("red inicial: 5 amistades", r.contarAmistades() == 5);
        verificar("red inicial: 10 aristas internas", r.contarAristas() == 10);
        verificar("grados 3,2,2,2,1,0",
                r.contarAmigos("P01") == 3 && r.contarAmigos("P02") == 2 && r.contarAmigos("P03") == 2
                && r.contarAmigos("P04") == 2 && r.contarAmigos("P05") == 1 && r.contarAmigos("P06") == 0);
        verificar("suma de grados = 2 x amistades", 3 + 2 + 2 + 2 + 1 + 0 == 2 * r.contarAmistades());
        verificar("la amistad es recíproca", r.sonAmigos("P01", "P02") && r.sonAmigos("P02", "P01"));

        // ---- agregarAmistad
        verificar("agregarAmistad rechaza lazo", !r.agregarAmistad("P03", "P03"));
        verificar("agregarAmistad rechaza duplicado", !r.agregarAmistad("P01", "P03"));
        verificar("agregarAmistad rechaza duplicado en orden inverso", !r.agregarAmistad("P03", "P01"));
        verificar("agregarAmistad rechaza usuario inexistente", !r.agregarAmistad("P01", "P99"));
        verificar("tras los rechazos siguen 5 amistades", r.contarAmistades() == 5);
        verificar("agregarAmistad acepta usuarios en minúscula", r.agregarAmistad("p05", "p06"));

        // ---- seguir / dejarDeSeguir
        r = redInicial();
        verificar("seguir válido devuelve true", r.seguir("P03", "P01"));
        verificar("seguir es dirigido: P03 sigue a P01", r.sigue("P03", "P01"));
        verificar("seguir es dirigido: P01 no sigue a P03", !r.sigue("P01", "P03"));
        verificar("seguir rechaza duplicado", !r.seguir("P03", "P01"));
        verificar("seguir rechaza lazo", !r.seguir("P01", "P01"));
        verificar("seguir permite a quien no tiene amigos", r.seguir("P06", "P01"));
        verificar("seguir no cambia el número de amigos", r.contarAmigos("P06") == 0);
        verificar("listarSeguidores(P01) incluye a Mateo y Camila",
                igual(r.listarSeguidores("P01"), "Camila, Mateo"));
        verificar("listarSeguidos(P03) = Lucas", igual(r.listarSeguidos("P03"), "Lucas"));
        verificar("dejarDeSeguir quita solo SEGUIR", r.dejarDeSeguir("P03", "P01") && r.sonAmigos("P03", "P01")
                && !r.sigue("P03", "P01"));
        verificar("dejarDeSeguir sin relación devuelve false", !r.dejarDeSeguir("P03", "P01"));

        // ---- listar / contar
        r = redInicial();
        verificar("listarAmigos(P01) en orden de lista", igual(r.listarAmigos("P01"), "Valeria, Mateo, Sofía"));
        verificar("listarAmigos de usuario sin amigos = Ninguno", igual(r.listarAmigos("P06"), "Ninguno"));
        verificar("listarAmigos de inexistente = null", r.listarAmigos("P99") == null);
        verificar("listarSeguidos de inexistente = null", r.listarSeguidos("P99") == null);
        verificar("listarSeguidores de inexistente = null", r.listarSeguidores("P99") == null);
        verificar("contarAmigos de inexistente = -1", r.contarAmigos("P99") == -1);
        verificar("búsqueda no distingue mayúsculas", r.contarAmigos("p01") == 3 && r.contarAmigos(" P01 ") == 3);

        // ---- eliminarAmistad
        verificar("eliminarAmistad devuelve true", r.eliminarAmistad("P04", "P05"));
        verificar("eliminarAmistad quita ambos sentidos", !r.sonAmigos("P04", "P05") && !r.sonAmigos("P05", "P04"));
        verificar("eliminarAmistad actualiza grados", r.contarAmigos("P04") == 1 && r.contarAmigos("P05") == 0);
        verificar("eliminarAmistad repetida devuelve false", !r.eliminarAmistad("P04", "P05"));
        verificar("eliminarAmistad no toca SEGUIR", r.seguir("P04", "P05") && r.eliminarAmistad("P01", "P04")
                && r.sigue("P04", "P05"));

        // ---- eliminarPersona
        r = redInicial();
        r.seguir("P03", "P01");
        r.seguir("P06", "P01");
        verificar("eliminarPersona(P01) devuelve true", r.eliminarPersona("P01"));
        verificar("eliminarPersona deja 5 personas", r.contarPersonas() == 5);
        verificar("eliminarPersona borra al usuario", r.listarAmigos("P01") == null);
        verificar("eliminarPersona limpia amistades entrantes",
                r.contarAmigos("P02") == 1 && r.contarAmigos("P03") == 1 && r.contarAmigos("P04") == 1);
        verificar("eliminarPersona limpia SEGUIR entrantes",
                igual(r.listarSeguidos("P03"), "Ninguno") && igual(r.listarSeguidos("P06"), "Ninguno"));
        verificar("quedan 2 amistades (P02-P03 y P04-P05)", r.contarAmistades() == 2 && r.contarAristas() == 4);
        verificar("eliminarPersona repetida devuelve false", !r.eliminarPersona("P01"));
        verificar("se puede registrar de nuevo un usuario eliminado", r.registrarPersona("P01", "Lucas"));
        verificar("el usuario reingresado no conserva relaciones", r.contarAmigos("P01") == 0
                && igual(r.listarSeguidores("P01"), "Ninguno"));
        r = redInicial();
        verificar("eliminar el último vértice de la lista (P01)", r.eliminarPersona("P01") && r.contarPersonas() == 5);
        verificar("eliminar el primer vértice de la lista (P06)", r.eliminarPersona("P06") && r.contarPersonas() == 4);
        verificar("eliminar un vértice del medio (P03)", r.eliminarPersona("P03") && r.contarPersonas() == 3);

        System.out.println();
        System.out.println("Pruebas ejecutadas: " + total + " | Correctas: " + (total - fallidas) + " | Fallidas: " + fallidas);
        if (fallidas > 0) System.exit(1);
    }
}
