package tietorakenne;

import java.io.OutputStream;
import java.io.PrintStream;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Yksittäisen kalan tietoja käsittelevä luokka
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class Kala {

	private String kalalaji;
	private String sukupuoli;
	private String paino;
	private String pituus;

	private int pyyntiID;
	private int kalaID;
	private static int seuraavanID = 1;

	/**
	 * alustus
	 */
	public Kala(){

	}
	/**
	 * asetetaan kalalle pyynti ID, joka on tuotu parametrinä
	 * @param pID pyyntiID
	 */
	public Kala(int pID){
		this.pyyntiID = pID;
	}
	
	/**
	 * palauttaa kalastajan kenttien lukumäärän
	 * @return kenttien lkm
	 */
	public int getKenttia(){
		return 6;
	}
	
	/**
	 * Eka kenttä joka on mielekäs kysyttäväksi
	 * @return ekan kentän indeksi
	 */
	public int ekaKentta(){
		return 0;
	} 

	/**
	 * annetaan kalalle tunnistusnumero
	 * @return palautetaan kalan ID
	 */
	public int asetaID(){
		int ID = seuraavanID;
		seuraavanID++;
		return ID;
	}

	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa että
	 * seuraava numero on aina suurempi kuin tähän mennessä suurin.
	 * @param nr asetettava tunnusnumero
	 */
	private void setID(int nr) {
		pyyntiID = nr;
		if ( pyyntiID >= seuraavanID ) seuraavanID = pyyntiID + 1;
	}
	
	/**
	 * palautetaan pyyntitiedon tunnistusnumero
	 * @return pyynti ID
	 */
	public int getPyyntiID(){
		return pyyntiID;
	}

	/**
	 * palautetaan kalastajan tunnusnumero
	 * @return kala ID
	 */
	public int getKalaID(){
		return kalaID;
	}



	/**
	 * väliaikainen apumetodi kalan tietojen täyttämiseksi 
	 * @param ID pyyntitiedon ID
	 * @example
	 * <pre name="test"> 
	 * Kala kala1 = new Kala();
	 * kala1.lisaaKalaTiedot(1);
	 * kala1.getKalaID() === 1;
	 * kala1.getPyyntiID() === 1;
	 * 
	 * Kala kala2 = new Kala();
	 * kala2.lisaaKalaTiedot(1);
	 * kala2.getKalaID() === 2;
	 * kala2.getPyyntiID() === 1;
	 * 
	 * Kala kala3 = new Kala();
	 * kala3.lisaaKalaTiedot(2);
	 * kala3.getKalaID() === 3;
	 * kala3.getPyyntiID() === 2;
	 * 
	 * Kala kala4 = new Kala();
	 * kala4.lisaaKalaTiedot(4);
	 * kala4.getKalaID() === 4;
	 * kala4.getPyyntiID() === 4;
	 * </pre>
	 */
	public void lisaaKalaTiedot(int ID){
		this.kalalaji = rndKala();
		this.sukupuoli = rndSukupuoli();
		this.paino = rndLuku(100, 950) + "g";
		this.pituus = rndLuku(10, 40) + "cm";
		if(kalaID > 0) kalaID = getKalaID(); 
		else this.kalaID = asetaID();
		this.pyyntiID = ID;

	}

	/**
	 * väliaikainen apumetodi pituuden luomiselle
	 * @return
	 */
	private int rndLuku(int ala, int yla) {

		return Kalastaja.rand(ala, yla);
	}

	/**
	 * väliaikainen apumetodi
	 * @return
	 */
	private String rndSukupuoli() {
		@SuppressWarnings("hiding")
		String[] sukupuoli = {"Naaras", "Koiras"};
		int satunnainen = Kalastaja.rand(0, 1);
		return sukupuoli[satunnainen];
	}

	/**
	 * väliaikainen apumetodi random kalalajin lisäämiseksi
	 * @return
	 */
	private String rndKala() {
		String[] kalalajit = {"Lahna", "Ahven", "Hauki", "Särki", "Kuha"};
		int satunnainen = Kalastaja.rand(0, kalalajit.length-1);

		return kalalajit[satunnainen];
	}
	
	/**
	 * Selvittää kalastajan tiedot tolpilla erotellusta merkkijonosta
	 * ja pitää huolen siitä, että sama ID ei esiinny eri kalastajalla
	 * useaa kertaa
	 * @param rivi rivi jolta kalastajan tiedot otetaan
	 */
	public void parse(String rivi) {
	    StringBuffer sb = new StringBuffer(rivi);
	    pyyntiID = Mjonot.erota(sb, '|', pyyntiID);
	    setTunnusNro(Mjonot.erota(sb, '|', getKalaID()));
	    kalalaji = Mjonot.erota(sb, '|', kalalaji);
	    sukupuoli = Mjonot.erota(sb, '|', sukupuoli);
	    paino = Mjonot.erota(sb, '|', paino);
	    pituus = Mjonot.erota(sb, '|', pituus);
	    
	}
	
	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa että
	 * seuraava numero on aina suurempi kuin tähän mennessä suurin.
	 * @param nr asetettava tunnusnumero
	 */
	private void setTunnusNro(int nr) {
	    kalaID = nr;
	    if ( kalaID >= seuraavanID ) seuraavanID = kalaID + 1;
	}

	/**
	 * tulostetaan kalan tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out){
		out.println("pyyntiID: " + pyyntiID);
		out.println("kalanID: " + kalaID);
		out.println("Kalalaji: " + kalalaji);
		out.println("Sukupuoli: " + sukupuoli);
		out.println("Kalan paino: " + paino + "g");
		out.println("Kalan pituus: " + pituus + "cm");
		
	} 

	/**
	 * tulostetaan kalan tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os){
		tulosta(new PrintStream(os));
	}
	
	/**
	 * Antaa k:n kentän sisällön merkkijonona
	 * @param k monenenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 */
	public String anna(int k) {
	    switch ( k ) {
	    case 0:
	        return "" + getPyyntiID();
	    case 1:
	        return "" + kalaID;
	    case 2:
	        return "" + kalalaji;
	    case 3:
	        return "" + sukupuoli;
	    case 4:
	        return "" + paino;
	    case 5:
	        return "" + pituus;
	    default:
	        return "Äääliö";
	    }
	}
	
	/**
	 * Asettaa k:n kentän arvoksi parametrina tuodun merkkijonon arvon
	 * @param k kuinka monennen kentän arvo asetetaan
	 * @param jono jonoa joka asetetaan kentän arvoksi
	 * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
	 * @example
	 * <pre name="test">
	 *   Jasen jasen = new Jasen();
	 *   jasen.aseta(1,"Ankka Aku") === null;
	 *   jasen.aseta(2,"kissa") =R= "Hetu liian lyhyt"
	 *   jasen.aseta(2,"030201-1111") === "Tarkistusmerkin kuuluisi olla C"; 
	 *   jasen.aseta(2,"030201-111C") === null; 
	 *   jasen.aseta(9,"kissa") === "Liittymisvuosi väärin jono = \"kissa\"";
	 *   jasen.aseta(9,"1940") === null;
	 * </pre>
	 */
	public String aseta(int k, String jono) {
	    String tjono = jono.trim();
	    StringBuffer sb = new StringBuffer(tjono);
	    switch ( k ) {
	    case 0:
	        setID(Mjonot.erota(sb, '§', getKalaID()));
	        return null;
	    case 1:
	    	pyyntiID = Mjonot.erota(sb, '$', getPyyntiID());
	        return null;
	    case 2:
	        kalalaji = tjono;
	        return null;
	    case 3:
	        sukupuoli = tjono;
	        return null;
	    case 4:
	        paino = tjono;
	        return null;
	    case 5:
	        pituus = tjono;
	        return null;
	    default:
	        return "ÄÄliö";
	    }
	}
	
	/**
	 * Palauttaa k:tta kalan kenttää vastaavan kysymyksen
	 * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
	 * @return k:netta kenttää vastaava kysymys
	 */
	public String getKysymys(int k) {
	    switch ( k ) {
	    case 0:
	        return "Pyynnin ID";
	    case 1:
	        return "Kalan ID";
	    case 2:
	        return "Kalalaji";
	    case 3:
	        return "Sukupuoli";
	    case 4:
	        return "Paino";
	    case 5:
	        return "Pituus";
	    default:
	        return "Äääliö";
	    }
	}
	
	/**
	 * erotellaan kalan tiedot tolpilla
	 */
	@Override
	public String toString() {
	    return "" +
	    		getPyyntiID() + "|" +
	            getKalaID() + "|" +
	            kalalaji + "|" +
	            sukupuoli + "|" +
	            paino + "|" +
	            pituus + "|" ;
	            

	}

	/**
	 * kala luokan main metodi
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Kala testikala = new Kala(), testikala2 = new Kala();
		testikala.lisaaKalaTiedot(1);
		testikala2.lisaaKalaTiedot(2);
		testikala.tulosta(System.out);
		System.out.println("===================================");
		testikala2.tulosta(System.out);

	}

}
