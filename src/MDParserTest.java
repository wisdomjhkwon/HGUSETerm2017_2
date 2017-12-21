import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Test;

public class MDParserTest {
	
	@Test
	public void testLineAnalysis() {
		MDParser mp = new MDParser();
		assertTrue(mp.lineAnalysis("") == 0);
		assertTrue(mp.lineAnalysis("paragraph") == 1);
		assertTrue(mp.lineAnalysis("# header #") == 2);
		assertTrue(mp.lineAnalysis("=====") == 3);
		assertTrue(mp.lineAnalysis("-----") == 3);
		assertTrue(mp.lineAnalysis(">quotedblock") == 4);
		assertTrue(mp.lineAnalysis("* itemlist") == 5);
		assertTrue(mp.lineAnalysis("+ itemlist") == 5);
		assertTrue(mp.lineAnalysis("  + itemlist") == 5);
		assertTrue(mp.lineAnalysis("- itemlist") == 5);
		assertTrue(mp.lineAnalysis("- - -") == 8);
		assertTrue(mp.lineAnalysis("[1]: Github.com (GitHub)") == 9);
		assertTrue(mp.lineAnalysis("[1]: http://www.handong.edu \"Handong\"") == 9);
	}
	
	@Test
	public void test1() {
		try {
			FileReader[] inputs = {new FileReader("samples/doc1.md"),
									new FileReader("samples/doc2.md"),
									new FileReader("samples/doc3.md"),
									new FileReader("samples/doc4.md")};
			MDParser mp = new MDParser(inputs);

		} catch (FileNotFoundException e) {}

	}
}
