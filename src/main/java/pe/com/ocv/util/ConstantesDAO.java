package pe.com.ocv.util;

public final class ConstantesDAO {
	
//	GENERAL FOR BD
	public static final String ORACLE_PROCEDURES_SCHEMA = "oracle.schema.procedures";
	public static final String P_CURSOR = "SYS_REFCURSOR";
	public static final String PAR_OUT_CURSOR = "PAR_OUT_CURSOR";
	public static final String P_SEPARADOR = ".";
	public static final String BD_SCHEMA = "SGCPROD";
	public static final String COD_RESPUESTA = "nRESP_SP";
	public static final String MENSAJE_RESPUESTA = "cRESP_SP";
	
//	PACKAGES STORED PROCEDURE
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
	public static final String PKG_AUDITORIA = "PCK_OCV_AUDITORIA";
	public static final String PRC_GET_N_NUM_LIQUIDACION = "PRC_GET_N_NUM_LIQUIDACION";
	
//	PROCEDURES NAME
	public static final String PRC_ENVIAR_CORREO_NOTIFICACION = "PRC_ENVIAR_CORREO_NOTIFICACION";
	public static final String PRC_ENVIAR_CORREO_GENER = "PRC_ENVIAR_CORREO_GENER";
	public static final String PRC_INSERTAR_REGISTRO_PREV = "PRC_INSERTAR_REGISTRO_PREV";
	public static final String PRC_LISTA_REGISTRO_PREV = "PRC_LISTA_REGISTRO_PREV";
	public static final String PRC_OBTENER_CANAL_AUDITORIA = "PRC_OBTENER_CANAL_AUDITORIA";
	public static final String PRC_INSERTAR_LOG = "PRC_INSERTAR_LOG";
	/*cvalenciap-16/04/2020: inicio verificacion version*/
	public static final String PRC_COMPARE_VERSION_APK = "PRC_COMPARE_VERSION_APK";
	/*init intermediate screen*/
	public static final String PRC_LISTAR_DATOS_PAGO = "PRC_LISTAR_DATOS_PAGO";
	public static final String PRC_GET_CLIENTE = "PRC_GET_CLIENTE";
	/*add code verify*/
	public static final String PRC_UPDATE_CODE_VERIFY = "PRC_UPDATE_CODE_VERIFY";
	/*adecuacion pago ejecutado*/
	public static final String PRC_ACTUALIZAR_REGISTRO_PREV = "PRC_ACTUALIZAR_REGISTRO_PREV";
	public static final String PRC_INSERTAR_PAGO_EJECUTADO_V2 = "PRC_INSERTAR_PAGO_EJECUTADO_V2";
	public static final String PRC_OBTENER_DATOS_TARJETA = "PRC_OBTENER_DATOS_TARJETA";
	/*validacion vesion*/
	public static final String PRC_VALIDA_IOS = "PRC_VALIDA_IOS";
	/*gjaramillo-14/08/2020: inicio verificacion version*/
	public static final String PRC_VALIDA_APK = "PRC_VALIDA_APK";
	public static final String PRC_ENVIO_CORREO = "PRC_ENVIO_CORREO";
	public static final String PRC_OBTENER_PARAM_CATEGORIA = "PRC_OBTENER_PARAM_CATEGORIA";
	public static final String PRC_INSERTAR_LIQUI_VISA = "PRC_INSERTAR_LIQUI_VISA";
	
//	PROCEDURES PARAMETERS
	public static final String PAR_N_ID_NOTIFICACION = "PAR_N_ID_NOTIFICACION";
	public static final String PAR_V_CORREO = "PAR_V_CORREO";
	public static final String PAR_V_NUM_OPERACION = "PAR_V_NUM_OPERACION";
	public static final String PAR_V_NUM_TARJETA = "PAR_V_NUM_TARJETA";
	public static final String PAR_N_NIS_RAD = "PAR_N_NIS_RAD";
	public static final String PAR_V_FECHA = "PAR_V_FECHA";
	public static final String PAR_V_HORA = "PAR_V_HORA";
	public static final String PAR_N_MONTO = "PAR_N_MONTO";
	public static final String PAR_N_NUM_LIQUIDACION = "PAR_N_NUM_LIQUIDACION";
	public static final String PAR_N_SIMBOLO_VAR = "PAR_N_SIMBOLO_VAR";
	public static final String PAR_D_FE_EMISION = "PAR_D_FE_EMISION";
	public static final String PAR_D_FE_VENCIMIENTO = "PAR_D_FE_VENCIMIENTO";
	public static final String PAR_N_ID_CANAL_AUDITORIA = "PAR_N_ID_CANAL_AUDITORIA";
	public static final String PAR_N_ID_CANAL = "PAR_N_ID_CANAL";
	public static final String PAR_N_ID_TIPO_AUDITORIA = "PAR_N_ID_TIPO_AUDITORIA";
	public static final String VAR1 = "VAR1";
	public static final String VAR2 = "VAR2";
	public static final String VAR3 = "VAR3";
	public static final String VAR4 = "VAR4";
	public static final String VAR5 = "VAR5";
	public static final String VAR6 = "VAR6";
	public static final String VAR7 = "VAR7";
	/*cvalenciap-16/04/2020: inicio verificacion version*/
	public static final String PAR_V_VALOR = "PAR_V_VALOR";
	/*init intermediate screen*/
	public static final String PAR_V_TRX_ID = "PAR_V_TRX_ID";
	public static final String PAR_V_ID_CLIENTE = "PAR_V_ID_CLIENTE";
	/*add cod verify*/
	public static final String PAR_N_COD_VERIFY = "PAR_N_COD_VERIFY";
	/*adecuacion proceso pago*/
	public static final String PAR_V_ESTADO = "PAR_V_ESTADO";
	public static final String PAR_V_ERROR_CODE = "PAR_V_ERROR_CODE";
	public static final String PAR_V_ERROR_MSG = "PAR_V_ERROR_MSG";
	public static final String PAR_V_DESCRIPCION = "PAR_V_DESCRIPCION";
	public static final String PAR_V_NUM_LIQUIDACION = "PAR_V_NUM_LIQUIDACION";
	public static final String PAR_D_FECHA_EMISION = "PAR_D_FECHA_EMISION";
	public static final String PAR_D_FECHA_VENCIMIENTO = "PAR_D_FECHA_VENCIMIENTO";
	public static final String PAR_N_COD_AGENCIA = "PAR_N_COD_AGENCIA";
	public static final String PAR_N_ID_TARJETA = "PAR_N_ID_TARJETA";
	
	/*Parametros de salida del PRC_VALIDA_APK*/
//	public static final String CRESP_SP = "CRESP_SP";
//	public static final String NRESP_SP = "NRESP_SP";
	public static final String CCODIGOERROR = "CCODIGOERROR";
	public static final String NESOBLIGATORIO = "NESOBLIGATORIO";
	
	public static final String PAR_V_CATEGORIA = "vCATEGORIA";
	public static final String PAR_IO_CURSOR = "IO_CURSOR";
	
	/*Liquidación Visa*/
	public static final String PAR_V_NRO_LIQUI = "PAR_V_NRO_LIQUI";
	public static final String PAR_V_TRAMA_JSON = "PAR_V_TRAMA_JSON";
	
}
