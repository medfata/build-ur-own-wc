package com.codechallenges.app;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.nio.charset.StandardCharsets;

public class App {

    private static Map<String, String> wcCounter(byte[] bytes){
        Map<String, String> res = new HashMap<>();
        int lineCounter = 0;
        int wordCouter = 0;
        int wordLength = 0;
        for(int i = 0; i < bytes.length; i++){
            byte newLineByte = (byte) '\n';
            if(bytes[i] == newLineByte){
                lineCounter++;
            }
            if(Character.isWhitespace(bytes[i]) == false){
                wordLength++;
                continue;
            }
            if(wordLength > 0){
                wordCouter++;
                wordLength = 0;
            }
       }
       if(wordLength > 0){
        wordCouter++;
       }
       res.put("-c", String.valueOf(bytes.length));
       res.put("-l", String.valueOf(lineCounter));
       res.put("-w", String.valueOf(wordCouter));
       res.put("-m", String.valueOf(new String(bytes, StandardCharsets.UTF_8).length()));
       return res;
    }
    
    public static void main( String[] args )
    {
        Scanner scanner = new Scanner(System.in);
        while(true){
            String line = scanner.nextLine();
            if (line.equalsIgnoreCase("exit")) {
                System.out.println("bye");
                break;
            }
            String[] commandParts = line.split(" ");
            if(commandParts[0].equals("ccwc") == false || commandParts.length > 3){
                System.out.println("Usage: ccwc <arg1> <file-path> OR ccwc <file-path>\n" +
                   "Example: ccwc -c /path/to/file.txt\n" +
                   "Example: ccwc /path/to/file.txt");
                continue;
            }
            String filePath = commandParts.length == 3 ? commandParts[2] : "";
            String arg = commandParts.length == 3 ? commandParts[1] : "";
            byte[] fileBytes = ReadLinesFromFile(filePath);
            if(fileBytes == null){
                System.err.println("there was an error reading file content !");
                continue;
            }
            Map<String, String> res = wcCounter(fileBytes);
            String wcRes = arg != null ? res.get(arg) : res.values().stream().collect(Collectors.joining(" "));
            printResult(wcRes, filePath);
            break;
        }
        scanner.close();
    }

   public static byte[] ReadLinesFromFile(String filePath) {
        byte[] res = null;
       try (FileInputStream fis = new FileInputStream(filePath);
             BufferedInputStream bis = new BufferedInputStream(fis)) {

            res = bis.readAllBytes();
        } catch (IOException e) {
            String currentDirectory = System.getProperty("user.dir");
            System.err.println("currentDirectory: "+currentDirectory);
            System.err.println("Error reading the file: " + e.getMessage());
        }
        return res;
    }

    public static void printResult(String wcRes, String filePath){
        System.err.println(wcRes.toString()+" "+filePath);
    }
}
