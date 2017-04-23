package com.concurrency.fpwordcounter;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class WordCounter {

    public static void main(String[] args) throws XMLStreamException {

        InputStream resourceAsStream = WordCounter.class.getClassLoader().getResourceAsStream("enwiki.xml");

        XMLStreamReader xmlStreamReader = XMLInputFactory.newInstance().createXMLStreamReader(resourceAsStream);

        Page page = null;
        String tagContent = null;
        List<Page> pages = new ArrayList<>();

        while (xmlStreamReader.hasNext()) {
            int event = xmlStreamReader.next();
            
            switch (event) {
                case XMLStreamConstants.START_ELEMENT:
                    if ("page".equals(xmlStreamReader.getLocalName())) {
                        page = new Page();
                    }
                    break;

                case XMLStreamConstants.CHARACTERS:
                    tagContent = xmlStreamReader.getText().trim();
                    break;

                case XMLStreamConstants.END_ELEMENT:
                    switch (xmlStreamReader.getLocalName()) {
                        case "page":
                            pages.add(page);
                            break;
                        case "title":
                            page.setTitle(tagContent);
                            break;
                        case "text":
                            page.setText(tagContent);
                            break;
                    }
                    break;
            }
        }

        System.out.printf("Pages size: " + pages.size());

    }
}
