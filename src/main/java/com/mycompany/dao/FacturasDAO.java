package com.mycompany.dao;

import com.mycompany.dao.GenericoDAO;
import com.mycompany.Modelo.Articulos;
import com.mycompany.Modelo.Clientes;
import com.mycompany.Modelo.Facturas;
import com.mycompany.Modelo.Lineas_Facturas;
import com.mycompany.Modelo.Vendedores;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import javax.sql.rowset.JdbcRowSet;
import javax.sql.rowset.RowSetProvider;
import javafx.application.Platform;

public class FacturasDAO implements GenericoDAO<Facturas> {

    protected static final String sql_select_by_PK = "SELECT * FROM "
            + Conexion.nombre_base_datos
            + ".facturas, "
            + Conexion.nombre_base_datos
            + ".clientes , "
            + Conexion.nombre_base_datos
            + ".vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id and clientes.id=?;";
    protected static final String sql_select_by_PK_user = "SELECT * FROM "
            + Conexion.nombre_base_datos
            + ".facturas, "
            + Conexion.nombre_base_datos
            + ".clientes , "
            + Conexion.nombre_base_datos
            + ".vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id and clientes.id=?;";
    protected static final String sql_select_all = "SELECT * FROM "
            + Conexion.nombre_base_datos
            + ".facturas, "
            + Conexion.nombre_base_datos
            + ".clientes , "
            + Conexion.nombre_base_datos
            + ".vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id ORDER BY facturas.id;";
    protected static final String sql_UPDATE = "UPDATE `"
            + Conexion.nombre_base_datos
            + "`.`clientes` SET `nombre`=?, `direccion`=?, `passwd`=? WHERE `id`=?;";

    protected static final String sql_INSERT = "INSERT INTO `"
            + Conexion.nombre_base_datos
            + "`.`facturas` (`fecha`, `cliente`, `vendedor`, `formapago`) VALUES (?, ?, ?, ?);";
    protected static final String sql_insert_Lineas_factura = "INSERT INTO `"
            + Conexion.nombre_base_datos
            + "`.`lineas_factura` (`factura`, `articulo`, `cantidad`, `importe`,`linea`) VALUES (?, ?, ?, ?, ?);";
    protected static final String sql_DELETE = "DELETE FROM `"
            + Conexion.nombre_base_datos
            + "`.`facturas` WHERE `id`=?;";
    protected static final String sql_DELETE_Lineas_facturas = "DELETE FROM "
            + Conexion.nombre_base_datos
            + ".lineas_factura WHERE `factura`=?;";
    protected static final String sql_Lineas_factura = "SELECT * FROM "
            + Conexion.nombre_base_datos
            + ".lineas_factura, "
            + Conexion.nombre_base_datos
            + ".articulos,"
            + Conexion.nombre_base_datos
            + ".grupos WHERE lineas_factura.articulo=articulos.id and articulos.grupo=grupos.id and factura=? ORDER BY "
            + Conexion.nombre_base_datos
            + ".lineas_factura.linea , "
            + Conexion.nombre_base_datos
            + ".lineas_factura.factura;";

    protected static final String sql_actualizar_factura = "UPDATE "
            + Conexion.nombre_base_datos
            + ".facturas SET fecha=?,cliente=?,vendedor=? WHERE id=?;";
    protected static final String sql_actualizar_linea_factura = "UPDATE "
            + Conexion.nombre_base_datos
            + ".lineas_factura SET articulo=?,importe=?,cantidad=? WHERE linea=? AND factura=?;";
    protected static final String sql_ver_ultima_factura = "SELECT "
            + Conexion.nombre_base_datos
            + ".lineas_factura SET articulo=?,importe=?,cantidad=? WHERE linea=? AND factura=?;";

    public static PreparedStatement preparedstatement = null;
    public static ArrayList<String> campos = null;
    private static Connection conexion;

    public FacturasDAO() {
        try {
            conexion = Conexion.getConnection();

        } catch (Exception e) {

            Platform.exit();
        }
    }

    public Facturas findByPK(int id) throws Exception {

        return null;
    }

    public List<Facturas> findAll() throws Exception {
        List<Facturas> Facturas_recibidas = null;
        Facturas_recibidas = new ArrayList<Facturas>();
        ResultSet resultset = null;
        ResultSet resultset_lineas = null;
        PreparedStatement preparedstatement_lineas = null;
        preparedstatement = conexion.prepareStatement(sql_select_all);
        resultset = preparedstatement.executeQuery();

        Facturas factura = null;
        while (resultset.next()) {

            factura = new Facturas();
            factura.setId(resultset.getInt(1));
            factura.setFecha(resultset.getDate(2));
            factura.setCliente(new Clientes());
            factura.setVendedor(new Vendedores());
            factura.setForma_de_pago(resultset.getString(5));
            factura.getCliente().setId(resultset.getInt(6));
            factura.getCliente().setNombre(resultset.getString(7));
            factura.getCliente().setDireccion(resultset.getString(8));
            factura.getCliente().setpasswd(resultset.getString(9));
            factura.getVendedor().setId(resultset.getInt(10));
            factura.getVendedor().setNombre(resultset.getString(11));
            factura.getVendedor().setFecha_ingreso(resultset.getDate(12));
            factura.getVendedor().setSalario(resultset.getDouble(13));

            preparedstatement_lineas = conexion.prepareStatement(sql_Lineas_factura);
            preparedstatement_lineas.setInt(1, factura.getId());
            resultset_lineas = preparedstatement_lineas.executeQuery();

            while (resultset_lineas.next()) {
                Lineas_Facturas linea_factura = new Lineas_Facturas();
                linea_factura.setLinea(resultset_lineas.getInt(1));
                linea_factura.setImporte(resultset_lineas.getDouble(5));
                linea_factura.setCantidad(resultset_lineas.getInt(4));
                linea_factura.setArticulo(new Articulos());
                linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
                linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
                linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
                linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
                linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
                linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));
                factura.getLineas_de_la_factura().add(linea_factura);
            }
            Facturas_recibidas.add(factura);
        }
        return Facturas_recibidas;
    }

    public List<Facturas> findAll2(Clientes cliente) throws Exception {
        List<Facturas> Facturas_recibidas = null;
        Facturas_recibidas = new ArrayList<Facturas>();
        ResultSet resultset = null;
        ResultSet resultset_lineas = null;
        PreparedStatement preparedstatement_lineas = null;
        preparedstatement = conexion.prepareStatement(sql_select_by_PK_user);
        preparedstatement.setInt(1, cliente.getId());
        resultset = preparedstatement.executeQuery();
        Facturas factura = null;
        while (resultset.next()) {
            factura = new Facturas();
            factura.setId(resultset.getInt(1));
            factura.setFecha(resultset.getDate(2));
            factura.setCliente(new Clientes());
            factura.setVendedor(new Vendedores());
            factura.setForma_de_pago(resultset.getString(5));
            factura.getCliente().setId(resultset.getInt(6));
            factura.getCliente().setNombre(resultset.getString(7));
            factura.getCliente().setDireccion(resultset.getString(8));
            factura.getCliente().setpasswd(resultset.getString(9));
            factura.getVendedor().setId(resultset.getInt(10));
            factura.getVendedor().setNombre(resultset.getString(11));
            factura.getVendedor().setFecha_ingreso(resultset.getDate(12));
            factura.getVendedor().setSalario(resultset.getDouble(13));
            preparedstatement_lineas = conexion.prepareStatement(sql_Lineas_factura);
            preparedstatement_lineas.setInt(1, factura.getId());
            resultset_lineas = preparedstatement_lineas.executeQuery();

            while (resultset_lineas.next()) {
                Lineas_Facturas linea_factura = new Lineas_Facturas();
                linea_factura.setLinea(resultset_lineas.getInt(1));
                linea_factura.setImporte(resultset_lineas.getDouble(5));
                linea_factura.setCantidad(resultset_lineas.getInt(4));
                linea_factura.setArticulo(new Articulos());
                linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
                linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
                linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
                linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
                linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
                linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));
                factura.getLineas_de_la_factura().add(linea_factura);
            }
            Facturas_recibidas.add(factura);
        }
        return Facturas_recibidas;
    }

    public List<Facturas> findBySQL(String sqlselect) throws Exception {
        List<Facturas> Facturas_recibidas = null;
        Facturas_recibidas = new ArrayList<Facturas>();
        Facturas factura = null;
        String jdbcUrl = Conexion.url;
        String usr = Conexion.kusuario;
        String pw = Conexion.kcontrasenya;
        campos = new ArrayList();
        try ( JdbcRowSet resultset = RowSetProvider.newFactory().createJdbcRowSet();  JdbcRowSet resultset_lineas = RowSetProvider.newFactory().createJdbcRowSet();) {

            resultset.setUrl(jdbcUrl);
            resultset.setUsername(usr);
            resultset.setPassword(pw);
            resultset_lineas.setUrl(jdbcUrl);
            resultset_lineas.setUsername(usr);
            resultset_lineas.setPassword(pw);
            resultset.setCommand(sqlselect);
            resultset.execute();
            ResultSetMetaData rsmd = resultset.getMetaData();
            int columnas = rsmd.getColumnCount();
            for (int cont = 1; cont <= columnas; cont++) {
                campos.add(rsmd.getColumnName(cont));
            }
            while (resultset.next()) {
                factura = new Facturas();
                factura.setId(resultset.getInt(1));
                factura.setFecha(resultset.getDate(2));
                factura.setCliente(new Clientes());
                factura.setVendedor(new Vendedores());
                factura.getCliente().setId(resultset.getInt(4));
                factura.getCliente().setNombre(resultset.getString(5));
                factura.getVendedor().setId(resultset.getInt(6));
                factura.getVendedor().setNombre(resultset.getString(7));
                resultset_lineas.setCommand(sql_Lineas_factura);
                resultset_lineas.setInt(1, factura.getId());
                resultset_lineas.execute();

                while (resultset_lineas.next()) {

                    Lineas_Facturas linea_factura = new Lineas_Facturas();

                    linea_factura.setLinea(resultset_lineas.getInt(1));
                    linea_factura.setImporte(resultset_lineas.getDouble(5));
                    linea_factura.setCantidad(resultset_lineas.getInt(4));
                    linea_factura.setArticulo(new Articulos());

                    linea_factura.getArticulo().setId(resultset_lineas.getInt(6));
                    linea_factura.getArticulo().setNombre(resultset_lineas.getString(7));
                    linea_factura.getArticulo().setPrecio(resultset_lineas.getDouble(8));
                    linea_factura.getArticulo().setStock(resultset_lineas.getInt(11));
                    linea_factura.getArticulo().setCodigo(resultset_lineas.getString(9));
                    linea_factura.getArticulo().setGrupo(resultset_lineas.getInt(10));

                    factura.getLineas_de_la_factura().add(linea_factura);
                }
                Facturas_recibidas.add(factura);

            }
        } catch (SQLException se) {

        }
        return Facturas_recibidas;
    }

    public void FindBySQL(String sqlselect) throws Exception {
        try {
            conexion.setAutoCommit(false);
            preparedstatement = conexion.prepareStatement(sqlselect);
            preparedstatement.executeUpdate();
            conexion.commit();
        } catch (SQLException e) {
            conexion.rollback();
        }
    }

    public boolean insert(Facturas t) throws Exception {
        boolean realizado = true;

        try {
            conexion.setAutoCommit(false);
            preparedstatement = conexion.prepareStatement("SELECT MAX(id) FROM "
                    + Conexion.nombre_base_datos
                    + ".facturas;");
            ResultSet id_maxima = preparedstatement.executeQuery();
            id_maxima.beforeFirst();
            int id_de_factura = 0;
            while (id_maxima.next()) {
                id_de_factura = (id_maxima.getInt(1)) + 1;
            }
            preparedstatement = conexion.prepareStatement(sql_INSERT);
            preparedstatement.setDate(1, (Date) t.getFecha());
            preparedstatement.setInt(2, t.getCliente().getId());
            preparedstatement.setInt(3, t.getVendedor().getId());
            preparedstatement.setString(4, t.getForma_de_pago());
            preparedstatement.addBatch();
            preparedstatement.executeBatch();
            conexion.commit();

            preparedstatement = conexion.prepareStatement(sql_insert_Lineas_factura);

            for (int contador = 0; contador < t.getLineas_de_la_factura().size(); contador++) {
                preparedstatement.setInt(1, id_de_factura);
                preparedstatement.setDouble(2, t.getLineas_de_la_factura().get(contador).getArticulo().getId());
                preparedstatement.setInt(3, t.getLineas_de_la_factura().get(contador).getCantidad());
                preparedstatement.setDouble(4, t.getLineas_de_la_factura().get(contador).getImporte());
                preparedstatement.setDouble(5, t.getLineas_de_la_factura().get(contador).getLinea());
                preparedstatement.addBatch();
            }

            int[] resultados = preparedstatement.executeBatch();
            conexion.commit();
            for (int contador = 0; contador < resultados.length; contador++) {
                if (resultados[contador] <= 0) {
                    realizado = false;
                    contador = resultados.length;
                    throw new SQLException();
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            e.printStackTrace();
        }
        return realizado;
    }

    public boolean insert(Lineas_Facturas t, int id_factura) throws Exception {
        boolean realizado = true;

        try {

            preparedstatement = conexion.prepareStatement(sql_insert_Lineas_factura);
            preparedstatement.setInt(1, id_factura);
            preparedstatement.setDouble(2, t.getArticulo().getId());
            preparedstatement.setInt(3, t.getCantidad());
            preparedstatement.setDouble(4, t.getImporte());
            preparedstatement.setDouble(5, t.getLinea());
            preparedstatement.executeUpdate();
            //"SELECT * FROM v_empresa_ad_p1.lineas_factura ORDER by linea DESC, factura DESC LIMIT 1"

        } catch (SQLException e) {
            conexion.rollback();
        }
        return realizado;
    }

    public boolean update(Facturas t) throws Exception {
        boolean realizado = true;

        try {
            conexion.setAutoCommit(false);

            preparedstatement = conexion.prepareStatement(sql_actualizar_factura);

            preparedstatement.setDate(1, (Date) t.getFecha());
            preparedstatement.setInt(2, t.getCliente().getId());
            preparedstatement.setInt(3, t.getVendedor().getId());
            preparedstatement.setInt(4, t.getId());

            preparedstatement.addBatch();
            System.out.println(t.getCliente() + " " + t.getCliente().getId());

            preparedstatement.executeBatch();
            conexion.commit();

            preparedstatement = conexion.prepareStatement(sql_actualizar_linea_factura);

            for (int contador = 0; contador < t.getLineas_de_la_factura().size(); contador++) {
                preparedstatement.setInt(1, t.getLineas_de_la_factura().get(contador).getArticulo().getId());
                preparedstatement.setDouble(2, t.getLineas_de_la_factura().get(contador).getImporte());
                preparedstatement.setInt(3, t.getLineas_de_la_factura().get(contador).getCantidad());
                preparedstatement.setInt(4, t.getLineas_de_la_factura().get(contador).getLinea());
                preparedstatement.setInt(5, t.getId());
                preparedstatement.addBatch();
            }

            int[] resultados = preparedstatement.executeBatch();
            conexion.commit();
            for (int contador = 0; contador < resultados.length; contador++) {
                if (resultados[contador] <= 0) {
                    realizado = false;
                    contador = resultados.length;
                }
            }
        } catch (SQLException e) {
            conexion.rollback();
            e.printStackTrace();
        }
        return realizado;
    }

    public boolean delete(int id) throws Exception {

        int realizado = 0;
        int realizado2 = 0;

        preparedstatement = conexion.prepareStatement(sql_DELETE_Lineas_facturas);
        preparedstatement.setInt(1, id);
        realizado2 = preparedstatement.executeUpdate();
        preparedstatement = conexion.prepareStatement(sql_DELETE);
        preparedstatement.setInt(1, id);
        realizado = preparedstatement.executeUpdate();
        return (realizado > 0) && (realizado2 > 0);
    }

    public List<String> findAll_formas_pago() throws SQLException, Exception {
        List<String> formas_pago = new ArrayList();
        preparedstatement = conexion.prepareStatement("SELECT DISTINCT formapago FROM "
                + Conexion.nombre_base_datos
                + ".facturas");
        ResultSet resultSet = preparedstatement.executeQuery();
        while (resultSet.next()) {
            formas_pago.add(resultSet.getString(1));
        }
        return formas_pago;
    }

}
