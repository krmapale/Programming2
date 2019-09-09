package fxKrmapale;


import java.io.PrintStream;
import java.net.URL;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import fi.jyu.mit.fxgui.Dialogs;
import fi.jyu.mit.fxgui.ListChooser;
//import fi.jyu.mit.fxgui.StringGrid;
import fi.jyu.mit.fxgui.TextAreaOutputStream;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import tietorakenne.Kala;
import tietorakenne.Kalastaja;
import tietorakenne.PyyntiTieto;
import tietorakenne.Rekisteri;
import tietorakenne.SailoException;

/**
 * Ohjelman koodi, t‰ll‰ hetkell‰
 * sis‰lt‰‰ vain dialogeja joissa kerrotaan,
 * ett‰ mit‰‰n ei osata tehd‰
 * @author Kristian Leirimaa
 * @version 18.2.2016
 */
public class KrmapaleGUIController implements Initializable {

	@FXML private ComboBox<String> cbKentat;
	@FXML private ComboBox<PyyntiTieto> cbPyyntikentat;
	@FXML private ComboBox<Kala> cbKalakentat;
	@FXML private Label labelVirhe;
	@FXML private GridPane gridKalastaja;
	@FXML private ScrollPane panelKalastaja;
	//@FXML private ListChooser chooserPyyntitiedot;
	@FXML private ListChooser chooserKalastajat;
	@FXML private TextField textHaku;
	@FXML private GridPane gridPyyntitiedot;
	@FXML private GridPane gridKalat;

	@Override
	public void initialize(URL url, ResourceBundle bundle) {
		alusta();      
	}

	// ei k‰ytˆss‰ viel‰
	@FXML private void handleHakuteksti(){
		textHaku.getText();	
	}

	@FXML private void handleHakuehto(){
		hae(0);
	}

	@FXML private void handleUusiKalastaja() {
		uusiKalastaja();
	}

	@FXML private void handleUusiPyyntiTieto() {
		uusiPyyntitieto();
	}

	@FXML private void handleUusiKala() {
		uusiKala();
	}

	@FXML private void handleTallenna(){
		tallenna(true);
	}

	@FXML private void handlePoistaKalastaja(){
		poistaKalastaja();
	}

	@FXML private void handlePoistaPyynti(){
		poistaPyyntitieto();
	}

	@FXML private void handlePoistaKala(){
		poistaKala();
	}

	@FXML private void handleSulje(){
		tallenna(false);
		Platform.exit();
	}

	@FXML private void handleApua(){
		Dialogs.showMessageDialog("Viel‰ ei osata auttaa!");
	}

	@FXML private void handleTietoja(){
		Dialogs.showMessageDialog("Viel‰ ei osata antaa enemp‰‰ tietoja");
	}

	//===========================================================================================    
	// T‰st‰ eteenp‰in ei k‰yttˆliittym‰‰n suoraan liittyv‰‰ koodia    

	private Rekisteri rekisteri;
	private Kalastaja kalastajaKohdalla;
	private PyyntiTieto pyyntiKohdalla;
	private EditoitavaKalastaja editoitavaKalastaja = new EditoitavaKalastaja();
	private Kalastaja apukalastaja = new Kalastaja(); // kalastaja jolta voidaan kysell‰ tietoja
	private PyyntiTieto apupyynti = new PyyntiTieto();
	private Kala apukala = new Kala();
	private ListView<Kalastaja> listKalastajat = new ListView<Kalastaja>(); 
	private ObservableList<Kalastaja> listdataKalastajat = FXCollections.observableArrayList();
	private TextField[] edits;
	private TextField[] editsP;
	private TextField[] editsK;


	/**
	 * Luokka jossa on tallessa editoitavaksi otettu tai uusi kalastaja
	 * ja sen alkuper‰inen arvo.  Alkuper‰isest‰ tehd‰‰n aluksi klooni
	 * ja kloonia muokataan.  Sitten voidaan verrata tarvittaessa ett‰
	 * onko editointia tapahtunut ja tarvitaanko tallennusta.
	 * Ja jos ei haluta tallennusta, niin ei tarvita mit‰‰n undo, koska
	 * muokkaus on koskenut kopiota.  
	 * Haasteen aiheuttaa ListView, jossa kalastajan vaihtaminen voi johtaa siihen,
	 * ett‰ editoitavana oleva kalastaja pit‰isi ehk‰ tallentaa ja t‰m‰ saattaa
	 * johtaa listan virkistyksentarpeeseen.  Mutta listaa ei saa virkist‰‰
	 * silloin kun ollaan sen tapahtumassa.  T‰m‰n takia tallennuksessa
	 * joudutaan temppuilemaan saakoHakea kanssa.  Ja ListView joutuu
	 * viiv‰stytt‰m‰‰n kalastajan n‰yt‰mist‰.
	 */
	private class EditoitavaKalastaja {
		private Kalastaja editoitava;
		private Kalastaja alkuperainen;

		public EditoitavaKalastaja() { editoitava = null; }

		/**
		 * Asetetaan editoitava kalastaja. Jos on olemassa entinen editoiva
		 * niin pit‰‰ tarkistaa onko se muuttunut ja pit‰‰kˆ se tallentaa.
		 * @param kalastaja uusi editoitava kalastaja
		 * @param saakoHakea saako hakea listan uudelleen
		 * @return asetettu editoitava kalastaja, voi olla myˆs null.
		 */
		public boolean tallennaJaAseta(Kalastaja kalastaja, boolean saakoHakea) {
			boolean oliko = tarkistaMuutos(saakoHakea);

			this.alkuperainen = kalastaja;
			if ( kalastaja == null ) editoitava = null;
			else try {
				this.editoitava = alkuperainen.clone();
			} catch (CloneNotSupportedException e) {
				// pit‰isi tulla aina Kalastaja
			}
			return oliko;
		}


		private boolean tarkistaMuutos(boolean saakoHakea) {
			if ( editoitava == null ) return  false;
			if ( !muuttunut() ) return false;
			if ( !Dialogs.showQuestionDialog("Kalastajan tiedot muuttuneet", "Tallennetaanko?", "Kyll‰", "Ei") )
				return false; 
			tallenna(saakoHakea);
			editoitava = null;
			return true;
		}


		public boolean tallennaJaTyhjenna(boolean saakoHakea) { return tallennaJaAseta(null, saakoHakea); } 
		private boolean muuttunut() {  return !editoitava.equals(alkuperainen);  }
		public Kalastaja getEditoitava() {  return editoitava; }
		public boolean onkoKetaan() { return editoitava != null;  }
		public void poista() { editoitava = null; }
	}


	/**
	 * Luokka, jolla hoidellaan miten kalastaja n‰ytet‰‰n listassa 
	 */
	public static class CellKalastaja extends ListCell<Kalastaja> {
		@Override protected void updateItem(Kalastaja item, boolean empty) {
			super.updateItem(item, empty); // ilman t‰t‰ ei n‰y valinta
			setText(empty ? "" : item.getNimi());
		}
	}

	/**
	 * Luokka, jolla hoidellaan miten pyyntitieto n‰ytet‰‰n comboboxissa
	 */
	public static class CellPyynti extends ListCell<PyyntiTieto> {
		@Override protected void updateItem(PyyntiTieto item, boolean empty) {
			super.updateItem(item, empty); // ilman t‰t‰ ei n‰y valinta
			setText(empty ? "" : item.anna(2) + " " + item.anna(5));
		}
	}

	/**
	 * Luokka, jolla hoidellaan miten kala n‰ytet‰‰n comboboxissa
	 */
	public static class CellKala extends ListCell<Kala> {
		@Override protected void updateItem(Kala item, boolean empty) {
			super.updateItem(item, empty); // ilman t‰t‰ ei n‰y valinta
			setText(empty ? "" : item.anna(2) + " " + item.anna(4));
		}
	}

	/**
	 * Tekee tarvittavat muut alustukset, tyhjennetaan GridPanen 
	 * ja luodaan tilalle uudet labelit ja editit
	 * Samoin vaihdetaan kalastaja listaksi oikeata tyyppi‰ oleva lista 
	 */
	private void alusta() {
		listKalastajat.setItems(listdataKalastajat);
		gridKalastaja.getChildren().clear();

		edits = new TextField[apukalastaja.getKenttia()];

		for (int i=0, k = apukalastaja.ekaKentta(); k < apukalastaja.getKenttia(); k++, i++) {
			Label label = new Label(apukalastaja.getKysymys(k));
			gridKalastaja.add(label, 0, i);
			TextField edit = new TextField();
			edits[k] = edit;
			final int kk = k;
			edit.setOnKeyReleased( e -> kasitteleMuutosKalastajaan(kk,(TextField)(e.getSource())));
			gridKalastaja.add(edit, 1, i);
		}
		cbKentat.getSelectionModel().select(0);

		cbPyyntikentat.getSelectionModel().selectFirst();
		cbPyyntikentat.setCellFactory( p -> new CellPyynti() );
		cbPyyntikentat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) naytaPyyntitieto(newValue);
			return;
		});

		cbKalakentat.getSelectionModel().selectFirst();
		cbKalakentat.setCellFactory( p -> new CellKala() );
		cbKalakentat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue != null) naytaKala(newValue);
			return;
		});

		BorderPane parent = (BorderPane)chooserKalastajat.getParent();
		listKalastajat.setPrefHeight(chooserKalastajat.getPrefHeight());
		listKalastajat.setPrefWidth(chooserKalastajat.getPrefWidth());
		parent.setCenter(listKalastajat);
		listKalastajat.setCellFactory( p -> new CellKalastaja() );
		listKalastajat.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if ( !editoitavaKalastaja.tallennaJaTyhjenna(false) ) { naytaKalastaja(newValue); return; }
			Platform.runLater( () -> { // koska listan muuttumisen aikana ei saa muuttaa listaa
				hae(0);
				naytaKalastaja(newValue);
			});
		});

		gridPyyntitiedot.getChildren().clear();

		editsP = new TextField[apupyynti.getKenttia()];

		for (int i=0, k = apupyynti.ekaKentta(); k < apupyynti.getKenttia(); k++, i++) {
			Label label = new Label(apupyynti.getKysymys(k));
			gridPyyntitiedot.add(label, 0, i);
			TextField edit = new TextField();
			editsP[k] = edit;
			gridPyyntitiedot.add(edit, 1, i);
		}

		gridKalat.getChildren().clear();

		editsK = new TextField[apukala.getKenttia()];

		for (int i=0, k = apukala.ekaKentta(); k < apukala.getKenttia(); k++, i++) {
			Label label = new Label(apukala.getKysymys(k));
			gridKalat.add(label, 0, i);
			TextField edit = new TextField();
			editsK[k] = edit;
			gridKalat.add(edit, 1, i);
		}

	}

	/**
	 * @return listatsta valittu kohdalla oleva kalastaja
	 */
	private Kalastaja getKalastajaKohdalla() {
		return listKalastajat.getSelectionModel().getSelectedItem();
	}

	/**
	 * @return listatsta valittu kohdalla oleva kalastaja

	private Kalastaja getPyyntiKohdalla() {
		return cbPyyntikentat.getSelectionModel().getSelectedIndex();
	} */

	/**
	 * K‰sitell‰‰n kalastajaan tullut muutos.  Mik‰li mik‰‰n kalastaja ei ole
	 * editoitavan, niin tehd‰‰n kohdalla olevasta kopio ja editoidaan
	 * sit‰.  Jos ei ole kohdalla olevaa, niin ei sitten editoida mit‰‰n.
	 * @param k mit‰ kentt‰‰ muutos koskee
	 * @param edit muuttunut kentt‰
	 */
	protected void kasitteleMuutosKalastajaan(int k, TextField edit) {
		if (!editoitavaKalastaja.onkoKetaan()) editoitavaKalastaja.tallennaJaAseta(getKalastajaKohdalla(),true);
		if (!editoitavaKalastaja.onkoKetaan()) return; // ei ole j‰sent‰ jota muokataan

		Kalastaja editoitava = editoitavaKalastaja.getEditoitava();
		String s = edit.getText();
		if(k == 5){
			if(Pattern.matches("[a-zA-Z]+", s)){
				Dialogs.showMessageDialog("Lis‰‰ vain numeroita!");
				return;
			}
			if(s.length() < 5 || s.length() > 5){
				Dialogs.showMessageDialog("Liikaa tai liian v‰h‰n numeroita postinumerokent‰ss‰!");
				return;
			}
		}
		if(k == 6){
			if(Pattern.matches("[0-9]+", s)){
				Dialogs.showMessageDialog("Lis‰‰ vain kirjaimia!");
				return;
			}
		}
		
		String virhe = null;
		virhe = editoitava.aseta(k,s); 
		if (virhe == null) {
			Dialogs.setToolTipText(edit,"");
			edit.getStyleClass().removeAll("virhe");
			naytaVirhe(virhe);
		} else {
			Dialogs.setToolTipText(edit,virhe);
			edit.getStyleClass().add("virhe");
			naytaVirhe(virhe);
		}
	}

	/*
	 * K‰sitell‰‰n kalastajaan tullut muutos.  Mik‰li mik‰‰n kalastaja ei ole
	 * editoitavan, niin tehd‰‰n kohdalla olevasta kopio ja editoidaan
	 * sit‰.  Jos ei ole kohdalla olevaa, niin ei sitten editoida mit‰‰n.
	 * @param k mit‰ kentt‰‰ muutos koskee
	 * @param edit muuttunut kentt‰

	protected void kasitteleMuutosPyyntitietoon(int k, TextField edit) {
	    if (!editoitavaPyyntitieto.onkoKetaan()) editoitavaPyyntitieto.tallennaJaAseta(getPyyntiKohdalla(),true);
	    if (!editoitavaPyyntitieto.onkoKetaan()) return; // ei ole j‰sent‰ jota muokataan

	    PyyntiTieto editoitava = editoitavaPyyntitieto.getEditoitava();
	    String s = edit.getText();
	    String virhe = null;
	    virhe = editoitava.aseta(k,s); 
	    if (virhe == null) {
	        Dialogs.setToolTipText(edit,"");
	        edit.getStyleClass().removeAll("virhe");
	        naytaVirhe(virhe);
	    } else {
	        Dialogs.setToolTipText(edit,virhe);
	        edit.getStyleClass().add("virhe");
	        naytaVirhe(virhe);
	    }
	}
	 */

	/**
	 * N‰ytt‰‰ listasta valitun kalastajan tiedot
	 */
	private void naytaKalastaja(Kalastaja kalastaja) {
		for (int k = apukalastaja.ekaKentta(); k < apukalastaja.getKenttia(); k++) {
			String arvo = "";
			if ( kalastaja != null ) arvo = kalastaja.anna(k);
			TextField edit = edits[k];
			edit.setText(arvo);
			Dialogs.setToolTipText(edit,"");
			edit.getStyleClass().removeAll("virhe");
		}

		naytaPyyntitieto(kalastaja);
		gridKalastaja.setVisible(kalastaja != null); 
		naytaVirhe(null);
	}

	/**
	 * N‰ytet‰‰n pyyntitiedot taulukkoon.  Tyhjennet‰‰n ensin taulukko ja sitten
	 * lis‰t‰‰n siihen kaikki pyyntitiedot
	 * @param kalastaja kalastaja, jonka pyyntitiedot n‰ytet‰‰n
	 */
	private void naytaPyyntitieto(Kalastaja kalastaja) {
		if(kalastaja == null) return;
		if(rekisteri.annaPyyntiTiedot(kalastaja).size() <= 0) {
			for (int k = apukala.ekaKentta(); k < apukala.getKenttia(); k++) {
				String arvo = "";
				TextField edit = editsK[k];
				edit.setText(arvo);
				Dialogs.setToolTipText(edit,"");
				edit.getStyleClass().removeAll("virhe");
			}
			cbKalakentat.getItems().clear();
		}

		if(rekisteri.annaPyyntiTiedot(kalastaja).size() < 0) return;
		List<PyyntiTieto> pyynnit = rekisteri.annaPyyntiTiedot(kalastaja);

		for (int k = apupyynti.ekaKentta(); k < apupyynti.getKenttia(); k++) {
			String arvo = "";
			if ( !pyynnit.isEmpty() ) arvo = pyynnit.get(pyynnit.size()-1).anna(k);
			TextField edit = editsP[k];
			edit.setText(arvo);
			Dialogs.setToolTipText(edit,"");
			edit.getStyleClass().removeAll("virhe");

		}

		naytaPyynticombo(kalastaja);
		if(pyynnit.size() > 0) naytaKala(pyynnit.get(pyynnit.size()-1));
		if(pyynnit.size() > 0) naytaKalacombo(pyynnit.get(pyynnit.size()-1)); 
		gridPyyntitiedot.setVisible(true);
		naytaVirhe(null);
	}

	/**
	 * N‰ytet‰‰n kalat taulukkoon.  Tyhjennet‰‰n ensin taulukko ja sitten
	 * lis‰t‰‰n siihen kaikki kalat
	 * @param kalastaja kalastaja, jonka kalat n‰ytet‰‰n
	 */
	private void naytaKala(PyyntiTieto pyynti) {
		if(pyynti == null) return;
		List<Kala> kalat = rekisteri.annaKalanTiedot(pyynti); 

		for (int k = apukala.ekaKentta(); k < apukala.getKenttia(); k++) {
			String arvo = "";
			if ( !kalat.isEmpty() ) arvo = kalat.get(kalat.size()-1).anna(k);
			TextField edit = editsK[k];
			edit.setText(arvo);
			Dialogs.setToolTipText(edit,"");
			edit.getStyleClass().removeAll("virhe");

		}

		naytaKalacombo(pyynti); 
		gridKalat.setVisible(true);
		naytaVirhe(null);
	}

	/**
	 * N‰ytet‰‰n pyyntitiedot taulukkoon.  Tyhjennet‰‰n ensin taulukko ja sitten
	 * lis‰t‰‰n siihen kaikki pyyntitiedot
	 * @param kalastaja kalastaja, jonka pyyntitiedot n‰ytet‰‰n
	 */
	private void naytaPyyntitieto(PyyntiTieto pyyntitieto) {

		for (int k = apupyynti.ekaKentta(); k < apupyynti.getKenttia(); k++) {
			String arvo = "";
			if(pyyntitieto != null) {
				arvo = pyyntitieto.anna(k);
			}
			TextField edit = editsP[k];
			edit.setText(arvo);
			Dialogs.setToolTipText(edit,"");
			edit.getStyleClass().removeAll("virhe");

		}

		naytaKala(pyyntitieto);
	}

	/**
	 * N‰ytet‰‰n pyyntitiedot taulukkoon.  Tyhjennet‰‰n ensin taulukko ja sitten
	 * lis‰t‰‰n siihen kaikki pyyntitiedot
	 * @param kalastaja kalastaja, jonka pyyntitiedot n‰ytet‰‰n
	 */
	private void naytaKala(Kala kala) {

		for (int k = apukala.ekaKentta(); k < apukala.getKenttia(); k++) {
			String arvo = "";
			if(kala != null) {
				arvo = kala.anna(k);
			}
			TextField edit = editsK[k];
			edit.setText(arvo);
			Dialogs.setToolTipText(edit,"");
			edit.getStyleClass().removeAll("virhe");

		}
	}

	/**
	 * n‰ytet‰‰n kalastajan pyyntitiedot comboboxissa
	 * @param kalastaja tietty kalastaja
	 */
	private void naytaPyynticombo(Kalastaja kalastaja){
		if(kalastaja != null) {
			cbPyyntikentat.getItems().clear();
			cbPyyntikentat.getItems().addAll(rekisteri.annaPyyntiTiedot(kalastaja));
		}
	}

	/**
	 * n‰ytet‰‰n pyyntitiedon sis‰lt‰m‰t kalat comboboxissa
	 * @param pyyntitieto tietty pyyntitieto
	 */
	private void naytaKalacombo(PyyntiTieto pyyntitieto){
		if(pyyntitieto != null) {
			cbKalakentat.getItems().clear();
			cbKalakentat.getItems().addAll(rekisteri.annaKalanTiedot(pyyntitieto));
		}
	}

	/**
	 * Tulostaa tietyn kalastajan pyyntitiedot
	 * @param os tietovirta johon tulostetaan
	 * @param kalastaja viite kalastajaan
	 */
	public void tulostaPyyntitiedot(PrintStream os, final Kalastaja kalastaja) {
		List<PyyntiTieto> pyyntitiedot = rekisteri.annaPyyntiTiedot(kalastaja);
		for (PyyntiTieto pyynti:pyyntitiedot) 
			pyynti.tulosta(os);     
	}

	/**
	 * Tulostaa kalastajan tiedot
	 * @param os tietovirta johon tulostetaan
	 * @param kalastaja tulostettava kalastaja
	 */
	public void tulosta(PrintStream os, final Kalastaja kalastaja) {
		os.println("----------------------------------------------");
		kalastaja.tulosta(os);
		os.println("----------------------------------------------");
		List<PyyntiTieto> pyyntitiedot;
		pyyntitiedot = rekisteri.annaPyyntiTiedot(kalastaja);
		for (PyyntiTieto pyynti:pyyntitiedot){  
			pyynti.tulosta(os);
			os.println("----------------------------------------------");
			List<Kala> kalat = rekisteri.annaKalanTiedot(pyynti);
			for(Kala kala: kalat){
				kala.tulosta(os);
				os.println("----------------------------------------------");
			}
		}

	}

	private void naytaVirhe(String virhe) {
		if ( virhe == null || virhe.isEmpty() ) {
			labelVirhe.setText("");
			labelVirhe.getStyleClass().removeAll("virhe");
			return;
		}
		labelVirhe.setText(virhe);
		labelVirhe.getStyleClass().add("virhe");
	}

	/**
	private void setTitle(String title) {
		ModalController.getStage(textHaku).setTitle(title);
	}
	 */

	/**
	 * talennus aliohjelma
	 * @param saakoHakea true jos saa false jos ei
	 * @return palautetaan null
	 */
	public String tallenna(boolean saakoHakea) {
		try {
			if(editoitavaKalastaja.onkoKetaan()){
				Kalastaja editoitava = editoitavaKalastaja.getEditoitava();
				editoitavaKalastaja.poista();
				rekisteri.korvaaTaiLisaa(editoitava);
				final int kalastajaID = editoitava.getID();
				if ( saakoHakea ) hae(kalastajaID);
			}
			rekisteri.tallenna();
			Dialogs.showMessageDialog("Tallennus onnistui!");
			return null;
		} catch (SailoException ex) {
			Dialogs.showMessageDialog("Talletuksessa ongelmia! " + ex.getMessage());
			return ex.getMessage();
		}
	}


	/**
	 * Alustaa rekisterin lukemalla sen valitun nimisest‰ tiedostosta
	 * @param nimi tiedosto josta rekisterin tiedot luetaan
	 * @return null jos onnistuu, muuten virhe tekstin‰
	 */
	protected String lueTiedosto(String nimi) {
		try {
			rekisteri.lueTiedostosta(nimi);
			hae(0);
			return null;
		} catch (SailoException e) {
			hae(0);
			String virhe = e.getMessage(); 
			if ( virhe != null ) Dialogs.showMessageDialog(virhe);
			return virhe;
		}
	}


	/**
	 * Tarkistetaan onko tallennus tehty
	 * @return true jos saa sulkea sovelluksen, false jos ei
	 */
	public boolean voikoSulkea() {
		editoitavaKalastaja.tallennaJaTyhjenna(false);
		tallenna(false);
		return true;
	}

	/**
	 * Hakee kalastajien tiedot listaan
	 * @param kalastajaID kalastajan tunnusnumero
	 */
	protected void hae(int kalastajaID) {

		int kID = kalastajaID; 
		if ( kalastajaID <= 0 ) {
			Kalastaja kohdalla = getKalastajaKohdalla();
			if ( kohdalla != null ) kID = kohdalla.getID();
		}

		int k = cbKentat.getSelectionModel().getSelectedIndex() + apukalastaja.ekaKentta();
		String ehto = textHaku.getText(); 
		if (ehto.indexOf('*') < 0) ehto = "*" + ehto + "*";

		naytaVirhe(null);

		listdataKalastajat.clear();
		listKalastajat.setItems(null);

		int index = 0;
		Collection<Kalastaja> kalastajat;
		try {
			kalastajat = rekisteri.etsi(ehto, k);
			int i = 0;
			for (Kalastaja kalastaja:kalastajat) {
				if (kalastaja.getID() == kID) index = i;
				listdataKalastajat.add(kalastaja);
				i++;
			}
		} catch (SailoException ex) {
			Dialogs.showMessageDialog("Kalastajan hakemisessa ongelmia! " + ex.getMessage());
		}
		listKalastajat.setItems(listdataKalastajat);
		listKalastajat.getSelectionModel().select(index); // t‰st‰ tulee muutosviesti joka n‰ytt‰‰ kalastajan

	}

	/**
	 * lis‰t‰‰n uusi kala pyyntitietoon
	 */
	public void uusiKala(){
		try {
			kalastajaKohdalla = getKalastajaKohdalla();
			if(rekisteri.annaPyyntiTiedot(kalastajaKohdalla).size() < 1) return;
			List <PyyntiTieto> apuLista = rekisteri.annaPyyntiTiedot(kalastajaKohdalla);
			pyyntiKohdalla = apuLista.get(apuLista.size() - 1);

			Kala kala = new Kala();
			kala.lisaaKalaTiedot(pyyntiKohdalla.getPyyntiID());
			rekisteri.lisaa(kala);
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		}
		hae(kalastajaKohdalla.getID()); 
	}


	/**
	 * Luo uuden kalastajan 
	 */
	protected void uusiKalastaja() {
		Kalastaja uusi = new Kalastaja();
		uusi.luoArvot();
		/*
		editoitavaKalastaja.tallennaJaAseta(uusi,true);
		listKalastajat.getSelectionModel().clearSelection();
		naytaKalastaja(uusi);
		 */
		try {
			rekisteri.lisaa(uusi);
		} catch (SailoException e) {
			Dialogs.showMessageDialog("Ongelmia uuden luomisessa " + e.getMessage());
			return;
		}
		hae(uusi.getID());
	}


	/**
	 * Luo uuden pyyntitiedon kalastajalle
	 */
	public void uusiPyyntitieto() {
		kalastajaKohdalla = getKalastajaKohdalla();
		if ( kalastajaKohdalla == null ) return; 
		try {
			PyyntiTieto pyynti = new PyyntiTieto(); 
			pyynti.luoarvot(kalastajaKohdalla.getID()); 
			rekisteri.lisaa(pyynti);
		} catch (SailoException e) {
			System.out.println(e.getMessage());
		} 

		hae(kalastajaKohdalla.getID()); 
	}


	/**
	 * Poistetaan esill‰ oleva pyyntitieto 
	 */
	private void poistaPyyntitieto() {
		int indeksi = cbPyyntikentat.getSelectionModel().getSelectedIndex();
		if(indeksi < 0) indeksi = cbPyyntikentat.getItems().size()-1; 
		PyyntiTieto pyynti = cbPyyntikentat.getItems().get(indeksi);
		if ( pyynti == null ) return;
		if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko pyyntitieto: " + pyynti.anna(2), "Kyll‰", "Ei") )
			return;
		rekisteri.poistaPyyntitieto(pyynti);
		naytaPyyntitieto(getKalastajaKohdalla());
	}


	/**
	 * Poistetaan listalta valittu kalastaja
	 */
	private void poistaKalastaja() {
		Kalastaja kalastaja = getKalastajaKohdalla();
		if ( kalastaja == null ) return;
		if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko kalastaja: " + kalastaja.getNimi(), "Kyll‰", "Ei") )
			return;
		rekisteri.poista(kalastaja.getID());
		int index = listKalastajat.getSelectionModel().getSelectedIndex();
		hae(0);
		listKalastajat.getSelectionModel().select(index);
	}

	/**
	 * Poistetaan tietty kala
	 */
	private void  poistaKala(){
		int indeksi = cbKalakentat.getSelectionModel().getSelectedIndex();
		if(indeksi < 0) indeksi = cbKalakentat.getItems().size()-1; 
		Kala kala = cbKalakentat.getItems().get(indeksi);
		if ( kala == null ) return;
		if ( !Dialogs.showQuestionDialog("Poisto", "Poistetaanko kala: " + kala.anna(2) + ", " + kala.anna(4), "Kyll‰", "Ei") )
			return;
		rekisteri.poistaKala(kala);
		naytaPyyntitieto(getKalastajaKohdalla());
	}


	/**
	 * @param rekisteri Rekisteri jota k‰ytet‰‰n t‰ss‰ k‰yttˆliittym‰ss‰
	 */
	public void setRekisteri(Rekisteri rekisteri) {
		this.rekisteri = rekisteri;
		//naytaKalastaja();
	}

	/**
	 * Tulostaa listassa olevat kalastajat tekstialueeseen
	 * @param text alue johon tulostetaan
	 */
	public void tulostaValitut(TextArea text) {
		try (PrintStream os = TextAreaOutputStream.getTextPrintStream(text)) {
			os.println("Tulostetaan kaikki kalastajat");
			for (int i = 0; i < rekisteri.getKalastajia(); i++) {
				Kalastaja kalastaja = rekisteri.annaKalastaja(i);
				tulosta(os, kalastaja);
				os.println("\n\n");
			}
		}
	}


}

