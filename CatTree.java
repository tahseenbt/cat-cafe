import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import COMP250_A3_W2020.CatTree.CatNode;


public class CatTree implements Iterable<CatInfo>{
    public CatNode root;
    
    public CatTree(CatInfo c) {
        this.root = new CatNode(c);
    }
    
    private CatTree(CatNode c) {
        this.root = c;
    }
    
    
    public void addCat(CatInfo c)
    {
        this.root = root.addCat(new CatNode(c));
    }
    
    public void removeCat(CatInfo c)
    {
        this.root = root.removeCat(c);
    }
    
    public int mostSenior()
    {
        return root.mostSenior();
    }
    
    public int fluffiest() {
        return root.fluffiest();
    }
    
    public CatInfo fluffiestFromMonth(int month) {
        return root.fluffiestFromMonth(month);
    }
    
    public int hiredFromMonths(int monthMin, int monthMax) {
        return root.hiredFromMonths(monthMin, monthMax);
    }
    
    public int[] costPlanning(int nbMonths) {
        return root.costPlanning(nbMonths);
    }
    
    
    
    public Iterator<CatInfo> iterator()
    {
        return new CatTreeIterator();
    }
    
    
    class CatNode {
        
        CatInfo data;
        CatNode senior;
        CatNode same;
        CatNode junior;
        
        public CatNode(CatInfo data) {
            this.data = data;
            this.senior = null;
            this.same = null;
            this.junior = null;
        }
        
        public String toString() {
            String result = this.data.toString() + "\n";
            if (this.senior != null) {
                result += "more senior " + this.data.toString() + " :\n";
                result += this.senior.toString();
            }
            if (this.same != null) {
                result += "same seniority " + this.data.toString() + " :\n";
                result += this.same.toString();
            }
            if (this.junior != null) {
                result += "more junior " + this.data.toString() + " :\n";
                result += this.junior.toString();
            }
            return result;
        }
        
        
        public CatNode addCat(CatNode c) {
        	try {
        	if (c.data.monthHired < this.data.monthHired) {
        		if (this.senior == null) {
        			this.senior = c;
        		}
        		else {
        			this.senior.addCat(c);
        		}
        	}
        	else if (c.data.monthHired > this.data.monthHired) {
        		if (this.junior == null) {
        			this.junior = c;
        		}
        		else {
        			this.junior.addCat(c);
        		}
        	}
        	else if (c.data.monthHired == this.data.monthHired) {
        		if (c.data.furThickness <= this.data.furThickness){
        			if (this.same == null) {
        				this.same = c;
        			}
        			else {
        				this.same.addCat(c);
        			}
        		}
        		else if (this.data.furThickness < c.data.furThickness){
        			CatInfo tmp = this.data;
        			this.data = c.data;
        			c.data = tmp;
        			this.addCat(c);
        		}
        	}
            return this;
        }
        catch (Exception e) {
			return this;
		}
        }
        
        
        public CatNode removeCat(CatInfo c) {
        	try {
        	if (this.data.monthHired < c.monthHired) {
        		if (this.junior != null) {
        			this.junior = this.junior.removeCat(c);
        		}
        		
        	}
        	else if (this.data.monthHired > c.monthHired){
        		if (this.senior != null) {
        			this.senior = this.senior.removeCat(c);
        		}
        		
        	}
        	else if (!this.data.equals(c)) {
        		if (this.same != null) {
        			this.same = this.same.removeCat(c);
                }
        	}
        	else if (this.data.equals(c)){
        		if (this.same != null) {
        			this.data = this.same.data;
        			this.same = this.same.same;
            		}
        		else if (this.same == null && this.senior != null) {
        			CatNode senior = this.senior.senior;
        			CatNode junior = this.senior.junior;
        			this.same = this.senior.same;
        			this.data = this.senior.data;
        			if (junior != null) {
        				CatNode tmp = this.junior;
        				this.junior = junior;
        				junior.addCat(tmp);
        			}
        			this.senior = senior;
        		}
        	else if (this.same == null && this.senior == null && this.junior != null) {
        		this.data = this.junior.data;
        		this.senior = this.junior.senior;
        		this.same = this.junior.same;
        		this.junior = this.junior.junior;
        			}
        	else {
        			return null;
            	}
        	}
        	return this;
        	}
        	catch (Exception e) {
        		return this;
        	}
        }
        	
        		
        		
        
        
        public int mostSenior() {
        	try {
        	if (this.senior == null) {
        		return this.data.monthHired;
        	}
            return this.senior.mostSenior(); 
        	}
        	catch (Exception e) {
        		return this.data.monthHired;
        	}
        }
        
        public int fluffiest() {
        	int fluffness = this.data.furThickness;
        	try {
        	if (this.senior == null && this.junior == null) {
        		return fluffness;
        	}
        	else if (this.junior == null) {
        		if (this.senior.data.furThickness > fluffness) {
        			fluffness = this.senior.fluffiest();
        		}
        	}
        	else if (this.senior == null) {
        		if (this.junior.data.furThickness > fluffness) {
        			fluffness = this.junior.fluffiest();
        		}
        	}
        	else {
        		int j = this.junior.fluffiest();
        		int s = this.senior.fluffiest();
        		if (j > s) {
        			return j;
        	}
        		return s;
        	}
        	return fluffness;
        	}
        	catch (Exception e) {
        		return fluffness;
        	}
        }
        
        
        public int hiredFromMonths(int monthMin, int monthMax) {
        	if (monthMin > monthMax) return 0;
        	int count = 0;
        	try {
        	if (this.data.monthHired >= monthMin && this.data.monthHired <= monthMax) {
        		count ++;
        	}
        	if (this.same != null) {
        		count += this.same.hiredFromMonths(monthMin, monthMax);
        	}
        	if (this.senior != null) {
        		count += this.senior.hiredFromMonths(monthMin, monthMax);
        	}
        	if (this.junior != null) {
        		count += this.junior.hiredFromMonths(monthMin, monthMax);
        	}
            return count;
        	}
        	catch (Exception e) {
        		return count;
        	}
            
        }
        
        public CatInfo fluffiestFromMonth(int month) {
        	try {
        	if (this.data.monthHired == month) {
        		return this.data;
        	}
        	else if (this.data.monthHired > month) {
        		if (this.senior != null) {
        		return this.senior.fluffiestFromMonth(month);
        		}
        		return null;
        	}
        	else {
        		if (this.junior != null) {
        			return this.junior.fluffiestFromMonth(month);
        		}
        		return null;
        		}
        	}
        	catch (Exception e) {
        		return this.data;
        	}
        }
        
        public int[] costPlanning(int nbMonths) {
        	int arr[] = new int[nbMonths];
        	try {
        	if (nbMonths == 0) {
        		return arr;
        	}
        	CatTreeIterator cati = new CatTreeIterator();
        	for (CatInfo cat:cati.cats) {
        		if (cat.nextGroomingAppointment-243 < nbMonths && cat.nextGroomingAppointment-243 >= 0)
        			arr[cat.nextGroomingAppointment-243] += cat.expectedGroomingCost;
        	}
            return arr;
        	}
        	catch (Exception e) {
        		return arr;
        	}
		}
        
    }
    
    private class CatTreeIterator implements Iterator<CatInfo> {
        // HERE YOU CAN ADD THE FIELDS YOU NEED
        ArrayList<CatInfo> cats = new ArrayList<CatInfo>();
        int i;
        public CatTreeIterator() {
        	i = 0;
        	traverse(root);
        }
        private void traverse(CatNode node) {
        	if (node.senior != null) {
        		traverse(node.senior);
        	}
        	if (node.same != null) {
        		traverse(node.same);
        	}
        	if (node.junior != null) {
        		traverse(node.junior);
        	}
        	cats.add(node.data);
        }
        
        public CatInfo next(){
        	if (!hasNext()) {
        		throw new NoSuchElementException();
        	}
        	i += 1;
        	return cats.get(i-1);

        }
        
        public boolean hasNext() {
        	if (i < cats.size()) {
        		return true;
        	}
            return false;
        }
    }
    
}

