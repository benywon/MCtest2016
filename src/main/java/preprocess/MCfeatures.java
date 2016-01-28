package preprocess;

import MCTest.Document;
import MCTest.MCStructure;
import StanfordUtils.*;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;
import publicMethods.CompareUtils;
import publicMethods.Filebases;
import publicMethods.MathUtils;
import publicMethods.SortUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by benywon on 1/28 0028.
 */
public class MCfeatures
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
     * type:8| 1-n gram weight average
     */
    public static final String BOW="bag_of_words";
    public static final String Dependency="dependency matching";
    public static final String DRoot="dependency root matching";
    public static final String POS="part of speech";
    public static final String constituency="constituency matching";
    public static final String word2vec="word embedding matching";
    public static final String NER="named entity matching";
    public static final String Ngram="1-n gram weight average";

    public float BOW_match;
    public float Dependency_match;
    public float DependencyRoot_match;
    public float POS_match;
    public float Constituency_match;
    public float WordEmbedding_match;
    public float NE_match;
    public float N_gram_match;

    protected Set<String> attrs=new HashSet<>();

    public List<CoreMap> DocSentences;


    public MCfeatures(Set<String> aSfeatureSet)
    {
        this.attrs=aSfeatureSet;
    }

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
    public CoreMap questionSentence;

    public Map<String,Float> scores=new HashMap<>();

    public void Process(MCStructure mcStructure)
    {
        List<Document> documents=mcStructure.documents;
        documents.forEach(this::dealMCdocuments);
    }
    /**
     * this is the method that should be overwrite in chile method
     */
    public void dealMCdocuments(Document document)
    {
        String documentString=document.getDocument();
        this.setDocSentences(documentString);
        document.getQuestions().forEach(x->
        {
            String question=x.getQuestion();
            this.setquestionSentence(question);
            getmaxSentence();
            clear();
        });
    }
    public void clear()
    {
        this.scores.clear();
    }
    public MCfeatures(String questionString, String documentString)
    {
        CoreNLPBase doc=new CoreNLPBase(documentString);
        CoreNLPBase q=new CoreNLPBase(questionString);
        this.DocSentences = doc.document.get(CoreAnnotations.SentencesAnnotation.class);
        this.questionSentence=q.document.get(CoreAnnotations.SentencesAnnotation.class).get(0);
        this.getmaxSentence();
    }
    public void getmaxSentence()
    {
        this.DocSentences.forEach(x->
                {
                    String sentences =x.toString();
                    Float result=weightScore(getAllfeatures(x));
                    scores.put(sentences,result);
                }
        );
    }

    /**
     * this is the method that should be overwrite in chile method
     * @param sentence in sentence
     * @return score
     */
    public List<Feature> getAllfeatures(CoreMap sentence)
    {
        if(this.attrs.contains(BOW))
        {
            getBOW(sentence);
        }
        if(this.attrs.contains(Dependency))
        {
            getDependency(sentence);
        }
        if(this.attrs.contains(POS))
        {
            getPOS(sentence);
        }
        if(this.attrs.contains(constituency))
        {
            getConstituency(sentence);
        }
        if(this.attrs.contains(word2vec))
        {
            getWordEmbedding(sentence);
        }
        if(this.attrs.contains(NER))
        {
            getNER(sentence);
        }
        if(this.attrs.contains(Ngram))
        {
            getNgram(sentence);
        }
        return getScore();
    }
    /**
     * this is the method that should be overwrite in chile method
     * @return score
     */
    public List<Feature> getScore()
    {
        List<Feature> scoreList=new ArrayList<>();
        if(this.attrs.contains(BOW))
        {
            Feature feature=new Feature(BOW, getBOW_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(Dependency))
        {
            Feature feature=new Feature(Dependency, getDependency_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(NER))
        {
            Feature feature=new Feature(NER, getNE_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(Ngram))
        {
            Feature feature=new Feature(Ngram, getN_gram_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(POS))
        {
            Feature feature=new Feature(POS, getPOS_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(word2vec))
        {
            Feature feature=new Feature(word2vec, getWordEmbedding_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(constituency))
        {
            Feature feature=new Feature(constituency, getConstituency_match());
            scoreList.add(feature);
        }
        if(this.attrs.contains(DRoot))
        {
            Feature feature=new Feature(DRoot, getDependencyRoot_match());
            scoreList.add(feature);
        }
        return scoreList;
    }

    /**
     * we use average to all features
     * we can add more strategy to this method
     * @param scoreList score list
     * @return score
     */
    public float weightScore(List<Feature> scoreList)
    {
        return MathUtils.ListAverage(scoreList.stream().map(Feature::getFeatureValue).collect(Collectors.toList()));
    }

    public void getBOW(CoreMap sentence)
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
        this.BOW_match = NLPFeatures.get2SETmatching(sentenceSet,questionSet);
    }
    public void getDependency(CoreMap sentence)
    {
        Dependency dependency=new Dependency();
        Dependency.DependencyTree dependencyTreeS=dependency.annoSentence(sentence);
        Dependency.DependencyTree dependencyTreeQ=dependency.annoSentence(this.questionSentence);
        this.Dependency_match =NLPFeatures.getDepMatching(dependencyTreeS,dependencyTreeQ);
        if(dependencyTreeS.root.equals(dependencyTreeQ.root))
        {
            this.DependencyRoot_match =1;
        }
        else
        {
            this.DependencyRoot_match =0;
        }
    }
    public void getPOS(CoreMap sentence)
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
        this.POS_match = NLPFeatures.get2SETmatching(sentenceSet,questionSet);
    }
    public void getConstituency(CoreMap sentence)
    {
        Parsing parsing1=new Parsing();
        Parsing parsing2=new Parsing();
        List<Parsing.Triangle> list1=parsing1.annotateSentence(sentence);
        List<Parsing.Triangle> list2=parsing2.annotateSentence(this.questionSentence);
        this.Constituency_match =NLPFeatures.getParsingMatching(list1,list2);
    }
    public void getWordEmbedding(CoreMap sentence)
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
        this.WordEmbedding_match = (float) CompareUtils.getSumsimlarity(sentenceList,questionList);
    }
    public void getNER(CoreMap sentence)
    {
        NER ner=new NER();
        List<CoreNLPBase.Pair> sentenceList=ner.getPair(sentence);
        List<CoreNLPBase.Pair> questionList=ner.getPair(this.questionSentence);
        this.NE_match = NLPFeatures.getNEmatching(sentenceList,questionList);
    }
    public void getNgram(CoreMap sentence)
    {
        List<String> sentenceList=new ArrayList<>();
        List<String> questionList=new ArrayList<>();
        sentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.TextAnnotation.class);
            sentenceList.add(word);
        });
        this.questionSentence.get(CoreAnnotations.TokensAnnotation.class).forEach(x->{
            String word = x.get(CoreAnnotations.TextAnnotation.class);
            questionList.add(word);
        });
        int ngramN = 5;
        this.N_gram_match =NLPFeatures.getNgramMatching(sentenceList,questionList, ngramN);
    }
    //**********************************************************************//
    //**********************************************************************//
    //**********************************************************************//
    public float getBOW_match()
    {
        return BOW_match;
    }

    public float getDependency_match()
    {
        return Dependency_match;
    }

    public float getDependencyRoot_match()
    {
        return DependencyRoot_match;
    }

    public float getPOS_match()
    {
        return POS_match;
    }

    public float getConstituency_match()
    {
        return Constituency_match;
    }

    public float getWordEmbedding_match()
    {
        return WordEmbedding_match;
    }

    public float getNE_match()
    {
        return NE_match;
    }

    public float getN_gram_match()
    {
        return N_gram_match;
    }
    //*********************************************************************//
    //**************************** write to file **********************************//
    //*********************************************************************//
}
