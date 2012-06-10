/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.GCGA.client;

/**
 *
 * @author Owner
 */
public class GridCoordinates  {

	   private final Integer row;
	   private final Integer column;

	   public GridCoordinates( Integer r, Integer c )
	   {
	    this.row = r;
	    this.column = c;
	   }

	   public Integer column()
	   {
	    return column;
	   }
	   public Integer row()
	   {
	    return row;
	   }

	   public boolean equals( GridCoordinates other )
	   {
                if ( this == other )
                {
                  return true;
                }
                if ( other == null )
                {
                  return false;
                }

                return ( this.row== other.row() && this.column == other.column() );
	   }

           /**
            * @return :
            * 1  <=> this > other
            * 0  <=> this == other
            * -1 <=> this < other
            */
           public int compare(GridCoordinates other){
               if (other.isNull())
                   return 1;
               if (this.row != other.row() )
                   return this.row - other.row();
               else
                   return this.column - other.column();
           }

	   public boolean isNull(){
		   return this.row==null && this.column == null;
	   }


}
