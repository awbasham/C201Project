package c201.analyze;

import c201.Article;
import c201.SimilarArticle;
import c201.Site;
import c201.Utilities;
import edu.ucla.sspace.common.Similarity;
import edu.ucla.sspace.lsa.LatentSemanticAnalysis;
import edu.ucla.sspace.text.StringDocument;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Algorithms {
    private static Logger ALGORITHMS_LOGGER = Logger.getLogger(Algorithms.class.getName());

    /*
    Dice Coefficient algorithm for String similarity comparison.
    Implementation taken from https://stackoverflow.com/a/47054785
    */
    public static double diceCoefficient(String s1, String s2) {
        Set<String> nx = new HashSet<String>();
        Set<String> ny = new HashSet<String>();

        for (int i=0; i < s1.length()-1; i++) {
            char x1 = s1.charAt(i);
            char x2 = s1.charAt(i+1);
            String tmp = "" + x1 + x2;
            nx.add(tmp);
        }
        for (int j=0; j < s2.length()-1; j++) {
            char y1 = s2.charAt(j);
            char y2 = s2.charAt(j+1);
            String tmp = "" + y1 + y2;
            ny.add(tmp);
        }

        Set<String> intersection = new HashSet<String>(nx);
        intersection.retainAll(ny);
        double totcombigrams = intersection.size();

        return (2*totcombigrams) / (nx.size()+ny.size());
    }

    /*
    Perform LSA (Latent Sentiment Analysis) then cosine similarity using S-Space library
    https://github.com/fozziethebeat/S-Space (modified to support Jama SVD dependency in this project)
    Returns a Key/Value pair of Article to array of Similar Articles based on threshold/cosine similarity
     */
    public static Map<Article, ArrayList<SimilarArticle>> lsaCosineSimilarity(ArrayList<Site> sites, double threshold, int dimensions, boolean lemmatize, boolean removeStopWords) {
        try {
            if(sites.size() <= 1 || (threshold < 0 || threshold > 1)) {
                throw new IllegalArgumentException("Sites cannot be empty and threshold must be 0 to 1.");
            }

            int numOfArticles = 0;
            int[] articleSizeIndex = new int[sites.size()];
            for (int i = 0; i < sites.size(); i++) {
                int size = sites.get(i).getArticles().size();
                numOfArticles += size;
                articleSizeIndex[i] = size;
            }

            LatentSemanticAnalysis lsa = new LatentSemanticAnalysis(dimensions, true);
            //Empty, but could add properties for additional processing
            Properties properties = new Properties();

            //Used for lemmatization
            OpenNLP openNLP = new OpenNLP();
            //Get stop words array to use if removing stop words, reduces I/O
            ArrayList<String> stopWords = Utilities.getStopWords();
            //Add all website articles to LSA Object for processing
            for(Site site : sites) {
                for(Article article : site.getArticles()) {
                    String text = article.getArticleText();
                    if(removeStopWords) {
                        text = Utilities.removeStopWords(text, stopWords);
                    }
                    if(lemmatize) {
                        text = openNLP.lemmatize(text);
                    }
                    lsa.processDocument(new StringDocument(text).reader());
                }
            }

            //Perform LSA/produces document vectors
            lsa.processSpace(properties);

            Map<Article, ArrayList<SimilarArticle>> returnMap = new HashMap<>();
            int sum = 0; //Keep track of how many articles have been covered
            //Perform Cosine Similarity, handling for 1D document vector array made from multiple site arrays
            for(int i = 0; i < sites.size(); i++) {
                for(int j = 0; j < articleSizeIndex[i]; j++) {
                    ArrayList<SimilarArticle> arr = new ArrayList<>();
                    if(i + 1 < sites.size()) {
                        for (int k = 0; k < articleSizeIndex[i + 1]; k++) {
                            double coeff = Similarity.cosineSimilarity(lsa.getDocumentVector(j + sum),
                                    lsa.getDocumentVector(articleSizeIndex[i] + sum + k));

                            if(coeff >= threshold) {
                                arr.add(new SimilarArticle(sites.get(i + 1).getArticles().get(k),
                                        coeff, sites.get(i + 1)));
                            }
                        }
                        returnMap.put(sites.get(i).getArticles().get(j), arr);
                    }
                }
                sum += articleSizeIndex[i];
            }

            return returnMap;

        } catch(IOException ioe) {
            ALGORITHMS_LOGGER.log(Level.SEVERE, "lsaCosineIOError", ioe);
        } catch(IllegalArgumentException iae) {
            ALGORITHMS_LOGGER.log(Level.SEVERE, "lsaCosineArgumentError", iae);
        }
        return null;
    }
}
