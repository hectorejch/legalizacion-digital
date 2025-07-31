package ar.com.cnpmweb.legalizaciondigital.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Clase utilitaria para operaciones comunes con fechas.
 */
public class DateUtils {

    private DateUtils() {
        // Constructor privado para evitar instanciación
    }
    
    /**
     * Crea una fecha que representa '0000-00-00' en MySQL.
     * @return Una fecha (01/01/0001) que se puede usar para comparar con fechas
     * nulas en la base de datos.
     */
    public static Date crearFechaCero() {
        try {
            // Primero intentamos parsear directamente "0000-00-00"
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sdf.setLenient(false);
            return sdf.parse("0000-00-00");
        } catch (ParseException e) {
            // Si falla, creamos una fecha muy antigua (01/01/0001)
            Calendar cal = Calendar.getInstance();
            cal.set(Calendar.YEAR, 1);
            cal.set(Calendar.MONTH, Calendar.JANUARY);
            cal.set(Calendar.DAY_OF_MONTH, 1);
            cal.set(Calendar.HOUR_OF_DAY, 0);
            cal.set(Calendar.MINUTE, 0);
            cal.set(Calendar.SECOND, 0);
            cal.set(Calendar.MILLISECOND, 0);
            return cal.getTime();
        }
    }
    
    /**
     * Verifica si una fecha está dentro de un rango dado (inclusive).
     * La fecha está en el rango si: fechaInicio <= fecha <= fechaFin
     * 
     * @param fecha Fecha a verificar
     * @param fechaInicio Fecha de inicio del rango
     * @param fechaFin Fecha de fin del rango (puede ser null o una fecha nula '0000-00-00')
     * @return true si la fecha está dentro del rango
     */
    public static boolean fechaEstaDentroDeRango(Date fecha, Date fechaInicio, Date fechaFin) {
        if (fecha == null || fechaInicio == null) {
            return false;
        }

        // Si la fecha de fin es nula, verificamos si es posterior o igual a la fecha inicio
        if (fechaFin == null) {
            return fecha.compareTo(fechaInicio) >= 0;
        }
        
        // Si la fecha de fin es una fecha nula ('0000-00-00'), se interpreta como "sin fin"
        if (esFechaCero(fechaFin)) {
            return fecha.compareTo(fechaInicio) >= 0;
        }

        // Verificar si la fecha está entre fechaInicio y fechaFin (inclusive)
        return fecha.compareTo(fechaInicio) >= 0 && fecha.compareTo(fechaFin) <= 0;
    }
    
    /**
     * Verifica si una fecha es la fecha nula que representa '0000-00-00'.
     * 
     * @param fecha Fecha a verificar
     * @return true si la fecha representa '0000-00-00'
     */
    public static boolean esFechaCero(Date fecha) {
        if (fecha == null) {
            return false;
        }
        
        Date fechaNula = crearFechaCero();
        return fecha.equals(fechaNula);
    }
}