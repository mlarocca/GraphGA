package org.GCGA.client.utility;


/**
 *
 * @author Owner
 */
public class Pair<T1 extends Object & Comparable<T1>, T2 extends Object & Comparable<T2>> implements Comparable {
	   private final T1 first;
	   private final T2 second;

	   public Pair( T1 f, T2 s )
	   {
	    this.first = f;
	    this.second = s;
	   }

	   public T1 getFirst()
	   {
	    return first;
	   }
	   public T2 getSecond()
	   {
	    return second;
	   }

	   public boolean equals( Pair<T1, T2> other )
	   {
	    if ( this == other )
	    {
	      return true;
	    }
	    if ( other == null )
	    {
	      return false;
	    }

	    return (first == null? (other.first == null) : first.equals( other.first ))
	     && (second == null ? other.second == null : second.equals( other.second ));
	   }

           public int compareTo(Pair<T1,T2> other) throws NullPointerException, ClassCastException{
               if (other == null)
                   throw new NullPointerException();
               int res;
               if (  (res = first.compareTo(other.first) ) == 0  )
                   return second.compareTo( other.second );
               else
                   return res;
           }

            public int compareTo(Object o) {
                try{
                    Pair<T1,T2> other = (Pair<T1, T2>)o;
                    return compareTo(other);
                }catch(Exception e){
                    return -1;
                }
            }

	   public boolean isNull(){
		   return this.getFirst()==null && this.getSecond() == null;
	   }
}
