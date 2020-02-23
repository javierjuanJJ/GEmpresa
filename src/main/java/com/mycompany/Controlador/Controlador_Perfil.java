package com.mycompany.Controlador;

import com.mycompany.dao.ClientesDAO;
import com.mycompany.Modelo.Clientes;
import java.io.IOException;
import com.mycompany.mavenproject1.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class Controlador_Perfil {

	@FXML
	private Label titulo;
	@FXML
	private Label id_cliente;
	@FXML
	private Label t_antigua_contrasenya;

	@FXML
	private PasswordField Contrasenya_nueva;
	@FXML
	private PasswordField confirmar_Contrasenya_nueva;
	@FXML
	private PasswordField antigua_Contrasenya_nueva;

	@FXML
	private TextField nombre_usuario;
	@FXML
	private TextField direccion;

	@FXML
	private Button actualizar;
	@FXML
	private Button insertar_usuario;
	private static ClientesDAO controladorclientes;
	private static Clientes cliente_actual;

	public void Cambiar_Pantalla(ActionEvent action) throws IOException {
		String id_boton = "";
		id_boton = ((Button) action.getSource()).getId();
		Main main = new Main();
		main.Cambiar_Pantalla(id_boton);
	}

	@FXML
	public void initialize() {
		try {
			controladorclientes = new ClientesDAO();
			cliente_actual = ControladorPrincipio.cliente_actual;

			if (cliente_actual != null) {
				titulo.setText("Perfil del usuario " + cliente_actual.getNombre());
				t_antigua_contrasenya.setVisible(true);
				id_cliente.setText(String.valueOf(cliente_actual.getId()));
				actualizar.setVisible(true);
				insertar_usuario.setVisible(false);
				antigua_Contrasenya_nueva.setVisible(true);
				nombre_usuario.setText(cliente_actual.getNombre());
				direccion.setText(cliente_actual.getDireccion());

			} else {
				antigua_Contrasenya_nueva.setVisible(true);
				t_antigua_contrasenya.setVisible(false);
				titulo.setText("Registro de nuevo usuario");
				id_cliente.setText("");
				insertar_usuario.setVisible(true);
				actualizar.setVisible(false);
				antigua_Contrasenya_nueva.setVisible(false);
			}

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
			Platform.exit();
		}
	}

	public void actualizar() {

		Clientes cliente = null;
		String antigua_Contrasenya_nueva = this.antigua_Contrasenya_nueva.getText();
		String Contrasenya_nueva = this.Contrasenya_nueva.getText();
		String confirmar_Contrasenya_nueva = this.confirmar_Contrasenya_nueva.getText();
		String nombre_usuario = this.nombre_usuario.getText();
		String direccion = this.direccion.getText();

		if ((antigua_Contrasenya_nueva.isEmpty()) && (Contrasenya_nueva.isEmpty())
				&& (confirmar_Contrasenya_nueva.isEmpty())) {

			cliente = new Clientes();
			cliente.setId(Integer.parseInt(this.id_cliente.getText()));
			cliente.setDireccion(direccion);
			cliente.setNombre(nombre_usuario);
			cliente.setpasswd(cliente_actual.getpasswd());

			try {
				controladorclientes.update(cliente);

			} catch (Exception e) {
				(new Main()).mensajeExcepcion(e, e.getMessage());
			}

		} else {
			if ((((Contrasenya_nueva.equals(confirmar_Contrasenya_nueva)))) && ((cliente_actual.getpasswd().equals(controladorclientes.desencriptar_contrasenya(antigua_Contrasenya_nueva))))) {
                            System.out.print("ya");
				cliente = new Clientes();
                                cliente.setId(Integer.parseInt(id_cliente.getText()));
				cliente.setDireccion(direccion);
				cliente.setNombre(nombre_usuario);
				cliente.setpasswd(controladorclientes.desencriptar_contrasenya(Contrasenya_nueva));
                                
                                
                                try {
                                    controladorclientes.update(cliente);

                                } catch (Exception e) {
                                        (new Main()).mensajeExcepcion(e, e.getMessage());
                                }
                                
			}
		}
	}

	public void insertar_usuario() {
		Clientes cliente = null;
		String Contrasenya_nueva = this.Contrasenya_nueva.getText();
		String confirmar_Contrasenya_nueva = this.confirmar_Contrasenya_nueva.getText();
		String nombre_usuario = this.nombre_usuario.getText();
		String direccion = this.direccion.getText();

		cliente = new Clientes();
		cliente.setDireccion(direccion);
		cliente.setNombre(nombre_usuario);
		cliente.setpasswd(controladorclientes.desencriptar_contrasenya(Contrasenya_nueva));

		if ((Contrasenya_nueva.equals(confirmar_Contrasenya_nueva)) && (!(nombre_usuario.equalsIgnoreCase("admin")))) {
                    
			try {
				controladorclientes.insert(cliente);

			} catch (Exception e) {
				(new Main()).mensajeExcepcion(e, e.getMessage());
			}
		}
	}
}
