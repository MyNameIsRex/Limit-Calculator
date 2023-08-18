package psychologytheory.limitcalculator;

public enum LimitTypes {
    INDETERMINANT(false, "The limit is indeterminant"),
    POSITIVE_INFINITY(false, "The limit is positive infinity"),
    NEGATIVE_INFINITY(false, "The limit is negative infinity"),
    DOES_NOT_EXIST(false, "The limit does not exist"),
    EXISTS(true, "The limit exists");

    private boolean hasLimit;
    private String result;

    LimitTypes(boolean hasLimit, String result) {
        this.hasLimit = hasLimit;
        this.result = result;
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
