import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Decompress {
    BufferedReader myReader;
    public String decompress(File file){
        try {
            myReader = new BufferedReader(new FileReader(file));
            BufferedWriter myWriter = new BufferedWriter(new FileWriter("decompressedFile.txt"));
            List<String> lines = new ArrayList<>();
            List<String> codeTable;
            List<MyChar> charTable = new ArrayList<>();
            String compressedText;
            StringBuilder decompressedText = new StringBuilder();
            String line;
            String decompressedTemp = "";
            while ((line = myReader.readLine()) != null){
                lines.add(line);
            }
            compressedText = lines.get(lines.size()-1);
            codeTable = lines.subList(0, lines.size()-1);
            if(compressedText.equals("") || codeTable.isEmpty()){
                System.out.println("\nPlease choose a correct file");
                return null;
            }
            for (int i = 0; i < codeTable.size(); i++) {
                MyChar temp = new MyChar();
                String s = codeTable.get(i);
                if(s.isEmpty()){
                    i++;
                    temp.setMyChar('\n');
                    temp.setCharCode(codeTable.get(i));
                    charTable.add(temp);
                    continue;
                }
                temp.setMyChar(s.charAt(0));
                temp.setCharCode(s.substring(1));
                charTable.add(temp);
            }

            for (int i = 0; i < compressedText.length(); i++){
                decompressedTemp += compressedText.charAt(i);
                for (MyChar myChar : charTable) {
                    if (myChar.getCharCode().equals(decompressedTemp)) {
                        decompressedText.append(myChar.getMyChar());
                        decompressedTemp = "";
                        break;
                    }
                }
            }

            myWriter.append(decompressedText.toString());
            myWriter.close();
            return decompressedText.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //take input from text box
    public String decompress(String s) throws IOException {
        File tempFile = File.createTempFile("tempFile", ".tmp");
        BufferedWriter myWriter = new BufferedWriter(new FileWriter(tempFile));
        for(int i = 0; i < s.length(); i++){
            if(s.charAt(i) == '\n'){
                myWriter.newLine();
            }else{
                myWriter.append(s.charAt(i));
            }
        }
        myWriter.close();
        String temp = decompress(tempFile);
        tempFile.deleteOnExit();
        return temp;
    }
    //take input from file chooser
    public String decompress(Path p){
        String temp = decompress(p.toFile());
        return temp;
    }
}
