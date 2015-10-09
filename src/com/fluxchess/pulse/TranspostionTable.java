package com.fluxchess.pulse;

public class TranspostionTable {
	
	private TranspostionTableEntity[] hashTable;
	final int  defaultTranspostionTableSizeMB = 64;
	final int entrySizeByte = 40;
	private int hashSize;
	private int currentAge;
	
	/*public static void main(String [] args){
		Runtime runtime = Runtime.getRuntime();
		Log.log("init - " + (runtime.totalMemory() - runtime.freeMemory()));
		TranspostionTable t = new TranspostionTable();
		
		Log.log("init - " + (runtime.totalMemory() - runtime.freeMemory()));
		int count = 0;
		while(count < 1000000){
			t.putEntity(count,count,count,count,count);
			count ++;
		}
		Log.log("final - " + (runtime.totalMemory() - runtime.freeMemory()));
	}*/
	
	
	class Bound{
		final static int EXACT = 0;
		final static int LOWER = 1;
		final static int UPPER = 2;
	}
	
	class TranspostionTableEntity{
		long currentZobristkey;
		int currentDepth;
		int currentValue;
		int currentBounded;
		int currentAge;
		TranspostionTableEntity(long zobristkey, int value, int depth,int bounded,int age){
			
			currentZobristkey = zobristkey;
			currentValue = value;
			currentDepth = depth;
			currentBounded = bounded;
			currentAge = age;
		}
		
		int getValue(){
			return this.currentValue;
		}
		
		int getDepth(){
			return this.currentDepth;
		}
		
		int getBounded(){
			return currentBounded;
		}
		
		int getAge(){
			return currentAge;
		}
		
		long getZobristkey(){
			return this.currentZobristkey;
		}
		
		boolean isEquals(TranspostionTableEntity targetEntity){
			return targetEntity.currentZobristkey == this.currentZobristkey;
		}
		
		
	}

	
	
	TranspostionTable(){
		hashSize = this.defaultTranspostionTableSizeMB*1024*1024/this.entrySizeByte;
		this.hashTable = new TranspostionTableEntity[hashSize]; //new HashMap<Long,TranspostionTableEntity>(hashSize,1);
		currentAge = 0;
	}
	
	void setAge(int age){
		this.currentAge = age;
	}
	
	boolean containsKey(int key){
		return hashTable[key] != null;
	}
	
	TranspostionTableEntity get(int key){
		return hashTable[key];
	}
	
	void put(int key,TranspostionTableEntity entity){
		hashTable[key] = entity;
	}
	
	TranspostionTableEntity getEntity(Position position,int depth,int age){
		if(age > currentAge)
			return null;
		int key = zobristKeyToHashKey(position.zobristKey);
		
		if (!containsKey(key)){
			return null;
		}
		TranspostionTableEntity currentEntity = this.get(key);
		if(currentEntity.getAge() != age)
			return null;
		if(currentEntity.getDepth() < depth){
			return null;
		}
		if(currentEntity.getZobristkey() != position.zobristKey){
			return null;
		}else{
			return currentEntity;
		}
	}
	
	int zobristKeyToHashKey(long zobristKey){
		return Math.abs((int)zobristKey%hashSize);
	}
	
	boolean putEntity(long zobristkey, int value, int depth,int bounded,int age){
		if(age < currentAge)
			return false;
		int key = zobristKeyToHashKey(zobristkey);
		if(this.containsKey(key)){
			TranspostionTableEntity storedEntity = this.get(key);
			if(storedEntity.getAge() > age || storedEntity.getDepth() > depth){
				return false;
			}/*else if(storedEntity.getZobristkey() == zobristkey){
				return false;
			}*/ 
			//always replace approach
		}
		this.put(key, new TranspostionTableEntity(zobristkey,value,depth,bounded,age));
		return true;
	}
	
}
