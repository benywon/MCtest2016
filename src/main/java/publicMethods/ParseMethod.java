package publicMethods;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by benywon on 2015/10/24.
 */
public class ParseMethod
{
    /**
     * ��һ��string�����ҵ�һ������
     * @param str ����string
     * @return ������������ Ҫ����û���ҵ�����-1
     */
    public static int FindNumFromStr(String str)
    {
        String regEx="(\\d+)|(һ)|(��|��)|(��)|(��)|(��)|(��)|(��)|(��)|(��)|(ʮ)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find()){
            return  -1;
        }
        else
        {
            String out = matcher.group();
            if(matcher.group(1)!=null)
            {
                return Integer.parseInt(out);
            }
            else
            {
                for(int i=2;i<11;i++)
                {
                    if(matcher.group(i)!=null)
                    {
                        return i-1;
                    }
                }
                return -1;
            }
        }
    }
    /**
     * ��һ��string�����ҵ���������
     * @param str ����string
     * @param mode �Ƿָ���
     * @return ������������ Ҫ����û���ҵ�����-1
     */
    public static int[] FindNumFromStr2(String str,String mode)
    {
        int base=1;
        if(str.contains("��"))
        {
            base=100;
        }
        else if(str.contains("ǧ"))
        {
            base=1000;
        }
        else if(str.contains("��"))
        {
            base=10000;
        }
        int[] num2=new int[2];
        String regEx="(\\d+)"+mode+"(\\d+)";
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(str);
        if(!matcher.find()){
            return  null;
        }
        else
        {
            num2[0]=Integer.parseInt(matcher.group(1))*base;
            num2[1]=Integer.parseInt(matcher.group(2))*base;
            return  num2;
        }
    }
}
