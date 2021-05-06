package pe.com.ocv.model;

import java.io.Serializable;
import java.util.Date;

public class PagoEjecutado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String trxId;
	private String errorCode;
	private String errorMsg;
	private String fecha;
	private String hora;
	private String descripcion;
	private String numOperacion;
	private String numTarjeta;
	private long numLiquidacion;
	private int nisRad;
	private String estado;
	private double monto;
	private String correo;
	/**/
	private Date fechaEmision;
	private Date fechaVencimiento;
	private long simboloVar;
	private String fechaHora;
	private String nombreCompleto;
	private Integer canal;
	private Integer codAgencia;
	/**/
	
	public PagoEjecutado() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PagoEjecutado(String trxId, String errorCode, String errorMsg, String fecha, String hora, String descripcion,
			String numOperacion, String numTarjeta, long numLiquidacion, int nisRad, String estado, double monto,
			String correo, Date fechaEmision, Date fechaVencimiento, long simboloVar) {
		super();
		this.trxId = trxId;
		this.errorCode = errorCode;
		this.errorMsg = errorMsg;
		this.fecha = fecha;
		this.hora = hora;
		this.descripcion = descripcion;
		this.numOperacion = numOperacion;
		this.numTarjeta = numTarjeta;
		this.numLiquidacion = numLiquidacion;
		this.nisRad = nisRad;
		this.estado = estado;
		this.monto = monto;
		this.correo = correo;
		this.fechaEmision = fechaEmision;
		this.fechaVencimiento = fechaVencimiento;
		this.simboloVar = simboloVar;
	}

	public String getTrxId() {
		return trxId;
	}

	public void setTrxId(String trxId) {
		this.trxId = trxId;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getNumOperacion() {
		return numOperacion;
	}

	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}

	public String getNumTarjeta() {
		return numTarjeta;
	}

	public void setNumTarjeta(String numTarjeta) {
		this.numTarjeta = numTarjeta;
	}

	public long getNumLiquidacion() {
		return numLiquidacion;
	}

	public void setNumLiquidacion(long numLiquidacion) {
		this.numLiquidacion = numLiquidacion;
	}

	public int getNisRad() {
		return nisRad;
	}

	public void setNisRad(int nisRad) {
		this.nisRad = nisRad;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public double getMonto() {
		return monto;
	}

	public void setMonto(double monto) {
		this.monto = monto;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}
	/**/
	
	public long getSimboloVar() {
		return simboloVar;
	}
	public void setSimboloVar(long simboloVar) {
		this.simboloVar = simboloVar;
	}
	public Date getFechaEmision() {
		return fechaEmision;
	}

	public void setFechaEmision(Date fechaEmision) {
		this.fechaEmision = fechaEmision;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}
	/**/

	public String getFechaHora() {
		return fechaHora;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Integer getCanal() {
		return canal;
	}

	public void setCanal(Integer canal) {
		this.canal = canal;
	}

	public Integer getCodAgencia() {
		return codAgencia;
	}

	public void setCodAgencia(Integer codAgencia) {
		this.codAgencia = codAgencia;
	}
}
