/**
 * This class stores an entry to the frequency table after it is imported from file. These objects
 * are intended to be used in an array of similar objects to store the frequency table.
 *
 * @author Skyler Carlson
 * @version 1.0
 * @since 2019-04-19
 */
public class FreqTreeNode implements Comparable<FreqTreeNode> {

  private char character;
  private int frequency;
  private FreqTreeNode left = null;
  private FreqTreeNode right = null;
  private FreqTreeNode parent = null;
  private String huffmanSequence;
  private String huffmanCode = "";

    /**
     * This is the constructor for a Huffman tree node, when a string is passed
     * @param charString        The string of characters to be used by the Huffman tree
     * @param frequency         The frequency that this character or string appears.
     */
  public FreqTreeNode(String charString, int frequency) {
      this.huffmanSequence = charString;
      this.frequency = frequency;
  }

  /**
   * This is the constructor for the frequency table entry when a character is passed
   * @param character   The character to be stored.
   * @param frequency   The frequency of the character being stored.
   */
  public FreqTreeNode(char character, int frequency) {
    this.character = character;
    this.frequency = frequency;
  }

    /**
     * Default constructor for the frequency table entry
     */
    public FreqTreeNode() {

    }

  /**
   * Getter method for the character.
   * @return    The character being stored by the object.
   */
  public char getCharacter() {
    return this.character;
  }

  /**
   * Getter method for the frequency.
   * @return    Frequency of the character being stored.
   */
  public int getFrequency() {
    return this.frequency;
  }

    /**
     * Setter method for the frequency.
     * @param freq      Frequency valud for the node
     */
  public void setFrequency(int freq) {
      this.frequency = freq;
  }

    /**
     * Getter method for the left node.
     * @return The left node
     */
  public FreqTreeNode getLeft() {
      return this.left;
  }

    /**
     * Getter method for the right node.
     * @return The right node
     */
    public FreqTreeNode getRight() {
        return this.right;
    }


    /**
     * The setter method for the left node.
     * @param left      The left node to be assigned.
     */
    public void setLeft(FreqTreeNode left) {
        this.left = left;
    }


    /**
     * The setter method for hte right node.
     * @param right     The right node to be assigned.
     */
    public void setRight(FreqTreeNode right) {
        this.right = right;
    }

    /**
     * Getter method for the parent node.
     * @return      The parent node for this node.
     */
    public FreqTreeNode getParent() {
        return this.parent;
    }

    /**
     * Setter method for the parent of the node.
     * @param parent    The parent node for this node.
     */
    public void setParent(FreqTreeNode parent) {
        this.parent = parent;
    }

    /**
     * Setter method for the Huffman character sequence.
     * @param sequence      This is the string of chars that the Huffman tree will use.
     */
    public void setHuffmanSequence(String sequence) {
        this.huffmanSequence = sequence;
    }

    /**
     * Getter method for the Huffman character sequence.
     * @return String of characters that denotes the Huffman sequence.
     */
    public String getHuffmanSequence() {
        return this.huffmanSequence;
    }

  /**
   * Getter method for the Huffman code value.
   * @return Huffman code for this node
   */
  public String getHuffmanCode() {
      return this.huffmanCode;
    }

  /**
   * This method sets the Huffman code for the node.
   * @param code      The Huffman code for the node.
   */
  public void setHuffmanCode(String code) {
      this.huffmanCode = code;
    }

    /**
     * This method overrides the default compareTo() method in the priority queue so the custom
     * fields can be used as the priority queue ordering.
     * @param node      The node to which this node is being compared.
     * @return          Integer values indicating the results of the comparison.
     */
    @Override
    public int compareTo(FreqTreeNode node) {
      // TODO revert this back to original
      //int temp Integer.compare();

        return Integer.compare(this.getFrequency(), node.getFrequency());


      /**
      // Tie breaker for the compareTo, so there is a secondary sort order (not just frequency values)
      if (this.getFrequency() == node.getFrequency()) {
        if (this.getHuffmanSequence().compareTo(node.getHuffmanSequence()) < 0) {
          return -1;
        }
        else {
          return 1;
        }

      }
      else if (this.getFrequency() > node.getFrequency()) {
        return 1;
      }
      else if (this.getFrequency() < node.getFrequency()) {
        return -1;
      }
      else {
        return 0;
      } **/

    }

  /**
   * This toString method prints the value for easy viewing.
   */
  public String toString() {
    if (this.huffmanSequence != null) {
      return this.huffmanSequence + " : " + this.frequency;

    }
    else {
      return this.character + " : " + this.frequency + this.huffmanCode;
    }

  }
}
