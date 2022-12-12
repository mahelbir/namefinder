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
    private static final String RESOURCE_PATH = "src/main/resources/"; //trained models directory

    public static String[] sentences(String body) {
        try (InputStream modelIn = new FileInputStream(RESOURCE_PATH + "en-sent.bin")) { //read the model file
            SentenceModel model = new SentenceModel(modelIn);
            SentenceDetectorME sentenceDetector = new SentenceDetectorME(model); //create a sentence detector from model
            return sentenceDetector.sentDetect(body); //detect sentences in the body String
        } catch (IOException e) {
            System.out.println("Error: ModelHelper.sentences()");
            return new String[0];
        }
    }

    public static String[] tokens(String[] sentences) {
        LinkedList<String> tokensAll = new LinkedList<>(); //create a List to store all tokens, we don't know how many tokens we will have
        try (InputStream modelIn = new FileInputStream(RESOURCE_PATH + "en-token.bin")) { //read the model file
            TokenizerModel model = new TokenizerModel(modelIn);
            Tokenizer tokenizer = new TokenizerME(model); //create a tokenizer from model
            for (String sentence : sentences)
                tokensAll.addAll(Arrays.asList(tokenizer.tokenize(sentence))); //get single tokens[] array from single sentence String and cast it to List then add it to tokensAll List
        } catch (IOException e) {
            System.out.println("Error: ModelHelper.tokens()");
        }
        return tokensAll.toArray(new String[0]); //cast List to array because nameFinder.find() method needs an array
    }

    public static LinkedList<String> names(String[] tokens) {
        LinkedList<String> names = new LinkedList<>(); //create a List to store names, we don't know how many names we will have
        try (InputStream modelIn = new FileInputStream(RESOURCE_PATH + "en-ner-person.bin")) { //read the model file
            TokenNameFinderModel model = new TokenNameFinderModel(modelIn);
            NameFinderME nameFinder = new NameFinderME(model); //create a name finder from model
            names.addAll(Arrays.asList(Span.spansToStrings(nameFinder.find(tokens), tokens))); //get spans[] array from tokens[] array and convert it to String[] then cast it to List, add it to names List
        } catch (IOException e) {
            System.out.println("Error: ModelHelper.names()");
        }
        return names;
    }
}
