import static org.junit.Assert.*;

import org.junit.Test;

public class md2htmlTest {

	@Test
	public void test1() {
		String[] args = {"samples/doc1.md"};
		md2html.main(args);
	}
	
	@Test
	public void test2() {
		String[] args = {"samples/doc2.md"};
		md2html.main(args);
	}
	
	@Test
	public void test3() {
		String[] args = {"samples/doc3.md"};
		md2html.main(args);
	}
	
	@Test
	public void test4() {
		String[] args = {"samples/doc4.md"};
		md2html.main(args);
	}

}
