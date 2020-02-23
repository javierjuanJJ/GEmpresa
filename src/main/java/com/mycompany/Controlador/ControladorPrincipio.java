package com.mycompany.Controlador;

import com.mycompany.dao.ClientesDAO;
import java.io.IOException;
import java.util.List;

import com.mycompany.Modelo.Clientes;
import com.mycompany.mavenproject1.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class ControladorPrincipio {

    private static ClientesDAO controladorclientes;
    private static List<Clientes> Lista_de_clientes;
    protected static Clientes cliente_actual;
    @FXML
    private TextField nombre_usuario;
    @FXML
    private PasswordField contrasenya;
    @FXML
    private Button iniciar_sesion;
    @FXML
    private Button registro;
    @FXML
    private Button clientes;
    @FXML
    private Button facturas;
    @FXML
    private Button grupos;
    @FXML
    private Button articulos;
    @FXML
    private Button perfil;
    @FXML
    private Label entrado_label;

    @FXML
    public void initialize() {

        try {

            controladorclientes = new ClientesDAO();
            Lista_de_clientes = controladorclientes.findAll();
            clientes.setDisable(true);
            facturas.setDisable(true);
            grupos.setDisable(true);
            articulos.setDisable(true);
            perfil.setDisable(true);

            if (cliente_actual != null) {
                inicio_de_sesion();
            }

        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
            Platform.exit();
        }

    }
    
    @FXML
    public void cerrar_sesion() throws IOException {
        cliente_actual = null;
        Main main = new Main();
        main.Cambiar_Pantalla("Pantalla_Principio");
    }

    public void Cambiar_Pantalla(ActionEvent action) throws IOException {
        String id_boton = "";
        id_boton = ((Button) action.getSource()).getId();
        Main main = new Main();
        main.Cambiar_Pantalla(id_boton);
    }

    public void Iniciar_sesion() {
        String passwd = contrasenya.getText();
        int usuario = 0;
        try {
            usuario = Integer.parseInt(nombre_usuario.getText());
        } catch (Exception e) {
            usuario = 0;
        }
        boolean entrar = false;
        passwd = (controladorclientes.desencriptar_contrasenya(passwd));
        String comprobacion = "";
        for (int contador = 0; contador < Lista_de_clientes.size() && !entrar; contador++) {

            comprobacion = Lista_de_clientes.get(contador).getpasswd();
            
            if ((usuario == Lista_de_clientes.get(contador).getId()) && (passwd.equals(comprobacion))) {

                cliente_actual = Lista_de_clientes.get(contador);
                contador = Lista_de_clientes.size();
                inicio_de_sesion();
                entrar = true;

            }
        }
        
        entrado_label.setText(entrar && cliente_actual != null ? "El usuario " + cliente_actual.getNombre() + " es correcto " : "El usuario "  + " no es correcto ");

        

    }

    private void inicio_de_sesion() {
        if (cliente_actual.getNombre().equals("admin")) {
            clientes.setDisable(false);
            facturas.setDisable(false);
            grupos.setDisable(false);
            articulos.setDisable(false);
            perfil.setDisable(false);

        } else {
            facturas.setDisable(false);
            perfil.setDisable(false);
            registro.setDisable(true);
        }

    }
}
