package pe.edu.upeu.syspasteleriadulcesitofx.control;

import jakarta.persistence.Entity;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.syspasteleriadulcesitofx.modelo.Proveedor;
import pe.edu.upeu.syspasteleriadulcesitofx.repositorio.ProveedorRepository;

import java.util.List;
import java.util.Optional;
@Component
public class ProveedorController {
    @FXML
    public AnchorPane miContenedor;
    @FXML
    public Label lbnMsg;
    @FXML
    public TextField txtRuc;
    @FXML
    public TextField txtNombre;
    @FXML
    public TextField txtTelefono;
    @FXML
    public TextField txtDireccion;
    @FXML
    public TextField txtRazon;
    @FXML
    public TableView tableView;
    @FXML
    public TableColumn<Proveedor, String> colRuc;
    @FXML
    public TableColumn<Proveedor, String> colNombre;
    @FXML
    public TableColumn<Proveedor, String> colTelefono;
    @FXML
    public TableColumn<Proveedor, String> colDireccion;
    @FXML
    public TableColumn<Proveedor, String> colRazon;
    @Autowired
    ProveedorRepository proveedorRepository;

    private ObservableList<Proveedor> proveedorList;
    @FXML
    public void initialize() {
        configurarTabla();
        cargarDatos();
    }
    private void configurarTabla() {
        colRuc.setCellValueFactory(new PropertyValueFactory<>("dniRuc"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombresRaso"));
        colTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        colRazon.setCellValueFactory(new PropertyValueFactory<>("razonSosial"));
        proveedorList = FXCollections.observableArrayList();
        tableView.setItems(proveedorList);
    }

    private void cargarDatos() {
        List<Proveedor> proveedores = proveedorRepository.findAll();
        System.out.println(proveedores);
        proveedorList.setAll(proveedores);
    }
    @FXML
    public void validarFormulario(ActionEvent actionEvent) {
        String dniRuc = txtRuc.getText().trim();
        String nombresRaso = txtNombre.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String direccion = txtDireccion.getText().trim();
        String razonSosial = txtRazon.getText().trim();

        if (dniRuc.isEmpty() || nombresRaso.isEmpty() || telefono.isEmpty() || direccion.isEmpty() || razonSosial.isEmpty()) {
            mostrarAlerta("Error", "Todos los campos son obligatorios.", Alert.AlertType.ERROR);
            return;
        }

        try {
            Proveedor proveedor = new Proveedor();
            proveedor.setDniRuc(dniRuc);
            proveedor.setNombresRaso(nombresRaso);
            proveedor.setTelefono(telefono);
            proveedor.setDireccion(direccion);
            proveedor.setRazonSosial(razonSosial);

            // Guarda en la base de datos
            proveedorRepository.save(proveedor);
            cargarDatos();

            limpiarFormulario();
            mostrarAlerta("Éxito", "Proveedor guardado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al guardar el proveedor: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    @FXML
    public void cancelarFormulario(ActionEvent actionEvent) {
        limpiarFormulario();
    }

    @FXML
    public void editarFormulario(ActionEvent actionEvent) {
        Proveedor proveedorSeleccionado = (Proveedor) tableView.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un proveedor para editar.", Alert.AlertType.ERROR);
            return;
        }

        try {
            proveedorSeleccionado.setDniRuc(txtRuc.getText().trim());
            proveedorSeleccionado.setNombresRaso(txtNombre.getText().trim());
            proveedorSeleccionado.setTelefono(txtTelefono.getText().trim());
            proveedorSeleccionado.setDireccion(txtDireccion.getText().trim());
            proveedorSeleccionado.setRazonSosial(txtRazon.getText().trim());

            // Actualiza en la base de datos
            proveedorRepository.save(proveedorSeleccionado);

            cargarDatos();
            limpiarFormulario();
            mostrarAlerta("Éxito", "Proveedor editado correctamente.", Alert.AlertType.INFORMATION);

        } catch (Exception e) {
            mostrarAlerta("Error", "Ocurrió un error al editar el proveedor: " + e.getMessage(), Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }
    @FXML
    public void eliminarFormulaio(ActionEvent actionEvent) {
        Proveedor proveedorSeleccionado = (Proveedor) tableView.getSelectionModel().getSelectedItem();
        if (proveedorSeleccionado == null) {
            mostrarAlerta("Error", "Selecciona un proveedor para eliminar.", Alert.AlertType.ERROR);
            return;
        }

        proveedorRepository.delete(proveedorSeleccionado);
        cargarDatos();
        mostrarAlerta("Éxito", "Proveedor eliminado correctamente.", Alert.AlertType.INFORMATION);
    }

    private void limpiarFormulario() {
        txtRuc.clear();
        txtNombre.clear();
        txtTelefono.clear();
        txtDireccion.clear();
        txtRazon.clear();
    }

    private void mostrarAlerta(String titulo, String mensaje, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}