package encryptdecrypt;

import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
public class Main {
    public static void main(String[] args) throws Exception{

        boolean isIn = false;
        boolean isData = false;
        boolean isOut = false;
        String inFile = "";
        String outFile = "";
        String decEnc = "enc";
        String alg = "shift";
        String msg = "";
        int key = 0;

        for(int i = 0,j=1;i<args.length;i+=2,j+=2){
            if(args[i].equals("-mode")){
                decEnc = args[j];
            }
            if(args[i].equals("-data")){
                msg = args[j];
                isData = true;
            }
            if(args[i].equals("-key")){
                key = Integer.parseInt(args[j]);
            }
            if (args[i].equals("-in")) {
                inFile = args[j];
                isIn = true;
            }
            if (args[i].equals("-out")) {
                outFile = args[j];
                isOut = true;
            }
            if (args[i].equals("-alg")) {
                alg = args[j];
            }
        }
        if (alg.equals("unicode")) {
            if (isOut == true) {
                if (isData == true){
                    FileHandler.writeFileContent(outFile,UnicodeEncDecHandler.resultEncDec(decEnc, msg, key));
                    //System.out.print(resultEncDec(decEnc, msg, key));
                }else if(isData == false && isIn == true && !new File(inFile).isDirectory()){
                    try {
                        FileHandler.writeFileContent(outFile, UnicodeEncDecHandler.resultEncDec(decEnc, FileHandler.readFileContent(inFile), key));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    FileHandler.writeFileContent(outFile, UnicodeEncDecHandler.resultEncDec(decEnc, msg, key));;
                }
            }else{
                if (isData == true){
                    System.out.print(UnicodeEncDecHandler.resultEncDec(decEnc, msg, key));
                }else if(isData == false && isIn == true && !new File(inFile).isDirectory()){
                    try {
                        System.out.print(UnicodeEncDecHandler.resultEncDec(decEnc, FileHandler.readFileContent(inFile), key));
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }else {
                    System.out.print(UnicodeEncDecHandler.resultEncDec(decEnc, msg, key));
                }
            }
        } else {
            if (isOut == true) {
                if (isData == true){
                    FileHandler.writeFileContent(outFile,ShiftEncDecHandler.resultEncDec(decEnc,msg,key));
                    //System.out.print(resultEncDec(decEnc, msg, key));
                }else if(isData == false && isIn == true && !new File(inFile).isDirectory()){
                    try {
                        FileHandler.writeFileContent(outFile, ShiftEncDecHandler.resultEncDec(decEnc,FileHandler.readFileContent(inFile),key));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    FileHandler.writeFileContent(outFile, ShiftEncDecHandler.resultEncDec(decEnc,msg,key));;
                }
            }else{
                if (isData == true){
                    System.out.print(ShiftEncDecHandler.resultEncDec(decEnc, msg, key));
                }else if(isData == false && isIn == true && !new File(inFile).isDirectory()){
                    try {
                        System.out.print(ShiftEncDecHandler.resultEncDec(decEnc,FileHandler.readFileContent(inFile),key));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else {
                    System.out.print(ShiftEncDecHandler.resultEncDec(decEnc, msg, key));
                }
            }
        }
    }
}
/**
 * UnicodeEncDecHandler
 */
class UnicodeEncDecHandler {
    protected static String resultEncDec(String decEnc,String msg,int key){

        if (decEnc.equals("enc")) {
            return encrypt(msg, key);
        } else {
            return decrypt(msg, key);
        }

    }

    protected static String encrypt(String plainText,int key){

        String cipherText = "";
        char[] myMsg = plainText.toCharArray();
        for (char c : myMsg) {
            char ca = c;
            ca += key;
            cipherText = cipherText + ca;
        }
        return cipherText;

    }
    protected static String decrypt(String cipherText,int key){

        String plainMsg = "";
        char[] myMsg = cipherText.toCharArray();
        for (char c : myMsg) {
            char ca = c;
            ca -= key;
            plainMsg = plainMsg + ca;
        }
        return plainMsg;

    }
}
/**
 * ShiftEncDecHandler
 */
class ShiftEncDecHandler {
    public static String resultEncDec(String decEnc,String msg, int key) throws Exception{
        if (decEnc.equals("enc")) {
            return createCipher(msg, key);
        } else {
            return createPlainText(msg, key);
        }
    }

    public  static String createCipher(String plainMessage, int key) throws Exception {
        String cipherText="";
        char a = 'a';
        char z = 'z';
        char aa = 'A';
        char zz = 'Z';
        int size  = 26;

        for (char item : plainMessage.toCharArray()) {
            if (item >= a && item <= z) {
                char shiftItem = (char) (((item - a + key) % size) + a);
                cipherText+=shiftItem;
            } else if(item >= aa && item <= zz){
                char shiftItem = (char) (((item - aa + key) % size) + aa);
                cipherText+=shiftItem;
            }else{
                cipherText+=item;
            }
        }
        return cipherText;
    }
    public  static String createPlainText(String plainMessage, int key) throws Exception {
        String cipherText="";
        char a = 'a';
        char z = 'z';
        char aa = 'A';
        char zz = 'Z';
        int size  = 26;

        for (char item : plainMessage.toCharArray()) {
            if (item >= a && item <= z) {
                if (item - key - a < 0) {
                    char shiftItem = (char) (item + 26 -key);
                    cipherText+=shiftItem;
                } else {
                    char shiftItem = (char) (((item - a - key)%size)  + a);
                    cipherText+=shiftItem;
                }
            } else if(item >= aa && item <= zz){
                if (item - key - aa < 0) {
                    char shiftItem = (char) (item + 26 -key);
                    cipherText+=shiftItem;
                } else {
                    char shiftItem = (char) (((item - aa - key)%size)  + aa);
                    cipherText+=shiftItem;
                }
            }else{
                cipherText+=item;
            }
        }
        return cipherText;
    }
}
/**
 * FileHandler
 */
class FileHandler {

    protected static String readFileContent(String filePath){
        //return new String(Files.readAllBytes(Paths.get(filePath)));
        File file = new File(filePath);
        String content = "";
        try (Scanner scanner = new Scanner(file)){
            while (scanner.hasNextLine()) {

                content += scanner.nextLine();
            }
            content = content.trim();
        } catch (Exception e) {
            //TODO: handle exception
            e.printStackTrace();
        }
        return content;
    }
    protected static void writeFileContent(String fileName, String content){
        try (PrintWriter printWriter = new PrintWriter(fileName)){
            printWriter.write(content);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}