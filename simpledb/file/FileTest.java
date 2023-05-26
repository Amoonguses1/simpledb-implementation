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
      Page p2 = new Page(fm.blockSize());

      // // check setInt
      // for(int i=0; i<5; i++){
      //    p1 = p1.fixedSetInt(fm, blk, p1, pos1+4*i, 60+10*i);   
      // }
      // fm.write(blk, p1);
      // blk.decrement();
      // blk.decrement();

      // // check getInt
      // for(int i=0; i<3; i++){
      //    fm.read(blk, p2);
      //    System.out.println(blk.number());
      //    for(int j=0; j<5; j++){
      //       System.out.println("offset " + pos1+4*j + " contains " + p2.getInt(p2, pos1+4*j));  
      //    }
      //    blk.increment();
      // }


      // // check setShort
      // pos1 += 6;
      // System.out.println("Check setShort");
      // for(int i=0; i<5; i++){
      //    p1 = p1.fixedSetShort(fm, blk, p1, pos1+2*i, (short)(60+10*i));   
      // }
      // fm.write(blk, p1);
      // System.out.println("Check get");
      // blk.decrement();
      // blk.decrement();

      // // check getShort
      // for(int i=0; i<3; i++){
      //    fm.read(blk, p2);
      //    System.out.println(blk.number());
      //    for(int j=0; j<5; j++){
      //       System.out.println("offset " + pos1+2*j + " contains " + p2.getShort(p2, pos1+2*j));  
      //    }
      //    blk.increment();
      // }

      // check setString
      p1 = p1.fixedSetString(fm, blk, p1, pos1, "abc");
      p1 = p1.fixedSetString(fm, blk, p1, pos1+40, "afohi");
      fm.write(blk, p1);
      blk.decrement();

      // check getString
      for(int i=0; i<2; i++){
         fm.read(blk, p2);
         System.out.println("offset " + pos1+40*i + " contains " + p2.getString(p2, pos1+40*i));
         blk.increment();
      }
   }
}