package ontologyRepresentations.greenContextOntology;

/**
 * Created by IntelliJ IDEA.
 * User: Me
 * Date: May 27, 2010
 * Time: 11:24:49 AM
 * To change this template use File | Settings | File Templates.
 */
public interface CloneableOntologyElement {
    Object clone(DatacenterProtegeFactory protegeFactory);
}
