package stats;

import MCTest.Document;
import MCTest.MCStructure;
import MCTest.process;
import publicMethods.NLPtools;
import publicMethods.SortUtils;

import java.util.*;

import static publicMethods.Filebases.WriteMap2File;

/**
 * Created by benywon on 12/10 0010.
 */
public class Question {

    public static double WordsPerQuestion=-1;
    public static Map<String,Integer> QuestionType=new HashMap<>();
    private static List<List<String>> QuestionList=new ArrayList<>();
    private static boolean UseLemma=false;//Ĭ�ϲ�ʹ��lemma
    public void UseLemma()
    {
        UseLemma=true;
    }
    public void UseWord()
    {
        UseLemma=false;
    }
    public boolean IsUseLemma()
    {
        return UseLemma;
    }
    public Question(MCStructure mcStructure)
    {
        AddStructure(mcStructure);
    }
    public Question(MCStructure mcStructure,boolean useLemma)
    {
        UseLemma=useLemma;
        AddStructure(mcStructure);
    }
    public void AddStructure(MCStructure mcStructure)
    {
        mcStructure.documents.forEach(doc->addQtypeStaticsPerDoc(doc));
        CalcNumber();
    }
    private void addQtypeStaticsPerDoc(Document document)
    {
        document.getQuestions().forEach(question -> addQTypeStaticsPerQuestion(question));
    }
    private void addQTypeStaticsPerQuestion(MCTest.Question question)
    {
        List<String> term;
        if(UseLemma)
        {
            term= NLPtools.getWordsRaw(question.getQuestion());
        }
        else
        {
            term=NLPtools.getWordsLemma(question.getQuestion());
        }
        Add2List(term);
    }
    private void Add2List(List<String> QuestionItermList)
    {
        QuestionList.add(QuestionItermList);
        String Qword=QuestionItermList.get(0);
        Optional<Integer> number=Optional.ofNullable(QuestionType.get(Qword));
        QuestionType.put(Qword, number.orElse(0) + 1);
    }
    private static void CalcNumber()
    {
        Integer sum=QuestionList.stream().map(x->x.size()).reduce(0, (a, b) -> a + b);
        Integer totalQ=QuestionList.size();
        WordsPerQuestion=(double)(sum/totalQ);
    }
    public void clear()
    {
        WordsPerQuestion=-1;
        QuestionList.clear();
        QuestionType.clear();
    }
    public static void main(String[] args) {
        process process=new process(500,"train");
        Question question=new Question(process.MCstructure);
        process=new process(500,"test");
        question.AddStructure(process.MCstructure);
        process=new process(500,"dev");
        question.AddStructure(process.MCstructure);
        Map<Object, Object> map= SortUtils.sortByValue(QuestionType, false);
        System.out.println(question.WordsPerQuestion);
        WriteMap2File("question500.txt", map);
        map.clear();
        question.clear();
        process=new process(160,"dev");
        question.AddStructure(process.MCstructure);
        process=new process(160,"train");
        question.AddStructure(process.MCstructure);
        process=new process(160,"test");
        question.AddStructure(process.MCstructure);
        map= SortUtils.sortByValue(QuestionType, false);
        System.out.println(question.WordsPerQuestion);
        WriteMap2File("question160.txt", map);
    }
}
