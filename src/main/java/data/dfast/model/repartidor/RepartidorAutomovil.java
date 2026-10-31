package data.dfast.model.repartidor;

public class RepartidorAutomovil extends Repartidor {

    public RepartidorAutomovil(Long id, String nombre, String zona, boolean disponible) {
        super(id, nombre, zona, disponible);
    }

    @Override public String getTipoVehiculo() { return "AUTOMOVIL"; }
    @Override public double getVelocidadPromedioKmH() { return 30; }
    @Override public int getCapacidadMaximaPedidos() { return 5; }
    @Override public double getPesoMaximoKg() { return 60; }
    @Override protected double getTarifaPorKm() { return 5.0; }
}
