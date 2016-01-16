import java.io.*;

/**
 * Created by shekhar on 25/9/15.
 */
public class Main {
    public static void main(String[] args) throws IOException {
        if(args.length !=2) {
            System.err.println("Usage: umlparser [path to directory]");
            System.exit(1);
        }

        File inputDirectory = new File(args[0]);
        String filename;
        filename = args[1];


        if(!inputDirectory.exists() || !inputDirectory.isDirectory()) {
            System.err.println("Invalid directory: " + inputDirectory.getAbsolutePath());
            System.exit(1);
        }

        JavaUmlParser umlParser = new JavaUmlParser();
        umlParser.parseDirectory(inputDirectory, filename);

    }
}
