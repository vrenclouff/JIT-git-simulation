package de.oth.ajp.jit;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.options.*;
import de.oth.ajp.jit.util.Logger;

import static de.oth.ajp.jit.util.CollectionsUtils.get;
import static de.oth.ajp.jit.util.Logger.print;

/**
 * Class parse input parameters to the option.
 *
 * @author Lukas Cerny
 * @since 1.8
 * @version 1.0
 */
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

    /**
     * Parse input parameters to the option
     * @param args input parameters
     * @return option
     */
    public static Option parseArgs(String... args) {
        String optionString = get(args, 0);
        try {
            return Type.valueOf(optionString.toUpperCase()).getOption(get(args, 1));
        } catch (java.lang.IllegalArgumentException e) {
            print("jit: '%s' is not a jit command.", optionString);
            System.exit(1);
        }
        return null;
    }
}
