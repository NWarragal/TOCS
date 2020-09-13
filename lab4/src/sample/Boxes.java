package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

public class Boxes {
    TextField listOutput = new TextField();
    ListView<String> listDebug = new ListView<>();
    ObservableList<String> listdeb = FXCollections.observableArrayList();
}
