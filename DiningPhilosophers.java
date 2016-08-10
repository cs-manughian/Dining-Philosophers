import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.ReentrantLock;

public class DiningPhilosophers {

	public static class Chopstick {
		
		   // A ReentrantLock is owned by the thread last successfully locking,
		   // but not yet unlocking it.
		   private final ReentrantLock lockPickedUp = new ReentrantLock();
		   private final int num;

		   public Chopstick( int num ) {
			   this.num = num;
		   }
		   
		   @Override
		    public String toString() {
		      return "chopstick " + num;
		   }
		    
		   public boolean pickUpChopstick( Philosopher p ) throws InterruptedException {
			   
			     // Throws when a thread is waiting, sleeping, or 
			     // otherwise occupied, and the thread is interrupted, 
			     // either before or during the activity.
			     
			     // Acquires the lock only if it is not held by another 
			     // thread at the time of invocation.
			   	 return lockPickedUp.tryLock();
		   }
		   
		   public void putDownChopstick( Philosopher p ) {
			   
			   	 // Attempts to release this lock.
			   	 lockPickedUp.unlock();
		   }
		   
	}// end Chopstick class
	
	
	public static class Philosopher implements Runnable  {
		
	       private final Chopstick lchopstick;
	       private final Chopstick rchopstick;
		   private final int num;
		   private int turnsToEat = 0;
		   
		   public Philosopher(Chopstick l, Chopstick r, int num) {
			   this.lchopstick = l;
			   this.rchopstick = r;
			   this.num = num;
		   }
		   
		   @Override
		    public String toString() {
		      return "philosopher " + num;
		   }
		   
		   private void think() throws InterruptedException {
		        
		        // Sleep for a [random] while
				// The random number generator is isolated to the 
		        // current thread. It returns a pseudorandom, uniformly 
		        // distributed integer value between 0 (inclusive)
		        // and param (exclusive)
		        Thread.sleep( ThreadLocalRandom.current().nextInt(50) );
		   }

		   private void eat() throws InterruptedException {
			    turnsToEat++;
		        //System.out.println(this + " is eating for the "+turnsToEat+" time!");
		        Thread.sleep( ThreadLocalRandom.current().nextInt(50) );
		   }
		
		   @Override
		   public void run() {
			   
			   try {
				    
				   // Do it this many times
				   for(int i = 0; i < 1000; i++) {
					   
				    	// Think for a random time
					   think();
					   
					   // Then try pick up chopsticks and eat
					   // if the chopsticks aren't locked
				       if(lchopstick.pickUpChopstick(this)) {
				           if (rchopstick.pickUpChopstick(this)) {
				              eat();
				              rchopstick.putDownChopstick(this);
				            }
				           lchopstick.putDownChopstick(this);
				       }
				   }
				   
			   }catch(Exception e){
				   e.printStackTrace(); 
				   
			   }finally{
				   
				   System.out.println(this+" ate "+turnsToEat+" times total.");
			   }
			
		   }
		
	}// end Philosopher class
	
	public static void main(String args[]) throws InterruptedException {

		   int num = 10;

		   Chopstick chopstick[] = new Chopstick[num];
		   Philosopher philosopher[] = new Philosopher[num];
		   
		   // Initialize 5 chopsticks
		   for (int i = 0; i < num; i++) 
			   chopstick[i] = new Chopstick(i);
			   
		   // If I initialize 5 philosophers,
		   // Must initialize 0th philoshoper first.
		   // The chopstick/philosopher numbers go 
		   // 0 to 4 in a counter clockwise manner.
		   philosopher[0] = new Philosopher(chopstick[num-1], chopstick[0], 0);
		   
		   for (int j = 1; j < num; j++)   
			   philosopher[j] = new Philosopher(chopstick[j-1], chopstick[j], j);

		   // Start the threads
		   for (int k = 0; k < num; k++) 
			   new Thread(philosopher[k]).start();
			   
	    }
	
}

