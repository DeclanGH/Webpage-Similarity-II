import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Organizer {

    public static void main(String[] args) throws Exception {
        ArrayList<String> ht1 = new ArrayList<>();
        ht1.add("this");
        ht1.add("is");
        ht1.add("the");
        ht1.add("man");
        ht1.add("that");
        ht1.add("is");
        ht1.add("coming");
        ht1.add("here");

        ArrayList<String> ht2 = new ArrayList<>();
        ht2.add("this");
        ht2.add("cow");
        ht2.add("ate");
        ht2.add("that");
        ht2.add("man");
        ht2.add("head");

        ArrayList<String> ht3 = new ArrayList<>();
        ht3.add("that");
        ht3.add("man");
        ht3.add("head");
        ht3.add("look");
        ht3.add("scrunchy");

        CustomHashTable idfCounts = new CustomHashTable();
        int count = 0;
        for(String s : ht1){
            idfCounts.countForIdf(s,1);
        }
        for(String s : ht2){
            idfCounts.countForIdf(s,2);
        }
        for(String s : ht3){
            idfCounts.countForIdf(s,3);
        }

        idfCounts.printAll();

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

        CustomHashTable dictionary = new CustomHashTable();

        for(int i=0; i<arrayOfLinks.size(); i++){ // populate dictionary
            String url = arrayOfLinks.getString(i);

            for(String s : scraper.webScrape(url)){
                dictionary.countForIdf(s,i+1);
            }
        }

        for(int i=0; i<arrayOfLinks.size(); i++){
            String url = arrayOfLinks.getString(i);
            for(String s : scraper.webScrape(url)){
                ht.add(s,0);
            }
        }

    }
}
