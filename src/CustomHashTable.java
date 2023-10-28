import java.io.*;

class CustomHashTable implements java.io.Serializable {

    static final class Node {
        Object key;
        Node next;
        int wordCount = 1;
        int documentCount = 0;
        double tf = 0;
        int idfCount = 0; // the number of documents a term can be found
        double idf = 0;
        double tfidf = tf * idf;

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

    // for a single document. Advanced add lets you calculate tf dynamically, and insert idf.
    void advancedAdd(Object key, int totalWordsInDocument, double idf) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.wordCount += 1;
                e.tf = (double) e.wordCount / totalWordsInDocument;
                e.idf = idf;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        table[i].tf = (double) table[i].wordCount / totalWordsInDocument;
        table[i].idf = idf;
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
        for (int i = 0; i < table.length; ++i)
            for (Node e = table[i]; e != null; e = e.next)
                System.out.println(e.key + " -> " + e.idfCount);
    }

    void writeObject(ObjectOutputStream s) throws Exception {
        //s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
                s.writeObject(e.wordCount);
                s.writeObject(e.wordCount);
            }
        }
    }

     void readObject(ObjectInputStream s) throws Exception {
        //s.defaultReadObject();
        int n = s.readInt();
        for (int i = 0; i < n; ++i){
            //add(s.readObject(),3);
        }

    }
}