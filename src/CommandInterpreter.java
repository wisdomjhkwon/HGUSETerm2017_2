import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

class CommandInterpreter {
	public ArrayList<String> md_files;
	public ArrayList<String> html_files;
	public int option = 0; // 0: no option, 1: plain, 2: stylish, 3: slide
	public int mdcount = 0;
	public int htmlcount = 0;
	public FileReader[] input;

	public CommandInterpreter() {}
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
		return;

	}

	public void printNoFile() {
		System.out.println("File does not exist.");
		return;	
	}
	public void printHelp(){
		System.out.println("usage : java md2html [mdfiles] [-o htmlfiles] [--option]");
		System.out.println();
		System.out.println("  --plain(default)	plain design");
		System.out.println("  --stylish		stylish design");
		System.out.println("  --slide		make slide");
		System.out.println();

		return;
	}
}