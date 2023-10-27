import javax.json.*;
import java.io.*;
import java.util.Properties;

public class Organizer {

    public static void main(String[] args) throws Exception {
        CustomHashTable ht = new CustomHashTable();
        ht.add("man", "declan");
        ht.add("woman", "grace");
        ht.add("dog", "bruno");
        ht.add("color", "red");
        ht.add("season", "christmas");
        ht.add("gpa", "3.7");
        ht.add("country", "usa");
        ht.add("color", "purple");
        ht.printAll();

       /* // Properties
        Properties properties = new Properties();
        FileInputStream fis = null;
        try{
            fis = new FileInputStream("config.properties");
        }catch (FileNotFoundException e){
            System.out.println("change the filepath in `config.properties` to match yours.");
        }
        properties.load(fis);

        // GET json file location (change the .json file name below to match yours)
        String filePath = properties.getProperty("filepath") + File.separator + "MyLinks.json";
        File inputFile = new File(filePath);

        // READ json file
        InputStream inputStream = null;
        try{
            inputStream = new FileInputStream(inputFile);
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        JsonReader reader = Json.createReader(inputStream);
        JsonArray jArray = reader.readArray();
        inputStream.close();
        reader.close();

        createPersistentFiles(jArray);*/
    }

    private static void createPersistentFiles(JsonArray arrayOfLinks) throws Exception {
        WebScraper scraper = new WebScraper();
        CustomHashTable ht = new CustomHashTable();

        CustomHashTable dictionary;

        for(int i=0; i<arrayOfLinks.size(); i++){
            String url = arrayOfLinks.getString(i);

            for(String s : scraper.webScrape(url)){
                ht.add(s,12);
            }

            String filePath = "/Users/declan/IdeaProjects/Wikipedia-Page-Similarity-II/src/records/" + url.substring(30);
            File records = new File(filePath);
            FileOutputStream fos = new FileOutputStream(filePath);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fos);
            ht.writeObject(objectOutputStream);
        }
    }
}
