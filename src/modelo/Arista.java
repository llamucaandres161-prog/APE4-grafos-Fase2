package modelo;

/** Nodo de la lista de aristas de un vértice (lista enlazada propia). */
public class Arista {

    public Vertice destino;
    public TipoRelacion tipo;
    public Arista sig;

    public Arista(Vertice destino, TipoRelacion tipo, Arista sig) {
        this.destino = destino;
        this.tipo = tipo;
        this.sig = sig;
    }
}
