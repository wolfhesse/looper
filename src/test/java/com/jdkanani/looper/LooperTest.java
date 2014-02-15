package com.jdkanani.looper;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author jdkanani
 */
public class LooperTest extends TestCase {
    @Test
    public void test() {
        new Thread(Looper::start).start();
        assertTrue("Server started!", true);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Looper.stop();
        assertTrue("Server stopped!", true);
    }
}
