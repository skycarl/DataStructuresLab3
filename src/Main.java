/*
  This program implements Huffman encoding. It imports a frequency table containing frequencies of
  letters, some clear (unencoded) text, and some encoded text. It translates the clear text to
  Huffman-encoded text, and decodes the encoded data, outputting all to a file. The user must
  specify input and output file names in the runtime arguments.

  @author Skyler Carlson
 * @version 1.0
 * @since 2019-04-19
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        System.out.println("Hello World!");

        // Verify filenames are specified
        if (args.length != 3) {
            System.out.println(
                "Error: insufficient runtime arguments. Enter file names for frequency table, clear text, and encoded files.");
            System.exit(1);
        }

        // Assume that the output file will be named output.txt
        String outputFilename = "output.txt"; // TODO check is this is legal or if user needs to specify

        // Load file names into variables
        String freqTableFilename = args[0];
        String encodedTextFilename = args[1];
        String clearTextFilename = args[2];

        // Create file objects

        File encodedTextFile = new File(encodedTextFilename);
        File clearTextFile = new File(clearTextFilename);

        // Read in the ClearText file
        try {
            importFreqTable(freqTableFilename)
        }

        // Create scanner objects

        Scanner encodedTextScanner = new Scanner(System.in);
        Scanner clearTextScanner = new Scanner(System.in);
    }

    /**
     * This method imports the frequency table and returns an array of String objects that contain
     * the frequency data.
     * @param filename      The name of the file containing the frequency table.
     * @return freqTable    The frequency table, stored in an array of Strings.
     */
    private static void importFreqTable(String filename) {

        String[] freqTable = new String[26];
        String tempLine;
        String[] tempLineArray;




        try {
            // Create the file and scanner objects
            File freqTableFile = new File(filename);
            Scanner freqTableScanner = new Scanner(freqTableFile);

            // Read the file line by line
            while (freqTableScanner.hasNext()) {
                tempLine = freqTableScanner.next();

                // Split on possible delimiter options
                tempLineArray = tempLine.split("([ :\\-ñ])+");
                //line.split("( |:|-|ñ)+"); // Original version in case above doesn't work
            }
            freqTableScanner.close();

            // Split the tempLineArray into the frequency table class
        }

        catch (FileNotFoundException fileExc) {
            System.out.println("File not found: " + fileExc.getMessage() +
                ". Program exiting.");
            System.exit(1);
        }

        // TODO remove this test print area


        // TODO return custom object for frequency table
        //return freqTable;
    }
}
