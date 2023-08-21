package psychologytheory.limitcalculator;

public enum LimitTypes {
    INDETERMINANT(false, "is indeterminant", true),
    POSITIVE_INFINITY(true, "is positive infinity", false),
    NEGATIVE_INFINITY(true, "is negative infinity", false),
    DOES_NOT_EXIST(false, "does not exist", false),
    EXISTS(true, "exists", false);

    private boolean hasLimit;

    private boolean continueSolving;
    private String result;

    LimitTypes(boolean hasLimit, String result, boolean continueSolving) {
        this.hasLimit = hasLimit;
        this.result = result;
        this.continueSolving = continueSolving;
    }

    public boolean hasLimit() {
        return hasLimit;
    }

    public void setHasLimit(boolean hasLimit) {
        this.hasLimit = hasLimit;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
