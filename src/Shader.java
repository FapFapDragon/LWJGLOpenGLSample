import org.lwjgl.opengl.ARBVertexArrayObject;
import org.lwjgl.opengl.ARBVertexBufferObject;

import static org.lwjgl.opengl.GL20.glCreateProgram;

public class Shader {

    private int program;

    public static Shader default_program;

    private String loadShadertext()
    {
        return "Text";
    }

    public boolean compileShader()
    {
        glCreateProgram();

        return true;
    }

    public boolean bindShader()
    {
        return true;
    }

    public Shader()
    {

    }
}
