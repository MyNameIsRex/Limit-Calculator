package psychologytheory.limitcalculator;

import psychologytheory.limitcalculator.evaluatemethods.DirectSubstitutionMethod;

import java.util.Objects;
import java.util.Scanner;

public class LimitCalculator {

    private Scanner scanner;
    private int startSolvingTime;
    private int endSolvingTime;
    private int currentStepNum = 0;

    private DirectSubstitutionMethod directSubstitutionMethod;

    public String problem = "";
    public String answer = "";

    public static void main(String[] args) {
        new LimitCalculator();
    }

    public LimitCalculator() {
        this.scanner = new Scanner(System.in);
        this.startSolvingTime = 0;
        this.endSolvingTime = 0;
        this.directSubstitutionMethod = new DirectSubstitutionMethod();

        String problemInput = "";

        System.out.println("====================================================================================================================\n");
        System.out.println("Welcome to the Humanized Limit Calculator v 0.1 beta, this calculator is designed to follow the steps a human takes!\n");
        System.out.println("Please enter a problem\n");
        System.out.println("Format Example: lim x -> 0 ( 1 / x + x )\n");
        System.out.println("Note: The parentheses and spaces are mandatory!\n");
        System.out.println("Note: Add an asterisk ' * ' between the multiplicand and the multiplier!\n");
        System.out.println("Note: The format _( a _/ b ) represents a fraction, \n\nthe representation aids the program of identifying between normal division and fractions!\n");
        System.out.println("====================================================================================================================\n");

        problemInput = this.scanner.nextLine();

        while (!isFormatCorrect(problemInput)) {
            System.out.println("The problem does not follow the required format, please check the format example above!");
            problemInput = scanner.nextLine();
        }

        this.scanner.close();
        this.problem = problemInput;

        this.answer = solveProblem(this.problem);
        System.out.println("====================================================================================================================\n");
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
                case '-' -> limit.append("-");
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

        this.currentStepNum++;
        hasLimit = this.directSubstitutionMethod.directSubstitution(this.currentStepNum, variable, Integer.parseInt(limit.toString()), expression).hasLimit();

        endSolvingTime = System.nanoTime();
        overallSolveTime = (float) (endSolvingTime - startSolvingTime) / 1000000000;
        return "\nVariable: " + variable + " | " + "Limit: " + limit + " | " + "Expression: " + expression + " | " + "Solving Time: " + Math.round(overallSolveTime) + " seconds";
    }
}
