import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class md2html {
	private static FileReader[] inputStreams;
	private static ArrayList<String> htmlFiles;
	private static int option;
	public static void main(String[] args) {
		CommandInterpreter ci = new CommandInterpreter(args);
		inputStreams = ci.getInputStreams();
		htmlFiles = ci.getHTMLFiles();
		for(int i=0; i<htmlFiles.size(); i++){
			System.out.println(htmlFiles.get(i));
		}
		option = ci.getOption();
		MDParser mp = new MDParser(inputStreams);
	}
}

class MDParser{
	public ArrayList<String> bufferedLine;
   public MDParser(FileReader[] inputs){
	   bufferedLine = new ArrayList<String>();
	   
      for(int i=0; i<inputs.length; i++){
         BufferedReader in = new BufferedReader(inputs[i]);
         String line;
		 int curState;
		 int prevState;
		 
         try {
            while ((line = in.readLine()) != null) {
				curState = lineAnalysis(line);
				System.out.println(line + " " + curState);
			   


				prevState = curState;
            }
         } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
      }
   }
   
   public int lineAnalysis(String line){
      if(line.length() == 0){
         return 0;
      }
    //   else if(line.charAt(0)==' ' && line.charAt(1)==' ' && line.charAt(2)==' ' && line.charAt(3)==' '){
    //      if(line.charAt(4)==' ' && line.charAt(5)==' ' && line.charAt(6)==' ' && line.charAt(7)==' '){
    //         return 7; // double indented
    //      }
    //      return 6; // indented
    //   }
      else{
		int i=0;
		String[] words = line.split("\\s");
		// System.out.println(words.length);
		if(words.length == 1){
			int j=0;
			for(i=0; i<words[0].length(); i++){
				if(words[0].charAt(i) != '=' && words[0].charAt(i) != '-'){
					break;
				}
			}
			if(i == words[0].length()){
				return 3;
			}
		}
		else{
			if(words.length >= 3){
				if(words.length == 3){
					if(words[0].charAt(0) == '[' && 
							words[0].charAt(words[0].length()-2) == ']' && 
							words[0].charAt(words[0].length()-1) == ':' &&
							words[2].charAt(0) == '(' &&
							words[2].charAt(words[2].length()-1) == ')'){
						return 9;
					}
				}
				for(i=0; i<words.length; i++){
					if(!words[i].equals("-"))
						break;
				}
				if(i == words.length){
					return 8;
				}
			}
			for(i=0; i<words[0].length(); i++){
				if(words[0].charAt(i) != '#'){
					break;
				}
			}
			if(i == words[0].length() && i != 0){
				// System.out.println(words[0]);
				return 2;
			}
			if(words[0].length() !=0 && words[0].charAt(0) == '>'){
				return 4;
			}
			int k=0;
			while(words[k].length() == 0)
				k++;
			// System.out.println(words[k]);
			if(words[k].equals("*") || words[k].equals("+") || words[k].equals("-")){
				return 5;				
			}
		}
		return 1;
      }
   }

   public void toBuffer(ArrayList<String> theBuffer, String inputLine) {
	   theBuffer.add(inputLine);
   }
   
   public void clearBuffer(ArrayList<String> theBuffer) {
	   theBuffer.clear();
   }
   
   public ArrayList<String> getBuffer(ArrayList<String> theBuffer) {
	   if(!theBuffer.isEmpty()) {
		   for(int i = 0; i < theBuffer.size(); i++) {
               System.out.println("one index " + i + " : value " + theBuffer.get(i));
           }
	   }
	   return theBuffer;
   }
}

interface MDElement{
//	public void accept();
}

class Document implements MDElement{
	
}

class Structure implements MDElement{
	private String parsedLine;
	public static Structure create(String parsedLine){
		Structure newStructure = new Structure();
		newStructure.setParsedLine(parsedLine);
		return newStructure;
	}
	public void setParsedLine(String parsedLine){
		this.parsedLine = parsedLine;
	}
}

class Header extends Structure{
	private int headerNum;

	public int getHeaderNum() {
		return headerNum;
	}

	public void setHeaderNum(int headerNum) {
		this.headerNum = headerNum;
	}
}
class Block extends Structure{
	
}
class QuotedBlock extends Structure{
	
}
class CodeBlock extends Structure{
	
}
class ItemList extends Structure{
	private int type;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
} 

class Text implements MDElement{
	
}

class PlainText extends Text{
	
}
class SytleText extends Text{
	
}
class HTMLCode extends Text{
	
}

class CommandInterpreter {
	private ArrayList<String> md_files;
	private ArrayList<String> html_files;
	private int option = 0; // 0: no option, 1: plain, 2: stylish, 3: slide
	private int mdcount = 0;
	private int htmlcount = 0;
	private FileReader[] input;

	public CommandInterpreter(String[] args) {
		md_files = new ArrayList<String>();
		html_files = new ArrayList<String>();
		String c1 = "";
		String word;
		String next_word;
		if (args.length < 1) {
			printIllegalMessage();
		}
		c1 = args[0];
		if (c1.indexOf('.') != -1) {
			String sp[] = c1.split("\\.");
			if (sp[sp.length-1].equals("md")) {
				addMdFile(c1);
			} else {
				System.out.println("???");
				printIllegalMessage();
			}
		} else if (c1.equals("--plain")) {
			option = 1;
		} else if (c1.equals("--stylish")) {
			option = 2;
		} else if (c1.equals("--slide")) {
			option = 3;
		} else if (c1.equals("help")) {
		} else {
			printIllegalMessage();
		}

		for (int i = 0; i < args.length; i++) {
			word = args[i];
			if (i + 1 == args.length)
				next_word = null;
			else
				next_word = args[i + 1];
			if (word.equals("help")) {
				if (next_word == null) {
					printHelp();
				} else {
					printIllegalMessage();
				}
			} else if (word.equals("--plain")) {
				if (next_word == null) {
					if (mdcount == 0) {
						printIllegalMessage();
					}
					option = 1;
					break;
				} else if (next_word.equals("-o")) {
					if (htmlcount == 0) {
						printIllegalMessage();
					}
					option = 1;
				} else if (getFormat(next_word).equals("md")) {
					if (mdcount != 0) {
						printIllegalMessage();
					} else {
						addMdFile(next_word);
						option = 1;
					}
				} else {
					printIllegalMessage();
				}
			} else if (word.equals("--stylish")) {
				if (next_word == null) {
					if (mdcount == 0) {
						printIllegalMessage();
					}
					option = 2;
					break;
				} else if (next_word.equals("-o")) {
					if (htmlcount == 0) {
						printIllegalMessage();
					}
					option = 2;
				} else if (getFormat(next_word).equals("md")) {
					if (mdcount != 0) {
						printIllegalMessage();
					} else {
						addMdFile(next_word);
						option = 2;
					}
				} else {
					printIllegalMessage();
				}
			} else if (word.equals("--slide")) {
				if (next_word == null) {
					if (mdcount == 0) {
						printIllegalMessage();
					}
					option = 3;
					break;
				} else if (next_word.equals("-o")) {
					if (htmlcount == 0) {
						printIllegalMessage();
					}
					option = 3;
				} else if (getFormat(next_word).equals("md")) {
					if (mdcount != 0) {
						printIllegalMessage();
					} else {
						addMdFile(next_word);
						option = 3;
					}
				} else {
					printIllegalMessage();
				}
			} else if (word.equals("-o")) {
				if (next_word == null) {
					printIllegalMessage();
				} else if (getFormat(next_word).equals("html")) {
					addHtmlFile(next_word);
				} else {
					printIllegalMessage();
				}
			} else if (getFormat(word).equals("md")) {
				if (next_word == null) {
					break;
				} else if (option == 0 && next_word.equals("--plain")) {
					option = 1;
				} else if (option == 0 && next_word.equals("--stylish")) {
					option = 2;
				} else if (option == 0 && next_word.equals("--slide")) {
					option = 3;
				} else if (next_word.equals("-o")) {
				} else if (getFormat(next_word).equals("md")) {
					addMdFile(next_word);
				} else {
					printIllegalMessage();
				}
			} else if (getFormat(word).equals("html")) {
				if (next_word == null && mdcount != htmlcount) {
					printIllegalMessage();
				} else if (next_word == null && mdcount == htmlcount) {
					break;
				} else if (option == 0 && next_word.equals("--plain")) {
					option = 1;
				} else if (option == 0 && next_word.equals("--stylish")) {
					option = 2;
				} else if (option == 0 && next_word.equals("--slide")) {
					option = 3;
				} else if (getFormat(next_word).equals("html") && mdcount > htmlcount) {
					addHtmlFile(next_word);
				} else {
					printIllegalMessage();
				}
			} else {
				printIllegalMessage();
			}
		}
		if (option == 0) {
			option = 1;
		}
		input = new FileReader[mdcount];
		for (int i = 0; i < mdcount; i++) {
			try {
				input[i] = new FileReader(md_files.get(i));
			} catch (FileNotFoundException e) {
				printNoFile();
			}
		}
		
//		if(html_files)
	}

	public String getFormat(String file) {
		if (file.lastIndexOf('.') == -1) {
			printIllegalMessage();
			return "";
		} else {
			return file.substring(file.lastIndexOf('.') + 1, file.length());
		}
	}

	public void addHtmlFile(String file) {
		html_files.add(file);
		htmlcount++;
	}

	public void addMdFile(String file) {
		md_files.add(file);
		mdcount++;
	}

	public void printIllegalMessage() {
		System.out.println("usage : java md2html [mdfiles] [-o htmlfiles] [--option]");
		System.exit(-1);
	}

	public void printNoFile() {
		System.out.println("File does not exist.");
		System.exit(-1);
	}
	public void printHelp(){
		System.out.println("usage : java md2html [mdfiles] [-o htmlfiles] [--option]");
		System.out.println();
		System.out.println("  --plain(default)	plain design");
		System.out.println("  --stylish		stylish design");
		System.out.println("  --slide		make slide");
		System.out.println();

		System.exit(0);
	}
	public FileReader[] getInputStreams(){
		return input;
	}
	public ArrayList<String> getHTMLFiles(){
		return html_files;
	}
	public int getOption() {
		return option;
	}
}