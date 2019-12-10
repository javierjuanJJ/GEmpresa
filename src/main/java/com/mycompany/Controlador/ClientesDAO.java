package com.mycompany.Controlador;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import Modelo.Clientes;
import javafx.application.Platform;

public class ClientesDAO implements GenericoDAO<Clientes> {

	protected static final String sql_select_by_PK = "SELECT * FROM v_empresa_ad_p1.clientes WHERE id=?;";
	protected static final String sql_select_all = "SELECT * FROM v_empresa_ad_p1.clientes;";
	protected static final String sql_UPDATE = "UPDATE `v_empresa_ad_p1`.`clientes` SET `nombre`=?, `direccion`=?, `passwd`=? WHERE `id`=?;";
	
	protected static final String sql_INSERT = "INSERT INTO `v_empresa_ad_p1`.`clientes` (`nombre`, `direccion`, `passwd`) VALUES (?, ?, ?);";
	protected static final String sql_DELETE = "DELETE FROM `v_empresa_ad_p1`.`clientes` WHERE `id`=?;";
	public static PreparedStatement preparedstatement = null;
	
	public ClientesDAO() {
		try {
			Conexion.getConnection();
			
		} catch (Exception e) {
			
			Platform.exit();
		}
	}

	public Clientes findByPK(int id) throws Exception {

		Clientes cliente_recibido = null;
		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_by_PK);
		preparedstatement.setInt(1, id);
		resultset = preparedstatement.executeQuery();
		resultset.first();

		cliente_recibido = new Clientes(resultset.getInt("id"), resultset.getString("nombre"),
				resultset.getString("direccion"),resultset.getString("passwd"));

		return cliente_recibido;
	}

	public List<Clientes> findAll() throws Exception {
		List<Clientes> clientes_recibidos = null;
		clientes_recibidos = new ArrayList<Clientes>();
		ResultSet resultset = null;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_select_all);
		resultset = preparedstatement.executeQuery();

		while (resultset.next()) {
			clientes_recibidos.add(new Clientes(resultset.getInt("id"), resultset.getString("nombre"),
					resultset.getString("direccion"),resultset.getString("passwd")));
		}
		return clientes_recibidos;
	}

	public List<Clientes> findBySQL(String sqlselect) throws Exception {
		return null;
	}

	public boolean insert(Clientes t) throws Exception {

		int salida = 0;
		boolean resultado = false;

		preparedstatement = Conexion.getConnection().prepareStatement(sql_INSERT);
		preparedstatement.setString(1, t.getNombre());
		preparedstatement.setString(2, t.getDireccion());
		preparedstatement.setString(3, t.getpasswd());
		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}
		return resultado;
	}

	public boolean update(Clientes t) throws Exception {

		int salida = 0;
		boolean resultado = false;

		preparedstatement = Conexion.getConnection().prepareStatement(sql_UPDATE);
		preparedstatement.setString(1, t.getNombre());
		preparedstatement.setString(2, t.getDireccion());
		preparedstatement.setString(3, t.getpasswd());
		preparedstatement.setInt(4, t.getId());

		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}

		return resultado;

	}

	public boolean delete(int id) throws Exception {
		boolean resultado = false;

		int salida = 0;
		preparedstatement = Conexion.getConnection().prepareStatement(sql_DELETE);
		preparedstatement.setInt(1, id);
		salida = preparedstatement.executeUpdate();

		if (salida > 0) {
			resultado = true;
		}

		return resultado;

	}
	
	public String desencriptar_contrasenya(String texto) {
		String desencripcion="";
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		
		digest.reset();
		digest.update(texto.getBytes("utf8"));
		desencripcion = String.format("%040x", new BigInteger(1, digest.digest()));
		} catch (NoSuchAlgorithmException e) {
			// TODO Bloque catch generado autom�ticamente
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Bloque catch generado autom�ticamente
			e.printStackTrace();
		}
		return desencripcion;
		
	}

}
