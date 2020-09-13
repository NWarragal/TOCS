package sample;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    private int attemptCount = 0;
    private Boxes box = new Boxes();
    private Send sending = new Send(box);

    @Override
    public void start(Stage primaryStage) {

        VBox root = new VBox(10);
        VBox inputLabels = new VBox(5);
        inputLabels.setAlignment(Pos.BASELINE_CENTER);
        root.setPadding(new Insets(10));

        Label textInput = new Label("INPUT");
        textInput.setAlignment(Pos.BASELINE_LEFT);

        TextField input = new TextField("");
        input.setPrefSize(400, 15);

        Button send = new Button("Send message");
        send.setPadding(new Insets(10));
        send.setPrefSize(150, 20);

        Label freespace1 = new Label("");
        Label freespace2 = new Label("");

        Label textOutput = new Label("OUTPUT");
        textOutput.setAlignment(Pos.BASELINE_LEFT);

        HBox controlDebug = new HBox(20);
        VBox simpleDebug = new VBox(5);

        box.listOutput.setPrefSize(400, 300);
        box.listOutput.setEditable(false);
        box.listDebug.setPrefSize(250,150);

        Label debugConsole = new Label("Debug console");

        Label textConrol = new Label("CONTROL & DEBUG");
        textConrol.setAlignment(Pos.BASELINE_LEFT);

        HBox boxx = new HBox(5);

        Label textActive = new Label("Number of attempts: ");
        textActive.setAlignment(Pos.BASELINE_LEFT);

        ObservableList<String> check = FXCollections.observableArrayList();

        for (int i = 0; i < 10; i++){
            check.add(String.valueOf(i + 1));
        }

        ComboBox<String> attempts = new ComboBox<>(check);
        attempts.setValue("10");

        attempts.setOnAction(event  ->
        {
            attemptCount = Integer.parseInt(attempts.getValue());
            sending.setAttemptCount(attemptCount);
        });

        send.setOnAction(event  ->
        {
            sending.send(input.getText());
            input.clear();
        });

        input.setOnAction(e ->
        {
            sending.send(input.getText());
            input.clear();
        });

        boxx.getChildren().addAll(textActive, attempts);
        inputLabels.getChildren().addAll(input,send);
        simpleDebug.getChildren().addAll(debugConsole, box.listDebug);
        controlDebug.getChildren().addAll(boxx, simpleDebug);
        root.getChildren().addAll(textInput, inputLabels, freespace1, textOutput,
                box.listOutput, freespace2, textConrol, controlDebug);
        primaryStage.setTitle("Collision prog");
        primaryStage.setScene(new Scene(root, 500, 700));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
