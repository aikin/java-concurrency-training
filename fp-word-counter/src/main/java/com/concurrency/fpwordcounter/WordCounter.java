package com.concurrency.fpwordcounter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WordCounter {

    private final static Logger logger = Logger.getLogger("fp.word.counter");

    public static void main(String[] args) throws XMLStreamException {

        InputStream resourceAsStream = WordCounter.class.getClassLoader().getResourceAsStream("enwiki.xml");
        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(resourceAsStream);

        Page page = null;
        String tagContent = null;
        List<Page> pages = new ArrayList<>();

        while (xmlStreamReader.hasNext()) {
            int event = xmlStreamReader.next();

            if (event == XMLStreamConstants.START_ELEMENT && "page".equals(xmlStreamReader.getLocalName())) {
                page = new Page();
            }
            if (event == XMLStreamConstants.CHARACTERS) {
                tagContent = xmlStreamReader.getText().trim();
            }
            if (event == XMLStreamConstants.END_ELEMENT) {
                if ("page".equals(xmlStreamReader.getLocalName())) {
                    pages.add(page);
                }
                if ("title".equals(xmlStreamReader.getLocalName())) {
                    page.setTitle(tagContent);
                }
                if ("text".equals(xmlStreamReader.getLocalName())) {
                    page.setText(tagContent);
                }
            }
        }

        logger.log(Level.INFO,"Pages size: {0}", pages.size());

    }
}
