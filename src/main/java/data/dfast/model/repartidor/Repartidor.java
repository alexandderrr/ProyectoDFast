package data.dfast.model.repartidor;

import data.dfast.exception.ReglaNegocioException;
import data.dfast.interfaces.Calificable;
import data.dfast.interfaces.Notificable;
import data.dfast.interfaces.Rastreable;

/**
 * Clase abstracta con el comportamiento común de todos los repartidores.
 * Cada tipo de vehículo define sus propias capacidades y tarifa.
 */
public abstract class Repartidor implements Rastreable, Calificable, Notificable {

    private static final double TARIFA_BASE = 10.0;

    private final Long id;
    private final String nombre;
    private final String zona;
    private boolean disponible;
    private int pedidosActivos;
    private double sumaCalificaciones;
    private int totalCalificaciones;

    protected Repartidor(Long id, String nombre, String zona, boolean disponible) {
        this.id = id;
        this.nombre = nombre;
        this.zona = zona;
        this.disponible = disponible;
    }

    // ---- Comportamiento propio de cada vehículo (lo redefinen las subclases) ----
    public abstract String getTipoVehiculo();
    public abstract double getVelocidadPromedioKmH();
    public abstract int getCapacidadMaximaPedidos();
    public abstract double getPesoMaximoKg();
    protected abstract double getTarifaPorKm();

    // ---- Comportamiento común ----
    public double calcularCostoEnvio(double distanciaKm) {
        return TARIFA_BASE + distanciaKm * getTarifaPorKm();
    }

    public int calcularTiempoEstimadoMin(double distanciaKm) {
        return (int) Math.ceil(distanciaKm / getVelocidadPromedioKmH() * 60);
    }

    public boolean puedeTomarPedido(double pesoKg) {
        return disponible
                && pedidosActivos < getCapacidadMaximaPedidos()
                && pesoKg <= getPesoMaximoKg();
    }

    @Override
    public void registrarCalificacion(int puntos) {
        if (puntos < 1 || puntos > 5) {
            throw new ReglaNegocioException("La calificación debe estar entre 1 y 5.");
        }
        sumaCalificaciones += puntos;
        totalCalificaciones++;
    }

    @Override
    public double getPromedioCalificacion() {
        return totalCalificaciones == 0 ? 0 : sumaCalificaciones / totalCalificaciones;
    }

    @Override
    public String generarNotificacion(String mensaje) {
        return "[Notificación para " + nombre + " (" + getTipoVehiculo() + ")] " + mensaje;
    }

    @Override
    public String consultarUbicacion() {
        return "Repartidor " + nombre + " operando en la zona " + zona;
    }

    /** Carga el historial de calificaciones guardado en la base de datos. */
    public void cargarHistorial(double promedio, int total) {
        this.totalCalificaciones = total;
        this.sumaCalificaciones = promedio * total;
    }

    public void incrementarPedidosActivos() { pedidosActivos++; }

    public void decrementarPedidosActivos() {
        if (pedidosActivos > 0) {
            pedidosActivos--;
        }
    }

    // ---- Getters / setters (encapsulamiento) ----
    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getZona() { return zona; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public int getPedidosActivos() { return pedidosActivos; }
    public void setPedidosActivos(int pedidosActivos) { this.pedidosActivos = pedidosActivos; }
    public int getTotalCalificaciones() { return totalCalificaciones; }
}
