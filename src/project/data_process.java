// Student Name: Phan Tan Dat
// Student ID: 18127078
package project;
import java.io.*;
import java.util.*;
/*
1. Chức năng tìm kiếm theo slang word.
2. Chức năng tìm kiếm theo definition, hiển thị ra tất cả các slang words mà trong
defintion có chứa keyword gõ vào.
3. Chức năng hiển thị history, danh sách các slang word đã tìm kiếm.
4. Chức năng add 1 slang words mới. Nếu slang words trùng thì thông báo cho người
dùng, confirm có overwrite hay duplicate ra 1 slang word mới.
5. Chức năng edit 1 slang word.
6. Chức năng delete 1 slang word. Confirm trước khi xoá.
7. Chức năng reset danh sách slang words gốc.
8. Chức năng random 1 slang word (On this day slang word).
9. Chức năng đố vui, chương trình hiển thị 1 random slang word, với 4 đáp án cho
người dùng chọn.
10. Chức năng đố vui, chương trình hiển thị 1 definition, với 4 slang words đáp án cho
người dùng chọn.
 */
public class data_process {
    public static final String INPUT_PATH = "input/slang.txt";
    public TreeMap<String, LinkedList<String>> data;

    private void readfile(String dir){
        this.data = new TreeMap<String, LinkedList<String>>();

        try {
            File file = new File(dir);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line_info;
            while ((line_info = br.readLine()) != null){
                String[] content = line_info.split("`"); // Parse a line to <Slang> and <Definitions>
                if (content.length != 2)
                    continue;
                String[] syns = content[1].split("\\|"); // Parse <Definitions> if there are many of it
                LinkedList<String> li = new LinkedList<String>(Arrays.asList(syns)); // Append processed data to treemap
                (this.data).put(content[0], li);
            }
            fr.close();

        }
        catch (IOException err){
            err.printStackTrace();
        }
    }
    public void printdata(){
        Set<Map.Entry<String, LinkedList<String>>> entries = this.data.entrySet();
        for(Map.Entry<String, LinkedList<String>> entry : entries){
            System.out.println( entry.getKey() + "\t->\t" + entry.getValue() );
        }
    }
    //1. Chức năng tìm kiếm theo slang word.
    public LinkedList<String> searchSlang(String word){
        return this.data.get(word.trim());
    }




    // ------------------------ Console UI things --------------------------
    public void Console_searchSlang(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter any word to search: ");
        String kb = scan.nextLine();
        scan.close();
        LinkedList<String> query = this.searchSlang(kb);
        if (query == null)
            System.out.println("No matches");
        else
            System.out.println("Definition: " + query.toString());
    }

    // -----------------------------------------------------------------------
    public static void main(String args[]){
        data_process k = new data_process();
        k.readfile(INPUT_PATH);
        // --------------------
        k.Console_searchSlang();
    }

}
