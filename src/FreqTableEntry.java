/**
 * This class stores an entry to the frequency table after it is imported from file. These objects
 * are intended to be used in an array of similar objects to store the frequency table.
 *
 * @author Skyler Carlson
 * @version 1.0
 * @since 2019-04-19
 */
public class FreqTableEntry {

  private char character;
  private int frequency;

  /**
   * This is the constructor for the frequency table entry
   * @param character   The character to be stored.
   * @param frequency   The frequency of the character being stored.
   */
  public FreqTableEntry(char character, int frequency) {
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
   * This toString method prints the value for easy viewing.
   */
  public String toString() {
    return character + " : " + frequency;
  }
}
