package me.daniilmann.weeks;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

class Result {

    /*
     * https://www.hackerrank.com/challenges/matrix-rotation-algo/problem
     *
     * Complete the 'matrixRotation' function below.
     *
     * The function accepts following parameters:
     *  1. 2D_INTEGER_ARRAY matrix
     *  2. INTEGER r
     */

    public static void matrixRotation(List<List<Integer>> matrix, int r) {
        int circle = 0;
        int m = matrix.size();
        int n = matrix.get(0).size();
        Integer[] jValues = new Integer[2];

        List<Integer> circleNumbers = null;
        int lastCircle = Math.min(n, m) / 2;
        while (circle < lastCircle) {
            circleNumbers = getCircle(matrix, circle, m, n);
            circleNumbers = shiftCircle(circleNumbers, r);
            replaceCircle(matrix, circleNumbers, circle);
            circle++;
        }
        printMatrix(matrix);
    }

    private static List<Integer> getCircle(List<List<Integer>> matrix, int circle, int m, int n) {
        List<Integer> circleNumbers = new ArrayList<>();
        int circleEnd = n - circle - 1;

        // add all numbers from first row of circle
        circleNumbers.addAll(matrix.get(circle).subList(circle, circleEnd + 1));

        // add numbers from rows in the middle of circle
        int maxI = m - circle - 1;
        int i = circle + 1;
        for (; i < maxI; i++) {
            circleNumbers.add(0, matrix.get(i).get(circle));
            circleNumbers.add(matrix.get(i).get(circleEnd));
        }

        // add numbers from last row of circle
        List<Integer> tmp = matrix.get(m - circle - 1).subList(circle, circleEnd + 1);
        Collections.reverse(tmp);
        circleNumbers.addAll(tmp);

        circleNumbers = shiftCircle(circleNumbers, i - circle - 1);

        return circleNumbers;
    }

    private static List<Integer> shiftCircle(List<Integer> circle, int r) {
        int split = r > circle.size() ? r % circle.size() : r;
        if (split == 0) {
            return circle;
        } else {
            List<Integer> newCircle = new ArrayList<>();
            newCircle.addAll(circle.subList(split, circle.size()));
            newCircle.addAll(circle.subList(0, split));
            return newCircle;
        }
    }

    private static void replaceCircle(List<List<Integer>> matrix, List<Integer> circleNumbers, int circle) {
        int circleEnd = matrix.get(0).size() - circle;
        int length = circleEnd - circle;

        // replace first row
        List<Integer> row = matrix.get(circle);
        ListIterator<Integer> iterator = circleNumbers.subList(0, length).listIterator();
        for (int i = circle; i < circleEnd; i++) {
            row.set(i, iterator.next());
        }

        // replace middle rows
        int maxI = matrix.size() - circle - 1;
        int end = circleEnd - 1;
        int steps = 0;
        for (int i = circle + 1; i < maxI; i++) {
            matrix.get(i).set(end, circleNumbers.get(length + steps));
            matrix.get(i).set(circle, circleNumbers.get(circleNumbers.size() - steps - 1));
            steps++;
        }

        // replace last row
        row = matrix.get(matrix.size() - circle - 1);
        iterator = circleNumbers.subList(length + steps, 2 * length + steps).listIterator(0);
        for (int i = circleEnd - 1; i >= circle; i--) {
            row.set(i, iterator.next());
        }
    }

    private static void printMatrix(List<List<Integer>> matrix) {
        int m = matrix.get(0).size();
        for (int i = 0; i < matrix.size(); i++) {
            StringJoiner stringJoiner = new StringJoiner(" ");
            for (int j = 0; j < m; j++) {
                stringJoiner.add(matrix.get(i).get(j).toString());
            }
            System.out.println(stringJoiner.toString());
        }
    }

}

public class Week1 {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        String[] firstMultipleInput = bufferedReader.readLine().replaceAll("\\s+$", "").split(" ");

        int m = Integer.parseInt(firstMultipleInput[0]);

        int n = Integer.parseInt(firstMultipleInput[1]);

        int r = Integer.parseInt(firstMultipleInput[2]);

        List<List<Integer>> matrix = new ArrayList<>();

        IntStream.range(0, m).forEach(i -> {
            try {
                matrix.add(
                        Stream.of(bufferedReader.readLine().replaceAll("\\s+$", "").split(" "))
                                .map(Integer::parseInt)
                                .collect(toList())
                );
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        Result.matrixRotation(matrix, r);

        bufferedReader.close();
    }

}
