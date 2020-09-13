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
import java.util.List;

public class Main extends Application {

    private Boxes box = new Boxes();
    private String comPortChar = "none";
    private boolean isActive = false;
    private String mode;
    private Port port;
    private Package pack = new Package(box);

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
        Label freespace4 = new Label("");
        Label freespace5 = new Label("");

        Label textOutput = new Label("OUTPUT");
        textOutput.setAlignment(Pos.BASELINE_LEFT);

        VBox simpleControl = new VBox(1);
        HBox controlDebug = new HBox(20);
        VBox simpleDebug = new VBox(5);

        box.listOutput.setPrefSize(400, 300);
        box.listDebug.setPrefSize(300,150);

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

        Label sourcAdr = new Label("Source address (now 0)");
        sourcAdr.setAlignment(Pos.BASELINE_LEFT);
        Label destAdr = new Label("Destination address (now 1)");
        destAdr.setAlignment(Pos.BASELINE_LEFT);

        TextField source = new TextField("0");
        source.setPrefSize(100, 15);
        TextField destination = new TextField("1");
        destination.setPrefSize(100, 15);

        CheckBox error = new CheckBox("Simulate error on destination point");

        source.setOnAction(event  ->
        {
            try {
                String info = source.getText();
                int address = Integer.parseInt(info);
                if(address > 255){
                    showAlert("ERROR: Number must be lower than 255");
                    source.clear();
                }
                if(address < 0){
                    showAlert("ERROR: Number must be more than zero");
                    source.clear();
                }
                if (address >= 0 && address <= 255) {
                    if (!source.getText().equals(destination.getText())) {
                        sourcAdr.setText("Source adress (now " + address + ")");
                        pack.setSource(address);
                    }else {
                        showAlert("ERROR: Adress mustn't be equal to destination");
                        source.clear();
                    }
                }
            }catch (Exception ex){
                showAlert("ERROR: Not INT in source address");
                source.clear();
            }
        });

        destination.setOnAction(event  ->
        {
            try {
                String info = destination.getText();
                int address = Integer.parseInt(info);
                if(address > 255){
                    showAlert("ERROR: Number must be lower than 255");
                    destination.clear();
                }
                if(address < 0){
                    showAlert("ERROR: Number must be more than zero");
                    destination.clear();
                }
                if (address >= 0 && address <= 255) {
                    if (!source.getText().equals(destination.getText())) {
                        destAdr.setText("Destination adress (now " + address + ")");
                        pack.setDestination(address);
                    }else {
                        showAlert("ERROR: Adress mustn't be equal to source");
                        destination.clear();
                    }
                }
            }catch (Exception ex){
                showAlert("ERROR: Not INT in destination address");
                destination.clear();
            }
        });

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
                    port.setPak(pack);
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
                input.clear();
                if (!msg.equals("")) {
                    if (error.isSelected()) pack.setEmerrror(true);
                    if (!error.isSelected()) pack.setEmerrror(false);
                    List<byte[]> sending = pack.GetToPackage(msg);
                    for (int i = 0; i < sending.size(); i++){
                        byte[] buf = pack.doByteStuffing(sending.get(i));
                        StringBuilder sb = new StringBuilder();
                        for (byte d : buf) {
                            sb.append(String.format("0x%02X ", d));
                        }
                        box.listdeb.add(sb.toString());
                        box.listDebug.setItems(box.listdeb);
                        port.SendByteArr(buf, mode);
                    }
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
                input.clear();
                if (!msg.equals("")) {
                    if (error.isSelected()) pack.setEmerrror(true);
                    if (!error.isSelected()) pack.setEmerrror(false);
                    List<byte[]> sending = pack.GetToPackage(msg);
                    for (int i = 0; i < sending.size(); i++){
                        byte[] buf = pack.doByteStuffing(sending.get(i));
                        StringBuilder sb = new StringBuilder();
                        for (byte d : buf) {
                            sb.append(String.format("0x%02X ", d));
                        }
                        box.listdeb.add(sb.toString());
                        box.listDebug.setItems(box.listdeb);
                        port.SendByteArr(buf, mode);
                    }
                }
            } else {
                showAlert("ERROR: COM port is not chosen");
            }
        });

        boxx.getChildren().addAll(textActive, comPort);
        inputLabels.getChildren().addAll(input,send);
        simpleControl.getChildren().addAll(boxx, freespace3, sourcAdr, source,
                destAdr, destination, freespace4, textForm, box.rts, box.dtr, box.none);
        simpleDebug.getChildren().addAll(debugConsole, box.listDebug, freespace5, error);
        controlDebug.getChildren().addAll(simpleControl, simpleDebug);
        root.getChildren().addAll(textInput, inputLabels, freespace1, textOutput,
                box.listOutput, freespace2, textConrol, controlDebug);
        primaryStage.setTitle("COM port prog");
        primaryStage.setScene(new Scene(root, 500, 800));
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
