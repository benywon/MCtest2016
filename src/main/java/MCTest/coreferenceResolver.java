package MCTest;


import StanfordUtils.Coreference;
import edu.stanford.nlp.util.StringUtils;
import publicMethods.Filebases;

import java.util.List;
import java.util.stream.Collectors;

/** this is the coreference resolver for mcTest data
 * Created by benywon on 1/20 0020.
 */
public class coreferenceResolver
{
    /**
     * transfer all sentence to its co-reference chain
     */
    static int i=0;
    public void resolveFile(String filenme,String outputfilename)
    {
        List<String> list=Filebases.GetListFromFile(filenme);
        list=list.stream().map(this::transferSentence).collect(Collectors.toList());
        Filebases.WriteList2File(list,outputfilename);
    }
    private String transferSentence(String sentence)
    {
        String[] lists=sentence.split("\t");
        String story=lists[2];
        Coreference coreference=new Coreference(story);
        String after=coreference.getResolvedSentence();
        lists[2]=after;
        String tranferLine=StringUtils.join(lists,"\t");
        System.out.println(i++);
        return tranferLine;

    }

    public static void resolveMCtest()
    {
        coreferenceResolver coreferenceResolver=new coreferenceResolver();
        coreferenceResolver.resolveFile(process.DataRootDir+"MCTest/mc500.train.tsv",process.DataRootDir+"MCTest/1CR_mc500.train.tsv");
        coreferenceResolver.resolveFile(process.DataRootDir+"MCTest/mc500.test.tsv",process.DataRootDir+"MCTest/1CR_mc500.test.tsv");
        coreferenceResolver.resolveFile(process.DataRootDir+"MCTest/mc500.dev.tsv",process.DataRootDir+"MCTest/1CR_mc500.dev.tsv");
    }
}
