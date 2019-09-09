package tietorakenne;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;

import fi.jyu.mit.ohj2.WildChars;

import java.io.*;

/**
 * Kalastajia käsittelevä luokka
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class Kalastajat implements Iterable<Kalastaja> {

	private int lkm = 0;
	private Kalastaja alkiot[] = new Kalastaja[5];
	private String tiedostonPerusNimi = "kalastajat";
	private boolean muutettu = false;

	/**
	 * Oletusmuodostaja
	 */
	public Kalastajat(){

	}

	/**
	 * palautetaan kalastajien lukumäärä
	 * @return lukumäärä
	 */
	public int getLkm(){
		return lkm;
	}

	/**
	 * lisätään kalastaja ja sen sisältämät
	 * tiedot alkiot[] - taulukkoon ja kasvatetaan
	 * lkm yhdellä, jotta seuraava kalastaja tallennetaan
	 * seuraavaan taulukon vapaaseen indeksiin
	 * @param kalastaja kalastajan tiedot
	 * @throws SailoException jos tietorakenne on jo täynnä
	 * @example
	 * <pre name="test">
	 * #THROWS SailoException 
	 * Kalastajat kalastajat = new Kalastajat();
	 * Kalastaja testi1 = new Kalastaja(), testi2 = new Kalastaja();
	 * kalastajat.getLkm() === 0;
	 * kalastajat.lisaa(testi1); kalastajat.getLkm() === 1;
	 * kalastajat.lisaa(testi2); kalastajat.getLkm() === 2;
	 * kalastajat.lisaa(testi1); kalastajat.getLkm() === 3;
	 * Iterator<Kalastaja> it = kalastajat.iterator();
	 * it.next() === testi1;
	 * it.next() === testi2;
	 * it.next() === testi1;
	 * kalastajat.lisaa(testi1); kalastajat.getLkm() === 4;
	 * kalastajat.lisaa(testi1); kalastajat.getLkm() === 5;
	 * </pre>
	 */
	public void lisaa(Kalastaja kalastaja) throws SailoException{
		//if(lkm >= MAX_LKM) throw new SailoException("Liikaa alkioita");
		if (lkm >= alkiot.length) alkiot = Arrays.copyOf(alkiot, lkm+20);
		alkiot[lkm] = kalastaja;
		lkm++;
		muutettu = true;
	}

	/**
	 * palautetaan kalastaja, joka löytyy indeksistä
	 * i
	 * @param i indeksi
	 * @return kalastaja
	 */
	public Kalastaja anna(int i){
		if(i < 0 || lkm <= 0) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot[i];
	}

	/** 
	 * Poistaa jäsenen jolla on valittu tunnusnumero  
	 * @param id poistettavan jäsenen tunnusnumero 
	 * @return 1 jos poistettiin, 0 jos ei löydy 
	 * @example 
	 * <pre name="test"> 
	 * #THROWS SailoException  
	 * Jasenet jasenet = new Jasenet(5); 
	 * Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen(); 
	 * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
	 * int id1 = aku1.getTunnusNro(); 
	 * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
	 * jasenet.poista(id1+1) === 1; 
	 * jasenet.annaId(id1+1) === null; jasenet.getLkm() === 2; 
	 * jasenet.poista(id1) === 1; jasenet.getLkm() === 1; 
	 * jasenet.poista(id1+3) === 0; jasenet.getLkm() === 1; 
	 * </pre> 
	 *  
	 */ 
	public int poista(int id) { 
	    int ind = etsiId(id); 
	    if (ind < 0) return 0; 
	    lkm--; 
	    for (int i = ind; i < lkm; i++) 
	        alkiot[i] = alkiot[i + 1]; 
	    alkiot[lkm] = null; 
	    muutettu = true; 
	    return 1; 
	} 
	
	/** 
	 * Etsii jäsenen id:n perusteella 
	 * @param id tunnusnumero, jonka mukaan etsitään 
	 * @return löytyneen jäsenen indeksi tai -1 jos ei löydy 
	 * <pre name="test"> 
	 * #THROWS SailoException  
	 * Jasenet jasenet = new Jasenet(5); 
	 * Jasen aku1 = new Jasen(), aku2 = new Jasen(), aku3 = new Jasen(); 
	 * aku1.rekisteroi(); aku2.rekisteroi(); aku3.rekisteroi(); 
	 * int id1 = aku1.getTunnusNro(); 
	 * jasenet.lisaa(aku1); jasenet.lisaa(aku2); jasenet.lisaa(aku3); 
	 * jasenet.etsiId(id1+1) === 1; 
	 * jasenet.etsiId(id1+2) === 2; 
	 * </pre> 
	 */ 
	public int etsiId(int id) { 
	    for (int i = 0; i < lkm; i++) 
	        if (id == alkiot[i].getID()) return i; 
	    return -1; 
	} 
	
	/**
	 * 
	 * @param kalastaja lisättävän kalastajan viite. Tietorakenne muuttuu omistajaksi
	 * @throws SailoException liikaa alkioita
	 */
	public void korvaaTaiLisaa(Kalastaja kalastaja) throws SailoException {
	    int id = kalastaja.getID();
	    for (int i = 0; i < lkm; i++) {
	        if ( alkiot[i].getID() == id ) {
	            alkiot[i] = kalastaja;
	            muutettu = true;
	            return;
	        }
	    }
	    lisaa(kalastaja);
	}
	

	/**
	 * Tallentaa kalastajat tiedostoon.
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {
		if ( !muutettu ) return;

		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete(); // if .. System.err.println("Ei voi tuhota");
		ftied.renameTo(fbak); // if .. System.err.println("Ei voi nimetä");

		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
			fo.println(alkiot.length); 
			for (Kalastaja kalastaja : this) {
				fo.println(kalastaja.toString());
			}
			//} catch ( IOException e ) { // ei heitä poikkeusta
			//  throw new SailoException("Tallettamisessa ongelmia: " + e.getMessage());
		} catch ( FileNotFoundException ex ) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch ( IOException ex ) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}

		muutettu = false;
	}

	/**
	 * lukee kalastajat tiedostosta
	 * @param tied tiedoston nimi
	 * @throws SailoException liikaa alkioita
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {
			String rivi = fi.readLine();
			if ( rivi == null ) throw new SailoException("Maksimikoko puuttuu");

			while ( (rivi = fi.readLine()) != null ) {
				rivi = rivi.trim();
				if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
				Kalastaja kalastaja = new Kalastaja();
				kalastaja.parse(rivi); // voisi olla virhekäsittely
				lisaa(kalastaja);
			}
			muutettu = false;
		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch ( IOException e ) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}

	/**
	 * palautetaan tiedoston perusnimi
	 * @return tiedostonPerusNimi
	 */
	public String getTiedostonPerusNimi(){
		return tiedostonPerusNimi;
	}

	/**
	 * asetetaan tiedostolle nimi
	 * @param nimi tiedostolle annettava nimi
	 */
	public void setTiedostonPerusNimi(String nimi) {
		tiedostonPerusNimi = nimi;
	}

	/**
	 * palautetaan tiedoston nimi + .dat
	 * @return tiedoston nimi dat päätteellä
	 */
	public String getTiedostonNimi() {
		return getTiedostonPerusNimi() + ".dat";
	}

	/**
	 * Palauttaa varakopiotiedoston nimen
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}

	/**
	 * Luetaan aikaisemmin annetun nimisestä tiedostosta
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}

	/**
	 * Kalastajat luokan iteraattori
	 * @author krmapale
	 * @version 8.4.2016
	 *
	 */
	public class KalastajatIterator implements Iterator<Kalastaja> {
		private int kohdalla = 0;


		/**
		 * Onko olemassa vielä seuraavaa jäsentä
		 * @see java.util.Iterator#hasNext()
		 * @return true jos on vielä jäseniä
		 */
		@Override
		public boolean hasNext() {
			return kohdalla < getLkm();
		}


		/**
		 * Annetaan seuraava jäsen
		 * @return seuraava jäsen
		 * @throws NoSuchElementException jos seuraava alkiota ei enää ole
		 * @see java.util.Iterator#next()
		 */
		@Override
		public Kalastaja next() throws NoSuchElementException {
			if ( !hasNext() ) throw new NoSuchElementException("Ei oo");
			return anna(kohdalla++);
		}


		/**
		 * Tuhoamista ei ole toteutettu
		 * @throws UnsupportedOperationException aina
		 * @see java.util.Iterator#remove()
		 */
		@Override
		public void remove() throws UnsupportedOperationException {
			throw new UnsupportedOperationException("Me ei poisteta");
		}
	}


	/**
	 * Palautetaan iteraattori kalastajista
	 * @return kalastaja iteraattori
	 */
	@Override
	public Iterator<Kalastaja> iterator() {
		return new KalastajatIterator();
	}

	/** 
	 * Palauttaa "taulukossa" hakuehtoon vastaavien jäsenten viitteet 
	 * @param hakuehto hakuehto 
	 * @param k etsittävän kentän indeksi  
	 * @return tietorakenteen löytyneistä jäsenistä 
	 * @example 
	 * <pre name="test"> 
	 * #THROWS SailoException  
	 *   Jasenet jasenet = new Jasenet(); 
	 *   Jasen jasen1 = new Jasen(); jasen1.parse("1|Ankka Aku|030201-115H|Ankkakuja 6|"); 
	 *   Jasen jasen2 = new Jasen(); jasen2.parse("2|Ankka Tupu||030552-123B|"); 
	 *   Jasen jasen3 = new Jasen(); jasen3.parse("3|Susi Sepe|121237-121V||131313|Perämetsä"); 
	 *   Jasen jasen4 = new Jasen(); jasen4.parse("4|Ankka Iines|030245-115V|Ankkakuja 9"); 
	 *   Jasen jasen5 = new Jasen(); jasen5.parse("5|Ankka Roope|091007-408U|Ankkakuja 12"); 
	 *   jasenet.lisaa(jasen1); jasenet.lisaa(jasen2); jasenet.lisaa(jasen3); jasenet.lisaa(jasen4); jasenet.lisaa(jasen5); 
	 *   List<Jasen> loytyneet; 
	 *   loytyneet = (List<Jasen>)jasenet.etsi("*s*",1); 
	 *   loytyneet.size() === 2; 
	 *   loytyneet.get(0) == jasen3 === true; 
	 *   loytyneet.get(1) == jasen4 === true; 
	 *    
	 *   loytyneet = (List<Jasen>)jasenet.etsi("*7-*",2); 
	 *   loytyneet.size() === 2; 
	 *   loytyneet.get(0) == jasen3 === true; 
	 *   loytyneet.get(1) == jasen5 === true;
	 *    
	 *   loytyneet = (List<Jasen>)jasenet.etsi(null,-1); 
	 *   loytyneet.size() === 5; 
	 * </pre> 
	 */ 
	public Collection<Kalastaja> etsi(String hakuehto, int k) {
	    String ehto = "*";
	    if ( hakuehto != null && hakuehto.length() > 0 ) ehto = hakuehto;
	    int hk = k;
	    if ( k < 0 ) hk = 1;
	    Collection<Kalastaja> loytyneet = new ArrayList<Kalastaja>(); 
	    for (Kalastaja kalastaja : this) { 
	        //        if ( jasen.anna(k).toUpperCase().startsWith(hakuehto.toUpperCase()) ) loytyneet.add(jasen); 
	        if (WildChars.onkoSamat(kalastaja.anna(hk), ehto)) loytyneet.add(kalastaja);  
	    } 
	    //  TODO: lajittelua varten vertailija 
	    return loytyneet; 
	} 
	
	/**
	 * testiohjelma kalastajille
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Kalastajat kalastajat = new Kalastajat();

		Kalastaja kalastaja1 = new Kalastaja(), kalastaja2 = new Kalastaja();
		kalastaja1.luoArvot();
		kalastaja2.luoArvot();
		try{
			kalastajat.lisaa(kalastaja1);
			kalastajat.lisaa(kalastaja2);

			System.out.println("============= Jäsenet testi =================");

			for (int i = 0; i < kalastajat.getLkm(); i++) {
				Kalastaja kalastaja = kalastajat.anna(i);
				System.out.println("Kalastaja nro: " + i);
				kalastaja.tulosta(System.out);
				System.out.println("=========================================");
			}
		}
		catch(SailoException ex){
			System.out.println(ex.getMessage());
		}


	}

}
