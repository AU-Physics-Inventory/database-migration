package edu.andrews.cas.physics.migration.parsing;

import edu.andrews.cas.physics.measurement.Quantity;
import edu.andrews.cas.physics.measurement.Unit;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ParseHelper extends Application {
    private static final Logger logger = LogManager.getLogger();
    private static ParseHelper instance;

    private final ConcurrentLinkedQueue<ParseDocument<?>> queue;
    private Stage stage;
    private ParseDocument<?> head;
    private Label parseLabel1, parseLabel2;
    private Text parseText0;
    private ChoiceBox<Unit> units;
    private Thread queuePoller;
    private boolean stopPolling = false;

    private ParseHelper() {
        this.queue = new ConcurrentLinkedQueue<>();
        Platform.startup(() -> {});
        Platform.runLater(this::init);
    }

    @Override
    public void init() {
        this.start(new Stage());
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        parseText0 = new Text();
        parseLabel1 = new Label();
        parseLabel2 = new Label();

        TextField parseField = new TextField();

        Button doneButton = new Button("Done");
        doneButton.setDisable(true);

        Label parseResult = new Label("Enter numeric values:");
        parseField.setPromptText("0.00");
        parseField.textProperty().addListener((ob, ov, nv) -> {
            if (nv.isBlank()) doneButton.setDisable(true);
            try {
                Double.parseDouble(parseField.textProperty().get());
                doneButton.setDisable(false);
            } catch (NumberFormatException e) {
                doneButton.setDisable(true);
            }
        });
        parseField.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER && !doneButton.isDisable()) doneButton.fire();
        });

        units = new ChoiceBox<>();
        units.setItems(FXCollections.observableList(Arrays.asList(Unit.values())));
        units.getSelectionModel().selectFirst();

        doneButton.setOnAction(ev -> {
            ParseDocument<?> d = queue.peek();
            if (d != null) {
                if (d.getType() == ParseDocument.DocumentType.QUANTITY) ((ParseDocument<Quantity>) d).parseAs(new Quantity(Double.parseDouble(parseField.getText()), units.getValue()));
                else ((ParseDocument<Double>) d).parseAs(Double.parseDouble(parseField.getText()));
                queue.remove();
                parseField.clear();
                units.getSelectionModel().selectFirst();
            }
        });

        grid.add(parseText0, 0, 0);
        grid.add(parseLabel1, 0, 1);
        grid.add(parseLabel2, 0, 2);
        grid.add(new Separator(), 0, 3, 2, 1);
        grid.add(parseResult, 0, 4);
        grid.add(parseField, 0, 5);
        grid.add(units, 1, 5);
        grid.add(doneButton, 1, 6);

        this.pollQueue();

        Scene scene = new Scene(grid);
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED );
    }

    public static ParseHelper getInstance() {
        if (instance == null) instance = new ParseHelper();
        return instance;
    }

    public CompletableFuture<Quantity> parseQuantity(String s, String objectName, String identifier) {
        CompletableFuture<Quantity> future = new CompletableFuture<>();
        ParseDocument<Quantity> document = new ParseDocument<>(s, future, ParseDocument.DocumentType.QUANTITY, objectName, identifier);
        queue.add(document);
        return future;
    }

    public CompletableFuture<Double> parsePrice(String s, String objectName, String identifier) {
        CompletableFuture<Double> future = new CompletableFuture<>();
        ParseDocument<Double> document = new ParseDocument<>(s, future, ParseDocument.DocumentType.PRICE, objectName, identifier);
        queue.add(document);
        return future;
    }

    private void pollQueue() {
        queuePoller = new Thread(() -> {
            while (!stopPolling) {
                ParseDocument<?> head = queue.peek();
                if (this.head == null || !this.head.equals(head)) {
                    this.head = head;
                    if (this.head != null) {
                        switch (this.head.getType()) {
                            case QUANTITY -> Platform.runLater(() -> {
                                this.parseText0.setText(String.format("QUANTITY for %s %s", head.getObjectName(), head.getIdentifier()));
                                this.parseLabel1.setText("Unable to parse the following quantity");
                                this.parseLabel2.setText(this.head.getParseString());
                                this.units.setVisible(true);
                            });
                            case PRICE -> Platform.runLater(() -> {
                                this.parseText0.setText(String.format("COST for %s %s", head.getObjectName(), head.getIdentifier()));
                                this.parseLabel1.setText("Unable to parse the following dollar amount");
                                this.parseLabel2.setText(this.head.getParseString());
                                this.units.setVisible(false);
                            });
                        }
                    }
                }
            }
            logger.info("[Parse Helper -- Queue Poller] Thread terminated successfully.");
        });
        queuePoller.start();
    }

    public boolean isStageShowing() {
        return this.stage.isShowing();
    }

    public void showStage() {
        Platform.runLater(this.stage::show);
    }

    public void hideStage() {
        Platform.runLater(this.stage::hide);
    }

    public void startPolling() {
        this.stopPolling = false;
        queuePoller.start();
    }

    public boolean isPollingStopped() {
        return stopPolling;
    }

    @Override
    public void stop() {
        this.hideStage();
        this.stopPolling = true;
    }

    public void exit() {
        Platform.exit();
    }
}