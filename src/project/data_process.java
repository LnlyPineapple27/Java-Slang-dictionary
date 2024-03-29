// Student Name: Phan Tan Dat
// Student ID: 18127078
// Java project Slang Dictionary with Console UI
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
package project;

import java.io.*;
import java.util.*;

public class data_process {
    public static final String INPUT_PATH = "input/slang.txt";
    public static final String SEARCH_PATH = "save/search_log.txt";
    public static final String CHANGE_PATH = "save/change_log.txt";
    public static final String TRASH_PATH = "save/trash_bin.txt";
    private Scanner keyboard;
    public TreeMap<String, Set<String>> data, changes;
    public List<String> history, deleted;
    public String[] paths;
    public String day_word;
    public data_process(){
        this.keyboard = new Scanner(System.in);
        this.paths = new String[]{INPUT_PATH, SEARCH_PATH, CHANGE_PATH, TRASH_PATH};
        this.readfile(this.paths[0]);
        this.loadHistory(this.paths[1]);
        this.loadChanges(this.paths[2]);
        this.loadDeleted(this.paths[3]);
        this.day_word = this.getRandomWord();
    }
    public data_process(String[] input_path){
        if (input_path.length != 4)
            throw new IllegalArgumentException("[Invalid input!]");
        this.keyboard = new Scanner(System.in);
        this.paths = input_path;
        this.readfile(this.paths[0]);
        this.loadHistory(this.paths[1]);
        this.loadChanges(this.paths[2]);
        this.loadDeleted(this.paths[3]);
        this.day_word = this.getRandomWord();
    }
    protected void finalize(){
        this.backupHistory();
        this.backupChange();
        this.backupDeleted();
        keyboard.close();
        System.out.println("[Good bye]");
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

                Set<String> words = new HashSet<String>(Arrays.asList(syns)); // Append processed data to treemap
                (this.data).put(content[0].trim(), words);
            }
            fr.close();

        }
        catch (IOException err){
            err.printStackTrace();
            System.out.println("[No input file was found in given directory!!]");
            System.exit(0);
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

            System.out.println("[History save loaded]");
        }
        catch (IOException err){
            //err.printStackTrace();
            System.out.println("[No history save is found]");
        }
    }
    private void loadDeleted(String dir){
        this.deleted = new ArrayList<String>();

        try {
            File file = new File(dir);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);

            String line_info;
            while ((line_info = br.readLine()) != null){
                this.deleted.add(line_info.trim());
            }
            fr.close();

            System.out.println("[Delete save loaded]");
        }
        catch (IOException err){
            //err.printStackTrace();
            System.out.println("[No delete save is found]");
        }
        int count = 0;
        //System.out.println(this.deleted.size());
        for(String i : this.deleted){
            if (!this.realDelete(i))
                continue;
            count++;
        }
        System.out.println("[Deleted " + Integer.toString(count) + " words]");
    }
    private void loadChanges(String dir){
        this.changes = new TreeMap<String, Set<String>>(String.CASE_INSENSITIVE_ORDER);
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

                Set<String> words = new HashSet<String>(Arrays.asList(syns)); // Append processed data to treemap
                this.data.put(content[0].trim(), words);
                this.changes.put(content[0].trim(), words);
            }
            fr.close();
            System.out.println("[Changes save loaded]");
        }
        catch (IOException err){
            //err.printStackTrace();
            System.out.println("[No change save is found]");
        }
    }
    public void print_all_data(){
        Set<Map.Entry<String, Set<String>>> entries = this.data.entrySet();
        for(Map.Entry<String, Set<String>> entry : entries){
            System.out.println( entry.getKey() + "\t->\t" + entry.getValue() );
        }
    }
    private void printWord(String key){
        Set<String> s = this.data.get(key);
        if (s != null) {
            System.out.println("\t" + key.toUpperCase() + "\t->\t" + s.toString());
        }
        else
            System.out.println("No matches");
    }
    //1. Chức năng tìm kiếm theo slang word.
    public Set<String> searchSlang(String word) {
        this.appendHistory(word);
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
    private void appendHistory(String search_word){
        this.history.add(search_word.trim());
    }
    public void printHistory(){
        if (this.history.isEmpty()){
            System.out.println("Empty history!");
            return;
        }
        System.out.println("[Searched slangs]:");
        for (String i: this.history){
            System.out.println("\t" + i);
        }
    }
    private void backupHistory(){
        File fold = new File(this.paths[1]);
        fold.delete();
        File fnew = new File(this.paths[1]);
        File parentDir = fnew.getParentFile();
        if(! parentDir.exists())
            parentDir.mkdirs();
        try {
            FileWriter f2 = new FileWriter(fnew, false);
            for(String i : this.history) {
                f2.write(i + "\n");
            }
            f2.close();

        } catch (IOException err) {
            err.printStackTrace();
        }
    }
    private void backupChange(){
        File fold = new File(this.paths[2]);
        fold.delete();
        File fnew = new File(this.paths[2]);
        File parentDir = fnew.getParentFile();
        if(! parentDir.exists())
            parentDir.mkdirs();

        try {
            FileWriter f2 = new FileWriter(fnew, false);
            Set<Map.Entry<String, Set<String>>> entries = this.changes.entrySet();
            for(Map.Entry<String, Set<String>> entry : entries){
                f2.write( entry.getKey() + "`");
                Set<String> s =  entry.getValue();
                int num = s.size();
                for(String i: s){
                    num--;
                    if(num != 0)
                        f2.write(i + "| ");
                    else
                        f2.write(i + "\n");
                }

            }
            f2.close();

        } catch (IOException err) {
            err.printStackTrace();
        }
    }
    // 4. Chức năng add 1 slang words mới. Nếu slang words trùng thì thông báo cho người
    // dùng, confirm có overwrite hay duplicate ra 1 slang word mới.

    // 5. Chức năng edit 1 slang word.
    private void editSlang(String word, Set<String> definitions, boolean overwrite){
        if (overwrite){
            this.changes.put(word, definitions);
            this.data.put(word, definitions);
        }
        else{
            Set <String> old = this.data.get(word);
            definitions.addAll(old);
            this.changes.put(word, definitions);
            this.data.put(word, definitions);
        }
    }
    private void addSlang(String word, Set<String> definitions){
        this.changes.put(word, definitions);
        this.data.put(word, definitions);
    }

    // 6. Chức năng delete 1 slang word. Confirm trước khi xoá.
    private void deleteSlang(String word){
        word = word.toUpperCase();
        if (realDelete(word))
            this.deleted.add(word);
    }
    private boolean realDelete(String word){
        if (this.data.remove(word) == null)
            return false; //nothing was deleted
        return true;
    }
    private void backupDeleted(){
        File fold = new File(this.paths[3]);
        fold.delete();
        File fnew = new File(this.paths[3]);

        File parentDir = fnew.getParentFile();
        if(! parentDir.exists())
            parentDir.mkdirs();
        try {
            FileWriter f2 = new FileWriter(fnew, false);
            for(String i : this.deleted) {
                f2.write(i + "\n");
            }
            f2.close();

        } catch (IOException err) {
            err.printStackTrace();
        }
    }

    // 7. Chức năng reset danh sách slang words gốc.
    private void resetDictionary(){
        this.readfile(this.paths[0]);
        this.deleted.clear();
        this.changes.clear();
    }

    // 8. Chức năng random 1 slang word (On this day slang words).
    private String getRandomWord(){
        Object[] Keys = this.data.keySet().toArray();
        int index = new Random().nextInt(Keys.length);
        return (String) Keys[index];
    }

    // 9, 10. Chức năng đố vui

    private Map<String, Set<String>> generateQuiz() {
        Map<String, Set<String>> result = new HashMap<String, Set<String>>();
        while (result.size() != 4) {
            String word = getRandomWord();
            result.put(word, this.data.get(word));
        }
        return result;
    }
    // ------------------------ Console UI things --------------------------
    public void Console_searchSlang(){
        System.out.print("Enter any word to search: ");
        String kb = this.keyboard.nextLine().trim();
        Set<String> query = this.searchSlang(kb);

        if (query == null) {
            System.out.println("[No matches]");
            if (kb != "") {
                System.out.println("\tShow suggestions?");
                System.out.println("\t\tYes (1)");
                System.out.println("\t\tNo  (2)");
                int kb2 = 0;
                while (true) {
                    while (true) {
                        try {
                            System.out.print("\nPlease enter an integer (1 or 2): ");
                            kb2 = Integer.parseInt(this.keyboard.nextLine().trim());
                            break;
                        } catch (NumberFormatException err) {
                            System.out.println("Invalid input! Try again");
                        }
                    }
                    if (kb2 == 1 || kb2 == 2)
                        break;
                }
                if (kb2 == 1) {
                    System.out.println("Similar words");
                    List<String> sugg = this.searchSlang_suggestions(Character.toString(kb.charAt(0)));
                    System.out.println("Found " + sugg.size() + " matches");
                    for (String i : sugg)
                        this.printWord(i);
                }
            }
        }
        else {
            System.out.print("\t" + kb.toUpperCase() + "\t->\t");
            System.out.println(query);
        }
    }

    public void Console_searchDefinition(){
        System.out.print("Enter any definition word to search: ");
        String kb = this.keyboard.nextLine();
        kb = kb.trim();
        List<String> query = this.searchDefinition(kb);
        if (query.isEmpty())
            System.out.println("[No matches]");
        else{
            System.out.println("Found " + query.size() + " matches");
            for (String i : query) {
                this.printWord(i);
                this.appendHistory(i);
            }
        }
    }

    public void Console_menuUI(){
        String [] options = new String[]{
                "1.  Slang Search",
                "2.  Definition Search",
                "3.  Show Slang Search History",
                "4.  Add/Edit Slang",
                "5.  Delete Slang",
                "6.  Reset dictionary",
                "7.  On this day slang word",
                "8.  Slang quiz",
                "9.  Definition quiz",
                "10. Exit"
        };
        label:
        while (true){
            System.out.println("\n-------------------------------------------------------------------------");
            for (String i : options){
                System.out.println(i);
            }

            System.out.print("Enter a number to select task: ");
            String input = keyboard.nextLine();

            switch (input){
                case "1":
                    this.Console_searchSlang();
                    break;
                case "2":
                    this.Console_searchDefinition();
                    break;
                case "3":
                    this.printHistory();
                    break;
                case "4":
                    this.Console_addSlang();
                    break;
                case "5":
                    this.Console_deleteSlang();
                    break;
                case "6":
                    this.Console_resetDictionary();
                    break;
                case "7":
                    this.Console_slang_of_the_day();
                    break;
                case "8":
                    this.Console_SlangQuiz();
                    break;
                case "9":
                    this.Console_DefinitionQuiz();
                    break;
                case "10":
                    break label;
                default:
                    System.out.println("[Invalid input! Please enter again]");
            }

        }

        this.finalize();
    }

    public void Console_slang_of_the_day(){
        System.out.println("Slang of the day is: ");
        this.printWord(this.day_word);
    }

    public void Console_addSlang(){
        System.out.print("Enter a slang(this will be converted to uppercase automatically): ");
        String word = this.keyboard.nextLine().trim().toUpperCase();
        if(this.data.containsKey(word)){
            System.out.println("\tThis Slang was defined in data base:");
            this.printWord(word);
            System.out.println("\tDo you wish to overwrite it (1)\n" +
                                "\tor just add new definition to it (2)");
            int kb = 0;
            while (true) {
                while (true) {
                    try {
                        System.out.print("\nPlease enter an integer (1 or 2): ");
                        kb = Integer.parseInt(this.keyboard.nextLine().trim());
                        break;
                    } catch (NumberFormatException err) {
                        System.out.println("Invalid input! Try again");
                    }
                }
                if (kb == 1 || kb == 2)
                    break;
            }
            int num = 0;
            while (true) {
                try {
                    System.out.print("\nEnter number of definitions to add: ");
                    num = Integer.parseInt(this.keyboard.nextLine().trim());
                    break;
                } catch (NumberFormatException err) {
                    System.out.println("Invalid input! Try again");
                }
            }
            Set<String> definitions = new HashSet<String>();
            for (int i = 1; i <= num; i++){
                System.out.print("Enter definition " + Integer.toString(i) + ": ");
                definitions.add(this.keyboard.nextLine().trim());
            }
            if (kb == 1)
                this.editSlang(word, definitions, true);
            else

            if (kb == 2)
                this.editSlang(word, definitions, false);

        }
        else {
            int num = 0;
            while (true) {
                try {
                    System.out.print("\nEnter number of definitions to add this new slang: ");
                    num = Integer.parseInt(this.keyboard.nextLine().trim());
                    break;
                } catch (NumberFormatException err) {
                    System.out.println("Invalid input! Try again");
                }
            }

            Set<String> definitions = new HashSet<String>();
            for (int i = 1; i <= num; i++){
                System.out.print("Enter definition " + Integer.toString(i) + ": ");
                definitions.add(this.keyboard.nextLine().trim());
            }
            this.addSlang(word, definitions);
        }
    }

    public void Console_deleteSlang(){
        System.out.print("Enter a slang to delete(this will be converted to uppercase automatically): ");
        String word = this.keyboard.nextLine().trim().toUpperCase();
        if(this.data.containsKey(word)){
            System.out.println("\tAre you sure to delete this slang?:");
            System.out.println("\t\tYes (1)");
            System.out.println("\t\tNo  (2)");
            int kb = 0;
            while (true) {
                while (true) {
                    try {
                        System.out.print("\t\nPlease enter an integer (1 or 2): ");
                        kb = Integer.parseInt(this.keyboard.nextLine().trim());
                        break;
                    } catch (NumberFormatException err) {
                        System.out.println("Invalid input! Try again");
                    }
                }

                if (kb == 1 || kb == 2)
                    break;
            }
            if (kb == 1)
                this.deleteSlang(word);
                System.out.println("Deletion completed");
        }
        else {
            System.out.println("This slang does not exist!!");
        }
    }

    public void Console_resetDictionary(){
        System.out.println("\tAre you sure to reset dictionary?:");
        System.out.println("\t\tYes (1)");
        System.out.println("\t\tNo  (2)");
        int kb = 0;
        while (true) {
            while (true) {
                try {
                    System.out.print("\t\nPlease enter an integer (1 or 2): ");
                    kb = Integer.parseInt(this.keyboard.nextLine().trim());
                    break;
                } catch (NumberFormatException err) {
                    System.out.println("Invalid input! Try again");
                }
            }

            if (kb == 1 || kb == 2)
                break;
        }
        if (kb == 1)
            this.resetDictionary();
    }

    public void Console_SlangQuiz(){
        System.out.println("____________SLANG QUIZ_____________");
        Map<String, Set<String>> quiz = this.generateQuiz();
        int max = quiz.size(), min = 1;
        int ans = new Random().nextInt((max - min) + 1) + min;
        int i  = 0;
        String ques = "";
        for (Map.Entry<String, Set<String>> entry : quiz.entrySet()) {
            i++;
            System.out.print(Integer.toString(i) + ".  ");
            System.out.println(entry.getValue());
            if (i == ans)
                ques = entry.getKey();
        }
        int choice = 0;
        while (true) {
            try {
                System.out.print("\t\nWhich one is the definition of Slang [" + ques +"]\n\t(choose 1 to " + Integer.toString(i) + "): ");
                choice = Integer.parseInt(this.keyboard.nextLine().trim());
                break;
            } catch (NumberFormatException err) {
                System.out.println("Invalid input! Try again");
            }
        }

        if (choice == ans)
            System.out.println("\t\tCorrect!");
        else System.out.println("\t\tWrong :<");
    }

    public void Console_DefinitionQuiz(){
        System.out.println("____________DEFINITION QUIZ_____________");
        Map<String, Set<String>> quiz = this.generateQuiz();
        int max = quiz.size(), min = 1;
        int ans = new Random().nextInt((max - min) + 1) + min;
        int i  = 0;
        Set<String> ques = null;
        for (Map.Entry<String, Set<String>> entry : quiz.entrySet()) {
            i++;
            System.out.print(Integer.toString(i) + ".  ");
            System.out.println(entry.getKey());
            if (i == ans)
                ques = entry.getValue();
        }
        if (ques == null) {
            System.out.println("Something went wrong :<");
            return;
        }

        int choice = 0;
        while (true) {
            try {
                System.out.print("\t\nWhich one is the slang of " + ques + "\n\t(choose 1 to " + Integer.toString(i) + "): ");
                choice = Integer.parseInt(this.keyboard.nextLine().trim());
                break;
            } catch (NumberFormatException err) {
                System.out.println("Invalid input! Try again");
            }
        }

        if (choice == ans)
            System.out.println("\t\tCorrect!");
        else System.out.println("\t\tWrong :<");
    }

    // -----------------------------------------------------------------------
    public static void main(String args[]){
        data_process k = new data_process();
        k.Console_menuUI();
    }

}