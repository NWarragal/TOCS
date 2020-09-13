package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jssc.SerialPortList;

import java.util.Arrays;

public class Main extends Application {

    private Boxes box = new Boxes();
    private String comPortChar = "none";
    private boolean isActive = false;
    private String mode;
    private Port port;

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

        Button send = new Button("Send to other port");
        send.setPadding(new Insets(10));
        send.setPrefSize(150, 20);

        Label freespace1 = new Label("");
        Label freespace2 = new Label("");
        Label freespace3 = new Label("");

        Label textOutput = new Label("OUTPUT");
        textOutput.setAlignment(Pos.BASELINE_LEFT);

        VBox simpleControl = new VBox(1);
        HBox controlDebug = new HBox(20);
        VBox simpleDebug = new VBox(5);

        box.listOutput.setPrefSize(400, 300);
        box.listDebug.setPrefSize(250,150);

        Label debugConsole = new Label("Debug console");

        Label textConrol = new Label("CONTROL & DEBUG");
        textConrol.setAlignment(Pos.BASELINE_LEFT);

        HBox boxx = new HBox(5);

        Label textActive = new Label("Chosen port: ");
        textActive.setAlignment(Pos.BASELINE_LEFT);

        ObservableList<String> check = FXCollections.observableArrayList();
        String[] comports = SerialPortList.getPortNames();

        check.add("none");
        check.addAll(Arrays.asList(comports));

        ComboBox<String> comPort = new ComboBox<>(check);

        Label textForm = new Label("Choose the form of control:");
        textForm.setAlignment(Pos.BASELINE_LEFT);

        box.none.setSelected(true);

        comPort.setOnAction(event  ->
        {
            comPortChar = comPort.getValue();
            if(!comPortChar.equals("none")){
                if(isActive){

                    port.ClosePort();
                    isActive = false;
                }
                try{
                    port = new Port(comPortChar);
                    port.setBox(box);
                    port.OpenPort();
                    isActive = true;
                }catch (Exception ignored){
                    isActive = false;
                    showAlert("ERROR: COM port is already chosen or not exists");
                    Platform.runLater(() -> {
                        comPort.setValue("none");
                    });
                }
            }
            if(comPortChar.equals("none")){
                if(isActive){
                    port.ClosePort();
                    isActive = false;
                }
            }
        });

        box.none.setOnAction(event  ->
        {
            if(box.none.isSelected()){
                box.rts.setSelected(false);
                box.dtr.setSelected(false);
            }
            if(!box.none.isSelected())box.none.setSelected(true);
        });

        box.rts.setOnAction(event  ->
        {
            if(box.rts.isSelected()){
                box.dtr.setSelected(false);
                box.none.setSelected(false);
            }
            if(!box.rts.isSelected())box.rts.setSelected(true);
        });

        box.dtr.setOnAction(event  ->
        {
            if(box.dtr.isSelected()){
                box.rts.setSelected(false);
                box.none.setSelected(false);
            }
            if(!box.dtr.isSelected())box.dtr.setSelected(true);
        });

        send.setOnAction(event  ->
        {
            if(isActive) {
                if (!box.rts.isSelected() && !box.dtr.isSelected()) mode = "none";
                if (box.rts.isSelected() && !box.dtr.isSelected()) mode = "RTS";
                if (!box.rts.isSelected() && box.dtr.isSelected()) mode = "DTR";
                String msg = input.getText();
                msg += "\n";
                input.clear();
                if (!msg.equals("\n")) {
                    port.SendString(msg, mode);
                }
            } else {
                showAlert("ERROR: COM port is not chosen");
            }
        });

        input.setOnAction(e ->
        {
            if(isActive) {
                if (!box.rts.isSelected() && !box.dtr.isSelected()) mode = "none";
                if (box.rts.isSelected() && !box.dtr.isSelected()) mode = "RTS";
                if (!box.rts.isSelected() && box.dtr.isSelected()) mode = "DTR";
                String msg = input.getText();
                msg += "\n";
                input.clear();
                if (!msg.equals("\n")) {
                    port.SendString(msg, mode);
                }
            } else {
                showAlert("ERROR: COM port is not chosen");
            }
        });

        boxx.getChildren().addAll(textActive, comPort);
        inputLabels.getChildren().addAll(input,send);
        simpleControl.getChildren().addAll(boxx, freespace3, textForm, box.rts, box.dtr, box.none);
        simpleDebug.getChildren().addAll(debugConsole, box.listDebug);
        controlDebug.getChildren().addAll(simpleControl, simpleDebug);
        root.getChildren().addAll(textInput, inputLabels, freespace1, textOutput,
                box.listOutput, freespace2, textConrol, controlDebug);
        primaryStage.setTitle("COM port prog");
        primaryStage.setScene(new Scene(root, 500, 700));
        primaryStage.show();
    }

    private void showAlert(String mes) {
        box.listdeb.add(mes);
        box.listDebug.setItems(box.listdeb);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
