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
            System.out.println("You must provide an URL!");
            System.exit(0);
        }
        String url = args[0];
        try {
            if (!new UrlValidator().isValid(url)) {
                System.out.println("URL is not valid!");
                System.exit(0);
            }
        } catch (Exception e) {
            System.out.println("URL is not valid!");
            System.exit(0);
        }
        String body = body(url);
        if (body == null) {
            System.out.println("No body content!");
            System.exit(0);
        }
        String[] sentences = ModelHelper.sentences(body);
        String[] tokens = ModelHelper.tokens(sentences);
        LinkedList<String> names = ModelHelper.names(tokens);
        for (String name : names)
            System.out.println(name);
    }

    public static String body(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element tag = doc.select("body").first();
            if (tag != null) return tag.text();
        } catch (IOException e) {
            return null;
        }
        return null;
    }

}