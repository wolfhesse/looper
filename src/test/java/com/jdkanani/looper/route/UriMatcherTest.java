package com.jdkanani.looper.route;


import java.util.LinkedHashMap;
import java.util.Map;

import junit.framework.TestCase;
import org.junit.Test;

/**
 * @author jdkanani
 */
public class UriMatcherTest extends TestCase {
    @Test
    public void testConstructor() {
        Map<String, Boolean> templates = new LinkedHashMap<String, Boolean>();
        templates.put("/", true);
        templates.put("/*", true);
        templates.put("/*/", true);
        templates.put("/*/", true);
        templates.put("/users", true);
        templates.put("/users/", true);
        templates.put("/users/*", true);
        templates.put("/users/uname", true);
        templates.put("/users/UNAME", true);
        templates.put("/users/uname/", true);
        templates.put("/users/uname/", true);
        templates.put("/users/+uname", true);
        templates.put("/users/~uname", true);
        templates.put("/users/@uname", true);
        templates.put("/users/@uname/*", true);
        templates.put("/v1/+uname", true);
        templates.put("/v2/d1/uname", true);
        templates.put("/v3/i/uname", true);
        templates.put("/users/uname fname/", true);
        templates.put("/doc/jar!com.uri.test.jar", true);

        templates.put("/users/<uname>", true);
        templates.put("/<uname>", true);
        templates.put("/<UnaNE>", true);
        templates.put("/<uname>/", true);
        templates.put("/<uname>/login", true);
        templates.put("/<uname>/dashboard/users", true);
        templates.put("/<uname>/edit/<uname>", true);
        templates.put("/<uname>/edit/<looper0>", true);
        templates.put("/<aname>/edit/<bname>", true);
        templates.put("/<aname>/edit/<bname>/*", true);
        templates.put("/<aname>/edit/<bname>/*/", true);
        templates.put("/<uname>/*", true);
        templates.put("/<uname>/*/", true);

        // Non-matchers
        templates.put("//*/*", false);
        templates.put("/*/users", false);
        templates.put("/*/users/", false);
        templates.put("//*/users", false);
        templates.put("/users/*/data", false);

        templates.forEach((uri, result) -> {
            boolean r = false;
            try {
                new UriMatcher(uri);
                r = true;
            } catch(NullPointerException |  IllegalArgumentException ex) {}
            assertTrue("URI ::: `" + uri + "`" + (r ? "passes!" : "fails!"), r == result);
        });
    }

    @Test
    public void testPattern() {
        Map<String, String> pass = new LinkedHashMap<String, String>();
        pass.put("/", "^/?$");
        pass.put("/*", "^/.*/?$");
        pass.put("/*/", "^/.*/?$");
        pass.put("/users", "^/users/?$");
        pass.put("/users/", "^/users/?$");
        pass.put("/users/*", "^/users/.*/?$");
        pass.put("/users/uname", "^/users/uname/?$");
        pass.put("/users/UNAME", "^/users/UNAME/?$");
        pass.put("/users/uname/", "^/users/uname/?$");
        pass.put("/users/uname/*", "^/users/uname/.*/?$");
        pass.put("/users/uname/*/", "^/users/uname/.*/?$");
        pass.put("/users/+uname", "^/users/\\+uname/?$");
        pass.put("/users/~uname", "^/users/~uname/?$");
        pass.put("/users/@uname", "^/users/@uname/?$");
        pass.put("/users/uname fname/", "^/users/uname fname/?$");
        pass.put("/doc/jar!com.uri.test.jar", "^/doc/jar!com\\.uri\\.test\\.jar/?$");

        pass.put("/users/<uname>", "^/users/(?<uname>[^/]+)/?$");
        pass.put("/<uname>", "^/(?<uname>[^/]+)/?$");
        pass.put("/<UnaNE>", "^/(?<UnaNE>[^/]+)/?$");
        pass.put("/<uname>/", "^/(?<uname>[^/]+)/?$");
        pass.put("/<uname>/login", "^/(?<uname>[^/]+)/login/?$");
        pass.put("/<uname>/dashboard/users", "^/(?<uname>[^/]+)/dashboard/users/?$");
        pass.put("/<uname>/edit/<uname>", "^/(?<uname>[^/]+)/edit/\\k<uname>/?$");
        pass.put("/<uname>/<uname>/<uname>/<uname>", "^/(?<uname>[^/]+)/\\k<uname>/\\k<uname>/\\k<uname>/?$");
        pass.put("/<aname>/edit/<bname>", "^/(?<aname>[^/]+)/edit/(?<bname>[^/]+)/?$");

        pass.forEach((template, pattern) -> {
            UriMatcher um = new UriMatcher(template);
            assertEquals(pattern, um.getPattern().pattern());
        });
    }

    @Test
    public void testMatches() {
        // Match for simple `/`
        UriMatcher um1 = new UriMatcher("/");
        Map<String, String> result1 = um1.matches("/");
        assertTrue(result1 != null);
        assertEquals(result1.size(), 0);
        result1 = um1.matches("//");
        assertTrue(result1 == null);
        result1 = um1.matches("uname");
        assertTrue(result1 == null);
        result1 = um1.matches("/uname");
        assertTrue(result1 == null);
        result1 = um1.matches("/uname/");
        assertTrue(result1 == null);
        result1 = um1.matches("/*");
        assertTrue(result1 == null);

        UriMatcher um2 = new UriMatcher("/*");
        Map<String, String> result2 = um2.matches("/");
        assertTrue(result2 != null);
        assertEquals(result2.size(), 0);
        result2 = um2.matches("/uname");
        assertTrue(result2 != null);
        assertEquals(result2.size(), 0);
        result2 = um2.matches("/uname/");
        assertTrue(result2 != null);
        assertEquals(result2.size(), 0);
        result2 = um2.matches("/users/uname");
        assertTrue(result2 != null);
        assertEquals(result2.size(), 0);
        result2 = um2.matches("/users/~uname");
        assertTrue(result2 != null);
        assertEquals(result2.size(), 0);
        result2 = um2.matches("/doc/jar!com.uri.test.jar");
        assertTrue(result2 != null);
        assertEquals(result2.size(), 0);

        UriMatcher um3 = new UriMatcher("/users");
        Map<String, String> result3 = um3.matches("/");
        assertTrue(result3 == null);
        result3 = um3.matches("/path");
        assertTrue(result3 == null);
        result3 = um3.matches("/users/");
        assertTrue(result3 != null);
        assertEquals(result3.size(), 0);
        result3 = um3.matches("/users");
        assertTrue(result3 != null);
        assertEquals(result3.size(), 0);

        UriMatcher um4 = new UriMatcher("/users/<uname>");
        Map<String, String> result4 = um4.matches("/");
        assertTrue(result4 == null);
        result4 = um4.matches("/path");
        assertTrue(result4 == null);
        result4 = um4.matches("/users/");
        assertTrue(result4 == null);
        result4 = um4.matches("/users");
        assertTrue(result4 == null);
        result4 = um4.matches("/users/looper");
        assertTrue(result4 != null);
        assertEquals(result4.size(), 1);
        assertEquals(result4.get("uname"), "looper");
        result4 = um4.matches("/users/looper/");
        assertTrue(result4 != null);
        assertEquals(result4.size(), 1);
        assertEquals(result4.get("uname"), "looper");
        result4 = um4.matches("/users/looper/edit");
        assertTrue(result4 == null);

        UriMatcher um5 = new UriMatcher("/users/<uname>/edit/<id>");
        Map<String, String> result5 = um5.matches("/users");
        assertTrue(result5 == null);
        result5 = um5.matches("/users");
        assertTrue(result5 == null);
        result5 = um5.matches("/users/looper");
        assertTrue(result5 == null);
        result5 = um5.matches("/users/looper/edit");
        assertTrue(result5 == null);
        result5 = um5.matches("/users/looper/edit/123");
        assertTrue(result5 != null);
        assertEquals(result5.size(), 2);
        assertEquals(result5.get("uname"), "looper");
        assertEquals(result5.get("id"), "123");

        UriMatcher um6 = new UriMatcher("/users/+uname");
        Map<String, String> result6 = um6.matches("/users");
        assertTrue(result6 == null);
        result6 = um6.matches("/users/+uname");
        assertTrue(result6 != null);
        assertEquals(result6.size(), 0);
        result6 = um6.matches("/users///uname");
        assertTrue(result6 == null);

        UriMatcher um7 = new UriMatcher("/users/*");
        Map<String, String> result7 = um7.matches("/us");
        assertTrue(result7 == null);
        result7 = um7.matches("/users");
        assertTrue(result7 == null);
        result7 = um7.matches("/users/");
        assertTrue(result7 != null);
        assertEquals(result7.size(), 0);
        result7 = um7.matches("/users/uname");
        assertTrue(result7 != null);
        assertEquals(result7.size(), 0);
        result7 = um7.matches("/users/bname");
        assertTrue(result7 != null);
        assertEquals(result7.size(), 0);
        result7 = um7.matches("/users/bname/edit/123");
        assertTrue(result7 != null);
        assertEquals(result7.size(), 0);

        UriMatcher um8 = new UriMatcher("/users/<uname>/*");
        Map<String, String> result8 = um8.matches("/users");
        assertTrue(result8 == null);
        result8 = um8.matches("/users/looper");
        assertTrue(result8 == null);
        result8 = um8.matches("/users/looper/");
        assertTrue(result8 != null);
        assertEquals(result8.size(), 1);
        assertEquals(result8.get("uname"), "looper");
        result8 = um8.matches("/users/bname");
        assertTrue(result8 == null);
        result8 = um8.matches("/users/bname/edit/123");
        assertTrue(result8 != null);
        assertEquals(result8.size(), 1);
        assertEquals(result8.get("uname"), "bname");
    }
}
