package com.unideb.qsa.config.pack.validators.schema;

import static org.apache.commons.text.StringEscapeUtils.ESCAPE_JSON;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.apache.commons.text.translate.CharSequenceTranslator;
import org.apache.commons.text.translate.UnicodeUnescaper;

/**
 * Escapes UNICODE characters into UNESCAPED UNICODE characters.
 */
public final class EscapedUnicodeToUnescapedUnicodeTranslator extends CharSequenceTranslator {

    private static final int ZERO_ESCAPES = 0;

    private final CharSequenceTranslator unicodeUnescaper = new UnicodeUnescaper();

    @Override
    public int translate(CharSequence input, int index, Writer out) throws IOException {
        Writer writerForUnescapedUnicode = new StringWriter();
        return consumeUnicode(input, index, out, writerForUnescapedUnicode);
    }

    private int consumeUnicode(final CharSequence input, final int index, final Writer out, final Writer writerForUnescapedUnicode) throws IOException {
        int unicodeConsumedCode = unicodeUnescaper.translate(input, index, writerForUnescapedUnicode);
        if (unicodeConsumedCode != ZERO_ESCAPES) {
            consumeJson(out, writerForUnescapedUnicode);
        }
        return unicodeConsumedCode;
    }

    private void consumeJson(final Writer out, final Writer writerForUnescapedUnicode) throws IOException {
        int jsonConsumedCode = ESCAPE_JSON.translate(writerForUnescapedUnicode.toString(), 0, out);
        if (jsonConsumedCode == ZERO_ESCAPES) {
            out.write(writerForUnescapedUnicode.toString());
        }
    }
}
