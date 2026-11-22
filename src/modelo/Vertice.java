package modelo;

/** Nodo de la lista de vértices (multilista): cada vértice guarda su propia lista de aristas. */
public class Vertice {

    public Persona persona;
    public Arista aristas;   // cabeza de la lista de aristas de este vértice
    public Vertice sig;      // siguiente vértice de la red

    public Vertice(Persona persona) {
        this.persona = persona;
    }
}
