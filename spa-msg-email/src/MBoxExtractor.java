package lode.miner.extraction.email;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;

import org.apache.james.mime4j.dom.Entity;
import org.apache.james.mime4j.dom.Message;
import org.apache.james.mime4j.dom.MessageBuilder;
import org.apache.james.mime4j.dom.Multipart;
import org.apache.james.mime4j.dom.TextBody;
import org.apache.james.mime4j.message.BodyPart;
import org.apache.james.mime4j.message.DefaultMessageBuilder;
import org.apache.james.mime4j.parser.ContentHandler;
import org.apache.james.mime4j.parser.MimeStreamParser;

import org.apache.james.mime4j.stream.MimeConfig;
import org.apache.lucene.queryparser.flexible.messages.MessageImpl;

import lode.miner.PipelineUtil;
import lode.miner.TypicalPipelineComponent;
import lode.model.Element;
import lode.model.internet.EmailResource;
import lode.model.internet.MailBoxResource;

public class MBoxExtractor extends TypicalPipelineComponent
{
	private final static CharsetEncoder ENCODER = Charset.forName("UTF-8").newEncoder();
	
	@Override
	public Class<? extends Element>[] getInputResourceTypes() {
		return PipelineUtil.getResourceTypes(MailBoxResource.class);
	}

	@Override
	public Class<? extends Element>[] getOutputResourceTypes() {
		return PipelineUtil.getResourceTypes(EmailResource.class);
	}

	private static String fileToString(File file) throws IOException {
		 BufferedReader reader = new BufferedReader(new FileReader(file));
		 StringBuilder sb = new StringBuilder();
		 int ch;
		 while ((ch = reader.read()) != -1) {
			 sb.append((char) ch);
		 }
		 reader.close();
		 return sb.toString();
	}
	
    /**
    *
    * @param part
    * @return
    * @throws IOException
    */
   private String getTxtPart(Entity part) throws IOException {
       //Get content from body
       TextBody tb = (TextBody) part.getBody();
       ByteArrayOutputStream baos = new ByteArrayOutputStream();
       tb.writeTo(baos);
       return new String(baos.toByteArray());
   }
	
	/**
     * This method classifies bodyPart as text, html or attached file
     *
     * @param multipart
     * @throws IOException
     */
    private void parseBodyParts(Multipart multipart) throws IOException {
    	/*
        for (BodyPart part : multipart.getBodyParts()) {
            if (part.isMimeType("text/plain")) {
                String txt = getTxtPart(part);
                txtBody.append(txt);
            } else if (part.isMimeType("text/html")) {
                String html = getTxtPart(part);
                htmlBody.append(html);
            } else if (part.getDispositionType() != null && !part.getDispositionType().equals("")) {
                //If DispositionType is null or empty, it means that it's multipart, not attached file
                attachments.add(part);
            }

            //If current part contains other, parse it again by recursion
            if (part.isMultipart()) {
                parseBodyParts((Multipart) part.getBody());
            }
        }
        */
    }
	
	@Override
	protected void process(Element resource) {
		MailBoxResource mbox = (MailBoxResource) resource;
		MessageBuilder builder = new DefaultMessageBuilder();
		MboxArchive iterator = new MboxArchive(mbox.getFile());
        /*
		for (CharBuffer messageBuffer : iterator.iterator()) {
	        Message message = messageBuffer. builder.parseMessage(messageBuffer.asInputStream(ENCODER.charset()));
	        
	      //Get some standard headers
	       * txtBody = new StringBuffer();
        htmlBody = new StringBuffer();
        attachments<BodyPart> = new ArrayList();
            System.out.println("To: " + mimeMsg.getTo().toString());
            System.out.println("From: " + mimeMsg.getFrom().toString());
            System.out.println("Subject: " + mimeMsg.getSubject());

            //Get custom header by name
            Field priorityFld = mimeMsg.getHeader().getField("X-Priority");
            //If header doesn't found it returns null
            if (priorityFld != null) {
                //Print header value
                System.out.println("Priority: " + priorityFld.getBody());
            }

            //If message contains many parts - parse all parts
            if (mimeMsg.isMultipart()) {
                Multipart multipart = (Multipart) mimeMsg.getBody();
                parseBodyParts(multipart);
            } else {
                //If it's single part message, just get text body
                String text = getTxtPart(mimeMsg);
                txtBody.append(text);
            }

            //Print text and HTML bodies
            System.out.println("Text body: " + txtBody.toString());
            System.out.println("Html body: " + htmlBody.toString());

            for (BodyPart attach : attachments) {
                String attName = attach.getFilename();
                //Create file with specified name
                FileOutputStream fos = new FileOutputStream(attName);
                try {
                    //Get attach stream, write it to file
                    BinaryBody bb = (BinaryBody) attach.getBody();
                    bb.writeTo(fos);
                } finally {
                    fos.close();
                }
            }
        }
        */
	}
}
