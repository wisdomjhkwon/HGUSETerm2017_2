import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class md2html {
    private static FileReader[] inputStreams;
    private static ArrayList<String> htmlFiles;
    private static MDParser mp;
    private static int option;
    public static void main(String[] args) {
        
        CommandInterpreter ci = new CommandInterpreter(args);
        inputStreams = ci.input;
        htmlFiles = ci.html_files;
        for(int i=0; i<htmlFiles.size(); i++){
            System.out.println(htmlFiles.get(i));
        }
        option = ci.option;
        mp = new MDParser(inputStreams);
        
        int i;
        
        System.out.println("visitDocument_size = "+mp.mdFiles.size());
        for(i=0; i<mp.mdFiles.size(); i++) {
            if(option == 1){
                PlainVisitor v = new PlainVisitor(htmlFiles.get(i));
                v.visitDocument(mp.mdFiles.get(i));
                v = null;
            } else if (option == 2){
                StyleVisitor v = new StyleVisitor(htmlFiles.get(i));
                v.visitDocument(mp.mdFiles.get(i));
                v = null;
            } else if (option == 3){
                // slide
            }
            System.out.println("!!!visitDocument("+i+")");
        }
        
        
        
    }

static public void print(){
		for(int i=0; i<mp.mdFiles.size(); i++){
			System.out.println(mp.mdFiles.get(i).structures.size());
			for(int j=0; j<mp.mdFiles.get(i).structures.size(); j++){
				if(mp.mdFiles.get(i).structures.get(j) instanceof Block){
					System.out.println("Block");
				}
				else if(mp.mdFiles.get(i).structures.get(j) instanceof Header){
					System.out.println("Header");
				}
				else if(mp.mdFiles.get(i).structures.get(j) instanceof QuotedBlock){
					System.out.println("QuotedBlock");
				}
				else if(mp.mdFiles.get(i).structures.get(j) instanceof ItemList){
					System.out.println("ItemList");
				}
				else if(mp.mdFiles.get(i).structures.get(j) instanceof Horizontal ){
					System.out.println("Horizontal");
				}
				else if(mp.mdFiles.get(i).structures.get(j) instanceof LinkReference){
					System.out.println("LinkReference");
				}
				for(int k=0; k<mp.mdFiles.get(i).structures.get(j).lines.size(); k++){
					System.out.println(mp.mdFiles.get(i).structures.get(j).lines.get(k));
				}
				System.out.println();
			}
		}
	}
}