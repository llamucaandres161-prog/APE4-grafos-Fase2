package app;

import negocio.RedSocial;
import java.util.Scanner;

/** APE 4 · Fase 2: demostración de «Conexión Ambato» siguiendo la secuencia de comprobación. */
public class Main {

    static void t(String llamada, Object resultado) {
        System.out.println(llamada + " -> " + resultado);
    }

    public static void main(String[] args) {
        RedSocial red = new RedSocial();

        System.out.println("=== PASO 1A: registrarPersona y mostrarRed ===");
        t("registrarPersona(P01, Lucas)", red.registrarPersona("P01", "Lucas"));
        t("registrarPersona(P02, Sofía)", red.registrarPersona("P02", "Sofía"));
        t("registrarPersona(P03, Mateo)", red.registrarPersona("P03", "Mateo"));
        t("registrarPersona(P04, Valeria)", red.registrarPersona("P04", "Valeria"));
        t("registrarPersona(P05, Daniel)", red.registrarPersona("P05", "Daniel"));
        t("registrarPersona(P06, Camila)", red.registrarPersona("P06", "Camila"));
        System.out.println("--- mostrarRed() ---");
        System.out.print(red.mostrarRed());

        System.out.println("\n=== PASO 1B: agregarAmistad ===");
        t("agregarAmistad(P01, P02)", red.agregarAmistad("P01", "P02"));
        t("agregarAmistad(P02, P03)", red.agregarAmistad("P02", "P03"));
        t("agregarAmistad(P03, P01)", red.agregarAmistad("P03", "P01"));
        t("agregarAmistad(P01, P04)", red.agregarAmistad("P01", "P04"));
        t("agregarAmistad(P04, P05)", red.agregarAmistad("P04", "P05"));
        System.out.println("Personas: " + red.contarPersonas() + " | Amistades: " + red.contarAmistades()
                + " | Aristas internas: " + red.contarAristas());
        System.out.println("Grados (P01..P06): " + red.contarAmigos("P01") + ", " + red.contarAmigos("P02") + ", "
                + red.contarAmigos("P03") + ", " + red.contarAmigos("P04") + ", " + red.contarAmigos("P05") + ", "
                + red.contarAmigos("P06"));
        System.out.println("--- mostrarRed() ---");
        System.out.print(red.mostrarRed());

        System.out.println("\n=== PASO 2: seguir(P03, P01) ===");
        t("seguir(P03, P01)", red.seguir("P03", "P01"));
        t("listarSeguidores(P01)", red.listarSeguidores("P01"));

        System.out.println("\n=== PASO 3: seguir(P06, P01) ===");
        t("seguir(P06, P01)", red.seguir("P06", "P01"));
        t("listarSeguidos(P06)", red.listarSeguidos("P06"));
        t("contarAmigos(P06)", red.contarAmigos("P06"));
        t("seguir(P03, P01) otra vez", red.seguir("P03", "P01"));

        System.out.println("\n=== PASO 4: listarAmigos, listarSeguidos, listarSeguidores y contarAmigos ===");
        t("listarAmigos(P01)", red.listarAmigos("P01"));
        t("listarSeguidos(P03)", red.listarSeguidos("P03"));
        t("listarSeguidores(P01)", red.listarSeguidores("P01"));
        t("listarSeguidores(P03)", red.listarSeguidores("P03"));
        t("contarAmigos(P01)", red.contarAmigos("P01"));
        t("listarAmigos(P99)", red.listarAmigos("P99"));
        t("contarAmigos(P99)", red.contarAmigos("P99"));

        System.out.println("\n=== PASO 5: eliminarAmistad(P04, P05) ===");
        t("eliminarAmistad(P04, P05)", red.eliminarAmistad("P04", "P05"));
        t("listarAmigos(P04)", red.listarAmigos("P04"));
        t("listarAmigos(P05)", red.listarAmigos("P05"));
        t("eliminarAmistad(P04, P05) otra vez", red.eliminarAmistad("P04", "P05"));

        System.out.println("\n=== PASO 6: eliminarPersona(P01) ===");
        t("eliminarPersona(P01)", red.eliminarPersona("P01"));
        t("eliminarPersona(P01) otra vez", red.eliminarPersona("P01"));
        System.out.println("Personas: " + red.contarPersonas() + " | Amistades: " + red.contarAmistades());
        t("listarAmigos(P02)", red.listarAmigos("P02"));
        t("listarSeguidos(P03)", red.listarSeguidos("P03"));
        t("listarSeguidos(P06)", red.listarSeguidos("P06"));
        System.out.println("--- mostrarRed() ---");
        System.out.print(red.mostrarRed());

        System.out.println("\n=== PASO 7: registrarPersona con usuario duplicado ===");
        t("registrarPersona(P02, otro nombre)", red.registrarPersona("P02", "otro nombre"));
        t("registrarPersona(p02, Otro)", red.registrarPersona("p02", "Otro"));

        System.out.println("\n=== PASO 8: agregarAmistad con lazo ===");
        t("agregarAmistad(P03, P03)", red.agregarAmistad("P03", "P03"));
        t("agregarAmistad(P03, P99)", red.agregarAmistad("P03", "P99"));

        if (args.length > 0 && args[0].equals("-i")) menu(red);
    }

    static void menu(RedSocial red) {
        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- MENÚ CONEXIÓN AMBATO ---");
            System.out.println("1. Registrar persona      2. Eliminar persona");
            System.out.println("3. Agregar amistad        4. Eliminar amistad");
            System.out.println("5. Seguir                 6. Dejar de seguir");
            System.out.println("7. Listar amigos          8. Listar seguidos");
            System.out.println("9. Listar seguidores     10. Contar amigos");
            System.out.println("11. Mostrar red           0. Salir");
            System.out.print("Opción: ");
            String op = sc.nextLine().trim();
            switch (op) {
                case "1": System.out.print("Usuario: "); String u = sc.nextLine();
                          System.out.print("Nombre: "); System.out.println(red.registrarPersona(u, sc.nextLine())); break;
                case "2": System.out.print("Usuario: "); System.out.println(red.eliminarPersona(sc.nextLine())); break;
                case "3": System.out.print("Usuario A: "); String a3 = sc.nextLine();
                          System.out.print("Usuario B: "); System.out.println(red.agregarAmistad(a3, sc.nextLine())); break;
                case "4": System.out.print("Usuario A: "); String a4 = sc.nextLine();
                          System.out.print("Usuario B: "); System.out.println(red.eliminarAmistad(a4, sc.nextLine())); break;
                case "5": System.out.print("Quien sigue: "); String a5 = sc.nextLine();
                          System.out.print("A quién sigue: "); System.out.println(red.seguir(a5, sc.nextLine())); break;
                case "6": System.out.print("Quien deja de seguir: "); String a6 = sc.nextLine();
                          System.out.print("A quién: "); System.out.println(red.dejarDeSeguir(a6, sc.nextLine())); break;
                case "7": System.out.print("Usuario: "); System.out.println(red.listarAmigos(sc.nextLine())); break;
                case "8": System.out.print("Usuario: "); System.out.println(red.listarSeguidos(sc.nextLine())); break;
                case "9": System.out.print("Usuario: "); System.out.println(red.listarSeguidores(sc.nextLine())); break;
                case "10": System.out.print("Usuario: "); System.out.println(red.contarAmigos(sc.nextLine())); break;
                case "11": System.out.print(red.mostrarRed()); break;
                case "0": return;
                default: System.out.println("Opción inválida.");
            }
        }
    }
}
