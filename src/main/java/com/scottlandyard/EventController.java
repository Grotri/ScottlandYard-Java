package com.scottlandyard;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import org.jgrapht.*;
import org.jgrapht.graph.*;

public class EventController {
    @FXML
    public Pane mainPane;
    private Pane mainPaneLink;
    public void setMainPane(Pane mainPane) {
        this.mainPaneLink = mainPane;
    }
    private final HashMap<Integer, ImageView> numAndPoint = new HashMap<>();
    private void initPoints() {
        ArrayList<Node> allIVs = GameProcessing.getAllNodes(mainPaneLink);
        for (int i = 0; i <= 199; i++) {
            numAndPoint.put(i, (ImageView) allIVs.get(i));
        }
        for (int i = 0; i <= 199; i++) {
            numAndPoint.get(i).setVisible(true);
            numAndPoint.get(i).setOnMouseClicked(pointClicked);
        }
    }
    @FXML
    private TextFlow logArea;
    private TextFlow logAreaLink;
    public void setTextFlow(TextFlow logArea) {
        this.logAreaLink = logArea;
    }
    @FXML
    private MenuButton modeChoose;
    @FXML
    private javafx.scene.control.Label taxiText;
    @FXML
    private javafx.scene.control.Label busText;
    @FXML
    private javafx.scene.control.Label metroText;
    @FXML
    private javafx.scene.control.Label blackText;
    @FXML
    private ToggleButton taxiButton;
    @FXML
    private ToggleButton busButton;
    @FXML
    private ToggleButton metroButton;
    @FXML
    private ToggleButton blackButton;
    @FXML
    private ImageView mrX;
    @FXML
    private ImageView doc1;
    @FXML
    private ImageView doc2;
    @FXML
    private ImageView doc3;
    @FXML
    private ImageView doc4;
    private Integer mrXCurrPos;
    private Integer doc1CurrPos;
    private Integer doc2CurrPos;
    private Integer doc3CurrPos;
    private Integer doc4CurrPos;
    private Integer mrXTaxiTickets;
    private Integer mrXBusTickets;
    private Integer mrXMetroTickets;
    private Integer mrXBlackTickets;
    private Integer doc1TaxiTickets;
    private Integer doc1BusTickets;
    private Integer doc1MetroTickets;
    private Integer doc2TaxiTickets;
    private Integer doc2BusTickets;
    private Integer doc2MetroTickets;
    private Integer doc3TaxiTickets;
    private Integer doc3BusTickets;
    private Integer doc3MetroTickets;
    private Integer doc4TaxiTickets;
    private Integer doc4BusTickets;
    private Integer doc4MetroTickets;
    private String mode;
    private Integer turn;
    private String player;
    private final WeightedMultigraph<Integer, DefaultWeightedEdge> pathsGraph = new WeightedMultigraph<>(DefaultWeightedEdge.class);

    private void fillUpGraphs() {
        for (int i = 1; i <= 199; i++) {
            pathsGraph.addVertex(i);
        }

        Deque<Integer> paths = GameProcessing.pathsSplitter();
        int size = paths.size();
        for (int i = 0; i < size; i = i + 3) {
            int v1 = paths.pop();
            int weight = paths.pop();
            int v2 = paths.pop();

            DefaultWeightedEdge edge = pathsGraph.addEdge(v1, v2);
            pathsGraph.setEdgeWeight(edge, weight);
        }
    }

    HashMap<String, ImageView> playerAndMapSync = new HashMap<>();
    private void initPlayerSync() {
        playerAndMapSync.put("mrx", mrX);
        playerAndMapSync.put("doc1", doc1);
        playerAndMapSync.put("doc2", doc2);
        playerAndMapSync.put("doc3", doc3);
        playerAndMapSync.put("doc4", doc4);
    }
    HashMap<String, Integer> playerAndMapCordsSync = new HashMap<>();
    private void initPlayerCordsSync() {
        playerAndMapCordsSync.put("mrx", mrXCurrPos);
        playerAndMapCordsSync.put("doc1", doc1CurrPos);
        playerAndMapCordsSync.put("doc2", doc2CurrPos);
        playerAndMapCordsSync.put("doc3", doc3CurrPos);
        playerAndMapCordsSync.put("doc4", doc4CurrPos);
    }
    HashMap<String, String> inTurnSwitches = new HashMap<>();
    private void initInTurnSwitches() {
        inTurnSwitches.put("mrx", "doc1");
        inTurnSwitches.put("doc1", "doc2");
        inTurnSwitches.put("doc2", "doc3");
        inTurnSwitches.put("doc3", "doc4");
        inTurnSwitches.put("doc4", "mrx");
    }

    private void initTickets() {
        mrXTaxiTickets = 4;
        mrXBusTickets = 3;
        mrXMetroTickets = 3;
        mrXBlackTickets = 3;
        doc1TaxiTickets = 11;
        doc1BusTickets = 8;
        doc1MetroTickets = 4;
        doc2TaxiTickets = 11;
        doc2BusTickets = 8;
        doc2MetroTickets = 4;
        doc3TaxiTickets = 11;
        doc3BusTickets = 8;
        doc3MetroTickets = 4;
        doc4TaxiTickets = 11;
        doc4BusTickets = 8;
        doc4MetroTickets = 4;
    }
    private void initGameScreen() {
        taxiButton.setSelected(false);
        busButton.setSelected(false);
        metroButton.setSelected(false);
        blackButton.setSelected(false);
        for (int i = 1; i <= 199; i++) {
            numAndPoint.get(i).setVisible(false);
            numAndPoint.get(i).setDisable(true);
        }
    }
    @FXML
    private void initAutoplay() throws IOException {
        modeChoose.setVisible(false);
        fillUpGraphs();
        setMainPane(mainPane);
        initPoints();
        initPlayerSync();
        initPlayerCordsSync();
        mode = "auto";
        turn = 1;
        player = "mrx";
        for (int i = 1; i <= 199; i++) {
            ImageView iv = numAndPoint.get(i);
            iv.setVisible(true);
        }
        initTickets();
        setTicketsSection();
        setPositions(GameProcessing.getStartPos());
    }
    @FXML
    private void initSelfplay() throws IOException {
        logArea.getChildren().clear();
        modeChoose.setVisible(false);
        fillUpGraphs();
        setMainPane(mainPane);
        setTextFlow(logArea);
        initPoints();
        initPlayerSync();
        initPlayerCordsSync();
        initInTurnSwitches();
        mode = "self";
        turn = 1;
        Text text = new Text("Turn " + turn +" has started!" + "\r\n");
        text.setFill(Color.BLUE);
        logArea.getChildren().addAll(text);
        player = "mrx";
        System.out.println(player);
        initTickets();
        setTicketsSection();
        setPositions(GameProcessing.getStartPos());
        initGameScreen();
    }

    EventHandler<MouseEvent> pointClicked = mouseEvent -> {
        taxiButton.setSelected(false);
        busButton.setSelected(false);
        metroButton.setSelected(false);
        blackButton.setSelected(false);
        for (int i = 1; i < 199; i++) {
            numAndPoint.get(i).setVisible(false);
            numAndPoint.get(i).setDisable(true);
        }
        Integer key = GameProcessing.getKeyByValue(numAndPoint, (ImageView)mouseEvent.getSource());
        updatePositionVariable(key);
        initPlayerCordsSync();
        updateOnMapPosition();
        updateTickets();
        if (player.equals("doc4")) {
            turn += 1;
            Text text = new Text("Turn " + turn +" has started!" + "\r\n");
            text.setFill(Color.BLUE);
            logArea.getChildren().addAll(text);
        }
        player = inTurnSwitches.get(player);
        setTicketsSection();
        checkEndOfGame();
    };

    private void setTicketsSection() {
        if (player.equals("mrx")) {
            setMrXSetup();
        }
        if (player.equals("doc1")) {
            setDocSetup(doc1TaxiTickets, doc1BusTickets, doc1MetroTickets);
        }
        if (player.equals("doc2")) {
            setDocSetup(doc2TaxiTickets, doc2BusTickets, doc2MetroTickets);
        }
        if (player.equals("doc3")) {
            setDocSetup(doc3TaxiTickets, doc3BusTickets, doc3MetroTickets);
        }
        if (player.equals("doc4")) {
            setDocSetup(doc4TaxiTickets, doc4BusTickets, doc4MetroTickets);
        }
    }
    private void setMrXSetup() {
        taxiText.setText(mrXTaxiTickets + "x Taxi tickets");
        taxiText.setVisible(true);
        taxiButton.setVisible(true);
        busText.setText(mrXBusTickets + "x Bus tickets");
        busText.setVisible(true);
        busButton.setVisible(true);
        metroText.setText(mrXMetroTickets + "x Metro tickets");
        metroText.setVisible(true);
        metroButton.setVisible(true);
        blackText.setText(mrXBlackTickets + "x Black tickets");
        blackText.setVisible(true);
        blackButton.setVisible(true);
    }
    private void setDocSetup(Integer docTaxi, Integer docBus, Integer docMetro) {
        taxiText.setText(docTaxi + "x Taxi tickets");
        taxiText.setVisible(true);
        taxiButton.setVisible(true);
        busText.setText(docBus + "x Bus tickets");
        busText.setVisible(true);
        busButton.setVisible(true);
        metroText.setText(docMetro + "x Metro tickets");
        metroText.setVisible(true);
        metroButton.setVisible(true);
        blackText.setVisible(false);
        blackButton.setVisible(false);
    }
    private void setPositions(Integer[] positions) {
        mrXCurrPos = positions[0];
        doc1CurrPos = positions[1];
        doc2CurrPos = positions[2];
        doc3CurrPos = positions[3];
        doc4CurrPos = positions[4];
        double x = numAndPoint.get(mrXCurrPos).getLayoutX();
        double y = numAndPoint.get(mrXCurrPos).getLayoutY();
        mrX.setLayoutX(x);
        mrX.setLayoutY(y);
        mrX.setVisible(true);
        x = numAndPoint.get(doc1CurrPos).getLayoutX();
        y = numAndPoint.get(doc1CurrPos).getLayoutY();
        doc1.setLayoutX(x);
        doc1.setLayoutY(y);
        doc1.setVisible(true);
        x = numAndPoint.get(doc2CurrPos).getLayoutX();
        y = numAndPoint.get(doc2CurrPos).getLayoutY();
        doc2.setLayoutX(x);
        doc2.setLayoutY(y);
        doc2.setVisible(true);
        x = numAndPoint.get(doc3CurrPos).getLayoutX();
        y = numAndPoint.get(doc3CurrPos).getLayoutY();
        doc3.setLayoutX(x);
        doc3.setLayoutY(y);
        doc3.setVisible(true);
        x = numAndPoint.get(doc4CurrPos).getLayoutX();
        y = numAndPoint.get(doc4CurrPos).getLayoutY();
        doc4.setLayoutX(x);
        doc4.setLayoutY(y);
        doc4.setVisible(true);
        initPlayerSync();
        initPlayerCordsSync();
    }
    private void updatePositionVariable (Integer key) {
        if (player.equals("mrx")) {
            mrXCurrPos = key;
            mrX.setVisible(false);
        }
        if (player.equals("doc1")) {
            doc1CurrPos = key;
            mrX.setVisible(false);
        }
        if (player.equals("doc2")) {
            doc2CurrPos = key;
            mrX.setVisible(false);
        }
        if (player.equals("doc3")) {
            doc3CurrPos = key;
            mrX.setVisible(false);
        }
        if (player.equals("doc4")) {
            doc4CurrPos = key;
            mrX.setVisible(true);
        }
    }
    private void updateOnMapPosition() {
        double x = numAndPoint.get(playerAndMapCordsSync.get(player)).getLayoutX();
        double y = numAndPoint.get(playerAndMapCordsSync.get(player)).getLayoutY();
        playerAndMapSync.get(player).setLayoutX(x);
        playerAndMapSync.get(player).setLayoutY(y);
    }
    @FXML
    private void taxiPressed() {
        busButton.setSelected(false);
        metroButton.setSelected(false);
        blackButton.setSelected(false);
        mode = "taxi";
        Integer pos = playerAndMapCordsSync.get(player);
        List<Integer> availableStations = new ArrayList<>(Graphs.neighborListOf(pathsGraph, pos));
        deleteDiffWeights(availableStations, 1, pos);
        for (int i = 1; i <= 199; i++) {
            numAndPoint.get(i).setDisable(true);
            numAndPoint.get(i).setVisible(false);
        }
        for (Integer availableStation : availableStations) {
            numAndPoint.get(availableStation).setVisible(true);
            numAndPoint.get(availableStation).setDisable(false);
        }
    }
    @FXML
    private void busPressed() {
        taxiButton.setSelected(false);
        metroButton.setSelected(false);
        blackButton.setSelected(false);
        mode = "bus";
        Integer pos = playerAndMapCordsSync.get(player);
        List<Integer> availableStations = new ArrayList<>(Graphs.neighborListOf(pathsGraph, pos));
        deleteDiffWeights(availableStations, 2, pos);
        for (int i = 1; i <= 199; i++) {
            numAndPoint.get(i).setDisable(true);
            numAndPoint.get(i).setVisible(false);
        }
        for (Integer availableStation : availableStations) {
            numAndPoint.get(availableStation).setVisible(true);
            numAndPoint.get(availableStation).setDisable(false);
        }
    }
    @FXML
    private void metroPressed() {
        busButton.setSelected(false);
        taxiButton.setSelected(false);
        blackButton.setSelected(false);
        mode = "metro";
        Integer pos = playerAndMapCordsSync.get(player);
        List<Integer> availableStations = new ArrayList<>(Graphs.neighborListOf(pathsGraph, pos));
        deleteDiffWeights(availableStations, 3, pos);
        for (int i = 1; i <= 199; i++) {
            numAndPoint.get(i).setDisable(true);
            numAndPoint.get(i).setVisible(false);
        }
        for (Integer availableStation : availableStations) {
            numAndPoint.get(availableStation).setVisible(true);
            numAndPoint.get(availableStation).setDisable(false);
        }
    }
    @FXML
    private void blackPressed() {
        busButton.setSelected(false);
        metroButton.setSelected(false);
        taxiButton.setSelected(false);
        mode = "black";
        Integer pos = playerAndMapCordsSync.get(player);
        List<Integer> availableStations = new ArrayList<>(Graphs.neighborListOf(pathsGraph, pos));
        for (int i = 1; i <= 199; i++) {
            numAndPoint.get(i).setDisable(true);
            numAndPoint.get(i).setVisible(false);
        }
        for (Integer availableStation : availableStations) {
            numAndPoint.get(availableStation).setVisible(true);
            numAndPoint.get(availableStation).setDisable(false);
        }
    }

    private List<Integer> deleteDiffWeights(List<Integer> stations, int weight, int pos) {
        List<Integer> deletes = new ArrayList<>();
        boolean check = false;

        for (int i = 0; i < stations.size(); i++) {
            DefaultWeightedEdge[] edges = pathsGraph.getAllEdges(pos, stations.get(i)).toArray(new DefaultWeightedEdge[]{});
            for (DefaultWeightedEdge edge : edges) {
                if (pathsGraph.getEdgeWeight(edge) == weight) {
                    check = true;
                }
            }
            if (!check) {
                deletes.add(i);
            }
            check = false;
        }

        if (deletes.size() != 0) {
            for (int i = 0; i < deletes.size(); i++) {
                stations.remove(deletes.get(i) - i);
            }
        }

        return stations;
    }

    private void updateTickets() {
        if (mode.equals("taxi")) {
            if (player.equals("mrx")) {
                mrXTaxiTickets -= 1;
                Text text = new Text("Mister X used Taxi ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc1")) {
                doc1TaxiTickets -= 1;
                mrXTaxiTickets += 1;
                Text text = new Text("Detective 1 used Taxi ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc2")) {
                doc2TaxiTickets -= 1;
                mrXTaxiTickets += 1;
                Text text = new Text("Detective 2 used Taxi ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc3")) {
                doc3TaxiTickets -= 1;
                mrXTaxiTickets += 1;
                Text text = new Text("Detective 3 used Taxi ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc4")) {
                doc4TaxiTickets -= 1;
                mrXTaxiTickets += 1;
                Text text = new Text("Detective 4 used Taxi ticket\r\n");
                logArea.getChildren().addAll(text);
            }
        }
        if (mode.equals("bus")) {
            if (player.equals("mrx")) {
                mrXBusTickets -= 1;
                Text text = new Text("Mister X used Bus ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc1")) {
                doc1BusTickets -= 1;
                mrXBusTickets += 1;
                Text text = new Text("Detective 1 used Bus ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc2")) {
                doc2BusTickets -= 1;
                mrXBusTickets += 1;
                Text text = new Text("Detective 2 used Bus ticket\r\n");
                logArea.getChildren().addAll(text);

            }
            if (player.equals("doc3")) {
                doc3BusTickets -= 1;
                mrXBusTickets += 1;
                Text text = new Text("Detective 3 used Bus ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc4")) {
                doc4BusTickets -= 1;
                mrXBusTickets += 1;
                Text text = new Text("Detective 4 used Bus ticket\r\n");
                logArea.getChildren().addAll(text);
            }
        }
        if (mode.equals("metro")) {
            if (player.equals("mrx")) {
                mrXMetroTickets -= 1;
                Text text = new Text("Mister X used Metro ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc1")) {
                doc1MetroTickets -= 1;
                mrXMetroTickets += 1;
                Text text = new Text("Detective 1 used Metro ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc2")) {
                doc2MetroTickets -= 1;
                mrXMetroTickets += 1;
                Text text = new Text("Detective 2 used Metro ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc3")) {
                doc3MetroTickets -= 1;
                mrXMetroTickets += 1;
                Text text = new Text("Detective 3 used Metro ticket\r\n");
                logArea.getChildren().addAll(text);
            }
            if (player.equals("doc4")) {
                doc4MetroTickets -= 1;
                mrXMetroTickets += 1;
                Text text = new Text("Detective 4 used Metro ticket\r\n");
                logArea.getChildren().addAll(text);
            }
        }
        if (mode.equals("black")) {
            mrXBlackTickets -= 1;
            Text text = new Text("Mister X used Black ticket\r\n");
            logArea.getChildren().addAll(text);
        }
    }
    private void checkEndOfGame() {
        if (turn == 19) {
            Text text = new Text("Mister X won the game!\r\n");
            text.setFill(Color.RED);
            logArea.getChildren().addAll(text);
            clearGameScreen();
        }
        if (Objects.equals(doc1CurrPos, mrXCurrPos) || Objects.equals(doc2CurrPos, mrXCurrPos) ||
                Objects.equals(doc3CurrPos, mrXCurrPos) || Objects.equals(doc4CurrPos, mrXCurrPos)) {
            Text text = new Text("Detectives won the game!\r\n");
            text.setFill(Color.RED);
            logArea.getChildren().addAll(text);
            clearGameScreen();
        }
    }
    private void clearGameScreen() {
        modeChoose.setVisible(true);
        taxiText.setVisible(false);
        taxiButton.setVisible(false);
        busText.setVisible(false);
        busButton.setVisible(false);
        metroText.setVisible(false);
        metroButton.setVisible(false);
        blackText.setVisible(false);
        blackButton.setVisible(false);
        for (int i = 1; i <= 199; i++) {
            numAndPoint.get(i).setDisable(true);
            numAndPoint.get(i).setVisible(false);
        }
        mrX.setVisible(false);
        doc1.setVisible(false);
        doc2.setVisible(false);
        doc3.setVisible(false);
        doc4.setVisible(false);
    }
}