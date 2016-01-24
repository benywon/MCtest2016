package StanfordUtils;

import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphCoreAnnotations;
import edu.stanford.nlp.semgraph.SemanticGraphEdge;
import edu.stanford.nlp.util.CoreMap;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by benywon on 1/19 0019.
 */
public class Dependency extends CoreNLPBase
{
    public class DependencyTree
    {
        public String root;
        public List<Triple> dList = new ArrayList<>();
        public int size()
        {
            return this.dList.size();
        }
    }
    public Dependency(String txt)
    {
        super(txt);
    }
    public Dependency()
    {
    }
    public class Triple
    {
        String parent;
        String children;
        String relation;

        public Triple(String parent, String children, String relation)
        {
            this.parent = parent;
            this.children = children;
            this.relation = relation;
        }
        public boolean equals(Object obj)
        {
            if(this==obj)
            {
                return true;
            }
            if(getClass()!=obj.getClass())
            {
                return false;
            }
            Triple triple2=(Triple)obj;
            boolean equal=(this.children.equals(triple2.children))&&(this.parent.equals(triple2.parent))&&(triple2.relation.equals(this.relation));
            return equal;
        }
    }
    public List annoDocument(String document)
    {
        preparetion(document);
        return annoDocument();
    }
    public List annoDocument()
    {
        List<DependencyTree> list=new ArrayList<>();
        this.document.get(CoreAnnotations.SentencesAnnotation.class).forEach(x->{
            SemanticGraph dependencies=x.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
            DependencyTree dependencyTree=new DependencyTree();
            dependencyTree.root=dependencies.getFirstRoot().word();
            dealDependency(dependencies,dependencyTree);
            list.add(dependencyTree);
        });
        return list;
    }
    public DependencyTree annoSentence(String sentence)
    {
        preparetion(sentence);
        return annoSentence();
    }
    public DependencyTree annoSentence()
    {
        SemanticGraph dependencies = this.document.get(CoreAnnotations.SentencesAnnotation.class).get(0).get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
        return getDependencyTreeFromSemanticGraph(dependencies);
    }
    /**
     * a sentence that has been annoted
     * @param sentence sen
     * @return triple
     */
    public DependencyTree annoSentence(CoreMap sentence)
    {
        SemanticGraph dependencies=sentence.get(SemanticGraphCoreAnnotations.BasicDependenciesAnnotation.class);
        return getDependencyTreeFromSemanticGraph(dependencies);
    }
    private DependencyTree getDependencyTreeFromSemanticGraph(SemanticGraph dependencies)
    {
        DependencyTree dependencyTree=new DependencyTree();
        dependencyTree.root=dependencies.getFirstRoot().word();
        dealDependency(dependencies,dependencyTree);
        return dependencyTree;
    }
    private void dealDependency(SemanticGraph dependencies,DependencyTree dependencyTree)
    {
        for(SemanticGraphEdge edge:dependencies.edgeIterable())
        {
            String head=edge.getGovernor().word();
            String child=edge.getDependent().word();
            String rel=edge.getRelation().getShortName();
            Triple triple=new Triple(head,child,rel);
            dependencyTree.dList.add(triple);
        }
    }
    private void preparetion(String txt)
    {
        annotate(txt);
    }
    public static void main(String[] args)
    {
        String sent="Dad fired up the grill and made some hamburgers";
        String doc="On Sunday, our family went for a picnic.  Mom, dad, Alice, Sissy (our dog), and I got in the van and drove to the park.  Alice and I played fetch with Sissy.  Dad fired up the grill and made some hamburgers.  Mom made some salad.  Mom asked me to spread the picnic cloth.  Sissy wanted to help me too.  Once the burgers were ready, we sat down to eat.   They were delicious, I ate two!  Alice only ate one hamburger.  I didn't like the salad because I don't like carrots.  Mom says they're good for me.  Sissy doesn't like carrots either.  After lunch, I played with dad and Alice played with mom and Sissy.  Dad and I were flying a kite.  Alice and mom played with flowers.  Then we packed up the van and went home.  I fell asleep on the way home.  Sunday was a lot of fun.  I want to do it again!";
        Dependency Dependency =new Dependency();
        DependencyTree cc= Dependency.annoSentence(sent);
        System.out.println(cc);
        List docl= Dependency.annoDocument(doc);
        System.out.println(docl);
    }
}
