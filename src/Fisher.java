import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Mateusz on 2016-06-05.
 */
public class Fisher {

    SystemDecyzyjny sys;
    ArrayList<Integer> listaDecyzji;
    public Fisher(String sciezka){
        sys=new SystemDecyzyjny(sciezka);
        listaDecyzji=stworzListeDecyzji();
    }
    ArrayList<Integer> stworzListeDecyzji(){
        ArrayList<Integer> listaDecyzji=new ArrayList<>();
        for(ObiektDecyzyjny obiekt:this.sys.obiekty){
            if(!listaDecyzji.contains(obiekt.decyzja)){
                listaDecyzji.add(obiekt.decyzja);
            }
        }
        return listaDecyzji;
    }

//    HashMap<Integer,ArrayList<ObiektDecyzyjny>> podzielNaKoncepty(){
//        ArrayList<ObiektDecyzyjny> listaKonceptu;
//        HashMap<Integer,ArrayList<ObiektDecyzyjny>> sysPodzielony=new HashMap<>();
//        for (Integer decyzja:this.listaDecyzji) {
//            listaKonceptu=new ArrayList<>();
//            for (ObiektDecyzyjny obiekt:sys.obiekty) {
//                if(decyzja==obiekt.decyzja){
//                    listaKonceptu.add(obiekt);
//                }
//            }
//            sysPodzielony.put(decyzja,listaKonceptu);
//        }
//       // stworzListeSeparacji(sysPodzielony);
//        return sysPodzielony;
//    }
    HashMap<Integer,ArrayList<Separacja>> generujListeSeparacji(){
        ArrayList<Double> doSredniej;
        ArrayList<Double> doSrPozostalych;
        Double sredniaKlasy;
        Double sredniaPozostalych;
        Double wariancjaKlasy;
        Double wariancjaPozostalych;
        HashMap<Integer,ArrayList<Separacja>> listaSeparacji=new HashMap<>();
        ArrayList<Separacja> obSeparacji;

        for (Integer decyzja:listaDecyzji) {
            obSeparacji=new ArrayList<>();
            for(int i=0;i<sys.obiekty.get(0).deskryptory.size();i++){
                doSredniej=new ArrayList<>();
                doSrPozostalych=new ArrayList<>();
                Separacja s=new Separacja();
                for(int j=0;j<sys.obiekty.size();j++){
                    if(sys.obiekty.get(j).decyzja==decyzja){
                        doSredniej.add(sys.obiekty.get(j).deskryptory.get(i).wartosc);
                    }
                    else{
                        doSrPozostalych.add(sys.obiekty.get(j).deskryptory.get(i).wartosc);
                    }
                }
                sredniaKlasy=obliczSrednia(doSredniej);
                sredniaPozostalych=obliczSrednia(doSrPozostalych);
                wariancjaKlasy=obliczWariancje(doSredniej,sredniaKlasy);
                wariancjaPozostalych=obliczWariancje(doSrPozostalych,sredniaPozostalych);
                s.nrAtrybutu=i;
                s.separacja=obliczSeparacje(sredniaKlasy,sredniaPozostalych,wariancjaKlasy,wariancjaPozostalych);
                obSeparacji.add(s);
            }
            Collections.sort(obSeparacji, new Comparator<Separacja>() {
                @Override
                public int compare(Separacja o1, Separacja o2) {
                    return Double.compare(o1.separacja,o2.separacja);
                }
            });
            Collections.reverse(obSeparacji);
            listaSeparacji.put(decyzja,obSeparacji);

        }
        return listaSeparacji;
    }

    ArrayList<Integer> wybierzAtrybuty(){
        ArrayList<Integer> atrybutyDoSystemu=new ArrayList<>();
        HashMap<Integer,ArrayList<Separacja>> listaSeparacji=generujListeSeparacji();
        int licznik=0;
        while(atrybutyDoSystemu.size()!=listaDecyzji.size()){

            for (Integer decyzja:listaDecyzji) {
                if(!atrybutyDoSystemu.contains(listaSeparacji.get(decyzja).get(licznik).nrAtrybutu)
                        &&atrybutyDoSystemu.size()!=listaDecyzji.size()){
                    atrybutyDoSystemu.add(listaSeparacji.get(decyzja).get(licznik).nrAtrybutu);
                }
            }
            licznik++;
        }
        return atrybutyDoSystemu;
    }
    SystemDecyzyjny generujSystem(){
        ArrayList<Integer> atrybutyDoSystemu=wybierzAtrybuty();
        Deskryptor deskryptor;
        ObiektDecyzyjny obiekt;
        SystemDecyzyjny system=new SystemDecyzyjny();
        for (ObiektDecyzyjny o:sys.obiekty) {
            obiekt=new ObiektDecyzyjny();
            for (Deskryptor d:o.deskryptory) {
                for (Integer atrybut:atrybutyDoSystemu) {
                    if(atrybut==d.nrAtrybutu){
                        deskryptor=new Deskryptor();
                        deskryptor.nrAtrybutu=d.nrAtrybutu;
                        deskryptor.wartosc=d.wartosc;
                        obiekt.deskryptory.add(deskryptor);
                        obiekt.decyzja=o.decyzja;
                    }
                }
            }
            system.obiekty.add(obiekt);
        }
        return system;
    }

    String generujWidok(){
        String wynik;
        SystemDecyzyjny system=generujSystem();
        int nrOb=1;
        wynik="====================================\n       ";
        for (Deskryptor deskryptor:system.obiekty.get(0).deskryptory) {
            wynik+=" a"+(deskryptor.nrAtrybutu+1)+" ";
        }
        wynik+=" d ";
        wynik+="\n";
        for(ObiektDecyzyjny obiekt:system.obiekty){
            wynik+="u"+nrOb+" ";
            for(Deskryptor deskryptor:obiekt.deskryptory){
                wynik+=" "+deskryptor.wartosc;
            }
            wynik+="  "+obiekt.decyzja+"\n";
            nrOb++;
        }
        return wynik;
    }

    Double obliczSrednia(ArrayList<Double> lista){
        Double wynik=0.0;
        for (Double l:lista) {
            wynik+=l;
        }
        return wynik/lista.size();
    }
    Double obliczWariancje(ArrayList<Double> lista, Double srednia){
        Double wynik=0.0;
        for (Double l:lista) {
            wynik+=Math.pow(l-srednia,2);
        }
        return wynik/lista.size();
    }
    Double obliczSeparacje(Double sredniaKlasy,Double sredniaPozostalych,Double wariancjaKlasy,Double wariancjaPozostalych){
        Double wynik;
        wynik=Math.pow(sredniaKlasy-sredniaPozostalych,2)/(wariancjaKlasy+wariancjaPozostalych);
        return wynik;
    }

}
