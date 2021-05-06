package pe.com.ocv.util;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.cxf.common.util.StringUtils;

public class ParametrosUtil {
	
	/**
	 * Recorta una cadena desde el lado izquierdo
	 * 
	 * @param cadena
	 * @param longitud
	 * @return
	 */
	public static String left(String cadena, int longitud)
	{
		String result = "";

		if (longitud > cadena.length())
		{
			result = cadena;
		}
		else
		{
			result = cadena.substring(0, longitud);
		}

		return result;
	}
	/**
	 * Recorta una cadena desde el lado derecho
	 * 
	 * @param cadena
	 * @param longitud
	 * @return
	 */
	public static String right(String cadena, int longitud)
	{
		String result = "";

		if (longitud > cadena.length())
		{
			result = cadena;
		}
		else
		{
			result = cadena.substring(cadena.length() - longitud);
		}

		return result;
	}	
	
	/**
	 * Permite extraer una subcadena de una cadena principal dado un valor
	 * inicial y una longitud
	 * 
	 * @param cadena
	 * @param inicio
	 * @return
	 */
	public static String mid(String cadena, int inicio)
	{
		String result = "";

		if (inicio > cadena.length())
		{
			result = "";
		}
		else
		{
			inicio = inicio - 1;
			result = cadena.substring(inicio);
		}

		return result;
	}
	/**
	 * Permite extraer una subcadena de una cadena principal dado un valor
	 * inicial y una longitud
	 * 
	 * @param cadena
	 * @param inicio
	 * @param longitud
	 * @return
	 */
	public static String mid(String cadena, int inicio, int longitud)
	{
		String result = "";

		if (inicio > cadena.length())
		{
			result = "";
		}
		else
		{
			inicio = inicio - 1;
			if (inicio+longitud>cadena.length()){
				result = cadena.substring(inicio, cadena.length());
			}else{
				result = cadena.substring(inicio, inicio + longitud);
			}
		}

		return result;
	}

	/**
	 * Convierte una cadena de caracteres a mayúscula
	 * 
	 * @param cadena
	 * @return
	 */
	public static String uCase(String cadena)
	{
		return cadena.toUpperCase();
	}

	/**
	 * Convierte una cadena de caracteres a minúsculas
	 * 
	 * @param cadena
	 * @return
	 */
	public static String lCase(String cadena)
	{
		return cadena.toLowerCase();
	}
	/**
	 * Devuelve un valor numérico que especifica la posición de la primera
	 * aparición de una cadena en otra, desde el principio de la cadena
	 * 
	 * @param cadena
	 * @param cadenaBuscada
	 * @return
	 */
	public static int inStr(String cadena, String cadenaBuscada)
	{
		int posicion = cadena.indexOf(cadenaBuscada);
		if (posicion == -1)
		{
			return 0;
		}
		else
		{
			return posicion + 1;
		}

	}
	/**
	 * Devuelve la longitud de una cadena de caracteres
	 * 
	 * @param cadena
	 * @return
	 */
	public static int len(String cadena)
	{
		return cadena.length();
	}
	/**
	 * Quita espacios en blanco al inicio y fin de una cadena de caracteres
	 * 
	 * @param cadena
	 * @return
	 */
	public static String trim(String cadena)
	{
		return cadena.trim();
	}
	/**
	 * Devuelve la representaci�n String de un n�mero.
	 * 
	 * @param numero
	 * @return
	 */
	public static String str(int numero)
	{
		return String.valueOf(numero);
	}

	/**
	 * Devuelve la representaci�n String de un n�mero.
	 * 
	 * @param numero
	 * @return
	 */
	public static String str(double numero)
	{
		return String.valueOf(numero);
	}
	/**
	 * Adiciona o recorta espacios a la cadena de acuerdo a la longitud
	 * @param cadena
	 * @param longitud
	 * @return
	 */
	public static String rightBlankPad(String cadena,int longitud){
		String cadenaRepetida=string(longitud," ");
		cadena=cadena+cadenaRepetida;
		cadena=left(cadena, longitud);
		return cadena;		
	}
	
	/* Método que rellena de ceros el lado derecho */
	public static String padRight(String str,String pad){
		String retorno="";
		if(str !=null && pad!=null){
			retorno = str + pad.substring(0, pad.length() - str.length());
		}
		return retorno;		
	}

	/**
	 * Repite una cadena de caracteres n (cantidad) veces
	 * 
	 * @param cantidad
	 * @param cadenaRepetir
	 * @return
	 */
	public static String string(int cantidad, String cadenaRepetir)
	{
		StringBuffer sb = new StringBuffer();

		for (int i = 0; i < cantidad; i++)
		{
			sb.append(cadenaRepetir);
		}

		return sb.toString();
	}
	/* Método que rellena de ceros el lado izquierdo  */
	public static String padleft(String str,String pad){
		String retorno="";
		if(str !=null && pad!=null){
		retorno = pad.substring(0, pad.length() - str.length()) + str;
		}
		return retorno;		
	}
	/**
	 * Metodo que permite obtener la fecha actual
	 * @return fecha actual formateada
	 */
	public static String getFechaActual() {
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
	    return formateador.format(ahora);
	}
	
	public static String getHoraActual() {
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");	
	    String hora = formateador.format(ahora);
	    String arr[] = hora.split(":");
	    return arr[0]+":"+arr[1];
	}
	
	public static String getFechaHoraActual() {
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	    return formateador.format(ahora);
	}
	
	/**
	 * Metodo que permite obtener la fecha actual
	 * @return fecha actual formateada
	 */
	public static String getFechaActualconFormato(String formato) {
	    Date ahora = new Date();
	    SimpleDateFormat formateador = new SimpleDateFormat(formato);
	    return formateador.format(ahora);
	}
	
	
	public static Timestamp getFechaActualTimestamp(){
		Timestamp timestamp = null;

		try{
			
			Date ahora = new Date();
			timestamp = new java.sql.Timestamp(ahora.getTime());			

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return timestamp;
	}
	
	
	/**
	 * Metodo que permite obtener la fecha actual
	 * @return fecha actual formateada
	 */
	public static String getFechaconFormato(Date date, String formato) {
	    SimpleDateFormat formateador = new SimpleDateFormat(formato);
	    return formateador.format(date);
	}
	/**
	 * Metodo que permite obtener la fecha actual
	 * @return fecha actual formateada
	 */
	public static Date getFechaActualToDate() {
		Calendar now = Calendar.getInstance();		
	    return now.getTime();
	}
	public static Date getFechaActualToDateHour(){
		String formato = "dd/MM/yyyy hh:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato); //"yyyy-MM-dd hh:mm:ss.SSS");
		Date parsedDate = new Date();
		try {
			parsedDate = dateFormat.parse(getFechaHoraActual());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return parsedDate;
		
	}
	public static String getFechaActualToDateHourString(){
		String formato = "dd/MM/yyyy hh:mm:ss";
		SimpleDateFormat dateFormat = new SimpleDateFormat(formato); //"yyyy-MM-dd hh:mm:ss.SSS");
		Date parsedDate = new Date();
		return dateFormat.format(parsedDate);
		
	}
	 public static Date sumarRestarDiasFecha(Date fecha, int dias){
		       Calendar calendar = Calendar.getInstance();
		       calendar.setTime(fecha); // Configuramos la fecha que se recibe
		       calendar.add(Calendar.DAY_OF_YEAR, dias);  // numero de días a añadir, o restar en caso de días<0
		       return calendar.getTime(); // Devuelve el objeto Date con los nuevos días añadidos

	 }
	 
	public static Timestamp getFechaToTimestamp(String fecha, String formato){
		Timestamp timestamp = null;

		try{
			
			if(!StringUtils.isEmpty(fecha)){
				SimpleDateFormat dateFormat = new SimpleDateFormat(formato); //"yyyy-MM-dd hh:mm:ss.SSS");
				Date parsedDate = dateFormat.parse(fecha);
				timestamp = new java.sql.Timestamp(parsedDate.getTime());
			}

		}catch(Exception ex){
			ex.printStackTrace();
		}

		return timestamp;
	}
	
	public static Timestamp convertirDateaTimestamp(Date fecha){
		Timestamp timestamp = null;
		try{
			timestamp = new java.sql.Timestamp(fecha.getTime());
		}catch(Exception ex){
			ex.printStackTrace();
		}
		return timestamp;
	}
	
	/*
	 * Permite convertir un string dd/MM/yyyy to Timestamp
	 * */
	public static Timestamp convertStringToTimestamp(String fecha) throws ParseException{
	    Timestamp timestamp = null;  //declare timestamp
		try{
		    SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyy");
		    Date d=dt.parse(fecha); 
		    		//new Date(fecha); // Intialize date with the string date
		    if(d!=null){  // simple null check
		    	timestamp = new java.sql.Timestamp(d.getTime());
		    }
		}catch (Exception e) {
			return null;
		}	    
	    return timestamp;
	}
	
	/*
	 * Permite convertir y  Timestamp a  un string dd/MM/yyy
	 * */
	public static String convertTimestampToString(Timestamp timestamp) throws ParseException{
	   
	    Date date = new Date();
	    date.setTime(timestamp.getTime());
	    
	    String dateString = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(date);
	    return dateString;
	}
	
	/*
	 * Permite convertir un string dd/MM/yyyy to Date
	 * */
	public static Date convertObjectToDate(Object objeto){
	    Date fecha = null;
		try{
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	        String dateInString = String.valueOf(objeto);
        	fecha = formatter.parse(dateInString);
		} catch(Exception e) {
			fecha = null;
		}
	    return fecha;
	}
	
	/*
	 * Permite convertir un objeto to Date con formato
	 * */
	public static Date convertObjectToDateFormat(Object objeto, String formato){
	    Date fecha = null;
		try{
			SimpleDateFormat formatter = new SimpleDateFormat(formato);
	        String dateInString = String.valueOf(objeto);
        	fecha = formatter.parse(dateInString);
		} catch(Exception e) {
			fecha = null;
		}
	    return fecha;
	}
	
	public static Timestamp convertStringToTimestamp(String fecha, String formato) throws ParseException{
	    Timestamp timestamp = null;  //declare timestamp
	    
	    try{

	    	if(!fecha.equals("")){
	    		SimpleDateFormat dt = new SimpleDateFormat(formato);
	    		Date d=dt.parse(fecha); 
	    		//new Date(fecha); // Intialize date with the string date
	    		if(d!=null){  // simple null check
	    			timestamp = new java.sql.Timestamp(d.getTime());
	    		}
	    	}

	    }catch(Exception excepcion){
	    	timestamp = null;
	    }

	    return timestamp;
	}
	
	
	
	/*
	 * Permite convertir un string dd/MM/yyyy to Timestamp y time hh:mm:ss
	 * caso 1: time hh:mm
	 * caso 2: time mm:ss
	 * caso 3: time hh:mm:ss
	 * */
	@SuppressWarnings("deprecation")
	public static Timestamp convertStringToTimestamp(String fecha, String time, Integer caso){
	    Timestamp timestamp = null;  //declare timestamp
	    Date d=new Date(fecha); // Intialize date with the string date
	    
	    if(d!=null){  // simple null check
	    	timestamp = new java.sql.Timestamp(d.getTime());
	    	
	    	addTimeToTimestamp(timestamp, time, caso);
	    }
	    
	    return timestamp;
	}
	
	@SuppressWarnings("deprecation")
	public static Timestamp addTimeToTimestamp(Timestamp timestamp, String time, Integer caso){
		
		switch(caso){
    	case 1:
    		timestamp.setHours(Integer.parseInt(time.substring(0,2)));
    		timestamp.setMinutes(Integer.parseInt(time.substring(3,5)));
    		break;
    	case 2:
    		timestamp.setMinutes(Integer.parseInt(time.substring(0,2)));
    		timestamp.setSeconds(Integer.parseInt(time.substring(3,5)));
    		break;
    	case 3:
    		timestamp.setHours(Integer.parseInt(time.substring(0,2)));
    		timestamp.setMinutes(Integer.parseInt(time.substring(3,5)));
    		timestamp.setSeconds(Integer.parseInt(time.substring(6,7)));
    		break;
    	}
		return timestamp;
	}
	
	@SuppressWarnings("deprecation")
	public static Timestamp addTimeToTimestamp(String time, Integer caso){	
	
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		switch(caso){
    	case 1:
    		timestamp.setHours(Integer.parseInt(time.substring(0,2)));
    		timestamp.setMinutes(Integer.parseInt(time.substring(3,5)));
    		break;
    	case 2:
    		timestamp.setMinutes(Integer.parseInt(time.substring(0,2)));
    		timestamp.setSeconds(Integer.parseInt(time.substring(3,5)));
    		break;
    	case 3:
    		timestamp.setHours(Integer.parseInt(time.substring(0,2)));
    		timestamp.setMinutes(Integer.parseInt(time.substring(3,5)));
    		timestamp.setSeconds(Integer.parseInt(time.substring(6,7)));
    		break;
    	}
		return timestamp;
			    
	}
	
	/*
	 * Permite acceder a un atributo del objeto para setear un valor
	 */
	public static void setValueToFieldObject(Object object, String fieldName, Object value){
		try {
			Field field = object.getClass().getDeclaredField(fieldName);    
			field.setAccessible(true);
			field.set(object, value);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static boolean ValidarKeyMapParametros(String llave,Map<String, Object> map){
		boolean retorno = false;
		if (map.containsKey(llave)){
			retorno = true;           
	    }else{
	    	retorno =  false;
	    }
		return retorno;
	}
	
	public static String obtenerIdDepartamentoPorUbigeo(String idUbigeo){
		return idUbigeo.substring(0, 2);
	}

	public static String obtenerIdProvinciaPorUbigeo(String idUbigeo){
	 	return idUbigeo.substring(2, 4);
	}
	
	public static String obtenerIdDistritoPorUbigeo(String idUbigeo){
	 	return idUbigeo.substring(4, 6);
	}
	public static String formatearAUTF8(String texto){
        String cadena=null;
        try {
            cadena = new String(texto.getBytes("ISO-8859-1"),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return cadena;
    }
	public static String isNullOrBlank(Object objeto){
		String respuesta;
		if(objeto == null || String.valueOf(objeto).equals("null") || String.valueOf(objeto).trim().equals("")){
			respuesta = "";
		} else {
			respuesta = String.valueOf(objeto);
		}
		return respuesta;
	}
		
	public static String generarNombreArchivo(String nombre, String extension){
		
		Calendar now = Calendar.getInstance();

		SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyHHmmssSSS");
		
		String nombreArchivo = nombre + sdf.format(now.getTime()) + "." + extension;
		
		return nombreArchivo;
	}
	
	public static Date sumaroRestarTiempoaFecha(Date fecha, int campoaInteractuar, int valorTiempo){
		  //en campo va el atributo que se quiere modificar osea minutos o segundos
		  // si valorTiempo es > a 0 quiere decir que vas agregar tiempo a la fecha
		  //caso contrario si es < a 0 quiere decir que vas a restar tiempo a la fecha
	      if (valorTiempo==0){
	    	  return fecha;
	      }else{
		      Calendar calendar = Calendar.getInstance();
		      calendar.setTime(fecha); 
		      calendar.add(campoaInteractuar, valorTiempo);
		      return calendar.getTime(); 
	      } 
	}
	
	@SuppressWarnings("unused")
	public static boolean esEntero(String cadena) {
		if(!StringUtils.isEmpty(cadena)) {
			 for(int i = 0; i<cadena.length(); i++) {
				 if(!Character.isDigit(cadena.charAt(i))) {
					 return false;
				 }
				 return true;
			 }
			return false;
		} else {
			return false;
		}
	}
	

	
	public static boolean validarRangoLong(String dato, int min, long max){
		try {
			long datoEntero = Long.parseLong(dato);
			if(datoEntero<min || datoEntero>max)return false;
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean validarLongitudLong(String dato, int min, long max){
		try {
			long datoEntero = Long.parseLong(dato);
			String longitud = String.valueOf(datoEntero);
			if(longitud != "null") {
				if(longitud.length()<min || longitud.length()>max)return false;
			}else {
				return false;
			}
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	
	public static boolean validarRangoEntero(String dato, int min, int max){
		try {
			int datoEntero = Integer.parseInt(dato);
			if(datoEntero<min || datoEntero>max)return false;
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean validarRangoFloat(String dato, int parteEntera, int parteDecimal){
		try {
			 float datoFloat= Float.parseFloat(dato);
			 dato = String.valueOf(datoFloat);
			 String arr[] = dato.split("\\.");
			 int p1= arr[0].length();
			 int d1= arr[1].length();
		
			if(parteEntera<p1 || d1>parteDecimal)return false;
			
	    } catch (Exception e) {
				return false;
		}
		return true;
	}
	
	
	public static boolean validarFormatoHorario(String horario){
		try {		
			String arreglo[] = horario.split(":");
			String hora = arreglo[0];
			String minuto = arreglo[1];
			
			if(hora.length()>2 || minuto.length()>2 ) return false;
			if(Integer.parseInt(hora)>23 || Integer.parseInt(hora)<0)return false;
			if(Integer.parseInt(minuto)>59 || Integer.parseInt(minuto)<0)return false;
			
		} catch (Exception e) {
			return false;
		}
		return true;
	}
	
	public static boolean validarFecha(String fecha) {
        try {
            SimpleDateFormat formatoFecha = new SimpleDateFormat("dd/MM/yyyy");
            formatoFecha.setLenient(false);
            formatoFecha.parse(fecha);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }


	
	public static boolean validarFloat_Mayor0(String dato){
		try {
			Float datoFloat= Float.parseFloat(dato);
			if(datoFloat<=0)return false;
			
	    } catch (Exception e) {
				return false;
		}
		return true;
	}
	
	public static boolean validarEntero_Mayor0(String dato){
		try {
			Integer datoInteger =  Integer.parseInt(dato);
			if(datoInteger<=0)return false;
	    } catch (Exception e) {
				return false;
		}
		return true;
	}
	
	
	
	public static boolean validarFloat(String dato){
		try {
			 Float.parseFloat(dato);
	    } catch (Exception e) {
				return false;
		}
		return true;
	}
	
	public static boolean isMayorHora(String horaInicio,String horaFin){
		
		String arr1[]= horaFin.split(":");
		String arr2[]= horaFin.split(":");
		
		int hora1= Integer.parseInt(arr1[0]);
		int min1= Integer.parseInt(arr1[1]);
		int hora2= Integer.parseInt(arr2[0]);
		int min2= Integer.parseInt(arr1[1]);
		
		if(hora1 <= hora2){
			if(hora1 ==  hora2 &&  min1>= min2){
				return false;
			}
			return true;
		}
		else{
			return false;
		}
		
	}
	
	public static boolean isDate(String date, String formato) {
        try {
            DateFormat df = new SimpleDateFormat(formato);
            df.setLenient(false);
            df.parse(date);
            return true;
        } catch (ParseException e) {
            return false;
        }
	}	
	public static int diferenciaMinutosFechas(Date fechaInicial, Date fechaFinal)
	{
	
		long fechainicialms = fechaInicial.getTime();
		long fechafinalms = fechaFinal.getTime();
		long diferencia1 = fechainicialms - fechafinalms;
		double minutosDiferencia = Math.floor(diferencia1 /60000);// para Minutos
		int minutos = (int) minutosDiferencia;
		return minutos;
	}
	
	public static List<Map<String, String>> ajustarFechas(List<Map<String, String>> ltaDetalleMap) {
		try {
			for (Map<String, String> map : ltaDetalleMap) {
				for (Map.Entry<String, String> entry : map.entrySet()) {
				    if(entry.getKey().contains("D_FEC_HOR")){				    	
				    	System.out.println("clave=" + entry.getKey() + ", valor=" + convertTimestampToString(Timestamp.valueOf(entry.getValue().toString())));						
				    }else if(entry.getKey().contains("D_FEC_")){
				    	System.out.println("clave=" + entry.getKey() + ", valor=" + entry.getValue());
				    }
					
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}		
		return ltaDetalleMap;
	}
	
	public static String convertTimestampToStringFormato(Timestamp timestamp,String Formato) throws ParseException{
		String dateString = "";
		try { 
		    Date date = new Date();
		    date.setTime(timestamp.getTime());
		    
		    dateString = new SimpleDateFormat(Formato).format(date);
		}catch (Exception e) {
			e.printStackTrace();
			return "";
		}
		return dateString;
	}
	
	public static Object cloneObject(Object obj){
        try{
            Object clone = obj.getClass().newInstance();
            for (Field field : obj.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if(field.get(obj) == null || Modifier.isFinal(field.getModifiers())){
                    continue;
                }
                if(field.getType().isPrimitive() || field.getType().equals(String.class)
                        || field.getType().getSuperclass().equals(Number.class)
                        || field.getType().equals(Boolean.class)){
                    field.set(clone, field.get(obj));
                }else{
                    Object childObj = field.get(obj);
                    if(childObj == obj){
                        field.set(clone, clone);
                    }else{
                        field.set(clone, cloneObject(field.get(obj)));
                    }
                }
            }
            return clone;
        }catch(Exception e){
            return null;
        }
    }
	
	/*adecuacion de casteo doubles*/
	public static String roundDecimal(Double number, Integer decimal) {
		String init = "#.";
		for(int i=0; i<decimal; i++) {
			init = init.concat("0");
		}
		DecimalFormat df = new DecimalFormat(init);
		df.setGroupingSize(3);
		df.setGroupingUsed(true);
		return df.format(number);
	}
	
	public static double roundDouble(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	/*add cod verify*/
	/*generate aleatory code
	 * 4 digitos
	 */
	public static Integer generateCodVerify() throws Exception {
		Random rand = new Random();
		Integer cod = 0;
		try {
			cod = rand.nextInt((Constantes.RANDOM_MAX - Constantes.RANDOM_LESS) + 1) + Constantes.RANDOM_COD;
			if(cod < (Constantes.RANDOM_LESS * Constantes.RANDOM_COD)) {
				cod = cod + (Constantes.RANDOM_LESS * Constantes.RANDOM_COD);
			}
			System.out.println(cod);
		} catch (Exception excepcion) {
			throw new Exception("Error generando codigo aleatorio");
		}
		return cod;
	}
	
}
