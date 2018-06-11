package de.oth.ajp.jit;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.options.*;

import static de.oth.ajp.jit.util.CollectionsUtils.get;


public class Commands {

    private Commands() {}

    interface CommandType {
        Option getOption(String param);
    }

    enum Type implements CommandType {
        INIT {
            @Override
            public Option getOption(String param) {
                return new Init();
            }
        },
        ADD {
            @Override
            public Option getOption(String param) {
                return new Add(param);
            }
        },
        REMOVE {
            @Override
            public Option getOption(String param) {
                return new Remove(param);
            }
        },
        COMMIT {
            @Override
            public Option getOption(String param) {
                return new Commit(param);
            }
        },
        CHECKOUT {
            @Override
            public Option getOption(String param) {
                return new Checkout(param);
            }
        },
        LOG {
            @Override
            public Option getOption(String param) {
                return new Log();
            }
        },
        STATUS {
            @Override
            public Option getOption(String param) {
                return new Status();
            }
        },

        ;
    }

    public static Option parseArgs(String... args) {
        String optionString = get(args, 0).toUpperCase();
        Type optionType = Type.valueOf(optionString);
        if (optionType != null) {
            return optionType.getOption(get(args, 1));
        } else {
            throw new IllegalArgumentException("Neplatny argument");
        }
    }
}
