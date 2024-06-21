import java.util.Scanner;
import java.util.Arrays;

public class BoyerMoore {

    // Computes the last occurrence of each character in the pattern.
    public int[] preprocessBadCharacters(char[] pattern) {
        int alphabetSize = 256;                       // Assuming ASCII characters
        int[] lastOccurence = new int[alphabetSize];  // Array to store the last occurrence index of each
                                                      // character in the pattern

        // Initialize the lastOccurrence array with -1
        Arrays.fill(lastOccurence, -1);

        // Store the index of the last occurrence of each character in the pattern
        for (int i = 0; i < pattern.length; i++) {
            lastOccurence[pattern[i]] = i;
        }

        // Return the last occurrence array
        return lastOccurence;
    }

    // Preprocesses the good suffix table for pattern matching.
    public int[] preprocessGoodSuffix(char[] pattern) {
        int m = pattern.length;                // Store pattern length
        int[] borderPosition = new int[m + 1]; // Array to store border positions
        int[] shift = new int[m + 1];          // Array to store shift values

        int i = m, j = m + 1;
        borderPosition[i] = j; // Initialize the borderPosition for the last character

        // Strong suffix preprocessing
        while (i > 0) {
            while (j <= m && pattern[i - 1] != pattern[j - 1]) {
                if (shift[j] == 0) {
                    shift[j] = j - i; // Calculate shift value
                }
                j = borderPosition[j];  // Update j using borderPosition
            }
            i--;
            j--;
            borderPosition[i] = j; // Update border position
        }

        // Partial suffix preprocessing
        j = borderPosition[0];
        for (i = 0; i <= m; i++) {
            if (shift[i] == 0) {
                shift[i] = j;  // Update shift value
            }
            if (i == j) {
                j = borderPosition[j]; // Update j using borderPosition
            }
        }

        // Return the shift array
        return shift;
    }

    // Search for pattern in text using Boyer-Moore algorithm
    public int search(String text, String pattern) {

        // Input validation
        if (text == null || pattern == null || text.length() < pattern.length()) {
            return -1; // No match possible
        }
        else if (pattern.isEmpty()) {
            return 0; // Pattern is empty, match at position 0
        }

        // Converts the text and pattern to character arrays
        char[] textCharArray = text.toCharArray();
        char[] patternCharArray = pattern.toCharArray();

        // Preprocess good suffix and bad character tables
        int[] shift = preprocessGoodSuffix(patternCharArray); // Combined good suffix heuristic
        int[] lastOccurrence = preprocessBadCharacters(patternCharArray); // Bad characters heuristic

        int i = 0; // Starting index in the text where the pattern is being compared

        // Search for pattern in text
        while (i <= textCharArray.length - patternCharArray.length) {
            int j = patternCharArray.length - 1; // Process pattern from right to left backwards

            // Compare pattern characters with text characters from right to left
            while (j >= 0 && patternCharArray[j] == textCharArray[i + j]) {
                j--;
            }


            // Print the current state of comparison
            System.out.println(" ");
            System.out.println("Text:    " + text);
            System.out.println("Pattern: " + " ".repeat(i) + new String(patternCharArray));

            // Complete match found
            if (j < 0) {
                return i; // Return the position of the match
            }
            else {
                // Mismatch found
                // Store the maximum of the values given by the good-suffix and the bad-character heuristics
                int max = Math.max(shift[j + 1], j - lastOccurrence[textCharArray[i + j]]);


                // Print the mismatch information
                System.out.println(" ".repeat(9 + i + j) + "^");
                System.out.println("Mismatch at index " + j + " (pattern) and " + (i + j) + " (text)");
                System.out.println("Shifting pattern by " + max + " positions");

                // Shift pattern by adding the maximum of the values given
                // by the good-suffix and the bad-character heuristics
                i = i + max;
            }
        }

        return -1; // No match found
    }


    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);
        BoyerMoore bm = new BoyerMoore();

        // Input text and pattern from the user
        System.out.print("Enter a text: ");
        String text = input.nextLine();

        System.out.print("Enter the pattern: ");
        String pattern = input.nextLine();
        System.out.println("--------------------------------------------");

        // Search for the pattern in the text
        int result = bm.search(text, pattern);

        // Display the result
        System.out.println("---------------------------------------------");
        if (result == -1)
            System.out.println("No match pattern found in the given text.");

        else
            System.out.println("Pattern match at position: " + result);

        System.out.println("---------------------------------------------");
    }
}
