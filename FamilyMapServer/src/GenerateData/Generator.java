package GenerateData;

import com.google.gson.Gson;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Generator {
    String genPath = "src/GenerateData/";
    public String generateFemaleName() throws IOException {
        Gson gson = new Gson();
        String path = genPath + "fnames.json";
        FemaleNames femaleNames = gson.fromJson(Files.readString(Paths.get(path)), FemaleNames.class);
        return femaleNames.getRandomName();
    }

    public String generateMaleName() throws IOException {
        Gson gson = new Gson();
        String path =  genPath + "mnames.json";
        MaleNames maleNames = gson.fromJson(Files.readString(Paths.get(path)), MaleNames.class);
        return maleNames.getRandomName();
    }

    public String generateSirName() throws IOException {
        Gson gson = new Gson();
        String path =  genPath + "snames.json";
        SirNames sirNames = gson.fromJson(Files.readString(Paths.get(path)), SirNames.class);
        return sirNames.getRandomName();
    }

    public Locations generateLocation() throws IOException {
        Gson gson = new Gson();
        String path =  genPath + "Locations.json";
        AllLocations allLocations = gson.fromJson(Files.readString(Paths.get(path)), AllLocations.class);
        Locations locations = allLocations.getRandomLocation();
        return locations;
    }

}
