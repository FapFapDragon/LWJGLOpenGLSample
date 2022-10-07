
//File Stuff
import java.io.IOException;
import java.lang.RuntimeException;
import java.nio.file.Files;
import java.nio.file.Path;

//OpenGL Stuff
import static org.lwjgl.opengl.GL31C.*;
public class Shader {

    private int program;

    public static Shader default_program;

    private String loadShaderText(String path) throws IOException
    {
        String str;
        try
        {
            Path fileName = Path.of(path);
            str = Files.readString(fileName);
        }
        catch (IOException e)
        {
            System.err.println("Unable to open shader file");
            throw new IOException("Couldn't open file: "+path);
        }

        return str;
    }

    public boolean compileShader(int shader, String shader_text)
    {
        //glCreateProgram();
        glShaderSource(shader, shader_text);
        glCompileShader(shader);

        int result = glGetShaderi(shader, GL_COMPILE_STATUS);

        if (result == 0)
        {
            String info = glGetShaderInfoLog(shader);
            System.err.println(info);
            return false;
        }

        return true;
    }

    private void buildProgram(int VertShaderID, int FragShaderID)
    {
        this.program = glCreateProgram();
        glAttachShader(this.program, VertShaderID);
        glAttachShader(this.program, FragShaderID);
        glLinkProgram(this.program);

        int result = glGetProgrami(this.program, GL_LINK_STATUS);
        if (result == 0)
        {
            String info = glGetProgramInfoLog(this.program);
            throw new RuntimeException(info);
        }
        glDetachShader(this.program, VertShaderID);
        glDetachShader(this.program, FragShaderID);

        glDeleteShader(VertShaderID);
        glDeleteShader(FragShaderID);

    }

    public int getProgram()
    {
        return this.program;
    }

    public Shader(String vertex_path,String fragment_path) throws IOException, RuntimeException
    {
        int VertShaderID = glCreateShader(GL_VERTEX_SHADER);
        int FragShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        String vert = loadShaderText(vertex_path);
        String frag = loadShaderText(fragment_path);

        compileShader(VertShaderID ,vert);
        compileShader(FragShaderID, frag);

        buildProgram(VertShaderID, FragShaderID);
    }

    public Shader(String vertex_path,String fragment_path, boolean default_shader) throws IOException, RuntimeException
    {
        int VertShaderID = glCreateShader(GL_VERTEX_SHADER);
        int FragShaderID = glCreateShader(GL_FRAGMENT_SHADER);

        String vert = loadShaderText(vertex_path);
        String frag = loadShaderText(fragment_path);

        if (!compileShader(VertShaderID ,vert))
        {
            throw new RuntimeException("Unable to compile vertex shader");
        }
        if (!compileShader(FragShaderID ,frag))
        {
            throw new RuntimeException("Unable to compile fragment shader");
        }

        buildProgram(VertShaderID, FragShaderID);

        if (default_shader)
        {
            Shader.default_program = this;
        }
    }
}
