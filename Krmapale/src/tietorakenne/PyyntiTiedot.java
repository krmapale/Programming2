package tietorakenne;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * Pyyntitietoja käsittelevä luokka.
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class PyyntiTiedot implements Iterable<PyyntiTieto> {


	private boolean muutettu = false;
	private String tiedostonPerusNimi = "";

	/** Taulukko pyyntitiedoista */
	private final Collection<PyyntiTieto> alkiot = new ArrayList<PyyntiTieto>();
	
	/**
	 * oletusmuodostaja
	 */
	public PyyntiTiedot(){

	}

	/**
	 * palautetaan pyyntitietojen lukumäärä
	 * @return lkm
	 */
	public int getLkm(){
		return alkiot.size();
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
	 * palautetaan pyyntitieto, joka löytyy indeksistä
	 * i
	 * @param i indeksi
	 * @return pyyntitiedon indeksistä
	
	public PyyntiTieto anna(int i){
		if(i < 0 || alkiot.size() <= 0) throw new IndexOutOfBoundsException("Laiton indeksi: " + i);
		return alkiot;
	}
	 */

	/**
	 * lisää yksittäisen pyyntitiedon pyyntitieto-
	 * listaan
	 * @param pyyntitieto tietty pyyntitieto
	 */
	public void lisaa(PyyntiTieto pyyntitieto){
		alkiot.add(pyyntitieto);
		muutettu = true;
	}

	/**
	 * haetaan kaikki kalastajat
	 * @param kalastajaID tietyn kalastajan tunnusnumero
	 * @return löydetyt pyyntitiedot
	 * @example
	 * <pre name="test">
	 * #import java.util.*;
	 * #THROWS SailoException
	 * PyyntiTiedot pyyntitiedot = new PyyntiTiedot();
	 * PyyntiTieto pyynti1 = new PyyntiTieto(), pyynti2 = new PyyntiTieto(), pyynti3 = new PyyntiTieto();
	 * pyynti1.luoarvot(1);
	 * pyynti2.luoarvot(2);
	 * pyynti1.luoarvot(1);
	 * pyyntitiedot.lisaa(pyynti1); #THROWS SailoException
	 * pyyntitiedot.lisaa(pyynti2); #THROWS SailoException
	 * pyyntitiedot.lisaa(pyynti3); #THROWS SailoException
	 * List<PyyntiTieto> loytyneet;
	 * loytyneet = pyyntitiedot.annaPyyntiTiedot(1);
	 * loytyneet.size() === 2;
	 * loytyneet.get(0) === pyynti1;
	 * loytyneet.get(1) === pyynti3;
	 * loytyneet = pyyntitiedot.annaPyyntiTiedot(2);
	 * loytyneet.size() === 1;
	 * loytyneet.get(0) === pyynti2;
	 * </pre>
	 */
	public List<PyyntiTieto> annaPyyntiTiedot(int kalastajaID) {
		List<PyyntiTieto> loydetyt = new ArrayList<PyyntiTieto>();
		
		for(PyyntiTieto pyynti: alkiot){
			if (pyynti.getKalastajaID() == kalastajaID) loydetyt.add(pyynti);
		}
		
		return loydetyt;
	}
	
	/**
	 * 
	 * @param kalastajaID kalastajan tunnusnro
	 * @return lista löytyneistä päivämääristä ja vesistöistä
	
	public List<String> annaPvmVesisto(int kalastajaID){
		List<String> loydetyt = new ArrayList<String>();
		for (int i = 0; i < lkm; i++){
			String pvmVesisto = "Pvm: " + alkiot[i].anna(2) + ", " + "Vesistö " + alkiot[i].anna(5);
			PyyntiTieto pyynti = alkiot[i];
			if (pyynti.getKalastajaID() == kalastajaID) loydetyt.add(pvmVesisto);
		}
		return loydetyt;
	}
	 */
	
	/**
	 * luetaan pyyntitiedot tiedostosta 
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
	            PyyntiTieto pyynti = new PyyntiTieto();
	            pyynti.parse(rivi); // voisi olla virhekäsittely
	            lisaa(pyynti);
	        }
	        muutettu = false;

	    } catch ( FileNotFoundException e ) {
	        throw new SailoException("Tiedosto " + getTiedostonNimi() + " ei aukea");
	    } catch ( IOException e ) {
	        throw new SailoException("Ongelmia tiedoston kanssa: " + e.getMessage());
	    }
	}

	/**
	 * Palautetaan iteraattori jäsenistään.
	 * @return jäsen iteraattori
	 */
	@Override
	public Iterator<PyyntiTieto> iterator() {
	    return alkiot.iterator();
	}
	/**
	 * Luetaan aikaisemmin annetun nimisestä tiedostosta
	 * @throws SailoException jos tulee poikkeus
	 */
	public void lueTiedostosta() throws SailoException {
	    lueTiedostosta(getTiedostonPerusNimi());
	}
	
	/** 
	 * Poistaa valitun harrastuksen 
	 * @param pyyntitieto poistettava harrastus 
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
	public boolean poista(PyyntiTieto pyyntitieto) { 
	    boolean ret = alkiot.remove(pyyntitieto); 
	    if (ret) muutettu = true; 
	    return ret; 
	} 
	
	/** 
	 * Poistaa kaikki tietyn jäsenen harrastukset 
	 * @param kalastajaID viite siihen, minkä jäsenen harrastukset poistetaan 
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
	public List<Integer> poista(int kalastajaID) { 
	    int n = 0; 
	    List<Integer> poistettujenIDt = new ArrayList<Integer>();
	    for (Iterator<PyyntiTieto> it = alkiot.iterator(); it.hasNext();) { 
	        PyyntiTieto pyynti = it.next(); 
	        if (pyynti.getKalastajaID() == kalastajaID) {
	        	poistettujenIDt.add(pyynti.getPyyntiID());
	            it.remove(); 
	            n++; 
	        } 
	    } 
	    if (n > 0) muutettu = true; 
	    return poistettujenIDt; 
	}

	/**
	 * Tallentaa jäsenistön tiedostoon.  Kesken.
	 * @throws SailoException jos talletus epäonnistuu
	 */
	public void tallenna() throws SailoException {
		if ( !muutettu ) return;

		File fbak = new File(getBakNimi());
		File ftied = new File(getTiedostonNimi());
		fbak.delete(); //  if ... System.err.println("Ei voi tuhota");
		ftied.renameTo(fbak); //  if ... System.err.println("Ei voi nimetä");

		try ( PrintWriter fo = new PrintWriter(new FileWriter(ftied.getCanonicalPath())) ) {
		    for (PyyntiTieto pyynti : this) {
		        fo.println(pyynti.toString());
		    }
		} catch ( FileNotFoundException ex ) {
		    throw new SailoException("Tiedosto " + ftied.getName() + " ei aukea");
		} catch ( IOException ex ) {
		    throw new SailoException("Tiedoston " + ftied.getName() + " kirjoittamisessa ongelmia");
		}

		muutettu = false;
	}

	/**
	 * pyyntitiedot luokan main metodi. Testausta varten käytössä
	 * @param args ei käytössä
	 */
	public static void main(String[] args) {
		PyyntiTiedot pyyntitiedot = new PyyntiTiedot();

		PyyntiTieto pyyntitieto1 = new PyyntiTieto(), pyyntitieto2 = new PyyntiTieto();
		pyyntitieto1.luoarvot(1);
		pyyntitieto2.luoarvot(2);

		pyyntitiedot.lisaa(pyyntitieto1);
		pyyntitiedot.lisaa(pyyntitieto2);
	
			System.out.println("Kalastaja nro: ");
			System.out.println("=========================================");
		
	}

}
