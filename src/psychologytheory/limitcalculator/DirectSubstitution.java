package psychologytheory.limitcalculator;

public class DirectSubstitution {
    private int[] indicesOfWhiteSpaces;
    private char variable;
    private String limit;
    private String expression;

    public DirectSubstitution(String function) {
        this.interpretExpression(function);
        this.expression = this.substitute(this.variable, this.limit, this.expression);
        this.indicesOfWhiteSpaces = this.indiciesOfWhiteSpaces(this.expression);
        this.evaluateExpression(this.expression, this.indicesOfWhiteSpaces);
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

        LimitCalculator.prompt("\nVariable: " + this.variable + " | Limit: " + this.limit + " | New Expression: " + newExpression);
        return newExpression.toString();
    }

    private void evaluateExpression(String expression, int[] indicesOfWhiteSpaces) {
        boolean isFirstIntModified = false;
        int a = 0, b = 0;
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
                expression.charAt(indicesOfWhiteSpaces[i] + 1) == '*' || expression.charAt(indicesOfWhiteSpaces[i] + 1) == '/') {
                operator = expression.charAt(indicesOfWhiteSpaces[i] + 1);
                continue;
            }

            //All false, therefore, the next term must be an integer
            if (isFirstIntModified) {
                //Breaking out of the loop because both a and b have been obtained
                b = Integer.parseInt(expression.substring(indicesOfWhiteSpaces[i] + 1, indicesOfWhiteSpaces[i + 1]));
                break;
            }

            a = Integer.parseInt(expression.substring(indicesOfWhiteSpaces[i] + 1, indicesOfWhiteSpaces[i + 1]));
            isFirstIntModified = true;
        }

        LimitCalculator.prompt("\nA: " + a + " | B: " + b + " | Operator: " + operator);
    }

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
