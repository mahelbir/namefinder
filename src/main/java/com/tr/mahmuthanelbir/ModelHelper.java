package com.tr.mahmuthanelbir;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

public class ModelHelper {
    private static final String RESOURCE_PATH = "src/main/resources/";

    public static String[] sentences(String body) {
        try (InputStream modelIn = new FileInputStream(RESOURCE_PATH + "opennlp-en-ud-ewt-sentence-1.0-1.9.3.bin")) {
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model);
            return sentenceDetector.sentDetect(body);
        } catch (IOException e) {
            System.out.println("Error: sentences()");
            return new String[0];
        }
    }

    public static String[] tokens(String[] sentences) {
        LinkedList<String> tokens = new LinkedList<>();
        try (InputStream modelIn = new FileInputStream(RESOURCE_PATH + "opennlp-en-ud-ewt-tokens-1.0-1.9.3.bin")) {
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model);
            for (String sentence : sentences)
                tokens.addAll(Arrays.asList(tokenizer.tokenize(sentence)));
        } catch (IOException e) {
            System.out.println("Error: tokens()");
        }
        return tokens.toArray(new String[0]);
    }

    public static LinkedList<String> names(String[] tokens) {
        LinkedList<String> names = new LinkedList<>();
        try (InputStream modelIn = new FileInputStream(RESOURCE_PATH + "en-ner-person.bin")) {
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model);
            names.addAll(Arrays.asList(Span.spansToStrings(nameFinder.find(tokens), tokens)));
        } catch (IOException e) {
            System.out.println("Error: names()");
        }
        return names;
    }
}
