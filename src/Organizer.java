import javax.json.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Properties;

public class Organizer {

    public static void main(String[] args) throws Exception {
        ArrayList<String> list1 = new ArrayList<>();
        list1.add("this");
        list1.add("is");
        list1.add("the");
        list1.add("man");
        list1.add("that");
        list1.add("is");
        list1.add("coming");
        list1.add("here");

        ArrayList<String> list2 = new ArrayList<>();
        list2.add("this");
        list2.add("cow");
        list2.add("ate");
        list2.add("that");
        list2.add("man");
        list2.add("head");

        ArrayList<String> list3 = new ArrayList<>();
        list3.add("that");
        list3.add("man");
        list3.add("head");
        list3.add("look");
        list3.add("scrunchy");

        CustomHashTable dictionary = new CustomHashTable();
        CustomHashTable doc1 = new CustomHashTable();
        CustomHashTable doc2 = new CustomHashTable();
        CustomHashTable doc3 = new CustomHashTable();

        for(String s : list1){
            dictionary.countForIdf(s,1);
        }
        for(String s : list2){
            dictionary.countForIdf(s,2);
        }
        for(String s : list3){
            dictionary.countForIdf(s,3);
        }

        for(String s : list1) {
            double preIdf = 3.0/dictionary.getIdfCount(s);
            doc1.advancedAdd(s,list1.size(),preIdf);
        }
        for(String s : list2) {
            double preIdf = 3.0/dictionary.getIdfCount(s);
            doc2.advancedAdd(s,list2.size(),preIdf);
        }
        for(String s : list3) {
            double preIdf = 3.0/dictionary.getIdfCount(s);
            doc3.advancedAdd(s,list3.size(),preIdf);
        }

        doc1.printAll();
        doc2.printAll();
        doc3.printAll();

        FileOutputStream fos = new FileOutputStream("doc1");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(doc1);
        FileOutputStream fos2 = new FileOutputStream("doc2");
        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
        oos2.writeObject(doc2);
        FileOutputStream fos3 = new FileOutputStream("doc3");
        ObjectOutputStream oos3 = new ObjectOutputStream(fos3);
        oos3.writeObject(doc3);

        FileInputStream fis = new FileInputStream("doc1");
        ObjectInputStream ois = new ObjectInputStream(fis);
        CustomHashTable stream1 = (CustomHashTable) ois.readObject();
        FileInputStream fis2 = new FileInputStream("doc2");
        ObjectInputStream ois2 = new ObjectInputStream(fis2);
        CustomHashTable stream2 = (CustomHashTable) ois2.readObject();
        FileInputStream fis3 = new FileInputStream("doc3");
        ObjectInputStream ois3 = new ObjectInputStream(fis3);
        CustomHashTable stream3 = (CustomHashTable) ois3.readObject();

        stream1.printAll();
        stream2.printAll();
        stream3.printAll();



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

            }
        }

    }
}