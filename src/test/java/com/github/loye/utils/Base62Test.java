package com.github.loye.utils;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.Assert;
import org.junit.Test;

public class Base62Test {

    @Test
    public void encodeAndDecode() {
        Charset charsets = StandardCharsets.UTF_8;
        String source = "Test String with special characters. ~!@#$%^&*()[中文]";
        byte[] sourceBytes = source.getBytes(charsets);
        byte[] encodedBytes = Base62.encode(sourceBytes);
        String encodedString = new String(encodedBytes, charsets);
        byte[] decodedBytes = Base62.decode(encodedBytes);
        String decodedString = new String(decodedBytes, charsets);

        Assert.assertEquals(source, decodedString);
        Assert.assertArrayEquals(sourceBytes, decodedBytes);

        System.out.println("source string \t:\t" + source.length() + "\t" + source);
        System.out.println("encoded string\t:\t" + encodedString.length() + "\t" + encodedString);
    }
}
