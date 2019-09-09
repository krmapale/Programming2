package tietorakenne;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * Kalastajia, pyyntitietoja ja kaloja k‰sittelev‰ luokka.
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class Rekisteri {

	private Kalastajat kalastajat = new Kalastajat();
	private PyyntiTiedot pyyntitiedot = new PyyntiTiedot();
	private Kalat kalat = new Kalat();

	/**
	 * palautetaan kalastajien lukum‰‰r‰
	 * @return kalastajien lkm
	 */
	public int getKalastajia(){
		return kalastajat.getLkm();
	}

	/**
	 * palautetaan pyyntitietojen lukum‰‰r‰
	 * @return pyyntitietojen lkm
	 */
	public int getPyynnit(){
		return pyyntitiedot.getLkm();
	}
	
	/**
	 * Korvaa kalastajan tietorakenteessa. Ottaa kalastajan omistukseensa. 
	 * Etsit‰‰n samalla tunnusnumerolla oleva kalastaja.  Jos ei lˆydy, 
	 * niin lis‰t‰‰n uutena kalastajana.
	 * @param kalastaja korvattava kalastaja
	 * @throws SailoException jos tietorakenne on jo t‰ynn‰
	 */
	public void korvaaTaiLisaa(Kalastaja kalastaja) throws SailoException { 
	    kalastajat.korvaaTaiLisaa(kalastaja); 
	} 
	
	/**
	 * lis‰t‰‰n kalastaja kalastajiin
	 * @param kalastaja tietty kalastaja
	 * @throws SailoException liikaa alkioita
	 */
	public void lisaa(Kalastaja kalastaja) throws SailoException{
		kalastajat.lisaa(kalastaja);
	}

	/**
	 * lis‰t‰‰n yksitt‰inen kala, listaukseen kaloista
	 * @param kala tietty kala
	 * @throws SailoException liikaa alkioita
	 */
	public void lisaa(Kala kala) throws SailoException{
		kalat.lisaa(kala);
	}

	/**
	 * lis‰t‰‰n pyyntitieto
	 * @param pyyntitieto tietty pyyntitieto
	 * @throws SailoException liikaa alkioita
	 */
	public void lisaa(PyyntiTieto pyyntitieto) throws SailoException{
		pyyntitiedot.lisaa(pyyntitieto);
	}

	/**
	 * kutsutaan Kalastajat luokan anna metodia,
	 * joka palauttaa meille tietyss‰ indeksiss‰
	 * olevan alkion tiedot, eli palauttaa siis
	 * kalastajan tiedot
	 * @param i indeksi
	 * @return kalastaja tietyss‰ indeksiss‰
	 * @throws IndexOutOfBoundsException indeksi taulukon ulkopuolella
	 * @example
	 * <pre name="test"> 
	 * #THROWS SailoException
	 * Rekisteri rekisteri = new Rekisteri();
	 * Kalastaja kalastaja1 = new Kalastaja();
	 * kalastaja1.luoArvot();
	 * rekisteri.lisaa(kalastaja1);
	 * Kalastaja kalastaja2 = new Kalastaja();
	 * kalastaja2.luoArvot();
	 * rekisteri.lisaa(kalastaja2); 
	 * Kalastaja kalastaja3 = new Kalastaja();
	 * kalastaja3.luoArvot();
	 * rekisteri.lisaa(kalastaja3); 
	 * rekisteri.getKalastajia() === 3;
	 * rekisteri.annaKalastaja(0) === kalastaja1;
	 * rekisteri.annaKalastaja(1) === kalastaja2;
	 * rekisteri.annaKalastaja(2) === kalastaja3; 
	 * rekisteri.lisaa(kalastaja1); rekisteri.annaKalastaja(3) === kalastaja1;
	 * rekisteri.lisaa(kalastaja2); rekisteri.annaKalastaja(4) === kalastaja2;
	 * rekisteri.lisaa(kalastaja3); #THROWS IndexOutOfBoundsException
	 * rekisteri.getKalastajia() === 5; 
	 * </pre>
	 */
	public Kalastaja annaKalastaja(int i) throws IndexOutOfBoundsException{
		return kalastajat.anna(i);
	}


	/**
	 * kutsutaan PyyntiTiedot luokassa olevaa annaPyyntiTiedot
	 * metodia, joka palauttaa yksitt‰isen kalastajan ID:n perusteella
	 * haetut pyyntitiedot
	 * @param kalastaja tietty kalastaja
	 * @return tietyn kalastajan pyyntitiedot
	 * @example
	 * <pre name="test">
	 * #import java.util.*;
	 * #THROWS SailoException
	 * Rekisteri rekisteri = new Rekisteri();
	 * Kalastaja kalastaja1 = new Kalastaja(), kalastaja2 = new Kalastaja();
	 * kalastaja1.luoArvot();
	 * kalastaja2.luoArvot();
	 * rekisteri.lisaa(kalastaja1); #THROWS SailoException
	 * rekisteri.lisaa(kalastaja2); #THROWS SailoException
	 * int id1 = kalastaja1.getID();
	 * int id2 = kalastaja2.getID();
	 * PyyntiTieto pyynti1 = new PyyntiTieto(id1), pyynti2 = new PyyntiTieto(id2), pyynti3 = new PyyntiTieto(id1);
	 * pyynti1.luoarvot(id1); pyynti2.luoarvot(id2); pyynti3.luoarvot(id1);
	 * rekisteri.lisaa(pyynti1); #THROWS SailoException
	 * rekisteri.lisaa(pyynti2); #THROWS SailoException
	 * rekisteri.lisaa(pyynti3); #THROWS SailoException
	 *  List<PyyntiTieto> loytyneet;
	 *  loytyneet = rekisteri.annaPyyntiTiedot(kalastaja1); #THROWS SailoException
	 *  loytyneet.size() === 2; 
	 *  loytyneet.get(0) === pyynti1;
	 *  loytyneet.get(1) === pyynti3;
	 *  loytyneet = rekisteri.annaPyyntiTiedot(kalastaja2); #THROWS SailoException
	 *  loytyneet.size() === 1;
	 *  loytyneet.get(1) === pyynti2; 
	 * </pre>
	 */
	public List<PyyntiTieto> annaPyyntiTiedot(Kalastaja kalastaja) {
		return pyyntitiedot.annaPyyntiTiedot(kalastaja.getID());
	}
	
	/**
	 * palautetaan yksitt‰isen kalan tiedot
	 * @param pyyntitieto tietty pyyntitieto
	 * @return pyyntitiedon kalat
	 * @example
	 * <pre name="test">
	 * #import java.util.*;
	 * #THROWS SailoException
	 * Rekisteri rekisteri = new Rekisteri();
	 * Kalastaja kalastaja1 = new Kalastaja(), kalastaja2 = new Kalastaja();
	 * kalastaja1.luoArvot();
	 * kalastaja2.luoArvot();
	 * rekisteri.lisaa(kalastaja1); #THROWS SailoException
	 * rekisteri.lisaa(kalastaja2); #THROWS SailoException
	 * int id1 = kalastaja1.getID();
	 * int id2 = kalastaja2.getID();
	 * PyyntiTieto pyynti1 = new PyyntiTieto(id1), pyynti2 = new PyyntiTieto(id2), pyynti3 = new PyyntiTieto(id1);
	 * pyynti1.luoarvot(id1); pyynti2.luoarvot(id2); pyynti3.luoarvot(id1);
	 * rekisteri.lisaa(pyynti1); #THROWS SailoException
	 * rekisteri.lisaa(pyynti2); #THROWS SailoException
	 * rekisteri.lisaa(pyynti3); #THROWS SailoException
	 * int id01 = pyynti1.getPyyntiID();
	 * int id02 = pyynti2.getPyyntiID();
	 * int id03 = pyynti3.getPyyntiID();
	 * Kala kala1 = new Kala(id01), kala2 = new Kala(id03), kala3 = new Kala(id02), kala4 = new Kala(id01), kala5 = new Kala(id02);
	 * rekisteri.lisaa(kala1); #THROWS SailoException
	 * rekisteri.lisaa(kala2); #THROWS SailoException
	 * rekisteri.lisaa(kala3); #THROWS SailoException
	 * List<Kala> loytyneet;
	 * loytyneet = rekisteri.annaKalanTiedot(pyynti1);
	 * loytyneet.size() === 2;
	 * loytyneet.get(0) === kala1;
	 * loytyneet.get(1) === kala4;
	 * loytyneet = rekisteri.annaKalanTiedot(pyynti2);
	 * loytyneet.size() === 2;
	 * loytyneet.get(0) === kala3;
	 * loytyneet.get(1) === kala5;
	 * loytyneet = rekisteri.annaKalanTiedot(pyynti3);
	 * loytyneet.size() === 1;
	 * loytyneet.get(0) === kala2;
	 * </pre>
	 */
	public List<Kala> annaKalanTiedot(PyyntiTieto pyyntitieto){
		return kalat.annaKalat(pyyntitieto.getPyyntiID());
	}
	/**
	 * luetaan tiedostosta sinne tallennetut tiedot
	 * @param nimi tiedoston nimi
	 * @throws SailoException liikaa alkioita
	 */
	public void lueTiedostosta(String nimi) throws SailoException {
	    kalastajat = new Kalastajat(); // jos luetaan olemassa olevaan niin helpoin tyhjent‰‰ n‰in
	    pyyntitiedot = new PyyntiTiedot();
	    kalat = new Kalat();

	    setTiedosto(nimi);
	    kalastajat.lueTiedostosta();
	    pyyntitiedot.lueTiedostosta();
	    kalat.lueTiedostosta();
	}
	
	/**
	 * Asettaa tiedostojen perusnimet
	 * @param nimi uusi nimi
	 */
	public void setTiedosto(String nimi) {
	    File dir = new File(nimi);
	    dir.mkdirs();
	    String hakemistonNimi = "";
	    if ( !nimi.isEmpty() ) hakemistonNimi = nimi +"/";
	    kalastajat.setTiedostonPerusNimi(hakemistonNimi + "kalastajat");
	    pyyntitiedot.setTiedostonPerusNimi(hakemistonNimi + "pyyntitiedot");
	    kalat.setTiedostonPerusNimi(hakemistonNimi + "kalat");
	}
	
	/**
	 * Tallentaa rekisterin tiedot tiedostoon.  
	 * Vaikka kalastajien tallettamien ep‰onistuisi, niin yritet‰‰n silti tallettaa
	 * pyyntitietoja ja kaloja ennen poikkeuksen heitt‰mist‰.
	 * @throws SailoException jos tallettamisessa ongelmia
	 */
	public void tallenna() throws SailoException {
	    String virhe = "";
	    try {
	        kalastajat.tallenna();
	    } catch ( SailoException ex ) {
	        virhe = ex.getMessage();
	    }

	    try {
	        pyyntitiedot.tallenna();
	    } catch ( SailoException ex ) {
	        virhe += ex.getMessage();
	    }
	    
	    try {
	        kalat.tallenna();
	    } catch ( SailoException ex ) {
	        virhe += ex.getMessage();
	    }
	    
	    if ( !"".equals(virhe) ) throw new SailoException(virhe);
	}

	/** 
	 * Palauttaa "taulukossa" hakuehtoon vastaavien kalastajien viitteet 
	 * @param hakuehto hakuehto  
	 * @param k etsitt‰v‰n kent‰n indeksi  
	 * @return tietorakenteen lˆytyneist‰ kalastajista 
	 * @throws SailoException Jos jotakin menee v‰‰rin
	 */ 
	public Collection<Kalastaja> etsi(String hakuehto, int k) throws SailoException { 
	    return kalastajat.etsi(hakuehto, k); 
	} 
	
	/**
	 * Poistaa kalastajista, pyyntitiedoista ja kalastajista ne joilla on kyseinen id
	 * @param id viitenumero, jonka mukaan poistetaan
	 * @return montako kalastajaa poistettiin
	 */
	public int poista(int id) {
	    int ret = kalastajat.poista(id);
	    List<Integer> poistetutIDt = pyyntitiedot.poista(id);
	    kalat.poista(poistetutIDt);
 
	    return ret; 
	}
	
	/** 
	 * Poistaa t‰m‰n pyyntitiedon 
	 * @param pyyntitieto poistettava pyyntitieto 
	 */ 
	public void poistaPyyntitieto(PyyntiTieto pyyntitieto) { 
	    pyyntitiedot.poista(pyyntitieto); 
	}

	/** 
	 * Poistaa t‰m‰n kalan 
	 * @param kala poistettava pyyntitieto 
	 */ 
	public void poistaKala(Kala kala) { 
	    kalat.poista(kala); 
	}
	
	/**
	 * testausta varten luotu main metodi
	 * @param args ei k‰ytˆss‰
	 */
	public static void main(String[] args) {
		Rekisteri rekisteri = new Rekisteri(); 

		try{
			Kalastaja kalastaja1 = new Kalastaja(), kalastaja2 = new Kalastaja();
			kalastaja1.luoArvot();
			kalastaja2.luoArvot();
			rekisteri.lisaa(kalastaja1);
			rekisteri.lisaa(kalastaja2);

			int id1 = kalastaja1.getID();
			int id2 = kalastaja2.getID();

			PyyntiTieto pyynti1 = new PyyntiTieto(id1), pyynti2 = new PyyntiTieto(id2), pyynti3 = new PyyntiTieto(id1);
			pyynti1.luoarvot(id1); pyynti2.luoarvot(id2); pyynti3.luoarvot(id1);

			rekisteri.lisaa(pyynti1); rekisteri.lisaa(pyynti2); rekisteri.lisaa(pyynti3);

			Kala kala1 = new Kala(), kala2 = new Kala(), kala3 = new Kala();
			kala1.lisaaKalaTiedot(pyynti1.getPyyntiID());
			kala2.lisaaKalaTiedot(pyynti2.getPyyntiID()); 
			kala3.lisaaKalaTiedot(pyynti3.getPyyntiID());

			rekisteri.lisaa(kala1); rekisteri.lisaa(kala2); rekisteri.lisaa(kala3);

			// Rekisterin testi‰

			for (int i = 0; i < rekisteri.getKalastajia(); i++) {

				Kalastaja kalastaja = rekisteri.annaKalastaja(i);
				System.out.println("-----------------Kalastaja-------------------");
				System.out.println("Kalastaja paikassa: " + i);

				kalastaja.tulosta(System.out);


				List<PyyntiTieto> loytyneet = rekisteri.annaPyyntiTiedot(kalastaja);
				for (PyyntiTieto pyyntitieto : loytyneet){
					System.out.println("==========Pyyntitiedot===============");
					pyyntitieto.tulosta(System.out);


					List<Kala> loydetyt = rekisteri.annaKalanTiedot(pyyntitieto);
					for(Kala kala : loydetyt){
						System.out.println("~~~~~~~~~~~~~Kalan tiedot~~~~~~~~~~~~~~~");
						kala.tulosta(System.out);

					}
				}

			}

		} catch (SailoException ex){
			System.out.println(ex.getMessage());
		}

	}


}
