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
      bb.get(b);
      return b[0] == 1 ? true : false;
   }

   public Page fixedSetBool (FileMgr fm, BlockId blk, Page page, int offset, boolean value){
      if (page.pagesize - offset >= 1){
         System.out.println("Page is not full");
         page.setBool(offset, value);
         return page;
      }else{
         System.out.println("Page is full");
         fm.write(blk, page);
         blk.increment();
         Page new_page = new Page(fm.blockSize());
         new_page.setBool(offset, value);
         return new_page;
      }
   }
   public void setBool (int offset, boolean value) {
      bb.put(offset, (byte) (value ? 1: 0));
   }

   public short getShort(int offset) {
      return bb.getShort();
   }

   public Page fixedSetShort(FileMgr fm, BlockId blk, Page page, int offset, short value){
      if (page.pagesize - offset >= 2){
         System.out.println("Page is not full");
         page.setShort(offset, value);
         return page;
      }else{
         System.out.println("Page is full");
         fm.write(blk, page);
         blk.increment();
         Page new_page = new Page(fm.blockSize());
         new_page.setShort(offset, value);
         return new_page;
      }
   }
   public void setShort(int offset, short value) {
      bb.putShort(offset, value);
   }
   
   public int getInt(int offset) {
      return bb.getInt(offset);
   }
   public Page fixedSetInt(FileMgr fm, BlockId blk, Page page, int offset, int n) {
      if (page.pagesize - offset >= 4){
         System.out.println("Page is not full");
         page.setInt(offset, n);
         return page;
      }else {
         System.out.println("Page is full");
         fm.write(blk, page);
         blk.increment();
         Page new_page = new Page(fm.blockSize());
         new_page.setInt(0, n);
         return new_page;
      }
   }

   public void setInt(int offset, int n) {
      bb.putInt(offset, n);
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

   public Page fixedSetString(FileMgr fm, BlockId blk, Page page, int offset, String s) {
      if (page.pagesize - offset >= s.getBytes(CHARSET).length){
         System.out.println("Page is not full");
         page.setString(offset, s);
         return page;
      }else {
         System.out.println("Page is full");
         fm.write(blk, page);
         blk.increment();
         Page new_page = new Page(fm.blockSize());
         new_page.setString(0, s);
         return new_page;
      }
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
