package psychologytheory.limitcalculator;

import java.util.ArrayList;
import java.util.List;

public class DirectSubstitution {
    private int[] indicesOfWhiteSpaces;
    private char variable;
    private String limit;
    private String expression;

    public DirectSubstitution(String function) {
        this.interpretExpression(function);
        this.expression = this.substitute(this.variable, this.limit, this.expression);
        while (this.expression.contains("+") || this.expression.contains("- ") || this.expression.contains("*") || this.expression.contains(" / ")) {
            this.indicesOfWhiteSpaces = this.indiciesOfWhiteSpaces(this.expression);
            this.expression = this.evaluateExpression(this.expression, this.indicesOfWhiteSpaces);
        }
    }

    private void interpretExpression(String function) {
        this.indicesOfWhiteSpaces = this.indiciesOfWhiteSpaces(function);

        this.variable = function.charAt(this.indicesOfWhiteSpaces[0] + 1);
        this.limit = function.substring(this.indicesOfWhiteSpaces[2] + 1, this.indicesOfWhiteSpaces[3]);
        this.expression = function.substring(this.indicesOfWhiteSpaces[3] + 1);

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

    private String evaluateExpression(String expression, int[] indicesOfWhiteSpaces) {
        StringBuilder newExpression = new StringBuilder();
        String answer;
        boolean isFirstIntModified = false;
        int a = 0, b = 0;
        int replaceBeginIndex = 0, replaceEndIndex = 0;
        char operator = ' ';

        for (int i = 0; i < indicesOfWhiteSpaces.length; i++) {
            //Current Index obtained from indicesOfWhiteSpace[i]
            //Check if we are at the last index of the array, this check is to prevent ArrayIndexOutOfBounds
            if (indicesOfWhiteSpaces[i] == indicesOfWhiteSpaces[indicesOfWhiteSpaces.length - 1]) {
                break;
            }

            //Check operators
            if (expression.charAt(indicesOfWhiteSpaces[i] + 1) == '(' || expression.charAt(indicesOfWhiteSpaces[i] + 1) == ')' ||
                expression.charAt(indicesOfWhiteSpaces[i] + 1) == '+' || expression.charAt(indicesOfWhiteSpaces[i] + 1) == '-' && expression.charAt(indicesOfWhiteSpaces[i] + 2) == ' ' ||
                expression.charAt(indicesOfWhiteSpaces[i] + 1) == '*' || expression.charAt(indicesOfWhiteSpaces[i] + 1) == '/' ||
                expression.charAt(indicesOfWhiteSpaces[i] + 1) == '^') {
                operator = expression.charAt(indicesOfWhiteSpaces[i] + 1);
                continue;
            }

            //All false, therefore, the next term must be an integer
            if (isFirstIntModified) {
                //Breaking out of the loop because both a and b have been obtained
                replaceEndIndex = indicesOfWhiteSpaces[i + 1];
                b = Integer.parseInt(expression.substring(indicesOfWhiteSpaces[i] + 1, replaceEndIndex));
                break;
            }

            replaceBeginIndex = indicesOfWhiteSpaces[i] + 1;
            a = Integer.parseInt(expression.substring(replaceBeginIndex, indicesOfWhiteSpaces[i + 1]));
            isFirstIntModified = true;
        }

        answer = this.calculate(a, b, operator).toString();
        newExpression.append(expression, 0, replaceBeginIndex).append(answer).append(expression.substring(replaceEndIndex));
        LimitCalculator.prompt("\nNew Expression: " + newExpression);
        return newExpression.toString();
    }

    private StringBuilder calculate(int a, int b, char operator) {
        StringBuilder newExpression = new StringBuilder();
        switch (operator) {
            case '+':
                newExpression.append(Integer.valueOf(a + b));
                break;
            case '-':
                newExpression.append(Integer.valueOf(a - b));
                break;
            case '*':
                newExpression.append(Integer.valueOf(a * b));
                break;
            case '/':
                newExpression.append(this.simplifyFraction(a, b));
                break;
            //case '^':
                //newExpression.append(this.simplifyExponents(a, b));
                //break;
            default:
                newExpression.append(0);
                break;
        }

        return newExpression;
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

    private int[] indiciesOfWhiteSpaces(String function) {
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
