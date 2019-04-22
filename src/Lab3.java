/*
  This program implements Huffman encoding. It imports a frequency table containing frequencies of
  letters, some clear (unencoded) text, and some encoded text. It translates the clear text to
  Huffman-encoded text, and decodes the encoded data, outputting all to a file. The user must
  specify input and output file names in the runtime arguments.

  @author Skyler Carlson
  @version 1.0
  @since 2019-04-19
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Lab3 {

    public static void main(String[] args) {

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
        String[] clearTextArray = clearText.toString().split("\\u00A0");
        //System.out.println(Arrays.toString(clearTextArray));

        // Read in the encoded file
        StringBuilder encodedText = null;
        try {
            encodedText = importEncodedText(encodedTextFilename);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Split encodedText on \u00A0 character to put into an array
        String[] encodedArray = encodedText.toString().split("\\u00A0");
        //System.out.println(Arrays.toString(encodedArray));

        // Create a priority queue of FreqTreeNode objects
        //PriorityQueue nodeQueue = new PriorityQueue(26, )
        PriorityQueue<FreqTreeNode> nodeQueue = new PriorityQueue<FreqTreeNode>();

        // Add elements to the Priority Queue
        nodeQueue.addAll(Arrays.asList(freqTable).subList(0, 26));

        // Test print area
        // TODO remove
        //System.out.println("Priority queue: " + nodeQueue);
        //while (!nodeQueue.isEmpty()) {
        //    System.out.println(nodeQueue.remove());
        //}

        // Build the Huffman tree based on the frequency table
        FreqTreeNode huffmanTree = new FreqTreeNode();
        huffmanTree = buildHuffmanTree(nodeQueue);

        // Traverse the Huffman tree, printing as we go along
        traverseHuffman(huffmanTree);

        // Encode a string using the Huffman tree, and write to file
        //String encoded = encodeHuffman(huffmanTree, clearTextArray[0]);
        //System.out.println(clearTextArray[0] + " is " + encoded);
        addHuffmanCodes(huffmanTree, "");

        //System.out.println(encodedText.toString());

        System.out.println("\nProgram exiting...");
    }

    /**
     * This method traverses the Huffman tree and adds the Huffman codes to each node.
     * @param node
     */
    private static void addHuffmanCodes(FreqTreeNode node, String encoded) {

        // Add the code when a leaf node is found
        if (node.getLeft() == null && node.getRight() == null) {
            node.setHuffmanCode(encoded);
            System.out.println(node.getCharacter() + " : " + encoded);
            return;
        }

        // Go to the left child
        addHuffmanCodes(node.getLeft(), encoded + "0");

        // Go to the right child
        addHuffmanCodes(node.getRight(), encoded + "1");
    }

    /**
     * This method traverses the Huffman tree and returns the structure of the tree.
     * @param huffmanTree       The root node of the Huffman tree.
     */
    private static void traverseHuffman(FreqTreeNode huffmanTree) {

        // Base case for when recursion should end
        if (huffmanTree == null) {
            return;
        }

        // Start at the root node
        //System.out.println(huffmanTree.toString());

        // Go to the left child
        traverseHuffman(huffmanTree.getLeft());

        // Go to the right child
        traverseHuffman(huffmanTree.getRight());


    }

    /**
     * Builds the Huffman tree based on the priority queue passed from main().
     * @param nodeQueue     The priority queue containing frequency table data.
     * @return              The root node of the tree.
     */
    private static FreqTreeNode buildHuffmanTree(PriorityQueue<FreqTreeNode> nodeQueue) {
        FreqTreeNode rootNode = null;
        FreqTreeNode smallestNode;
        FreqTreeNode secondSmallestNode;

        // Add to the tree while the priority queue is still empty
        while (nodeQueue.size() > 1) {

            // Get the smallest nodes
            smallestNode = nodeQueue.poll();
            secondSmallestNode = nodeQueue.poll();

            // Assign the HuffmanSequence strings, if it's empty
            if (smallestNode.getHuffmanSequence() == null) {
                smallestNode.setHuffmanSequence(String.valueOf(smallestNode.getCharacter()));
            }

            if (secondSmallestNode.getHuffmanSequence() == null) {
                secondSmallestNode.setHuffmanSequence(String.valueOf(secondSmallestNode.getCharacter()));
            }


            // Assign a String value to the tempNode
            //String smallestNodeStr = String.valueOf(smallestNode.getCharacter());
            //String secondSmallestNodeStr = String.valueOf(secondSmallestNode.getCharacter());
            //FreqTreeNode tempNode = new FreqTreeNode(smallestNodeStr + secondSmallestNodeStr, smallestNode.getFrequency() + secondSmallestNode.getFrequency());
            //String smallestNodeStr = String.valueOf(smallestNode.getCharacter());
            //String secondSmallestNodeStr = String.valueOf(secondSmallestNode.getCharacter());
            String newHuffman = smallestNode.getHuffmanSequence() + secondSmallestNode.getHuffmanSequence();
            FreqTreeNode tempNode = new FreqTreeNode(newHuffman, smallestNode.getFrequency() + secondSmallestNode.getFrequency());

            // Assign the left and right pointers for the tempNode
            tempNode.setLeft(smallestNode);
            tempNode.setRight(secondSmallestNode);

            // Add tempNode back into the queue
            nodeQueue.add(tempNode);

            // Set the root node to be the tempNode; this will be incorrect until the while loop is exited
            rootNode = tempNode;
        }


        return rootNode;
    }

    /**
     * This method imports the encoded text to a StringBuilder object.
     * @param encodedTextFilename       The name of the file containing the encoded text.
     * @return encodedText              A StringBuilder object containing the encoded text, read from file.
     */
    private static StringBuilder importEncodedText(String encodedTextFilename) {

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
     * @param clearTextFilename The name of the file containing the clear text.
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

                // Remove weird whitespace from string
                //tempLine = tempLine.replaceAll("\\u00A0", "");

                // Convert all characters to uppercase for easier processing
                tempLine = tempLine.toUpperCase();

                // Append temp line to the StringBuilder
                clearText.append(tempLine);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }




        //System.out.println(clearText.toString());
        return clearText;
    }

    /**
     * This method imports the frequency table and returns an array of String objects that contain
     * the frequency data.
     * @param filename      The name of the file containing the frequency table.
     * @return freqTable    The frequency table, stored in an array of Strings.
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
            Scanner freqTableScanner = new Scanner(new BufferedReader(new FileReader(freqTableFile)));

            // Read the file line by line
            while (freqTableScanner.hasNextLine()) {
                tempLine = freqTableScanner.nextLine();

                // Test if the line is empty; if so, continue to next loop
                if (tempLine.isEmpty()) {
                    continue;
                }

                // TODO all this is janky; just extract the number and character from each line using a regex
                // Split on some possible delimiter options
                tempLineArray = tempLine.split("([ :\\-ñ])+");

                // TODO improve this logic, if there's time
                // Try-catch block to handle weird ñ character that causes the split to behave unpredictably
                // This was residual from when the IDE was interpreting the input file as UTF-8
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
                FreqTreeNode tempFreqTreeNode = new FreqTreeNode(tempChar, tempFreqVal);

                // Store tempFreqTreeNode into array
                freqTable[i] = tempFreqTreeNode;
                i++;

                // TODO remove temp print freqtable entry
                //System.out.println(tempFreqTreeNode.toString());

            }
            freqTableScanner.close();
        }

        catch (FileNotFoundException fileExc) {
            System.out.println("File not found: " + fileExc.getMessage() +
                ". Program exiting.");
            System.exit(1);
        }

        return freqTable;
    }
}
