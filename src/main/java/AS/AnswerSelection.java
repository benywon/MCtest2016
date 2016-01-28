package AS;

import MCTest.Document;
import MCTest.MCStructure;
import MCTest.process;
import edu.stanford.nlp.util.CoreMap;
import preprocess.MCfeatures;
import preprocess.MCinfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**this is our baseline answer selection method similar with
 [Smith et al., 2015] Ellery Smith, Nicola Greco, Matko
 Bosnjak, and Andreas Vlachos. A strong lexical match-
 ing method for the machine comprehension test. In Pro-
 ceedings of the 2015 Conference on Empirical Methods in
 Natural Language Processing, September 2015.
 * Created by benywon on 1/19 0019.
 */
public class AnswerSelection extends MCfeatures
{
    private List<MCinfo> mCinfo=new ArrayList<>();
    public static Set<String> ASfeatureSet=new HashSet<>();
    static {
        ASfeatureSet.add(BOW);
        ASfeatureSet.add(Dependency);
        ASfeatureSet.add(POS);
        ASfeatureSet.add(NER);
        ASfeatureSet.add(DRoot);
        ASfeatureSet.add(Ngram);
        ASfeatureSet.add(constituency);
    }
    public AnswerSelection(MCStructure mcStructure)
    {
        super(ASfeatureSet);
        Process(mcStructure);
    }
    public AnswerSelection()
    {
        super(ASfeatureSet);
    }
    public static void main(String[] args)
    {
        process process=new process(500,"train",true);
        AnswerSelection answerSelection=new AnswerSelection();
        answerSelection.Process(process.MCstructure);
        System.out.println("ok");
    }
    public void dealMCdocuments(Document document)
    {
        String documentString=document.getDocument();
        this.setDocSentences(documentString);
        document.getQuestions().forEach(x->
        {
            MCinfo mCinfo=new MCinfo();
            mCinfo.documents=documentString;
            String question=x.getQuestion();
            this.setquestionSentence(question);
            mCinfo.question=question;
            getSentence(mCinfo);
            this.mCinfo.add(mCinfo);
            clear();
        });
    }
    public void getSentence(MCinfo mCinfo)
    {
        this.DocSentences.forEach(x->
                {
                    String sentences =x.toString();
                    mCinfo.sentences.add(sentences);
                    mCinfo.scores.add(getAllfeatures(x));
                }
        );
    }
}
