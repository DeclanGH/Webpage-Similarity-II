import java.io.*;

public class Organizer {

    public static void main(String[] args) throws IOException {
        CustomHashTable h1 = new CustomHashTable();
        h1.add("mouse");
        h1.add("mouse");
        h1.add("is");
        h1.add("going");
        h1.add("to");
        h1.add("beat");
        h1.add("cat");
        h1.add("is");
        h1.add("up");
        h1.add("the");
        h1.add("cat");
        h1.add("mouse");

        h1.printAll();
    }
}
