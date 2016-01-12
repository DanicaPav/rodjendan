package Chat;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class ChatAppClient extends Application {

    private TextArea messages = new TextArea();
    private Client connection = new Client("127.0.0.1", 55555, data -> {
        Platform.runLater(() -> {
            messages.appendText(data.toString() + "\n");
        });
    });

    private Parent createContent() {
        messages.setFont(Font.font(72));
        messages.setPrefHeight(650);
        TextField input = new TextField();
        input.setOnAction(event -> {
            String message = "Client: " + input.getText();
            input.clear();

            messages.appendText(message + "\n");

            try {
                connection.send(message);
            }
            catch (Exception e) {
                messages.appendText("Failed to send\n");
            }
        });

        VBox root = new VBox(20, messages, input);
        root.setPrefSize(600, 600);
        return root;
    }

    @Override
    public void init() throws Exception {
        connection.startConnection();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setScene(new Scene(createContent()));
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        connection.closeConnection();
    }

    public static void main(String[] args) {
        launch(args);
    }
}