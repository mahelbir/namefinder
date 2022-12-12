package com.tr.mahmuthanelbir;

import java.io.IOException;
import java.util.LinkedList;

import org.apache.commons.validator.routines.UrlValidator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Main {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Invalid parameters! You must provide an URL.");
            System.exit(0);
        }
        String url = args[0];
        try {
            if (!new UrlValidator().isValid(url)) {
                System.out.println("URL is not valid! Please provide a valid URL.");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("URL is not valid! Check the syntax.");
            System.exit(0);
        }
        String body = body(url);
        if (body == null) {
            System.out.println("No body found!");
            System.exit(0);
        }
        String[] sentences = ModelHelper.sentences(body); //get sentences[] array from body String
        String[] tokens = ModelHelper.tokens(sentences); //get tokens[] array from sentences[] array
        LinkedList<String> names = ModelHelper.names(tokens); //get names List from tokens[] array
        for (String name : names)
            System.out.println(name);
    }

    public static String body(String url) {
        try {
            Document doc = Jsoup.connect(url).get(); //get html document from the URL
            Element tag = doc.select("body").first(); // get first body tag
            if (tag != null) return tag.text(); // get text inside the body tag
            else return null;
        } catch (IOException e) {
            return null;
        }
    }

}