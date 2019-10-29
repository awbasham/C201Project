package c201.analyze;

import c201.Utilities;
import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.lemmatizer.DictionaryLemmatizer;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.InputStream;
import java.util.ArrayList;

public class OpenNLP {
    private POSModel model;
    private WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;
    private DictionaryLemmatizer lemmatizer;

    public OpenNLP() {
        try {
            //Loading Parts of speech-maxent model
            InputStream inputStream = OpenNLP.class.getClassLoader().getResourceAsStream("en-pos-maxent.bin");
            InputStream dictionary = OpenNLP.class.getClassLoader().getResourceAsStream("en-lemmatizer.dict");
            if(inputStream == null) {
                System.out.println("Failed to load en-pos-maxent.bin file! Exiting.");
                System.exit(0);
            }
            if(dictionary == null) {
                System.out.println("Failed to load en-lemmatizer.dict file! Exiting.");
                System.exit(0);
            }
            lemmatizer = new DictionaryLemmatizer(dictionary);
            model = new POSModel(inputStream);
            dictionary.close();
            inputStream.close();
        } catch(Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
    }

    public ArrayList<String> getNouns(String sentence) {
        POSTaggerME tagger = new POSTaggerME(model);
        sentence = sentence.toLowerCase();
        try {
            String[] tokens = tokenizer.tokenize(sentence);
            String[] tags = tagger.tag(tokens);
            double[] probabilities = tagger.probs();
            ArrayList<String> nouns = new ArrayList<>();

            for(int i = 0; i < tokens.length; i++) {
                /*
                NN Noun, singular or mass
                NNS Noun, plural
                NNP Proper noun, singular
                NNPS Proper noun, plural
                */
                if((tags[i].equals("NN") || tags[i].equals("NNS") || tags[i].equals("NNP") || tags[i].equals("NNPS")) && probabilities[i] >= 0.5) {
                    nouns.add(tokens[i]);
                }
            }

            return nouns;
        } catch(Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
        return null;
    }

    public ArrayList<String> getNounGroups(String sentence) {
        POSTaggerME tagger = new POSTaggerME(model);

        try {
            String[] tokens = tokenizer.tokenize(sentence);
            String[] tags = tagger.tag(tokens);
            double[] probabilities = tagger.probs();

            InputStream inputStream = OpenNLP.class.getClassLoader().getResourceAsStream("en-chunker.bin");

            if(inputStream == null) {
                System.out.println("Failed to load en-chunker.bin file! Exiting.");
                System.exit(0);
            }

            ChunkerModel chunkerModel = new ChunkerModel(inputStream);
            ChunkerME chunkerME = new ChunkerME(chunkerModel);
            String[] chunks = chunkerME.chunk(tokens, tags);

            ArrayList<String> nouns = new ArrayList<>();

            for(int i = 0; i < chunks.length; i++) {
                if(chunks[i].equals("B-NP")) {
                    StringBuilder temp = new StringBuilder(tokens[i] + " ");
                    while(i < chunks.length - 1 && chunks[i+1].contains("I")) {
                        i++;
                        if(chunks[i].contains("NP") && probabilities[i] >= 0.5) {
                            temp.append(tokens[i]).append(" ");
                        }
                    }
                    nouns.add(temp.toString().trim());
                }
            }

            inputStream.close();
            return nouns;
        } catch(Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
        return null;
    }

    /*
    Convert words to Lemma form if possible. Lemma = words of different "form" that all have same meaning.
    See https://en.wikipedia.org/wiki/Lemmatisation
     */
    public String lemmatize(String s) {
        s = Utilities.getAlphanumericString(s).toLowerCase();
        String returnString = "";
        try {
            POSTaggerME tagger = new POSTaggerME(model);
            String[] tokens = tokenizer.tokenize(s);
            String[] tags = tagger.tag(tokens);

            String[] lemmas = lemmatizer.lemmatize(tokens, tags);

            for(int i = 0; i < tokens.length; i++) {
                if(!lemmas[i].equals("O")) {
                    tokens[i] = lemmas[i];
                }
            }

            returnString = String.join(" ", tokens);
        } catch (Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }

        return returnString;
    }
}
