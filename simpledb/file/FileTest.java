package simpledb.file;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import simpledb.server.SimpleDB;

public class FileTest {
   public static void main(String[] args) throws IOException {
      SimpleDB db = new SimpleDB("filetest", 100, 8);
      FileMgr fm = db.fileMgr();
      BlockId blk = new BlockId("testfile", 2);
      int pos1 = 88;
      Page p1 = new Page(fm.blockSize());
      Page p2 = new Page(fm.blockSize());

      // check setInt
      int intSize = Integer.BYTES;
      for(int i=0; i<5;i++){
         System.out.println(p1.setInt(pos1+intSize*i, i+1));
         System.out.println(pos1+intSize*i);
      }
      fm.write(blk, p1);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 2

      // check getInt
      fm.read(blk, p2);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 3
      for(int i=0; i<3; i++){
         System.out.println("offset " + (pos1+intSize*i) + " contains " + p2.getInt(pos1+intSize*i));
      }

      // check setShort
      int shortSize = Short.BYTES;
      for(short i=1; i<6;i++){
         System.out.println(p1.setShort(pos1+shortSize*i, i));
         System.out.println(pos1+shortSize*i);
      }
      fm.write(blk, p1);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 4

      // check getShort
      fm.read(blk, p2);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 5
      for(int i=1; i<6; i++){
         System.out.println("offset " + (pos1+shortSize*i) + " contains " + p2.getShort(pos1+shortSize*i));
      }

      // check setString
      System.out.println(p1.setString(pos1, "hoge"));
      System.out.println(p1.setString(pos1+"hoge".length()+1, "piy"));
      fm.write(blk, p1);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 6
      System.out.println(pos1+"hoge".length()+1);

      // check getString
      fm.read(blk, p2);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 7
      System.out.println("offset " + (pos1) + " contains " + p2.getString(pos1));
      System.out.println("offset " + (pos1+"hoge".length()+1) + " contains " + p2.getString(pos1+"hoge".length()+1));

      // check setStringIndividually
      System.out.println(p1.setStringIndividually(pos1, "hoge"));
      System.out.println(p1.setStringIndividually(pos1+"hoge".length()+1, "piy"));
      fm.write(blk, p1);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 8
      System.out.println(pos1+"hoge".length()+1);

      // check getStringIndividually
      fm.read(blk, p2);
      System.out.println("readandwriteCount:"+fm.CountReadWrite()); // expected 9
      System.out.println("offset " + (pos1) + " contains " + p2.getStringIndiviudally(pos1));
      System.out.println("offset " + (pos1+"hoge".length()+1) + " contains " + p2.getStringIndiviudally(pos1+"hoge".length()+1));
   }
}