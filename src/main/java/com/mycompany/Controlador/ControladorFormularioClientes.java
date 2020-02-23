package com.mycompany.Controlador;

import com.mycompany.dao.ClientesDAO;
import com.mycompany.Modelo.Clientes;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.mavenproject1.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class ControladorFormularioClientes {

    private static ClientesDAO controladorclientes;
    private static List<Clientes> Lista_de_clientes;
    private static int pos;
    static final String CLIENTE = "Cliente";
    @FXML
    private Button boton_anyadir_atras_clientes;
    @FXML
    private Button boton_actualizar_clientes;
    @FXML
    private Button boton_eliminar_clientes;
    @FXML
    private Button boton_aceptar_clientes;
    @FXML
    private Button boton_atras_clientes;
    @FXML
    private Button boton_buscar_por_id_clientes;
    @FXML
    private Button boton_buscar;
    @FXML
    private Label label_informacion;
    private static boolean es_admin;
    @FXML
    private Button siguiente_clientes;
    @FXML
    private Button anterior_clientes;
    @FXML
    private Button ultimo_clientes;
    @FXML
    private Button primero_clientes;
    private static Clientes cliente_actual;
    public static Clientes cliente_ver_facturas;
    @FXML
    private ComboBox<Clientes> ComboBox_id_clientes;
    @FXML
    private TextField TextField_Nombre_clientes;
    @FXML
    private TextField TextField_direccion;
    @FXML
    private TextField TextField_buscar_por_id_clientes;

    @FXML
    public void initialize() {

        try {
            cliente_actual = ControladorPrincipio.cliente_actual;
            es_admin = (cliente_actual.getId() == 1);
            boton_buscar.setVisible(es_admin);
            controladorclientes = new ClientesDAO();
            Lista_de_clientes = controladorclientes.findAll();
            Clientes cliente = Lista_de_clientes.get(0);
            ComboBox_id_clientes.getItems().addAll(Lista_de_clientes);
            ComboBox_id_clientes.getSelectionModel().select(0);
            poner_informacion(cliente);
            label_informacion.setText("Registro " + 1 + " de " + Lista_de_clientes.size());
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
    
    public void cambiar_a_facturas() {

        try {
            cliente_ver_facturas = ComboBox_id_clientes.getSelectionModel().getSelectedItem();
            new Main().Cambiar_Pantalla("Pantalla_Facturas");
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }

    }

    public void actualizar_informacion_clientes() {

        try {
            ComboBox_id_clientes.getItems().clear();
            ComboBox_id_clientes.getItems().setAll(Lista_de_clientes);
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }

    }

    public void poner_informacion_clientes() {
        Clientes cliente_seleccionado = null;
        cliente_seleccionado = ComboBox_id_clientes.getSelectionModel().getSelectedItem();

        try {
            poner_informacion(cliente_seleccionado);
        } catch (NullPointerException e) {

        }
    }

    public void insertar_informacion_clientes() {

        Clientes cliente_seleccionado = null;

        try {
            cliente_seleccionado = coger_informacion();
            if ((!(controladorclientes.insert(cliente_seleccionado)))) {

                throw new Exception("Error en la insercion de datos del formulario");

            } else {
                Lista_de_clientes.add(cliente_seleccionado);
                ComboBox_id_clientes.getSelectionModel().select(cliente_seleccionado);
                (new Main()).mensajeConfirmacion("Insercion completada", "La operacion ha sido un exito", CLIENTE);
            }
        } catch (Exception e) {
            ComboBox_id_clientes.getItems().clear();
            ComboBox_id_clientes.getSelectionModel().selectFirst();
            ComboBox_id_clientes.setPromptText("");
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }

    }

    public void actualizar_informacion() {
        Clientes cliente_seleccionado = coger_informacion();

        try {

            if (!(controladorclientes.update(cliente_seleccionado))) {
                throw new Exception("Error en la actualizacion de datos del formulario");

            } else {
                Lista_de_clientes.set(pos, cliente_seleccionado);
                (new Main()).mensajeConfirmacion("Actualizacion completada", "La operacion ha sido un exito", CLIENTE);
            }

        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }

    public void eliminar_informacion() {
        Clientes cliente_seleccionado = coger_informacion();

        try {

            if (!(controladorclientes.delete(cliente_seleccionado.getId()))) {
                throw new Exception("Error en el borrado de datos del formulario");
            } else {
                Lista_de_clientes.remove(pos);
                ComboBox_id_clientes.getSelectionModel().select(pos - 1);
                (new Main()).mensajeConfirmacion("Eliminacion completada", "La operacion ha sido un exito", CLIENTE);
            }

        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }

    public void Buscar_por_id() {
        Clientes cliente_seleccionado = null;
        try {
            cliente_seleccionado = controladorclientes.findByPK(Integer.parseInt(TextField_buscar_por_id_clientes.getText()));
            ComboBox_id_clientes.getSelectionModel().select(cliente_seleccionado);
            label_informacion.setText("Registro " + (Lista_de_clientes.indexOf(cliente_seleccionado) + 1) + " de " + Lista_de_clientes.size());
        } catch (Exception e) {
            cliente_seleccionado = new Clientes();
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }

        poner_informacion(cliente_seleccionado);
    }

    public void poner_informacion(Clientes cliente) {
        ComboBox_id_clientes.getSelectionModel().select(cliente);
        TextField_Nombre_clientes.setText(cliente.getNombre());
        TextField_direccion.setText(cliente.getDireccion());
    }

    public Clientes coger_informacion() {
        int id = 0;

        try {
            if (ComboBox_id_clientes.getPromptText().isEmpty()) {
                id = 0;
            } else {
                id = ComboBox_id_clientes.getSelectionModel().getSelectedItem().getId();
                pos = ComboBox_id_clientes.getSelectionModel().getSelectedIndex();
            }
        } catch (Exception e) {
            id = 0;
        }

        Clientes cliente = new Clientes(id, TextField_Nombre_clientes.getText(), TextField_direccion.getText(), "");

        if ((cliente.getNombre().isEmpty()) && (cliente.getDireccion().isEmpty())) {
            cliente = null;
        }

        return cliente;
    }

    public void cambiar_de_extremos(ActionEvent action) {
        ultimo_clientes.setDisable(false);
        siguiente_clientes.setDisable(false);
        anterior_clientes.setDisable(false);
        primero_clientes.setDisable(false);

        String id_boton = "";
        id_boton = ((Button) action.getSource()).getId();
        Clientes cliente = null;
        int posicion = 0;
        int tamanyo = 0;

        try {
            tamanyo = Lista_de_clientes.size() - 1;
            posicion = ComboBox_id_clientes.getSelectionModel().getSelectedIndex();

            switch (id_boton) {

                case "Siguiente":
                    posicion++;
                    break;
                case "Ultimo":
                    posicion = tamanyo;
                    break;
                case "Anterior":
                    posicion--;
                    break;
                case "Primero":
                    posicion = 0;
                    break;
            }

            if ((posicion == tamanyo) || (posicion == 0)) {
                if (posicion == tamanyo) {

                    ultimo_clientes.setDisable(true);
                    siguiente_clientes.setDisable(true);

                } else {
                    anterior_clientes.setDisable(true);
                    primero_clientes.setDisable(true);
                }
            }

            cliente = Lista_de_clientes.get(posicion);
            ComboBox_id_clientes.getSelectionModel().select(posicion);
            poner_informacion(cliente);
            label_informacion.setText("Registro " + (posicion + 1) + " de " + Lista_de_clientes.size());
        } catch (Exception e) {
            posicion = 0;
        }

    }

}
