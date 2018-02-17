package kakuro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A class to generate partitions of values.
 */
class Partition {
    /**
     * A cache to save already calculated values.
     */
    private static final HashMap<Integer, List<List<Integer>>> cachedValues = new HashMap<>();

    /**
     * Get all partitions for a specified value.
     *
     * @param n The value which should be partitioned.
     * @return A list of the partitions.
     */
    public static List<List<Integer>> partition(int n) {
        if (cachedValues.containsKey(n)) {
            return cachedValues.get(n);
        }
        List<List<Integer>> result = innerPartition(n);
        cachedValues.put(n, result);
        return result;
    }

    /**
     * Get all partitions for a specified value.
     *
     * @param n The value which should be partitioned.
     * @return A list of the partitions.
     */
    private static List<List<Integer>> innerPartition(int n) {
        ArrayList<String> result = new ArrayList<>();
        partition(n, n, "", result);
        List<List<Integer>> intResult = new ArrayList<>();
        for (String entry : result) {
            String[] numberStrings = entry.split(" ");
            ArrayList<Integer> numbers = new ArrayList<>();
            for (String numberString : numberStrings) {
                if (numberString.length() == 0) continue;
                numbers.add(Integer.parseInt(numberString));
            }
            intResult.add(numbers);
        }
        return intResult;
    }

    /**
     * A recursive method to generate the partitions.
     *
     * @param n      The value which should be partitioned.
     * @param max    The maximum.
     * @param prefix The already generated prefix.
     * @param result The list to add the results to.
     */
    private static void partition(int n, int max, String prefix, ArrayList<String> result) {

        if (n == 0) {
            result.add(prefix);
            return;
        }

        for (int i = Math.min(max, n); i >= 1; i--) {
            partition(n - i, i, prefix + " " + i, result);
        }
    }
}
