package com.chengxin.common;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class PinyinUtils {

	private static String[] BUFFERS;
	
	public PinyinUtils() {
		
	}
	
	public static void create(String filename) {
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			Document document = docBuilder.parse(filename);
			NodeList nodesStringArray = document.getDocumentElement().getElementsByTagName("item");
			int len = nodesStringArray.getLength();
			BUFFERS = new String[len];
			for(int i=0; i<len; i++) {
				Node pinyinItem = nodesStringArray.item(i);
				String val = pinyinItem.getFirstChild() != null ? pinyinItem.getFirstChild().getNodeValue() : "";
				BUFFERS[i] = val;
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
	
	public static void release() {
        BUFFERS = null;
    }
	
	public static String convert(String value) {
        if (value == null) {
            return null;
        }

        if (BUFFERS == null) {
            return value;
        }

        char[] array = value.toCharArray();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            try {
                int buff = array[i] & 0xFFFF;
                if (buff == 0x3007) {
                    builder.append(BUFFERS[0]);
                } else if ((buff >= 0x4E00) && (buff <= 0x9FA5)) {
                    builder.append(BUFFERS[buff - 0x4E00 + 1]);
                } else {
                    builder.append(array[i]);
                }
            } catch (Exception e) {
                builder.append(array[i]);
            }
        }

        return builder.toString();
   }

}
