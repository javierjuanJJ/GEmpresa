package com.mycompany.Controlador;

import com.mycompany.dao.ClientesDAO;
import com.mycompany.dao.VendedoresDAO;
import com.mycompany.dao.Conexion;
import com.mycompany.dao.FacturasDAO;
import com.mycompany.Modelo.Clientes;
import com.mycompany.Modelo.Facturas;
import com.mycompany.Modelo.Vendedores;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.mavenproject1.Main;
import java.util.Collections;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControladorFormularioConsultas {

    @FXML
    private TableView tabla_consulta;
    @FXML
    private Button boton_consultar;
    @FXML
    private Button boton_reiniciar_por_defecto;

    @FXML
    private CheckBox checkbox_factura;
    @FXML
    private CheckBox checkbox_vendedor;
    @FXML
    private CheckBox checkbox_cliente;
    @FXML
    private CheckBox checkbox_cantidad;
    @FXML
    private CheckBox checkbox_fecha;

    @FXML
    private ComboBox<Facturas> combobox_factura_desde;
    @FXML
    private ComboBox<Vendedores> combobox_vendedor_desde;
    @FXML
    private ComboBox<Clientes> combobox_cliente_desde;
    @FXML
    private TextField combobox_cantidad_desde;
    @FXML
    private DatePicker combobox_fecha_desde;

    @FXML
    private ComboBox<Facturas> checkbox_factura_hasta;
    @FXML
    private ComboBox<Vendedores> checkbox_vendedor_hasta;
    @FXML
    private ComboBox<Clientes> checkbox_cliente_hasta;
    @FXML
    private TextField checkbox_cantidad_hasta;
    @FXML
    private DatePicker checkbox_fecha_hasta;
    @FXML
    private Label cliente;
    @FXML
    private Label vendedor;

    private static FacturasDAO controladorfacturas;
    private static List<Facturas> Lista_de_Facturas;
    private static Clientes cliente_actual;
    private static ClientesDAO controladorclientes;
    private static VendedoresDAO controladorVendedores;
    private static boolean es_admin;
    private static List<Vendedores> Lista_de_Vendedores;
    private static List<Clientes> Lista_de_Clientes;

    @FXML
    public void initialize() {

        try {
            cliente_actual = ControladorPrincipio.cliente_actual;
            controladorfacturas = new FacturasDAO();
            controladorclientes = new ClientesDAO();
            controladorVendedores = new VendedoresDAO();

            es_admin = (cliente_actual.getNombre().toLowerCase().equals("admin"));
            Lista_de_Facturas = (es_admin) ? controladorfacturas.findAll()
                    : controladorfacturas.findAll2(cliente_actual);
            Lista_de_Vendedores = controladorVendedores.findAll();
            Lista_de_Clientes = controladorclientes.findAll();
            checkbox_cliente.setVisible(es_admin);
            checkbox_vendedor.setVisible(es_admin);
            this.combobox_cliente_desde.setVisible(es_admin);
            this.checkbox_cliente_hasta.setVisible(es_admin);
            this.combobox_vendedor_desde.setVisible(es_admin);
            this.checkbox_vendedor_hasta.setVisible(es_admin);
            cliente.setVisible(es_admin);
            vendedor.setVisible(es_admin);
            iniciar_datos();

        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
            Platform.exit();
        }

    }

    public void Cambiar_Pantalla(ActionEvent action) throws IOException {
        String id_boton = "";
        id_boton = ((Button) action.getSource()).getId();
        Main main = new Main();
        main.Cambiar_Pantalla(id_boton);
    }

    public void actualizar_clientes() {
        combobox_cliente_desde.getItems().clear();

        try {
            combobox_cliente_desde.getItems().addAll(controladorclientes.findAll());
        } catch (Exception e) {

        }

    }

    public void actualizar_clientes2() {
        checkbox_cliente_hasta.getItems().clear();

        try {
            checkbox_cliente_hasta.getItems().addAll(controladorclientes.findAll());
        } catch (Exception e) {

        }

    }

    public void actualizar_facturas() {
        combobox_factura_desde.getItems().clear();
        combobox_factura_desde.getItems().addAll(Lista_de_Facturas);
    }

    public void actualizar_facturas2() {
        checkbox_factura_hasta.getItems().clear();
        checkbox_factura_hasta.getItems().addAll(Lista_de_Facturas);
    }

    public void actualizar_vendedor() {
        combobox_vendedor_desde.getItems().clear();
        try {
            combobox_vendedor_desde.getItems().addAll(controladorVendedores.findAll());
        } catch (Exception e) {

        }
    }

    public void actualizar_vendedor2() {
        checkbox_vendedor_hasta.getItems().clear();
        try {
            checkbox_vendedor_hasta.getItems().addAll(controladorVendedores.findAll());
        } catch (Exception e) {

        }
    }

    public void coordinar() {

        int seleccionado_tabla_1 = (tabla_consulta.getSelectionModel().getSelectedIndex());

        int posicion = -1;

        if (seleccionado_tabla_1 >= 0) {
            posicion = seleccionado_tabla_1;
            tabla_consulta.getSelectionModel().clearSelection();
        }
        tabla_consulta.scrollTo(posicion);

    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public void consultar() {

        String nombre_base_datos = Conexion.nombre_base_datos;
        try {

            StringBuilder query = new StringBuilder();
            String part = "";
            tabla_consulta.getItems().clear();
            tabla_consulta.getColumns().clear();

            List<Facturas> Facturas_recibidas = null;
            Facturas_recibidas = new ArrayList<Facturas>();
            String campos_admin = ""
                    + nombre_base_datos
                    + ".facturas.id as id, "
                    + nombre_base_datos
                    + ".facturas.fecha as fecha, '' as total, "
                    + nombre_base_datos
                    + ".clientes.id as cliente, "
                    + nombre_base_datos
                    + ".clientes.nombre as nombre, "
                    + nombre_base_datos
                    + ".vendedores.id as vendedor, "
                    + nombre_base_datos
                    + ".vendedores.nombre as nombre";
            query.append("SELECT ").append(campos_admin).append(" FROM ").append(nombre_base_datos).append(".facturas, ").append(nombre_base_datos).append(".clientes , ").append(nombre_base_datos).append(".vendedores WHERE facturas.cliente=clientes.id AND facturas.vendedor=vendedores.id ");
            String todo = query.toString();
            part = (checkbox_factura.isSelected()) ? "and (facturas.id >= "+ this.combobox_factura_desde.getSelectionModel().getSelectedItem().getId() + " and facturas.id <= "+ this.checkbox_factura_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";
            query.append(part);
            part = (!es_admin) ? "and (facturas.cliente = " + cliente_actual.getId() + " ) " : "";
            query.append(part);
            part = (checkbox_cliente.isSelected()) ? "and (facturas.cliente >= "+ this.combobox_cliente_desde.getSelectionModel().getSelectedItem().getId()+ " and facturas.cliente <= "+ this.checkbox_cliente_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";
            query.append(part);
            part = (checkbox_vendedor.isSelected()) ? "and (facturas.vendedor >= "+ this.combobox_vendedor_desde.getSelectionModel().getSelectedItem().getId()+ " and facturas.vendedor <= "+ this.checkbox_vendedor_hasta.getSelectionModel().getSelectedItem().getId() + " ) " : "";
            query.append(part);
            LocalDate dateToConvert = this.combobox_fecha_desde.getValue();
            java.sql.Date fecha_sql = java.sql.Date.valueOf(dateToConvert);
            LocalDate dateToConvert2 = this.checkbox_fecha_hasta.getValue();
            java.sql.Date fecha_sql2 = java.sql.Date.valueOf(dateToConvert2);
            part = (checkbox_fecha.isSelected()) ? "and (facturas.fecha >= '" + fecha_sql.toString()+ "' and facturas.fecha <= '" + fecha_sql2.toString() + "' ) " : "";
            query.append(part);
            query.append(" ORDER BY facturas.id");
            double numero_desde = 0.0;
            double numero_hasta = 0.0;
            ArrayList campos = controladorfacturas.campos;
            Facturas_recibidas.addAll(controladorfacturas.findBySQL(query.toString()));

            for (int contador = 0; contador < 3; contador++) {
                tabla_consulta.getColumns().add(new TableColumn<Facturas, String>(campos.get(contador).toString()));
            }
            tabla_consulta.getColumns().add(new TableColumn<Facturas, String>("Cliente"));
            tabla_consulta.getColumns().add(new TableColumn<Facturas, String>("Vendedor"));
            tabla_consulta.getColumns().add(new TableColumn<Facturas, String>("Total"));

            ((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(0))
                    .setCellValueFactory(new PropertyValueFactory<>(FacturasDAO.campos.get(0)));
            ((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(1))
                    .setCellValueFactory(new PropertyValueFactory<>(FacturasDAO.campos.get(1)));
            ((TableColumn<Facturas, String>) tabla_consulta.getColumns().get(2))
                    .setCellValueFactory(new PropertyValueFactory<>(FacturasDAO.campos.get(2)));
            ((TableColumn<Facturas, Clientes>) tabla_consulta.getColumns().get(3))
                    .setCellValueFactory(new PropertyValueFactory<>("cliente"));
            ((TableColumn<Facturas, Vendedores>) tabla_consulta.getColumns().get(4))
                    .setCellValueFactory(new PropertyValueFactory<>("vendedor"));
            ((TableColumn<Facturas, Vendedores>) tabla_consulta.getColumns().get(5))
                    .setCellValueFactory(new PropertyValueFactory<>("Total"));

            boolean cantidad = this.checkbox_cantidad.isSelected();
            if (cantidad) {
                numero_desde = Double.parseDouble(combobox_cantidad_desde.getText());
                numero_hasta = Double.parseDouble(checkbox_cantidad_hasta.getText());
                for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
                    Facturas factura = new Facturas(Facturas_recibidas.get(contador));
                    factura.calcular_total_factura();
                    if (((factura.getTotal() >= numero_desde) && (factura.getTotal() <= numero_hasta))) {
                        tabla_consulta.getItems().add(factura);
                    }
                }
            } else {
                for (int contador = 0; contador < Facturas_recibidas.size(); contador++) {
                    Facturas factura = new Facturas(Facturas_recibidas.get(contador));
                    factura.calcular_total_factura();
                    tabla_consulta.getItems().add(factura);
                }
            }
            tabla_consulta.refresh();
        } catch (Exception e) {
            
        }

    }

    public void iniciar_datos() {

        try {

            Facturas factura_del_principio = null;
            Facturas factura_del_final = null;
            String campos_admin = ""
                    + Conexion.nombre_base_datos
                    + ".facturas.id as id, "
                    + Conexion.nombre_base_datos
                    + ".facturas.fecha as fecha, '' as total, "
                    + Conexion.nombre_base_datos
                    + ".clientes.id as cliente, "
                    + Conexion.nombre_base_datos
                    + ".clientes.nombre as nombre, "
                    + Conexion.nombre_base_datos
                    + ".vendedores.id as vendedor, "
                    + Conexion.nombre_base_datos
                    + ".vendedores.nombre as nombre";

            String principio_sql = "SELECT " + (campos_admin)
                    + " FROM "
                    + Conexion.nombre_base_datos
                    + ".facturas, " + ""
                    + Conexion.nombre_base_datos
                    + ".clientes , " + ""
                    + Conexion.nombre_base_datos
                    + ".vendedores "
                    + "WHERE facturas.cliente=clientes.id " + "AND facturas.vendedor=vendedores.id ";
            StringBuilder query_principio = new StringBuilder(principio_sql);
            StringBuilder query_final = new StringBuilder(principio_sql);
            String modificador_sql_max = "MAX";
            String modificador_sql_min = "MIN";
            String part_min = (!es_admin)
                    ? "AND facturas.id= " + "(SELECT MIN("
                    + Conexion.nombre_base_datos
                    + ".facturas.id) FROM "
                    + ""
                    + Conexion.nombre_base_datos
                    + ".facturas,"
                    + Conexion.nombre_base_datos
                    + " "
                    + "WHERE facturas.cliente=clientes.id and clientes.id=" + cliente_actual.getId() + ") "
                    : "AND facturas.id=(SELECT " + modificador_sql_min + "("
                    + Conexion.nombre_base_datos
                    + ".facturas.id) "
                    + "FROM "
                    + Conexion.nombre_base_datos
                    + ".facturas) ";

            String part_max = (!es_admin)
                    ? "AND facturas.id= " + "(SELECT MAX("
                    + Conexion.nombre_base_datos
                    + ".facturas.id) FROM "
                    + ""
                    + Conexion.nombre_base_datos
                    + ".facturas,"
                    + Conexion.nombre_base_datos
                    + ".clientes "
                    + "WHERE facturas.cliente=clientes.id and clientes.id=" + cliente_actual.getId() + ") "
                    : "AND facturas.id=(SELECT " + modificador_sql_max + "("
                    + Conexion.nombre_base_datos
                    + ".facturas.id) "
                    + "FROM "
                    + Conexion.nombre_base_datos
                    + ".facturas) ";

            query_principio.append(part_min);
            query_final.append(part_max);

            try {
                if (es_admin) {
                    factura_del_principio = new Facturas(controladorfacturas.findBySQL(query_principio.toString()).get(0));
                    factura_del_final = new Facturas(controladorfacturas.findBySQL(query_final.toString()).get(0));
                    factura_del_principio.calcular_total_factura();
                    factura_del_final.calcular_total_factura();
                } else {
                    List<Facturas> facturas_ordenadas = controladorfacturas.findBySQL(principio_sql + " AND clientes.id= " + cliente_actual.getId());
                    System.out.println(principio_sql + " AND clientes.id= " + cliente_actual.getId());
                    Collections.sort(facturas_ordenadas);
                    factura_del_principio = new Facturas(facturas_ordenadas.get(0));
                    factura_del_final = new Facturas(facturas_ordenadas.get(facturas_ordenadas.size() - 1));
                }

            } catch (Exception e) {

            }

            this.combobox_cantidad_desde.setText(factura_del_principio.getTotal() + "");
            this.checkbox_cantidad_hasta.setText(factura_del_final.getTotal() + "");
            this.combobox_factura_desde.getSelectionModel().select(factura_del_principio);
            this.checkbox_factura_hasta.getSelectionModel().select(factura_del_final);
            this.combobox_cliente_desde.getSelectionModel().select(Lista_de_Clientes.get(0));
            this.checkbox_cliente_hasta.getSelectionModel().select(Lista_de_Clientes.get(Lista_de_Clientes.size() - 1));
            this.combobox_vendedor_desde.getSelectionModel().select(Lista_de_Vendedores.get(0));
            this.checkbox_vendedor_hasta.getSelectionModel().select(Lista_de_Vendedores.get(Lista_de_Vendedores.size() - 1));

            java.sql.Date date2 = (java.sql.Date) factura_del_principio.getFecha();

            LocalDate date = date2.toLocalDate();

            this.combobox_fecha_desde.setValue(date);

            java.sql.Date date1 = (java.sql.Date) factura_del_final.getFecha();

            LocalDate date3 = date1.toLocalDate();

            this.checkbox_fecha_hasta.setValue(date3);

        } catch (Exception e) {

        }

    }

}
