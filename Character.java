import java.util.ArrayList;
import java.io.Serializable;

public class Character implements Comparable, Serializable{
	private enum HealthBar{
		Healthy,
		Winded,
		Wounded,
		Broken
	}
	private int AC;
	private int maxHP;
	private int curHP;
	private String name;
	private int init;
	private ArrayList<String> statuses;
	
	public Character(){}
	
	public Character(int max, int cur, String name, int init){
		//this.AC = AC;
		this.curHP = cur;
		this.maxHP = max;
		this.name = name;
		this.init = init;
		this.statuses = new ArrayList<String>();
	}
	
	public int getAC(){
		return this.AC;
	}
	public void setAC(int val){
		this.AC = val;
	}
	
	public int getMaxHP(){
		return this.maxHP;
	}
	public void setMaxHP(int val){
		this.maxHP = val;
	}
	
	public int getCurHP(){
		return this.curHP;
	}
	public void setCurHP(int val){
		this.curHP = val;
	}
	
	public int getInit(){
		return this.init;
	}
	public void setInit(int val){
		this.init = val;
	}
	
	public String getName(){
		return this.name;
	}
	public void setName(String val){
		this.name = val;
	}
	
	public ArrayList<String> getStatus(){
		return this.statuses;
	}
	public void setStatus(String status){
		this.statuses.add(status);
	}
	@Override
    public int compareTo(Object comp) {
        int compInit=((Character)comp).getInit();
        return this.init-compInit;

        /* For Descending order do like this */
        //return compareage-this.studentage;
    }

    @Override
    public String toString() {
        return this.name;
    }
}