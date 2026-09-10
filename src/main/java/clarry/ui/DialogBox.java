package clarry.ui;

import java.io.IOException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Displays one user or Clarry message with a speaker badge.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private Label avatar;

    /** Loads the reusable dialog layout and fills it with message content. */
    private DialogBox(String text, String avatarText) {
        try {
            FXMLLoader loader = new FXMLLoader(MainWindow.class.getResource("/view/DialogBox.fxml"));
            loader.setController(this);
            loader.setRoot(this);
            loader.load();
        } catch (IOException e) {
            throw new AssertionError("Unable to load a dialog box", e);
        }
        dialog.setText(text);
        avatar.setText(avatarText);
    }

    /**
     * Creates a right-aligned dialog for the user.
     *
     * @param text user's message
     * @return user dialog box
     */
    public static DialogBox getUserDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "YOU");
        dialogBox.dialog.getStyleClass().add("user-bubble");
        dialogBox.avatar.getStyleClass().add("user-avatar");
        return dialogBox;
    }

    /**
     * Creates a left-aligned dialog for Clarry.
     *
     * @param text Clarry's response
     * @return Clarry dialog box
     */
    public static DialogBox getClarryDialog(String text) {
        DialogBox dialogBox = new DialogBox(text, "C");
        dialogBox.flip();
        dialogBox.dialog.getStyleClass().add("clarry-bubble");
        dialogBox.avatar.getStyleClass().add("clarry-avatar");
        return dialogBox;
    }

    /** Places the speaker badge before the message for Clarry responses. */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(getChildren());
        FXCollections.reverse(children);
        getChildren().setAll(children);
        setAlignment(Pos.TOP_LEFT);
    }
}
