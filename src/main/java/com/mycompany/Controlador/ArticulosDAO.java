package com.mycompany.Controlador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import Modelo.Articulos;
import Modelo.Grupos;
import javafx.application.Platform;

public class ArticulosDAO implements GenericoDAO<Articulos> {

	private static final String sql_select_by_PK = "SELECT * FROM v_empresa_ad_p1.articulos WHERE id=?;";
	private static final String sql_select_by_PK_grupos = "SELECT * FROM v_empresa_ad_p1.grupos WHERE id=?;";
	private static final String sql_select_all = "SELECT * FROM v_empresa_ad_p1.articulos;";
	private static final String sql_select_all_grupos = "SELECT * FROM v_empresa_ad_p1.grupos;";
	private static final String sql_UPDATE = "UPDATE `v_empresa_ad_p1`.`articulos` SET `nombre`=?, `precio`=?, `codigo`=?, `grupo`=?, `stock`=? WHERE `id`=?;";
	private static final String sql_INSERT = "INSERT INTO `v_empresa_ad_p1`.`articulos` (`nombre`, `precio`, `codigo`, `grupo`, `stock`) VALUES (?, ?, ?, ?, ?);";
	private static final String sql_DELETE = "DELETE FROM `v_empresa_ad_p1`.`articulos` WHERE `id`=?;";
	private static final String sql_INSERT_GRUPO = "INSERT INTO `v_empresa_ad_p1`.`grupos` (`descripcion`) VALUES (?);";
	public static PreparedStatement preparedstatement = null;
	private static Connection conexion;

	public ArticulosDAO() {
		try {
			conexion=Conexion.getConnection();

		} catch (Exception e) {

			Platform.exit();
		}
	}

	public Articulos findByPK(int id) throws Exception {
		Articulos articulo_recibido = null;

		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_by_PK);
		preparedstatement.setInt(1, id);
		resultset = preparedstatement.executeQuery();
		resultset.first();

		articulo_recibido = new Articulos(resultset.getInt("id"), resultset.getString("nombre"),
				resultset.getDouble("precio"), resultset.getInt("grupo"), resultset.getString("codigo"),
				resultset.getInt("stock"));

		return articulo_recibido;
	}

	public Grupos findByPK_grupos(int id) throws Exception {
		Grupos articulo_recibido = null;
		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_by_PK_grupos);
		preparedstatement.setInt(1, id);
		resultset = preparedstatement.executeQuery();
		resultset.first();
		articulo_recibido = new Grupos(resultset.getInt("id"), resultset.getString("descripcion"));

		return articulo_recibido;
	}

	public List<Articulos> findAll() throws Exception {
		List<Articulos> articulos_recibidos = null;
		articulos_recibidos = new ArrayList<Articulos>();
		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_all);
		resultset = preparedstatement.executeQuery();

		while (resultset.next()) {
			articulos_recibidos.add(
					new Articulos(resultset.getInt("id"), resultset.getString("nombre"), resultset.getDouble("precio"),
							resultset.getInt("grupo"), resultset.getString("codigo"), resultset.getInt("stock")));
		}

		return articulos_recibidos;
	}

	public List<Grupos> findAll_grupos() throws Exception {
		List<Grupos> grupos_recibidos = new ArrayList<Grupos>();

		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_all_grupos);
		resultset = preparedstatement.executeQuery();

		while (resultset.next()) {
			grupos_recibidos.add(new Grupos(resultset.getInt("id"), resultset.getString("descripcion")));
		}

		return grupos_recibidos;
	}

	public List<Articulos> findBySQL(String sqlselect) throws Exception {
		return null;
	}

	public boolean insert(Articulos t) throws Exception {
		int salida = 0;
		boolean resultado = false;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_INSERT);
		preparedstatement.setString(1, t.getNombre());
		preparedstatement.setDouble(2, t.getPrecio());
		preparedstatement.setString(3, t.getCodigo());
		preparedstatement.setInt(4, t.getGrupo());
		preparedstatement.setInt(5, t.getStock());
		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}

		return resultado;
	}

	public boolean update(Articulos t) throws Exception {
		int salida = 0;
		boolean resultado = false;

		preparedstatement = Conexion.getConnection().prepareStatement(sql_UPDATE);
		preparedstatement.setString(1, t.getNombre());
		preparedstatement.setDouble(2, t.getPrecio());
		preparedstatement.setString(3, t.getCodigo());
		preparedstatement.setInt(4, t.getGrupo());
		preparedstatement.setInt(5, t.getStock());
		preparedstatement.setInt(6, t.getId());

		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}

		return resultado;

	}

	public boolean delete(int id) throws Exception {
		int salida = 0;
		boolean resultado = false;

		preparedstatement = Conexion.getConnection().prepareStatement(sql_DELETE);
		preparedstatement.setInt(1, id);
		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}
		return resultado;

	}

	public boolean insert(Grupos t) throws Exception {

		int salida = 0;
		boolean resultado = false;

		preparedstatement = Conexion.getConnection().prepareStatement(sql_INSERT_GRUPO);
		preparedstatement.setString(1, t.getDescripcion());
		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}

		return resultado;
	}

	public String insert_batch(ArrayList<Articulos> t) throws Exception {
		Conexion.getConnection().setAutoCommit(false);
		preparedstatement = conexion.prepareStatement(sql_INSERT);
		int[] Salidas = new int[t.size()];
		String errores="";
		
		try {
			for (int contador = 0; contador < t.size(); contador++) {
				preparedstatement.setString(1, t.get(contador).getNombre());
				preparedstatement.setDouble(2, t.get(contador).getPrecio());
				preparedstatement.setString(3, t.get(contador).getCodigo());
				preparedstatement.setInt(4, t.get(contador).getGrupo());
				preparedstatement.setInt(5, t.get(contador).getStock());
				preparedstatement.addBatch();
			}

			Salidas=preparedstatement.executeBatch();
			conexion.commit();
			preparedstatement.close();
		} catch (SQLException e) {
			errores="Ha habido los errores : " + e.getMessage();
		}
		finally
		{
			try {
				conexion.commit();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
			}
		}

		StringBuilder respuesta=new StringBuilder();
		
		for (int contador=0;contador<Salidas.length;contador++) {
			
			if (Salidas[contador]<=0) {
				respuesta.append("El artículo " + t.get(contador) + "ha sido importado." + System.lineSeparator());
			}
			
		}
		
		respuesta.append(errores);
		errores=respuesta.toString();
		return errores;
	}

}
