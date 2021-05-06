package pe.com.ocv.model;

public class PreguntaSecreta {

	private int idPreguntaSecreta;
	private String preguntaSecreta;

	public PreguntaSecreta() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PreguntaSecreta(int idPreguntaSecreta, String preguntaSecreta) {
		super();
		this.idPreguntaSecreta = idPreguntaSecreta;
		this.preguntaSecreta = preguntaSecreta;
	}

	public int getIdPreguntaSecreta() {
		return idPreguntaSecreta;
	}

	public void setIdPreguntaSecreta(int idPreguntaSecreta) {
		this.idPreguntaSecreta = idPreguntaSecreta;
	}

	public String getPreguntaSecreta() {
		return preguntaSecreta;
	}

	public void setPreguntaSecreta(String preguntaSecreta) {
		this.preguntaSecreta = preguntaSecreta;
	}

}
