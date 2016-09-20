package test;

public class CloneTest {

	public static void main(String[] argv) throws CloneNotSupportedException {
		
		Chess chess = new Chess();
		chess.setId(1);
		chess.setName("foo");
		Chess clone = (Chess) Chess.copy(chess);
		clone.setId(2);
		clone.setName("foobar");
		System.out.println("done");
	}
}


class ChessVO{
	protected int id;
	protected String name;

	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
}

class Chess extends ChessVO implements Cloneable{
	
	
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }	
	
	public static Chess copy(Chess chess){
		try {
			return (Chess) chess.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return null;
	}
}