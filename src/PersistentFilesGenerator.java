import javax.json.*;
import java.io.*;
import java.util.Properties;

public class PersistentFilesGenerator {

    public static void main(String[] args) throws Exception {

        // Properties
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

        createPersistentFiles(jArray);
    }

    private static void createPersistentFiles(JsonArray arrayOfLinks) throws Exception {
        WebScraper scraper = new WebScraper();

        ExtendibleHashing urlMapToFile = new ExtendibleHashing();
        CustomHashTable dictionary = new CustomHashTable();

        for(int i=0; i<arrayOfLinks.size(); i++){ // populate dictionary
            String url = arrayOfLinks.getString(i);

            for(String s : scraper.webScrape(url)){
                dictionary.countForIdf(s,i+1);
            }
        }

        for(int i=0; i<arrayOfLinks.size(); i++){
            CustomHashTable ht = new CustomHashTable();
            String url = arrayOfLinks.getString(i);
            String[] arrayOfWords = scraper.webScrape(url);

            for(String s : arrayOfWords){
                double idf = (double) arrayOfLinks.size() /dictionary.getIdfCount(s);
                ht.advancedAdd(s,arrayOfWords.length,idf);
            }

            // For each url, serialize its hashtable object and map to its url to serialized object
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(ht);
            urlMapToFile.insert(url,bos.toByteArray());
        }

        // Serialize the Extendible Hashing class to be used by other classes
        FileOutputStream fos = new FileOutputStream("SerializedExtensibleHashingClass");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(urlMapToFile);

    }
}