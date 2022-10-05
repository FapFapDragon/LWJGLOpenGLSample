import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.ARBVertexBufferObject;

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
        return;
    }

}
