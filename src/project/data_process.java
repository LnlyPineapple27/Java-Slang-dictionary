package project;
import java.io.*;
import java.util.*;

public class data_process {
    public static final String INPUT_PATH = "input/slang.txt";
    public StringBuffer rawString(String path){
        StringBuffer sb = new StringBuffer();
        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line_info;
            while ((line_info = br.readLine()) != null){
                sb.append(line_info);
                sb.append("\n");
            }
            fr.close();

        }
        catch (IOException err){
            err.printStackTrace();
        }
        return sb;
    }
    public TreeMap<String, LinkedList<String>> readfile(String path){
        TreeMap<String, LinkedList<String>> result = new TreeMap<String, LinkedList<String>>();

        try {
            File file = new File(path);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line_info;
            while ((line_info = br.readLine()) != null){
                /*
                sb.append(line_info);
                sb.append("\n");
                */
                String[] content = line_info.split("`");
                if (content.length != 2)
                    continue;
                String[] syns = content[1].split("\\|");
                LinkedList<String> li = new LinkedList<String>(Arrays.asList(syns));
                result.put(content[0], li);
            }
            fr.close();

        }
        catch (IOException err){
            err.printStackTrace();
        }
        return result;
    }
    public void printTreemap(TreeMap<String, LinkedList<String>> data){
        Set<Map.Entry<String, LinkedList<String>>> entries = data.entrySet();
        for(Map.Entry<String, LinkedList<String>> entry : entries){
            System.out.println( entry.getKey() + "\t->\t" + entry.getValue() );
        }
    }
    public static void main(String args[]){
        data_process k = new data_process();
        /*
        StringBuffer file = k.rawString(INPUT_PATH);
        String kk = file.toString();
        System.out.println("Contents of File: ");
        System.out.println(file.charAt(0));
        //System.out.println(kk);
         */
        
        TreeMap<String, LinkedList<String>> tree = k.readfile(INPUT_PATH);
        k.printTreemap(tree);
    }

}
