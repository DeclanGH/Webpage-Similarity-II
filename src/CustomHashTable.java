import java.io.*;

class CustomHashTable implements java.io.Serializable {

    static final class Node implements Serializable{
        Object key;
        Node next;
        int wordCount = 1;
        int documentCount = 0; // only used in dictionary
        double tf = 0;
        int idfCount = 0; // the number of documents a word can be found (only used in dictionary)
        double idf = 0;
        double tfidf = 0;

        Node(Object k, Node n) {
            key = k;
            next = n;
        }
    }

    Node[] table = new Node[8]; // always a power of 2
    int size = 0; // size of a document without repeating objects

    boolean contains(Object key) {
        int hashCode = key.hashCode();
        int index = hashCode & (table.length - 1);
        for (Node e = table[index]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }

    // normal add method for normal HashTable. (Might not be used in this project)
    void add(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.wordCount += 1;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        table[i].wordCount += 1;
        ++size;
        if ((float)size/table.length >= 0.75f)
            resize();
    }

    // for a single document. Advanced add lets you calculate tfidf dynamically.
    void advancedAdd(Object key, int totalWordsInDocument, double idf) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.wordCount += 1;
                e.tf = (double) e.wordCount / totalWordsInDocument; // dynamic calculation of tf
                e.tfidf = e.tf * e.idf;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        table[i].tf = (double) table[i].wordCount / totalWordsInDocument;
        table[i].idf = Math.log(idf); // only needs to be set once. It constant per word
        table[i].tfidf = table[i].tf * table[i].idf;
        ++size;
        if ((float)size/table.length >= 0.75f)
            resize();
    }

    // only use this for dictionary. Where 'dictionary' is Hashtable containing words in all urls
    void countForIdf(Object key, int documentIndex) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key) && !(e.documentCount == documentIndex)){
                e.documentCount = documentIndex;
                e.idfCount += 1;
                return;
            } else if(key.equals(e.key)){
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        table[i].documentCount = documentIndex;
        table[i].idfCount += 1;
        ++size;
        if ((float)size/table.length >= 0.75f)
            resize();
    }

    int getIdfCount(String key){ // Normally going to be for dictionary
        int hashCode = key.hashCode();
        int index = hashCode & (table.length - 1);
        for (Node e = table[index]; e != null; e = e.next) {
            if (key.equals(e.key))
                return e.idfCount;
        }
        return 0;
    }

    double getTfidf(String key){
        int hashCode = key.hashCode();
        int index = hashCode & (table.length - 1);
        for (Node e = table[index]; e != null; e = e.next) {
            if (key.equals(e.key))
                return e.tfidf;
        }
        return 0;
    }

    void resize() {
        Node[] oldTable = table;
        int oldCapacity = oldTable.length;
        int newCapacity = oldCapacity << 1;
        Node[] newTable = new Node[newCapacity];
        for (int i = 0; i < oldCapacity; ++i) {
            for (Node e = oldTable[i]; e != null; e = e.next) {
                int h = e.key.hashCode();
                int j = h & (newTable.length - 1);
                newTable[j] = new Node(e.key, newTable[j]);
                newTable[j].wordCount = e.wordCount;
                newTable[j].documentCount = e.documentCount;
                newTable[j].tf = e.tf;
                newTable[j].idfCount = e.idfCount;
                newTable[j].idf = e.idf;
                newTable[j].tfidf = e.tfidf;
            }
        }
        table = newTable;
    }

    void printAll() {
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                System.out.println(e.key + " -> " + e.tf + " -> " + e.idf + " -> " + e.tfidf);
            }
        }
        System.out.println("-----------------------------------------------------------------");
    }

}