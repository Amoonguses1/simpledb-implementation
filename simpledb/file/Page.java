package simpledb.file;

import java.nio.ByteBuffer;
import java.nio.charset.*;

public class Page {
   private ByteBuffer bb;
   public static Charset CHARSET = StandardCharsets.US_ASCII;

   // For creating data buffers
   public Page(int blocksize) {
      bb = ByteBuffer.allocateDirect(blocksize);
   }
   
   // For creating log pages
   public Page(byte[] b) {
      bb = ByteBuffer.wrap(b);
   }

   public short getShort(short offset) {
      return bb.getShort();
   }

   public void setShort(int offset, short value) {
      bb.putShort(offset, value);
   }
   
   public int getInt(int offset) {
      return bb.getInt(offset);
   }

   public void setInt(int offset, int n) {
      if (bb.remaining() >= 4){
         bb.putInt(offset, n);
      }else {
         System.out.println("page size error.");
      }
      
   }

   public byte[] getBytes(int offset) {
      bb.position(offset);
      int length = bb.getInt();
      byte[] b = new byte[length];
      bb.get(b);
      return b;
   }

   public void setBytes(int offset, byte[] b) {
      if (bb.remaining() >= b.length){
         bb.position(offset);
         bb.putInt(b.length);
         bb.put(b);
      }else{
         System.out.println("page size error.");
      }
   }
   
   public String getString(int offset) {
      byte[] b = getBytes(offset);
      return new String(b, CHARSET);
   }

   public void setString(int offset, String s) {
      byte[] b = s.getBytes(CHARSET);
      setBytes(offset, b);
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
