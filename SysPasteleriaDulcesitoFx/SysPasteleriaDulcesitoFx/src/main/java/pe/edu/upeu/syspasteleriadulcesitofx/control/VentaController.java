package pe.edu.upeu.syspasteleriadulcesitofx.control;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pe.edu.upeu.syspasteleriadulcesitofx.componente.*;
import pe.edu.upeu.syspasteleriadulcesitofx.dto.ComboBoxOption;
import pe.edu.upeu.syspasteleriadulcesitofx.modelo.Usuario;
import pe.edu.upeu.syspasteleriadulcesitofx.modelo.Venta;
import pe.edu.upeu.syspasteleriadulcesitofx.servicio.PerfilService;
import pe.edu.upeu.syspasteleriadulcesitofx.servicio.UsuarioService;
import pe.edu.upeu.syspasteleriadulcesitofx.servicio.VentaService;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Component
public class VentaController {

    @FXML
    private AnchorPane miVenta;
    Stage stage;

    @Autowired
    VentaService daoV;

    @FXML
    private TableView<Venta> tableView;

    @FXML
    private TextField txtFiltroDato;

    private Validator validator;

    JasperPrint jasperPrint;

    public void initialize() {

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(2000), event -> {
            stage = (Stage) miVenta.getScene().getWindow();
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

        // Configuración de la tabla
        TableViewHelper<Venta> tableViewHelper = new TableViewHelper<>();
        LinkedHashMap<String, ColumnInfo> columns = new LinkedHashMap<>();
        columns.put("ID", new ColumnInfo("idUsuario", 60.0));
        columns.put("Usuario", new ColumnInfo("user", 150.0));
        columns.put("Estado", new ColumnInfo("estado", 100.0));
        columns.put("Perfil", new ColumnInfo("idPerfil.nombre", 150.0));

        Consumer<Venta> updateAction = (Venta venta) -> {
            System.out.println("Actualizar: " + venta);
        };
        Consumer<Venta> deleteAction = (Venta venta) -> {
            double with = stage.getWidth() / 1.5;
            double h = stage.getHeight() / 2;
        };

        tableViewHelper.addColumnsInOrderWithSize(tableView, columns, updateAction, deleteAction);
        tableView.setTableMenuButtonVisible(true);
    }

    @FXML
    public void cancelarAccion() {
    }

    @FXML
    public void validarFormulario() {
    }


    @FXML
    public void pruebaVenta() {
        //txtFiltroDato

        try {
            jasperPrint= daoV.runReport(Long.parseLong(String.valueOf(txtFiltroDato.getText())));
            Platform.runLater(() -> {
                ReportAlert reportAlert=new ReportAlert(jasperPrint);
                reportAlert.show();
                //ReportDialog reportDialog = new ReportDialog(jasperPrint);
                //reportDialog.show();
            });
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}
