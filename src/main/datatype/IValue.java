package main.datatype;

//The type IValue allows us to work with bigger numbers.
//It consists out of a POSITIVE double d and an integer i and represents d x 10^i


public class IValue {
    
    private Double d;
    private Integer i;
    
    public IValue(Double d, Integer i) {
        this.d = d;
        this.i = i;
        if(d<0){
        	System.out.println("Negative d");
        }
        this.assimilate();
    }
    
    public Boolean isZero(){
    	return(this.d == 0.0);
    }
    
    
    //Division by a double
    public IValue div(double x) {  
        
    	this.assimilate();
    	
    	if(x==0.0){
    		System.out.println("Dividing by 0");
    	}else if(x<0.0) {
    		System.out.println("Dividing by negative value");
    	}
    	
    	this.d = d/x;
    	//if the result of that division is smaller than 1, we change i and d until we have a more convenient representation
        while(d < 1 && d > 0.0) {
            this.d = this.d * 10;
            i--;
        }
        this.assimilate();
        return this;
    }
    
    //IValue Addition
    public IValue add(IValue v) {
    	IValue max, min;
    	
    	this.assimilate();
    	v.assimilate();
    	
    	//Determine which IValue has the bigger i
    	if(this.i >= v.i) {
    		max = this;
    		min = v;
    	}else{
    		max = v;
    		min = this;
    	}
    	if(min.getDouble()==0.0){
    		return max;
    	}
    	//Difference between the is
    	int dif = Math.abs(max.i - min.i);
    	//Express smaller value with the same i
   	   	for(int i = 0; i < dif; i++) {
	    	min.d = min.d / 10;
   	   	}
    	//add ds as they are now in the same base i
    	this.d = max.d + min.d;
    	this.i = max.i;
    	
    	this.assimilate();
    	return this;
    }
    
    //IValue Subtraction ATTENTION: does NOT return negative values
    //IMPORTANT: Does expect IValues in ASSIMILATED FORMAT
    public IValue sub(IValue v){    
    	this.assimilate();
    	v.assimilate();
    	
    	int dif = Math.abs(this.i - v.i);
    	//if v > i						- NOTE: check if isHigher Function would be better
    	if(this.i > v.i) {
    		for(int i = 0; i < dif; i++){
        		v.d = v.d / 10;
        	}
    		this.d = this.d - v.d;
    	} else {
    		this.d = 0.0;
    		this.i = 0;
    	}
    	this.assimilate();
    	return this;
    }
    
    
    // IValue Multiplication: adding is (as e.g (10^2) * (10^1) = 10^(2+1), multiplying ds
    public IValue mult(IValue v) {
    	
    	this.assimilate();
    	v.assimilate();
    	
    	this.i = this.i + v.i;
    	this.d = this.d * v.d;
    	this.assimilate();
    	return this;
    }
    
    // IValue Division: analogous to Multiplication
    public IValue frac(IValue v) {
    	
    	this.assimilate();
    	v.assimilate();
    	
    	this.i = this.i - v.i;
    	this.d = this.d / v.d;
    	this.assimilate();
    	return this;
    }
    
    //formats an IValue to one digit before the dot - if possible
    public void assimilate() {
    	
    	while(this.d >= 10.0){
    		if(this.i == Integer.MAX_VALUE){
    			System.out.println("Max int");
    			break;
    		}
    		this.d = this.d / 10.0;
    		this.i++;
    	}
    	
    	while(this.d < 1.0 && this.d > 0.0){
    		if(this.i == Integer.MIN_VALUE){
    			System.out.println("Min int");
    			break;
    		}
    		this.d = this.d * 10;
    		this.i--;
    	}
    	
    }

    //compares two IValues
    public boolean isHigher(IValue c) {
    	
    	this.assimilate();
    	c.assimilate();
    	
        if(this.i > c.i) {
            return true;
        } else if(this.i < c.i) {
            return false;
        } else {
            return (this.d > c.d);
        }
    }
        
    public Double getDouble() {
        return this.d;
    }

    public Integer getInteger() {
        return this.i;
    }
    
    public void set(double d, int i) {
    	this.d = d;
    	this.i = i;
    	this.assimilate();
    }
    
    public double parseToDouble() {
    	IValue v = this;
    	while(v.i != 0) {
    		
    		if(i > 0) {
    			if(d == Double.MAX_VALUE || i == Integer.MIN_VALUE){ break;}
    			v.d = v.d / 10;
    			v.i--;
    		} else {
    			if(d == Double.MIN_VALUE || i == Integer.MAX_VALUE){ break;}
    			v.d = v.d * 10;
    			v.i++;
    		}
    	}
    	
    	return v.d; 
    }
    
    //Division by a double
    public static IValue div(IValue ix, int x) {  
        
    	IValue iv = new IValue(ix.getDouble(), ix.getInteger());
    	iv.assimilate();
    	
    	if(x==0.0){
    		System.out.println("Dividing by 0");
    	}
    	
    	iv.d = iv.d/x;
    	//if the result of that division is smaller than 1, we change i and d until we have a more convenient representation
        while(iv.d < 1 && iv.d > 0.0) {
            iv.d = iv.d * 10;
            iv.i--;
        }
        iv.assimilate();
        return iv;
    }
    
   
    
    public static IValue add(IValue ix1, IValue ix2) {
    	
    	IValue v1 = new IValue(ix1.getDouble(), ix1.getInteger());
    	IValue v2 = new IValue(ix2.getDouble(), ix2.getInteger());
    	
    	v1.assimilate();
    	v2.assimilate();
    	
    	IValue max;
    	IValue min;
    	
    	if(v1.i >= v2.i) {
    		max = v1;
    		min = v2;
    	}else {
    		max = v2;
    		min = v1;
    	}
    	
    	int dif = Math.abs(max.i - min.i);
    	
    	for(int i = 0; i < dif; i++) {
    		max.d = max.d / 10;
    	}
    	v1.d = max.d + min.d;
    	v1.i = min.i;
    	v1.assimilate();
    	return v1;
    }
    
    
    public static IValue sub(IValue ix1, IValue ix2) {
    	
    	IValue v1 = new IValue(ix1.getDouble(), ix1.getInteger());
    	IValue v2 = new IValue(ix2.getDouble(), ix2.getInteger());

    	v1.assimilate();
    	v2.assimilate();
    	
    	int dif = Math.abs(v1.i - v2.i);
    	
    	if(v1.i > v2.i) {
    		for(int i = 0; i < dif; i++) {
        		v2.d = v2.d / 10;
        	}
    		v1.d = v1.d - v2.d;
    	} else {
    		v1.d = 0.0;
    		v1.i = 0;
    	}
    	v1.assimilate();
    	return v1;
    }
    
    
    public static IValue mult(IValue ix1, IValue ix2) {
    	
    	IValue v1 = new IValue(ix1.getDouble(), ix1.getInteger());
    	IValue v2 = new IValue(ix2.getDouble(), ix2.getInteger());
    	
    	v1.assimilate();
    	v2.assimilate();
    	
    	v1.i = v1.i + v2.i;
    	v1.d = v1.d * v2.d;
    	
    	v1.assimilate();
    	return v1;
    }
    
    
    public static IValue assimilate(IValue v) {
    	IValue iv = new IValue(v.getDouble(), v.getInteger());
    	
    	while(iv.d >= 10.0){
    		if(iv.i == Integer.MAX_VALUE){
    			System.out.println("Max int");
    			break;
    		}
    		iv.d = iv.d / 10.0;
    		iv.i++;
    	}
    	while(iv.d < 1.0 && iv.d > 0.0){
    		if(iv.i == Integer.MIN_VALUE){
    			System.out.println("Min int");
    			break;
    		}
    		iv.d = iv.d * 10;
    		iv.i--;
    	}
    	return iv;
    }
    

    public String toString() {    	
    	return this.d + "/" + this.i;    	
    }
}
