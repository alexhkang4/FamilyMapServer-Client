package GenerateData;

import java.util.ArrayList;
import java.util.Random;

public class NameParent {
    ArrayList<String> data = new ArrayList<>();
    public String getRandomName() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(data.size());
        return data.get(index);
    }
}
