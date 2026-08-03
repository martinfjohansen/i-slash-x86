import DataStructures.Array.Structures.Array;
import DataStructures.Array.Structures.DataReference;
import Translator.Translator.Ast;
import Translator.Translator.Translator2;
import Translator.Translator.Translator2SA;
import Translator.Translator.Translator2T;
import references.references.StringReference;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static DataStructures.Array.Arrays.Arrays.CreateArray;
import static Translator.Translator.Translator.*;

public class RunTranslator {
    public static void main(String[] args) throws IOException {
        char [] input;
        Array functions, structures;
        StringReference message, output, cheaderoutput, asm, cInclude;
        boolean success;

        input = ReadStringFromFile(args[0].toCharArray());

        functions = CreateArray();
        structures = CreateArray();
        message = new StringReference();
        output = new StringReference();
        cheaderoutput = new StringReference();
        asm = new StringReference();
        cInclude = new StringReference();

        // New system
        Array tokens;
        DataReference tokensRef = new DataReference();
        success = Translator2.Tokenize(input, tokensRef, message);
        if(success){
            tokens = tokensRef.data.array;

            Ast ast = new Ast();
            success = Translator2.Parse(tokens, ast, message);

            if(success){
                success = Translator2SA.StaticAnalysis(ast, message);

                if(success){
                    success = Translator2.PrettyPrint(ast, message);

                    if(success) {
                        success = Translator2T.Translate(ast, asm, message);
                        if(success){
                            //System.out.println(asm.string);
                            WriteStringToFile(args[1].toCharArray(), asm.string);

                        }
                    }

                    if(success) {
                        success = Translator2T.WriteCHeader(ast, cInclude, message);
                        if(success){
                            //System.out.println(cInclude.string);
                            WriteStringToFile(args[2].toCharArray(), cInclude.string);
                        }
                    }

                }
            }
        }

        if(success) {
            //System.out.println("success: " + success);
        }else{
            System.out.println("failed: " +  new String(message.string));
        }

        // --

        /*success = FindFunctionsAndStructures(input, functions, structures, message);
        if(success) {
            success = Translate(input, functions, structures, message, output);
            if(success){
                success = GenerateCHeader(input, functions, structures, message, cheaderoutput);
            }
        }

        if(success) {
            WriteStringToFile((args[1] + ".old").toCharArray(), output.string);
            WriteStringToFile((args[2] + ".old").toCharArray(), cheaderoutput.string);
        }else{
            System.out.println("Failed: " + new String(message.string));
        }*/

    }

    public static char [] ReadStringFromFile(char[] filename) throws IOException {
        Path filePath = Paths.get(new String(filename));

        char [] content = new String(Files.readAllBytes(filePath)).toCharArray();

        return content;
    }

    public static void WriteStringToFile(char [] filename, char [] str) throws IOException {
        FileWriter out = new FileWriter(new String(filename));
        BufferedWriter writer = new BufferedWriter(out);
        writer.write(str);
        writer.close();
        out.close();
    }
}
