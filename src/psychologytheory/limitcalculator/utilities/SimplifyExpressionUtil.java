package psychologytheory.limitcalculator.utilities;

import java.util.ArrayList;
import java.util.HashMap;

public class SimplifyExpressionUtil {

    public String simplyFractionExpression(String fractionExpression) {
        StringBuilder savedFractionExpression = new StringBuilder();
        savedFractionExpression.append(fractionExpression);

        int numerator = 0, denominator = 0;

        for (int i = 0; i < savedFractionExpression.length(); i++) {
             if (savedFractionExpression.charAt(i) == '_') {
                 //findNumerator
                 for (int j = i - 2; j >= 0; j--) {
                     if (savedFractionExpression.charAt(j) == ' ') {
                         numerator = Integer.parseInt(savedFractionExpression.substring(j, i - 1));
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

        return null;
    }

    private boolean areEvenNumbers(int a, int b) {
        return (a % 2 == 0 ) && (b % 2 == 0);
    }

    private ArrayList<Integer> calculateFactors(int a) {
        ArrayList<Integer> factors = new ArrayList<>();
        factors.add(0, 1);

        for (int i = 1; i <= a; i++) {
            if (a % i == 0) {
                factors.add(i);
            }
        }

        return factors;
    } 
}
