/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package sistemadecontroldeinventario.hardware.equiposComputo;

import Modelo.DAO.HardwareDAO;
import Modelo.POJO.Hardware;
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
public class EquiposDeComputoFXMLControlador implements Initializable {

    private String cargoUsuario;
    private ObservableList<Hardware> listaHardware; 
    
    @FXML
    private TableView<Hardware> tvEquiposComputo;
    @FXML
    private TextField tfBusqueda;
    @FXML
    private TableColumn tcMarca;
    @FXML
    private TableColumn tcModelo;
    @FXML
    private TableColumn tcNumeroSerie;
    @FXML
    private TableColumn tcEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        configurarTabla();
        cargarTabla();
        inicializarBusquedaEquiposComputo();
    }    

    @FXML
    private void cerrarVentana(ActionEvent event) {
        Stage stage = (Stage) tfBusqueda.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void registrarEquipoComputo(ActionEvent event) {
        try {
            FXMLLoader loaderVentanaRegistrarEquipoDeComputo = new FXMLLoader(getClass().getResource("RegistrarEquipoComputoFXML.fxml"));
            Parent ventanaRegistrarEquipoDeComputo = loaderVentanaRegistrarEquipoDeComputo.load();
            
            Scene escenarioRegistrarEquipoDeComputo = new Scene(ventanaRegistrarEquipoDeComputo);
            Stage stageEquiposDeComputo = new Stage();
            stageEquiposDeComputo.setScene(escenarioRegistrarEquipoDeComputo);
            stageEquiposDeComputo.initModality(Modality.APPLICATION_MODAL);
            stageEquiposDeComputo.showAndWait();
            cargarTabla();
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void modificarEquipoComputo(ActionEvent event) {
        if(verificarSeleccion()){
            try {
                FXMLLoader loaderVentanaModificarEquipoDeComputo = new FXMLLoader(getClass().getResource("ModificarEquipoComputoFXML.fxml"));
                Parent ventanaModificarEquipoDeComputo = loaderVentanaModificarEquipoDeComputo.load();
                
                ModificarEquipoComputoFXMLControlador controlador = loaderVentanaModificarEquipoDeComputo.getController();
                controlador.inicializarVentana(HardwareDAO.buscarHardwarePorNumeroSerie(tvEquiposComputo.getSelectionModel().getSelectedItem().getNumeroSerie()));      

                Scene escenarioModificarEquipoDeComputo = new Scene(ventanaModificarEquipoDeComputo);
                Stage stageEquiposDeComputo = new Stage();
                stageEquiposDeComputo.setScene(escenarioModificarEquipoDeComputo);
                stageEquiposDeComputo.initModality(Modality.APPLICATION_MODAL);
                stageEquiposDeComputo.showAndWait();
                cargarTabla();            
            }catch (IOException | SQLException e) {
                Utilidades.mostrarAlertaSimple("Error", "Algo ocurrió mal: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }else{
            Utilidades.mostrarAlertaSimple("Equipo no seleccionado", "No se ha seleccionado el equipo de cómputo a modificar.", Alert.AlertType.WARNING);
        }     
    }

    @FXML
    private void consultarEquipoComputo(ActionEvent event) {
        if(verificarSeleccion()){
            try {
                FXMLLoader loaderVentanaConsultarEquipoDeComputo = new FXMLLoader(getClass().getResource("ConsultarEquipoComputoFXML.fxml"));
                Parent ventanaConsultarEquipoDeComputo = loaderVentanaConsultarEquipoDeComputo.load();

                ConsultarEquipoComputoFXMLControlador controlador = loaderVentanaConsultarEquipoDeComputo.getController();
                controlador.inicializarVentana(HardwareDAO.buscarHardwarePorNumeroSerie(tvEquiposComputo.getSelectionModel().getSelectedItem().getNumeroSerie()));

                Scene escenarioConsultarEquipoDeComputo = new Scene(ventanaConsultarEquipoDeComputo);
                Stage stageEquiposDeComputo = new Stage();
                stageEquiposDeComputo.setScene(escenarioConsultarEquipoDeComputo);
                stageEquiposDeComputo.initModality(Modality.APPLICATION_MODAL);
                stageEquiposDeComputo.showAndWait();            
            } catch(IOException | SQLException e) {
                Utilidades.mostrarAlertaSimple("Error", "Algo ocurrió mal: " + e.getMessage(), Alert.AlertType.ERROR);
                e.printStackTrace();
            }
        }else{
            Utilidades.mostrarAlertaSimple("Equipo no seleccionado", "No se ha seleccionado el equipo de cómputo a consultar.", Alert.AlertType.WARNING);
        }     
    }

    @FXML
    private void eliminarEquipoComputo(ActionEvent event) {
        if(verificarSeleccion()){
            if(Utilidades.mostrarDialogoConfirmacion("Eliminar equipo de cómputo", "¿Desea eliminar el equipo de cómputo seleccionado?")){
                try {
                    if(HardwareDAO.eliminarEquipoComputo(tvEquiposComputo.getSelectionModel().getSelectedItem().getIdHardware())){
                        Utilidades.mostrarAlertaSimple("Eliminación exitosa.", "Se eliminó exitosamente el equipo de cómputo.", Alert.AlertType.INFORMATION);                        
                    }
                } catch (SQLException e) {
                    Utilidades.mostrarAlertaSimple("Error", "Algo ocurrió mal: " + e.getMessage(), Alert.AlertType.ERROR);
                }
            }
        }else{
            Utilidades.mostrarAlertaSimple("Equipo no seleccionado", "No se ha seleccionado el equipo de cómputo a eliminar.", Alert.AlertType.WARNING);
        }        
        
        cargarTabla();
    }
    
    public void inicializarVentana(String cargoUsuario){
        this.cargoUsuario = cargoUsuario;
    }
    
    private void configurarTabla(){
        tcMarca.setCellValueFactory(new PropertyValueFactory("marca"));
        tcModelo.setCellValueFactory(new PropertyValueFactory("modelo"));
        tcNumeroSerie.setCellValueFactory(new PropertyValueFactory("numeroSerie"));
        tcEstado.setCellValueFactory(new PropertyValueFactory("estado"));
    }
    
    private void cargarTabla(){
        try{
            listaHardware = FXCollections.observableArrayList();
            ArrayList<Hardware> hardwareBD = HardwareDAO.recuperarTodoHardware();            

            listaHardware.addAll(hardwareBD);
            tvEquiposComputo.setItems(listaHardware);

        }catch(SQLException e){
            Utilidades.mostrarAlertaSimple("Error", "Algo ocurrió mal: " + e.getMessage(), Alert.AlertType.ERROR);
        }
    }
    
    private void inicializarBusquedaEquiposComputo(){
        if(listaHardware.size() > 0){
            FilteredList<Hardware> filtro = new FilteredList<>(listaHardware, p -> true);
            
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
            
            SortedList<Hardware> sortedRefaccion = new SortedList<>(filtro);
            sortedRefaccion.comparatorProperty().bind(tvEquiposComputo.comparatorProperty());
            tvEquiposComputo.setItems(sortedRefaccion);
        }
    }
    
    private boolean verificarSeleccion(){
        return tvEquiposComputo.getSelectionModel().getSelectedItem() != null;
    }
}
