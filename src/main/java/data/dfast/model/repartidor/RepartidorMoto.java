package data.dfast.model.repartidor;

public class RepartidorMoto extends Repartidor {

    public RepartidorMoto(Long id, String nombre, String zona, boolean disponible) {
        super(id, nombre, zona, disponible);
    }

    @Override public String getTipoVehiculo() { return "MOTO"; }
    @Override public double getVelocidadPromedioKmH() { return 35; }
    @Override public int getCapacidadMaximaPedidos() { return 3; }
    @Override public double getPesoMaximoKg() { return 15; }
    @Override protected double getTarifaPorKm() { return 3.0; }
}
