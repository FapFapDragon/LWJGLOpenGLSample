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

    double interpolation = 0;
    final int TICKS_PER_SECOND = 25;
    final int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
    final int MAX_FRAMESKIP = 5;


    private Matrix4f view_matrix = new Matrix4f();

    private Matrix4f perspective_matrix = new Matrix4f();

    private Matrix4f vp_matrix = new Matrix4f();

    private Input input;
    Shader default_shader;
    Triangle tri;

    Square sqr;

    /*
     init: Initialize GLFW, openGL, And any objects to be drawn
     inputs:     None
     returns:    True or false depending on whether or not everything falls apart
    */
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

        input = new Input();
        glfwSetKeyCallback(window, input);


        glfwMakeContextCurrent(window);

        GL.createCapabilities();

        String vertShaderPath = "C:\\Users\\Troy\\Documents\\GitHub\\LWJGLOpenGLSample\\src\\shaders\\vert.vsh";
        String fragShaderPath = "C:\\Users\\Troy\\Documents\\GitHub\\LWJGLOpenGLSample\\src\\shaders\\frag.fg";
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

    /*
        run: The function called to kick off the program
        inputs:     None
        returns:    None
    */
    private void run()
    {
        init();

        loop();

        glfwDestroyWindow(window);

        // Terminate GLFW and free the error callback
        glfwTerminate();
        return;
    }

    /*
        Loop: main program loop
        inputs:     None
        returns:    None
    */
    private void loop()
    {

        double next_game_tick = System.currentTimeMillis();
        int loops;

        // Set the clear color
        glClearColor(0f, 0.0f, 0.0f, 0.0f);
        // Ensure we can capture the escape key being pressed below
        glfwSetInputMode(window, GLFW_STICKY_KEYS, GL_TRUE);

        perspective_matrix.mul(view_matrix, vp_matrix);

        //Loop gets updated 60 times per second (locked to 60FPS)
        do{
            loops = 0;
            do {
                checkKeys();
                next_game_tick += SKIP_TICKS;
                loops++;
            } while (System.currentTimeMillis() > next_game_tick && loops < MAX_FRAMESKIP);

            interpolation = (System.currentTimeMillis() + SKIP_TICKS - next_game_tick / (double) SKIP_TICKS);
            drawThings();
        } // Check if the ESC key was pressed or the window was closed
        while( glfwGetKey(window, GLFW_KEY_ESCAPE ) != GLFW_PRESS && glfwWindowShouldClose(window) == false );
    }

    /*
        Draw Things: All draw calls for openGL and otherwise are processed here
        inputs:     None
        returns:    None
    */
    private void drawThings()
    {
        // Clear the screen. It's not mentioned before Tutorial 02, but it can cause flickering, so it's there nonetheless.
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

        //tri.draw(vp_matrix);
        sqr.draw(vp_matrix);
        // Swap buffers
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /*
        Check Keys: a function to check whether up down left or right arrow keys are being pressed
        inputs:     None
        returns:    None
    */
    private void checkKeys()
    {
        if (input.isKeyDown(GLFW_KEY_UP) && !input.isKeyDown(GLFW_KEY_DOWN))
        {
            System.out.println("GOgin up");
        }
        else if (input.isKeyDown(GLFW_KEY_DOWN) && !input.isKeyDown(GLFW_KEY_UP))
        {
            System.out.println("GOgin donw");
        }
        if (input.isKeyDown(GLFW_KEY_LEFT) && !input.isKeyDown(GLFW_KEY_RIGHT))
        {
            System.out.println("GOgin left");
        }
        else if (input.isKeyDown(GLFW_KEY_RIGHT)&& !input.isKeyDown(GLFW_KEY_LEFT))
        {
            System.out.println("GOgin RIGHT");
        }
    }

    public static void main(String[] args) {
        new Main().run();
    }
}