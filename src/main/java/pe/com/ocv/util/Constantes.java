package pe.com.ocv.util;

public final class Constantes { 
	
	public static final int ESTADO_REGISTRADO = 5;
	public static final int ESTADO_ACTIVO = 6;
	public static final int ESTADO_BLOQUEADO = 7;
	public static final int ESTADO_INACTIVO = 8;
	/*Constantes Respuestas*/
	public static final int ESTADO_SUCCESS = 1;
	public static final int ESTADO_FAILURE = 0;
	/*Constantes Base Datos*/
	public static final String ORACLE_PROCEDURES_SCHEMA = "oracle.schema.procedures";
	public static final String P_CURSOR = "SYS_REFCURSOR";
	public static final String PAR_OUT_CURSOR = "PAR_OUT_CURSOR";
	public static final String P_SEPARADOR = ".";
	public static final String BD_SCHEMA = "SGCPROD";
	public static final String PKG_AUTENTICACION = "PCK_OCV_AUTENTICACION";
	public static final String PKG_INCIDENCIA = "PCK_OCV_INCIDENCIA";
	public static final String PKG_LUGAR_PAGO = "PCK_OCV_LUGAR_PAGO";
	public static final String PKG_MAESTRA = "PCK_OCV_MAESTRA";
	public static final String PKG_RECIBO_PAGO = "PCK_OCV_RECIBO_PAGO";
	public static final String PKG_REGISTRO = "PCK_OCV_REGISTRO";
	public static final String PKG_SUMINISTRO = "PCK_OCV_SUMINISTRO";
	public static final String PKG_UTIL = "PCK_OCV_UTIL";
	public static final String PKG_PAGO_EJECUTADO = "PCK_OCV_PAGO_EJECUTADO";
	public static final String PKG_NOTIFICACIONES = "PCK_OCV_NOTIFICACIONES";
	public static final String METHOD_POST = "POST";
	public static final String METHOD_PUT = "PUT";
	public static final String CONTENT_JSON = "application/json";
	public static final String CONTENT_FORM_URLENCODED = "application/x-www-form-urlencoded";
	public static final String USER_TOKEN = "OCV_Sedapal";
	public static final String PASSWORD_TOKEN = "OCV0109";
	public static final String ROLE_TOKEN = "OCV_ADMIN";
	public static final String MENSAJE_ERROR_ENCHUFATE = "El servicio de pago en línea de SEDAPAL está fuera de servicio. \n\n Vuelva a intentarlo más tarde.";
	public static final String MENSAJE_ERROR_VISA = "El servicio de pago en línea con tarjeta de crédito está fuera de servicio. \n\n Vuelva a intentarlo más tarde.";
	public static final String MENSAJE_VERSION_VALIDA = "La versión movil utilizada es válida.";
	public static final String MENSAJE_SUCCESS_ENCHUFATE = "Ejecución correcta del pago por servicio Enchufate";
	public static final String MENSAJE_SUCCESS_VISA = "Ejecución correcta del pago por VISA";
	public static final String MENSAJE_GET_PAY_DATA_FAILURE = "Error al obtener los datos del pago ejecutado.";
	public static final String MENSAJE_ERROR_GENERATE_LIQUI = "Tenemos problemas para procesar el pago de los recibos seleccionados. Comuníquese al correo %MAIL% o al teléfono %PHONE%";
	/**constantes moviles**/
	public static final String RESPONSE_INSERT_MOVIL = "ejecutado";
	public static final String REQUEST_CODE_MOVIL_SUCCESS = "0";
	public static final String REQUEST_CODE_MOVIL_FAILURE = "1";
	public static final String PAYMENT_SUCCESS = "000";
	public static final String FLAG_SEND_MAIL = "0";
	/**/
	
	public static final int AUDITORIA_DEFAULT = 0;
	
	//APP WEB
	public static final int FLAG_CHANNEL_WEB = 1;
	
	//APP MOVIL ANDROID
	public static final int FLAG_CHANNEL_APK = 2;
	
	//APP MOVIL IOS
	public static final int FLAG_CHANNEL_IOS = 3;
	
//	TIPO AUDITORIA LOG
	public static final int AUDITORIA_LOG_SUCCESS = 1;
	public static final int AUDITORIA_LOG_FAILURE = 2;
	public static final int AUDITORIA_CONSULTA = 3;
	public static final int AUDITORIA_PAGO_SUCCESS = 4;
	public static final int AUDITORIA_REGISTRO = 5;
	public static final int AUDITORIA_PAGO_FAILURE = 6;
	
	public static final int AUDITORIA_SUMINISTRO_MULTIPLE = 1;
	
//	IDENTIFICADORES NOTIFICACION
	public static final Integer ID_NOTIFICACION_REGISTRO = 1;
	public static final Integer ID_NOTIFICACION_PAGO = 2;
	
	/*FORMATOS DE FECHA*/
	public static final String FORMAT_DATE_ENCHUFATE = "yyyy-MM-dd";
	
	/*cvalenciap-16/04/2020: inicio verificacion version*/
	/*CONSTANTES VERIFICACION VERSION*/
	public static final String CAMP_CATEGORIA = "categoria";
	public static final String CATEGORIA_VERIF_VERSION = "DATOS_VERSION";
	public static final String CODIGO_ERROR_VERSION = "err001";
	/*cvalenciap-16/04/2020: fin verificacion version*/
	/*CONSTANTES SOPORTE*/
	public static final String CATEGORIA_SOPORTE = "DATOS_SOPORTE";
	/*CONSTANTES URL WEB*/
	public static final String CATEGORIA_URL_WEB = "URL_WEB";
	
	/*random generate*/
	public static final int RANDOM_MAX = 9999;
	public static final int RANDOM_LESS = 100;
	public static final int RANDOM_COD = 10;
	
	/*adecuacion pago*/
	public static final String FLAG_MODO_ENCHUFATE = "0";
	public static final String FLAG_MODO_JOB = "1";
	
	public static final String ST_REGIST_OCV = "PO000";
	public static final String ST_REGIST_OCV_ENCHUFATE = "PO005";
	public static final String ST_PAGADO_OCV = "PO001";
	public static final String ST_PAGO_ERROR_OCV = "PO002";
	
	/*support parameters*/
	public static final String REPLACE_MAIL_CONSTANT = "%MAIL%";
	public static final String REPLACE_PHONE_CONSTANT = "%PHONE%";
	
	/*liquidacion default*/
	public static final String NUM_LIQ_DEFAULT = "0";
	
	/*valor BRAND*/
	public static final String BRAND_VISA = "visa";
	public static final String BRAND_MASTERCARD = "mastercard";
	
	public static final String TIPO_REGISTRO_CLIENTE = "REGISTRADO";
	
}