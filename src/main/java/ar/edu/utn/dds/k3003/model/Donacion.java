package ar.edu.utn.dds.k3003.model;

public class Donacion {

    private String categoria;
    private Integer cantidad;
    private boolean aceptada;
    private boolean conQueja;

    public Donacion(
            String categoria,
            Integer cantidad,
            boolean aceptada,
            boolean conQueja
    ) {
        this.categoria = categoria;
        this.cantidad = cantidad;
        this.aceptada = aceptada;
        this.conQueja = conQueja;
    }

    public String getCategoria() {
        return categoria;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public boolean isAceptada() {
        return aceptada;
    }

    public boolean isConQueja() {
        return conQueja;
    }
}