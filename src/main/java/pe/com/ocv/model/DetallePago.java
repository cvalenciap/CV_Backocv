package pe.com.ocv.model;

public class DetallePago {

	private double monto_pago;
	private String fecha_pago;
	private String nom_agencia;
	private String nom_sucursal;
	private String forma_pago;
	private String hora_pago;
	private String tipo_doc;

	public DetallePago() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DetallePago(double monto_pago, String fecha_pago, String nom_agencia, String nom_sucursal, String forma_pago,
			String hora_pago, String tipo_doc) {
		super();
		this.monto_pago = monto_pago;
		this.fecha_pago = fecha_pago;
		this.nom_agencia = nom_agencia;
		this.nom_sucursal = nom_sucursal;
		this.forma_pago = forma_pago;
		this.hora_pago = hora_pago;
		this.tipo_doc = tipo_doc;
	}

	public double getMonto_pago() {
		return monto_pago;
	}

	public void setMonto_pago(double monto_pago) {
		this.monto_pago = monto_pago;
	}

	public String getFecha_pago() {
		return fecha_pago;
	}

	public void setFecha_pago(String fecha_pago) {
		this.fecha_pago = fecha_pago;
	}

	public String getNom_agencia() {
		return nom_agencia;
	}

	public void setNom_agencia(String nom_agencia) {
		this.nom_agencia = nom_agencia;
	}

	public String getNom_sucursal() {
		return nom_sucursal;
	}

	public void setNom_sucursal(String nom_sucursal) {
		this.nom_sucursal = nom_sucursal;
	}

	public String getForma_pago() {
		return forma_pago;
	}

	public void setForma_pago(String forma_pago) {
		this.forma_pago = forma_pago;
	}

	public String getHora_pago() {
		return hora_pago;
	}

	public void setHora_pago(String hora_pago) {
		this.hora_pago = hora_pago;
	}

	public String getTipo_doc() {
		return tipo_doc;
	}

	public void setTipo_doc(String tipo_doc) {
		this.tipo_doc = tipo_doc;
	}

}
