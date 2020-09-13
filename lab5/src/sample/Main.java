package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Main extends Application {
    private static final int DELAY = 1000;
    private static int PERIOD = 1000;
    private static final String marker = "*";

    private List<Station> stationList = new ArrayList<>();
    private int holdTime = 12;
    private int transferTime = 0;
    private static int wasOnMonitor = 0;
    private boolean removeWas = false;
    private boolean enterWasBefore = false;

    private Timer timer;
    private Package packager;
    private byte[] addresses = {0, 1, 2, 3, 4};


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage stage) {
        ArrayList<Station> stations = new ArrayList<>();
        stations.add(new Station(addresses[0], addresses[1], (byte) 0));
        stations.add(new Station(addresses[1], addresses[2], (byte) 0));
        stations.add(new Station(addresses[3], addresses[0], (byte) 1));
        stations.add(new Station(addresses[2], addresses[3], (byte) 0));
        stage = new Stage();
        Stage stage2 = new Stage();
        Stage stage3 = new Stage();
        Stage stage4 = new Stage();
        packager = new Package();
        stations.get(0).start(stage);
        stations.get(1).start(stage2);
        stations.get(2).start(stage4);
        stations.get(3).start(stage3);

        timer = new Timer();

        for (int i = 0; i < 4; i++) {
            int currentStation = i;
            if (i == 2) {
                stations.get(i).getInput().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        holdTime = Integer.parseInt(stations.get(currentStation).getInput().getText());
                    }
                });
            } else {
                stations.get(i).getInput().setOnKeyPressed(event -> {
                    if (event.getCode() == KeyCode.ENTER) {
                        if (stations.get(currentStation).getInput().getText().equals("")) {
                            stations.get(currentStation).setTextInput("INPUT");
                            stations.get(currentStation).getInput().setEditable(true);
                            stations.get(currentStation).setEnterWasPressed(false);
                            stations.get(currentStation).getInput().setDisable(false);
                            stations.get(currentStation).getSend().setDisable(false);
                            stations.get(currentStation).getDestination().setDisable(false);
                        }
                        if (!stations.get(currentStation).getInput().getText().equals("")
                                && !stations.get(currentStation).getDestination().getValue().equals("")) {
                            stations.get(currentStation).setTextInput("INPUT (Now is sending)");
                            stations.get(currentStation).getInput().setEditable(false);
                            stations.get(currentStation).setEnterWasPressed(true);
                            stations.get(currentStation).getInput().setDisable(true);
                            stations.get(currentStation).getSend().setDisable(true);
                            stations.get(currentStation).getDestination().setDisable(true);
                            if (!enterWasBefore) {
                                stationList.add(stations.get(currentStation));
                                stationList.get(0).setEnterWasPressed(true);
                                enterWasBefore = true;
                            } else {
                                stationList.add(stations.get(currentStation));
                            }
                        }
                    }
                });
                stations.get(i).getSend().setOnAction(event -> {
                    if (stations.get(currentStation).getInput().getText().equals("")) {
                        stations.get(currentStation).setTextInput("INPUT");
                        stations.get(currentStation).getInput().setEditable(true);
                        stations.get(currentStation).setEnterWasPressed(false);
                        stations.get(currentStation).getInput().setDisable(false);
                        stations.get(currentStation).getSend().setDisable(false);
                        stations.get(currentStation).getDestination().setDisable(false);
                    }
                    if (!stations.get(currentStation).getInput().getText().equals("")
                            && !stations.get(currentStation).getDestination().getValue().equals("")) {
                        stations.get(currentStation).setTextInput("INPUT (Now is sending)");
                        stations.get(currentStation).getInput().setEditable(false);
                        stations.get(currentStation).setEnterWasPressed(true);
                        stations.get(currentStation).getInput().setDisable(true);
                        stations.get(currentStation).getSend().setDisable(true);
                        stations.get(currentStation).getDestination().setDisable(true);
                        if (!enterWasBefore) {
                            stationList.add(stations.get(currentStation));
                            stationList.get(0).setEnterWasPressed(true);
                            enterWasBefore = true;
                        } else {
                            stationList.add(stations.get(currentStation));
                        }
                    }

                });
            }
        }

        stations.get(2).getStartButton().setOnAction((event) -> {
            timer = new Timer();
            for (int i = 0; i < 4; i++) {
                int currentStation = i;
                timer.schedule(new TimerTask() {//задержка 1с и запуск спустя 1 сек
                    @Override
                    public void run() {
                        stationRoutine(stations.get(currentStation), packager);
                    }
                }, DELAY, PERIOD);
                stations.get(2).getStartButton().setDisable(true);
                stations.get(2).getStopButton().setDisable(false);
            }
        });

        stations.get(2).getStopButton().setOnAction((event) -> {
            timer.cancel();
            timer.purge();
            stations.get(2).getStartButton().setDisable(false);
            stations.get(2).getStopButton().setDisable(true);
        });

        stations.get(0).getDebug().textProperty().addListener(observable -> sendPackage(stations.get(0)));
        stations.get(1).getDebug().textProperty().addListener(observable -> sendPackage(stations.get(1)));
        stations.get(2).getDebug().textProperty().addListener(observable -> sendPackage(stations.get(2)));
        stations.get(3).getDebug().textProperty().addListener(observable -> sendPackage(stations.get(3)));
    }

    public static void stationRoutine(Station station, Package packagerr) {
        try {
            if (packagerr.getData() == 0) {
                packagerr.setSource(station.getSourceAddress());
                packagerr.setDestination(station.getDestinationAddress());
                packagerr.setMonitor((byte) 0);
            } else {
                runOnUIThread(() -> {
                    if (packagerr.getDestination() == station.getSourceAddress()) {
                        String data = "";
                        data += (char) packagerr.getData();
                        packagerr.setControl((byte) 1);
                        station.getOutput().appendText(data);
                    }

                    if (station.getMonitor() == 1) {
                        packagerr.setMonitor((byte) 1);
                        wasOnMonitor++;
                    }

                    if (station.getMonitor() == 1 && packagerr.getMonitor() == 1 && wasOnMonitor == 2) {
                        packagerr.setControl((byte) 0);
                        packagerr.freeData();
                        wasOnMonitor = 0;
                    }

                    if (packagerr.getControl() == 1 && station.getMonitor() == 1 || packagerr.getControl() == 1 && packagerr.getMonitor() == 1) {
                        packagerr.setControl((byte) 0);
                        packagerr.freeData();
                        wasOnMonitor = 0;
                    }
                });
            }
            String info;
            if (packagerr.getData() != 0){
                info = marker + packagerr.getData();
            } else {
                info = marker;
            }
            runOnUIThread(() -> station.getDebug().setText(info));
            Thread.sleep(DELAY);
            runOnUIThread(() -> station.getDebug().setText(""));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public void sendPackage(Station station) {
        if (station.getInput().getText().equals("")) {
            Platform.runLater(() -> {
                station.setTextInput("INPUT");
                station.getInput().setEditable(true);
                station.getInput().setDisable(false);
                station.getSend().setDisable(false);
                station.getDestination().setDisable(false);
            });
        }
        if (!station.getInput().getText().equals("")
                && !station.getDestinationValue().equals("")
                && packager.getData() == 0
                && packager.getControl() == 0
                && packager != null
                && station.isEnterWasPressed()) {
            if (!station.getDebug().getText().equals("")) {
                if (station.getDestinationValue().equals("1")) {
                    station.setDestinationAddress(addresses[0]);
                }
                if (station.getDestinationValue().equals("2")) {
                    station.setDestinationAddress(addresses[1]);
                }
                if (station.getDestinationValue().equals("3")) {
                    station.setDestinationAddress(addresses[2]);
                }

                if (station.getDestinationValue().equals("4")) {
                    station.setDestinationAddress(addresses[4]);
                }

                if (station.getSourceAddress() == station.getDestinationAddress()) {//если пересылка самому себе
                    String text = station.getInput().getText();
                    station.getOutput().setText(text);
                    station.getInput().setText("");
                    enterWasBefore = false;
                } else {
                    packager.setDestination(station.getDestinationAddress());
                    packager.setSource(station.getSourceAddress());
                    packager.setMonitor(station.getMonitor());
                    String reduced = station.getInput().getText().substring(1);
                    packager.setData(station.getInput().getText().getBytes()[0]);
                    station.getInput().setText(reduced);
                    transferTime++;
                    if (transferTime >= holdTime / 4) {
                        stationList.add(stationList.get(0));
                        stationList.get(0).setEnterWasPressed(false);
                        stationList.remove(0);
                        stationList.get(0).setEnterWasPressed(true);
                        transferTime = 0;
                    }
                    removeWas = false;
                }
            }
        } else {
            if (station.getInput().getText().equals("") && packager.getSource() == station.getSourceAddress() && !station.getDestinationValue().equals("")) {
                if (stationList.size() != 0 && !removeWas && station.isEnterWasPressed()) {
                    stationList.get(0).setEnterWasPressed(false);
                    stationList.remove(0);
                    transferTime = 0;
                    removeWas = true;
                }
                if (stationList.size() != 0) {
                    stationList.get(0).setEnterWasPressed(true);
                    enterWasBefore = true;
                } else {
                    enterWasBefore = false;
                }
            }
        }
    }


    private static void runOnUIThread(Runnable task) {
        if (task == null) throw new NullPointerException("Param task can not be null");

        if (Platform.isFxApplicationThread()) task.run();
        else Platform.runLater(task);
    }
}