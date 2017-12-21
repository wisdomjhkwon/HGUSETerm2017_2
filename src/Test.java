class Test{
	public static void main(String[] args) {
			
		MDParserTest mdTest = new MDParserTest();
		mdTest.testLineAnalysis();
		mdTest.test1();
		
		CommandInterpreterTest ciTest = new CommandInterpreterTest();
		ciTest.testGetFormat();
		ciTest.test1();
		ciTest.test2();
		ciTest.test3();
		ciTest.test4();
		ciTest.test5();
		ciTest.test6();
		ciTest.test7();
		ciTest.test8();
		ciTest.test9();
		ciTest.test10();
		ciTest.test11();
		ciTest.test12();
		ciTest.test13();
		ciTest.test14();
		ciTest.test15();
		
		md2htmlTest mainTest = new md2htmlTest();
		mainTest.test1();	
		mainTest.test4();
		mainTest.test2();				
		mainTest.test3();			
	}
}