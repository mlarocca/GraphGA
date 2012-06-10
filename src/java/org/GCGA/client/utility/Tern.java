package org.GCGA.client.utility;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Eurialo
 */
public class Tern <T1 extends Object & Comparable<T1>, T2 extends Object & Comparable<T2>, T3 extends Object & Comparable<T3>> implements Comparable {
	   protected final T1 first;
	   protected final T2 second;
           protected T3 third;

	   public Tern( T1 f, T2 s, T3 t )
	   {
	    this.first = f;
	    this.second = s;
            this.third = t;
	   }

	   public T1 getFirst()
	   {
                return first;
	   }
	   public T2 getSecond()
	   {
                return second;
	   }

	   public T3 getThird()
	   {
                return third;
	   }

	   public boolean equals( Tern<T1, T2, T3> other )
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
	     && (second == null ? other.second == null : second.equals( other.second ))
             && (third == null ? other.third == null : third.equals( other.third ));
	   }

           public int compareTo(Tern<T1, T2, T3> other) throws NullPointerException, ClassCastException{
               if (other == null)
                   throw new NullPointerException();
               int res;
               if (  (res = first.compareTo(other.first) ) == 0  ){
                   if ( (res = second.compareTo(other.second)) == 0 )
                        return third.compareTo( other.third );
                   else
                       return res;
               }
               else
                   return res;
           }

            public int compareTo(Object o) {
                try{
                    Tern<T1, T2, T3> other = (Tern<T1, T2, T3>)o;
                    return compareTo(other);
                }catch(Exception e){
                    return -1;
                }
            }

	   public boolean isNull(){
		   return this.getFirst()==null && this.getSecond() == null && this.getThird() == null;
	   }
}