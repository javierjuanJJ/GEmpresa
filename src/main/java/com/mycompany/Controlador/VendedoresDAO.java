package com.mycompany.Controlador;

import com.mycompany.Modelo.Vendedores;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;

public class VendedoresDAO implements GenericoDAO<Vendedores> {

	protected static final String sql_select_all = "SELECT * FROM "
                + Conexion.nombre_base_datos
                + ".vendedores;";
	
	public static PreparedStatement preparedstatement = null;
	private static Connection conexion;
	public VendedoresDAO() {
		try {
			conexion = Conexion.getConnection();
			
		} catch (Exception e) {
			
			Platform.exit();
		}
	}
	
	public List<Vendedores> findAll() throws Exception {
		List<Vendedores> Vendedores =  new ArrayList();
		ResultSet resultset = null;
		preparedstatement = conexion.prepareStatement(sql_select_all);
		resultset = preparedstatement.executeQuery();
		
		while (resultset.next()) {
			Vendedores vendedor =  new Vendedores();
			vendedor.setId(resultset.getInt(1));
			vendedor.setNombre(resultset.getString(2));
			vendedor.setFecha_ingreso(resultset.getDate(3));
			vendedor.setSalario(resultset.getDouble(4));
			Vendedores.add(vendedor);
		}
		
		return Vendedores;
	}


	public List<Vendedores> findBySQL(String sqlselect) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
	public boolean insert(Vendedores t) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}
	public boolean update(Vendedores t) throws Exception {
		
		return false;
	}

	public boolean delete(int id) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public Vendedores findByPK(int id) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
