import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

class MDParser{
	public ArrayList<String> bufferedLine;
	public ArrayList<Document> mdFiles;
	public MDParser(){}
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
		if(words[0].length() !=0 && words[0].charAt(0) == '>'){
			return 4;
		}
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
}