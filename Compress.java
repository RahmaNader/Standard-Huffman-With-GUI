import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;


public class Compress {
    int [] fr;
    int n=0;
    Vector <Integer> freq = new Vector<>();
    Vector <Character> chars = new Vector<>();
    static ArrayList<MyChar> compressedCode = new ArrayList<>();
    String str;

    //to reverse the change of priority queue
    static class condition implements Comparator<Node> {
        public int compare(Node x, Node y)
        {
            return x.freq - y.freq;
        }
    }

    //calculating the frequency
    public String freq(String data) throws IOException {
        str = data;
        fr = new int [data.length()];
        String myCode;
        int visited = -1;
        for(int i = 0; i < data.length(); i++){
            int count = 1;
            for(int j = i+1; j < data.length(); j++){
                if(data.charAt(i) == data.charAt(j)){
                    count++;
                    fr[j] = visited;
                }
            }
            if(fr[i] != visited)
                fr[i] = count;
        }
        //??????
        if (Files.exists(Path.of("compressedFile.txt"))){
            BufferedWriter myWriter = Files.newBufferedWriter(Path.of("compressedFile.txt")) ;
            myWriter.write("");
            myWriter.flush();
        }

        //to have a vector of the different symbols and their frequency
        for(int i = 0; i < fr.length; i++){
            if(fr[i] != visited) {
                n++;
                freq.add(fr[i]);
                chars.add(data.charAt(i));
            }
        }

        //now that everything is ready call the compress function
        this.compress();

        //?????
        BufferedReader myReader = new BufferedReader(new FileReader("compressedFile.txt"));
        String line;
        ArrayList<String> lines = new ArrayList<>();
        while ((line = myReader.readLine()) != null){
            lines.add(line);
        }
        myCode = lines.get(lines.size()-1);
        return myCode;
    }

    //generating the code of each character
    public static void printCode(Node root,String s) {
        if(root==null) return;
        try{
            if (root.childl == null && root.childr == null) {
                root.code=s;
                //writing the code of each symbol in a new line in the compressed
                //file to be used as dictionary
                BufferedWriter myWriter = new BufferedWriter(new FileWriter("compressedFile.txt",true)) ;
                myWriter.append(root.symbol + s);
                myWriter.newLine();
                myWriter.close();
                //??????????
                MyChar temp = new MyChar();
                temp.setMyChar(root.symbol);
                temp.setCharCode(s);
                compressedCode.add(temp);
            }
            else {
                printCode(root.childl, s + "0");
                printCode(root.childr, s + "1");
            }
        } catch (NullPointerException | IOException e){
            System.out.println(e.getMessage());
        }
    }

    //actual compression function
    public void compress() throws IOException {
        PriorityQueue<Node> q = new PriorityQueue<Node>(n, new condition());

        //creating and adding the nodes to the queue
        for (int i = 0; i < n; i++) {
            //??????
            if(chars.get(i).equals('\r')){
                continue;
            }
            Node node = new Node();
            node.symbol = chars.get(i);
            node.freq = freq.get(i);
            node.childl = null;
            node.childr = null;
            q.add(node);
        }

        //creating a node for the root
        Node root=new Node();

        if(q.size() == 1){
            root = q.peek();
            q.poll();
            printCode(root, "0");
            writeCode();
            return;
        }

        while (q.size() > 1) {
            Node l = q.peek();
            q.poll();
            Node r = q.peek();
            q.poll();
            Node father = new Node();
            //بجمع كل اتنين نود في نود واحدة
            father.freq = l.freq + r.freq;
            father.symbol = '-';
            father.childl = l;
            father.childr = r;
            root = father;
            q.add(root);
        }
        compressedCode.clear();
        printCode(root, "");
        writeCode();
    }

    private void writeCode() throws IOException {
        BufferedWriter myWriter = new BufferedWriter(new FileWriter("compressedFile.txt",true)) ;
        for(int i=0; i<str.length(); i++){
            for (MyChar myChar : compressedCode) {
                if (str.charAt(i) == myChar.getMyChar()) {
                    myWriter.append(String.valueOf(myChar.getCharCode()));
                }
            }
        }
        myWriter.close();
    }

    //take input from text box
    public String compress(String s) throws IOException {
        return freq(s);
    }
    //take input from file chooser
    public String compress(Path p) throws IOException {
        String myData;
        myData = Files.readString(p);
        return freq(myData);
    }
}

