package com.hcz.core.utils;

import junit.framework.TestCase;

/**
 * Created by chz on 2016/11/17.
 */
public class StringUtilsTest extends TestCase {
    public void setUp() throws Exception {
        super.setUp();

    }

    public void testParseFileSize() throws Exception {
        System.out.println(StringUtils.parseFileSize(148814534, 0));
        System.out.println(StringUtils.parseFileSize(148814534, 1));
        System.out.println(StringUtils.parseFileSize(148814534, 2));
        System.out.println(StringUtils.parseFileSize(148814534, 3));
    }

    public void testParsePrice() throws Exception {
        System.out.println(StringUtils.parsePrice(148814534.14324, "e",0));
        System.out.println(StringUtils.parsePrice(148814534.14324, "e",1));
        System.out.println(StringUtils.parsePrice(148814534.14324, "e",2));
        System.out.println(StringUtils.parsePrice(148814534.14324, "e",3));
    }

}