import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.json.*;
import java.io.*;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

public class WikipediaLinkGenerator {
    public static void main(String[] args) throws IOException {

        String wikiLink = "https://en.wikipedia.org/wiki/";

        JsonArray links = readFile().getJsonArray("Links");
        JsonArrayBuilder arrayBuilder = Json.createArrayBuilder(links);

        int watchDog = 0;

        while(links.size() < 200){
            String newLink = (wikiLink + generateRandomString()).replaceAll(" ","_");
            JsonValue newLinkAsValue = Json.createValue(newLink);
            if(!links.contains(newLinkAsValue)){
                URL url = new URL(newLink);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("HEAD"); // Use HEAD request to check for the existence of the resource
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    arrayBuilder.add(newLink);
                    links = arrayBuilder.build();
                }
            }
            watchDog += 1;
            if (watchDog == 2000) break;
        }

        writeJsonFile(links);

    }

    private static void writeJsonFile(JsonArray links) {
        // GET json file location
        String filePath = "/Users/declan/IdeaProjects/Wikipedia-Page-Similarity-II/src/";
        File inputFile = new File(filePath + "MyLinks.json");
        inputFile.delete();
        File outputFile = new File(filePath + "MyLinks.json");

        // READ json file
        OutputStream outputStream = null;
        try{
            outputStream = new FileOutputStream(outputFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        JsonObjectBuilder obj = Json.createObjectBuilder();
        obj.add("Links",links);
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

    private static char generateRandomLetter(){
        Random random = new Random();

        // from letter 'a', get a letter 0 to 25 spots from 'a'. That is, random a-z
        char letter = (char) ('a' + random.nextInt(26));
        return letter;
    }

    private static String generateRandomString() throws IOException {
        // go to a section or letter in the dictionary
        String sourceUrl = "https://en.wikipedia.org/wiki/Wikipedia:Vital_articles/Level/5";

        // get the list of words container
        Document doc = Jsoup.connect(sourceUrl).get();
        Elements liElements = doc.select("ul li");

        // randomly select a word based on the random index
        int sizeOfList = liElements.size();
        Random random = new Random();
        int randomIndex = random.nextInt(sizeOfList);
        Element randomLiElement = liElements.get(randomIndex);

        String randomString = randomLiElement.text();

        return randomString;
    }
}