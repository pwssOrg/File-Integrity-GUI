package org.pwss.util;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OSUtilTest {

    @Test
    void testNullInput() {
        assertNull(OSUtil.formatQuarantinePath(null), "Null input should return null");
    }

    @Test
    void testSimplePathConversion() {
        final String input = "usr.local.bin.myfile.txt";
        final String expected;

        if (OSUtil.isWindows()) {
            expected = "usr\\local\\bin\\myfile.txt";
        } else {
            expected = "usr/local/bin/myfile.txt";
        }

        assertEquals(expected, OSUtil.formatQuarantinePath(input),
                "Path separators should be converted correctly for the current OS");
    }

    @Test
    void testDoubleDotPreservation() {
        final String input = "etc..config.file.txt";
        final String expected;

        if (OSUtil.isWindows()) {
            expected = "etc\\.config\\file.txt";
        } else {
            expected = "etc/.config/file.txt";
        }

        assertEquals(expected, OSUtil.formatQuarantinePath(input),
                "Double dots should be preserved correctly as literal dots in the output");
    }

    @Test
    void testExtensionPreservation() {
        final String input = "var.log.myapp.log";
        final String expected;

        if (OSUtil.isWindows()) {
            expected = "var\\log\\myapp.log";
        } else {
            expected = "var/log/myapp.log";
        }

        assertEquals(expected, OSUtil.formatQuarantinePath(input),
                "File extensions should be preserved with a single dot");
    }

    @Test
    void testComplexMixedPath() {
        final String input;
        final String expected;

        if (OSUtil.isWindows()) {
            input = "C_drive__.Program.Files.Java.myapp.jar";
            expected = "C:\\Program\\Files\\Java\\myapp.jar";
        } else {
            input = "etc..config..file.txt";
            expected = "etc/.config/.file.txt";
        }

        assertEquals(expected, OSUtil.formatQuarantinePath(input),
                "Complex paths should convert consistently across OS types");
    }
}
