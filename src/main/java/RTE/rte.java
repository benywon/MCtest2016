package RTE;

import StanfordUtils.CoreNLPBase;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.util.List;

/**
 * Created by benywon on 1/28 0028.
 */
public class rte
{
    /**
     * to build a answer selection linguistic model.we must use a lot of features
     * the features we use are list below
     * type:1| bag of words matching
     * type:2| dependency matching: all args of a tipple must all equals
     * type:3| dependency root matching
     * type:4| POS matching
     * type:5| constituency matchings
     * type:6| named entity matching
     * type:7| 1-n gram weight average
     */
    private float BOG_match;
    private float Dependency_match;
    private float DependencyRoot_match;
    private float POS_match;
    private float Constituency_match;
    private float NE_match;
    private float N_gram_match;
    private List<CoreMap> DocSentences;
    public void setDocSentences(String documents)
    {
        CoreNLPBase doc=new CoreNLPBase(documents);
        this.DocSentences = doc.document.get(CoreAnnotations.SentencesAnnotation.class);
    }
    public void setquestionSentence(String question)
    {
        CoreNLPBase q=new CoreNLPBase(question);
        this.questionSentence=q.document.get(CoreAnnotations.SentencesAnnotation.class).get(0);
    }
    private CoreMap questionSentence;



    public static void main(String[] args)
    {

    }
}
