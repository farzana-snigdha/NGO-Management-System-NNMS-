package Registration;

import Person.Validation;
import Utilities.ShowAlertDialogue;
import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {
    @FXML
    private AnchorPane registerPane;


    @FXML
    private JFXTextField userNameTextfield;

    @FXML
    private JFXTextField emailTextfield;

    @FXML
    private JFXPasswordField passwordTextfield;

    @FXML
    private JFXTextField employeePhoneNumber;

    @FXML
    private JFXComboBox employeeGender;

    @FXML
    private JFXDatePicker employeeDOB;

    @FXML
    private JFXTextField employeeAddress;

    @FXML
    private JFXPasswordField retypePasswordTextfield;

    @FXML
    private void handleBackToLoginPage() throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
        registerPane.getChildren().setAll(pane);
    }

    RegisterModel registerModel = new RegisterModel();

    public String getUserNameTextfield() {
        return userNameTextfield.getText().trim();
    }

    public String getEmailTextfield() {
        return emailTextfield.getText().trim();
    }

    public String getPasswordTextfield() {
        String passwordToHash = passwordTextfield.getText();
        return encryptPassword(passwordToHash);
    }

    private static byte[] getSalt() throws NoSuchAlgorithmException, NoSuchProviderException {
        //Always use a SecureRandom generator
        SecureRandom sr = SecureRandom.getInstance("SHA512PRNG", "SUN");
        //Create array for salt
        byte[] salt = new byte[16];
        //Get a random salt
        sr.nextBytes(salt);
        //return salt
        return salt;
    }

    private static String get_SHA_512_SecurePassword(String passwordToHash, byte[] salt) {
        String generatedPassword = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);
            byte[] bytes = md.digest(passwordToHash.getBytes());
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            generatedPassword = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return generatedPassword;
    }

    public String getRetypePasswordTextfield() {
        String passwordToHash = retypePasswordTextfield.getText();
        return encryptPassword(passwordToHash);
    }

    public String encryptPassword(String passwordToHash) {
        byte[] salt = new byte[0];
        try {
            salt = getSalt();
        } catch (NoSuchAlgorithmException | NoSuchProviderException e) {
         //   e.printStackTrace();
        }

        return get_SHA_512_SecurePassword(passwordToHash, salt);
    }

    private final ObservableList<String> gender = FXCollections.observableArrayList("Male", "Female", "Others");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        employeeGender.setItems(gender);

    }

    ShowAlertDialogue showAlertDialogue = new ShowAlertDialogue();

    @FXML
    public void confirmRegistration() {
        try {
            Date dobDate = new SimpleDateFormat("MM/dd/yyyy").parse(employeeDOB.getEditor().getText());

            if (new Validation().checkEmailAndPhoneValidation(getEmailTextfield(), employeePhoneNumber.getText())) {
                if (new Validation().checkPasswordValidation(getPasswordTextfield())) {
                    if (getPasswordTextfield().equals(getRetypePasswordTextfield())) {
                        if (registerModel.isRegistrationSuccessful(getUserNameTextfield(), dobDate, employeeGender.getSelectionModel().getSelectedItem().toString(),
                                employeeAddress.getText(), employeePhoneNumber.getText(), getEmailTextfield(), getPasswordTextfield())) {
                            showAlertDialogue.infoBox("Registration Successful!", null, "Registration");
                            AnchorPane pane = FXMLLoader.load(getClass().getResource("../Login/login.fxml"));
                            registerPane.getChildren().setAll(pane);
                        } else
                            showAlertDialogue.infoBox("Registration unsuccessful!", null, "Failed");
                    } else
                        showAlertDialogue.infoBox("Password doesn't match!", null, "Failed");

                } else
                    showAlertDialogue.infoBox("Input valid Password!", null, "Failed");
            } else
                showAlertDialogue.infoBox("Input valid Email and Contact number!", null, "Failed");


        } catch (Exception e) {
            System.out.println(e + "  registerControllerView");
        }

    }


}