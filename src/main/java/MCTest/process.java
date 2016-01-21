package MCTest;

import baseline.DistanceBased;
import baseline.SWbeta;
import baseline.SlidingWindow;
import publicMethods.Filebases;
import publicMethods.Myconfig;

import java.io.File;

/**
 * Created by benywon on 2015/10/24.
 */
public class process
{
    public MCStructure MCstructure;
    public static final String DataRootDir=Myconfig.Getconfiginfo("MCtestDir");

    private static final String MCtestFilePathTemplate=DataRootDir+"MCTest/mc_MCID_._PURPOSE_._IsAnswer_";

    public process(int id,String purpose)
    {
        String filepath=getFilePath(id,purpose,false);
        this.MCstructure=new MCStructure(filepath);
        filepath=getFilePath(id,purpose,true);
        this.MCstructure.setAnswerFromFile(filepath);
    }
    public float evaluate(String method)
    {
        Evaluate evaluate=new Evaluate();
        if(method.equals("precision"))
        {
            return evaluate.Precision(this.MCstructure);
        }
        return 0.0f;
    }
    private String getFilePath(int id,String purpose,boolean IsAnswer)
    {
        String filepath=MCtestFilePathTemplate;
        if(id==500)
        {
            filepath=filepath.replaceAll("_MCID_","500");
        }
        else
        {
            filepath=filepath.replaceAll("_MCID_","160");
        }

        filepath=filepath.replaceAll("_PURPOSE_",purpose);
        if(IsAnswer)
        {
            filepath = filepath.replaceAll("_IsAnswer_","ans");
        }
        else
        {
            filepath = filepath.replaceAll("_IsAnswer_","tsv");
        }
        return filepath;
    }
    public void Process(String method)
    {
        if(method.equals("SWbeta"))
        {
            SWbeta sWbeta=new SWbeta(this.MCstructure);
        }
        else if(method.equals("SW"))
        {
            SlidingWindow slidingWindow=new SlidingWindow(this.MCstructure);
        }
        else if(method.equals("SW+D"))
        {
            DistanceBased ds=new DistanceBased(this.MCstructure);
        }
    }
    public void AddAnswerdQuestion2File(String method)
    {
        String filename=DataRootDir+"result/baseline/"+method+".txt";
        StringBuilder strYes=new StringBuilder();
        for(Document document:this.MCstructure.documents)
        {
            strYes.append(getResultDocument(document));
        }
        Filebases.Write2File(strYes.toString(),filename,false);
    }
    private StringBuffer getResultDocument(Document document)
    {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(document.getDocument());
        addCR(stringBuffer);
        document.getQuestions().forEach(x -> stringBuffer.append(getResultQuestion(x)));
        addCR(stringBuffer);
        return stringBuffer;
    }
    private StringBuffer getResultQuestion(Question question)
    {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(question.getQuestion());
        String rightorNo="\t"+(question.isRightAnswerd()?"YES":"NO");
        stringBuffer.append(rightorNo);
        addCR(stringBuffer);
        question.getAnswer().forEach(x -> {
            stringBuffer.append(getResultAnswer(x));
        });
        addCR(stringBuffer);

        return stringBuffer;
    }
    private StringBuffer getResultAnswer(Answer answer)
    {
        StringBuffer stringBuffer=new StringBuffer();
        stringBuffer.append(answer.getAnswer()+"--------"+answer.isAnswer()+"~~~~"+answer.isPredictResult());
        addCR(stringBuffer);
        return stringBuffer;
    }
    /**
     * add carrier return
     * @param sb stringbuffer
     */
    private void addCR(StringBuffer sb)
    {
        sb.append("\r\n");
    }
}
