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
		inputStreams = ci.getInputStreams();
		htmlFiles = ci.getHTMLFiles();
		for(int i=0; i<htmlFiles.size(); i++){
			System.out.println(htmlFiles.get(i));
		}
		option = ci.getOption();
		mp = new MDParser(inputStreams);
		PlainVisitor v = new PlainVisitor();
		
		int i;

		System.out.println("visitDocument_size = "+mp.mdFiles.size());
		for(i=0; i<mp.mdFiles.size(); i++) {
			v.visitDocument(mp.mdFiles.get(i));
			
			//for test
			System.out.println("visitDocument("+i+")");
		}
		
		
		// mp.mdFiles.get(0).structures.get(0).lines.size();

	}

void print(){
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

class MDParser{
	public ArrayList<String> bufferedLine;
	public ArrayList<Document> mdFiles;
	public MDParser(FileReader[] inputs){
		bufferedLine = new ArrayList<String>();
		mdFiles = new ArrayList<Document>();
		int curState = -1;
		int prevState = -1;
		int flag = -1;
	   
		for(int i=0; i<inputs.length; i++){
			mdFiles.add(new Document());
			BufferedReader in = new BufferedReader(inputs[i]);
			String line;
			
			try {  
				while ((line = in.readLine()) != null) {
					curState = lineAnalysis(line);
					
					switch(curState) {
						case 0: {
							if(bufferedLine.size() != 0){
								if(lineAnalysis(bufferedLine.get(0)) == 1){
									mdFiles.get(i).structures.add(new Block(bufferedLine));
								}
								else if(lineAnalysis(bufferedLine.get(0)) == 4){
									mdFiles.get(i).structures.add(new QuotedBlock(bufferedLine));
								}
								else if(lineAnalysis(bufferedLine.get(0)) == 5){
									mdFiles.get(i).structures.add(new ItemList(bufferedLine));
								}
								else{
									mdFiles.get(i).structures.add(new Block(bufferedLine));
								}
							}
							this.clearBuffer(bufferedLine);
							break;
						}
						case 1: {
							this.toBuffer(bufferedLine, line);
							break;
						}
						case 2: {
							this.toBuffer(bufferedLine, line);
							mdFiles.get(i).structures.add(new Header(bufferedLine));
							this.clearBuffer(bufferedLine);
							break;
						}
						case 3:{
							if(prevState==1) {
								this.toBuffer(bufferedLine, line);
								mdFiles.get(i).structures.add(new Header(bufferedLine));
								this.clearBuffer(bufferedLine);
							}
							break;
						}
						case 4: { // > quoted block
							if(prevState != 4) {
								flag = prevState;
								if(bufferedLine.size() != 0){
									if(lineAnalysis(bufferedLine.get(0)) == 1){
										mdFiles.get(i).structures.add(new Block(bufferedLine));
									}
									else if(lineAnalysis(bufferedLine.get(0)) == 4){
										mdFiles.get(i).structures.add(new QuotedBlock(bufferedLine));
									}
									else if(lineAnalysis(bufferedLine.get(0)) == 5){
										mdFiles.get(i).structures.add(new ItemList(bufferedLine));
									}
									else{
										mdFiles.get(i).structures.add(new Block(bufferedLine));
									}
								}
								this.clearBuffer(bufferedLine);
								this.toBuffer(bufferedLine, line);
							}
							else 
								this.toBuffer(bufferedLine, line);
							break;
						}
						case 5: { // itemlist
							this.toBuffer(bufferedLine, line);
							break;
						}
						case 6: {
							break;
						}
						case 7: {
							break;
						}
						case 8: {
							this.toBuffer(bufferedLine, line);
							mdFiles.get(i).structures.add(new Horizontal(bufferedLine));
							this.clearBuffer(bufferedLine);
							break;
						}
						case 9: {
							this.toBuffer(bufferedLine, line);
							mdFiles.get(i).structures.add(new LinkReference(bufferedLine));
							this.clearBuffer(bufferedLine);
							break;
						}
					}
					prevState = curState;
				}

				if(bufferedLine.size() != 0){
					if(lineAnalysis(bufferedLine.get(0)) == 1){
						mdFiles.get(i).structures.add(new Block(bufferedLine));
					}
					else if(lineAnalysis(bufferedLine.get(0)) == 4){
						mdFiles.get(i).structures.add(new QuotedBlock(bufferedLine));
					}
					else if(lineAnalysis(bufferedLine.get(0)) == 5){
						mdFiles.get(i).structures.add(new ItemList(bufferedLine));
					}
					else{
						mdFiles.get(i).structures.add(new Block(bufferedLine));
					}
				}
				this.clearBuffer(bufferedLine);
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
      else{
		int i=0;
		String[] words = line.split("\\s");
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
							(words[2].charAt(0) == '(' || words[2].charAt(0) == '"') &&
							(words[2].charAt(words[2].length()-1) == ')' || words[2].charAt(words[2].length()-1) == '"')){
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
				return 2;
			}
			if(words[0].length() !=0 && words[0].charAt(0) == '>'){
				return 4;
			}
			int k=0;
			while(words[k].length() == 0)
				k++;
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
           }
	   }
	   return theBuffer;
   }
}

interface MDElement{
}

class Document implements MDElement{
	public ArrayList<Structure> structures;
	public Document(){
		structures = new ArrayList<Structure>();
	}
}

class Structure implements MDElement{
	public ArrayList<String> lines;
	public Structure(ArrayList<String> lines){
		this.lines = (ArrayList<String>)lines.clone();
	}
}

interface MDElementVisitor{
	void visitDocument(Document d);
	void visitStructure(Structure s);
	void visitStructure(Header s);
	void visitStructure(Block s);
	void visitStructure(QuotedBlock s);
	void visitStructure(ItemList s);
	void visitStructure(Horizontal s);
	void visitStructure(LinkReference s);
}
class PlainVisitor implements MDElementVisitor {
	public void visitDocument(Document d){
		
      // d ���� 
      System.out.println("struct size === "+d.structures.size());
		
      int i;
      for(i=0; i < d.structures.size() ;i++) {

          //for test
          System.out.println("visitStruct("+i+")");
    	  
          visitStructure( d.structures.get(i) );
          

      }
	}         


	public void visitStructure(Structure s) {
        System.out.println("overload");
		if(s instanceof Header) {			visitStructure((Header)s);		}
		else if (s instanceof Block) {			visitStructure((Block)s); 		}
		else if (s instanceof QuotedBlock) {			visitStructure((QuotedBlock)s);		}
		else if (s instanceof ItemList) {			visitStructure((ItemList)s);		}
		else if (s instanceof Horizontal) {		visitStructure((Horizontal)s);	}
		else if (s instanceof LinkReference) {		visitStructure((LinkReference)s);	}
		else {	System.out.println("error");		}
	}

	
	public void visitStructure(Header s) {
		
		
		//�ܾ� ���ϳ� ���ڸ�, ���ΰ� ����, image��ũ
		//�� ���� �ܾ�� �����ؾ�

		
		
		//for test
		System.out.println("		visitStructure(Header)");
		
		for(int k=0; k<s.lines.size(); k++){
			System.out.println("===>> "+s.lines.get(k));
		}
		System.out.println();
	}
	public void visitStructure(Block s) {
		//for test
		System.out.println("		visitStructure(Block)");
		
		for(int k=0; k<s.lines.size(); k++){
			System.out.println("===>> "+s.lines.get(k));
		}
		System.out.println();
	}
	public void visitStructure(QuotedBlock s) {
		//for test
		System.out.println("		visitStructure(QuotedBlock)");
		
		for(int k=0; k<s.lines.size(); k++){
			System.out.println("===>> "+s.lines.get(k));
		}
		System.out.println();
	}
	public void visitStructure(ItemList s) {
		//for test
		System.out.println("		visitStructure(ItemList)");
		
		for(int k=0; k<s.lines.size(); k++){
			System.out.println("===>> "+s.lines.get(k));
		}
		System.out.println();
	}
	public void visitStructure(Horizontal s) {

		//for test
		System.out.println("		visitStructure(Horizontal)");
		
		for(int k=0; k<s.lines.size(); k++){
			System.out.println("===>> "+s.lines.get(k));
		}
		System.out.println();
		
	}
	public void visitStructure(LinkReference s) {

		//for test
		System.out.println("		visitStructure(LinkReference)");
		
		for(int k=0; k<s.lines.size(); k++){
			System.out.println("===>> "+s.lines.get(k));
		}
		System.out.println();
	}

}


class Header extends Structure{
	public Header(ArrayList<String> lines) {
		super(lines);
	}
}
class Block extends Structure{
	public Block(ArrayList<String> lines) {
		super(lines);
	}
}
class QuotedBlock extends Structure{
	public QuotedBlock(ArrayList<String> lines) {
		super(lines);
	}
}

class ItemList extends Structure{
	public ItemList(ArrayList<String> lines) {
		super(lines);
	}
}
class Horizontal extends Structure{
	public Horizontal(ArrayList<String> lines) {
		super(lines);
	}
}
class LinkReference extends Structure{
	public LinkReference(ArrayList<String> lines) {
		super(lines);
	}
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
		if(html_files.size() == 0){
			for(int i=0; i<md_files.size(); i++){
			   String fileName = md_files.get(i).substring(md_files.get(i).lastIndexOf('/') + 1);
			   html_files.add(fileName.substring(0, fileName.lastIndexOf('.')) + ".html");
			}
		 }
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