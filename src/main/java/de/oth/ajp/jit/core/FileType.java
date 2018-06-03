package de.oth.ajp.jit.core;


public enum  FileType {

    FILE("File"),

    DIRECTORY("Directory"),

    COMMIT("Commit\t%s"),

    ;
    private final String title;

    FileType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
