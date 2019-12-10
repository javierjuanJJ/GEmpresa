package com.mycompany.Controlador;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import Modelo.Articulos;
import Modelo.Clientes;
import Modelo.Grupos;
import com.mycompany.mavenproject1.Main;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControladorFormularioArticulos {
	
	private static ArticulosDAO controladorarticulos;
	private static List<Articulos> Lista_de_articulos;
	private static List<Grupos> Lista_de_grupos;
	private static int pos;
	private Stage escenario;
	private File f;
	
	static final String ARTICULO="Articulo";
	@FXML
	private Button boton_anyadir_atras_articulos;
	@FXML
	private Button boton_actualizar_articulos;
	@FXML
	private Button boton_eliminar_articulos;
	@FXML
	private Button boton_aceptar_articulos;
	@FXML
	private Button boton_atras_articulos;
	@FXML
	private Button boton_buscar_por_id_articulos;
	
	@FXML
	private Button siguiente_clientes;
	@FXML
	private Button anterior_clientes;
	@FXML
	private Button ultimo_clientes;
	@FXML
	private Button primero_clientes;
	
	@FXML
	private ComboBox<Articulos> ComboBox_id_articulos;
	@FXML
	private TextField TextField_Nombre_articulos;
	@FXML
	private TextField TextField_descripcion;
	@FXML
	private TextField TextField_direccion_articulos;
	@FXML
	private ComboBox<Grupos> ComboBox_grupos_articulos;
	@FXML
	private TextField TextField_precio_articulos;
	@FXML
	private ComboBox<Articulos> ComboBox_codigo_articulos;
	@FXML
	private TextField TextField_buscar_por_id_articulos;
	@FXML
	private TextField stock;

	@FXML
	public void initialize() {

		try {
			controladorarticulos=new ArticulosDAO();
			Lista_de_articulos = controladorarticulos.findAll();
			Lista_de_grupos = controladorarticulos.findAll_grupos();
			
		} catch (Exception e) {
			
			Platform.exit();
		}

	}

	public void Cambiar_Pantalla(ActionEvent action) throws IOException {
		String id_boton = "";
		id_boton = ((Button) action.getSource()).getId();
		Main main = new Main();
		main.Cambiar_Pantalla(id_boton);
	}

	

	public void actualizar_informacion_clientes() {

		try {
			
			ComboBox_id_articulos.getItems().clear();
			ComboBox_id_articulos.getItems().setAll(Lista_de_articulos);

		} catch (Exception e) {

		}
	}

	public void actualizar_informacion_grupos() {

		try {
			
			ComboBox_grupos_articulos.getItems().clear();
			ComboBox_grupos_articulos.getItems().setAll(Lista_de_grupos);

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}
	}

	public void poner_informacion_clientes() {
		Articulos articulo_seleccionado = null;
		try {
			articulo_seleccionado = ComboBox_id_articulos.getSelectionModel().getSelectedItem();
			poner_informacion(articulo_seleccionado);
		}
		catch (Exception e) {
			articulo_seleccionado =new Articulos();
			poner_informacion(articulo_seleccionado);
		}
		
		
		
	}

	public void insertar_informacion_clientes() {

		Articulos articulo_seleccionado = null;

		try {
			articulo_seleccionado = coger_informacion();
			articulo_seleccionado.setCodigo(articulo_seleccionado.getNombre().substring(0, articulo_seleccionado.getNombre().length() / 2));
			if ((!(controladorarticulos.insert(articulo_seleccionado)))) {
				throw new Exception("Error en la insercion de datos del formulario");
			} else {
				ComboBox_id_articulos.getSelectionModel().select(articulo_seleccionado);
				(new Main()).mensajeConfirmacion("Insercion completada", "La operacion ha sido un exito",ARTICULO);
			}

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}
	}

	public void actualizar_informacion() {

		Articulos articulo_seleccionado = null;

		try {
			articulo_seleccionado = coger_informacion();

			if ((!(controladorarticulos.update(articulo_seleccionado)))) {
				throw new Exception("Error en la actualizacion de datos del formulario");
			} else {
				Lista_de_articulos.set(pos, articulo_seleccionado);
				(new Main()).mensajeConfirmacion("Actualizacion completada", "La operacion ha sido un exito",ARTICULO);
			}

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}
	}

	public void eliminar_informacion() {

		Articulos articulo_seleccionado = null;

		try {
			articulo_seleccionado = coger_informacion();
			if ((!(controladorarticulos.delete(articulo_seleccionado.getId())))) {
				throw new Exception("Error en el borrado de datos del formulario");
			} else {
				Lista_de_articulos.remove(pos);
				ComboBox_id_articulos.getSelectionModel().select(pos - 1);
				(new Main()).mensajeConfirmacion("Eliminacion completada", "La operacion ha sido un exito",ARTICULO);
			}

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}
	}

	public void Buscar_por_id() {

		Articulos articulo_seleccionado = null;
		try {
			articulo_seleccionado = controladorarticulos.findByPK(Integer.parseInt(TextField_buscar_por_id_articulos.getText()));

			ComboBox_id_articulos.getItems().clear();
			ComboBox_id_articulos.getItems().add(controladorarticulos.findByPK(articulo_seleccionado.getId()));
			ComboBox_id_articulos.getSelectionModel().select(0);

		} catch (Exception e) {
			poner_informacion(new Articulos());
		}
		poner_informacion(articulo_seleccionado);
	}

	public void poner_informacion(Articulos articulo) {

		try {
			TextField_Nombre_articulos.setText(articulo.getNombre());
			ComboBox_grupos_articulos.getItems().clear();
			ComboBox_grupos_articulos.getItems().add(controladorarticulos.findByPK_grupos(articulo.getGrupo()));
			ComboBox_grupos_articulos.getSelectionModel().select(0);
			TextField_precio_articulos.setText(articulo.getPrecio() + "");
			ComboBox_codigo_articulos.setPromptText(articulo.getCodigo());
			stock.setText(articulo.getStock() + "");
		} catch (Exception e) {

		}

	}

	public Articulos coger_informacion() {

		Articulos articulo = null;
		int id = 0;
		try {
			
			if (ComboBox_id_articulos.getSelectionModel().getSelectedItem() != null) {
				articulo = new Articulos(ComboBox_id_articulos.getSelectionModel().getSelectedItem());
				id = articulo.getId();
				pos = ComboBox_id_articulos.getSelectionModel().getSelectedIndex();
				
			}
			else {
				articulo = new Articulos();
			}
						
			articulo.setNombre(TextField_Nombre_articulos.getText());
			articulo.setPrecio(Double.parseDouble(TextField_precio_articulos.getText()));
			articulo.setCodigo(ComboBox_codigo_articulos.getPromptText());
			articulo.setId(id);
			articulo.setGrupo(cogergrupo().getId());
			articulo.setStock(Integer.parseInt(stock.getText()));
			
		} catch (Exception e) {
			articulo = null;
		}

		return articulo;
	}

	public Grupos cogergrupo() {
		Grupos grupo_seleccionado = new Grupos();
		try {
			grupo_seleccionado = Lista_de_grupos.get(ComboBox_grupos_articulos.getSelectionModel().getSelectedIndex());
		}
		catch (Exception e) {
			
		}
		
		
		return grupo_seleccionado;
	}
	
	public void cambiar_de_extremos(ActionEvent action) {
		ultimo_clientes.setDisable(false);
		siguiente_clientes.setDisable(false);
		anterior_clientes.setDisable(false);
		primero_clientes.setDisable(false);

		String id_boton = "";
		id_boton = ((Button) action.getSource()).getId();
		Articulos articulo = null;
		int posicion = 0;
		int tamanyo = 0;

		try {
			tamanyo = Lista_de_articulos.size() - 1;
			posicion = ComboBox_id_articulos.getSelectionModel().getSelectedIndex();

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

			articulo = Lista_de_articulos.get(posicion);
			ComboBox_id_articulos.getSelectionModel().select(posicion);
			poner_informacion(articulo);

		} catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}

	}
	
	public void Importar() {
		FileChooser fc = creaFileChooser("Abriendo...");
        f = fc.showOpenDialog(escenario);
        String respuesta="";
        ArrayList<Articulos> articulos_importados= new ArrayList();
        
        try {
        	articulos_importados=cargar_articulos();
			respuesta=controladorarticulos.insert_batch(articulos_importados);
			(new Main()).mensajeConfirmacion("Importacion completada", respuesta ,"batch");
			Lista_de_articulos = controladorarticulos.findAll();
        } catch (Exception e) {
			(new Main()).mensajeExcepcion(e, e.getMessage());
		}
        
        
        
	}
	
	private ArrayList<Articulos> cargar_articulos() {
		ArrayList<Articulos> articulos_importados= new ArrayList();
		
		
		try (BufferedReader lectura=new BufferedReader(new FileReader(f));) {
			
			String linea="";
			String[] separados=null;
			linea=lectura.readLine();
			while ((linea=lectura.readLine()) != null) {
				separados=linea.split(",");
				Articulos articulo=new Articulos();
				articulo.setNombre(separados[1]);
				articulo.setCodigo(separados[2]);
				articulo.setGrupo(Integer.parseInt(separados[3]));
				articulo.setPrecio(Double.parseDouble(separados[4]));
				articulo.setStock(Integer.parseInt(separados[5]));
				articulos_importados.add(articulo);
			}
			
			
		}
		catch (IOException e) {
			
		}
		
		return articulos_importados;
	}

	private FileChooser creaFileChooser(String titulo) {
        FileChooser fc = new FileChooser();
        fc.setTitle(titulo);
        fc.setInitialDirectory(new File(System.getProperty("user.home")));
        FileChooser.ExtensionFilter filtro1 = new FileChooser.ExtensionFilter("Archivos de texto comma-separated values *.csv", "*.csv");
        fc.getExtensionFilters().add(filtro1);
        return fc;
    }
}
