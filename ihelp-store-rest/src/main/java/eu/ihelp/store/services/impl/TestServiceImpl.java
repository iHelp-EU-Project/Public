package eu.ihelp.store.services.impl;

import eu.ihelp.store.services.TestService;

/**
 *
 * @author Pavlos Kranas (LeanXcale)
 */
public class TestServiceImpl implements TestService {

    @Override
    public String hello() {
        return "<b>Hello world!</b>";
    }

    @Override
    public String hello(String name) {
        return "<b>Hello " + name + "!</b>";
    }
    
}
