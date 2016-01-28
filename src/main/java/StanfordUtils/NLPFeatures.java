package StanfordUtils;

import edu.stanford.nlp.util.StringUtils;

import java.util.*;

/**
 * this is feature method we use in our model
 * Created by benywon on 1/19 0019.
 */
public class NLPFeatures
{
    /**
     * the bag of words matching between two sentence
     * in order of the matching problem not affected by length
     * we divide the max length
     * @param sent1 sent1
     * @param sent2 sent2
     * @return matching value
     */
    public static float get2SETmatching(String sent1, String sent2)
    {
        Set<String> set1=new HashSet<>();
        set1.addAll(Arrays.asList(sent1.split(" ")));
        Set<String> set2=new HashSet<>();
        set2.addAll(Arrays.asList(sent2.split(" ")));
        return get2SETmatching(set1,set2);
    }
    public static float get2SETmatching(Set<String> set1, Set<String> set2)
    {
        int len=set1.size()>set2.size()?set1.size():set2.size();
        final float[] match = {0};
        set1.forEach(x->{
                    if(set2.contains(x))
                    {
                        match[0]++;
                    }
                }
        );
        return match[0]/len;
    }

    /**
     * the dependency matching between two sentence
     * @param sent1 sent1
     * @param sent2 sent2
     * @return value
     */
    public static float getDepMatching(String sent1,String sent2)
    {
        Dependency dependency=new Dependency();
        Dependency.DependencyTree dependencyTree1=dependency.annoSentence(sent1);
        Dependency.DependencyTree dependencyTree2=dependency.annoSentence(sent2);
        return getDepMatching(dependencyTree1,dependencyTree2);
    }
    public static float getDepMatching(Dependency.DependencyTree dependencyTree1, Dependency.DependencyTree dependencyTree2)
    {
        int len=dependencyTree1.size()>dependencyTree2.size()?dependencyTree1.size():dependencyTree2.size();
        final float[] match = {0};
        dependencyTree1.dList.forEach(x-> dependencyTree2.dList.forEach(y->{
            if(x.equals(y))
            {
                match[0]++;
            }
        }));
        return match[0]/len;
    }
    /**
     * named entity matching
     * 1 for matching
     * 0 for do not have ne
     * -1 for NE not matching
     * @param sent1
     * @param sent2
     * @return
     */
    public static float getNEmatching(String sent1,String sent2)
    {
        NER ner1=new NER(sent1);
        NER ner2=new NER(sent2);
        List<CoreNLPBase.Pair> list1=ner1.getNEPair();
        List<CoreNLPBase.Pair> list2=ner2.getNEPair();
        return getNEmatching(list1,list2);
    }
    public static float getNEmatching(List<CoreNLPBase.Pair> list1,List<CoreNLPBase.Pair> list2)
    {
        if(list1.size()==0||list2.size()==0)
        {
            return 0;
        }
        final float[] match = {0};
        list1.forEach(x-> list2.forEach(y->{
            if(y.label.equals(x.label))
            {
                if(x.word.equals(y.word))
                {
                    match[0]++;
                }
            }
        }));
        if(match[0]>0)
        {
            return 1;
        }
        return 0;
    }

    public static float getPOSmatching(String sent1,String sent2)
    {
        POS pos1=new POS(sent1);
        POS pos2=new POS(sent2);
        List<String> list1=pos1.getPOS();
        List<String> list2=pos2.getPOS();
        Set<String> set1=new HashSet<>(list1);
        Set<String> set2=new HashSet<>(list2);
        final float[] match = {0};
        int len=set1.size()>set2.size()?set1.size():set2.size();
        set1.forEach(x-> set2.forEach(y->{
            if(x.equals(y))
            {
                match[0]++;
            }
        }));
        return match[0]/len;
    }
    public static float getParsingMatching(String sent1,String sent2)
    {
        Parsing parsing1=new Parsing(sent1);
        Parsing parsing2=new Parsing(sent2);
        List<Parsing.Triangle> list1=parsing1.annotateSentence();
        List<Parsing.Triangle> list2=parsing2.annotateSentence();
        return getParsingMatching(list1,list2);
    }
    public static float getParsingMatching(List<Parsing.Triangle> list1,List<Parsing.Triangle> list2)
    {
        int len=list1.size()>list2.size()?list1.size():list2.size();
        final float[] match = {0};
        list1.forEach(x-> list2.forEach(y->{
            if(x.equals(y))
            {
                match[0]++;
            }
        }));
        return match[0]/len;
    }

    /**
     * 我们找最短的那一个匹配 就是两句话 看看那个话的长度短 就以这个为单位进行匹配
     * 看看其中所有词是不是都在另外一个句子中出现了
     * @param list1 第一个列表
     * @param list2 第二个列表
     * @param N N-gram的N
     * @return 匹配度
     */
    public static float getNgramMatching(List<String> list1,List<String> list2,int N)
    {
        int len1=list1.size();
        int len2=list2.size();
        int maxLen=len1>len2?len2:len1;
        boolean List1OrList2=len2>len1;
        if(N>0)
        {
            maxLen=N>=maxLen?maxLen:N;
        }
        float value;
        if(List1OrList2)
        {
            value=getListNgramMatching(list2,list1,maxLen);
        }
        else
        {
            value=getListNgramMatching(list1,list2,maxLen);
        }
        return value;
    }
    public static float getNgramMatching(String in1,String in2,int N)
    {
        List<String> list1=Arrays.asList(in1.split(" "));
        List<String> list2=Arrays.asList(in2.split(" "));
        return getNgramMatching(list1,list2,N);
    }
    private static float getListNgramMatching(List<String> Long,List<String> Short,int MaxLen)
    {
        int lenS=Short.size();
        int lenL=Long.size();
        List<Float> matches=new ArrayList<>();
        for (int i = 1; i <=MaxLen; i++)
        {
            float number1=0;
            float number2=0;
            for (int j = 0; (i+j) <= lenS; j++)
            {
                number1++;
                List<String> listshort=Short.subList(j,j+i);
                for (int k = 0; (k+i) <= lenL; k++)
                {
                    List<String> listLong=Long.subList(k,k+i);
                    String longStr=StringUtils.join(listLong);
                    String shortStr=StringUtils.join(listshort);
                    if(longStr.equals(shortStr))
                    {
                        number2++;
                        break;
                    }
                }
            }
            matches.add(number1==0?0:number2/number1);
        }
        return getListWeigthMatching(matches);
    }

    /**
     * the input are 1-n matches score
     * we set 1-gram weight w  and 2-gram:w+x 3-gram:w+2x ...n-gram:w+(n-1)x
     * they total sum to 1
     * THE INITIATION WEIGHT W IS VERY IMPORTANT
     * WE SUGGEST W1=1.0/(2n)  W2=1.0/（n+n^2）
     * if the sentence matching are restrict.use W2
     * when set w as default 1.0/(2n)
     * x=2(1-nw)/(n(n-1))
     * @param matches input score
     * @return output score
     */
    private static float getListWeigthMatching(List<Float> matches)
    {
        int n=matches.size();
        if(n==1)
        {
            return matches.get(0);
        }
        double w=1.0/(2*n);
        double x=(2*(1-n*w))/(n*(n-1));
        float score=0;
        for (int i = 0; i < n; i++)
        {
            score+=matches.get(i)*(w+i*x);
        }
        return score;
    }
    public static void main(String[] args)
    {
        String sten1="Alyssa got to the beach after a long trip";
        String sten2="Alyssa is in the Miami city.";
        System.out.println(getNgramMatching(sten1,sten2,2));
    }
}
