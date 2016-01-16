
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;

import java.io.*;

/**
 * Created by shekhar on 25/9/15.
 */
public class JavaUmlParser {

    public void parseDirectory(File directory, String filename) throws IOException {
        File[] listOfFiles = directory.listFiles();
        StringBuilder allFilesTextBuilder = new StringBuilder();

        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".java")) {
                FileInputStream stream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = reader.readLine()) != null) {
                    allFilesTextBuilder.append(line + "\n");

                }

            }
        }

    String allFileText = allFilesTextBuilder.toString();
    parse(allFileText, filename);
}

    public void parse(String fileText, String imagename) throws IOException {
        //System.out.println("Hello world");

        ANTLRInputStream input = new ANTLRInputStream(fileText);
        JavaLexer lexer = new JavaLexer(null);
        lexer.setInputStream(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        JavaParser javaParser = new JavaParser(tokens);
        tokens.fill(); //Get all tokens from lexer until EOF
        Printer printer = new Printer(imagename); // Printer class implements JavaListener class
        //PrinterBallSocket printer = new PrinterBallSocket(imagename);
        javaParser.addParseListener(printer);

        JavaParser.CompilationUnitContext tree = javaParser.compilationUnit();


        // System.out.println("###" + tree.getPayload());

    }

}
