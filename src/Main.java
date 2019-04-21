/*
  This program implements Huffman encoding. It imports a frequency table containing frequencies of
  letters, some clear (unencoded) text, and some encoded text. It translates the clear text to
  Huffman-encoded text, and decodes the encoded data, outputting all to a file. The user must
  specify input and output file names in the runtime arguments.

  @author Skyler Carlson
 * @version 1.0
 * @since 2019-04-19
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
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
        String clearTextFilename  = args[1];
        String encodedTextFilename = args[2];

        // Create file objects

        File encodedTextFile = new File(encodedTextFilename);
        File clearTextFile = new File(clearTextFilename);

        // Read in the ClearText file
        try {
            importFreqTable(freqTableFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Create scanner objects

        Scanner encodedTextScanner = new Scanner(System.in);
        Scanner clearTextScanner = new Scanner(System.in);

        System.out.println("Program exiting...");
    }

    /**
     * This method imports the frequency table and returns an array of String objects that contain
     * the frequency data.
     * @param filename      The name of the file containing the frequency table.
     * @return freqTable    The frequency table, stored in an array of Strings.
     */
    private static void importFreqTable(String filename) {

        FreqTableEntry[] freqTable = new FreqTableEntry[26];
        String tempLine;
        String[] tempLineArray;
        int tempFreqVal;
        char tempChar;
        //FreqTableEntry tempFreqTableEntry;




        try {
            // Create the file and scanner objects
            File freqTableFile = new File(filename);
            //Scanner freqTableScanner = new Scanner(freqTableFile);
            Scanner freqTableScanner = new Scanner(new BufferedReader(new FileReader(freqTableFile)));

            // Read the file line by line
            while (freqTableScanner.hasNextLine()) {
                tempLine = freqTableScanner.nextLine();

                // Test if the line is empty; if so, continue to next loop
                if (tempLine.isEmpty()) {
                    continue;
                }

                // Split on possible delimiter options
                tempLineArray = tempLine.split("([ :\\-ñ])+");

                // TODO improve this logic, if there's time
                // Try-catch block to handle weird ñ character that causes the split to behave unpredictably
                try {
                    // Strip off EOL character by replacing any non-numeric characters from frequency value
                    String tempFreq = tempLineArray[1].replaceAll("[\\D]", "");
                    tempFreqVal = Integer.parseInt(tempFreq);

                }
                catch (NumberFormatException formatExc) {
                    // Strip off EOL character by replacing any non-numeric characters from frequency value
                    String tempFreq = tempLineArray[2].replaceAll("[\\D]", "");
                    tempFreqVal = Integer.parseInt(tempFreq);
                }

                // Convert String object containing the character to a char type
                tempChar = tempLineArray[0].charAt(0);

                // Add values to freqTableEntry object
                FreqTableEntry tempFreqTableEntry = new FreqTableEntry(tempChar, tempFreqVal);

                // Temp print freqtable entry
                System.out.println(tempFreqTableEntry.toString());


            }
            freqTableScanner.close();

            // Split the tempLineArray into the frequency table class
        }

        catch (FileNotFoundException fileExc) {
            System.out.println("File not found: " + fileExc.getMessage() +
                ". Program exiting.");
            System.exit(1);
        }



        // TODO return custom object for frequency table
        //return freqTable;
    }
}
