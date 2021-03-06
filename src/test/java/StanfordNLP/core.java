package StanfordNLP;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by benywon on 12/10 0010.
 */
public class core {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");

        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        // read some text in the text variable
        String text = "Bob was flying a toy plane in his yard.  He was having a great time!  Bob had a dog and a cat that were also playing in the yard.  He was also singing \"Twinkle, Twinkle Little Star.\"  Bob flew his toy plane too high.  It landed on the roof of his house.  Bob asked his dad to get it.  Bob's dad said he had to borrow a ladder from next door.  Bob's dad took a walk next door.  On the way, Bob's dad waited for a duck to cross the road.  Finally, Bob's dad went next door and asked his neighbor, Frank, for a ladder.  Frank gave the ladder to Bob's dad.  Frank also gave Bob's dad a toy car, a toy train, a toy boat, and a coloring book that Bob could play with in case he couldn't get the plane.  Bob's dad walked back to the house.  Bob's dad climbed the ladder and took the plane from the roof.  Bob laughed and flew the plane again.  Afterward, they wanted to go to the beach.  Bob wanted to bring his bicycle.  Bob's dad wanted to bring a towel and beach ball.  They made a big sand castle.  They saw a seagull walking on the sand.  It was getting late, so they went home and turned on the TV for a little bit.  They then went to bed, looking forward to another day of fun tomorrow!";

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        List<CoreLabel> ss=document.get(CoreAnnotations.TokensAnnotation.class);
        System.out.println(ss);
        for (CoreMap sentence : sentences) {
            for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
                // this is the text of the token

                String word = token.get(CoreAnnotations.TextAnnotation.class);
                // this is the POS tag of the token
                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);

            tree.pennPrint();
            // this is the StanfordUtils dependency graph of the current sentence
            SemanticGraph dependencies = sentence.get(SemanticGraphCoreAnnotations.CollapsedCCProcessedDependenciesAnnotation.class);

        }

        // This is the coreference link graph
        // Each chain stores a set of mentions that link to each other,
        // along with a method for getting the most representative mention
        // Both sentence and token offsets start at 1!
        Map<Integer, CorefChain> graph =
                document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        System.out.println(graph);
    }

}
