package Models;


import java.util.ArrayList;

public class Location {
	
	private int a; 
	private int b;
	private int t;
	private ArrayList<Integer> dist;
	private int id;
	
	public int getA() {
		return a;
	}

	public void setA(int a) {
		this.a = a;
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = b;
	}

	public int getT() {
		return t;
	}

	public void setT(int t) {
		this.t = t;
	}

	public ArrayList<Integer> getDist() {
		return dist;
	}

	public void setDist(ArrayList<Integer> dist) {
		this.dist = dist;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Location(){
		
	}
	
	public Location(int id, int a, int b, int t){
		this.id = id;
		this.a = a;
		this.b = b;
		this.t = t;
		dist = new ArrayList<>();
	}



	public Location(int a, int b, int tValue, ArrayList<Integer> dist) {
		super();
		this.a = a;
		this.b = b;
		this.t = tValue;
		this.dist = dist;
	}

	public Location(int id) {
		this.id = id;
		this.a = 0;
		this.b = 0;
		this.t = 0;
		this.dist = new ArrayList<>();
		
	}

	@Override
	public String toString() {
		
		return "ID: " + this.id + " & min value: " + this.a; 
	}
	
	
}