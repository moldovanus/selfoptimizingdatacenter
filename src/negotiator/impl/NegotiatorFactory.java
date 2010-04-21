package negotiator.impl;

import negotiator.Negotiator;
import contextawaremodel.GlobalVars;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Apr 17, 2010
 * Time: 11:35:04 AM
 * To change this template use File | Settings | File Templates.
 */
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
      //      fuzzyLogicNegotiator = new FuzzyLogicNegotiator(GlobalVars.FUZZY_LOGIC_CONTROL_FILE);
            negotiator = fuzzyLogicNegotiator;
        }
        return null;
        //return null;
    }
}
