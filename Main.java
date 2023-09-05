package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import application.Driver;

public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {

			Alert alert = new Alert(AlertType.INFORMATION);
			HBox hbox = new HBox();
			hbox.setPrefSize(500, 500);

			GridPane root = new GridPane();
			Button back = new Button("Back");
			back.setPrefSize(100, 50);
			back.setTextFill(Color.WHITE);
			back.setStyle("-fx-background-color:green;");
			back.setFont(Font.font("Verdana", 20));

			TextField pa = new TextField();
			pa.setPromptText("Path");
			pa.setFont(Font.font("Verdana", 15));
			pa.setPrefSize(300, 50);

			Button load = new Button("Load");
			load.setPrefSize(100, 50);
			load.setTextFill(Color.WHITE);
			load.setStyle("-fx-background-color:green;");
			load.setFont(Font.font("Verdana", 20));

			Label eqL = new Label("Equations");
			eqL.setFont(Font.font("Verdana", 20));

			Label fL = new Label("Files");
			fL.setFont(Font.font("Verdana", 20));

			TextArea eq = new TextArea();
			eq.setFont(Font.font("Verdana", 20));
			eq.setPrefSize(500, 500);
			eq.setMaxWidth(600);

			TextArea fil = new TextArea();
			fil.setFont(Font.font("Verdana", 20));
			fil.setPrefSize(500, 500);
			fil.setMaxWidth(600);

			root.addColumn(0, back);
			root.addColumn(1, pa);
			root.addColumn(2, load);
			root.add(eqL, 1, 5);
			root.add(eq, 1, 6);
			root.add(fL, 1, 7);
			root.add(hbox, 1, 8);

			root.setHgap(10);
			root.setVgap(10);

			load.setOnAction(e -> {
				try {

					if (pa.getText().trim().isEmpty()) {
						FileChooser fileChooser = new FileChooser();
						fileChooser.setTitle("Choose File");
						File selectedFile = fileChooser.showOpenDialog(primaryStage);
						pa.setText(selectedFile.getPath());

						alert.setContentText(Driver.FileReader(pa.getText()));
						alert.show();

					} else {

						alert.setContentText(Driver.FileReader(pa.getText().trim()));
						alert.show();

					}
					eq.setText(Driver.SolveEquations(Driver.equationList));
					ObservableList ob = FXCollections.observableList(Driver.fileList);
					ListView listV = new ListView(ob);
					hbox.getChildren().add(listV);
					listV.setOnMouseClicked(new EventHandler<MouseEvent>() {

						@Override
						public void handle(MouseEvent event) {

							String st = listV.getSelectionModel().getSelectedItem().toString();
							Driver.undoStack.push(pa.getText().trim());
							pa.clear();
							pa.setText(st);
							hbox.getChildren().remove(0);
							eq.clear();

						}
					});
				} catch (Exception t) {
					t.printStackTrace();
				}
			});

			back.setOnAction(e -> {
				try {
					if (!hbox.getChildren().isEmpty()) {
						hbox.getChildren().remove(0);
					}
					if (Driver.undoStack.isEmpty()) {
						if (!pa.getText().trim().isEmpty()) {
							pa.clear();
						};
						if (!eq.getText().trim().isEmpty()) {
							eq.clear();
						};
						alert.setContentText(Driver.undo());
						alert.show();
						
					} else {
						pa.clear();
						String st1 = Driver.undoStack.pop().getElement();
						alert.setContentText(st1);
						alert.show();
						pa.setText(st1);
						Driver.FileReader(pa.getText().trim());
						eq.setText(Driver.SolveEquations(Driver.equationList));
						ObservableList ob = FXCollections.observableList(Driver.fileList);
						ListView listV = new ListView(ob);
						hbox.getChildren().add(listV);
						listV.setOnMouseClicked(new EventHandler<MouseEvent>() {

							@Override
							public void handle(MouseEvent event) {

								String st = listV.getSelectionModel().getSelectedItem().toString();
								Driver.undoStack.push(pa.getText().trim());

								pa.clear();
								pa.setText(st);
								hbox.getChildren().remove(0);
								eq.clear();

							}
						});
					}
				} catch (Exception o) {
					o.printStackTrace();
				}
			});
			Scene sc = new Scene(root, 800, 800);
			primaryStage.setScene(sc);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}
