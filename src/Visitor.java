import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

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

class StyleVisitor implements MDElementVisitor {
    public String htmlFilePath;
    StyleVisitor(String path){
        this.htmlFilePath = path;
        
        
        try{
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            
            String styleSetting ="<html>    <head>    <style>"
            + "p {color: gray;    background-color: yellow;} h1 {color: green;}"
            + "h2 {color: red;}   h3 {color: blue;}   h4 {color: gray;}   h5 {color: purple;}   h6 {color: pink;}"
            + "</style> </head> <body>";
            
            
            pw.write(styleSetting);
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
    }
    
    public void visitDocument(Document d){
        
        try{
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath));
            PrintWriter pw = new PrintWriter(bw);
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        System.out.println("struct size === "+d.structures.size());
        
        int i;
        for(i=0; i < d.structures.size() ;i++) {
            
            //for test
            System.out.println("visitStruct("+i+")");
            
            visitStructure( d.structures.get(i) );
            
            
        }
        
        try{
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            
            String endstr ="</body> </html>";
            pw.write(endstr);
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
    }

    
    public void visitStructure(Structure s) {
        System.out.println("overload");
        if(s instanceof Header) {            visitStructure((Header)s);        }
        else if (s instanceof Block) {            visitStructure((Block)s);         }
        else if (s instanceof QuotedBlock) {            visitStructure((QuotedBlock)s);        }
        else if (s instanceof ItemList) {            visitStructure((ItemList)s);        }
        else if (s instanceof Horizontal) {        visitStructure((Horizontal)s);    }
        else if (s instanceof LinkReference) {        visitStructure((LinkReference)s);    }
        else {    System.out.println("error");        }
    }
    
    public String IB(String str)
    {
        int part1 = 0;
        int part2 = 0;
        int check = 0;
        int first = 0;
        for(int i = 0;i<str.length();i++)
        {
            if(str.charAt(i) == '*')
            {
                if(first == 0)
                {
                    first++;
                    part1 = i;
                }
                else
                {
                    part2 = i;
                }
                check++;
            }
        }
        if(check == 2)
        {
            str = str.substring(0, part1-1) + "<i>" + str.substring(part1+1,part2-1) + "</i>" + str.substring(part2+1,str.length());
        }
        if(check == 4)
        {
            str = str.substring(0, part1-1) + "<b>" + str.substring(part1+2,part2-1) + "</b>" + str.substring(part2+2,str.length());
        }
        str = LK(str);
        return str;
    }
    public String IM(String str)
    {
        return str;
    }
    public String LK(String str)
    {
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int first = 0;
        for(int i = 0;i < str.length();i++)
        {
            if(str.charAt(i)=='[')
            {
                one = i;
            }
            if(str.charAt(i)==']')
            {
                two = i;
            }
            if(str.charAt(i)=='(')
            {
                three = i;
            }
            if(str.charAt(i)==')')
            {
                four = i;
            }
        }
        if(two + 1== three&&one < two&&two<three&&three<four)
        {
            for(int i = three; i < four; i++)
            {
                if(str.charAt(i)=='"'&&first == 0)
                {
                    first++;
                    five = i;
                }
                else if(str.charAt(i) == '"')
                {
                    six = i;
                }
            }
            str = str.substring(0,one) + "<a href=\"" + str.substring(three + 1, five - 1) + "\"" + "title=\"" + str.substring(five + 1, six - 1) + "\">"
            + str.substring(one + 1, two) + "</a>" + str.substring(four+1,str.length());
        }
        
        return str;
    }
    
    
    
    public void visitStructure(Header s) {
        
        
        //for test
        System.out.println("        visitStructure(Header)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        
        
        
        int h_idx = 0;
        int first = 0;
        
        String str = "";
        if(s.lines.size() == 1)
        {
            for(int i=0;i<s.lines.get(0).length();i++)
            {
                if(s.lines.get(0).charAt(i) != '#')
                {
                    if(first == 0)
                    {
                        h_idx = i;
                        first++;
                    }
                    str = str + s.lines.get(0).charAt(i);
                }
                
            }
        }
        else
        {
            if(s.lines.get(1).charAt(0) == '=')
            {
                h_idx = 1;
                str = s.lines.get(0);
            }
            else if(s.lines.get(1).charAt(0) == '-')
            {
                h_idx = 2;
                str = s.lines.get(0);
            }
        }
        first = 0;
        String str_ = IB(str);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            
            if(h_idx == 1) {
                pw.write("<h1>");
                pw.write(str_/*s.getString()*/);
                pw.write("</h1>");
                pw.write("\n");
            } else if (h_idx == 2) {
                pw.write("<h2>");
                pw.write(str_/*s.getString()*/);
                pw.write("</h2>");
                pw.write("\n");
            } else if (h_idx == 3) {
                pw.write("<h3>");
                pw.write(str_/*s.getString()*/);
                pw.write("</h3>");
                pw.write("\n");
            } else if (h_idx == 4) {
                pw.write("<h4>");
                pw.write(str_/*s.getString()*/);
                pw.write("</h4>");
                pw.write("\n");
            } else if (h_idx == 5) {
                pw.write("<h5>");
                pw.write(str_/*s.getString()*/);
                pw.write("</h5>");
                pw.write("\n");
            } else if (h_idx == 6) {
                pw.write("<h6>");
                pw.write(str_/*s.getString()*/);
                pw.write("</h6>");
                pw.write("\n");
            }
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
        
        
    }
    public void visitStructure(Block s) {
        //for test
        System.out.println("        visitStructure(Block)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        ///////////////////////////////
        
        
        
        
        
        
        
        
        String str1 = "";
        int lineNum1 = 0;

        String com1 = "";
        String com2 = "";
        int ch1 = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            for(int i = 0; i < s.lines.size() ; i++)
            {
                if(i < s.lines.size() - 1)
                {
                    str1 = str1 + s.lines.get(i) + "\n";
                }
                else
                {
                    str1 = str1 + s.lines.get(i) + "\0";
                }
                
            }
            str1 = "<p>" + str1 + "<br></p>";
            str1 = IB(str1);
            pw.write(str1);
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        
    }
    public void visitStructure(QuotedBlock s) {
        //for test
        System.out.println("        visitStructure(QuotedBlock)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        ///////////////////
        
        
        
        
        
        try {
            String str = "";
            for (int i = 0; i < s.lines.size(); i++) {
                str = str + s.lines.get(i);
            }
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true));
            PrintWriter pw = new PrintWriter(bw,true);
            
            pw.write("<article class=\"markdown-body entry-content\" itemprop=\"text\"><blockquote>\n" + "<p>");
            pw.write(str);
            pw.write("</p>\n" + "</blockquote>\n" + "</article>");
            pw.write("\n");
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        
    }
    public void visitStructure(ItemList s) {
        //for test
        System.out.println("        visitStructure(ItemList)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        
        String str = "";
        int lineNum = 0;
        String com1 = "";
        String com2 = "";
        String com3 = "";
        int firstLine = 0;
        int first = 0;
        int first1 = 0;
        int ch = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw, true);

            for (int i = 0; i < s.lines.size(); i++) {
                    str = str + s.lines.get(i);
            }
            System.out.println(str);
            if(str.indexOf('+')!=-1)
            {
                str = str.replace('+', '*');
            }
            if(str.indexOf('-')!=-1)
            {
                str = str.replace('-', '*');
            }

            for(int i = 0; i<str.length();i++)
            {
                if(str.charAt(i) == '*')
                {
                    if(str.indexOf('*') == 0)
                    {
                        str = "<ul><li>" + str.substring(str.charAt(i), str.length());
                        first= str.indexOf('*');
                    }
                    if(str.indexOf('*') > 0)
                    {
                        if(str.charAt(str.indexOf('*')-1) == ' '&&i < str.length()-1)
                        {
                            str =  str.substring(0, str.indexOf('*') - 1) + "<ul><li>" + str.substring(str.indexOf('*') + 1);
                            first1++;
                            if(str.indexOf('*') != -1)
                            {
                                first= str.indexOf('*');
                            }

                        }
                        if(str.charAt(str.indexOf('*')-1) != ' '&&i < str.length()-1)
                        {
                            str =  str.substring(0, str.indexOf('*') - 1) + "</li><li>" + str.substring(str.indexOf('*') + 1);
                            if(str.indexOf('*') != -1)
                            {
                                first= str.indexOf('*');
                            }
                        }
                    }
                    if(i == str.length() - 2&&first1>0)
                    {
                        str = str.substring(0,first) + "</li></ul></li></ul>" + str.substring(first);
                    }
                    if(i == str.length() - 2&&first1 == 0)
                    {
                        str = str.substring(0,first) + "</li></ul>" + str.substring(first);
                    }




                }




            }



            pw.write(str);


            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e);
            System.exit(1);
        }
    }
    public void visitStructure(Horizontal s) {
        
        //for test
        System.out.println("        visitStructure(Horizontal)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        ///////////////////////////
        
        
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw, true);
            pw.write("<hr>");
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        //for test
        System.out.println("      visitStructure(Horizontal)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        
    }
    public void visitStructure(LinkReference s) {
        
        //for test
        System.out.println("        visitStructure(LinkReference)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
    }
}


class PlainVisitor implements MDElementVisitor {
    public String htmlFilePath;
    PlainVisitor(String path){
        this.htmlFilePath = path;
    }
    
    
    public void visitDocument(Document d){
        try{
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath));
            PrintWriter pw = new PrintWriter(bw);
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        System.out.println("struct size === "+d.structures.size());
        
        int i;
        for(i=0; i < d.structures.size() ;i++) {
            
            //for test
            System.out.println("visitStruct("+i+")");
            
            visitStructure( d.structures.get(i) );
        }
        
        try{
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            
            String endstr ="</body> </html>";
            pw.write(endstr);
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
    }
    
    
    public void visitStructure(Structure s) {
        System.out.println("overload");
        if(s instanceof Header) {            visitStructure((Header)s);        }
        else if (s instanceof Block) {            visitStructure((Block)s);         }
        else if (s instanceof QuotedBlock) {            visitStructure((QuotedBlock)s);        }
        else if (s instanceof ItemList) {            visitStructure((ItemList)s);        }
        else if (s instanceof Horizontal) {        visitStructure((Horizontal)s);    }
        else if (s instanceof LinkReference) {        visitStructure((LinkReference)s);    }
        else {    System.out.println("error");        }
    }
    
    public String IB(String str)
    {
        int part1 = 0;
        int part2 = 0;
        int check = 0;
        int first = 0;
        for(int i = 0;i<str.length();i++)
        {
            if(str.charAt(i) == '*')
            {
                if(first == 0)
                {
                    first++;
                    part1 = i;
                }
                else
                {
                    part2 = i;
                }
                check++;
            }
        }
        if(check == 2)
        {
            str = str.substring(0, part1-1) + "<i>" + str.substring(part1+1,part2-1) + "</i>" + str.substring(part2+1,str.length());
        }
        if(check == 4)
        {
            str = str.substring(0, part1-1) + "<b>" + str.substring(part1+2,part2-1) + "</b>" + str.substring(part2+2,str.length());
        }
        str = LK(str);
        return str;
    }
    public String IM(String str)
    {
        return str;
    }
    public String LK(String str)
    {
        int one = 0;
        int two = 0;
        int three = 0;
        int four = 0;
        int five = 0;
        int six = 0;
        int first = 0;
        for(int i = 0;i < str.length();i++)
        {
            if(str.charAt(i)=='[')
            {
                one = i;
            }
            if(str.charAt(i)==']')
            {
                two = i;
            }
            if(str.charAt(i)=='(')
            {
                three = i;
            }
            if(str.charAt(i)==')')
            {
                four = i;
            }
        }
        if(two + 1== three&&one < two&&two<three&&three<four)
        {
            for(int i = three; i < four; i++)
            {
                if(str.charAt(i)=='"'&&first == 0)
                {
                    first++;
                    five = i;
                }
                else if(str.charAt(i) == '"')
                {
                    six = i;
                }
            }
            str = str.substring(0,one) + "<a href=\"" + str.substring(three + 1, five - 1) + "\"" + "title=\"" + str.substring(five + 1, six - 1) + "\">"
            + str.substring(one + 1, two) + "</a>" + str.substring(four+1,str.length());
        }
        
        return str;
    }
    
    
    
    public void visitStructure(Header s) {

        
        //for test
        System.out.println("        visitStructure(Header)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        /////////////////////
        
        
        
        int h_idx = 0;
        int first = 0;
        
        String str = "";
        if(s.lines.size() == 1)
        {
            for(int i=0;i<s.lines.get(0).length();i++)
            {
                if(s.lines.get(0).charAt(i) != '#')
                {
                    if(first == 0)
                    {
                        h_idx = i;
                        first++;
                    }
                    str = str + s.lines.get(0).charAt(i);
                }
                
            }
        }
        else
        {
            if(s.lines.get(1).charAt(0) == '=')
            {
                h_idx = 1;
                str = s.lines.get(0);
            }
            else if(s.lines.get(1).charAt(0) == '-')
            {
                h_idx = 2;
                str = s.lines.get(0);
            }
        }
        first = 0;
        str = IB(str);
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            
            if(h_idx == 1) {
                pw.write("<h1>");
                pw.write(str/*s.getString()*/);
                pw.write("</h1>");
                pw.write("\n");
            } else if (h_idx == 2) {
                pw.write("<h2>");
                pw.write(str/*s.getString()*/);
                pw.write("</h2>");
                pw.write("\n");
            } else if (h_idx == 3) {
                pw.write("<h3>");
                pw.write(str/*s.getString()*/);
                pw.write("</h3>");
                pw.write("\n");
            } else if (h_idx == 4) {
                pw.write("<h4>");
                pw.write(str/*s.getString()*/);
                pw.write("</h4>");
                pw.write("\n");
            } else if (h_idx == 5) {
                pw.write("<h5>");
                pw.write(str/*s.getString()*/);
                pw.write("</h5>");
                pw.write("\n");
            } else if (h_idx == 6) {
                pw.write("<h6>");
                pw.write(str/*s.getString()*/);
                pw.write("</h6>");
                pw.write("\n");
            }
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        
    }
    public void visitStructure(Block s) {
        //for test
        System.out.println("        visitStructure(Block)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        ///////////////////////////////
        
        
        
        
        
        
        
        
        String str1 = "";
        int lineNum1 = 0;

        String com1 = "";
        String com2 = "";
        int ch1 = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            for(int i = 0; i < s.lines.size() ; i++)
            {
                if(i < s.lines.size() - 1)
                {
                    str1 = str1 + s.lines.get(i) + "\n";
                }
                else
                {
                    str1 = str1 + s.lines.get(i) + "\0";
                }
                
            }
            str1 = "<p>" + str1 + "<br></p>";
            str1 = IB(str1);
            pw.write(str1);
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        
    }
    public void visitStructure(QuotedBlock s) {
        //for test
        System.out.println("        visitStructure(QuotedBlock)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        ///////////////////
        
        
        
        
        
        try {
            String str = "";
            for (int i = 0; i < s.lines.size(); i++) {
                str = str + s.lines.get(i);
            }
            
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw,true);
            
            pw.write("<article class=\"markdown-body entry-content\" itemprop=\"text\"><blockquote>\n" + "<p>");
            pw.write(str);
            pw.write("</p>\n" + "</blockquote>\n" + "</article>");
            pw.write("\n");
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        
    }
    public void visitStructure(ItemList s) {
        //for test
        System.out.println("        visitStructure(ItemList)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        /////////////////////////////
        
        String str = "";
        int lineNum = 0;

        String com1 = "";
        String com2 = "";
        String com3 = "";
        int firstLine = 0;
        int first = 0;
        int ch = 0;
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw, true);
            for (int i = 0; i < s.lines.size(); i++) {
                str = str + s.lines.get(i);
            }
            if(str.indexOf('+')!=-1)
            {
                str = str.replace('+', '*');
            }
            if(str.indexOf('-')!=-1)
            {
                str = str.replace('-', '*');
            }
            
            for(int i = 0; i<str.length();i++)
            {
                if(str.charAt(i) == '*')
                {
                    if(str.charAt(i) == 0)
                    {
                        str = "<ul><li>" + str.substring(str.charAt(i + 1), str.length());
                    }
                    if(str.charAt(i) > 0)
                    {
                    	if(i>0){
	                        if(str.charAt(i-1) == ' '&&str.charAt(i) == '*')
	                        {
	                            str = "<ul><li>" + str.substring(str.charAt(i + 1), str.length());
	                            first++;
	                        }
	                        if(str.charAt(i-1) != ' '&&str.charAt(i) == '*')
	                        {
	                            str = "</li><li>" + str.substring(str.charAt(i + 1), str.length());
	                        }
                    	}
                    }
                    if(i == str.length() - 1)
                    {
                        
                    }
                    
                }
            }
            
            
            pw.write(str);
            
            
            
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
    }
    public void visitStructure(Horizontal s) {
        
        //for test
        System.out.println("        visitStructure(Horizontal)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        ///////////////////////////
        
        
        
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(htmlFilePath, true)); 
            PrintWriter pw = new PrintWriter(bw, true);
            pw.write("<hr>");
            pw.close();
            bw.close();
            
        } catch (IOException e) {
            System.err.println(e); 
            System.exit(1);
        }
        
        //for test
        System.out.println("      visitStructure(Horizontal)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
        
    }
    public void visitStructure(LinkReference s) {
        
        //for test
        System.out.println("        visitStructure(LinkReference)");
        
        for(int k=0; k<s.lines.size(); k++){
            System.out.println("===>> "+s.lines.get(k));
        }
        System.out.println();
    }
    
}