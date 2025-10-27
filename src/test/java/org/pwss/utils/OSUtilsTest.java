package org.pwss.utils;

import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class OSUtilsTest {

    @Test
    void testNullInput() {
        assertNull(OSUtils.formatQuarantinePath(null), "Null input should return null");
    }

    @Test
    void testSimplePathConversion() {
        String input = "usr.local.bin.myfile.txt";
        String expected;

        if (OSUtils.isWindows()) {
            expected = "usr\\local\\bin\\myfile.txt";
        } else {
            expected = "usr/local/bin/myfile.txt";
        }

        assertEquals(expected, OSUtils.formatQuarantinePath(input),
                "Path separators should be converted correctly for the current OS");
    }

    @Test
    void testDoubleDotPreservation() {
        String input = "etc..config.file.txt";
        String expected;

        if (OSUtils.isWindows()) {
            expected = "etc\\.config\\file.txt";
        } else {
            expected = "etc/.config/file.txt";
        }

        assertEquals(expected, OSUtils.formatQuarantinePath(input),
                "Double dots should be preserved correctly as literal dots in the output");
    }

    @Test
    void testExtensionPreservation() {
        String input = "var.log.myapp.log";
        String expected;

        if (OSUtils.isWindows()) {
            expected = "var\\log\\myapp.log";
        } else {
            expected = "var/log/myapp.log";
        }

        assertEquals(expected, OSUtils.formatQuarantinePath(input),
                "File extensions should be preserved with a single dot");
    }

    @Test
    void testComplexMixedPath() {
        String input = "C_drive__.Program.Files.Java.myapp.jar";
        String expected;

        if (OSUtils.isWindows()) {
            expected = "C:\\Program\\Files\\Java\\myapp.jar";
        } else {
            expected = "C_drive__/Program/Files/./Java/myapp.jar";
        }

        assertEquals(expected, OSUtils.formatQuarantinePath(input),
                "Complex paths should convert consistently across OS types");
    }
}
