package pe.com.ocv.model;

public class ParametroGC {

	private String categoria;
	private String clase;
	private boolean flag;
	private String f_alta;
	private String valor;
	private String modulo;

	public ParametroGC() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ParametroGC(String categoria, String clase, boolean flag, String f_alta, String valor, String modulo) {
		super();
		this.categoria = categoria;
		this.clase = clase;
		this.flag = flag;
		this.f_alta = f_alta;
		this.valor = valor;
		this.modulo = modulo;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public boolean isFlag() {
		return flag;
	}

	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	public String getF_alta() {
		return f_alta;
	}

	public void setF_alta(String f_alta) {
		this.f_alta = f_alta;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public String getModulo() {
		return modulo;
	}

	public void setModulo(String modulo) {
		this.modulo = modulo;
	}

}
