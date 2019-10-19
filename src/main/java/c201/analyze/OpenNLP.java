package c201.analyze;

import opennlp.tools.chunker.ChunkerME;
import opennlp.tools.chunker.ChunkerModel;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

import java.io.InputStream;
import java.util.ArrayList;

public class OpenNLP {
    private POSModel model;
    private WhitespaceTokenizer tokenizer = WhitespaceTokenizer.INSTANCE;

    public OpenNLP() {
        try {
            //Loading Parts of speech-maxent model
            InputStream inputStream = OpenNLP.class.getClassLoader().getResourceAsStream("en-pos-maxent.bin");
            if(inputStream == null) {
                System.out.println("Failed to load en-pos-maxent.bin file! Exiting.");
                System.exit(0);
            }
            model = new POSModel(inputStream);
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

            return nouns;
        } catch(Exception e) {
            System.out.println(e.getMessage() + " " + e.getCause());
        }
        return null;
    }
}
