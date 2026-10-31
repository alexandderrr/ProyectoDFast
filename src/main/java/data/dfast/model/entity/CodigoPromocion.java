package data.dfast.model.entity;

import jakarta.persistence.*;

/** Código de promoción guardado en la base (el cálculo lo hace model.promocion.Promocion). */
@Entity
@Table(name = "codigos_promocion")
public class CodigoPromocion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String codigo;

    // PORCENTAJE, MONTO_FIJO o ENVIO_GRATIS
    @Column(nullable = false)
    private String tipo;

    private Double valor;

    private Boolean activa = Boolean.TRUE;

    public CodigoPromocion() {
    }

    public CodigoPromocion(String codigo, String tipo, Double valor) {
        this.codigo = codigo;
        this.tipo = tipo;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public boolean isActiva() { return activa == null || activa; }
    public void setActiva(Boolean activa) { this.activa = activa; }
}
