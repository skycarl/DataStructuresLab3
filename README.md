# DataStructuresLab3
Lab #3 for Data Structures

This program implements Huffman coding. The user must specify a frequency table in the following format:
A - 1
B - 2
...etc

OR

A : 1
B : 2
...etc

The user must then specify input files containing clear text and Huffman-encoded text. The program will then encode the clear text and decode the encoded text, and print all results to a file.

Note: the input files give in this assignment were ended as Windows-1252. If unpredictable behavior occurs, ensure that your IDE is set to recognize the encoding of the input files.

javac Lab3.java
java Lab3 FreqTable.txt ClearText.txt Encoded.txt

IDE: IntelliJ IDEA 2019.1 (Community Edition)
JRE: 1.8.0_152-release-1343-b26 x86_64
JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
OS: macOS Mojave 10.14.4
