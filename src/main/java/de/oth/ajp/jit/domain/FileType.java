package de.oth.ajp.jit.domain;

/**
 * Type of file which can be in application.
 */
public enum  FileType {

    FILE("File"),

    DIRECTORY("Directory"),

    COMMIT("Commit\t%s"),

    ;
    private final String title;

    /**
     * Constructor sets title name which will be displayed while hash process.
     * @param title
     */
    FileType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
