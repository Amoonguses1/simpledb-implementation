package simpledb.file;

import java.io.*;
import simpledb.server.SimpleDB;

public class FileTest {
   public static void main(String[] args) throws IOException {
      SimpleDB db = new SimpleDB("filetest", 100, 8);
      FileMgr fm = db.fileMgr();
      BlockId blk = new BlockId("testfile", 2);
      int pos1 = 88;
      Page p1 = new Page(fm.blockSize());
      p1.setString(pos1, "abcdefghijklm");
      int size = Page.maxLength("abcdefghijklm".length());
      int pos2 = pos1 + size;
      for(int i=0; i<5; i++){
         p1 = p1.fixedSetInt(fm, blk, p1, pos2+4*i, 60+10*i);   
      }
      fm.write(blk, p1);
      Page p2 = new Page(fm.blockSize());
      blk.decrement();
      blk.decrement();
      for(int i=0; i<3; i++){
         fm.read(blk, p2);
         for(int j=0; j<3; j++){
            System.out.println("offset " + pos2+4*j + " contains " + p2.getInt(pos1+4*j));  
         }
         blk.increment();
      }
   }
}