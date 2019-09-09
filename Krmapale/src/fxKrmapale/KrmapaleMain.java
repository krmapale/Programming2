package fxKrmapale;

import javafx.application.Application;
import javafx.stage.Stage;
import tietorakenne.Rekisteri;
import fxKrmapale.KrmapaleGUIController;
import javafx.scene.Scene;
//import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.fxml.FXMLLoader;


/**
 * Ohjelman k‰ynnist‰v‰ p‰‰ohjelma
 * @author Kristian Leirimaa
 * @version 18.2.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class KrmapaleMain extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {


			FXMLLoader ldr = new FXMLLoader(getClass().getResource("KrmapaleGUIView.fxml"));
			final Pane root = (Pane)ldr.load();
			final KrmapaleGUIController krmapaleCtrl = (KrmapaleGUIController)ldr.getController();

			final Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("krmapale.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Krmapale");

			primaryStage.setOnCloseRequest((event) -> {
				if ( !krmapaleCtrl.voikoSulkea() ) event.consume();
			});

			Rekisteri rekisteri = new Rekisteri();
			krmapaleCtrl.setRekisteri(rekisteri);
			primaryStage.show();
			String tiedNimi = "Kalarekisteri";
			krmapaleCtrl.lueTiedosto(tiedNimi);


		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * k‰ynnistet‰‰n ohjelma
	 * @param args k‰ynnistyksen parametrilista
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
