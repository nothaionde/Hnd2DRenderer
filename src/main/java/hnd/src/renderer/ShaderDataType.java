package hnd.src.renderer;

/**
 * An enum representing different types of data that can be used in shaders.
 * This includes floats, vectors of different sizes, matrices, integers, and booleans.
 */
public enum ShaderDataType {
    None, // No data type
    Float, // Float data type
    Float2, // Vector of two floats
    Float3, // Vector of three floats
    Float4, // Vector of four floats
    Mat3, // 3x3 matrix of floats
    Mat4, // 4x4 matrix of floats
    Int, // Integer data type
    Int2, // Vector of two integers
    Int3, // Vector of three integers
    Int4, // Vector of four integers
    Boolean // Boolean data type
}