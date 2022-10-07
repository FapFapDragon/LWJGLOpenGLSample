import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL31C.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {
    GLFWErrorCallback errorCallback;
    GLFWKeyCallback keyCallback;
    GLFWFramebufferSizeCallback fbCallback;

    long window;
    int width = 640;
    int height = 480;
    Object lock = new Object();
    boolean destroyed;

    private Matrix4f view_matrix = new Matrix4f();

    private Matrix4f perspective_matrix = new Matrix4f();

    private Matrix4f vp_matrix = new Matrix4f();
    Shader default_shader;
    Triangle tri;

    Square sqr;

    private boolean init()
    {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit())
        {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwWindowHint(GLFW_SAMPLES, 4); // 4x antialiasing
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3); // We want OpenGL 3.3
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE); // To make MacOS happy; should not be needed
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE); // We don't want the old OpenGL



        window = glfwCreateWindow(width, height, "JavaOpenGLXR", NULL, NULL);

        if (window == NULL)
        {
            throw new RuntimeException("Unable to Create window");
        }

        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if ( key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE )
                glfwSetWindowShouldClose(window, true); // We will detect this in the rendering loop
        });

        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        String vertShaderPath = "C:\\Users\\twalther\\Documents\\Programming\\LWJGLOpenGLSample-main\\src\\shaders\\vert.vsh";
        String fragShaderPath = "C:\\Users\\twalther\\Documents\\Programming\\LWJGLOpenGLSample-main\\src\\shaders\\frag.fg";
        try {
            default_shader = new Shader(vertShaderPath, fragShaderPath, true);
        }
        catch (Exception e){
            System.err.println("Unable to load shader");
        }

        tri  = new Triangle();

        sqr = new Square();

        perspective_matrix.setPerspective(45, width/height, 0.1f, 100);
        view_matrix.lookAt(new Vector3f(4, 3, 3f), new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
        perspective_matrix.mul(view_matrix, vp_matrix);
        return true;
    }

    private void run()
    {
        init();

        loop();

        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        return;
    }

    private void loop()
    {

        // Set the clear color
        glClearColor(0f, 0.0f, 0.0f, 0.0f);
        // Ensure we can capture the escape key being pressed below
        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);

        perspective_matrix.mul(view_matrix, vp_matrix);
        do{
            // Clear the screen. It's not mentioned before Tutorial 02, but it can cause flickering, so it's there nonetheless.
            glClear( GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            //tri.draw(vp_matrix);
            sqr.draw(vp_matrix);
            // Swap buffers
            glfwSwapBuffers(window);
            glfwPollEvents();

        } // Check if the ESC key was pressed or the window was closed
        while( glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS && glfwWindowShouldClose(window) == false );
    }

    public static void main(String[] args) {
        new Main().run();
    }
}