package psychologytheory.limitcalculator;

import java.util.Scanner;

public class LimitCalculator {

    private Scanner scanner;
    private int startSolvingTime;
    private int endSolvingTime;

    public String problem = "";
    public String answer = "";

    public static void main(String[] args) {
        new LimitCalculator();
    }

    public LimitCalculator() {
        this.scanner = new Scanner(System.in);
        this.startSolvingTime = 0;
        this.endSolvingTime = 0;

        String problemInput = "";

        System.out.println("===================================================================\n");
        System.out.println("Welcome to the Limit Calculator, the calculator will solve the problems entered like a human!\n");
        System.out.println("Please enter a problem\n");
        System.out.println("Format Example: lim x -> 0 (1 / x + x)\n");
        System.out.println("Note: The parentheses and spaces are mandatory!\n");
        System.out.println("===================================================================\n");

        problemInput = scanner.nextLine();

        while (!isFormatCorrect(problemInput)) {
            System.out.println("The problem does not follow the required format, please check the format example above!");
            problemInput = scanner.nextLine();
        }

        this.problem = problemInput;

        this.answer = solveProblem(this.problem);
        System.out.println(this.answer);
    }

    private boolean isFormatCorrect(String input) {
        return (input.startsWith("lim ")) && (input.contains(" -> ") && (input.contains("("))) && (input.endsWith(")"));
    }

    private String solveProblem(String problem) {
        long startSolvingTime = System.nanoTime();
        long endSolvingTime = 0;
        float overallSolveTime = 0;
        char variable = problem.charAt(4);
        StringBuilder limit = new StringBuilder();
        String expression = "";
        boolean hasLimit;
        String answer = "";

        for (int i = 9; i < problem.length(); i++) {
            switch (problem.charAt(i)) {
                case '0' -> limit.append(0);
                case '1' -> limit.append(1);
                case '2' -> limit.append(2);
                case '3' -> limit.append(3);
                case '4' -> limit.append(4);
                case '5' -> limit.append(5);
                case '6' -> limit.append(6);
                case '7' -> limit.append(7);
                case '8' -> limit.append(8);
                case '9' -> limit.append(9);
            }

            if (problem.charAt(i) == ' ') {
                expression = problem.substring(i + 2, problem.length() - 1);
                break;
            }
        }
        endSolvingTime = System.nanoTime();
        overallSolveTime = (float) (endSolvingTime - startSolvingTime) / 1000000000;
        hasLimit = directSubstitution(variable, Integer.parseInt(limit.toString()), expression).hasLimit();

        while(!hasLimit) {

        }

        return "Variable: " + variable + " | " + "Limit: " + limit + " | " + "Expression: " + expression + " | " + "Solving Time: " + Math.round(overallSolveTime) + " seconds";
    }

    private LimitTypes directSubstitution(char variable, int limit, String expression) {
        StringBuilder substitutedExpression = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == variable) {
                substitutedExpression.append(limit);
                continue;
            }

            substitutedExpression.append(expression.charAt(i));
        }

        System.out.println(substitutedExpression);
        return LimitTypes.DOES_NOT_EXIST;
    }
}
