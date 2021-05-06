package pe.com.ocv.util;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

//TODO: Auto-generated Javadoc
//created for cvalenciap : execute for jdbc
/**
* The Class ExecuteProcedure.
*/
public class ExecuteProcedure extends StoredProcedure {
	
	/**
	 * Constructor de la clase.
	 *
	 * @param ds Es el datasource de la base de datos
	 * @param storeProcedureName Es el nombre del procedimiento almacenado
	 * @param paramsInput Son los parámetros de entrada del procedimiento almacenado
	 * @param paramsOutput the params output
	 * @param isFunction Flag que indica si es procedimiento o función
	 */
	public ExecuteProcedure(DataSource ds, String storeProcedureName, List<SqlParameter> paramsInput,
			List<SqlOutParameter> paramsOutput, boolean isFunction) {

		setDataSource(ds);
		for (Iterator<SqlOutParameter> iterator = paramsOutput.iterator(); iterator.hasNext();) {
			SqlOutParameter sqlOutParameter = iterator.next();
			declareParameter(sqlOutParameter);
		}
		setFunction(isFunction);
		setSql(storeProcedureName);
		for (Iterator<SqlParameter> iterator = paramsInput.iterator(); iterator.hasNext();) {
			SqlParameter sqlParameter = iterator.next();
			declareParameter(sqlParameter);
		}

		compile();
	}

	/**
	 * Constructor de la clase.
	 *
	 * @param ds Es el datasource de la base de datos
	 * @param storeProcedureName Es el nombre del procedimiento almacenado
	 * @param schema the schema
	 * @param paramsInput Son los parámetros de entrada del procedimiento almacenado
	 * @param paramsOutput the params output
	 */
	public ExecuteProcedure(DataSource ds, String storeProcedureName, String schema, List<SqlParameter> paramsInput,
			List<SqlOutParameter> paramsOutput) {

		setDataSource(ds);
		setFunction(false);
		setSql(Util.concatenar(schema, Constantes.P_SEPARADOR,storeProcedureName));

		for (Iterator<SqlParameter> iterator = paramsInput.iterator(); iterator.hasNext();) {
			SqlParameter sqlParameter = iterator.next();
			declareParameter(sqlParameter);
		}
		for (Iterator<SqlOutParameter> iterator = paramsOutput.iterator(); iterator.hasNext();) {
			SqlOutParameter sqlOutParameter = iterator.next();
			declareParameter(sqlOutParameter);
		}

		compile();
	}

	/**
	 * Método que permite ejecutar el procedimiento almacenado.
	 *
	 * @param inputs Contiene los parámetros de entrada (los valores)
	 * @return the map
	 * @Return Objeto de tipo Map que contiene el resultado
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map executeSp(Map inputs) {
		return execute(inputs);
	}

	/**
	 * Método que permite retornar lista de resultados.
	 *
	 * @param map Contiene los parámetros de entrada (los valores)
	 * @return the list
	 * @Return Objeto de tipo List que contiene los resultados
	 */
	@SuppressWarnings("rawtypes")
	public static List retornaLista(Map map) {

		Iterator it = map.entrySet().iterator();
		List list = null;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();

			String key = (String) entry.getKey();
			if (Constantes.P_CURSOR.equals(key)) {
				list = (List) entry.getValue();
				break;
			}
		}
		return list;
	}

	/**
	 * Método que permite retornar el valor de un campo.
	 *
	 * @param map Contiene los parámetros de entrada (los valores)
	 * @return the object
	 * @Return Objeto genérico que contiene el resultado
	 */
	@SuppressWarnings("rawtypes")
	public static Object retornaValue(Map map) {

		Iterator it = map.entrySet().iterator();
		Object value = null;
		while (it.hasNext()) {

			Map.Entry entry = (Map.Entry) it.next();

//			String key = (String) entry.getKey();

			value = (Object) entry.getValue();

			break;

		}
		return value;
	}
	
	/*public static Object retornaValor(Map<K, V> map, String key) {
		Iterator it = map.entrySet().iterator();
		Object value = null;
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String keyAux = (String) entry.getKey();
			if (keyAux.equals(key)) {
				value = (Object) entry.getValue();
				break;
			}
		}
		return value;
	}*/
}