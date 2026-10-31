package data.dfast.interfaces;

/** Algo que puede recibir calificaciones de 1 a 5 y calcular su promedio. */
public interface Calificable {
    void registrarCalificacion(int puntos);
    double getPromedioCalificacion();
}
