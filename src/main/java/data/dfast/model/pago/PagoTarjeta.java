package data.dfast.model.pago;

import data.dfast.exception.PagoInvalidoException;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/** Pago con tarjeta: valida los datos y simula el cobro. */
public class PagoTarjeta extends Pago {

    private final String numero;
    private final String titular;
    private final String vencimiento; // formato MM/yy
    private final String cvv;

    public PagoTarjeta(double monto, String numero, String titular,
                       String vencimiento, String cvv) {
        super(monto);
        this.numero = numero == null ? "" : numero.replaceAll("[\\s-]", "");
        this.titular = titular;
        this.vencimiento = vencimiento;
        this.cvv = cvv;
    }

    @Override public String getMetodo() { return "TARJETA"; }

    @Override
    protected void validar() {
        if (titular == null || titular.isBlank()) {
            throw new PagoInvalidoException("Falta el nombre del titular de la tarjeta.");
        }
        if (!numero.matches("\\d{13,19}") || !pasaLuhn(numero)) {
            throw new PagoInvalidoException("El número de tarjeta no es válido.");
        }
        if (cvv == null || !cvv.matches("\\d{3,4}")) {
            throw new PagoInvalidoException("El CVV no es válido.");
        }
        try {
            YearMonth vence = YearMonth.parse(vencimiento, DateTimeFormatter.ofPattern("MM/yy"));
            if (vence.isBefore(YearMonth.now())) {
                throw new PagoInvalidoException("La tarjeta está vencida.");
            }
        } catch (DateTimeParseException | NullPointerException e) {
            throw new PagoInvalidoException("La fecha de vencimiento debe tener formato MM/yy.");
        }
    }

    @Override
    protected ResultadoPago ejecutarCobro() {
        String ultimos4 = numero.substring(numero.length() - 4);
        return new ResultadoPago(getMetodo(), getMonto(), generarReferencia("TAR"),
                "Cobro aprobado a la tarjeta terminada en " + ultimos4);
    }

    /** Algoritmo de Luhn: el estándar para validar números de tarjeta. */
    private static boolean pasaLuhn(String digitos) {
        int suma = 0;
        boolean duplicar = false;
        for (int i = digitos.length() - 1; i >= 0; i--) {
            int d = digitos.charAt(i) - '0';
            if (duplicar) {
                d *= 2;
                if (d > 9) {
                    d -= 9;
                }
            }
            suma += d;
            duplicar = !duplicar;
        }
        return suma % 10 == 0;
    }
}
