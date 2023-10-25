package parquimetros.modelo.inspector.dao;

import java.sql.Connection;
import java.sql.ResultSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mysql.cj.protocol.Resultset;

import parquimetros.modelo.beans.InspectorBean;
import parquimetros.modelo.beans.InspectorBeanImpl;
import parquimetros.modelo.inspector.exception.InspectorNoAutenticadoException;
import parquimetros.utils.Mensajes;

public class DAOInspectorImpl implements DAOInspector {

	private static Logger logger = LoggerFactory.getLogger(DAOInspectorImpl.class);
	
	private Connection conexion;
	
	public DAOInspectorImpl(Connection c) {
		this.conexion = c;
	}

	@Override
	public InspectorBean autenticar(String legajo, String password) throws InspectorNoAutenticadoException, Exception {
		/** 
		 * HECHO Código que autentica que exista en la B.D. un legajo de inspector y que el password corresponda a ese legajo
		 *      (recuerde que el password guardado en la BD está encriptado con MD5) 
		 *      En caso exitoso deberá retornar el inspectorBean.
		 *      Si la autenticación no es exitosa porque el legajo no es válido o el password es incorrecto
		 *      deberá generar una excepción InspectorNoAutenticadoException 
		 *      y si hubo algún otro error deberá producir y propagar una Exception.
		 *      
		 *      Importante: Para acceder a la B.D. utilice la propiedad this.conexion (de clase Connection) 
		 *      que se inicializa en el constructor.      
		 */
		int leg = Integer.parseInt(legajo);
		InspectorBean inspector = new InspectorBeanImpl();
        java.sql.Statement stmt = conexion.createStatement();
 	
		String sql = "SELECT legajo, dni, nombre, apellido, password" + "FROM Inspectores"
					+ "WHERE legajo =" + leg + "AND password = md5('" + password +"');"  ;
			
		ResultSet res = stmt.executeQuery(sql);

		if(res.next()) {
			inspector.setLegajo(res.getInt("legajo"));
			inspector.setDNI(res.getInt("dni"));
			inspector.setNombre(res.getString("nombre"));
			inspector.setApellido(res.getString("apellido"));
			inspector.setPassword(res.getString("password"));
		}else {
			throw new InspectorNoAutenticadoException("La autentificacion no es exitosa");
		}
		
		return inspector;
	}	


}
