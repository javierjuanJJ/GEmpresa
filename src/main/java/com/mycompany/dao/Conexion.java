package com.mycompany.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.TimeZone;

public class Conexion {
    private static Connection conexion = null;
    public static final String nombre_base_datos = "v_empresa_ad_p1";
    static final String ip_base_datos = "127.0.0.1";
    static final String url = "jdbc:mariadb://"
            + ip_base_datos
            + ":3306/"
            + nombre_base_datos
            + "?serverTimezone=" + TimeZone.getDefault().getID();
    static final String kusuario = "batoi";
    static final String kcontrasenya = "1234";

    public static Connection getConnection() throws SQLException, Exception {

        if (conexion == null) {

            Properties pc = new Properties();
            pc.put("passwd", kcontrasenya);
            pc.put("user", kusuario);

            conexion = DriverManager.getConnection(url, pc.getProperty("user"), pc.getProperty("passwd"));

        }

        return conexion;

    }

    public static void cerrar() {

        if (conexion != null) {
            try {
                conexion.close();

            } catch (SQLException ex) {
                System.err.println("Error " + ex.getMessage());
            } catch (Exception ex) {
                System.err.println("Error " + ex.getMessage());
            }
        }

    }

}
