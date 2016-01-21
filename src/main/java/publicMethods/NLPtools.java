package publicMethods;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by benywon on 12/10 0010.
 */
public class NLPtools {
    private static Properties props = new Properties();
    private static StanfordCoreNLP pipeline;

    static {
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma");
        pipeline = new StanfordCoreNLP(props);
    }

    public static List<String> getWordsRaw(String sentence)
    {
        List<String> list=new ArrayList<String>();
        Annotation document = new Annotation(sentence);
        pipeline.annotate(document);
        List<CoreLabel> labels=document.get(CoreAnnotations.TokensAnnotation.class);
        labels.forEach(x->{
            String word=x.get(CoreAnnotations.TextAnnotation.class);
            list.add(word);
        });
        return list;
    }
    public static List<String> getWordsLemma(String sentence)
    {
        List<String> list=new ArrayList<String>();
        Annotation document = new Annotation(sentence);
        pipeline.annotate(document);
        List<CoreLabel> labels=document.get(CoreAnnotations.TokensAnnotation.class);
        labels.forEach(x->{
            String lemma=x.getString(CoreAnnotations.LemmaAnnotation.class);
            list.add(lemma);
        });
        return list;
    }
     class PosPair{
        public String Word;
        public String Pos;
        public PosPair(String word,String Pos) {
            Word = word;
            this.Pos=Pos;
        }
    }
    public static List<PosPair> getWordsPos(String sentence)
    {
        List<PosPair> list=new ArrayList<PosPair>();
        Annotation document = new Annotation(sentence);
        pipeline.annotate(document);
        List<CoreLabel> labels=document.get(CoreAnnotations.TokensAnnotation.class);
        labels.forEach(x->{
            String word=x.get(CoreAnnotations.TextAnnotation.class);
            String pos = x.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            list.add(new NLPtools().new PosPair(word,pos));
        });
        return list;
    }


}
