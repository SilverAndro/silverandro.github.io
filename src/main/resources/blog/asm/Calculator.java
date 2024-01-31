package dev.silverandro.tutorial;

import java.util.Scanner;

public class Calculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("enter the calculation:");
        String input = scanner.nextLine();
        int result = calculateResult(input);
        System.out.println("result: " + result);
    }

    private static int calculateResult(String input) {
        int current = 0;
        char currentOperation = '\0';
        String[] split = input.split(" ");

        for (String part : split) {
            if (stringIsInteger(part)) {
                int value = Integer.parseInt(part);
                switch (currentOperation) {
                    case '\0' -> current = value;
                    case '+' -> current = current + value;
                    case '-' -> current = current - value;
                    case '/' -> current = current / value;
                    case '*' -> current = current * value;
                }
            } else {
                currentOperation = part.charAt(0);
            }
        }

        return current;
    }

    private static boolean stringIsInteger(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException exception) {
            return false;
        }
        return true;
    }
}