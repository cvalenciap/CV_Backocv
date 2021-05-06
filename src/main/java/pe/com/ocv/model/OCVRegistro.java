package pe.com.ocv.model;

public class OCVRegistro {
	private int nis_rad;
	private String ref_cobro;
	private int tipo_docu;
	private String nro_doc;
	private String apellido1;
	private String apellido2;
	private String nombres;
	private String correo;
	private String telefono1;
	private String telefono2;
	private String clave;
	private int acepta_terminos;
	private int acepta_noti;
	private int acepta_correo;
	private int tipo_soli;
	private String sexo;
	private String distrito;
	private String direccion;
	private String fecha_nac;
	private int id_cliente;
	/*add cod verify*/
	private Integer codVerify;

	public OCVRegistro() {
		super();
		// TODO Auto-generated constructor stub
	}

	public OCVRegistro(int nis_rad, String ref_cobro, int tipo_docu, String nro_doc, String apellido1, String apellido2,
			String nombres, String correo, String telefono1, String telefono2, String clave, int acepta_terminos,
			int acepta_noti, int acepta_correo, int tipo_soli, String sexo, String distrito, String direccion,
			String fecha_nac, int id_cliente, Integer codVerify) {
		super();
		this.nis_rad = nis_rad;
		this.ref_cobro = ref_cobro;
		this.tipo_docu = tipo_docu;
		this.nro_doc = nro_doc;
		this.apellido1 = apellido1;
		this.apellido2 = apellido2;
		this.nombres = nombres;
		this.correo = correo;
		this.telefono1 = telefono1;
		this.telefono2 = telefono2;
		this.clave = clave;
		this.acepta_terminos = acepta_terminos;
		this.acepta_noti = acepta_noti;
		this.acepta_correo = acepta_correo;
		this.tipo_soli = tipo_soli;
		this.sexo = sexo;
		this.distrito = distrito;
		this.direccion = direccion;
		this.fecha_nac = fecha_nac;
		this.id_cliente = id_cliente;
		this.codVerify = codVerify;
	}

	public int getNis_rad() {
		return nis_rad;
	}

	public void setNis_rad(int nis_rad) {
		this.nis_rad = nis_rad;
	}

	public String getRef_cobro() {
		return ref_cobro;
	}

	public void setRef_cobro(String ref_cobro) {
		this.ref_cobro = ref_cobro;
	}

	public int getTipo_docu() {
		return tipo_docu;
	}

	public void setTipo_docu(int tipo_docu) {
		this.tipo_docu = tipo_docu;
	}

	public String getNro_doc() {
		return nro_doc;
	}

	public void setNro_doc(String nro_doc) {
		this.nro_doc = nro_doc;
	}

	public String getApellido1() {
		return apellido1;
	}

	public void setApellido1(String apellido1) {
		this.apellido1 = apellido1;
	}

	public String getApellido2() {
		return apellido2;
	}

	public void setApellido2(String apellido2) {
		this.apellido2 = apellido2;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getCorreo() {
		return correo;
	}

	public void setCorreo(String correo) {
		this.correo = correo;
	}

	public String getTelefono1() {
		return telefono1;
	}

	public void setTelefono1(String telefono1) {
		this.telefono1 = telefono1;
	}

	public String getTelefono2() {
		return telefono2;
	}

	public void setTelefono2(String telefono2) {
		this.telefono2 = telefono2;
	}

	public String getClave() {
		return clave;
	}

	public void setClave(String clave) {
		this.clave = clave;
	}

	public int getAcepta_terminos() {
		return acepta_terminos;
	}

	public void setAcepta_terminos(int acepta_terminos) {
		this.acepta_terminos = acepta_terminos;
	}

	public int getAcepta_noti() {
		return acepta_noti;
	}

	public void setAcepta_noti(int acepta_noti) {
		this.acepta_noti = acepta_noti;
	}

	public int getAcepta_correo() {
		return acepta_correo;
	}

	public void setAcepta_correo(int acepta_correo) {
		this.acepta_correo = acepta_correo;
	}

	public int getTipo_soli() {
		return tipo_soli;
	}

	public void setTipo_soli(int tipo_soli) {
		this.tipo_soli = tipo_soli;
	}

	public String getSexo() {
		return sexo;
	}

	public void setSexo(String sexo) {
		this.sexo = sexo;
	}

	public String getDistrito() {
		return distrito;
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public String getFecha_nac() {
		return fecha_nac;
	}

	public void setFecha_nac(String fecha_nac) {
		this.fecha_nac = fecha_nac;
	}

	public int getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(int id_cliente) {
		this.id_cliente = id_cliente;
	}

	public Integer getCodVerify() {
		return codVerify;
	}

	public void setCodVerify(Integer codVerify) {
		this.codVerify = codVerify;
	}

}
