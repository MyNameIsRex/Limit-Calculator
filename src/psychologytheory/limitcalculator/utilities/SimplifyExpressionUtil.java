package psychologytheory.limitcalculator.utilities;

import java.util.ArrayList;

public class SimplifyExpressionUtil {
    public String simplifyFractionExpression(String fractionExpression) {
        StringBuilder savedFractionExpression = new StringBuilder();
        savedFractionExpression.append(fractionExpression);

        int numerator = 0, denominator = 0;

        ArrayList<Integer> numeratorFactors;
        ArrayList<Integer> denominatorFactors;
        ArrayList<Integer> commonFactors = new ArrayList<>();

        for (int i = 0; i < savedFractionExpression.length(); i++) {
             if (savedFractionExpression.charAt(i) == '_' && savedFractionExpression.charAt(i + 1) == '/') {
                 //findNumerator
                 for (int j = i - 2; j >= 0; j--) {
                     if (savedFractionExpression.charAt(j) == ' ') {
                         numerator = Integer.parseInt(savedFractionExpression.substring(j + 1, i - 1));
                         break;
                     }
                 }

                 //findDenominator
                 for (int k = i + 3; k < savedFractionExpression.length(); k++) {
                     if (savedFractionExpression.charAt(k) == ' ') {
                         denominator = Integer.parseInt(savedFractionExpression.substring(i + 3, k));
                         break;
                     }
                 }
                 break;
             }
        }

        while (areEvenNumbers(numerator, denominator)) {
            numerator /= 2;
            denominator /= 2;
        }

        numeratorFactors = calculateFactors(numerator);
        denominatorFactors = calculateFactors(denominator);

        if (numerator > denominator) {
            for (Integer denominatorFactor : denominatorFactors) {
                if (numeratorFactors.contains(denominatorFactor)) {
                    commonFactors.add(denominatorFactor);
                }
            }
        } else if (numerator < denominator) {
            for (Integer numeratorFactor : numeratorFactors) {
                if (denominatorFactors.contains(numeratorFactor)) {
                    commonFactors.add(numeratorFactor);
                }
            }
        }

        for (Integer commonFactor : commonFactors) {
            numerator /= commonFactor;
            denominator /= commonFactor;
        }

        return " _( " + numerator + " _/ " + denominator + " ) ";
    }

    private boolean areEvenNumbers(int a, int b) {
        return (a % 2 == 0 ) && (b % 2 == 0);
    }

    private ArrayList<Integer> calculateFactors(int a) {
        ArrayList<Integer> factors = new ArrayList<>();

        for (int i = 1; i <= a; i++) {
            if (a % i == 0) {
                factors.add(i);
            }
        }

        return factors;
    } 
}
