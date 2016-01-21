package MCTest;

import publicMethods.Filebases;
import publicMethods.Myconfig;

import java.util.Set;

/**
 * Created by benywon on 2015/11/25.
 */
public class StopWords
{
    public static Set<String> stopWordsSet=Filebases.GetSetFromFile(Myconfig.Getconfiginfo("StopWordsFile"));
}
