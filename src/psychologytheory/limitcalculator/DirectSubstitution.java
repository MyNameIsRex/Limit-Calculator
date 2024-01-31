package psychologytheory.limitcalculator;

import java.util.ArrayList;

public class DirectSubstitution {
    private int[] indiciesOfWhiteSpaces;
    private char variable;
    private String limit;
    private String expression;

    public DirectSubstitution(String function) {
        this.interpretExpression(function);
        this.expression = this.substitute(this.variable, this.limit, this.expression);
        while (this.expression.contains("+") || this.expression.contains("- ") || this.expression.contains("*") || this.expression.contains(" / ")) {
            this.indiciesOfWhiteSpaces = this.indiciesofwhitespaces(this.expression);
            this.expression = this.evaluateExpression(this.expression, this.indiciesOfWhiteSpaces);
        }
    }

    private void interpretExpression(String function) {
        this.indiciesOfWhiteSpaces = this.indiciesofwhitespaces(function);

        this.variable = function.charAt(this.indiciesOfWhiteSpaces[0] + 1);
        this.limit = function.substring(this.indiciesOfWhiteSpaces[2] + 1, this.indiciesOfWhiteSpaces[3]);
        this.expression = function.substring(this.indiciesOfWhiteSpaces[3] + 1);

        LimitCalculator.prompt("\nVariable: " + this.variable + " | Limit: " + this.limit + " | Expression: " + this.expression);
    }

    private String substitute(char variable, String limit, String expression) {
        StringBuilder newExpression = new StringBuilder();

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == variable) {
                newExpression.append(limit);
                continue;
            }
            newExpression.append(expression.charAt(i));
        }

        LimitCalculator.prompt("\nNew Expression: " + newExpression);
        return newExpression.toString();
    }

    private String evaluateExpression(String expression, int[] indiciesOfWhiteSpaces) {
        StringBuilder newExpression = new StringBuilder();
        String answer;

        StringBuilder a = new StringBuilder();
        StringBuilder b = new StringBuilder();
        boolean isAFraction = true, isBFraction = true;

        int replaceBeginIndex = 0, replaceEndIndex = 0;
        char operator = ' ';

        for (int i = 0; i < indiciesOfWhiteSpaces.length; i++) {
            //Check if we are at the last index of the array, this check is to prevent ArrayIndexOutOfBounds
            if (indiciesOfWhiteSpaces[i] == indiciesOfWhiteSpaces[indiciesOfWhiteSpaces.length - 1]) {
                break;
            }

            //Check operators
            if (expression.charAt(indiciesOfWhiteSpaces[i] + 1) == '(' || expression.charAt(indiciesOfWhiteSpaces[i] + 1) == ')' ||
                expression.charAt(indiciesOfWhiteSpaces[i] + 1) == '+' || expression.charAt(indiciesOfWhiteSpaces[i] + 1) == '-' && expression.charAt(indiciesOfWhiteSpaces[i] + 2) == ' ' ||
                expression.charAt(indiciesOfWhiteSpaces[i] + 1) == '*' || expression.charAt(indiciesOfWhiteSpaces[i] + 1) == '/'||
                expression.charAt(indiciesOfWhiteSpaces[i] + 1) == '^') {

                operator = expression.charAt(indiciesOfWhiteSpaces[i] + 1);

                //TODO: Fix ArrayIndexOutOfBounds
                if (expression.charAt(indiciesOfWhiteSpaces[i - 2] + 1) == '_') {
                    replaceBeginIndex = indiciesOfWhiteSpaces[i - 3] + 1;
                    isAFraction = false;
                }

                if (expression.charAt(indiciesOfWhiteSpaces[2] + 1) == '_') {
                    replaceEndIndex = indiciesOfWhiteSpaces[i + 4];
                    isBFraction = false;
                }

                if (!isAFraction && Character.isDigit(expression.charAt(indiciesOfWhiteSpaces[i - 1] + 1))) {
                    replaceBeginIndex = indiciesOfWhiteSpaces[i - 1] + 1;
                }

                if (!isBFraction && Character.isDigit(expression.charAt(indiciesOfWhiteSpaces[i + 1] + 1))) {
                    replaceEndIndex = indiciesOfWhiteSpaces[i + 2];
                }

                a.append(expression, replaceBeginIndex, indiciesOfWhiteSpaces[i]);
                b.append(expression, indiciesOfWhiteSpaces[i + 1] + 1, replaceEndIndex);
                break;
            }
        }

        answer = this.calculate(a.toString(), b.toString(), operator);
        newExpression.append(expression, 0, replaceBeginIndex).append(answer).append(expression.substring(replaceEndIndex));
        LimitCalculator.prompt("\nNew Expression: " + newExpression);
        return newExpression.toString();
    }

    private String calculate(String a, String b, char operator) {
        if (a.contains("_/") || b.contains("_/")) {
            return this.fractionalResult(a, b, operator);
        }
        return this.integerResult(a, b, operator);
    }

    private String fractionalResult(String a, String b, char operator) {
        String newExpression;
        return "69";
    }

    private String integerResult(String a, String b, char operator) {
        int aInt = Integer.parseInt(a);
        int bInt = Integer.parseInt(b);
        return switch (operator) {
            case '+' -> String.valueOf(aInt + bInt);
            case '-' -> String.valueOf(aInt - bInt);
            case '*' -> String.valueOf(aInt * bInt);
            case '/' -> this.simplifyFraction(aInt, bInt);
            case '^' -> "";
            default -> "0";
        };
    }

    private String simplifyFraction(int a, int b) {
        StringBuilder newExpression = new StringBuilder();
        if (a == 0) {
            newExpression.append(0);
            return newExpression.toString();
        }

        if (b == 0) {
            newExpression.append(a).append(" _/ ").append("0");
            return newExpression.toString();
        }

        if (a % b == 0) {
            newExpression.append(Integer.valueOf(a / b));
            return newExpression.toString();
        }

        int newA = a, newB = b;
        int greatestCommonFactors = this.findGreatestCommonFactor(a, b);

        newA /= greatestCommonFactors;
        newB /= greatestCommonFactors;

        return newExpression.append(newA).append(" _/ ").append(newB).toString();
    }

    private int findGreatestCommonFactor(int a, int b) {
        ArrayList<Integer> factorsA = this.findFactors(a);
        ArrayList<Integer> factorsB = this.findFactors(b);
        ArrayList<Integer> commonFactors = this.findCommonFactors(factorsA, factorsB);

        //Check empty because the two integers have no common factors
        if (commonFactors.isEmpty()) {
            return 1;
        }

        return commonFactors.get(commonFactors.size() - 1);
    }

    private ArrayList<Integer> findFactors(int value) {
        ArrayList<Integer> factors = new ArrayList<>();
        for (int i = 2; i <= value; i++) {
            if (value % i == 0) {
                factors.add(i);
            }
        }
        return factors;
    }

    private ArrayList<Integer> findCommonFactors(ArrayList<Integer> factorsA, ArrayList<Integer> factorsB) {
        ArrayList<Integer> commonFactors = new ArrayList<>();
        //Loop through the list with the least items, improves efficiency
        if (factorsA.size() > factorsB.size()) {
            factorsB.forEach((value) -> {
                if (factorsA.contains(value)) {
                    commonFactors.add(value);
                }
            });
        }

        //If factorsA.size() < factorsB.size() || factorsA.size() == factorsB.size(), hence, checking if the commonFactor list is empty
        if (commonFactors.isEmpty()) {
            factorsA.forEach((value) -> {
                if (factorsB.contains(value)) {
                    commonFactors.add(value);
                }
            });
        }

        return commonFactors;
    }

    /*
    private String simplifyExponents(int a, int b) {

    }
    */

    private int[] indiciesofwhitespaces(String function) {
        int numOfWhiteSpaces = 0;
        int listIndex = 0;
        int[] indicesOfWhiteSpaces;

        for (char c : function.toCharArray()) {
            if (c == ' ') {
                numOfWhiteSpaces++;
            }
        }

        indicesOfWhiteSpaces = new int[numOfWhiteSpaces];

        for (int i = 0; i < function.length(); i++) {
            if (function.charAt(i) == ' ') {
                indicesOfWhiteSpaces[listIndex] = i;
                listIndex++;
            }
        }

        return indicesOfWhiteSpaces;
    }
}
