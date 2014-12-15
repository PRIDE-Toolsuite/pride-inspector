package uk.ac.ebi.pride.toolsuite.gui.component.proteingroup;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.RoundRectangle2D;
import java.util.Collection;

import org.apache.commons.collections15.Transformer;

import uk.ac.ebi.pride.utilities.pia.intermediate.IntermediateGroup;
import uk.ac.ebi.pride.utilities.pia.intermediate.IntermediatePeptide;
import uk.ac.ebi.pride.utilities.pia.intermediate.IntermediatePeptideSpectrumMatch;
import uk.ac.ebi.pride.utilities.pia.intermediate.IntermediateProtein;
import edu.uci.ics.jung.visualization.RenderContext;


/**
 * Labeller and transformer for the group visualization
 * 
 * @author julianu
 *
 */
public class ProteinVertexShapeTransformer
        implements Transformer<VertexObject, Shape> {
    
    private RenderContext<VertexObject, String> rc;
    private int margin;
    
    
    /**
     * Constructor
     * 
     * @param rc
     * @param margin
     */
    public ProteinVertexShapeTransformer(RenderContext<VertexObject, String> rc, int margin) {
        this.rc = rc;
        this.margin = margin;
    }
    
    
    /**
     * Prepares the rendering and returns an component, to w.g. calculate the
     * preferred component size.
     * 
     * @param vertex
     * @return
     */
    public Component prepareRenderer(VertexObject vertex) {
        return rc.getVertexLabelRenderer().<VertexObject>getVertexLabelRendererComponent(
                rc.getScreenDevice(),
                rc.getVertexLabelTransformer().transform(vertex),
                rc.getVertexFontTransformer().transform(vertex),
                rc.getPickedVertexState().isPicked(vertex),
                vertex);
    }
    
    
    @Override
    public Shape transform(VertexObject vertex) {
        Object vObject = vertex.getObject();
        Shape shape = null;
        
        if (vObject instanceof Collection<?>) {
            vObject = ((Collection<?>)vertex.getObject()).iterator().next();
        }
        
        if (vObject instanceof IntermediateGroup) {
            shape = new Ellipse2D.Float(-10, -10, 20, 20);
        } else {
            Component component = prepareRenderer(vertex);
            
            Dimension size = new Dimension(
                    (int)component.getPreferredSize().getWidth() + margin*2,
                    (int)component.getPreferredSize().getHeight() + margin*2);
            
            if (vObject instanceof IntermediatePeptide) {
                shape = new RoundRectangle2D.Float(-size.width/2, -size.height/2, size.width, size.height, 6, 6);
            } else if (vObject instanceof IntermediateProtein) {
                shape = new Rectangle(-size.width/2 -2, -size.height/2 -2, size.width+4, size.height);
            } else if (vObject instanceof IntermediatePeptideSpectrumMatch) {
                shape = new RoundRectangle2D.Float(-size.width/2, -size.height/2, size.width, size.height, size.height/2, size.height/2);
            }
        }
        
        return shape;
    }
}
