import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.*;
import java.util.Properties;

public class WikipediaLinkGenerator {
    public static void main(String[] args) throws IOException {
        String prefix = "https://en.wikipedia.org/wiki/";
        String suffix = "";
        JsonArray links = readFile().getJsonArray("Links");
        while(links.size() < 200){

        }
    }

    private static JsonObject readFile() throws IOException {

        // GET json file location
        String filePath = "/Users/declan/IdeaProjects/Wikipedia-Page-Similarity-II/src/MyLinks.json";
        File inputFile = new File(filePath);

        // READ json file
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(inputFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        JsonReader reader = Json.createReader(inputStream);
        JsonObject object = reader.readObject();
        inputStream.close();
        reader.close();

        return object;
    }
}