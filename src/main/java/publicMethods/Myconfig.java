package publicMethods;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by benywon on 2015/10/24.
 */
public class Myconfig {

    public static String configerationfilepath="config.xml";
    public Myconfig(String configerationfilepath) {
        this.configerationfilepath = configerationfilepath;
    }
    public Myconfig()
    {
    }
    public void setConfigerationfilepath(String configerationfilepath)
    {
        this.configerationfilepath = configerationfilepath;
    }
    /**
     * �������ļ��ж�ȡ��Ӧ����Ϣ
     * @param attr Ҫ��ȡ��Ϣ ����ֵ
     * @return ��ȡ������Ϣ
     */
    public  static String Getconfiginfo(String attr)
    {
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Element returnattr=doc.getElementsByTag(attr).first();
            if(returnattr==null)
            {
                return null;
            }
            else
            {
                return returnattr.text();
            }

        }
    }
    /**
     * �������ļ��ж�ȡ��Ӧ����Ϣ
     * @param attr Ҫ��ȡ��Ϣ ����ֵ
     *
     * @return ��ȡ������Ϣ
     */
    public  static String Getconfiginfo(String attr,String value)
    {
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements eles=doc.getElementsByTag(attr);
            Element returnattr=eles.attr("value", value).first();
            if(returnattr==null)
            {
                return null;
            }
            else
            {
                return returnattr.text();
            }

        }
    }

    /**
     * �õ�һϵ�еı�ǩ�ķ��� ֻ��ͨ����ǩ�õ���Ϣ
     */
    public static List<String> GetconfiginfoList(String attr)
    {
        List<String> list=new ArrayList<String>();
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements returnattr=doc.getElementsByTag(attr);
            for(Element element:returnattr)
            {
                list.add(element.text());
            }
        }
        return list;
    }

    /**
     * �õ�ĳ��tag�µ��������� ����һ���б�
     * @param tag
     * @return
     */
    public static List GetAllnodes(String tag)
    {
        List<String> list=new ArrayList<String>();
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Element returnattr=doc.getElementsByTag(tag).first();
            Elements eles=returnattr.children();
            for(Element element:eles)
            {
                list.add(element.text());
            }
        }
        return list;
    }
    /**
     * �õ�ĳ��tag�µ��������� ����һ���б�
     * @param tag
     * @return
     */
    public static List GetAllnodes(String tag,String value)
    {
        List<String> list=new ArrayList<String>();
        String xml=Filebases.readfile(configerationfilepath);
        if(xml==null||xml=="")
        {
            return null;
        }
        else
        {
            org.jsoup.nodes.Document doc = Jsoup.parse(xml);//
            Elements returnattr=doc.getElementsByTag(tag);
            for(Element ele:returnattr)
            {
                if(ele.attr("value").equals(value))
                {
                    Elements eles=ele.children();
                    for(Element element:eles)
                    {
                        list.add(element.text());
                    }
                    break;
                }
            }

        }
        return list;
    }
    public static void main(String[] args) {
//        Myconfig myconfig=new Myconfig();
        List ii=Myconfig.GetAllnodes("QueryRelate","1");
        System.out.println("jhidf");
    }
}
