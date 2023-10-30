/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadecontroldeinventario.hardware.perifericos;

import Modelo.DAO.PerifericoDAO;
import Modelo.POJO.Periferico;
import Utilidades.Utilidades;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author froyl
 */
public class PerifericosFXMLControlador implements Initializable {
    
    private String cargoUsuario;
    private ObservableList<Periferico> listaPerifericos; 

    @FXML
    private TableColumn tcMarca;
    @FXML
    private TableColumn tcModelo;
    @FXML
    private TableColumn tcNumeroSerie;
    @FXML
    private TableColumn tcEstado;
    @FXML
    private TextField tfBusqueda;
    @FXML
    private TableView<Periferico> tvPerifericos;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarTabla();
        inicializarBusquedaPerifericos();
    }    

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) tvPerifericos.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void eliminarPeriferico(ActionEvent event) {
    }

    @FXML
    private void consultarPeriferico(ActionEvent event) {
    }

    @FXML
    private void modificarPeriferico(ActionEvent event) {
        if(verificarSeleccion()){
            try {
                FXMLLoader loaderVentanaModificarPeriferico = new FXMLLoader(getClass().getResource("ModificarPerifericoFXML.fxml"));
                Parent ventanaModificarPeriferico = loaderVentanaModificarPeriferico.load();

                ModificarPerifericoFXMLControlador controlador = loaderVentanaModificarPeriferico.getController();
                controlador.inicializarVentana(PerifericoDAO.buscarPerifericoPorNumeroSerie(tvPerifericos.getSelectionModel().getSelectedItem().getNumeroSerie()));

                Scene escenarioModificarPeriferico = new Scene(ventanaModificarPeriferico);
                Stage stagePerifericos = new Stage();
                stagePerifericos.setScene(escenarioModificarPeriferico);
                stagePerifericos.initModality(Modality.APPLICATION_MODAL);
                stagePerifericos.showAndWait();
                cargarTabla();

            } catch (IOException | SQLException e) {
                Utilidades.mostrarAlertaSimple("Error", "Algo ocurrió mal: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }else{
            Utilidades.mostrarAlertaSimple("Periférico no seleccionado", "No se ha seleccionado el periférico a modificar.", Alert.AlertType.WARNING);
        }        
    }

    @FXML
    private void registrarPeriferico(ActionEvent event) {
        try {
            FXMLLoader loaderVentanaRegistrarPeriferico = new FXMLLoader(getClass().getResource("RegistrarPerifericoFXML.fxml"));
            Parent ventanaRegistrarPeriferico = loaderVentanaRegistrarPeriferico.load();
            
            Scene escenarioRegistrarPeriferico = new Scene(ventanaRegistrarPeriferico);
            Stage stagePerifericos = new Stage();
            stagePerifericos.setScene(escenarioRegistrarPeriferico);
            stagePerifericos.initModality(Modality.APPLICATION_MODAL);
            stagePerifericos.showAndWait();
            cargarTabla();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    private void configurarTabla(){
        tcMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        tcNumeroSerie.setCellValueFactory(new PropertyValueFactory("numeroSerie"));
        tcEstado.setCellValueFactory(new PropertyValueFactory("estado"));
    }
    
    private void cargarTabla(){
        try{
            listaPerifericos = FXCollections.observableArrayList();
            ArrayList<Periferico> perifericosBD = PerifericoDAO.recuperarTodoPeriferico();            

            listaPerifericos.addAll(perifericosBD);
            tvPerifericos.setItems(listaPerifericos);

        }catch(SQLException e){
            Utilidades.mostrarAlertaSimple("Error", "Algo ocurrió mal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void inicializarBusquedaPerifericos(){
        if(listaPerifericos.size() > 0){
            FilteredList<Periferico> filtro = new FilteredList<>(listaPerifericos, p -> true);
            
            tfBusqueda.textProperty().addListener(new ChangeListener<String>(){
                @Override
                public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                    filtro.setPredicate(busqueda -> {
                        if(newValue == null || newValue.isEmpty()){
                            return true;
                        }
                        
                        String filtroMinusculas = newValue.toLowerCase();
                        if(busqueda.getNumeroSerie().toLowerCase().contains(filtroMinusculas)){
                            return true;
                        }
                        
                        if(busqueda.getEstado().toLowerCase().contains(filtroMinusculas)){
                            return true;
                        }
                        
                        if(busqueda.getMarca().toLowerCase().contains(filtroMinusculas)){
                            return true;
                        }
                        
                        if(busqueda.getModelo().toLowerCase().contains(filtroMinusculas)){
                            return true;
                        }
                        
                        return false;
                    });
                }
                
            });
            
            SortedList<Periferico> sortedRefaccion = new SortedList<>(filtro);
            sortedRefaccion.comparatorProperty().bind(tvPerifericos.comparatorProperty());
            tvPerifericos.setItems(sortedRefaccion);
        }
    }
    
    private boolean verificarSeleccion(){
        return tvPerifericos.getSelectionModel().getSelectedItem() != null;
    }
    
    public void inicializarVentana(String cargoUsuario){
        this.cargoUsuario = cargoUsuario;
    }
}