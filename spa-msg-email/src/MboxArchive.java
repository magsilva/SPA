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

package lode.miner.extraction.email;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CoderResult;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * MBox archive. An mbox file is a sequence of email messages separated by
 * From_ lines. This class provides an iterator() method that allows you
 * to grab messages stored within a mbox.
 * 
 * Description ot the file format:
 * - http://tools.ietf.org/html/rfc4155
 * - http://qmail.org/man/man5/mbox.html
 */
public class MboxArchive implements Iterable<CharBuffer>, Closeable
{
    private FileInputStream theFile;

    private CharBuffer mboxCharBuffer;
    
    private Matcher fromLineMathcer;
    
    private boolean fromLineFound;
    
    private MappedByteBuffer byteBuffer;
    
    private CharsetDecoder charsetDecoder;
    
    /**
     * Flag to signal end of input to {@link java.nio.charset.CharsetDecoder#decode(java.nio.ByteBuffer)} .
     */
    private boolean endOfInputFlag = false;
    
    private int maxMessageSize;
    
    private Pattern MESSAGE_START;
    
    private int findStart = -1;
    
    private int findEnd = -1;
    
    /**
     * Match a line like: From ieugen@apache.org Fri Sep 09 14:04:52 2011.
     * 
     * Collection of From_ line patterns. Messages inside an mbox are separated by these lines.
     * The pattern is usually constant in a file but depends on the mail agents that wrote it.
     * It's possible that more mailer agents wrote in the same file using different From_ lines.
     */
    private static final Pattern DEFAULT_FROM_LINE_PATTERN = Pattern.compile("^From \\S+@\\S.*\\d{4}$", Pattern.MULTILINE);
    
    /**
     * Default charset to be used when parsing a message.
     */
    private static final Charset DEFAULT_CHARSET = Charset.forName("UTF-8");
    
    /**
     * Default buffer size used when parsing messages: 10 MiB chars.
     */
    private static final int DEFAULT_MESSAGE_BUFFER_SIZE = 10 * 1024 * 1024;
    
    /**
     * Default max message size in chars: 64 MiB. If the mbox file
     * contains larger messages they will not be decoded correctly.
     */
    private static final int MAX_MESSAGE_BUFFER_SIZE = 64 * 1024 * 1024;

    
    /**
     * Open a mbox using default configuration.
     * 
     * @param mbox File that stores the messages in mbox format.
     * @throws FileNotFoundException 
     */
    public MboxArchive(File mbox)  
    {
    	FileInputStream fis = null;
    	try {
    		fis = new FileInputStream(mbox);
    	} catch (FileNotFoundException fnfe) {
    		throw new IllegalArgumentException(fnfe);
    	}
    	
    	init(fis, DEFAULT_CHARSET, DEFAULT_FROM_LINE_PATTERN, (int) Math.min(MAX_MESSAGE_BUFFER_SIZE, mbox.length()));
    }
       
    /**
     * Open a mbox using default configuration.
     * 
     * @param mbox File that stores the messages in mbox format.
     * @throws FileNotFoundException 
     */
    public MboxArchive(InputStream mboxStream) 
    {
    	init(mboxStream, DEFAULT_CHARSET, DEFAULT_FROM_LINE_PATTERN, DEFAULT_MESSAGE_BUFFER_SIZE);
    }
    
    
    /**
     * Open a mbox using default configuration.
     * 
     * @param mbox File that stores the messages in mbox format.
     * @param charset Charset to be used to parse messages
     * @param regexpPattern Regular expression that identifies the beginning of a new message.
     * @param maxMessageSize Maximum size of a message.
     */
    public MboxArchive(InputStream mbox, Charset charset, Pattern regexpPattern, int maxMessageSize)
    {
    	init(mbox, charset, regexpPattern, maxMessageSize);
    }
    
    private void init(InputStream mbox, Charset charset, Pattern regexpPattern, int maxMessageSize)
    {
        this.maxMessageSize = maxMessageSize;
        this.MESSAGE_START = regexpPattern;
        this.charsetDecoder = charset.newDecoder();
        this.mboxCharBuffer = CharBuffer.allocate(maxMessageSize);
        
        try {
        	this.byteBuffer = theFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, theFile.getChannel().size());
        } catch (IOException ioe) {
        	throw new RuntimeException("Could not map the mbox file into memory", ioe);
        }
     
        decodeNextCharBuffer();
        fromLineMathcer = MESSAGE_START.matcher(mboxCharBuffer);
        fromLineFound = fromLineMathcer.find();
        if (fromLineFound) {
            saveFindPositions(fromLineMathcer);
        } else if (fromLineMathcer.hitEnd()) {
            throw new IllegalArgumentException("File does not contain From_ lines! Maybe not be a vaild Mbox.");
        }
    }

    private void decodeNextCharBuffer()
    {
        CoderResult coderResult = charsetDecoder.decode(byteBuffer, mboxCharBuffer, endOfInputFlag);
        updateEndOfInputFlag();
        mboxCharBuffer.flip();
        if (coderResult.isError()) {
            if (coderResult.isMalformed()) {
                throw new RuntimeException(new CharConversionException("Malformed input!"));
            } else if (coderResult.isUnmappable()) {
                throw new RuntimeException(new CharConversionException("Unmappable character!"));
            }
        }
    }

    private void updateEndOfInputFlag() {
        if (byteBuffer.remaining() <= maxMessageSize) {
            endOfInputFlag = true;
        }
    }

    private void saveFindPositions(Matcher lineMatcher) {
        findStart = lineMatcher.start();
        findEnd = lineMatcher.end();
    }

    public Iterator<CharBuffer> iterator() {
        return new MessageIterator();
    }

    public void close() throws IOException {
        theFile.close();
    }

    private class MessageIterator implements Iterator<CharBuffer>
    {
        public boolean hasNext()
        {
            if (!fromLineFound) {
                try {
                    close();
                } catch (IOException e) {
                    throw new RuntimeException("Exception closing file!");
                }
            }
            return fromLineFound;
        }

        /**
         * Returns a CharBuffer instance that contains a message between position and limit.
         * The array that backs this instance is the whole block of decoded messages.
         *
         * @return CharBuffer instance
         */
        public CharBuffer next() {
            final CharBuffer message;
            fromLineFound = fromLineMathcer.find();
            if (fromLineFound) {
                message = mboxCharBuffer.slice();
                message.position(findEnd + 1);
                saveFindPositions(fromLineMathcer);
                message.limit(fromLineMathcer.start());
            } else {
                /* We didn't find other From_ lines this means either:
                 *  - we reached end of mbox and no more messages
                 *  - we reached end of CharBuffer and need to decode another batch.
                 */
                if (byteBuffer.hasRemaining()) {
                    // decode another batch, but remember to copy the remaining chars first
                    CharBuffer oldData = mboxCharBuffer.duplicate();
                    mboxCharBuffer.clear();
                    oldData.position(findStart);
                    while (oldData.hasRemaining()) {
                        mboxCharBuffer.put(oldData.get());
                    }
                    decodeNextCharBuffer();
                    fromLineMathcer = MESSAGE_START.matcher(mboxCharBuffer);
                    fromLineFound = fromLineMathcer.find();
                    if (fromLineFound) {
                        saveFindPositions(fromLineMathcer);
                    }
                    message = mboxCharBuffer.slice();
                    message.position(fromLineMathcer.end() + 1);
                    fromLineFound = fromLineMathcer.find();
                    if (fromLineFound) {
                        saveFindPositions(fromLineMathcer);
                        message.limit(fromLineMathcer.start());
                    }
                } else {
                    message = mboxCharBuffer.slice();
                    message.position(findEnd + 1);
                    message.limit(message.capacity());
                }
            }
            return message;
        }

        public void remove() {
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}