/*
Key points to be observed when solving the problem:

1. The total number of '1' should be divisible by three.
2. The number of '0' between the groups (defined by their first and last '1') should be at least as much as the trailing '0' after the last group.
3. If both of the above are fulfilled, then check for a complete much between the groups starting from first '1' to the last '1' for each group.

Step 1 and 2 can be done after only one iteration. Step 3 needs one additional iteration, so for the sake of streamiling the code, it is best this to be the last step.
*/

public class Solution {

    public final int[] THREE_EQUAL_PARTS_IMPOSSIBLE = new int[]{-1, -1};
    public int[][] groups;//groups defined by their first and last '1'.
    public int totalOnes;
    public int trailingZeros;
    public int lastIndex;

    public int[] threeEqualParts(int[] arr) {

        lastIndex = arr.length - 1;
        totalOnes = 0;

        for (int n : arr) {
            if (n == 1) {
                totalOnes++;
            }
        }

        if (totalOnes % 3 != 0) {
            return THREE_EQUAL_PARTS_IMPOSSIBLE;
        }

        if (totalOnes == 0) {
            return new int[]{0, lastIndex};
        }

        if (totalOnes % 3 == 0 && totalOnes == arr.length) {
            return new int[]{(arr.length / 3) - 1, 2 * (arr.length / 3)};
        }

        findIndexes_firstOne_and_lastOne_for_theThreeGroups(arr);
        if (!groupsHaveSameLength_from_firstOne_to_lastOne()) {
            return THREE_EQUAL_PARTS_IMPOSSIBLE;
        }

        if (!check_zerosBetweenGroups_equalOrAreGreater_thanAnyTrailingZeros()) {
            return THREE_EQUAL_PARTS_IMPOSSIBLE;
        }

        if (!checkForMatchWithinGroups(arr)) {
            return THREE_EQUAL_PARTS_IMPOSSIBLE;
        }

        return new int[]{groups[0][1] + trailingZeros, groups[1][1] + trailingZeros + 1};
    }

    public void findIndexes_firstOne_and_lastOne_for_theThreeGroups(int[] arr) {
        groups = new int[3][2];
        for (int i = 0; i < groups.length; i++) {
            searchIndexes(arr, i, (i > 0 ? groups[i - 1][1] + 1 : 0));
        }
    }

    public void searchIndexes(int[] arr, int currentGroup, int currentStart) {

        int counter = 0;
        for (int i = currentStart; i < arr.length; i++) {
            if (arr[i] == 1) {
                groups[currentGroup][0] = i;
                counter++;
                break;
            }
        }
        if (counter == totalOnes / 3) {
            groups[currentGroup][1] = groups[currentGroup][0];
            return;
        }

        for (int i = groups[currentGroup][0] + 1; i < arr.length; i++) {
            if (arr[i] == 1 && ++counter == totalOnes / 3) {
                groups[currentGroup][1] = i;
                break;
            }
        }
    }

    public boolean groupsHaveSameLength_from_firstOne_to_lastOne() {
        int length = groups[0][1] - groups[0][0];
        for (int i = 1; i < groups.length; i++) {
            if (groups[i][1] - groups[i][0] != length) {
                return false;
            }
        }
        return true;
    }

    public boolean check_zerosBetweenGroups_equalOrAreGreater_thanAnyTrailingZeros() {
        trailingZeros = lastIndex - groups[2][1];
        for (int i = 0; i < groups.length - 1; i++) {
            if (groups[i + 1][0] - groups[i][1] - 1 < trailingZeros) {
                return false;
            }
        }
        return true;
    }

    public boolean checkForMatchWithinGroups(int[] arr) {

        int add_first_to_second = groups[1][0] - groups[0][0];
        int add_first_to_third = groups[2][0] - groups[0][0];

        int start = groups[0][0];
        int end = groups[0][1];

        for (int i = start; i <= end; i++) {
            int value = arr[i];
            if (arr[i + add_first_to_second] != value || arr[i + add_first_to_third] != value) {
                return false;
            }
        }
        return true;
    }
}
