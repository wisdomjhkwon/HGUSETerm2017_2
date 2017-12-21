import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

public class CommandInterpreterTest {

	@Test
	public void testGetFormat() {
		CommandInterpreter ci = new CommandInterpreter();

		ci.getFormat("test.md");
		ci.getFormat("test.html");
//		ci.getFormat("test");
	}
	
	@Test
	public void test1(){
		String[] args = {"samples/doc1.md"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test2(){
		String[] args = {"samples/doc1.md", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test3(){
		String[] args = {"samples/doc1.md", "-o", "test.html", "--plain"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test4(){
		String[] args = {"samples/doc1.md", "-o", "test.html", "--stylish"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test5(){
		String[] args = {"samples/doc1.md", "-o", "test.html", "--slide"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test6(){
		String[] args = {"samples/doc5.md", "-o", "test.html", "--slide"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test7(){
		String[] args = {"samples/doc", "-o", "test.html", "--slide"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
//	
	@Test
	public void test8(){
		String[] args = {"help"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test9(){
		String[] args = {"samples/doc5.md", "-o"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test10(){
		String[] args = {"samples/doc1.md", "--plain", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test11(){
		String[] args = {"samples/doc1.md", "--stylish", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test12(){
		String[] args = {"samples/doc1.md", "--slide", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test13(){
		String[] args = {"--plain", "samples/doc1.md", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test14(){
		String[] args = {"--stylish", "samples/doc1.md", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
	
	@Test
	public void test15(){
		String[] args = {"--slide", "samples/doc1.md", "-o", "test.html"};
		CommandInterpreter ci = new CommandInterpreter(args);
	}
//	
//	@Test
//	public void test1(){
//		String[] args = {"java -cp classes md2html"};
//		CommandInterpreter ci = new CommandInterpreter(args);
//	}

//	@Test
//	public void testAddHtmlFile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testAddMdFile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintIllegalMessage() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintNoFile() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testPrintHelp() {
//		fail("Not yet implemented");
//	}

}
