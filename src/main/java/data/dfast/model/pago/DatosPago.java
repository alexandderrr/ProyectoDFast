package data.dfast.model.pago;

/** Datos que el cliente envía para pagar (solo se usan los del método elegido). */
public record DatosPago(String numeroTarjeta, String titularTarjeta,
                        String vencimientoTarjeta, String cvv,
                        String referenciaTransferencia) {

    public static DatosPago vacio() {
        return new DatosPago(null, null, null, null, null);
    }
}
