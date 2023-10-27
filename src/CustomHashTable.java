import java.io.*;

class CustomHashTable implements java.io.Serializable {

    static final class Node {
        Object key;
        Node next;
        int wordCount = 1;
        Object value;
        int tfidf;

        Node(Object k, Node n) {
            key = k;
            next = n;
        }
    }

    Node[] table = new Node[8]; // always a power of 2
    int size = 0; // size of a document without repeating objects
    int documentSize = 0; // size of a document with repeating objects

    boolean contains(Object key) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key))
                return true;
        }
        return false;
    }

    void add(Object key, Object value) {
        int h = key.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (key.equals(e.key)){
                e.value = value;
                documentSize += 1;
                e.wordCount += 1;
                return;
            }
        }
        table[i] = new Node(key, table[i]);
        table[i].value = value;
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
                newTable[j].value = e.value;
                newTable[j].wordCount = e.wordCount;
            }
        }
        table = newTable;
    }

    void setTfidf(String s, boolean tfidf) {
        int h = s.hashCode();
        int i = h & (table.length - 1);
        for (Node e = table[i]; e != null; e = e.next) {
            if (s.equals(e.key)){

                return;
            }
        }
    }

    void printAll() {
        for (int i = 0; i < table.length; ++i)
            for (Node e = table[i]; e != null; e = e.next)
                System.out.println(e.key + " " + e.value);
    }

    void writeObject(ObjectOutputStream s) throws Exception {
        //s.defaultWriteObject();
        s.writeInt(size);
        for (int i = 0; i < table.length; ++i) {
            for (Node e = table[i]; e != null; e = e.next) {
                s.writeObject(e.key);
                s.writeObject(e.wordCount);
                s.writeObject((double) e.wordCount /documentSize);
            }
        }
    }

     void readObject(ObjectInputStream s) throws Exception {
        //s.defaultReadObject();
        int n = s.readInt();
        for (int i = 0; i < n; ++i){
            add(s.readObject(),3);
        }

    }
}

