package stats;

import MCTest.*;
import publicMethods.NLPtools;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * Created by benywon on 12/11 0011.
 */
public class Words
{
    public static Set<String> WordsLemma=new HashSet<>();
    public static Set<String> WordsRaw=new HashSet<>();
    public static Integer[] WordsOneDoc=new Integer[2];
    public static Integer[] WordsOneQuestion=new Integer[2];
    public static Integer[] WordsOneAnswer=new Integer[2];
    private static final int QUESTION=100;
    private static final int ANSWER=200;
    private static final int DOCUMENT=300;


    public Words(MCStructure mcStructure)
    {
        init();
        mcStructure.documents.forEach(doc -> AddSTAToneDoc(doc));
    }
    private void init()
    {
        Class<?> aClass = this.getClass();
        Field[] field=aClass.getFields();//获取全部参数
        Arrays.asList(field).forEach(x -> {
            int modifier = x.getModifiers();
            if (modifier == Modifier.PUBLIC + Modifier.STATIC) {
                String type = x.getType().getName();
                if (type.equals(Integer[].class.getName())) {
                    try {
                        x.set(this,initArray((Integer[]) x.get(this)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    private Integer[] initArray(Integer[] array)
    {
        for (int i = 0; i < array.length; i++) {
            array[i]=0;
        }
        return array;
    }
    private void AddSTAToneDoc(Document document)
    {
        AddSTAT(document.getDocument(),DOCUMENT);
        document.getQuestions().forEach(x -> AddSTAToneQ(x));
    }
    private void AddSTAToneQ(MCTest.Question question)
    {
        AddSTAT(question.getQuestion(),QUESTION);
        question.getAnswer().forEach(x -> AddSTAToneA(x));
    }
    private void AddSTAToneA(Answer answer)
    {
        AddSTAT(answer.getAnswer(), ANSWER);
    }
    private void AddSTAT(String STR,int type)
    {
        List<String> term;
        term= NLPtools.getWordsRaw(STR);
        WordsRaw.addAll(term);
        term=NLPtools.getWordsLemma(STR);
        WordsLemma.addAll(term);
        AddCount(term,type);
    }
    private void AddCount(List<String> list,int type)
    {
        switch (type)
        {
            case DOCUMENT:
            {
                AddArrays(WordsOneDoc,list);
            }break;
            case QUESTION:
            {
                AddArrays(WordsOneQuestion,list);
            }break;
            case ANSWER:
            {
                AddArrays(WordsOneAnswer,list);
            }break;
            default:{
                try {
                    throw new Exception("do not have this type");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }break;
        }
    }
    private void AddArrays(Integer[] WordsOne,List<String> list)
    {
        WordsOne[0]+=list.size();
        WordsOne[1]++;
    }
    public static void main(String[] args) {
        process process=new process(500,"train");
        Words words=new Words(process.MCstructure);
        System.out.println("hsidu");
    }
}
