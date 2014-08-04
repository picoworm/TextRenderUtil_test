import java.awt.Color;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.media.opengl.GL2;
import javax.media.opengl.GL2ES2;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLCapabilities;
import javax.media.opengl.GLEventListener;
import javax.media.opengl.GLProfile;
import javax.media.opengl.awt.GLCanvas;
import javax.media.opengl.fixedfunc.GLMatrixFunc;
import javax.swing.Timer;

import com.jogamp.graph.curve.opengl.RegionRenderer;
import com.jogamp.graph.curve.opengl.RenderState;
import com.jogamp.graph.curve.opengl.TextRegionUtil;
import com.jogamp.graph.font.Font;
import com.jogamp.graph.font.Font.Glyph;
import com.jogamp.graph.font.FontFactory;
import com.jogamp.graph.geom.SVertex;
import com.jogamp.opengl.util.PMVMatrix;


public class gltest {

	static RegionRenderer renderer;
	static Font TR_Font;
    static RenderState rs;
    static TextRegionUtil TRU;
    static PMVMatrix pmv;
    static Font font;
    
    static int Size = 10;
    
    static float StringWidth(Font f, CharSequence str, float pixelSize) {
    	final int len=str.length();
    	float TotalWidth=0;
    	for (int i=0; i<len; i++) {
    		final char c = str.charAt(i);
    		if (c==' ') TotalWidth+=f.getAdvanceWidth(Glyph.ID_SPACE, pixelSize);
    		else TotalWidth+=f.getGlyph(c).getAdvance(pixelSize, true);
    	}
    	return TotalWidth;
    }
    
	public static void main(String[] args) {
        Frame frame = new Frame("Hello World!");

        GLProfile glprofile = GLProfile.getDefault();
        GLCapabilities glcapabilities = new GLCapabilities( glprofile );
//        glcapabilities.setSampleBuffers(true);
//        glcapabilities.setNumSamples(2);
        final GLCanvas glcanvas = new GLCanvas( glcapabilities );
        frame.add(glcanvas);
 
        ActionListener AL = new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				 Size++;
				 glcanvas.display();
			}
		}; 
		
		new Timer(100, AL).start();
        
        glcanvas.addGLEventListener( new GLEventListener() {

            @Override
            public void dispose( GLAutoDrawable glautodrawable ) {
            }

            @Override
            public void init(GLAutoDrawable drawable) {
    	        pmv = new PMVMatrix();
    			rs = RenderState.createRenderState(SVertex.factory(), pmv);
    			renderer = RegionRenderer.create(rs, RegionRenderer.defaultBlendEnable, RegionRenderer.defaultBlendDisable);
    			int rendflags = 0;//Region.VBAA_RENDERING_BIT;
    			TRU = new TextRegionUtil(rendflags);
    	        rs.setHintMask(RenderState.BITHINT_GLOBAL_DEPTH_TEST_ENABLED);
    	        
                final GL2ES2 gl2e = drawable.getGL().getGL2ES2();
                gl2e.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

                renderer.init(gl2e, rendflags);
                rs.setColorStatic(1, 0, 0, 1);
                renderer.enable(gl2e, false);

    			try {
					font = FontFactory.get(new File("arialbd.ttf"));
				} catch (IOException e) {
					e.printStackTrace();
				}
    			
    			GL2 gl = drawable.getGL().getGL2();

                gl.glShadeModel(GL2.GL_SMOOTH);
                gl.glDisable(GL2.GL_DEPTH_TEST);
                gl.glDepthFunc(GL2.GL_LEQUAL);
                gl.glClearColor(0, 0, 0, 1);
                gl.glClearDepth(100.0);
            }
            @Override
            public void reshape( GLAutoDrawable glautodrawable, int x, int y, int width, int height ) {
//                teapot.render(glautodrawable.getGL().getGL2());
                final GL2ES2 gl = glautodrawable.getGL().getGL2ES2();

                System.out.println("reshape");
                
                gl.getGL2().glMatrixMode(GL2.GL_PROJECTION);
                gl.getGL2().glLoadIdentity();
                gl.glViewport(0, 0, width, height);        

                pmv.glLoadIdentity();

                renderer.enable(gl, true);
                renderer.reshapeNotify(width, height);
                renderer.enable(gl, false);
                
                gl.getGL2().glOrtho(0, width, height, 0, -1, 1);
//                pmv.glMatrixMode(GLMatrixFunc.GL_PROJECTION); pmv.glLoadIdentity();
                pmv.glOrthof(0, width, height, 0, -1, 1);
//                pmv.glMatrixMode(GLMatrixFunc.GL_MODELVIEW); pmv.glLoadIdentity();
            }

            @Override
            public void display(GLAutoDrawable drawable) {
            	
                GL2 gl = drawable.getGL().getGL2();
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

//                gl.glMatrixMode(GL2.GL_MODELVIEW);
//                gl.glLoadIdentity();

                int X=10, Y=10;
                int W = 600, H=200; 
                // Draw opengl rect
                gl.glTranslatef(0, 0, 0);
                gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);
                gl.glDisable(GL2.GL_LINE_SMOOTH);
                gl.glColor3f(0, 0, 1);
                gl.glBegin(GL2.GL_LINE_LOOP);        
                gl.glVertex3f(X, Y, 0);       
                gl.glVertex3f(X+W, Y, 0);      
                gl.glVertex3f(X+W, Y+H, 0);      
                gl.glVertex3f(X, Y+H, 0);      
                gl.glEnd();
                
                final GL2ES2 gl2e = drawable.getGL().getGL2ES2();
                                
                String text="QQ_oo_qq_OO";
                int fontSize = Size;
                //AABBox box = font.getMetricBounds(text, fontSize);
                System.out.println(font.getGlyph(' ').getAdvance(fontSize, true));
                System.out.println(font.getAdvanceWidth(Glyph.ID_SPACE, fontSize));
                
                float TextWidth = StringWidth(font, text, fontSize); 
                float A_Descent = font.getGlyph('A').getBBox().getHeight() * font.getGlyph('A').getScale(fontSize);
                
                // Render the text
                //final float[] textPosition = new float[] {0,0,0};
                final int[] texSize = new int[] { 4 }; 
                final PMVMatrix pmv = renderer.getMatrix();
                pmv.glMatrixMode(GLMatrixFunc.GL_MODELVIEW);
                pmv.glPushMatrix();
                // Place text in the center of the box
                pmv.glTranslatef(X+(W/2) - (TextWidth/2), Y+(H/2)+(A_Descent/2), 0);
//                System.out.println(box);
                //pmv.glTranslatef(0, 30, 0);
                pmv.glScalef(1, -1, 1);
                renderer.enable(gl, true);
                TRU.drawString3D(gl2e, renderer, font, fontSize, text, null, texSize);
                renderer.enable(gl, false);

                pmv.glPopMatrix();
                
                gl.glFlush();
            }
        });
        
        frame.setSize(640, 480);
        frame.setBackground(Color.white);

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        frame.setVisible(true);
    }
}
