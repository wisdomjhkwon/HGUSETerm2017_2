import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class md2html {
	private static FileReader[] inputStreams;
	private static String[] htmlFiles;
	private static int option;
	public static void main(String[] args) {
		CommandInterpreter ci = new CommandInterpreter(args);
		inputStreams = ci.getInputStreams();
		htmlFiles = ci.getHTMLFiles();
		option = ci.getOption();
		MDParser mp = new MDParser(inputStreams);
	}
}

class MDParser{
	public MDParser(FileReader[] inputs){
		for(int i=0; i<inputs.length; i++){
			BufferedReader in = new BufferedReader(inputs[i]);
			String line;
			String bufferedLine;
			try {
				while ((line = in.readLine()) != null) {
					Structure.create(line);
					System.out.println(line);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

interface MDElement{
//	public void accept();
}

class Document implements MDElement{
	
}

class Structure implements MDElement{
	private String parsedLine;
	public Structure(String parsedLine){
		this.parsedLine = parsedLine;
	}
	public static Structure create(String parsedLine){
		return new Structure(parsedLine);
	}
}

class Header extends Structure{

	public Header(String parsedLine) {
		super(parsedLine);
		// TODO Auto-generated constructor stub
	}
	
}
class Block extends Structure{

	public Block(String parsedLine) {
		super(parsedLine);
		// TODO Auto-generated constructor stub
	}
	
}
class QuotedBlock extends Structure{

	public QuotedBlock(String parsedLine) {
		super(parsedLine);
		// TODO Auto-generated constructor stub
	}
	
}
class CodeBlock extends Structure{

	public CodeBlock(String parsedLine) {
		super(parsedLine);
		// TODO Auto-generated constructor stub
	}
	
}
class ItemList extends Structure{

	public ItemList(String parsedLine) {
		super(parsedLine);
		// TODO Auto-generated constructor stub
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
	private String[] md_files;
	private String[] html_files;
	private int option = 0; // 0: no option, 1: plain, 2: stylish, 3: slide
	private int mdcount = 0;
	private int htmlcount = 0;
	private FileReader[] input;

	public CommandInterpreter(String[] args) {
		md_files = new String[args.length];
		html_files = new String[args.length];
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
					if (html_files.length == 0) {
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
					if (html_files.length == 0) {
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
					if (html_files.length == 0) {
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
				input[i] = new FileReader(md_files[i]);
			} catch (FileNotFoundException e) {
				printNoFile();
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
		html_files[htmlcount++] = file;
	}

	public void addMdFile(String file) {
		md_files[mdcount++] = file;
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
	public String[] getHTMLFiles(){
		return html_files;
	}
	public int getOption() {
		return option;
	}
}