package GenerateData;

import java.util.ArrayList;
import java.util.Random;

public class AllLocations {
    private ArrayList<Locations> data;

    public Locations getRandomLocation() {
        Random randomGenerator = new Random();
        int index = randomGenerator.nextInt(data.size());
        return data.get(index);
    }
}
