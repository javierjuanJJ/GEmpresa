package com.mycompany.Controlador;

import com.mycompany.dao.ArticulosDAO;
import com.mycompany.dao.ClientesDAO;
import com.mycompany.dao.VendedoresDAO;
import com.mycompany.dao.FacturasDAO;
import java.time.LocalDate;
import java.util.List;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.mycompany.Modelo.Articulos;
import com.mycompany.Modelo.Clientes;

import java.io.*;
import com.mycompany.Modelo.Facturas;
import com.mycompany.Modelo.Lineas_Facturas;
import com.mycompany.Modelo.Vendedores;
import com.mycompany.Modelo.Clientes;
import com.mycompany.Modelo.Articulos;
import com.mycompany.mavenproject1.Main;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ControladorFormularioFacturas {

    private Stage escenario;
    private static FacturasDAO controladorfacturas;
    private static List<Facturas> Lista_de_Facturas;
    private static List<String> Lista_de_formas_de_pago;
    private static List<Articulos> Lista_de_Articulos;
    private static List<Vendedores> Lista_de_Vendedores;
    private static List<Clientes> Lista_de_Clientes;
    private static Clientes cliente_actual;
    private static ClientesDAO controladorclientes;
    private static ArticulosDAO controladorArticulos;
    private static VendedoresDAO controladorVendedores;
    private static int contador_modificador = 0;
    private static int contador_modificador_lineas = 0;
    private static boolean es_admin;
    private static Facturas Factura_a_crear;

    @FXML
    private ComboBox<Facturas> ComboBox_Facturas;

    @FXML
    private ComboBox<String> formas_de_pago;

    @FXML
    private ComboBox<Articulos> cambiar_articulo;

    @FXML
    private ComboBox<Vendedores> cambiar_vendedor;

    @FXML
    private ComboBox<Clientes> cambiar_cliente;

    @FXML
    private ComboBox<Clientes> clientes_buscar;

    @FXML
    private TableView facturas;

    @FXML
    private DatePicker Fecha_factura2;

    @FXML
    private Label cliente;

    @FXML
    private Label buscar_facturas_texto;

    @FXML
    private TextField cambiar_cantidad;

    @FXML
    private Label vendedor;

    @FXML
    private Label numero_factura;

    @FXML
    private Label fecha_factura;

    @FXML
    private Label subtotal;

    @FXML
    private Label iva;

    @FXML
    private Label total_de_la_factura;

    @FXML
    private Label Cliente;

    @FXML
    private Label Vendedor;

    @FXML
    private Label Articulo;

    @FXML
    private Label Fecha_factura;

    @FXML
    private Label Cantidad;

    @FXML
    private Button boton_actualizar;
    @FXML
    private Button anyadir_linea;
    @FXML
    private Button insertar_linea;
    @FXML
    private Button eliminar_linea;
    @FXML
    private Button buscar_facturas;
    @FXML
    private Label forma_de_pago_texto;
    @FXML
    private Button eliminar_linea_boton;
    @FXML
    private Button nueva_factura;
    @FXML
    private Button atras_del_todo_facturas;
    @FXML
    private Button atras_facturas;
    @FXML
    private Button delante_facturas;
    @FXML
    private Button delante_del_todo_facturas;
    @FXML
    private Button atras_del_todo_lineas_facturas;
    @FXML
    private Button atras_lineas_facturas;
    @FXML
    private Button delante_lineas_facturas;
    @FXML
    private Button delante_del_todo_lineas_facturas;
    @FXML
    private Button boton_consultar;
    @FXML
    private Button buscar_todas_las_facturas;
    @FXML
    private Label label_informacion;
    @FXML
    private Label total_todas_las_facturas;

    @FXML
    public void initialize() {

        try {
            Clientes cliente_ver_facturas = ControladorFormularioClientes.cliente_ver_facturas;
            cliente_actual = cliente_ver_facturas != null ? cliente_ver_facturas : ControladorPrincipio.cliente_actual;
            controladorfacturas = new FacturasDAO();
            controladorclientes = new ClientesDAO();
            controladorArticulos = new ArticulosDAO();
            controladorVendedores = new VendedoresDAO();

            es_admin = (cliente_actual.getId() == 1);

            Cliente.setDisable(!es_admin);
            cambiar_cliente.setDisable(!es_admin);
            Vendedor.setDisable(!es_admin);
            cambiar_vendedor.setDisable(!es_admin);
            Articulo.setDisable(!es_admin);
            Fecha_factura.setDisable(!es_admin);
            Fecha_factura2.setDisable(!es_admin);
            Cantidad.setDisable(!es_admin);
            cambiar_cantidad.setDisable(!es_admin);
            cambiar_articulo.setDisable(!es_admin);
            boton_actualizar.setVisible(es_admin);
            anyadir_linea.setVisible(es_admin);
            insertar_linea.setVisible(es_admin);
            eliminar_linea.setVisible(es_admin);
            formas_de_pago.setDisable(!es_admin);
            forma_de_pago_texto.setDisable(es_admin);
            Factura_a_crear = new Facturas();
            buscar_facturas_texto.setVisible(es_admin);
            clientes_buscar.setVisible(es_admin);
            buscar_facturas.setVisible(es_admin);

            Articulo.setVisible(es_admin);
            cambiar_articulo.setVisible(es_admin);
            buscar_todas_las_facturas.setVisible(es_admin);
            Cantidad.setVisible(es_admin);
            cambiar_cantidad.setVisible(es_admin);
            boton_actualizar.setVisible(es_admin);
            anyadir_linea.setVisible(es_admin);
            insertar_linea.setVisible(es_admin);
            eliminar_linea_boton.setVisible(es_admin);
            nueva_factura.setVisible(es_admin);
            eliminar_linea.setVisible(es_admin);
            atras_del_todo_facturas.setVisible(es_admin);
            atras_facturas.setVisible(es_admin);
            delante_facturas.setVisible(es_admin);
            delante_del_todo_facturas.setVisible(es_admin);
            atras_del_todo_lineas_facturas.setVisible(es_admin);
            atras_lineas_facturas.setVisible(es_admin);
            delante_lineas_facturas.setVisible(es_admin);
            delante_del_todo_lineas_facturas.setVisible(es_admin);
            buscar_facturas.setVisible(es_admin);
            ComboBox_Facturas.setVisible(es_admin);

            Cargar_facturas();

            Lista_de_Articulos = controladorArticulos.findAll();
            Lista_de_Vendedores = controladorVendedores.findAll();
            Lista_de_Clientes = controladorclientes.findAll();
            Lista_de_formas_de_pago = controladorfacturas.findAll_formas_pago();

            try {

                Lista_de_Facturas.clear();
                Lista_de_Facturas.addAll((es_admin) ? controladorfacturas.findAll() : controladorfacturas.findAll2(ControladorPrincipio.cliente_actual));
                contador_modificador = 0;
                poner_datos();
                actualizar();

            } catch (Exception e) {
                poner_datos_en_la_tabla(new Facturas());
                (new Main()).mensajeExcepcion(e, e.getMessage());
            }
            calcular_total();
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
            Platform.exit();
        }

    }

    public void calcular_total() {
        label_informacion.setText("Factura " + contador_modificador + " de " + Lista_de_Facturas.size() + " facturas");
        double total = 0.0;
        for (int contador = 0; contador < Lista_de_Facturas.size(); contador++) {
            Lista_de_Facturas.get(contador).calcular_total_factura();
            total += Lista_de_Facturas.get(contador).getTotal();
        }
        total_todas_las_facturas.setText("Total de todas las facturas " + total + " euros.");
    }

    private void Cargar_facturas() {
        try {
            Lista_de_Facturas = (es_admin) ? controladorfacturas.findAll() : controladorfacturas.findAll2(cliente_actual);
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
            Platform.exit();
        }
    }

    public void Cambiar_Pantalla(ActionEvent action) throws IOException {
        String id_boton = "";
        id_boton = ((Button) action.getSource()).getId();
        Main main = new Main();
        ControladorFormularioClientes.cliente_ver_facturas = null;
        main.Cambiar_Pantalla(id_boton);
    }

    public void actualizar() {
        try {
            ComboBox_Facturas.getItems().clear();
            ComboBox_Facturas.getItems().addAll(Lista_de_Facturas);
        } catch (Exception e) {

        }

    }

    public void actualizar_cliente() {
        try {
            cambiar_cliente.getItems().clear();
            cambiar_cliente.getItems().addAll(Lista_de_Clientes);
            clientes_buscar.getItems().clear();
            clientes_buscar.getItems().addAll(Lista_de_Clientes);
        } catch (Exception e) {

        }

    }

    public void actualizar_formas_de_pago() {
        try {
            formas_de_pago.getItems().clear();
            formas_de_pago.getItems().addAll(Lista_de_formas_de_pago);
        } catch (Exception e) {

        }

    }

    public void actualizar_vendedores() {
        try {
            cambiar_vendedor.getItems().clear();
            cambiar_vendedor.getItems().addAll(Lista_de_Vendedores);
        } catch (Exception e) {

        }

    }

    public void actualizar_articulos() {
        try {
            cambiar_articulo.getItems().clear();
            cambiar_articulo.getItems().addAll(Lista_de_Articulos);
        } catch (Exception e) {

        }

    }

    public void poner_datos_en_la_tabla(Facturas factura) {
        try {
            facturas.getItems().clear();
            facturas.getColumns().clear();
            TableColumn<String, Lineas_Facturas> linea = new TableColumn<>("Linea de factura");
            linea.setCellValueFactory(new PropertyValueFactory<>("linea"));
            double precio_total = 0.0;

            TableColumn<Articulos, Lineas_Facturas> articulo = new TableColumn<>("Articulo");
            articulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

            TableColumn<String, Lineas_Facturas> cantidad = new TableColumn<>("Cantidad de productos");
            cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

            TableColumn<String, Lineas_Facturas> importe = new TableColumn<>("Precio del producto");
            importe.setCellValueFactory(new PropertyValueFactory<>("importe"));

            TableColumn<String, Lineas_Facturas> total_producto = new TableColumn<>("Total");
            total_producto.setCellValueFactory(new PropertyValueFactory<>("total_importe"));
            facturas.getColumns().addAll(linea, articulo, cantidad, importe, total_producto);

            for (int contador = 0; contador < factura.getLineas_de_la_factura().size(); contador++) {
                factura.getLineas_de_la_factura().get(contador).set_total_Importe();
                facturas.getItems().add(factura.getLineas_de_la_factura().get(contador));
                precio_total += factura.getLineas_de_la_factura().get(contador).getTotal_importe();
            }
            facturas.getSelectionModel().clearAndSelect(contador_modificador_lineas);
            cliente.setText(factura.getCliente().getNombre() + System.lineSeparator()
                    + factura.getCliente().getDireccion() + System.lineSeparator());
            vendedor.setText(factura.getVendedor().getNombre() + System.lineSeparator());
            numero_factura.setText(factura.getId() + "");
            fecha_factura.setText(factura.getFecha().toString());
            subtotal.setText(String.valueOf(precio_total));
            double precio_iva = 0.0;
            precio_iva = (precio_total * 21) / 100;
            iva.setText(String.valueOf(precio_iva));
            total_de_la_factura.setText(String.valueOf(precio_iva + precio_total));
        } catch (Exception e) {

        }

    }

    public void poner_datos_en_la_tabla() {
        try {
            facturas.getItems().clear();
            facturas.getColumns().clear();
            TableColumn<String, Facturas> linea = new TableColumn<>("Factura");
            linea.setCellValueFactory(new PropertyValueFactory<>("id"));

            TableColumn<Articulos, Facturas> articulo = new TableColumn<>("Fecha");
            articulo.setCellValueFactory(new PropertyValueFactory<>("fecha"));

            TableColumn<Clientes, Facturas> cantidad = new TableColumn<>("Cliente");
            cantidad.setCellValueFactory(new PropertyValueFactory<>("cliente"));

            TableColumn<Vendedores, Facturas> importe = new TableColumn<>("Vendedor");
            importe.setCellValueFactory(new PropertyValueFactory<>("vendedor"));

            TableColumn<String, Facturas> forma_de_pago = new TableColumn<>("Forma de pago");
            forma_de_pago.setCellValueFactory(new PropertyValueFactory<>("forma_de_pago"));
            
            TableColumn<String, Facturas> total = new TableColumn<>("Total");
            total.setCellValueFactory(new PropertyValueFactory<>("total"));
            facturas.getColumns().addAll(linea, articulo, cantidad, importe, forma_de_pago,total);
            facturas.getSelectionModel().clearAndSelect(contador_modificador_lineas);

            for (Facturas Factura : Lista_de_Facturas) {
                Factura.calcular_total_factura();
                facturas.getItems().add(Factura);
            }
            facturas.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        } catch (Exception e) {

        }

    }

    public void poner_informacion() {

        try {
            facturas.getColumns().clear();
            facturas.getItems().clear();
            contador_modificador = ComboBox_Facturas.getSelectionModel().getSelectedIndex();
            Facturas factura = new Facturas(Lista_de_Facturas.get(contador_modificador));
            poner_datos_en_la_tabla(factura);

        } catch (Exception e) {

        }

    }

    public void Exportar_factura_a_PDF() {
        Facturas factura = new Facturas(Lista_de_Facturas.get(contador_modificador));
        FileChooser fc = creaFileChooser("Guardar pdf");
        File file = (fc.showSaveDialog(escenario));

        if (file != null) {
            // Se crea el documento
            Document documento = new Document();

            // Se crea el OutputStream para el fichero donde queremos dejar el pdf.
            FileOutputStream ficheroPdf;
            try {
                ficheroPdf = new FileOutputStream(file);
                // Se asocia el documento al OutputStream y se indica que el espaciado entre
                // lineas sera de 20. Esta llamada debe hacerse antes de abrir el documento
                PdfWriter.getInstance(documento, ficheroPdf).setInitialLeading(20);

                // Se abre el documento.
                documento.open();

                documento.add(new Paragraph("Factura", FontFactory.getFont("arial", // fuente
                        22, // tamaño
                        Font.ITALIC, // estilo
                        BaseColor.CYAN))); // color

                documento.add(new Paragraph("Facturar a         Enviar a           Nº de factura          "
                        + factura.getId() + System.lineSeparator()));
                documento.add(new Paragraph(
                        factura.getCliente().getNombre() + "       " + "         " + factura.getVendedor().getNombre()
                        + "         " + "         " + factura.getFecha().toString() + System.lineSeparator()));
                documento.add(new Paragraph(
                        factura.getCliente().getDireccion() + "         " + "         " + System.lineSeparator()));

                documento.add(new Paragraph(System.lineSeparator()));
                documento.add(new Paragraph(System.lineSeparator()));
                documento.add(new Paragraph(System.lineSeparator()));
                documento.add(new Paragraph(System.lineSeparator()));
                documento.add(new Paragraph(System.lineSeparator()));
                documento.add(new Paragraph(System.lineSeparator()));
                PdfPTable tabla = new PdfPTable(4);
                int columnas = (factura.getLineas_de_la_factura().size());

                tabla.addCell("Cantidad");
                tabla.addCell("Descripcion");
                tabla.addCell("Precio unitario");
                tabla.addCell("Importe");
                double precio_total = 0.0;

                for (int i = 0; i < columnas; i++) {
                    tabla.addCell(factura.getLineas_de_la_factura().get(i).getCantidad() + "");
                    tabla.addCell(factura.getLineas_de_la_factura().get(i).getArticulo().toString());
                    tabla.addCell(factura.getLineas_de_la_factura().get(i).getImporte() + "");
                    tabla.addCell(factura.getLineas_de_la_factura().get(i).getTotal_importe() + "");
                    precio_total += factura.getLineas_de_la_factura().get(i).getTotal_importe();
                }

                tabla.addCell("");
                tabla.addCell("");
                tabla.addCell("Subtotal");
                tabla.addCell(precio_total + "");

                tabla.addCell("");
                tabla.addCell("");
                tabla.addCell("IVA: 21%");
                tabla.addCell((precio_total * 21 / 100) + "");

                tabla.addCell("");
                tabla.addCell("");
                tabla.addCell("Total: ");
                tabla.addCell((precio_total + (precio_total * 21 / 100)) + "");

                documento.add(tabla);

            } catch (FileNotFoundException e) {

            } catch (DocumentException e) {

            }

            documento.close();
        }
    }

    private FileChooser creaFileChooser(String titulo) {
        FileChooser fc = new FileChooser();
        try {

            fc.setTitle(titulo);
            fc.setInitialDirectory(new File(System.getProperty("user.home")));
            FileChooser.ExtensionFilter filtro1 = new FileChooser.ExtensionFilter("Archivos pdf *.pdf", "*.pdf");
            fc.getExtensionFilters().add(filtro1);
        } catch (Exception e) {

        }

        return fc;
    }

    public void poner_datos() {
        try {

            if (es_admin) {
                Facturas factura = new Facturas(Lista_de_Facturas.get(contador_modificador));
                contador_modificador_lineas = facturas.getSelectionModel().getSelectedIndex();
                factura(factura);
                poner_datos_en_la_tabla(factura);
            } else {
                contador_modificador = facturas.getSelectionModel().getSelectedIndex();
                poner_datos_en_la_tabla();
            }

        } catch (Exception e) {

        }

    }

    public void poner_datos_controles(ActionEvent action) {
        switch (((Button) action.getSource()).getId()) {
            case "Primero":
                contador_modificador = 0;
                break;
            case "Siguiente":
                contador_modificador++;
                break;
            case "Ultimo":
                contador_modificador = Lista_de_Facturas.size() - 1;
                break;
            case "Anterior":
                contador_modificador--;
                break;
            case "Primero_lineas":
                contador_modificador_lineas = 0;
                break;
            case "Siguiente_lineas":
                contador_modificador_lineas++;
                break;
            case "Ultimo_lineas":
                contador_modificador_lineas = Lista_de_Facturas.get(contador_modificador).getLineas_de_la_factura().size()
                        - 1;
                break;
            case "Anterior_lineas":
                contador_modificador_lineas--;
                break;
        }
        Facturas factura = null;
        try {
            factura = new Facturas(Lista_de_Facturas.get(contador_modificador));
        } catch (Exception e) {
            contador_modificador = 0;
            factura = new Facturas();
        }

        factura(factura);
        poner_datos_en_la_tabla(factura);
        calcular_total();
    }

    private void factura(Facturas factura) {

        if (contador_modificador_lineas == -1) {
            contador_modificador_lineas = 0;
        }
        Lineas_Facturas l = null;
        try {
            l = new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador_modificador_lineas));

            cambiar_cantidad.setText(l.getCantidad() + "");

            cambiar_cliente.getItems().clear();
            cambiar_cliente.getItems().add(factura.getCliente());
            cambiar_cliente.getSelectionModel().select(0);

            cambiar_articulo.getItems().clear();
            cambiar_articulo.getItems().add(l.getArticulo());
            cambiar_articulo.getSelectionModel().select(l.getArticulo());

            cambiar_vendedor.getItems().clear();
            cambiar_vendedor.getItems().add(factura.getVendedor());
            cambiar_vendedor.getSelectionModel().select(0);

            formas_de_pago.getSelectionModel().select(factura.getForma_de_pago());

            java.sql.Date date2 = (java.sql.Date) factura.getFecha();

            LocalDate date = date2.toLocalDate();

            Fecha_factura2.setValue(date);
        } catch (Exception e) {
            contador_modificador_lineas = 0;
            l = new Lineas_Facturas(new Lineas_Facturas());
        }
    }

    public void insertar_linea() {
        int factura_numero = contador_modificador;
        try {
            int id_factura = Integer.parseInt(this.numero_factura.getText());
            if (id_factura == 0) {
                facturas.getColumns().clear();
                Factura_a_crear = new Facturas(Factura_a_crear);

                Factura_a_crear.setCliente(new Clientes(cambiar_cliente.getSelectionModel().getSelectedItem()));
                Factura_a_crear.setVendedor(new Vendedores(cambiar_vendedor.getSelectionModel().getSelectedItem()));

                LocalDate dateToConvert = Fecha_factura2.getValue();
                java.sql.Date fecha_sql = java.sql.Date.valueOf(dateToConvert);
                Factura_a_crear.setForma_de_pago(formas_de_pago.getSelectionModel().getSelectedItem());
                Factura_a_crear.setFecha(fecha_sql);

                Lineas_Facturas linea_factura = new Lineas_Facturas();
                linea_factura.setArticulo(new Articulos(cambiar_articulo.getSelectionModel().getSelectedItem()));
                linea_factura.setCantidad(Integer.parseInt(cambiar_cantidad.getText()));
                linea_factura.setImporte(linea_factura.getArticulo().getPrecio());
                linea_factura.set_total_Importe();
                linea_factura.setLinea(Factura_a_crear.getLineas_de_la_factura().size() + 1);
                Factura_a_crear.getLineas_de_la_factura().add(linea_factura);

                TableColumn<String, Lineas_Facturas> linea = new TableColumn<>("Linea de factura");
                linea.setCellValueFactory(new PropertyValueFactory<>("linea"));

                TableColumn<Articulos, Lineas_Facturas> articulo = new TableColumn<>("Articulo");
                articulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

                TableColumn<String, Lineas_Facturas> cantidad = new TableColumn<>("Cantidad de productos");
                cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

                TableColumn<String, Lineas_Facturas> importe = new TableColumn<>("Precio del producto");
                importe.setCellValueFactory(new PropertyValueFactory<>("importe"));

                TableColumn<String, Lineas_Facturas> total_producto = new TableColumn<>("Total");
                total_producto.setCellValueFactory(new PropertyValueFactory<>("total_importe"));

                facturas.getColumns().addAll(linea, articulo, cantidad, importe, total_producto);
                facturas.getItems().add(
                        Factura_a_crear.getLineas_de_la_factura().get(Factura_a_crear.getLineas_de_la_factura().size() - 1));

                Factura_a_crear.calcular_total_factura();
                cliente.setText(Factura_a_crear.getCliente().getNombre() + System.lineSeparator()
                        + Factura_a_crear.getCliente().getDireccion() + System.lineSeparator());
                vendedor.setText(Factura_a_crear.getVendedor().getNombre() + System.lineSeparator());
                numero_factura.setText(Factura_a_crear.getId() + "");
                fecha_factura.setText(Factura_a_crear.getFecha().toString());
                subtotal.setText(String.valueOf(Factura_a_crear.getTotal()));
                iva.setText(String.valueOf((Factura_a_crear.getTotal() * 21) / 100));
                total_de_la_factura
                        .setText(String.valueOf(((Factura_a_crear.getTotal() * 21) / 100) + Factura_a_crear.getTotal()));
                formas_de_pago.getSelectionModel().select(Factura_a_crear.getForma_de_pago());
                Cargar_facturas();
            } else {
                facturas.getColumns().clear();
                Factura_a_crear = new Facturas(Factura_a_crear);

                Factura_a_crear.setCliente(new Clientes(cambiar_cliente.getSelectionModel().getSelectedItem()));
                Factura_a_crear.setVendedor(new Vendedores(cambiar_vendedor.getSelectionModel().getSelectedItem()));

                LocalDate dateToConvert = Fecha_factura2.getValue();
                java.sql.Date fecha_sql = java.sql.Date.valueOf(dateToConvert);
                Factura_a_crear.setLineas_de_la_factura(Lista_de_Facturas.get(contador_modificador).getLineas_de_la_factura());
                Factura_a_crear.setForma_de_pago(formas_de_pago.getSelectionModel().getSelectedItem());
                Factura_a_crear.setFecha(fecha_sql);

                Lineas_Facturas linea_factura = new Lineas_Facturas();
                linea_factura.setArticulo(new Articulos(cambiar_articulo.getSelectionModel().getSelectedItem()));
                linea_factura.setCantidad(Integer.parseInt(cambiar_cantidad.getText()));
                linea_factura.setImporte(linea_factura.getArticulo().getPrecio());
                linea_factura.set_total_Importe();
                linea_factura.setLinea(Factura_a_crear.getLineas_de_la_factura().size() + 1);
                Factura_a_crear.getLineas_de_la_factura().add(linea_factura);
                controladorfacturas.insert(linea_factura, id_factura);
                controladorfacturas.update(Factura_a_crear);
                TableColumn<String, Lineas_Facturas> linea = new TableColumn<>("Linea de factura");
                linea.setCellValueFactory(new PropertyValueFactory<>("linea"));

                TableColumn<Articulos, Lineas_Facturas> articulo = new TableColumn<>("Articulo");
                articulo.setCellValueFactory(new PropertyValueFactory<>("articulo"));

                TableColumn<String, Lineas_Facturas> cantidad = new TableColumn<>("Cantidad de productos");
                cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));

                TableColumn<String, Lineas_Facturas> importe = new TableColumn<>("Precio del producto");
                importe.setCellValueFactory(new PropertyValueFactory<>("importe"));

                TableColumn<String, Lineas_Facturas> total_producto = new TableColumn<>("Total");
                total_producto.setCellValueFactory(new PropertyValueFactory<>("total_importe"));

                facturas.getColumns().addAll(linea, articulo, cantidad, importe, total_producto);
                facturas.getItems().add(
                        Factura_a_crear.getLineas_de_la_factura().get(Factura_a_crear.getLineas_de_la_factura().size() - 1));

                Factura_a_crear.calcular_total_factura();
                cliente.setText(Factura_a_crear.getCliente().getNombre() + System.lineSeparator()
                        + Factura_a_crear.getCliente().getDireccion() + System.lineSeparator());
                vendedor.setText(Factura_a_crear.getVendedor().getNombre() + System.lineSeparator());
                numero_factura.setText(Factura_a_crear.getId() + "");
                fecha_factura.setText(Factura_a_crear.getFecha().toString());
                subtotal.setText(String.valueOf(Factura_a_crear.getTotal()));
                iva.setText(String.valueOf((Factura_a_crear.getTotal() * 21) / 100));
                total_de_la_factura
                        .setText(String.valueOf(((Factura_a_crear.getTotal() * 21) / 100) + Factura_a_crear.getTotal()));
                formas_de_pago.getSelectionModel().select(Factura_a_crear.getForma_de_pago());
                buscar_facturas_por_cliente();
                contador_modificador = factura_numero;
                poner_datos();
                actualizar();
            }

        } catch (NumberFormatException e) {
            Lineas_Facturas linea_factura = new Lineas_Facturas();
            linea_factura.setArticulo(new Articulos(cambiar_articulo.getSelectionModel().getSelectedItem()));
            linea_factura.setCantidad(Integer.parseInt(cambiar_cantidad.getText()));
            linea_factura.setImporte(linea_factura.getArticulo().getPrecio());
            linea_factura.set_total_Importe();
            linea_factura.setLinea(Factura_a_crear.getLineas_de_la_factura().size() + 1);
            facturas.getItems().add(linea_factura);
        } catch (Exception ex) {

        }

    }

    public void insertar_linea_a_factura_existente() {

        try {
            factura(new Facturas());
            poner_datos_en_la_tabla(new Facturas());
        } catch (Exception e) {

        }

    }

    public void eliminar_linea_a_factura_existente() {
        int factura_numero = contador_modificador;
        try {
            Facturas factura = new Facturas(Lista_de_Facturas.get(contador_modificador));
            Lineas_Facturas linea_factura = new Lineas_Facturas(
                    factura.getLineas_de_la_factura().get(facturas.getSelectionModel().getSelectedIndex()));

            String query_sql = "DELETE FROM v_empresa_ad_p1.lineas_factura WHERE linea=" + linea_factura.getLinea()
                    + " AND factura=" + factura.getId() + ";";

            controladorfacturas.FindBySQL(query_sql);

            buscar_facturas_por_cliente();

            contador_modificador = factura_numero;
            poner_datos();
            actualizar();
            poner_datos_en_la_tabla(Lista_de_Facturas.get(contador_modificador));
            calcular_total();
        } catch (Exception e) {

        }

    }

    public void insertar_factura() {

        try {

            if (controladorfacturas.insert(Factura_a_crear)) {
                (new Main()).mensajeConfirmacion("Innsercion de facturas completada", "", "");
            }

            for (Lineas_Facturas linea_factura : Factura_a_crear.getLineas_de_la_factura()) {
                actualizar_stock(linea_factura.getArticulo(), 0, linea_factura.getCantidad());
            }

            facturas.getColumns().clear();
            Factura_a_crear = new Facturas();
            facturas.getItems().clear();
            Lista_de_Facturas = (es_admin) ? controladorfacturas.findAll()
                    : controladorfacturas.findAll2(cliente_actual);
            Cargar_facturas();
            contador_modificador = Lista_de_Facturas.size() - 1;
            poner_datos();
            actualizar();

            calcular_total();
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }

    public void actualizar_linea() {
        int cantidad = 0;
        int cantidad_antes = 0;

        Facturas factura = new Facturas(Lista_de_Facturas.get(contador_modificador));

        cantidad = Integer.parseInt(cambiar_cantidad.getText());

        Lineas_Facturas l2 = factura.getLineas_de_la_factura().get(contador_modificador_lineas);
        l2.setArticulo(cambiar_articulo.getSelectionModel().getSelectedItem());
        cantidad_antes = l2.getCantidad();
        l2.setCantidad(cantidad);
        l2.set_total_Importe();
        double precio_total = 0.0;
        facturas.getSelectionModel().select(contador_modificador);

        Lineas_Facturas l = new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador_modificador_lineas));

        for (int contador = 0; contador < factura.getLineas_de_la_factura().size(); contador++) {
            l = new Lineas_Facturas(factura.getLineas_de_la_factura().get(contador));

            if (!l.equals(l2)) {
                l.set_total_Importe();
                precio_total += l.getTotal_importe();
            }
        }
        precio_total += l2.getTotal_importe();

        LocalDate dateToConvert = Fecha_factura2.getValue();
        java.sql.Date fecha_sql = java.sql.Date.valueOf(dateToConvert);
        java.util.Date fecha_util = (java.util.Date) fecha_sql;

        factura.setFecha(fecha_util);

        fecha_factura.setText(fecha_util.toString());

        factura.setCliente(new Clientes(cambiar_cliente.getSelectionModel().getSelectedItem()));
        factura.setVendedor(new Vendedores(cambiar_vendedor.getSelectionModel().getSelectedItem()));

        cliente.setText(factura.getCliente().toString());
        vendedor.setText(factura.getVendedor().toString());

        subtotal.setText(String.valueOf(precio_total));
        double precio_iva = (precio_total * 21) / 100;
        iva.setText(String.valueOf(precio_iva));
        total_de_la_factura.setText(String.valueOf(precio_iva + precio_total));

        actualizar_stock(cambiar_articulo.getSelectionModel().getSelectedItem(), cantidad_antes, cantidad);

        try {

            if (controladorfacturas.update(factura)) {
                (new Main()).mensajeConfirmacion("Actualizacion de facturas completada", "", "");
                Cargar_facturas();
            }

            Cargar_facturas();
            poner_datos_en_la_tabla(Lista_de_Facturas.get(contador_modificador));
            calcular_total();
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }

    public void actualizar_stock(Articulos articulo, int cantidad_antes, int cantidad) {
        try {

            char mas = '+';
            char menos = '-';
            String query = "";

            int id = articulo.getId();
            query = "UPDATE articulos SET stock=stock" + mas + cantidad_antes + " WHERE id=" + id + ";";
            controladorfacturas.FindBySQL(query);
            query = "UPDATE articulos SET stock=stock" + menos + cantidad + " WHERE id=" + id + ";";
            controladorfacturas.FindBySQL(query);
            Cargar_facturas();
            poner_datos_en_la_tabla(Lista_de_Facturas.get(contador_modificador));
        } catch (Exception e1) {

        }
    }

    public void eliminar_factura() {
        try {

            if (controladorfacturas.delete(ComboBox_Facturas.getSelectionModel().getSelectedItem().getId())) {
                (new Main()).mensajeConfirmacion("Eliminacion de facturas completada", "", "");
                Cargar_facturas();
                poner_datos_en_la_tabla(Lista_de_Facturas.get(contador_modificador));
                calcular_total();
            }

        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }

    public void buscar_todas_facturas_por_cliente() {
        try {
            List<Facturas> facturas_lista = controladorfacturas.findAll();
            if (!facturas_lista.isEmpty()) {
                Lista_de_Facturas.clear();
                Lista_de_Facturas.addAll(facturas_lista);
                contador_modificador = 0;
                poner_datos();
                actualizar();
                calcular_total();
            } else {
                throw new Exception("Error. El usuario " + clientes_buscar.getSelectionModel().getSelectedItem().getNombre() + " no tiene facturas. Inserte una factura e intentelo de nuevo.");
            }
            label_informacion.setText("Factura " + contador_modificador + " de " + Lista_de_Facturas.size() + " facturas");
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }

    public void buscar_facturas_por_cliente() {
        try {
            List<Facturas> facturas_lista = controladorfacturas.findAll2(clientes_buscar.getSelectionModel().getSelectedItem());
            if (!facturas_lista.isEmpty()) {
                Lista_de_Facturas.clear();
                Lista_de_Facturas.addAll(facturas_lista);
                contador_modificador = 0;
                poner_datos();
                actualizar();
                calcular_total();
            } else {
                throw new Exception("Error. El usuario " + clientes_buscar.getSelectionModel().getSelectedItem().getNombre() + " no tiene facturas. Inserte una factura e intentelo de nuevo.");
            }
            label_informacion.setText("Factura " + contador_modificador + " de " + Lista_de_Facturas.size() + " facturas");
        } catch (Exception e) {
            (new Main()).mensajeExcepcion(e, e.getMessage());
        }
    }
}
