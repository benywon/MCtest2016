package StanfordUtils;

import edu.stanford.nlp.ling.CoreAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * NER 工具
 * Created by benywon on 1/13 0013.
 */
public class NER extends Base
{
    /**
     * the input should be a sentence
     * @param txt input sentence
     */
    public NER(String txt)
    {
        super(txt);
    }
    public NER()
    {

    }
    public List<Pair> getPair()
    {
        List<Pair> list=new ArrayList<>();
        this.document.get(CoreAnnotations.TokensAnnotation.class).forEach(token->
                {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    list.add(new Pair(word,ne));
                }
        );
        return list;
    }
    public String Transfer2NE()
    {
        StringBuilder stringBuilder=new StringBuilder();
        this.document.get(CoreAnnotations.TokensAnnotation.class).forEach(token->
                {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String ne=token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    if (!ne.equals("O"))
                    {
                        append(stringBuilder, ne);
                    }
                    else
                    {
                        append(stringBuilder, word);
                    }
                }
        );
        return stringBuilder.toString();
    }
     public String Transfer2NE(String txt)
    {
        annotate(txt);
        return Transfer2NE();
    }
    public List<Pair> getNEPair()
    {
        List<Pair> listpair=new ArrayList<>();
        this.document.get(CoreAnnotations.TokensAnnotation.class).forEach(token->
                {
                    String word = token.get(CoreAnnotations.TextAnnotation.class);
                    String ne=token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                    if (!ne.equals("O"))
                    {
                       listpair.add(new Pair(word,ne));
                    }
                }
        );
        return listpair;
    }
    private void append(StringBuilder stringBuilder,String str)
    {
        stringBuilder.append(str);
        stringBuilder.append(" ");
    }
    public static void main(String[] args)
    {
        String sentence1="In physics , circular motion is a movement of an object along the circumference of a circle or rotation along a circular path.";
        String sentence="Creed had multiple nicknames, including The Master of Disaster, The King of Sting, The Dancing Destroyer, The Prince of Punch, The One and Only and The Count of Monte Fisto";
        NER ner=new NER(sentence);
        String str=ner.Transfer2NE();
        System.out.println("h");
    }
}
