/****************************************************************
 * Licensed to the Apache Software Foundation (ASF) under one   *
 * or more contributor license agreements.  See the NOTICE file *
 * distributed with this work for additional information        *
 * regarding copyright ownership.  The ASF licenses this file   *
 * to you under the Apache License, Version 2.0 (the            *
 * "License"); you may not use this file except in compliance   *
 * with the License.  You may obtain a copy of the License at   *
 *                                                              *
 *   http://www.apache.org/licenses/LICENSE-2.0                 *
 *                                                              *
 * Unless required by applicable law or agreed to in writing,   *
 * software distributed under the License is distributed on an  *
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY       *
 * KIND, either express or implied.  See the License for the    *
 * specific language governing permissions and limitations      *
 * under the License.                                           *
 ****************************************************************/

package org.apache.james.mime4j.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

/**
 * Utility methods for converting textual content of a message.
 */
public class ContentUtil {

    private ContentUtil() {
    }

    /**
     * Encodes the specified string into an immutable sequence of bytes using
     * the US-ASCII charset.
     * 
     * @param string
     *            string to encode.
     * @return encoded string as an immutable sequence of bytes.
     */
    public static ByteSequence encode(String string) {
        if (string == null) {
            return null;
        }
        ByteArrayBuffer buf = new ByteArrayBuffer(string.length());
        for (int i = 0; i < string.length(); i++) {
            buf.append((byte) string.charAt(i));
        }
        return buf;
    }

    /**
     * Encodes the specified string into an immutable sequence of bytes using
     * the specified charset.
     * 
     * @param charset
     *            Java charset to be used for the conversion.
     * @param string
     *            string to encode.
     * @return encoded string as an immutable sequence of bytes.
     */
    public static ByteSequence encode(Charset charset, String string) {
        if (string == null) {
            return null;
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        ByteBuffer encoded = charset.encode(CharBuffer.wrap(string));
        ByteArrayBuffer buf = new ByteArrayBuffer(encoded.remaining());
        buf.append(encoded.array(), encoded.position(), encoded.remaining());
        return buf;
    }

    /**
     * Decodes the specified sequence of bytes into a string using the US-ASCII
     * charset.
     * 
     * @param byteSequence
     *            sequence of bytes to decode.
     * @return decoded string.
     */
    public static String decode(ByteSequence byteSequence) {
        if (byteSequence == null) {
            return null;
        }
        return decode(byteSequence, 0, byteSequence.length());
    }

    /**
     * Decodes the specified sequence of bytes into a string using the specified
     * charset.
     * 
     * @param charset
     *            Java charset to be used for the conversion.
     * @param byteSequence
     *            sequence of bytes to decode.
     * @return decoded string.
     */
    public static String decode(Charset charset, ByteSequence byteSequence) {
        return decode(charset, byteSequence, 0, byteSequence.length());
    }

    /**
     * Decodes a sub-sequence of the specified sequence of bytes into a string
     * using the US-ASCII charset.
     * 
     * @param byteSequence
     *            sequence of bytes to decode.
     * @param offset
     *            offset into the byte sequence.
     * @param length
     *            number of bytes.
     * @return decoded string.
     */
    public static String decode(ByteSequence byteSequence, int offset,
            int length) {
        if (byteSequence == null) {
            return null;
        }
        StringBuilder buf = new StringBuilder(length);
        for (int i = offset; i < offset + length; i++) {
            buf.append((char) (byteSequence.byteAt(i) & 0xff));
        }
        return buf.toString();
    }

    /**
     * Decodes a sub-sequence of the specified sequence of bytes into a string
     * using the specified charset.
     * 
     * @param charset
     *            Java charset to be used for the conversion.
     * @param byteSequence
     *            sequence of bytes to decode.
     * @param offset
     *            offset into the byte sequence.
     * @param length
     *            number of bytes.
     * @return decoded string.
     */
    public static String decode(Charset charset, ByteSequence byteSequence,
            int offset, int length) {
        if (byteSequence == null) {
            return null;
        }
        if (charset == null) {
            charset = Charset.defaultCharset();
        }
        if (byteSequence instanceof ByteArrayBuffer) {
            ByteArrayBuffer bab = (ByteArrayBuffer) byteSequence;
            return decode(charset, bab.buffer(), offset, length);
        } else {
            byte[] bytes = byteSequence.toByteArray();
            return decode(charset, bytes, offset, length);
        }
    }

    private static String decode(Charset charset, byte[] buffer, int offset,
            int length) {
        return charset.decode(ByteBuffer.wrap(buffer, offset, length))
                .toString();
    }

}
