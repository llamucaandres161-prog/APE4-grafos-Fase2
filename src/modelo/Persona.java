package modelo;

/** Datos de un usuario de la red. El identificador se guarda en minúsculas. */
public class Persona {

    public final String usuario;
    public final String nombre;

    public Persona(String usuario, String nombre) {
        this.usuario = usuario;
        this.nombre = nombre;
    }
}
