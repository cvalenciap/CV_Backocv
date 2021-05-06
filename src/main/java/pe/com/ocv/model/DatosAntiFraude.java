package pe.com.ocv.model;


public class DatosAntiFraude {

	private int tipo_docu;
	private String des_documento;
	private String nro_doc;
	private String correo;
	private int cli_frecuente;
	private int cant_dias;
	private String tipo_registro_cliente;
	
	public int getTipo_docu() {
		return tipo_docu;
	}
	public void setTipo_docu(int tipo_docu) {
		this.tipo_docu = tipo_docu;
	}
	public String getDes_documento() {
		return des_documento;
	}
	public void setDes_documento(String des_documento) {
		this.des_documento = des_documento;
	}
	public String getNro_doc() {
		return nro_doc;
	}
	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public int getCli_frecuente() {
		return cli_frecuente;
	}
	public void setCli_frecuente(int cli_frecuente) {
		this.cli_frecuente = cli_frecuente;
	}
	public int getCant_dias() {
		return cant_dias;
	}
	public void setCant_dias(int cant_dias) {
		this.cant_dias = cant_dias;
	}
	public String getTipo_registro_cliente() {
		return tipo_registro_cliente;
	}
	public void setTipo_registro_cliente(String tipo_registro_cliente) {
		this.tipo_registro_cliente = tipo_registro_cliente;
	}
	
	
}
