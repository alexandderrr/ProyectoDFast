package data.dfast.model.repartidor;

public class RepartidorBicicleta extends Repartidor {

    public RepartidorBicicleta(Long id, String nombre, String zona, boolean disponible) {
        super(id, nombre, zona, disponible);
    }

    @Override public String getTipoVehiculo() { return "BICICLETA"; }
    @Override public double getVelocidadPromedioKmH() { return 15; }
    @Override public int getCapacidadMaximaPedidos() { return 1; }
    @Override public double getPesoMaximoKg() { return 8; }
    @Override protected double getTarifaPorKm() { return 2.0; }
}
