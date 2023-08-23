package psychologytheory.limitcalculator.evaluatemethods;

import psychologytheory.limitcalculator.LimitTypes;
import psychologytheory.limitcalculator.utilities.SimplifyExpressionUtil;

public class DirectSubstitutionMethod {
    private SimplifyExpressionUtil simplifyExpression;

    public LimitTypes directSubstitution(int stepNumber, char variable, int limit, String expression) {
        StringBuilder substitutedExpression = new StringBuilder();
        StringBuilder resultExpression = new StringBuilder();

        this.simplifyExpression = new SimplifyExpressionUtil();

        for (int i = 0; i < expression.length(); i++) {
            if (expression.charAt(i) == variable) {
                substitutedExpression.append(limit);
                continue;
            }
            substitutedExpression.append(expression.charAt(i));
        }

        System.out.println("\nStep " + stepNumber + ": Direct Substitution -> " + "lim " + variable + " -> " + limit + " (" + substitutedExpression + ")");
        System.out.println("\n====================================================================================================================");
        System.out.println(substitutedExpression);
        resultExpression.append(operateInOrder(substitutedExpression.toString()));

        if (resultExpression.toString().equals(" _( 0 _/ 0 ) ")) {
            return LimitTypes.INDETERMINANT;
        } else if (resultExpression.toString().contains(" _/ 0")) {
            if (resultExpression.toString().contains("_-")) {
                return LimitTypes.NEGATIVE_INFINITY;
            }
            return LimitTypes.POSITIVE_INFINITY;
        } else {
            return LimitTypes.EXISTS;
        }
    }

    private String operateInOrder(String unevaluatedExpression) {
        String savedExpression = unevaluatedExpression;
        for (int i = 1; i < 5; i++) {
            switch (i) {
                case 1 -> {
                    while (savedExpression.contains(" (")) {
                        System.out.println("====================================================================================================================");
                        savedExpression = evaluateParentheses(savedExpression);
                        System.out.println("====================================================================================================================");
                        System.out.println(" = " + savedExpression);
                    }
                }
                case 2 -> {
                    while (savedExpression.contains("^")) {
                        savedExpression = evaluateExponents(savedExpression);
                        System.out.println(" = " + savedExpression);
                    }
                }
                case 3 -> {
                    while (savedExpression.contains("*") || savedExpression.contains(" /")) {
                        savedExpression = evaluateMultiplicationAndFraction(savedExpression);
                        System.out.println(" = " + savedExpression);
                    }
                }
                case 4 -> {
                    while (savedExpression.contains("+") || savedExpression.contains(" - ")) {
                        savedExpression = evaluateAdditionAndSubtraction(savedExpression);
                        System.out.println(" = " + savedExpression);
                    }
                }
            }
        }

        //Simplify
        if (savedExpression.contains(" _/ ") && savedExpression.contains("_( ")) {
            savedExpression = simplifyExpression.simplifyFractionExpression(savedExpression);
            System.out.println(" = " + savedExpression);
        }

        return savedExpression;
    }

    private String evaluateParentheses(String expression) {
        int openingParenthesesPosition = 0;
        int endingParenthesesPosition = 0;
        StringBuilder savedExpression = new StringBuilder(expression);

        while (savedExpression.toString().contains(" (")) {
            for (int i = openingParenthesesPosition; i < savedExpression.length(); i++) {
                if (savedExpression.charAt(i) == ')') {
                    endingParenthesesPosition = i;
                    break;
                }
            }

            for (int i = endingParenthesesPosition; i > 0; i--) {
                if (savedExpression.charAt(i) == '(') {
                    openingParenthesesPosition = i;
                    break;
                }
            }

            if (savedExpression.charAt(openingParenthesesPosition - 1) == '_') {
                continue;
            }

            System.out.println(" " + savedExpression.substring(openingParenthesesPosition, endingParenthesesPosition + 1) + " ");

            return savedExpression.replace(openingParenthesesPosition - 1, endingParenthesesPosition + 2, operateInOrder(expression.substring(openingParenthesesPosition + 1, endingParenthesesPosition))).toString();
        }
        return savedExpression.toString();
    }

    private String evaluateExponents(String expression) {
        StringBuilder savedExpression = new StringBuilder();
        savedExpression.append(expression);

        int base = 0, exponent = 0, result = 0;
        int baseNumerator = 0, baseDenominator = 0, exponentNumerator = 0, exponentDenominator = 0, resultNumerator = 0, resultDenominator = 0;
        int baseStartingPosition = 0, exponentEndingPosition = 1;
        String baseExpression = "", exponentExpression = "";

        boolean baseIsFraction = false, exponentIsFraction = false;

        for (int i = 0; i < savedExpression.length(); i++) {
            if (savedExpression.charAt(i) == '^') {
                //base
                for (int j = i - 2; j >= 0; j--) {
                    if (savedExpression.charAt(j) == ')') {
                        for (int k = j; k >= 0; k--) {
                            if (savedExpression.charAt(k) == '(' && savedExpression.charAt(k - 1) == '_') {
                                baseExpression = savedExpression.substring(k - 1, j + 1);
                                break;
                            }
                        }
                        break;
                    } else if (savedExpression.charAt(j) == ' ') {
                        baseExpression = savedExpression.substring(j + 1, i - 1);
                        baseStartingPosition += j + 1;
                        break;
                    }
                }

                //exponent
                for (int j = i + 2; j < savedExpression.length(); j++) {
                    if (savedExpression.charAt(j) == '_' && savedExpression.charAt(j + 1) == '(') {
                        for (int k = j; k < savedExpression.length(); k++) {
                            if (savedExpression.charAt(k) == ')' && savedExpression.charAt(k + 1) == ' ') {
                                exponentExpression = savedExpression.substring(j, k + 1);
                                break;
                            }
                        }
                        break;
                    }
                    if (savedExpression.charAt(j) == ' ') {
                        exponentExpression = savedExpression.substring(i + 2, j);
                        exponentEndingPosition += j;
                        break;
                    }
                }

                if (baseExpression.contains("_(") && baseExpression.contains("_/")) {
                    baseIsFraction = true;
                    for (int k = 0; k < baseExpression.length(); k++) {
                        if (baseExpression.charAt(k) == '_' && baseExpression.charAt(k + 1) == '/') {
                            //baseNumerator
                            for (int l = k - 2; l >= 0; l--) {
                                if (baseExpression.charAt(l) == ' ') {
                                    baseNumerator = Integer.parseInt(baseExpression.substring(l + 1, k - 1));
                                    break;
                                }
                            }

                            //baseDenominator
                            for (int l = k + 3; l < baseExpression.length(); l++) {
                                if (baseExpression.charAt(l) == ' ') {
                                    baseDenominator = Integer.parseInt(baseExpression.substring(k + 3, l));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                if (exponentExpression.contains("_(") && exponentExpression.contains("_/")){
                    exponentIsFraction = true;
                    for (int k = 0; k < exponentExpression.length(); k++) {
                        if (exponentExpression.charAt(k) == '_' && exponentExpression.charAt(k + 1) == '/') {
                            //exponentNumerator
                            for (int l = k - 2; l >= 0; l--) {
                                if (exponentExpression.charAt(l) == ' ') {
                                    exponentNumerator = Integer.parseInt(exponentExpression.substring(l + 1, k - 1));
                                    break;
                                }
                            }

                            //exponentDenominator
                            for (int l = k + 3; l < exponentExpression.length(); l++) {
                                if (exponentExpression.charAt(l) == ' ') {
                                    exponentDenominator = Integer.parseInt(exponentExpression.substring(k + 3, l));
                                    break;
                                }
                            }
                            break;
                        }
                    }
                }

                if (baseIsFraction) {
                    if (exponentIsFraction) {
                        int newBaseNumerator = (int) Math.pow(baseNumerator, exponentNumerator);
                        int newBaseDenominator = (int) Math.pow(baseDenominator, exponentNumerator);

                        if (exponentDenominator == 2) {
                            //baseNumerator
                            for (int m = 1; m < newBaseNumerator; m++) {
                                if (m * m == newBaseNumerator) {
                                    resultNumerator = m;
                                    break;
                                } else if (m * m > newBaseNumerator) {
                                    break;
                                }
                            }

                            //baseDenominator
                            for (int n = 1; n < newBaseDenominator; n++) {
                                if (n * n == newBaseDenominator) {
                                    resultDenominator = n;
                                    break;
                                } else if (n * n > newBaseNumerator) {
                                    break;
                                }
                            }
                        }

                        if (exponentDenominator == 3) {
                            //baseNumerator
                            for (int m = 1; m < newBaseNumerator; m++) {
                                if (m * m * m == newBaseNumerator) {
                                    resultNumerator = m;
                                    break;
                                } else if (m * m * m > newBaseNumerator) {
                                    break;
                                }
                            }

                            //baseDenominator
                            for (int n = 1; n < newBaseDenominator; n++) {
                                if (n * n * n == newBaseDenominator) {
                                    resultDenominator = n;
                                    break;
                                } else if (n * n * n > newBaseDenominator) {
                                    break;
                                }
                            }
                        }

                        if (resultNumerator == 0) {
                            savedExpression.replace(baseStartingPosition, exponentEndingPosition, "_( " + newBaseNumerator + " _^ " + exponentExpression + " _/ " + resultDenominator + " )");
                        } else if (resultDenominator == 0) {
                            savedExpression.replace(baseStartingPosition, exponentEndingPosition, "_( " + resultNumerator + " _* " + newBaseDenominator + " _^ " + exponentExpression + " _/ " + newBaseDenominator + " )");
                        } else {
                            savedExpression.replace(baseStartingPosition, exponentEndingPosition, "_( " + resultNumerator + " _/ " + resultDenominator + " )");
                        }
                    } else {
                        exponent = Integer.parseInt(exponentExpression);
                        resultNumerator = (int) Math.pow(baseNumerator, exponent);
                        resultDenominator = (int) Math.pow(baseDenominator, exponent);

                        savedExpression.replace(baseStartingPosition, exponentEndingPosition, "_( " + resultNumerator + " _/ " + resultDenominator + " )");
                        return savedExpression.toString();
                    }
                } else if (exponentIsFraction) {
                    base = Integer.parseInt(baseExpression);
                    int newBase = (int) Math.pow(base, exponentNumerator);
                    if (exponentDenominator == 2) {
                        for (int m = 1; m < newBase; m++) {
                            if (m * m == newBase) {
                                savedExpression.replace(baseStartingPosition, exponentEndingPosition, String.valueOf(m));
                                break;
                            } else if (m * m > newBase) {
                                break;
                            }
                        }
                    } else if (exponentDenominator == 3) {
                        for (int m = 1; m < newBase; m++) {
                            if (m * m * m == newBase) {
                                savedExpression.replace(baseStartingPosition, exponentEndingPosition, String.valueOf(m));
                                break;
                            } else if (m * m * m > newBase) {
                                break;
                            }
                        }
                    }

                    savedExpression.replace(baseStartingPosition, exponentEndingPosition, newBase + " _^ " + "_( " + 1 + " _/ " + resultDenominator + " )");
                    return savedExpression.toString();
                } else {
                    base = Integer.parseInt(baseExpression);
                    exponent = Integer.parseInt(exponentExpression);

                    result = (int) Math.pow(base, exponent);
                    savedExpression.replace(baseStartingPosition, exponentEndingPosition, String.valueOf(result));
                    return savedExpression.toString();
                }
            }
        }
        return savedExpression.toString();
    }

    private String evaluateMultiplicationAndFraction(String expression) {
        StringBuilder savedExpression = new StringBuilder(expression);

        int multiplicandStartingPosition = 0, multiplicandEndingPosition = 0, multiplierStartingPosition = 0, multiplierEndingPosition = 0;
        boolean multiplicandIsFraction = false, multiplierIsFraction = false;
        int multiplicandNumerator = 0, multiplicandDenominator = 0, multiplierNumerator = 0, multiplierDenominator = 0, numeratorProduct = 0, denominatorProduct = 0;
        int multiplicand = 0, multiplier = 0, product = 0;

        int dividendStartingPosition = 0, dividendEndingPosition = 0, divisorStartingPosition = 0, divisorEndingPosition = 0;
        boolean dividendIsFraction = false, divisorIsFraction = false;
        int dividendNumerator = 0, dividendDenominator = 0, divisorNumerator = 0, divisorDenominator = 0, numeratorQuotient = 0, denominatorQuotient = 0;
        int dividend = 0, divisor = 0, quotient = 0;

        while (savedExpression.toString().contains("*") || savedExpression.toString().contains(" /")) {
            for (int i = 0; i < savedExpression.length(); i++) {
                if (savedExpression.charAt(i) == '*') {
                    for (int j = i + 2; j < savedExpression.length(); j++) {
                        if (savedExpression.charAt(j) == '_' && savedExpression.charAt(j + 1) == '(') {
                            multiplierIsFraction = true;

                            //multiplierNumerator
                            for (int k = j + 3; k < savedExpression.length(); k++) {
                                if (savedExpression.charAt(k) == ' ' && savedExpression.charAt(k + 1) == '_') {
                                    multiplierNumerator = Integer.parseInt(savedExpression.substring(j + 3, k));

                                    //multiplierDenominator
                                    for (int l = k + 3; l < savedExpression.length(); l++) {
                                        if (savedExpression.charAt(l) == ' ' && savedExpression.charAt(l + 1) == ')') {
                                            multiplierDenominator = Integer.parseInt(savedExpression.substring(k + 4, l));
                                            multiplierEndingPosition = l + 1;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            multiplierStartingPosition = i + 2;
                            multiplierEndingPosition = j - 1;
                            multiplier = Integer.parseInt(savedExpression.substring(multiplierStartingPosition, multiplierEndingPosition + 1));
                            break;
                        }
                    }

                    for (int j = i - 2; j >= 0; j--) {
                        if (savedExpression.charAt(j) == ')' && savedExpression.charAt(j - 1) == ' ') {
                            multiplicandIsFraction = true;

                            //multiplicandDenominator
                            for (int k = j - 2; k >= 0; k--) {
                                if (savedExpression.charAt(k) == ' ') {
                                    multiplicandDenominator = Integer.parseInt(savedExpression.substring(k + 1, j - 1));

                                    //multiplicandNumerator
                                    for (int l = k - 4; l >= 0; l--) {
                                        if (savedExpression.charAt(l) == ' ') {
                                            multiplicandNumerator = Integer.parseInt(savedExpression.substring(l + 1, k - 3));
                                            multiplicandStartingPosition = l - 2;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            multiplicandStartingPosition = j + 1;
                            multiplicandEndingPosition = i - 2;
                            multiplicand = Integer.parseInt(savedExpression.substring(multiplicandStartingPosition, multiplicandEndingPosition + 1));
                            break;
                        }
                    }

                    if (multiplicandIsFraction) {
                        //Both are fractions
                        if (multiplierIsFraction) {
                            numeratorProduct = multiplicandNumerator * multiplierNumerator;
                            denominatorProduct = multiplicandDenominator * multiplierDenominator;
                        } else {
                            numeratorProduct = multiplicandNumerator * multiplier;
                            denominatorProduct = multiplicandDenominator;
                        }
                        return savedExpression.replace(multiplicandStartingPosition, multiplierEndingPosition + 1, "_( " + numeratorProduct + " _/ " + denominatorProduct + " )").toString();
                    } else if (multiplierIsFraction) {
                        numeratorProduct = multiplicand * multiplierNumerator;
                        denominatorProduct = multiplierDenominator;
                        return savedExpression.replace(multiplicandStartingPosition, multiplierEndingPosition + 1, "_( " + numeratorProduct + " _/ " + denominatorProduct + " )").toString();
                    } else {
                        product = multiplicand * multiplier;
                        return savedExpression.replace(multiplicandStartingPosition, multiplierEndingPosition + 1, String.valueOf(product)).toString();
                    }
                }

                if (savedExpression.charAt(i) == '/' && !(savedExpression.charAt(i - 1) == '_')) {
                    for (int j = i + 2; j < savedExpression.length(); j++) {
                        if (savedExpression.charAt(j) == '_' && savedExpression.charAt(j + 1) == '(') {
                            divisorIsFraction = true;

                            //divisorNumerator
                            for (int k = j + 3; k < savedExpression.length(); k++) {
                                if (savedExpression.charAt(k) == ' ' && savedExpression.charAt(k + 1) == '_') {
                                    divisorNumerator = Integer.parseInt(savedExpression.substring(j + 3, k));

                                    //divisorDenominator
                                    for (int l = k + 3; l < savedExpression.length(); l++) {
                                        if (savedExpression.charAt(l) == ' ' && savedExpression.charAt(l + 1) == ')') {
                                            divisorDenominator = Integer.parseInt(savedExpression.substring(k + 4, l));
                                            divisorEndingPosition = l + 1;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            divisorStartingPosition = i + 2;
                            divisorEndingPosition = j - 1;
                            divisor = Integer.parseInt(savedExpression.substring(divisorStartingPosition, divisorEndingPosition + 1));
                            break;
                        }
                    }

                    for (int j = i - 2; j >= 0; j--) {
                        if (savedExpression.charAt(j) == ')' && savedExpression.charAt(j - 1) == ' ') {
                            dividendIsFraction = true;

                            //dividendDenominator
                            for (int k = j - 2; k >= 0; k--) {
                                if (savedExpression.charAt(k) == ' ') {
                                    dividendDenominator = Integer.parseInt(savedExpression.substring(k + 1, j - 1));

                                    //dividendNumerator
                                    for (int l = k - 4; l >= 0; l--) {
                                        if (savedExpression.charAt(l) == ' ') {
                                            dividendNumerator = Integer.parseInt(savedExpression.substring(l + 1, k - 3));
                                            dividendStartingPosition = l - 2;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            dividendStartingPosition = j + 1;
                            dividendEndingPosition = i - 2;
                            dividend = Integer.parseInt(savedExpression.substring(dividendStartingPosition, dividendEndingPosition + 1));
                            break;
                        }
                    }

                    if (dividendIsFraction) {
                        //Both are fractions
                        if (divisorIsFraction) {
                            numeratorQuotient = dividendNumerator * divisorDenominator;
                            denominatorQuotient = dividendDenominator * divisorNumerator;
                        } else {
                            numeratorQuotient = dividendNumerator;
                            denominatorQuotient = dividendDenominator * divisor;
                        }
                        return savedExpression.replace(dividendStartingPosition, divisorEndingPosition + 1, "_( " + numeratorQuotient + " _/ " + denominatorQuotient + " )").toString();
                    } else if (divisorIsFraction) {
                        numeratorQuotient = divisorNumerator;
                        denominatorQuotient = dividend * divisorDenominator;
                        return savedExpression.replace(dividendStartingPosition, divisorEndingPosition + 1, "_( " + numeratorQuotient + " _/ " + denominatorQuotient + " )").toString();
                    } else {
                        //Add check for divisor if necessary
                        if (divisor == 0) {
                            if (dividend == 0) {
                                return savedExpression.replace(dividendStartingPosition, divisorEndingPosition + 1, "_( " + 0 + " _/ " + 0 + " )").toString();
                            }
                            return savedExpression.replace(dividendStartingPosition, divisorEndingPosition + 1, "_( " + dividend + " _/ " + 0 + " )").toString();
                        } else if (dividend % divisor != 0) {
                            return savedExpression.replace(dividendStartingPosition, divisorEndingPosition + 1, "_( " + dividend + " _/ " + divisor + " )").toString();
                        } else {
                            quotient = dividend / divisor;
                            return savedExpression.replace(dividendStartingPosition, divisorEndingPosition + 1, String.valueOf(quotient)).toString();
                        }
                    }
                }
            }
        }
        return savedExpression.toString();
    }

    private String evaluateAdditionAndSubtraction(String expression) {
        StringBuilder savedExpression = new StringBuilder();
        savedExpression.append(expression);

        int firstAddendStartingPosition = 0, firstAddendEndingPosition = 0, secondAddendStartingPosition = 0, secondAddendEndingPosition = 0;
        boolean firstAddendIsFraction = false, secondAddendIsFraction = false;
        int firstAddendNumerator = 0, firstAddendDenominator = 0, secondAddendNumerator = 0, secondAddendDenominator = 0, numeratorSum = 0, denominatorSum = 0;
        int firstAddend = 0, secondAddend = 0, sum = 0;


        int firstSubtrahendStartingPosition = 0, firstSubtrahendEndingPosition = 0, secondSubtrahendStartingPosition = 0, secondSubtrahendEndingPosition = 0;
        boolean firstSubtrahendIsFraction = false, secondSubtrahendIsFraction = false;
        int firstSubtrahendNumerator = 0, firstSubtrahendDenominator = 0, secondSubtrahendNumerator = 0, secondSubtrahendDenominator = 0, numeratorDifference = 0, denominatorDifference = 0;
        int firstSubtrahend = 0, secondSubtrahend = 0, difference = 0;

        while (savedExpression.toString().contains("+") || savedExpression.toString().contains(" - ")) {
            for (int i = 0; i < savedExpression.length(); i++) {
                if (savedExpression.charAt(i) == '+') {
                    for (int j = i + 2; j < savedExpression.length(); j++) {
                        if (savedExpression.charAt(j) == '_' && savedExpression.charAt(j + 1) == '(') {
                            secondAddendIsFraction = true;

                            //secondAddendNumerator
                            for (int k = j + 3; k < savedExpression.length(); k++) {
                                if (savedExpression.charAt(k) == ' ' && savedExpression.charAt(k + 1) == '_') {
                                    secondAddendNumerator = Integer.parseInt(savedExpression.substring(j + 3, k));

                                    //secondAddendDenominator
                                    for (int l = k + 3; l < savedExpression.length(); l++) {
                                        if (savedExpression.charAt(l) == ' ' && savedExpression.charAt(l + 1) == ')') {
                                            secondAddendDenominator = Integer.parseInt(savedExpression.substring(k + 4, l));
                                            secondAddendEndingPosition = l + 1;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            secondAddendStartingPosition = i + 2;
                            secondAddendEndingPosition = j - 1;

                            secondAddend = Integer.parseInt(savedExpression.substring(secondAddendStartingPosition, secondAddendEndingPosition + 1));
                            break;
                        }
                    }

                    for (int j = i - 2; j >= 0; j--) {
                        if (savedExpression.charAt(j) == ')' && savedExpression.charAt(j - 1) == ' ') {
                            firstAddendIsFraction = true;

                            //firstAddendDenominator
                            for (int k = j - 2; k >= 0; k--) {
                                if (savedExpression.charAt(k) == ' ') {
                                    firstAddendDenominator = Integer.parseInt(savedExpression.substring(k + 1, j - 1));

                                    //firstAddendNumerator
                                    for (int l = k - 4; l >= 0; l--) {
                                        if (savedExpression.charAt(l) == ' ') {
                                            firstAddendNumerator = Integer.parseInt(savedExpression.substring(l + 1, k - 3));
                                            firstAddendStartingPosition = l - 2;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            firstAddendStartingPosition = j + 1;
                            firstAddendEndingPosition = i - 2;

                            firstAddend = Integer.parseInt(savedExpression.substring(firstAddendStartingPosition, firstAddendEndingPosition + 1));
                            break;
                        }
                    }

                    if (firstAddendIsFraction) {
                        //Both are fractions
                        if (secondAddendIsFraction) {
                            //If denominators are the same
                            if (firstAddendDenominator == secondAddendDenominator) {
                                numeratorSum = firstAddendNumerator + secondAddendNumerator;
                                denominatorSum = firstAddendDenominator;
                            } else {
                                //If one denominator is a multiple of the other
                                if (firstAddendDenominator % secondAddendDenominator == 0) {
                                    numeratorSum = firstAddendNumerator + secondAddendNumerator * (firstAddendDenominator / secondAddendDenominator);
                                    denominatorSum = firstAddendDenominator;
                                } else if (secondAddendDenominator % firstAddendDenominator == 0) {
                                    numeratorSum = firstAddendNumerator * (secondAddendDenominator / firstAddendDenominator) + secondAddendNumerator;
                                    denominatorSum = secondAddendDenominator;
                                } else {
                                    numeratorSum = firstAddendNumerator * secondAddendDenominator + secondAddendNumerator * firstAddendDenominator;
                                    denominatorSum = firstAddendDenominator * secondAddendDenominator;
                                }
                            }

                        } else {
                            denominatorSum = firstAddendDenominator;
                            numeratorSum = firstAddendNumerator + secondAddend * firstAddendDenominator;
                        }
                        return savedExpression.replace(firstAddendStartingPosition, secondAddendEndingPosition + 1, "_( " + numeratorSum + " _/ " + denominatorSum + " )").toString();
                    } else if (secondAddendIsFraction) {
                        denominatorSum = secondAddendDenominator;
                        numeratorSum = firstAddend * secondAddendDenominator + secondAddendNumerator;
                        return savedExpression.replace(firstAddendStartingPosition, secondAddendEndingPosition + 1, "_( " + numeratorSum + " _/ " + denominatorSum + " )").toString();
                    } else {
                        sum = firstAddend + secondAddend;
                        return savedExpression.replace(firstAddendStartingPosition, secondAddendEndingPosition + 1, String.valueOf(sum)).toString();
                    }
                }

                if (savedExpression.charAt(i) == '-' && savedExpression.charAt(i + 1) == ' ') {
                    for (int j = i + 2; j < savedExpression.length(); j++) {
                        if (savedExpression.charAt(j) == '_' && savedExpression.charAt(j + 1) == '(') {
                            secondSubtrahendIsFraction = true;

                            //secondSubtrahendNumerator
                            for (int k = j + 3; k < savedExpression.length(); k++) {
                                if (savedExpression.charAt(k) == ' ' && savedExpression.charAt(k + 1) == '_') {
                                    secondSubtrahendNumerator = Integer.parseInt(savedExpression.substring(j + 3, k));

                                    //secondSubtrahendDenominator
                                    for (int l = k + 3; l < savedExpression.length(); l++) {
                                        if (savedExpression.charAt(l) == ' ' && savedExpression.charAt(l + 1) == ')') {
                                            secondSubtrahendDenominator = Integer.parseInt(savedExpression.substring(k + 4, l));
                                            secondSubtrahendEndingPosition = l + 1;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }

                        if (savedExpression.charAt(j) == ' ') {
                            secondSubtrahendStartingPosition = i + 2;
                            secondSubtrahendEndingPosition = j - 1;

                            secondSubtrahend = Integer.parseInt(savedExpression.substring(secondSubtrahendStartingPosition, secondSubtrahendEndingPosition + 1));
                            break;
                        }
                    }

                    for (int j = i - 2; j >= 0; j--) {
                        if (savedExpression.charAt(j) == ')' && savedExpression.charAt(j - 1) == ' ') {
                            firstSubtrahendIsFraction = true;

                            //firstSubtrahendDenominator
                            for (int k = j - 2; k >= 0; k--) {
                                if (savedExpression.charAt(k) == ' ') {
                                    firstSubtrahendDenominator = Integer.parseInt(savedExpression.substring(k + 1, j - 1));

                                    //firstSubtrahendNumerator
                                    for (int l = k - 4; l >= 0; l--) {
                                        if (savedExpression.charAt(l) == ' ') {
                                            firstSubtrahendNumerator = Integer.parseInt(savedExpression.substring(l + 1, k - 3));
                                            firstSubtrahendStartingPosition = l - 2;
                                            break;
                                        }
                                    }
                                    break;
                                }
                            }
                            break;
                        }
                        if (savedExpression.charAt(j) == ' ') {
                            firstSubtrahendStartingPosition = j + 1;
                            firstSubtrahendEndingPosition = i - 2;

                            firstSubtrahend = Integer.parseInt(savedExpression.substring(firstSubtrahendStartingPosition, firstSubtrahendEndingPosition + 1));
                            break;
                        }
                    }

                    if (firstSubtrahendIsFraction) {
                        //Both are fractions
                        if (secondSubtrahendIsFraction) {
                            //If denominators are the same
                            if (firstSubtrahendDenominator == secondSubtrahendDenominator) {
                                numeratorDifference = firstSubtrahendNumerator - secondSubtrahendNumerator;
                                denominatorDifference = firstSubtrahendDenominator;
                            } else {
                                //If one denominator is a multiple of the other
                                if (firstSubtrahendDenominator % secondSubtrahendDenominator == 0) {
                                    numeratorDifference = firstSubtrahendNumerator - secondSubtrahendNumerator * (firstSubtrahendDenominator / secondSubtrahendDenominator);
                                    denominatorDifference = firstSubtrahendDenominator;
                                } else if (secondSubtrahendDenominator % firstSubtrahendDenominator == 0) {
                                    numeratorDifference = firstSubtrahendNumerator * (secondSubtrahendDenominator / firstSubtrahendDenominator) - secondSubtrahendNumerator;
                                    denominatorDifference = secondSubtrahendDenominator;
                                } else {
                                    numeratorDifference = firstSubtrahendNumerator * secondSubtrahendDenominator - secondSubtrahendNumerator * firstSubtrahendDenominator;
                                    denominatorDifference = firstSubtrahendDenominator * secondSubtrahendDenominator;
                                }
                            }

                        } else {
                            denominatorDifference = firstSubtrahendDenominator;
                            numeratorDifference = firstSubtrahendNumerator - secondSubtrahend * firstSubtrahendDenominator;
                        }
                        return savedExpression.replace(firstSubtrahendStartingPosition, secondSubtrahendEndingPosition + 1, "_( " + numeratorDifference + " _/ " + denominatorDifference + " )").toString();
                    } else if (secondSubtrahendIsFraction) {
                        denominatorDifference = secondSubtrahendDenominator;
                        numeratorDifference = firstSubtrahend * secondSubtrahendDenominator + secondSubtrahendNumerator;
                        return savedExpression.replace(firstSubtrahendStartingPosition, secondSubtrahendEndingPosition + 1, "_( " + numeratorDifference + " _/ " + denominatorDifference + " )").toString();
                    } else {
                        difference = firstSubtrahend - secondSubtrahend;
                        return savedExpression.replace(firstSubtrahendStartingPosition, secondSubtrahendEndingPosition + 1, String.valueOf(difference)).toString();
                    }
                }
            }
        }
        return savedExpression.toString();
    }
}
