package clarry.ui;

import clarry.Clarry;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

/**
 * Controls Clarry's main chat window.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private Clarry clarry;

    /** Keeps the newest chat message visible as the conversation grows. */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
    }

    /**
     * Supplies the chatbot backend after the FXML view has loaded.
     *
     * @param clarry chatbot backend
     */
    public void setClarry(Clarry clarry) {
        this.clarry = clarry;
        dialogContainer.getChildren().add(
                DialogBox.getClarryDialog("Hello! I'm Clarry.\nWhat can I do for you?"));
    }

    /** Sends non-blank input to Clarry and displays both sides of the exchange. */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        if (input.isBlank()) {
            return;
        }

        String response = clarry.getResponse(input);
        dialogContainer.getChildren().addAll(
                DialogBox.getUserDialog(input),
                DialogBox.getClarryDialog(response.stripLeading()));
        userInput.clear();

        if (input.equals("bye")) {
            userInput.setDisable(true);
            sendButton.setDisable(true);
        }
    }
}
