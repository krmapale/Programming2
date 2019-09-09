package tietorakenne;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * Kaloja käsittelevä luokka
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class Kalat implements Iterable<Kala> {

	private boolean muutettu = false;
	private String tiedostonPerusNimi = "";

	/** Taulukko kaloista */
	private final Collection<Kala> alkiot = new ArrayList<Kala>();

	/**
	 * alustus
	 */
	public Kalat(){

	}

	/**
	 * Lisää uuden kalan tietorakenteeseen. Ottaa kalan omistukseensa
	 * @param kala kalan tiedot
	 * @example
	 * <pre name="test">
	 * TODO: testit
	 * </pre>
	 */
	public void lisaa(Kala kala){
		alkiot.add(kala);
		muutettu = true;
	}


	/**
	 * Tallentaa kalastajat tiedostoon.  
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {
		if ( !muutettu ) return;

		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
		ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
			for (Kala kala : this) {
				fo.println(kala.toString());
			}
		} catch ( FileNotFoundException ex ) {
			throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch ( IOException ex ) {
			throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}

		muutettu = false;
	}

	/**
	 * palauttaa alkioiden lukumäärän
	 * @return alkioit-taulukon koko
	 */
	public int getLkm(){
		return alkiot.size();
	}

	/**
	 * Iteraattori kaikkien kalojen läpikäymiseen
	 */
	@Override
	public Iterator<Kala> iterator() {

		return alkiot.iterator();
	}

	/**
	 * haetaan kaikki pyyntitiedon kalat
	 * @param pyyntiID pyyntitiedon ID
	 * @return löydetyt pyyntitiedot
	 */
	public List<Kala> annaKalat(int pyyntiID){
		List<Kala> loydetyt = new ArrayList<Kala>();
		for (Kala kala : alkiot)
			if (kala.getPyyntiID() == pyyntiID) loydetyt.add(kala);

		return loydetyt;
	}

	/**
	 * lukee kalojen tiedot tiedostosta
	 * @param tied tiedoston nimi
	 * @throws SailoException liikaa alkioita
	 */
	public void lueTiedostosta(String tied) throws SailoException {
		setTiedostonPerusNimi(tied);
		try ( BufferedReader fi = new BufferedReader(new FileReader(getTiedostonNimi())) ) {

			String rivi;
			while ( (rivi = fi.readLine()) != null ) {
				rivi = rivi.trim();
				if ( "".equals(rivi) || rivi.charAt(0) == ';' ) continue;
				Kala kala = new Kala();
				kala.parse(rivi); // voisi olla virhekäsittely
				lisaa(kala);
			}
			muutettu = false;

		} catch ( FileNotFoundException e ) {
			throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
		} catch ( IOException e ) {
			throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
		}
	}

	/**
	 * Luetaan aikaisemmin annetun nimisestä tiedostosta
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
		lueTiedostosta(getTiedostonPerusNimi());
	}

	/**
	 * Asettaa tiedoston perusnimen ilan tarkenninta
	 * @param tied tallennustiedoston perusnimi
	 */
	public void setTiedostonPerusNimi(String tied) {
		tiedostonPerusNimi = tied;
	}


	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonPerusNimi() {
		return tiedostonPerusNimi;
	}


	/**
	 * Palauttaa tiedoston nimen, jota käytetään tallennukseen
	 * @return tallennustiedoston nimi
	 */
	public String getTiedostonNimi() {
		return tiedostonPerusNimi + ".dat";
	}


	/**
	 * Palauttaa varakopiotiedoston nimen
	 * @return varakopiotiedoston nimi
	 */
	public String getBakNimi() {
		return tiedostonPerusNimi + ".bak";
	}

	/** 
	 * Poistaa valitun harrastuksen 
	 * @param kala poistettava harrastus 
	 * @return tosi jos löytyi poistettava harrastus  
	 * <pre name="test"> 
	 * #THROWS SailoException  
	 * #import java.io.File; 
	 *  PyyntiTiedot pyynnit = new PyyntiTiedot();
	 *  PyyntiTieto pitsi21 = new PyyntiTieto(); pitsi21.luoArvot(2); 
	 *  PyyntiTieto pitsi11 = new PyyntiTieto(); pitsi11.luoArvot(1); 
	 *  PyyntiTieto pitsi22 = new PyyntiTieto(); pitsi22.luoArvot(2);  
	 *  PyyntiTieto pitsi12 = new PyyntiTieto(); pitsi12.luoArvot(1);  
	 *  PyyntiTieto pitsi23 = new PyyntiTieto(); pitsi23.luoArvot(2);  
	 *  pyynnit.lisaa(pitsi21); 
	 *  pyynnit.lisaa(pitsi11); 
	 *  pyynnit.lisaa(pitsi22); 
	 *  pyynnit.lisaa(pitsi12); 
	 *  pyynnit.poista(pitsi23) === false ; pyynnit.getLkm() === 4; 
	 *  pyynnit.poista(pitsi11) === true;   pyynnit.getLkm() === 3; 
	 *  List<PyyntiTieto> h = pyynnit.annaPyyntiTiedot(1); 
	 *  h.size() === 1;  
	 *  h = pyynnit.annaPyyntiTiedot(1); 
	 *  h.get(0) === pitsi12; 
	 * </pre> 
	 */ 
	public boolean poista(Kala kala) { 
		boolean ret = alkiot.remove(kala); 
		if (ret) muutettu = true; 
		return ret; 
	} 

	/** 
	 * Poistaa kaikki tietyn jäsenen harrastukset 
	 * @param poistetutIDt viite siihen, minkä jäsenen harrastukset poistetaan 
	 * @return montako poistettiin  
	 * @example 
	 * <pre name="test"> 
	 * #THROWS SailoException  
	 * #import java.io.File; 
	 *  Harrastukset harrasteet = new Harrastukset(); 
	 *  Harrastus pitsi21 = new Harrastus(); pitsi21.vastaaPitsinNyplays(2); 
	 *  Harrastus pitsi11 = new Harrastus(); pitsi11.vastaaPitsinNyplays(1); 
	 *  Harrastus pitsi22 = new Harrastus(); pitsi22.vastaaPitsinNyplays(2);  
	 *  Harrastus pitsi12 = new Harrastus(); pitsi12.vastaaPitsinNyplays(1);  
	 *  Harrastus pitsi23 = new Harrastus(); pitsi23.vastaaPitsinNyplays(2);  
	 *  harrasteet.lisaa(pitsi21); 
	 *  harrasteet.lisaa(pitsi11); 
	 *  harrasteet.lisaa(pitsi22); 
	 *  harrasteet.lisaa(pitsi12); 
	 *  harrasteet.lisaa(pitsi23); 
	 *  harrasteet.poista(2) === 3;  harrasteet.getLkm() === 2; 
	 *  harrasteet.poista(3) === 0;  harrasteet.getLkm() === 2; 
	 *  List<Harrastus> h = harrasteet.annaHarrastukset(2); 
	 *  h.size() === 0;  
	 *  h = harrasteet.annaHarrastukset(1); 
	 *  h.get(0) === pitsi11; 
	 *  h.get(1) === pitsi12; 
	 * </pre> 
	 */ 
	public int poista(List<Integer> poistetutIDt) { 
		int n = 0; 
		for (Iterator<Kala> it = alkiot.iterator(); it.hasNext();) { 
			Kala kala = it.next(); 
			if(poistetutIDt.contains(kala.getPyyntiID())){
				it.remove(); 
				n++;   
			}  	
		} 

		if (n > 0) muutettu = true; 
		return n; 
	}

	/**
	 * Kalat luokan main metodi. Testausta varten käytössä
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		Kalat kalat = new Kalat();

		Kala kala1 = new Kala(), kala2 = new Kala(), kala3 = new Kala();
		kala1.lisaaKalaTiedot(1); kala2.lisaaKalaTiedot(2); kala3.lisaaKalaTiedot(1);

		kalat.lisaa(kala1); kalat.lisaa(kala2); kalat.lisaa(kala3);

		System.out.println("============== testi =============");

		List<Kala> kalat2 = kalat.annaKalat(1);

		for (Kala kala : kalat2) {
			kala.tulosta(System.out);
			System.out.println("==========================");
		}

	}

}
