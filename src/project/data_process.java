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
    public static final String SEARCH_PATH = "save/change_log.txt";
    public static final String CHANGE_PATH = "save/search_log.txt";
    public TreeMap<String, Set<String>> data;
    public List<String> history;
    data_process(String[] input_path){
        if (input_path.length != 3)
            throw new IllegalArgumentException("Invalid input!");
        this.readfile(input_path[0]);
        this.loadHistory(input_path[0]);
    }
    private void readfile(String dir){
        this.data = new TreeMap<String, Set<String>>(String.CASE_INSENSITIVE_ORDER);

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
                for (int i = 0; i < syns.length; i++)
                    syns[i] = syns[i].trim();
                // Set<Integer> targetSet = new HashSet<Integer>(Arrays.asList(sourceArray));

                Set<String> words = new HashSet<String>(Arrays.asList(syns)); // Append processed data to treemap
                (this.data).put(content[0].trim(), words);
            }
            fr.close();

        }
        catch (IOException err){
            err.printStackTrace();
        }
    }
    private void loadHistory(String dir){
        this.history = new ArrayList<String>();

        try {
            File file = new File(dir);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line_info;
            while ((line_info = br.readLine()) != null){
                this.history.add(line_info.trim());
            }
            fr.close();

        }
        catch (IOException err){
            err.printStackTrace();
        }
    }


    public void printdata(){
        Set<Map.Entry<String, Set<String>>> entries = this.data.entrySet();
        for(Map.Entry<String, Set<String>> entry : entries){
            System.out.println( entry.getKey() + "\t->\t" + entry.getValue() );
        }
    }
    private void printWord(String key){
        Set<String> s = this.data.get(key);
        if (s != null) {
            System.out.println( key + "\t->\t" + s.toString());
        }
        else
            System.out.println("No matches");
    }
    //1. Chức năng tìm kiếm theo slang word.
    public Set<String> searchSlang(String word) {
        return this.data.get(word);
    }
    public List<String> searchSlang_suggestions(String word){
        List<String> result = new ArrayList<String>();
        Set<Map.Entry<String, Set<String>>> entries = this.data.entrySet();
        for(Map.Entry<String,Set<String>> entry: entries){
            String slang = entry.getKey();
            if (slang.startsWith(word.toLowerCase()) || slang.startsWith(word.toUpperCase())) {
                result.add(slang);
            }
        }
        return result;
    }

    // 2. Chức năng tìm kiếm theo definition, hiển thị ra tất cả các slang words mà trong
    // defintion có chứa keyword gõ vào.

    public List<String> searchDefinition(String word){
        List<String> result = new ArrayList<String>();
        Set<Map.Entry<String, Set<String>>> entries = this.data.entrySet();
        for(Map.Entry<String,Set<String>> entry: entries){
            Set<String> definit = entry.getValue();
            for (String i : definit){
                String lo = word.toLowerCase();
                String s1 = lo.substring(0, 1).toUpperCase();
                String nameCapitalized = s1 + lo.substring(1);
                if (i.contains(lo) || i.contains(word.toUpperCase()) || i.contains(word) || i.contains(nameCapitalized))
                    result.add(entry.getKey());
            }
        }
        return result;
    }

    // 3. Chức năng hiển thị history, danh sách các slang word đã tìm kiếm.

    // ------------------------ Console UI things --------------------------
    public void Console_searchSlang(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter any word to search: ");
        String kb = scan.nextLine();
        scan.close();
        kb = kb.trim();
        Set<String> query = this.searchSlang(kb);

        if (query == null) {
            System.out.println("No matches");
            System.out.println("Similar words");
            List<String> sugg = this.searchSlang_suggestions(Character.toString(kb.charAt(0)));
            System.out.println("Found " + sugg.size() + " matches");
            for (String i : sugg)
                this.printWord(i);
        }
        else {
            System.out.print(kb + "\t->\t");
            System.out.println(query);
        }
    }

    public void Console_searchDefinition(){
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter any definition word to search: ");
        String kb = scan.nextLine();
        scan.close();
        kb = kb.trim();
        List<String> query = this.searchDefinition(kb);
        if (query.isEmpty())
            System.out.println("No matches");
        else{
            System.out.println("Found " + query.size() + " matches");
            for (String i : query)
                this.printWord(i);
        }
    }

    // -----------------------------------------------------------------------
    public static void main(String args[]){
        String[] paths = {INPUT_PATH, SEARCH_PATH, CHANGE_PATH};
        try {
            data_process k = new data_process(paths);
        }
        catch (IllegalArgumentException ex){
            System.out.println(ex.getMessage());
        }
        // --------------------
        //k.printdata();
        //k.Console_searchSlang();
        //k.Console_searchDefinition();
    }

}