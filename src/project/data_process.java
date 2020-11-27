package project;
import java.io.*;
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
    public static void main(String args[]){
        data_process k = new data_process();
        StringBuffer file = k.rawString(INPUT_PATH);
        System.out.println("Contents of File: ");
        System.out.println(file.toString());
    }
}
