package tietorakenne;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Random;

import fi.jyu.mit.ohj2.Mjonot;

/**
 * Yksitt�ist� pyyntitietoa k�sittelev� luokka
 * @author Kristian Leirimaa
 * @version 8.4.2016
 * Kristian.m.p.leirimaa@student.jyu.fi
 */
public class PyyntiTieto {

	private int pyyntiID;
	private String pvm;
	private String pyyntitapa;
	private String saa;
	private String vesisto;
	private String ottisyvyys;
	private String vedensyvyys;
	private String viehesyotti;

	private int kalastajaID;
	private static int seuraavanID = 1;

	/**
	 * ei viel� tarvii muuta
	 */
	public PyyntiTieto(){

	}
	
	/**
	 * palauttaa kalastajan kenttien lukum��r�n
	 * @return kenttien lkm
	 */
	public int getKenttia(){
		return 9;
	}
	
	/**
	 * Eka kentt� joka on mielek�s kysytt�v�ksi
	 * @return ekan kent�n indeksi
	 */
	public int ekaKentta(){
		return 0;
	} 

	/**
	 * annetaan pyyntitiedolle kalastajan tunnusnumero, jotta
	 * pyyntitieto tiet�� mille kalastajalle se kuuluu. Pyyntitiedolla
	 * on viel� oma erillinen ID, josta sen tunnistaa.
	 * @param ID kalastajan ID
	 */
	public PyyntiTieto(int ID){
		this.kalastajaID = ID;
	}

	/**
	 * v�liaikainen metodi pyyntitietojen arvojen luomiseksi
	 * @param ID kalastajan ID
	 * @example
	 * <pre name="test"> 
	 * PyyntiTieto pyyntitieto1 = new PyyntiTieto();
	 * pyyntitieto1.luoarvot(1);
	 * pyyntitieto1.getPyyntiID() === 1;
	 * pyyntitieto1.getKalastajaID() === 1;
	 * 
	 * PyyntiTieto pyyntitieto2 = new PyyntiTieto();
	 * pyyntitieto2.luoarvot(1);
	 * pyyntitieto2.getPyyntiID() === 2;
	 * pyyntitieto2.getKalastajaID() === 1;
	 * 
	 * PyyntiTieto pyyntitieto3 = new PyyntiTieto();
	 * pyyntitieto3.luoarvot(2);
	 * pyyntitieto3.getPyyntiID() === 3;
	 * pyyntitieto3.getKalastajaID() === 2;
	 * 
	 * PyyntiTieto pyyntitieto4 = new PyyntiTieto();
	 * pyyntitieto4.luoarvot(4);
	 * pyyntitieto4.getPyyntiID() === 4;
	 * pyyntitieto4.getKalastajaID() === 4;
	 * </pre>
	 */
	public void luoarvot(int ID){
		if(pyyntiID > 0) pyyntiID = getPyyntiID(); 
		else this.pyyntiID = asetaPyyntiID();
		this.kalastajaID = ID;
		this.pvm = rndPvm();
		this.pyyntitapa = "Heittokalastus";
		this.saa = "Pilvinen";
		this.vesisto = "P�ij�nne";
		this.ottisyvyys = rndLuku();
		this.vedensyvyys = rndLuku();
		this.viehesyotti = "Bete Lotto";
		
	}

	/**
	 * annetaan pyyntitiedolle tunnistusnumero
	 * @return pyynnin ID
	 */
	public int asetaPyyntiID(){
		int ID = seuraavanID;
		seuraavanID++;
		return ID;
	}

	/**
	 * Asettaa tunnusnumeron ja samalla varmistaa ett�
	 * seuraava numero on aina suurempi kuin t�h�n menness� suurin.
	 * @param nr asetettava tunnusnumero
	 */
	private void setID(int nr) {
		pyyntiID = nr;
		if ( pyyntiID >= seuraavanID ) seuraavanID = pyyntiID + 1;
	}

	/**
	 * palautetaan pyyntitiedon tunnistusnumero
	 * @return pyyntiID
	 */
	public int getPyyntiID(){
		return pyyntiID;
	}

	/**
	 * palautetaan kalastajan tunnusnumero
	 * @return kalastajan ID
	 */
	public int getKalastajaID(){
		return kalastajaID;
	}

	/**
	 * luodaan random p�iv�m��r�
	 * @return
	 */
	private String rndPvm(){
		Random randPvm = new Random();
		return randPvm.nextInt(30) + "." + randPvm.nextInt(12) + ".2015";
	}

	/**
	 * v�liaikainen apuohjelma joka luo
	 * random arvon vedensyvyydelle ja ottisyvyydelle.
	 * Virheellisesti ottisyvyys voi olla syvempi kuin veden syvyys,
	 * mutta koska metodi on v�liaikainen, ei asialla ole niin merkityst�
	 * @return
	 */
	private String rndLuku(){
		Random rnd = new Random();
		return rnd.nextInt(10) + " m";
	}

	/**
	 * tulostusaliohjelma
	 * @param out printstreamin ulos sy�tt�m� tietovirta
	 */
	public void tulosta(PrintStream out){
		out.println("pyyntiID: " + pyyntiID);
		out.println("kalastajaID: " + kalastajaID);
		out.println("p�iv�m��r�: " + pvm);
		out.println("pyyntitapa: " + pyyntitapa);
		out.println("s��: " + saa);
		out.println("vesist�: " + vesisto);
		out.println("ottisyvyys: " + ottisyvyys);
		out.println("veden syvyys: " + vedensyvyys);
		out.println("Viehe/sy�tti: " + viehesyotti);

	}

	/**
	 * tulostetaan henkil�n tiedot
	 * @param os tietovirta johon tulostetaan
	 */
	public void tulosta(OutputStream os){
		tulosta(new PrintStream(os));
	}

	/**
	 * erotellaan pyyntitiedon tiedot tolpilla
	 */
	@Override
	public String toString() {
		return "" +
				getPyyntiID() + "|" +
				getKalastajaID() + "|" +
				pvm + "|" +
				pyyntitapa + "|" +
				saa + "|" +
				vesisto + "|" +
				ottisyvyys + "|" +
				vedensyvyys + "|" +
				viehesyotti + "|";

	}

	/**
	 * Selvitt�� pyyntitiedot tolpilla erotellusta merkkijonosta
	 * ja pit�� huolen siit�, ett� sama ID ei esiinny eri pyyntitiedoilla
	 * useaa kertaa
	 * @param rivi rivi jolta pyyntitiedot otetaan
	 */
	public void parse(String rivi) {
		StringBuffer sb = new StringBuffer(rivi);
		setID(Mjonot.erota(sb, '|', getPyyntiID()));
		kalastajaID = Mjonot.erota(sb, '|', getKalastajaID());
		pvm = Mjonot.erota(sb, '|', pvm);
		pyyntitapa = Mjonot.erota(sb, '|', pyyntitapa);
		saa = Mjonot.erota(sb, '|', saa);
		vesisto = Mjonot.erota(sb, '|', vesisto);
		ottisyvyys = Mjonot.erota(sb, '|', ottisyvyys);
		vedensyvyys = Mjonot.erota(sb, '|', vedensyvyys);
		viehesyotti = Mjonot.erota(sb, '|', viehesyotti);

	}
	
	/**
	 * Palauttaa k:tta kalastajan kentt�� vastaavan kysymyksen
	 * @param k kuinka monennen kent�n kysymys palautetaan (0-alkuinen)
	 * @return k:netta kentt�� vastaava kysymys
	 */
	public String getKysymys(int k) {
	    switch ( k ) {
	    case 0:
	        return "Pyynnin ID";
	    case 1:
	        return "Kalastajan ID";
	    case 2:
	        return "P�iv�m��r�";
	    case 3:
	        return "Pyyntitapa";
	    case 4:
	        return "S��";
	    case 5:
	        return "Vesist�";
	    case 6:
	        return "Ottisyvyys";
	    case 7:
	        return "Veden syvyys";
	    case 8:
	        return "Viehe tai sy�tti";
	    default:
	        return "���li�";
	    }
	}
	
	/**
	 * Asettaa k:n kent�n arvoksi parametrina tuodun merkkijonon arvon
	 * @param k kuinka monennen kent�n arvo asetetaan
	 * @param jono jonoa joka asetetaan kent�n arvoksi
	 * @return null jos asettaminen onnistuu, muuten vastaava virheilmoitus.
	 * @example
	 * <pre name="test">
	 *   Jasen jasen = new Jasen();
	 *   jasen.aseta(1,"Ankka Aku") === null;
	 *   jasen.aseta(2,"kissa") =R= "Hetu liian lyhyt"
	 *   jasen.aseta(2,"030201-1111") === "Tarkistusmerkin kuuluisi olla C"; 
	 *   jasen.aseta(2,"030201-111C") === null; 
	 *   jasen.aseta(9,"kissa") === "Liittymisvuosi v��rin jono = \"kissa\"";
	 *   jasen.aseta(9,"1940") === null;
	 * </pre>
	 */
	public String aseta(int k, String jono) {
	    String tjono = jono.trim();
	    StringBuffer sb = new StringBuffer(tjono);
	    switch ( k ) {
	    case 0:
	        setID(Mjonot.erota(sb, '�', getPyyntiID()));
	        return null;
	    case 1:
	    	kalastajaID = Mjonot.erota(sb, '$', getKalastajaID());
	        return null;
	    case 2:
	        pvm = tjono;
	        return null;
	    case 3:
	        pyyntitapa = tjono;
	        return null;
	    case 4:
	        saa = tjono;
	        return null;
	    case 5:
	        vesisto = tjono;
	        return null;
	    case 6:
	        vedensyvyys = tjono;
	        return null;
	    case 7:
	        ottisyvyys = tjono;
	        return null;
	    case 8:
	        viehesyotti = tjono;
	        return null;
	    default:
	        return "��li�";
	    }
	}
	
	/**
	 * Antaa k:n kent�n sis�ll�n merkkijonona
	 * @param k monenenko kent�n sis�lt� palautetaan
	 * @return kent�n sis�lt� merkkijonona
	 */
	public String anna(int k) {
	    switch ( k ) {
	    case 0:
	        return "" + pyyntiID;
	    case 1:
	        return "" + kalastajaID;
	    case 2:
	        return "" + pvm;
	    case 3:
	        return "" + pyyntitapa;
	    case 4:
	        return "" + saa;
	    case 5:
	        return "" + vesisto;
	    case 6:
	        return "" + vedensyvyys;
	    case 7:
	        return "" + ottisyvyys;
	    case 8:
	        return "" + viehesyotti;
	    default:
	        return "���li�";
	    }
	}

	/**
	 * pyyntitieto luokan main metodi testausta varten.
	 * @param args ei k�yt�ss�
	 */
	public static void main(String[] args) {
		PyyntiTieto testi = new PyyntiTieto(), testi2 = new PyyntiTieto();
		testi.luoarvot(1);
		testi.tulosta(System.out);
		System.out.println("==================================");
		testi2.luoarvot(2);
		testi2.tulosta(System.out);


	}

}
