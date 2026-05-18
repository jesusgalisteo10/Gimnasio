package com.example.view;

import com.example.App;
import com.example.model.dao.ClientDAO;
import com.example.model.entity.Client;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class AddClientController extends Controller implements Initializable {
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private Button addClientButton;
    @FXML
    private TextField fieldName;
    @FXML
    private TextField fieldSurname;
    @FXML
    private TextField fieldDni;
    @FXML
    private TextField fieldEmail;
    @FXML
    private TextField fieldPassword;
    @FXML
    private TextField fieldSex;


    @Override
    public void onOpen(Object input) throws Exception {

    }

    @Override
    public void onClose(Object output) {

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    /**
     * Maneja la acción de guardar cuando el usuario hace clic en el botón de guardar.
     * Valida los campos de entrada (nombre, apellido, DNI, correo electrónico, contraseña, sexo)
     * y muestra mensajes de error si algún campo está vacío o si los formatos de DNI o correo electrónico son inválidos.
     * Si todas las validaciones pasan, crea un nuevo objeto Client y lo guarda usando ClientDAO.
     * Muestra un mensaje de éxito al agregar el cliente correctamente y navega de vuelta a la página principal.
     * Cierra la ventana actual después de una operación exitosa.
     *
     * @param event el evento activado por la acción de guardar
     */
    @FXML
    public void onSave(Event event) {
        try {
            String name = fieldName.getText().trim();
            String surname = fieldSurname.getText().trim();
            String dni = fieldDni.getText().trim();
            String email = fieldEmail.getText().trim();
            String password = fieldPassword.getText().trim();
            String sex = fieldSex.getText().trim();

            // Verificar si algún campo está vacío
            if (name.isEmpty() || surname.isEmpty() || dni.isEmpty() || email.isEmpty() || password.isEmpty() || sex.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Campos Vacíos", "Por favor, complete todos los campos.");
            } else {
                // Validar formato de DNI
                if (!validateDNI(dni)) {
                    showAlert(Alert.AlertType.ERROR, "Error en DNI", "El formato del DNI no es válido.");
                } else {
                    // Validar formato de correo electrónico
                    if (!validateEmail(email)) {
                        showAlert(Alert.AlertType.ERROR, "Error en Correo Electrónico", "El formato del correo electrónico no es válido.");
                    } else {
                        // Crear objeto Cliente y guardar en la base de datos
                        Client client = new Client(name, surname, email, password, dni, sex);
                        ClientDAO cdao = new ClientDAO();
                        cdao.save(client);

                        // Mostrar mensaje de éxito y navegar a la página principal
                        showAlert(Alert.AlertType.INFORMATION, "Cliente Agregado", "El cliente se ha agregado correctamente.");
                        App.currentController.changeScene(Scenes.MAINPAGE, null);

                        // Cerrar ventana actual
                        ((Node) (event.getSource())).getScene().getWindow().hide();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error al Guardar", "Hubo un error al intentar guardar el cliente.");
        }
    }


    /**
     * Valida una dirección de correo electrónico usando una expresión regular.
     *
     * @param email la dirección de correo electrónico a validar
     * @return true si la dirección de correo electrónico es válida, false en caso contrario
     */
    private boolean validateEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(emailRegex);
    }

    /**
     * Valida el formato de un DNI español (Documento Nacional de Identidad).
     *
     * @param dni el número de DNI a validar
     * @return true si el formato del DNI es válido, false en caso contrario
     */
    private boolean validateDNI(String dni) {
        String dniRegex = "\\d{8}[a-zA-Z]";
        return dni.matches(dniRegex);
    }

    /**
     * Muestra un cuadro de diálogo de alerta con el tipo de alerta, título y contenido especificados.
     *
     * @param alertType el tipo de alerta (ej. error, información)
     * @param title     el texto del título de la alerta
     * @param content   el texto del contenido de la alerta
     */
    private void showAlert(Alert.AlertType alertType, String title, String content) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}



