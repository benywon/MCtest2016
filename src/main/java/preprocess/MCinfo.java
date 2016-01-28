package preprocess;

import java.util.ArrayList;
import java.util.List;

/**
 * every information for a question
 * Created by benywon on 1/28 0028.
 */
public class MCinfo
{
    public String question;
    public String documents;
    public List<String> sentences=new ArrayList<>();
    public List<List<Feature>> scores=new ArrayList<>();

    public MCinfo()
    {

    }
}
