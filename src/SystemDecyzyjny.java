import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mateusz on 2016-06-05.
 */
public class SystemDecyzyjny {
    ArrayList<ObiektDecyzyjny> obiekty=new ArrayList<>();

    public SystemDecyzyjny(String sciezka){
        try {
            List<String> wiersze= Files.readAllLines(Paths.get(sciezka));
            for (String wiersz:wiersze) {
                obiekty.add(new ObiektDecyzyjny(wiersz));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public SystemDecyzyjny(){}
}
