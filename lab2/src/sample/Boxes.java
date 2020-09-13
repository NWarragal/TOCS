package sample;

        import javafx.collections.FXCollections;
        import javafx.collections.ObservableList;
        import javafx.scene.control.ListView;
        import javafx.scene.control.RadioButton;

public class Boxes {
    ListView<String> listOutput = new ListView<>();
    ObservableList<String> list = FXCollections.observableArrayList();
    ListView<String> listDebug = new ListView<>();
    ObservableList<String> listdeb = FXCollections.observableArrayList();
    RadioButton rts = new RadioButton("RTS/CTS");
    RadioButton dtr = new RadioButton("DTR/DSR");
    RadioButton none = new RadioButton("No control");
}
