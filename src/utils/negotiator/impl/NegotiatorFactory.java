package utils.negotiator.impl;

import contextaware.GlobalVars;
import utils.negotiator.Negotiator;

/**
 * Class which implements Factory Method design pattern.
 * Used to create diferent Negotiator instances.
 * The creator methods are synchronized because only one instance of each utils.negotiator is
 * allowed. There is no need to be more negotiators of the same type because they are generic
 * utility classes.
 **/
public final class NegotiatorFactory {
    private static FuzzyLogicNegotiator fuzzyLogicNegotiator;
    private static NashNegotiator nashNegotiator;
    private NegotiatorFactory() {                           
    }

    public static synchronized Negotiator getNashNegotiator() {
        NashNegotiator negotiator = nashNegotiator;
        if (negotiator == null) {
            nashNegotiator = new NashNegotiator();
            negotiator = nashNegotiator;
        }
        return negotiator;
    }
    public static synchronized Negotiator getFuzzyLogicNegotiator() {
        FuzzyLogicNegotiator negotiator = fuzzyLogicNegotiator;
        if (negotiator == null) {
         fuzzyLogicNegotiator = new FuzzyLogicNegotiator(GlobalVars.FUZZY_LOGIC_CONTROL_FILE);
            negotiator = fuzzyLogicNegotiator;
        }

         return negotiator;
    }
}
