package StanfordUtils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;

import java.util.ArrayList;
import java.util.List;

/**
 * Constituency Parsing
 * Created by benywon on 1/20 0020.
 */
public class Parsing extends Base
{
    public Parsing(String txt)
    {
        super(txt);
    }
    public Parsing()
    {

    }

    /**
     * 我们定义的树的数据结构 有可能存在一个的三角形 或者是一条线下来 但是绝对是得有
     */
    public class Triangle
    {
        public String root;
        public List<String> children=new ArrayList<>();
        public int size()
        {
            return this.children.size();
        }
        public boolean equals(Object object)
        {
            if(this==object)
            {
                return true;
            }
            if(getClass()!=object.getClass())
            {
                return false;
            }
            Triangle triangle=(Triangle)object;
            boolean is_equal=(this.root.equals(triangle.root))&&(this.size()==triangle.size());
            if(is_equal)
            {
                boolean equal=true;
                for (int i = 0; i < size(); i++)
                {
                    if(!children.get(i).equals(triangle.children.get(i)))
                    {
                        equal=false;
                        break;
                    }
                }
                return equal;
            }
            return false;
        }
    }
    public List<Triangle> annotateSentence()
    {
        List<Triangle> list=new ArrayList<>();
        Tree tree=this.document.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(TreeCoreAnnotations.TreeAnnotation.class);
        getTreeTriagle(tree,list);
        return list;
    }

    /**
     * 递归的往这个list里面插入三元组或者二元组
     * @param tree
     * @param treeList
     */
    private void getTreeTriagle(Tree tree,List<Triangle> treeList)
    {
        Triangle triangle=new Triangle();
        String label=tree.label().value();
        triangle.root=label;
        List<Tree> trees=tree.getChildrenAsList();
        trees.forEach(x->{
            triangle.children.add(x.label().value());
            if(!x.isLeaf())
            {
                getTreeTriagle(x, treeList);
            }
        });
        treeList.add(triangle);
    }
    public static void main(String[] args)
    {
        Parsing parsing=new Parsing("we are from the same country");
        parsing.annotateSentence();
    }
}
