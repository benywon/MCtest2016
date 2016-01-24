package StanfordUtils;

import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.Properties;

/**
 * Created by benywon on 1/13 0013.
 */
public class CoreNLPBase
{
    /**
     * tokenize  TokensAnnotation (list of tokens), and CharacterOffsetBeginAnnotation, CharacterOffsetEndAnnotation, TextAnnotation (for each token)
     * ssplit Splits a sequence of tokens into sentences.
     * dcoref Implements both pronominal and nominal coreference resolution.
     */
    private static Properties props = new Properties();
    private static StanfordCoreNLP pipeline;
    static {
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse,depparse,dcoref");
        pipeline = new StanfordCoreNLP(props);
    }
    public Annotation document;
    public String text="" ;
    public CoreNLPBase(String text)
    {
        if(!this.text.equals(text))
        {
            this.text=text;
            annotate(text);
        }
    }
    public CoreNLPBase()
    {

    }
    public void annotate(String txt)
    {
        this.text=txt;
        this.document = new Annotation(txt);
        pipeline.annotate(this.document);
    }
    public class Pair
    {
        public String word;
        public String label;
        public void print()
        {
            System.out.println(word+"------"+label);
        }
        public Pair(String word, String label)
        {
            this.word = word;
            this.label = label;
        }
    }
}
