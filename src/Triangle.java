import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.ARBVertexBufferObject;
import static org.lwjgl.opengl.GL20.*;
public class Triangle {

    private float vertices[] = {-1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            0.0f,  1.0f, 0.0f};

    private int vao;
    private int vbo;

    public void Triangle()
    {
        vao = ARBVertexArrayObject.glGenVertexArrays();
        vbo = ARBVertexBufferObject.glGenBuffersARB();
        ARBVertexArrayObject.glBindVertexArray(vao);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo);
        ARBVertexBufferObject.glBufferDataARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vertices, ARBVertexBufferObject.GL_STATIC_DRAW_ARB);
        return;
    }

    public void draw()
    {
        ARBVertexArrayObject.glBindVertexArray(vao);
        ARBVertexBufferObject.glBindBufferARB(ARBVertexBufferObject.GL_ARRAY_BUFFER_ARB, vbo);
        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);
        glEnableVertexAttribArray(0);
        glDrawArrays(GL_TRIANGLES, 0, 3);
        glDisableVertexAttribArray(0);
    }
}
