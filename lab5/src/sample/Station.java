package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Station {
    private byte monitor;
    private byte sourceAddress;
    private byte destinationAddress;

    private boolean enterWasPressed;

    ComboBox<String> destination;

    private TextField input = new TextField();
    private TextArea output = new TextArea();
    private TextField debug = new TextField();
    private TextField source;
    private Button stop  = new Button();
    private Button start = new Button();
    Label textInput;
    Button send;

    private int width = 400;
    private int height = 500;

    Station(byte source, byte dest, byte monitor) {
        this.sourceAddress = source;
        this.destinationAddress = dest;
        this.monitor = monitor;

        this.source = new TextField();

        if (source == 0)
            this.source.setText("1");
        else if (source == 1)
            this.source.setText("2");
        else if (source == 2)
            this.source.setText("3");
        this.monitor = monitor;
    }

    public Button getSend() {
        return send;
    }

    public void setTextInput(String mes) {
        textInput.setText(mes);
    }

    public ComboBox<String> getDestination() {
        return destination;
    }

    public byte getSourceAddress() {
        return sourceAddress;
    }

    public byte getDestinationAddress() {
        return destinationAddress;
    }

    public byte getMonitor() {
        return monitor;
    }


    public Button getStartButton() {
        return start;
    }

    public TextArea getOutput() {
        return output;
    }

    public TextField getDebug() {
        return debug;
    }

    public TextField getInput() {
        return input;
    }

    public void setEnterWasPressed(boolean enterWasPressed) {
        this.enterWasPressed = enterWasPressed;
    }

    public boolean isEnterWasPressed() {
        return enterWasPressed;
    }

    public String getDestinationValue() {
        return destination.getValue();
    }

    public void setDestinationAddress(byte dest) {
        destinationAddress = dest;
    }

    public Button getStopButton() {
        return stop;
    }

    public void start(Stage stage) {
        try {
            VBox root = new VBox();
            root.setPadding(new Insets(5));
            root.setSpacing(5);
            root.setFillWidth(true);
            VBox inputLabels = new VBox(5);
            inputLabels.setAlignment(Pos.BASELINE_CENTER);
            root.setPadding(new Insets(10));

            textInput = new Label("INPUT");
            textInput.setAlignment(Pos.BASELINE_LEFT);

            input.setPrefSize(400, 15);
            start.setText("Start");
            start.setPadding(new Insets(10));
            start.setPrefSize(150, 20);

            send = new Button("Send to other port");
            send.setPadding(new Insets(10));
            send.setPrefSize(150, 20);

            Label freespace1 = new Label("");
            Label freespace2 = new Label("");

            Label textOutput = new Label("OUTPUT");
            textOutput.setAlignment(Pos.BASELINE_LEFT);

            VBox simpleControl = new VBox(1);
            HBox controlDebug = new HBox(20);
            VBox simpleDebug = new VBox(5);

            output.setPrefSize(400, 150);
            debug.setPrefSize(100, 50);
            debug.setEditable(false);
            output.setEditable(false);

            Label debugConsole = new Label("Debug console");

            Label textConrol = new Label("CONTROL & DEBUG");
            textConrol.setAlignment(Pos.BASELINE_LEFT);

            Label sourcAdr = new Label("Source address");
            sourcAdr.setAlignment(Pos.BASELINE_LEFT);
            Label destAdr = new Label("Destination address");
            destAdr.setAlignment(Pos.BASELINE_LEFT);

            source.setEditable(false);
            source.setPrefSize(100, 15);

            ObservableList<String> dest = FXCollections.observableArrayList();
            dest.add("1");
            dest.add("2");
            dest.add("3");
            dest.add("4");

            destination = new ComboBox<>(dest);
            destination.setValue("");
            destination.setPrefSize(110, 15);

        stage.setTitle("Ring prog");
            if (monitor == 1) {
                textInput.setText("SET HOLD TIME");
                input.setText("12");
                inputLabels.getChildren().addAll(input, start);
                root.getChildren().addAll(textInput, inputLabels, freespace1, textConrol, debug);
            } else {
                inputLabels.getChildren().addAll(input, send);
                simpleControl.getChildren().addAll(sourcAdr, source,
                        destAdr, destination);
                simpleDebug.getChildren().addAll(debugConsole, debug);
                controlDebug.getChildren().addAll(simpleDebug, simpleControl);
                root.getChildren().addAll(textInput, inputLabels, freespace1, textOutput,
                        output, freespace2, textConrol, controlDebug);
            }
            if (monitor == 1) {
                height = 200;
            }

            Scene scene = new Scene(root, width, height);

            stage.setTitle("Ring prog");
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}