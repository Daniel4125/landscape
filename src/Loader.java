import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class Loader {
   // list of the relevent vao and vbo IDs
   private List<Integer> vaos = new ArrayList<Integer>();
   private List<Integer> vbos = new ArrayList<Integer>();

   public RawModel loadToVAO(float[] positions) {
      int vaoID = creatVAO();
      storeDataInAttributeList(0,positions);
      unbindVAO();
      return new RawModel(vaoID, positions.length/3);
   }

   public void cleanUp(){
      for(int vao:vaos){
         GL30.glDeleteVertexArrays(vao);
      }
      for(int vbo:vbos){
         GL15.glDeleteBuffers(vbo);
      }
   }

   private int creatVAO(){
      int vaoID = GL30.glGenVertexArrays(); // empty vao creation
      vaos.add(vaoID);
      GL30.glBindVertexArray(vaoID); // activate vao
      return vaoID;
   }

   private void storeDataInAttributeList(int attributeNumber, float[] data){
      int vboID = GL15.glGenBuffers(); // vertex buffer objects inside vao
      vbos.add(vboID);
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // bind so data can be stored into it
      FloatBuffer buffer = storeDataInFloatBuffer(data);
      GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
      // size=3 as 3d vectors are being stored
      GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0, 0);
      GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
   }

   private void unbindVAO() {
      GL30.glBindVertexArray(0);
   }

   private FloatBuffer storeDataInFloatBuffer(float[] data){
      FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
      buffer.put(data);
      buffer.flip();
      return buffer;
   }
}
