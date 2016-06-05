import java.util.ArrayList;

/**
 * Created by Mateusz on 2016-06-05.
 */
public class ObiektDecyzyjny {
    ArrayList<Deskryptor> deskryptory=new ArrayList<Deskryptor>();
    int decyzja;

    public ObiektDecyzyjny(String wiersz){
        String[] atrybuty = wiersz.split(" ");
        for(int i=0; i<atrybuty.length-1;i++){
            Deskryptor d = new Deskryptor();
            d.nrAtrybutu=i;
            d.wartosc=Integer.parseInt(atrybuty[i]);
            deskryptory.add(d);
        }
        decyzja=Integer.parseInt(atrybuty[atrybuty.length-1]);
    }
    public ObiektDecyzyjny(){}
}
