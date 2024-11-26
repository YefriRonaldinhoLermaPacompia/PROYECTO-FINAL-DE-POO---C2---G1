package pe.edu.upeu.syspasteleriadulcesitofx.control;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.syspasteleriadulcesitofx.componente.ColumnInfo;
import pe.edu.upeu.syspasteleriadulcesitofx.componente.TableViewHelper;
import pe.edu.upeu.syspasteleriadulcesitofx.componente.Toast;
import pe.edu.upeu.syspasteleriadulcesitofx.modelo.Proveedor;
import pe.edu.upeu.syspasteleriadulcesitofx.servicio.ProveedorService;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class ProveedorController {

    @FXML
    private TextField txtNombreProveedor, txtTelefono, txtdireccion, txtFiltroDato, txtRuc, txtCorreo;
    @FXML
    private TableView<Proveedor> tableViewProveedores;
    @FXML
    private Label lbnMsg;
    @FXML
    private AnchorPane contprov;
    private Stage stage;

    @Autowired
    private ProveedorService proveedorService;

    private Validator validator;
    private ObservableList<Proveedor> listarProveedor;
    private Proveedor formulario;
    private Long idProveedorCE = 0L;

    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) contprov.getScene().getWindow();
            if (stage != null) {
                System.out.println("El título del stage es: " + stage.getTitle());
            } else {
                System.out.println("Stage aún no disponible.");
            }
        }));
        timeline.setCycleCount(1);
        timeline.play();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        TableViewHelper<Proveedor> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID Proveedor", new ColumnInfo("idProveedor", 60.0));

        columns.put("Nombre Proveedor", new ColumnInfo("nombre", 200.0));

        columns.put("Teléfono", new ColumnInfo("telefono", 150.0));

        columns.put("Dirección", new ColumnInfo("direccion", 200.0));

        Consumer<Proveedor> updateAction = (Proveedor proveedor) -> {
            System.out.println("Actualizar: " + proveedor);
            editForm(proveedor);
        };
        Consumer<Proveedor> deleteAction = (Proveedor proveedor) -> {
            System.out.println("Eliminar: " + proveedor);
            proveedorService.delete(proveedor.getIdProveedor());
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
            Toast.showToast(stage, "Se eliminó correctamente!!", 2000, with, h);
            listar();
        };

        tableViewHelper.addColumnsInOrderWithSize(tableViewProveedores, columns, updateAction, deleteAction);
        tableViewProveedores.setTableMenuButtonVisible(true);
        listar();

        txtFiltroDato.textProperty().addListener((observable, oldValue, newValue) -> {
            filtrarProveedores(newValue);
        });
    }

    public void listar() {
        try {
            tableViewProveedores.getItems().clear();
            listarProveedor = FXCollections.observableArrayList(proveedorService.list());
            tableViewProveedores.getItems().addAll(listarProveedor);
        } catch (Exception e) {
            System.out.println("Error al listar proveedores: " + e.getMessage());
        }
    }

    public void clearForm() {
        txtNombreProveedor.setText("");
        txtTelefono.setText("");
        txtdireccion.setText("");
        txtRuc.setText("");
        txtCorreo.setText("");
        idProveedorCE = 0L;
        limpiarError();
    }

    public void limpiarError() {
        // Limpiar los errores de los campos
        Arrays.asList(txtNombreProveedor, txtTelefono, txtdireccion, txtRuc, txtCorreo)
                .forEach(field -> field.getStyleClass().remove("text-field-error"));
    }

    @FXML
    public void cancelarAccion() {
        clearForm();
        limpiarError();
    }

    void validarCampos(List<ConstraintViolation<Proveedor>> violacionesOrdenadasPorPropiedad) {
        LinkedHashMap<String, String> erroresOrdenados = new LinkedHashMap<>();
        for (ConstraintViolation<Proveedor> violacion : violacionesOrdenadasPorPropiedad) {
            String campo = violacion.getPropertyPath().toString();
            // Mapeo claro de los campos y mensajes
            if ("nombre".equals(campo)) {
                erroresOrdenados.put("nombre", violacion.getMessage());
                txtNombreProveedor.getStyleClass().add("text-field-error");
            } else if ("telefono".equals(campo)) {
                erroresOrdenados.put("telefono", violacion.getMessage());
                txtTelefono.getStyleClass().add("text-field-error");
            } else if ("direccion".equals(campo)) {
                erroresOrdenados.put("direccion", violacion.getMessage());
                txtdireccion.getStyleClass().add("text-field-error");
            } else if ("ruc".equals(campo)) {
                erroresOrdenados.put("ruc", violacion.getMessage());
                txtRuc.getStyleClass().add("text-field-error");
            } else if ("correo".equals(campo)) {
                erroresOrdenados.put("correo", violacion.getMessage());
                txtCorreo.getStyleClass().add("text-field-error");
            }
        }
        Map.Entry<String, String> primerError = erroresOrdenados.entrySet().iterator().next();
        lbnMsg.setText(primerError.getValue());
        lbnMsg.setStyle("-fx-text-fill: red; -fx-font-size: 16px;");
    }

    @FXML
    public void validarFormulario() {
        formulario = new Proveedor();
        formulario.setNombresRaso(txtNombreProveedor.getText());
        formulario.setCelular(txtTelefono.getText());
        formulario.setDireccion(txtdireccion.getText());
        formulario.setDniRuc(txtRuc.getText());
        formulario.setEmail(txtCorreo.getText());

        // Validar el formulario utilizando el validador
        Set<ConstraintViolation<Proveedor>> violaciones = validator.validate(formulario);
        List<ConstraintViolation<Proveedor>> violacionesOrdenadasPorPropiedad = violaciones.stream()
                .sorted((v1, v2) -> v1.getPropertyPath().toString().compareTo(v2.getPropertyPath().toString()))
                .collect(Collectors.toList());

        if (violacionesOrdenadasPorPropiedad.isEmpty()) {
            // Mensaje de validación exitosa
            lbnMsg.setText("Formulario válido");
            lbnMsg.setStyle("-fx-text-fill: green; -fx-font-size: 16px;");
            limpiarError();

            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;

            if (idProveedorCE != 0L) {
                formulario.setIdProveedor(idProveedorCE);
                proveedorService.update(formulario);
                Toast.showToast(stage, "Se actualizó correctamente!!", 2000, with, h);
                clearForm();
            } else {
                proveedorService.save(formulario);
                Toast.showToast(stage, "Se guardó correctamente!!", 2000, with, h);
                clearForm();
            }
            listar();
        } else {
            // Manejo de errores de validación
            validarCampos(violacionesOrdenadasPorPropiedad);
        }
    }

    private void filtrarProveedores(String filtro) {
        if (filtro == null || filtro.isEmpty()) {
            tableViewProveedores.getItems().clear();
            tableViewProveedores.getItems().addAll(listarProveedor);
        } else {
            String lowerCaseFilter = filtro.toLowerCase();
            List<Proveedor> proveedoresFiltrados = listarProveedor.stream()
                    .filter(proveedor -> proveedor.getNombresRaso().toLowerCase().contains(lowerCaseFilter) ||
                            proveedor.getCelular().contains(lowerCaseFilter) ||
                            proveedor.getDireccion().toLowerCase().contains(lowerCaseFilter))
                    .collect(Collectors.toList());
            tableViewProveedores.getItems().clear();
            tableViewProveedores.getItems().addAll(proveedoresFiltrados);
        }
    }

    public void editForm(Proveedor proveedor) {
        txtNombreProveedor.setText(proveedor.getNombresRaso());
        txtTelefono.setText(proveedor.getCelular());
        txtdireccion.setText(proveedor.getDireccion());
        txtRuc.setText(proveedor.getDniRuc());
        txtCorreo.setText(proveedor.getEmail());
        idProveedorCE = proveedor.getIdProveedor();
        limpiarError();
    }
}
