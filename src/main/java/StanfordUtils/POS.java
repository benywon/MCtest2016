package StanfordUtils;

import edu.stanford.nlp.ling.CoreAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 1/20 0020.
 */
public class POS extends Base
{

    public POS(String text)
    {
        super(text);
    }
    public List<String> getPOS()
    {
        List<String> list=new ArrayList<>();
        this.document.get(CoreAnnotations.TokensAnnotation.class).forEach(token->
                {
                    String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                    list.add(pos);
                }
        );
        return list;
    }
}
