
// Spell Checker - Takes in input.txt, Returns output.txt

import java.io.*;
import java.util.*;
import java.lang.*;

public class CS245A1 {

    private static Trie trie = new Trie();
    // Uses Self-balancing Binary Tree - AVL Tree
    private static Tree<String> tree = new Tree<>();
    // Data Structure is 'trie' or 'tree' Based on 'a1properties.txt'
    private static String struct;

    public static void main(String[] args) {
        // MUST Run before 'readDictionary' to Take Effect
        struct = readConfig();

        // Call Function to Handle 'english.0'
        if (readDictionary()) {
            System.out.println(struct.substring(0, 1).toUpperCase() + struct.substring(1) + " dictionary loaded!");
        } else {
            System.out.println("Terminating program.");
            System.exit(0);
        }

        // Array of Words From 'input.txt' for Checking Spelling
        String[] input = readInput(args[0]);

        // Establish 'output.txt' Write Capabilites
        try {
            PrintWriter writer = new PrintWriter(new FileWriter("output.txt"));
            // Logic Loop to Run Through Each Word in 'input.txt'
            for (int target = 0; target < input.length; target++) {
                // Returns an Array of Correct Words Based on Target
                String[] results = spellCheck(input[target]);
                // System.out.println("Correct Spellings: " + results.length);
                // Push Resultants to 'output.txt'
                writeOutput(results, writer);
            }
            writer.close();
        } catch (IOException ex) {
            // Can't Spell Check Without 'output.txt'
            System.out.println("output.txt' was not found!");
            ex.printStackTrace();
        }

    }

    // Determines if Trie or Tree is Used for Storage
    private static String readConfig() {
        try (InputStream input = new FileInputStream("a1properties.txt")) {
            Properties prop = new Properties();
            prop.load(input);
            return prop.getProperty("storage", "trie");
        } catch (IOException ex) {
            System.out.println("'a1properties.txt' could not be read! Defaulting to 'trie'.");
            ex.printStackTrace();
            return "trie";
        }
    }

    // Reads 'input.txt' Returns Array of Input Words
    private static String[] readInput(String injection) {
        try {
            BufferedReader input = new BufferedReader(new FileReader(injection));
            // BufferedReader input = new BufferedReader(new FileReader("input.txt"));

            // Log 1000 Characters Ahead as a Buffer to Reset to
            // This is a Limiting Factor
            input.mark(1000);
            // Determine the Number of Words
            long wCountL = input.lines().count();
            int wCountI = (int) wCountL;
            // Create an Array of the Proper Size
            String[] inputed = new String[wCountI];
            // Return to the First Line in Buffer
            input.reset();

            int i = 0;
            String wCheck;
            // Add Each Word to an Array
            while ((wCheck = input.readLine()) != null) {
                inputed[i++] = wCheck;
            }
            input.close();
            return inputed;
        } catch (IOException ex) {
            System.out.println("'input.txt' could not be read!");
            return null;
        }
    }

    private static void writeOutput(String[] hvt, PrintWriter writer) {
        // Array of One Word Spelled Correctly
        if (hvt.length == 1) {
            writer.printf(hvt[0]);
            writer.printf("\n");
        } else {
            for (int i = 0; i < hvt.length; i++) {
                writer.printf(hvt[i]);
                writer.printf("\t");
            }
            writer.printf("\n");
        }
    }

    // Search & Read in 'english.0'
    private static boolean readDictionary() {
        try {
            Scanner dictFile = new Scanner(new File("english.0"));
            // Persist Each Word
            while (dictFile.hasNextLine()) {
                String dictWord = dictFile.nextLine();
                if (!dictWord.isBlank()) {
                    String prepped = dictWord.toLowerCase();
                    persistFile(prepped);
                }
            }
            dictFile.close();
            return true;
        } catch (Exception e) {
            System.out.println("'english.0' not located!");
            e.printStackTrace();
            return false;
        }
    }

    // Add Word to Trie or Tree
    private static void persistFile(String payload) {
        if (struct == "tree") {
            tree.insert(payload);
        } else {
            trie.add(payload);
        }
    }

    // Handles Spell Checking for Valid & Suggested Words
    private static String[] spellCheck(String target) {
        if (isCorrect(target)) {
            String[] correct = new String[1];
            correct[0] = target;
            return correct;
        } else {
            // Return Array of Suggestions
            return suggestCorrect(target);
        }

    }

    // Function to Check if Word is Valid
    private static boolean isCorrect(String target) {
        // Make Sure to Call Correct Structure
        if (struct == "tree") {
            // Pass in Word to Check
            if (tree.has(target)) {
                return true;
            } else {
                return false;
            }
        } else {
            if (trie.contains(target)) {
                return true;
            } else {
                return false;
            }
        }
    }

    // Search Structure & Find Keys For Suggestions
    private static String[] suggestCorrect(String target) {
        // Make Sure to Call Correct Structure
        ArrayList<String> suggested = new ArrayList<>();
        if (struct == "tree") {
            int max = 0;
            String pre = target;
            String post = target;
            while (max < 3 && pre != null || post != null) {
                pre = tree.predecessor(pre);
                if (pre != null) {
                    suggested.add(pre);
                    max++;
                }
                post = tree.successor(post);
                if (post != null) {
                    suggested.add(post);
                    max++;
                }
            }
            // Convert to Array of Strings First
            return toStringArray(suggested);
        } else {
            int max = 0;
            here: for (int i = target.length(); i > 0; i++) {
                for (String w : trie.keysWithPrefix(target.substring(0, i - 1))) {
                    if (!w.equals(target)) {
                        suggested.add(w);
                        max++;
                        if (max >= 3) {
                            // I Know This is Bad
                            break here;
                        }
                    }
                }
            }
            // Convert to Array of Strings First
            return toStringArray(suggested);
        }
    }

    // Helper Function to Convert Array Lists to Array of Strings
    private static String[] toStringArray(ArrayList<String> al) {
        String as[] = new String[al.size()];
        for (int i = 0; i < al.size(); i++) {
            as[i] = al.get(i);
        }
        return as;
    }
}
