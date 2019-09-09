package tietorakenne;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Yksittäistä kalastajaa käsittelevä luokka
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class Kalastaja implements Cloneable {

	
	private int kalastajaID;
	private String nimi;
	private String syntymapaiva;
	private String sahkoposti;
	private String kotiosoite;
	private String postinumero;
	private String kaupunki;

	private static int seuraavanID = 1;
	
	/*
	private String kentat[]    = { 
		    kalastajaID + "",
			nimi,
			syntymapaiva,
			sahkoposti,
			kotiosoite,
			postinumero,
			kaupunki
		};
*/
	
	/**
	 * palauttaa kalastajan kenttien lukumäärän
	 * @return kenttien lkm
	 */
	public int getKenttia(){
		return 7;
	}
	
	/**
	 * Eka kenttä joka on mielekäs kysyttäväksi
	 * @return ekan kentän indeksi
	 */
	public int ekaKentta(){
		return 1;
	}
	
	/**
	 * annetaan kalastajalle henkilökohtainen
	 * tunnistusnumero
	 * @return palautetaan ID
	 * @example
	 * <pre name="test"> 
	 * Kalastaja kalastaja1 = new Kalastaja();
	 * kalastaja1.luoArvot();
	 * kalastaja1.getID() === 1;
	 * Kalastaja kalastaja2 = new Kalastaja();
	 * kalastaja2.luoArvot();
	 * kalastaja2.getID() === 2;
	 * Kalastaja kalastaja3 = new Kalastaja();
	 * kalastaja3.luoArvot();
	 * kalastaja3.getID() === 3;
	 * </pre>
	 */
	public int asetaID(){
		int tunnistusnumero = seuraavanID;
		seuraavanID++;
		return tunnistusnumero;
	}
	
	/**
	 * palautetaan kalastajan ID
	 * @return palautetaan kalastajan ID
	 */
	public int getID(){
		return kalastajaID;
	}
	
	/**
	 * tehdään väliaikainen apumetodi, jolla
	 * saadaan testiarvot kalastajille
	 */
	public void luoArvot(){
		if(kalastajaID > 0) kalastajaID = getID(); 
		else kalastajaID = asetaID();
		if(kalastajaID == 1) nimi = "Kalle Kalastaja";
		else nimi = "Olga Onkija";
		syntymapaiva = rndPvm();
		sahkoposti = "kalle123@kalastus.fi";
		kotiosoite = "kalakuja 2 A";
		postinumero = rndPosti();
		kaupunki = "kalakylä";

	}
	
	/**
	 * palauttaa kalastajan nimen
	 * @return kalastajan nimi
	 */
	public String getNimi(){
		return nimi;
	}
	
	/**
	 * luodaan random päivämäärä
	 * @return
	 */
	private String rndPvm(){
		Random randPvm = new Random();
		return randPvm.nextInt(30) + "." + randPvm.nextInt(12) + ".2015";
	}
	
	
	/**
	 * väliaikainen random metodi, arvotaan
	 * satunnainen kokonaisluku tietyltä väliltä
	 * @param ala alaraja
	 * @param yla yläraja
	 * @return random luku tietyltä väliltä
	 */
	public static int rand(int ala, int yla) {
		double n = (yla-ala)*Math.random() + ala;
		return (int)Math.round(n);
	}
	
	/**
	 * väliaikainen apumetodi randomin
	 * postinumeron luomiseksi
	 * @return postinumero
	 */
	public String rndPosti(){
		int postiNro = rand(10000,99999);
		return postiNro + "";
	}
	
	/**
	 * Selvittää kalastajan tiedot tolpilla erotellusta merkkijonosta
	 * ja pitää huolen siitä, että sama ID ei esiinny eri kalastajalla
	 * useaa kertaa
	 * @param rivi rivi jolta kalastajan tiedot otetaan
	 */
	public void parse(String rivi) {
	    StringBuffer sb = new StringBuffer(rivi);
	    for (int k = 0; k < getKenttia(); k++)
	        aseta(k, Mjonot.erota(sb, '|'));
		/*
	    StringBuffer sb = new StringBuffer(rivi);
	    setID(Mjonot.erota(sb, '|', getID()));
	    nimi = Mjonot.erota(sb, '|', nimi);
	    syntymapaiva = Mjonot.erota(sb, '|', syntymapaiva);
	    sahkoposti = Mjonot.erota(sb, '|', sahkoposti);
	    kotiosoite = Mjonot.erota(sb, '|', kotiosoite);
	    postinumero = Mjonot.erota(sb, '|', postinumero);
	    kaupunki = Mjonot.erota(sb, '|', kaupunki);
	    */
	}
	
	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa että
	 * seuraava numero on aina suurempi kuin tähän mennessä suurin.
	 * @param nr asetettava tunnusnumero
	 */
	private void setID(int nr) {
	    kalastajaID = nr;
	    if (kalastajaID >= seuraavanID) seuraavanID = kalastajaID + 1;
	}
	
	/**
	 * tulostetaan henkilön tiedot
	 * @param out tietovirta johon tulostetaan
	 */
	public void tulosta(PrintStream out){
		out.println("ID: " + kalastajaID);
		out.println("nimi: " + nimi);
		out.println("syntymäpäivä: " + syntymapaiva);
		out.println("sähköpostiosoite: " + sahkoposti);
		out.println("kotiosoite: " + kotiosoite);
		out.println("postinumero: " + postinumero);
		out.println("postitoimipaikka: " + kaupunki);
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
	        setID(Mjonot.erota(sb, '§', getID()));
	        return null;
	    case 1:
	        nimi = tjono;
	        return null;
	    case 2:
	        syntymapaiva = tjono;
	        return null;
	    case 3:
	        sahkoposti = tjono;
	        return null;
	    case 4:
	        kotiosoite = tjono;
	        return null;
	    case 5:
	        postinumero = tjono;
	        return null;
	    case 6:
	        kaupunki = tjono;
	        return null;
	    default:
	        return "ÄÄliö";
	    }
	}
	
	/**
	 * Antaa k:n kentän sisällön merkkijonona
	 * @param k monenenko kentän sisältö palautetaan
	 * @return kentän sisältö merkkijonona
	 */
	public String anna(int k) {
	    switch ( k ) {
	    case 0:
	        return "" + kalastajaID;
	    case 1:
	        return "" + nimi;
	    case 2:
	        return "" + syntymapaiva;
	    case 3:
	        return "" + sahkoposti;
	    case 4:
	        return "" + kotiosoite;
	    case 5:
	        return "" + postinumero;
	    case 6:
	        return "" + kaupunki;
	    default:
	        return "Äääliö";
	    }
	}
	
	/**
	 * Palauttaa k:tta kalastajan kenttää vastaavan kysymyksen
	 * @param k kuinka monennen kentän kysymys palautetaan (0-alkuinen)
	 * @return k:netta kenttää vastaava kysymys
	 */
	public String getKysymys(int k) {
	    switch ( k ) {
	    case 0:
	        return "Kalastajan ID";
	    case 1:
	        return "nimi";
	    case 2:
	        return "syntymäpäivä";
	    case 3:
	        return "sähköpostiosoite";
	    case 4:
	        return "kotiosoite";
	    case 5:
	        return "postinumero";
	    case 6:
	        return "postitoimipaikka";
	    default:
	        return "Äääliö";
	    }
	}
	
	/**
	 * erotellaan kalastajan tiedot tolpilla
	 */
	@Override
	public String toString() {
	    return "" +
	            getID() + "|" +
	            nimi + "|" +
	            syntymapaiva + "|" +
	            sahkoposti + "|" +
	            kotiosoite + "|" +
	            postinumero + "|" +
	            kaupunki + "|" ;
	}
	
	/**
	 * tehdään uusi kopio kalastajasta
	 */
	@Override
	public Kalastaja clone() throws CloneNotSupportedException {
	    Kalastaja uusi;
	    uusi = (Kalastaja) super.clone();
	    uusi.kalastajaID = kalastajaID;
	    uusi.nimi = new String(nimi);
	    uusi.syntymapaiva = new String(syntymapaiva);
	    uusi.sahkoposti = new String(sahkoposti);
	    uusi.kotiosoite = new String(kotiosoite);
	    uusi.postinumero = new String(postinumero);
	    uusi.kaupunki = new String(kaupunki);
/*
	    for (int k = 0; k < getKenttia(); k++)
	        uusi.kentat[k] = kentat[k].clone();
	*/    
	    
	    return uusi;
	}
	
	/**
	 * tulostetaan henkilön tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os){
		tulosta(new PrintStream(os));
	}
	/**
	 * testiohjelma kalastajalle
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Kalastaja testi1 = new Kalastaja(), testi2 = new Kalastaja();
		testi1.luoArvot(); // annetaan kalastajalle testiarvot
		testi1.tulosta(System.out);
		System.out.println("-----------------------------------------------------------------");
		testi2.luoArvot();
		testi2.tulosta(System.out);

	}

}
