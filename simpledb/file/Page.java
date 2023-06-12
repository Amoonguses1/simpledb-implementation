package simpledb.file;

import java.nio.ByteBuffer;
import java.nio.charset.*;

public class Page {
   private ByteBuffer bb;
   private int pagesize;
   public static Charset CHARSET = StandardCharsets.US_ASCII;

   // For creating data buffers
   public Page(int blocksize) {
      bb = ByteBuffer.allocateDirect(blocksize);
      this.pagesize = blocksize;
   }
   
   // For creating log pages
   public Page(byte[] b) {
      bb = ByteBuffer.wrap(b);
   }

   public boolean getBool (int offset) {
      byte[] b = new byte[1];
      bb.get(b, offset, 1);
      return b[0] == 1 ? true : false;
   }

   public Boolean setBool (int offset, boolean value) {
      if(bb.capacity() < offset + 1){
         return false;
      } 
      bb.put(offset, (byte) (value ? 1: 0));
      return true;
   }

   public short getShort(int offset) {
      return bb.getShort(offset);
   }

   public Boolean setShort(int offset, short value) {
      if(bb.capacity() < offset+ Short.SIZE/8){
         return false;
      }
      bb.putShort(offset, value);
      return true;
   }
   
   public int getInt(int offset) {
      return bb.getInt(offset);
   }

   public Boolean setInt(int offset, int n) {
      if (bb.capacity() < offset + Integer.SIZE/8){
         return false;
      }
      bb.putInt(offset, n);
      return true;
   }

   public byte[] getBytes(int offset) {
      bb.position(offset);
      int length = bb.getInt();
      byte[] b = new byte[length];
      bb.get(b);
      return b;
   }

   public void setBytes(int offset, byte[] b) {
      bb.position(offset);
      bb.putInt(b.length);
      bb.put(b);
   }
   
   public String getString(int offset) {
      byte[] b = getBytes(offset);
      return new String(b, CHARSET);
   }

   public Boolean setString(int offset, String s) {
      byte[] b = s.getBytes(CHARSET);
      if(bb.capacity() < offset+b.length+Integer.SIZE/8){
         return false;
      }
      setBytes(offset, b);
      return true;
   }

   public String getStringIndiviudally(int offset){
      byte delimiter = (byte)'\0';
      int i;
      for(i=offset; bb.get(i) != delimiter; i++){
      }
      byte[] b = new byte[i-offset];
      for(int j = 0; j < b.length; j++){
         b[j] = bb.get(offset+j);
      }
      return new String(b, CHARSET);
   }
   
   public Boolean setStringIndividually(int offset, String s){
      byte delimeter = '\0';
      int byteslength = maxLength(offset+s.length()+1);
      byte[] b = s.getBytes(CHARSET);
      if (byteslength >= bb.capacity()){
         return false;
      }

      for(int i = 0; i < b.length; i++){
         bb.put(offset+i, b[i]);
      }
      bb.put(offset+b.length, delimeter);
      return true;
   }

   public static int maxLength(int strlen) {
      float bytesPerChar = CHARSET.newEncoder().maxBytesPerChar();
      return Integer.BYTES + (strlen * (int)bytesPerChar);
   }

   // a package private method, needed by FileMgr
   ByteBuffer contents() {
      bb.position(0);
      return bb;
   }
}
