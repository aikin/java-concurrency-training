package com.concurrency.wordcount;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Pages implements Iterable<Page> {
    private final String fileName;
    private final int maxPages;

    public Pages(int maxPages, String fileName) {
        this.maxPages = maxPages;
        this.fileName = fileName;
    }


    @Override
    public Iterator<Page> iterator() {
        try {
            return new PageIterator();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private class PageIterator implements Iterator<Page> {

        private XMLEventReader reader;
        private int remainingPages;

        PageIterator() throws Exception {
            remainingPages = maxPages;
            reader = XMLInputFactory.newInstance().createXMLEventReader(Pages.class.getClassLoader().getResourceAsStream(fileName));
        }

        public boolean hasNext() {
            return remainingPages > 0;
        }

        public Page next() {
            try {
                XMLEvent event;
                String title = "";
                String text = "";
                while (true) {
                    event = reader.nextEvent();
                    if (event.isStartElement()) {
                        if (event.asStartElement().getName().getLocalPart().equals("page")) {
                            while (true) {
                                event = reader.nextEvent();
                                if (event.isStartElement()) {
                                    String name = event.asStartElement().getName().getLocalPart();
                                    if (name.equals("title"))
                                        title = reader.getElementText();
                                    else if (name.equals("text"))
                                        text = reader.getElementText();
                                } else if (event.isEndElement()) {
                                    if (event.asEndElement().getName().getLocalPart().equals("page")) {
                                        --remainingPages;
                                        return new WikiPage(title, text);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                throw new NoSuchElementException();
            }
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}
