package StanfordUtils;

import edu.stanford.nlp.dcoref.CorefChain;
import edu.stanford.nlp.dcoref.CorefCoreAnnotations;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by benywon on 1/20 0020.
 */
public class Coreference extends Base
{
    public Coreference(String document)
    {
        super(document);
    }
    public String getResolvedSentence()
    {
        Map<Integer, CorefChain> graph = this.document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
        List<CoreMap> sentencesMap = this.document.get(CoreAnnotations.SentencesAnnotation.class);
        List<String> sentences=sentencesMap.stream().map(x->x.toString()).collect(Collectors.toList());
        graph.forEach((key,value)->{
            setcoreMap(value,sentences);
        });
        return StringUtils.join(sentences);
    }
    private void setcoreMap(CorefChain corefChain,List<String> sentences)
    {
        String baseName=corefChain.getRepresentativeMention().mentionSpan;
        if(!corefChain.getRepresentativeMention().animacy.name().equals("ANIMATE"))
        {
            return;
        }
        corefChain.getMentionsInTextualOrder().forEach(x->{
            String str=x.mentionSpan;
            if(isValidPronoun(str))
            {
                int id=x.sentNum-1;
                String sentence=sentences.get(id);
                List<String> wordList= Arrays.asList(sentence.split(" "));
                wordList=wordList.stream().map(m->transferName(m,str,baseName)).collect(Collectors.toList());
                sentences.set(id,StringUtils.join(wordList," "));
            }
        });
    }
    private String transferName(String s,String str,String baseName)
    {
        if(s.contains(str))
        {
            if(s.endsWith("'s"))
            {
                s=baseName+" is";
            }
            else
            {
                s = baseName;
            }
        }
        return s;
    }
    private boolean isValidPronoun(String str)
    {
        str=str.toLowerCase();
        if(str.equals("she"))
            return true;
        if(str.equals("he"))
            return true;
        if(str.equals("it"))
            return true;
        if(str.equals("I"))
            return true;
        return false;
    }
    public static void main(String[] args)
    {
        String documents="Sean the dragon liked the color green. He didn't like golden coins. They were too yellow. Every day he would go out and find green things to cover his bed of old treasure. He would put grass and clovers and leaves and vines all over his cave. The other dragons were worried.  When you breathe fire, it's not a good idea to sleep around a lot of plants.  They were afraid Sean might get hurt. Sean didn't think so but his bed never stayed green and he was getting tired of doing nothing but making his bed.  One day he went to visit his friend Zarah and he saw a piece of green in a golden necklace. \"What's that?\" he asked.  \"That's a green stone,\" she told him. \"Sometimes my treasures have them. I don't really like them. They're too green. I like yellow things.\"  \"If I bring you yellow things, can I have it?\" Sean asked. \"Yes you can,\" Zarah said, \"But it has to be treasure, or things that won't catch fire.\"  So Sean went to go find treasure. When he was gone Zarah told the other dragons. They said that they would trade all their green treasure to Sean too so he could have a safe dragon bed in his cave.  Soon Sean had more than enough green stones to cover his bed.  Green stones never got old and brown.  They don't catch fire either.  So, now everyone was happy.";
        Coreference coreference=new Coreference(documents);
        String after=coreference.getResolvedSentence();
        System.out.println(after);
    }
}
