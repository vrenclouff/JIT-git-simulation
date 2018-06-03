package de.oth.ajp.jit;

import de.oth.ajp.jit.core.Option;
import de.oth.ajp.jit.options.*;

import static de.oth.ajp.jit.utils.CollectionsUtils.get;


public class Commands {

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

//        String option = args[0];
//        if (option.equals("init")) {
//            return new Init();
//        } else if (option.equals("add")) {
//            return new Add(args[1]);
//        } else if (option.equals("removeStaging")) {
//            return new Remove(args[1]);
//        } else if (option.equals("commit")) {
//            return new Commit(args[1]);
//        } else if (option.equals("checkout")) {
//            return new Checkout(args[1]);
//        } else if (option.equals("log")) {
//            return null; // TODO
//        } else if (option.equals("status")) {
//            return null; // TODO
//        } else {
//            throw new IllegalArgumentException("Neplatny pocet argumentu");
//        }
    }
}
