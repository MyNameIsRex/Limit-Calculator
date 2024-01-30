package psychologytheory.limitcalculator;

import java.util.Scanner;

public class LimitCalculator {
    public Scanner scanner;
    public String inputFunction = "";

    private DirectSubstitution directSubstitution;

    public static void main(String[] args) {
        new LimitCalculator();
    }

    public LimitCalculator() {
        this.scanner = new Scanner(System.in);

        this.setupCalculator();
    }

    private void setupCalculator() {
        while (!this.isValidInputFunction(this.inputFunction)) {
            LimitCalculator.prompt("\nEnter the function in the following format:\nlim x -> 0 ( x )");
            this.inputFunction = this.scanner.nextLine();
        }

        this.scanner.close();

        this.evaluateExpression(this.inputFunction);
    }

    private void evaluateExpression(String function) {
        directSubstitution = new DirectSubstitution(function);
    }

    public static void prompt(String prompt) {
        for (int i = 0; i < 75; i++) {
            System.out.print("=");
        }
        System.out.println(prompt);
        for (int i = 0; i < 75; i++) {
            System.out.print("=");
        }
        System.out.println();
    }

    private boolean isValidInputFunction(String function) {
        if (!function.contains("lim")) {
            return false;
        }

        if (!function.contains(" ( ") || !function.contains(" )")) {
            return false;
        }

        return function.contains(" -> ");
    }
}
