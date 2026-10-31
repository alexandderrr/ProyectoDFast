# Integrante 5: pagos, promociones y calificaciones

## Pagos

Base URL: `/api/pagos`

- `POST /api/pagos`: registra un pago.
- `GET /api/pagos`: lista pagos.
- `GET /api/pagos/pedido/{pedidoId}`: consulta los pagos de un pedido.

Ejemplo de pago en efectivo:

```json
{
  "tipo": "EFECTIVO",
  "monto": 85.00,
  "pedidoId": 1,
  "efectivoRecibido": 100.00
}
```

Ejemplo de pago con tarjeta (no enviar ni almacenar el número completo):

```json
{
  "tipo": "TARJETA",
  "monto": 85.00,
  "pedidoId": 1,
  "titularTarjeta": "Ana Pérez",
  "ultimosCuatro": "1234"
}
```

Ejemplo de transferencia:

```json
{
  "tipo": "TRANSFERENCIA",
  "monto": 85.00,
  "pedidoId": 1,
  "referenciaTransferencia": "TRX-2026-0001"
}
```

Validaciones: monto mayor que cero, pedido existente, efectivo recibido suficiente, últimos cuatro dígitos numéricos y referencia de transferencia obligatoria.

## Promociones

Base URL: `/api/promociones`

- `POST /api/promociones`: crea una promoción.
- `GET /api/promociones`: lista promociones.
- `POST /api/promociones/aplicar`: calcula el descuento y el total.

Tipos aceptados: `PORCENTAJE`, `MONTO_FIJO`, `ENVIO_GRATIS`.

Ejemplo de creación:

```json
{
  "codigo": "BIENVENIDA10",
  "descripcion": "Diez por ciento de descuento",
  "tipo": "PORCENTAJE",
  "valor": 10
}
```

Ejemplo de aplicación:

```json
{
  "codigo": "BIENVENIDA10",
  "subtotal": 100.00,
  "costoEnvio": 15.00
}
```

La respuesta incluye descuento, subtotal, costo de envío final y total. El cálculo usa importes enviados por el cliente; debe coordinarse con el servicio de pedidos para usar el subtotal y costo de envío oficiales del sistema.

## Calificaciones

Base URL: `/api/calificaciones`

- `POST /api/calificaciones`: registra una calificación.
- `GET /api/calificaciones/pedido/{pedidoId}`: lista calificaciones de un pedido.

Ejemplo:

```json
{
  "pedidoId": 1,
  "usuarioId": 2,
  "puntuacion": 5,
  "comentario": "Servicio excelente"
}
```

Validaciones: pedido y usuario existentes, puntuación entre 1 y 5 y una calificación por usuario para cada pedido. La respuesta expone identificadores y datos de calificación, no la contraseña del usuario.

## Pruebas manuales en Postman

1. Crear primero un usuario y un pedido válidos usando los endpoints del proyecto.
2. Enviar los ejemplos de pago con un `pedidoId` existente. Probar también monto cero, tipo inválido, efectivo insuficiente y transferencia sin referencia; se espera HTTP 400.
3. Crear una promoción con cada uno de los tres tipos y probar `POST /api/promociones/aplicar`. Probar código inexistente, porcentaje mayor a 100 y valores negativos; se espera HTTP 400.
4. Crear una calificación con puntuación 1 y otra con 5. Probar puntuación 0, 6, IDs inexistentes y repetir la calificación del mismo usuario/pedido; se espera HTTP 400.
5. Consultar las listas por pedido y verificar que los datos coincidan con lo registrado.

Nota: estos endpoints no procesan cobros reales ni verifican una transacción bancaria; registran el método y los datos de referencia proporcionados.
