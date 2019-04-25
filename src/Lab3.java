/*
  This program implements Huffman encoding. It imports a frequency table containing frequencies of
  letters, some clear (unencoded) text, and some encoded text. It translates the clear text to
  Huffman-encoded text, and decodes the encoded data, outputting all to a file. The user must
  specify input and output file names in the runtime arguments.

  @author Skyler Carlson
  @since 2019-04-19
 */

import java.io.*;
import java.util.*;

public class Lab3 {

    /**
     * Main driver methods for the program.
     *
     * @param args          Passed in with runtime arguments; file names for frequency
     * table, clear text, encoded files, and output file
     */
    public static void main(String[] args) {

        String outputFilename = null;

        // Verify file names are specified
        if (args.length == 4) {
            outputFilename = args[3];

        } else if (args.length
            == 3) { // Assume the user did not specify an output file name
            outputFilename = "output.txt";
        } else {
            System.out.println(
                "Error: insufficient runtime arguments. Enter file names for "
                    + "frequency table, clear text, and encoded files.");
            System.exit(1);
        }

        deletePreviousFile(outputFilename);

        // Load file names into variables
        String freqTableFilename = args[0];
        String clearTextFilename = args[1];
        String encodedTextFilename = args[2];

        // Read in the Frequency table file
        FreqTreeNode[] freqTable = new FreqTreeNode[26];
        try {
            freqTable = importFreqTable(freqTableFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Read in the clear text file
        StringBuilder clearText = new StringBuilder();
        try {
            clearText = importClearText(clearTextFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Split encodedText on \u00A0 character to put into an array
        // TODO make this more robust; this character isn't present for UTF-8 encoded files
        String[] clearTextArray = clearText.toString().split("\\u00A0");

        // Read in the encoded file
        StringBuilder encodedText = null;
        try {
            encodedText = importEncodedText(encodedTextFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Split encodedText on \u00A0 character to put into an array
        // TODO make this more robust; this character isn't present for UTF-8 encoded files
        String[] encodedArray = encodedText.toString().split("\\u00A0");

        // Create a priority queue of FreqTreeNode objects
        PriorityQueue<FreqTreeNode> nodeQueue = new PriorityQueue<FreqTreeNode>();

        // Add elements to the Priority Queue
        //nodeQueue.addAll(Arrays.asList(freqTable).subList(0, 26)); // This was a suggestion from
        // my IDE that would replace the loop below; not sure if this would have allowed
        for (int i = 0; i < 26; i++) {
            nodeQueue.add(freqTable[i]);
        }

        // Build the Huffman tree based on the frequency table
        FreqTreeNode huffmanTree = new FreqTreeNode();
        huffmanTree = buildHuffmanTree(nodeQueue);

        // Traverse the Huffman tree, printing as we go along
        // TODO need to change this to print in the correct format
        //traverseHuffmanAndPrint(huffmanTree);

        // Encode a string using the Huffman tree, and write to file
        addHuffmanCodes(huffmanTree, "");

        // Encode the strings in the clearText array
        StringBuilder[] encodedOutput = new StringBuilder[clearTextArray.length];
        for (int i = 0; i < clearTextArray.length; i++) {
            encodedOutput[i] = encodeHuffman(freqTable, clearTextArray[i]);
        }

        // Print the tree in preorder
        File outputFile = new File(outputFilename);
        printStringToFile("\n\n---------Huffman tree in preorder---------\n",
            outputFile);
        traverseHuffmanAndPrint(huffmanTree, outputFile);

        printFreqTable(freqTable, outputFile);

        // Send the clear text output to the printing array
        printStringToFile("\n\n---------Input/output strings---------\n",
            outputFile);
        writeCodingToFile(clearTextArray, encodedOutput, outputFile);

        // Decode the strings in the encoded array
        StringBuilder[] decodedOutput = new StringBuilder[encodedArray.length];
        for (int i = 0; i < encodedArray.length; i++) {
            decodedOutput[i] = decodeHuffman(huffmanTree, encodedArray[i],
                freqTable, outputFile);
        }

        // Print to file
        writeCodingToFile(encodedArray, decodedOutput, outputFile);

        System.out.println("\nProgram completed.");
    }

    /**
     * This method traverses the Huffman tree to decode a string of encoded
     * data.
     *
     * @param node          The node at which the start of the traversal is happening.
     * @param coded         The encoded string of data.
     * @param freqLookup    The Frequency table lookup.
     * @param outputFile    The File to which it should be written.
     * @return decoded      A StringBuilder object that contains decoded data.
     */
    private static StringBuilder decodeHuffman(FreqTreeNode node,
        String coded, FreqTreeNode[] freqLookup, File outputFile) {

        // StringBuilder containing the decoded text
        String returnChar;
        StringBuilder decoded = new StringBuilder();
        int j;
        int codeLen;

        while (coded.length() > 0) {
            j = 0;

            try {
                // Traverse the Huffman tree to find the code
                returnChar = traverseHuffmanToDecode(node, coded, j, "");
                // Look up returnChar to get the length Huffman code
                codeLen = encodeHuffman(freqLookup, returnChar).length();

                // Remove the 1st n characters from the coded data, where n = codeLen
                coded = coded.substring(codeLen);

                // Append to the output string
                decoded.append(returnChar);

                // Catch specific exception if there is a possible invalid input code
            } catch (StringIndexOutOfBoundsException e) {
                return new StringBuilder(
                    "Error reading Huffman code; potential invalid input.");
            } catch (Exception e) { // catch general exception
                return new StringBuilder(
                    "Unspecified error reading Huffman code.");
            }
        }

        return decoded;
    }


    /**
     * This method traverses the Huffman tree to find the code for a specific
     * character.
     *
     * @param node          The root node.
     * @param coded         The encoded array to be decoded via the tree.
     * @param counter       An index that helps with the traversal.
     * @param charString    The character for which we are searching.
     * @return              Returns the decoded array.
     */
    private static String traverseHuffmanToDecode(FreqTreeNode node,
        String coded, int counter, String charString)
        throws StringIndexOutOfBoundsException {

        // Base condition
        if (counter != 0 && node.getHuffmanCode().equals(charString)) {
            return String.valueOf(node.getCharacter());
        }

        // Recursively traverse the tree
        else {

            // Go left
            if (String.valueOf(coded.charAt(counter)).equals("0")) {
                String decoded = traverseHuffmanToDecode(node.getLeft(),
                    coded, counter + 1, charString + "0");
                if (decoded != null) {
                    return decoded;
                }
            }
            // Go right
            if (String.valueOf(coded.charAt(counter)).equals("1")) {
                String decoded = traverseHuffmanToDecode(node.getRight(),
                    coded, counter + 1, charString + "1");
                if (decoded != null) {
                    return decoded;
                }
            }
        }
        return null;
    }

    /**
     * This method prints each leaf node and the Huffman code associated with
     * it.
     *
     * @param freqTable         The frequency table.
     * @param outputFile        The file object to be written to.
     */
    private static void printFreqTable(FreqTreeNode[] freqTable,
        File outputFile) {

        printStringToFile("\n\n---------Table of Huffman values---------\n",
            outputFile);
        printStringToFile("(Char : Freq : Huffman code)\n", outputFile);
        for (int i = 0; i < 26; i++) {
            printStringToFile(
                freqTable[i].getCharacter() + " : " + freqTable[i]
                    .getFrequency() + " : " + freqTable[i].getHuffmanCode(),
                outputFile);
            printStringToFile("\n", outputFile);
        }
    }

    /**
     * Simple method to print to the file
     *
     * @param str           String to be printed
     * @param outputFile    File object that is the output file
     */
    private static void printStringToFile(String str, File outputFile) {
        try {
            // Create FileWriter object in append mode
            FileWriter outWriter = new FileWriter(outputFile, true);

            // Write the node to file
            outWriter.write(str);
            outWriter.close();

        } catch (IOException ioExc) {
            System.out.println("Error writing to file " + ioExc.getMessage() +
                ". Program exiting.");
        }
    }

    /**
     * This method prints a Huffman tree node to the file.
     *
     * @param node              The node to be printed.
     * @param outputFile        The file object to be written to.
     */
    private static void printNodeToFile(FreqTreeNode node, File outputFile) {
        try {
            // Create FileWriter object in append mode
            FileWriter outWriter = new FileWriter(outputFile, true);

            // Write the node to file
            outWriter.write(
                node.getHuffmanSequence() + ": " + node.getFrequency()
                    + "\n");
            outWriter.close();

        } catch (IOException ioExc) {
            System.out.println("Error writing to file " + ioExc.getMessage() +
                ". Program exiting.");
        }
    }

    /**
     * This method deletes the previous file, if one exists of the same name.
     *
     * @param filename      The name of the output file, specified in runtime
     * parameters.
     */
    private static void deletePreviousFile(String filename) {
        File oldFile = new File(filename);
        oldFile.delete();
    }

    /**
     * This method accepts 2 arrays of StringBuilder objects and writes them
     * to file.
     *
     * @param input         A StringBuilder array containing the original text.
     * @param output        A StringBuilder array containing the encoded or decoded
     * text.
     * @param outputFile    The file to which the output should be written.
     */
    private static void writeCodingToFile(String[] input,
        StringBuilder[] output, File outputFile) {

        try {
            // Create FileWriter object in append mode
            FileWriter outWriter = new FileWriter(outputFile, true);

            // Loop through array and write
            for (int i = 0; i < input.length; i++) {
                outWriter.write("\nInput: " + input[i]);
                outWriter.write("\nOutput: " + output[i] + "\n");
            }

            outWriter.close();

        } catch (IOException ioExc) {
            System.out.println("Error writing to file " + ioExc.getMessage() +
                ". Program exiting.");
        }
    }

    /**
     * This method takes clear text and encodes it as Huffman.
     *
     * @param freqTable         The frequency table.
     * @param clear             The clear text to be encoded.
     * @return encoded          StringBuilder object containing the encoded string
     */
    private static StringBuilder encodeHuffman(FreqTreeNode[] freqTable,
        String clear) {
        StringBuilder encoded = new StringBuilder();

        // Loop through the clear text array
        for (int i = 0; i < clear.length(); i++) {
            for (int j = 0; j < 26; j++) {
                if (clear.charAt(i) == freqTable[j].getCharacter()) {

                    // Append to the StringBuilder
                    encoded.append(freqTable[j].getHuffmanCode());
                }
            }
        }

        return encoded;
    }

    /**
     * This method traverses the Huffman tree and adds the Huffman codes to
     * each node.
     * @param encoded       The encoded text.
     * @param node          The root node of the Huffman tree.
     */
    private static void addHuffmanCodes(FreqTreeNode node, String encoded) {

        // Add the code when a leaf node is found; base case
        if (node.getLeft() == null && node.getRight() == null) {
            node.setHuffmanCode(encoded);
            return;
        }

        // Go to the left child
        addHuffmanCodes(node.getLeft(), encoded + "0");

        // Go to the right child
        addHuffmanCodes(node.getRight(), encoded + "1");
    }

    /**
     * This method traverses the Huffman tree and returns the structure of the
     * tree.
     * @param outputFile        The file to which output will be written.
     * @param huffmanTree       The root node of the Huffman tree.
     */
    private static void traverseHuffmanAndPrint(FreqTreeNode huffmanTree,
        File outputFile) {

        // Base case for when recursion should end
        if (huffmanTree == null) {
            return;
        }

        // Start at the root node
        printNodeToFile(huffmanTree, outputFile);

        // Go to the left child
        traverseHuffmanAndPrint(huffmanTree.getLeft(), outputFile);

        // Go to the right child
        traverseHuffmanAndPrint(huffmanTree.getRight(), outputFile);

    }

    /**
     * Builds the Huffman tree based on the priority queue passed from
     * main().
     *
     * @param nodeQueue     The priority queue containing frequency table data.
     * @return              The root node of the tree.
     */
    private static FreqTreeNode buildHuffmanTree(
        PriorityQueue<FreqTreeNode> nodeQueue) {
        FreqTreeNode rootNode = null;
        FreqTreeNode smallestNode;
        FreqTreeNode secondSmallestNode;
        FreqTreeNode tempNode;

        // Add to the tree while the priority queue is still empty
        while (nodeQueue.size() > 1) {

            // Get the smallest nodes
            smallestNode = nodeQueue.poll();
            secondSmallestNode = nodeQueue.poll();

            // Send to the tie breaker method
            tempNode = breakTies(smallestNode, secondSmallestNode);

            // Add tempNode back into the queue
            nodeQueue.add(tempNode);

            // Set the root node to be the tempNode; this will be incorrect
            // until the while loop is exited
            rootNode = tempNode;
        }

        return rootNode;
    }

    /**
     * This breaks ties between two nodes and returns the temporary, combined
     * node.
     *
     * @param smallestNode          The smallest node removed from the Priority Queue.
     * @param secondSmallestNode    The second smallest node removed from the
     * Priority Queue.
     * @return                      The temporary, combined node of these 2 nodes.
     */
    private static FreqTreeNode breakTies(FreqTreeNode smallestNode,
        FreqTreeNode secondSmallestNode) {

        // Assign the HuffmanSequence strings, if it's empty
        if (smallestNode.getHuffmanSequence() == null) {
            smallestNode.setHuffmanSequence(
                String.valueOf(smallestNode.getCharacter()));
        }

        if (secondSmallestNode.getHuffmanSequence() == null) {
            secondSmallestNode.setHuffmanSequence(
                String.valueOf(secondSmallestNode.getCharacter()));
        }

        // Assign a String value to the tempNode
        String newHuffman =
            smallestNode.getHuffmanSequence() + secondSmallestNode
                .getHuffmanSequence();
        FreqTreeNode tempNode = new FreqTreeNode(newHuffman,
            smallestNode.getFrequency() + secondSmallestNode.getFrequency());

        // Resolve a tie scenario by giving precedence to smaller letter groups
        if (smallestNode.getFrequency() == secondSmallestNode
            .getFrequency()) {
            if (smallestNode.getHuffmanSequence().length()
                < secondSmallestNode.getHuffmanSequence().length()) {
                tempNode.setLeft(smallestNode);
                tempNode.setRight(secondSmallestNode);
            }

            // If there is another tie and the frequencies are the same as
            // well as the # of characters in the sequence
            else if (smallestNode.getHuffmanSequence().length()
                == secondSmallestNode.getHuffmanSequence().length()) {

                // Then use the smaller character as the left node (giving
                // precedence to alphabetical order)
                if (smallestNode.getCharacter() < secondSmallestNode
                    .getCharacter()) {
                    tempNode.setLeft(smallestNode);
                    tempNode.setRight(secondSmallestNode);
                } else {
                    tempNode.setLeft(secondSmallestNode);
                    tempNode.setRight(smallestNode);
                }
            }
            // Swap the right and the left
            else {
                tempNode.setLeft(secondSmallestNode);
                tempNode.setRight(smallestNode);
            }
        }
        // There is no tie, so assign like normal
        else {
            // Assign the left and right pointers for the tempNode
            tempNode.setLeft(smallestNode);
            tempNode.setRight(secondSmallestNode);
        }

        return tempNode;
    }

    /**
     * This method imports the encoded text to a StringBuilder object.
     *
     * @param encodedTextFilename       The name of the file containing the encoded
     * text.
     * @return encodedText              A StringBuilder object containing the
     * encoded text, read from file.
     */
    private static StringBuilder importEncodedText(
        String encodedTextFilename) {

        StringBuilder encodedText = new StringBuilder();
        String tempLine;

        try {
            // Create the file and scanner objects
            File freqTableFile = new File(encodedTextFilename);
            Scanner freqTableScanner = new Scanner(
                new BufferedReader(new FileReader(freqTableFile)));

            // Read the file line by line
            while (freqTableScanner.hasNextLine()) {
                tempLine = freqTableScanner.nextLine();

                // Append temp line to the StringBuilder
                encodedText.append(tempLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return encodedText;
    }

    /**
     * This method imports the clear text file.
     *
     * @param clearTextFilename The name of the file containing the clear
     * text.
     * @return clearText        StringBuilder object of the clear text.
     */
    private static StringBuilder importClearText(String clearTextFilename) {

        StringBuilder clearText = new StringBuilder();
        String tempLine;

        try {
            // Create the file and scanner objects
            File freqTableFile = new File(clearTextFilename);
            Scanner freqTableScanner = new Scanner(
                new BufferedReader(new FileReader(freqTableFile)));

            // Read the file line by line
            while (freqTableScanner.hasNextLine()) {
                tempLine = freqTableScanner.nextLine();

                // Remove punctuation from string
                tempLine = tempLine.replaceAll("[\\p{P}|\\s]", "");

                // Convert all characters to uppercase for easier processing
                tempLine = tempLine.toUpperCase();

                // Append temp line to the StringBuilder
                clearText.append(tempLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return clearText;
    }

    /**
     * This method imports the frequency table and returns an array of String
     * objects that contain the frequency data.
     *
     * @param filename      The name of the file containing the frequency table.
     * @return freqTable    The frequency table, stored in an array of
     * Strings.
     */
    private static FreqTreeNode[] importFreqTable(String filename) {

        FreqTreeNode[] freqTable = new FreqTreeNode[26];
        String tempLine;
        String[] tempLineArray;
        int tempFreqVal;
        char tempChar;
        int i = 0;

        try {
            // Create the file and scanner objects
            File freqTableFile = new File(filename);
            Scanner freqTableScanner = new Scanner(
                new BufferedReader(new FileReader(freqTableFile)));

            // Read the file line by line
            while (freqTableScanner.hasNextLine()) {
                tempLine = freqTableScanner.nextLine();

                // Test if the line is empty; if so, continue to next loop
                if (tempLine.isEmpty()) {
                    continue;
                }

                // TODO all this is janky; just extract the number and character
                //  from each line using a regex
                // Split on some possible delimiter options
                tempLineArray = tempLine.split("([ :\\-ñ])+");

                // TODO improve this logic, if there's time
                // Try-catch block to handle weird ñ character that causes the
                // split to behave unpredictably
                // This was residual from when the IDE was interpreting the
                // input file as UTF-8; may no longer be needed
                try {
                    // Strip off EOL character by replacing any non-numeric
                    // characters from frequency value
                    String tempFreq = tempLineArray[1]
                        .replaceAll("[\\D]", "");
                    tempFreqVal = Integer.parseInt(tempFreq);

                } catch (NumberFormatException formatExc) {
                    // Strip off EOL character by replacing any non-numeric
                    // characters from frequency value
                    String tempFreq = tempLineArray[2]
                        .replaceAll("[\\D]", "");
                    tempFreqVal = Integer.parseInt(tempFreq);
                }

                // Convert String object containing the character to a char type
                tempChar = tempLineArray[0].charAt(0);

                // Add values to freqTableEntry object
                FreqTreeNode tempFreqTreeNode = new FreqTreeNode(tempChar,
                    tempFreqVal);

                // Store tempFreqTreeNode into array
                freqTable[i] = tempFreqTreeNode;
                i++;

            }
            freqTableScanner.close();
        } catch (FileNotFoundException fileExc) {
            System.out.println("File not found: " + fileExc.getMessage() +
                ". Program exiting.");
            System.exit(1);
        }

        return freqTable;
    }
}
