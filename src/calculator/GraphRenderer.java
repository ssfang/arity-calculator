// Copyright (C) 2009 Mihai Preda

package calculator;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.opengl.GLSurfaceView;
import android.opengl.Matrix;

class GraphRenderer implements GLSurfaceView.Renderer {
    Graph3d graph;
    private float angleX, angleY = 15;
    private long lastTime;
    private int drawCnt = 1;
    private float[] matrix1 = new float[16], matrix2 = new float[16], matrix3 = new float[16];

    public GraphRenderer(Graph3d graph) {
        this.graph = graph;
        Matrix.setIdentityM(matrix1, 0);
        Matrix.rotateM(matrix1, 0, -75, 1, 0, 0);
    }

    public void incAngle(float dx, float dy) {
        angleX = dx;
        angleY = dy;
    }

    public void onDrawFrame(GL10 gl10) {
        GL11 gl = (GL11) gl10;
        // Calculator.log("ext: " + gl.glGetString(GL11.GL_EXTENSIONS) + "; " + gl.glGetString(GL11.GL_VERSION));

        /*
        if (--drawCnt == 0) {
            drawCnt = 30;
            long now = System.currentTimeMillis();
            Calculator.log("time " + ((now - lastTime) / 3));
            lastTime = now;
        }
        */

        // gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);        
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
        gl.glTranslatef(0, 0, -(NEAR+6.5f));

        Matrix.setIdentityM(matrix2, 0);
        if (angleY < .1f) {
            Matrix.rotateM(matrix2, 0, angleX, 0, 1, 0);
            Matrix.rotateM(matrix2, 0, angleY, 1, 0, 0);
        } else if (angleX < .1f) {
            Matrix.rotateM(matrix2, 0, angleY, 1, 0, 0);
            Matrix.rotateM(matrix2, 0, angleX, 0, 1, 0);
        } else {
            float halfX = angleX/2;
            Matrix.rotateM(matrix2, 0, halfX, 0, 1, 0);
            Matrix.rotateM(matrix2, 0, angleY, 1, 0, 0);
            Matrix.rotateM(matrix2, 0, halfX, 0, 1, 0);
        }
        Matrix.multiplyMM(matrix3, 0, matrix2, 0, matrix1, 0);
        gl.glMultMatrixf(matrix3, 0);
        
        System.arraycopy(matrix3, 0, matrix1, 0, 16);
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);        
        graph.draw(gl);
    }

    private static final int NEAR = 15;
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Calculator.log("onSurfaceChanged");
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float h = NEAR/5f * height / (float) width;
        gl.glFrustumf(-NEAR/5f, NEAR/5f, -h, h, NEAR, NEAR+12);
        // gl.glOrthof(-NEAR, NEAR, -h, h, NEAR, NEAR+12);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }


    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Calculator.log("onSurfaceCreated");
        gl.glDisable(GL10.GL_DITHER);
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);               
        gl.glClearColor(0, 0, 0, 1);
        gl.glShadeModel(GL10.GL_SMOOTH);
        
        /*
        gl.glEnable(GL10.GL_LINE_SMOOTH);
        gl.glHint (GL10.GL_LINE_SMOOTH_HINT, GL10.GL_NICEST);
        gl.glEnable(GL10.GL_BLEND);
        gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
        gl.glLineWidth(1.5f);
        */
        

        /*
        gl.glEnable(GL10.GL_CULL_FACE);
        gl.glDisable(GL10.GL_DEPTH_TEST);
        gl.glEnable(GL10.GL_DEPTH_TEST);

        */

        /*
        gl.glEnable(GL10.GL_LIGHTING);
        gl.glEnable(GL10.GL_LIGHT0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_AMBIENT, new float[] {.2f, .2f, .2f, 1}, 0);
        gl.glLightfv(GL10.GL_LIGHT0, GL10.GL_DIFFUSE, new float[] {1f, 1f, 1f, 1}, 0);

        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_AMBIENT, new float[] {1, 1, 1, 1}, 0);
        gl.glMaterialfv(GL10.GL_FRONT_AND_BACK, GL10.GL_DIFFUSE, new float[] {1, 1, 1, 1}, 0);
        gl.glLightModelx(GL10.GL_LIGHT_MODEL_TWO_SIDE, GL10.GL_FALSE);
        gl.glLightModelfv(GL10.GL_LIGHT_MODEL_AMBIENT, new float[]{.2f, 0, 0, 1f}, 0);
        */
    }
}
