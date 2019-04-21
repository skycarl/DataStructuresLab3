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

  /**
   * This is the constructor for the frequency table entry
   * @param character   The character to be stored.
   * @param frequency   The frequency of the character being stored.
   */
  public FreqTreeNode(char character, int frequency) {
    this.character = character;
    this.frequency = frequency;
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
     * This method overrides the default compareTo() method in the priority queue so the custom
     * fields can be used as the priority queue ordering.
     * @param node      The node to which this node is being compared.
     * @return          Integer values indicating the results of the comparison.
     */
    @Override
    public int compareTo(FreqTreeNode node) {
        // TODO accept this suggestion
        if (this.getFrequency() > node.getFrequency()) {
            return 1;
        }
        else if (this.getFrequency() < node.getFrequency()) {
            return -1;
        }
        else {
            return 0;
        }

    }

  /**
   * This toString method prints the value for easy viewing.
   */
  public String toString() {
    return character + " : " + frequency;
  }
}
