package edu.mit.adml.spec;

public enum SearchOperation {

    EQUALITY, NEGATION, GREATER_THAN, LESS_THAN, LIKE, STARTS_WITH, ENDS_WITH, CONTAINS, CONTAINS_ENDING,
    CONTAINS_WITH_SPACE;

    // for an optional rest api
    public static final String[] SIMPLE_OPERATION_SET = {":", "!", ">", "<", "~"};

    // for an optional rest api
    public static SearchOperation getSimpleOperation(final char input) {
        switch (input) {
            case ':':
                return EQUALITY;
            case '!':
                return NEGATION;
            case '>':
                return GREATER_THAN;
            case '<':
                return LESS_THAN;
            case '~':
                return LIKE;
            default:
                return null;
        }
    }
}
