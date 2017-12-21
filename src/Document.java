import java.util.ArrayList;

interface MDElement{
}

class Document implements MDElement{
	public ArrayList<Structure> structures;
	public Document(){
		structures = new ArrayList<Structure>();
	}
}