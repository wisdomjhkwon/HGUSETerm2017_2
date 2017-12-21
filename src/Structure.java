import java.util.ArrayList;

class Structure implements MDElement{
	public ArrayList<String> lines;
	public Structure(ArrayList<String> lines){
		this.lines = (ArrayList<String>)lines.clone();
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