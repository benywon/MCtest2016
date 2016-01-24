package AS;

import StanfordUtils.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.math.DoubleAD;
import edu.stanford.nlp.maxent.Features;
import edu.stanford.nlp.util.CoreMap;
import preprocess.WordEmbedding;
import publicMethods.CompareUtils;

import java.util.*;

/**this is our baseline answer selection method similar with
 [Smith et al., 2015] Ellery Smith, Nicola Greco, Matko
 Bosnjak, and Andreas Vlachos. A strong lexical match-
 ing method for the machine comprehension test. In Pro-
 ceedings of the 2015 Conference on Empirical Methods in
 Natural Language Processing, September 2015.
 * Created by benywon on 1/19 0019.
 */
public class AnswerSelection
{
    /**
     * to build a answer selection linguistic model.we must use a lot of features
     * the features we use are list below
     * type:1| bag of words matching
     * type:2| dependency matching: all args of a tipple must all equals
     * type:3| dependency root matching
     * type:4| POS matching
     * type:5| constituency matching
     * type:6| word embedding matching
     * type:7| named entity matching
     */
    private float type1;
    private float type2;
    private float type3;
    private float type4;
    private float type5;
    private double type6;
    private float type7;
    private String QuestionString="";
    private String DocumentString="";
    private List<CoreMap> DocSentences;
    private CoreMap questionSentence;
    public AnswerSelection(String questionString, String documentString)
    {
        QuestionString = questionString;
        DocumentString = documentString;
        CoreNLPBase doc=new CoreNLPBase(documentString);
        CoreNLPBase q=new CoreNLPBase(questionString);
        this.DocSentences = doc.document.get(CoreAnnotations.SentencesAnnotation.class);
        this.questionSentence=q.document.get(CoreAnnotations.SentencesAnnotation.class).get(0);
    }
    public static void main(String[] args)
    {
        String document="Mary was spending a few days over her grandma and grandpa's house. Mary and her grandpa went to the park on Thursday morning. Mary had so much fun with him, and Mary and her grandpa were smiling the whole time! grandpa 's pushed her on the swings, then helped her go down the slide. After then left the park, then went back to her grandpa's house. Mary asked her grandpa to make her lunch because Mary was starving! her grandpa to make her lunch because she was starving told her that her grandpa to make her lunch because she was starving could make her a few things. Mary could choose between chicken and pasta, beef and rice, or pizza and salad. Mary asked him to make her chicken and pasta. They ate lunch together at the kitchen table. The next day, Mary and her grandma went to see a movie at the movie theater. There was a new cartoon movie about cats and dogs that Mary couldn't wait to see! cats and dogs ate popcorn and candy, and Mary had some juice. On Saturday, Mary's grandparents brought her back home to her mom and dad. Mary 's grandparents were so excited to see her! Mary spent Sunday with her mom, dad, grandma, and grandpa. her mom and dad had a big picnic, and a big picnic was a great end to the week.";
        String question_str="What day did Mary and her grandma see a movie?";
        CoreNLPBase documents=new CoreNLPBase(document);
        CoreNLPBase question=new CoreNLPBase(question_str);
    }
    private void getBOW(CoreMap sentence)
    {
        //first get the question bag of words
        Set<String> sentenceSet=new HashSet<>();
        Set<String> questionSet=new HashSet<>();
        sentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.TextAnnotation.class);
            sentenceSet.add(word);
        });
        this.questionSentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.TextAnnotation.class);
            questionSet.add(word);
        });
        this.type1= NLPFeatures.get2SETmatching(sentenceSet,questionSet);
    }
    private void getDependency(CoreMap sentence)
    {
        Dependency dependency=new Dependency();
        Dependency.DependencyTree dependencyTreeS=dependency.annoSentence(sentence);
        Dependency.DependencyTree dependencyTreeQ=dependency.annoSentence(this.questionSentence);
        this.type2=NLPFeatures.getDepMatching(dependencyTreeS,dependencyTreeQ);
        if(dependencyTreeS.root.equals(dependencyTreeQ.root))
        {
            this.type3=1;
        }
        else
        {
            this.type3=0;
        }
    }
    private void getPOS(CoreMap sentence)
    {
        Set<String> sentenceSet=new HashSet<>();
        Set<String> questionSet=new HashSet<>();
        sentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            sentenceSet.add(word);
        });
        this.questionSentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.PartOfSpeechAnnotation.class);
            questionSet.add(word);
        });
        this.type4= NLPFeatures.get2SETmatching(sentenceSet,questionSet);
    }
    private void getConstituency(CoreMap sentence)
    {
        Parsing parsing1=new Parsing();
        Parsing parsing2=new Parsing();
        List<Parsing.Triangle> list1=parsing1.annotateSentence(sentence);
        List<Parsing.Triangle> list2=parsing2.annotateSentence(this.questionSentence);
        this.type5=NLPFeatures.getParsingMatching(list1,list2);
    }
    private void getWordEmbedding(CoreMap sentence)
    {
        List<Double[]> sentenceList=new ArrayList<>();
        List<Double[]> questionList=new ArrayList<>();
        sentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.TextAnnotation.class);
            sentenceList.add(WordEmbedding.getOneWordVector(word));
        });
        this.questionSentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.TextAnnotation.class);
            questionList.add(WordEmbedding.getOneWordVector(word));
        });
        this.type6=CompareUtils.getSumsimlarity(sentenceList,questionList);
    }
    private void getNER(CoreMap sentence)
    {
        NER ner=new NER();
        List<CoreNLPBase.Pair> sentenceList=ner.getPair(sentence);
        List<CoreNLPBase.Pair> questionList=ner.getPair(this.questionSentence);
        this.type7= NLPFeatures.getNEmatching(sentenceList,questionList);
    }
}
