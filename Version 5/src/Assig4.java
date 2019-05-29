/*
 * Title:   Module 4 Assignment - Optical Barcode Readers and Writers
 * Author:  Dalia Faria
 * Status:  Group project, not final version        
 * Date:    May 28, 2019
 */


/* Purpose: Main class where the objects get initialized.
 * Object methods to invoke reading in the image/text, manipulate the image
 * and display the image/text are called here.
 * White spaces/elements are translated to false.
 * Black elements are translated to true.
 */
public class Assig4 
{
   public static void main(String[] args)
   {
      String[] sImageIn =
         {
            "                                               ",
            "                                               ",
            "                                               ",
            "     * * * * * * * * * * * * * * * * * * * * * ",
            "     *                                       * ",
            "     ****** **** ****** ******* ** *** *****   ",
            "     *     *    ****************************** ",
            "     * **    * *        **  *    * * *   *     ",
            "     *   *    *  *****    *   * *   *  **  *** ",
            "     *  **     * *** **   **  *    **  ***  *  ",
            "     ***  * **   **  *   ****    *  *  ** * ** ",
            "     *****  ***  *  * *   ** ** **  *   * *    ",
            "     ***************************************** ",  
            "                                               ",
            "                                               ",
            "                                               "

         };
      
         String[] sImageIn_2 =
         {
               "                                          ",
               "                                          ",
               "* * * * * * * * * * * * * * * * * * *     ",
               "*                                    *    ",
               "**** *** **   ***** ****   *********      ",
               "* ************ ************ **********    ",
               "** *      *    *  * * *         * *       ",
               "***   *  *           * **    *      **    ",
               "* ** * *  *   * * * **  *   ***   ***     ",
               "* *           **    *****  *   **   **    ",
               "****  *  * *  * **  ** *   ** *  * *      ",
               "**************************************    ",
               "                                          ",
               "                                          ",
               "                                          ",
               "                                          "

         };
         
         /*  {
         "                                          ",
         "                                          ",
         "* * * * * * * * * * * * * * * * * * * * * ",
         "*                                         ",
         "****** **** ****** ******* ** *** *****   ",
         "*     *    *****************************  ",
         "* **    * *        **  *    * * *   *     ",
         "*   *    *  *****    *   * *   *  **  **  ",
         "*  **     * *** **   **  *    **  ***  *  ",
         "***  * **   **  *   ****    *  *  ** * *  ",
         "*****  ***  *  * *   ** ** **  *   * *    ",
         "***************************************** ",
         "                                          ",
         "                                          ",
         "                                          ",
         "                                          "
         
            }; 
        This is the image pattern produced from "CSUMB CSIT online program is top notch."
        when read in as a string but it is different from the given string in sImageIn...*/ 
           
      
         BarcodeImage bc = new BarcodeImage(sImageIn);
         System.out.println("Displaying original BarcodeImage Object"
               + "\n(Not cleaned)...");
         bc.displayToConsole();
         DataMatrix dm = new DataMatrix(bc);
         System.out.println("Displaying raw image (after cleanImage(), No border)...");
         dm.displayRawImage();
         
         // First secret message
         dm.translateImageToText();
         System.out.println("Now displaying first message...");
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         // second secret message
         bc = new BarcodeImage(sImageIn_2);
         System.out.println("Now displaying second original BarcodeImage Object"
               + "\n(Not cleaned, No border)...");
         bc.displayToConsole();
         dm.scan(bc); //Invoking scan here creates a clone of new BarcodeImage object and calls cleanImage()
         dm.translateImageToText();
         System.out.println("Now displaying second message...");
         dm.displayTextToConsole();
         dm.displayImageToConsole();
         
         //create your own message
         System.out.println("Now displaying own message...");
         dm.readText("What an insane assignment!");
         dm.generateImageFromText();
         dm.displayTextToConsole();
         dm.displayImageToConsole();
      }  
}

//***************************************************************
//phase 1
/* Purpose: Implements the BarcoideIO Interface; acts as a contract
 * between classes BarcodeImage and DataMatrix to ensure the implementation
 * these methods.
 */
interface BarcodeIO 
{
   public boolean scan(BarcodeImage bc);
   
   public boolean readText(String text);
   
   public boolean generateImageFromText();
   
   public boolean translateImageToText();
   
   public void displayTextToConsole();
   
   public void displayImageToConsole();
}

//***************************************************************
//phase 2
/* Purpose: Creates the BarcodeImage class;
 * BarcodeImage class accepts an image as a string array 
 * and performs no manipulation. It displays the image as is
 * by simply storing and retrieving 2D data.
 * Preconditions: Must implement Cloneable to override clone().
 */
class BarcodeImage implements Cloneable
{
 //Variables
 public static final int MAX_HEIGHT = 30;
 public static final int MAX_WIDTH = 65;
 private boolean[][] imageData; 

 //Constructors
 /* Purpose: Creates default BarcodeImage object.
  * Preconditions: None.
  * Postconditions: Constructs a BarcodeImage object with
  * an empty array to final dimensions.
  */
 public BarcodeImage()
 {
    imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
    int row, col;
    for(row = 0; row < MAX_HEIGHT; row++)
    {
       for(col = 0; col < MAX_WIDTH; col++)
       {
          imageData[row][col] = false;
       }
    }
 }
 
 /* Purpose: Creates a BarcodeImage object that accepts a String array object.
  * Preconditions: Must pass in a String array object.
  * Postconditions: Constructs a BarcodeImage object to final dimensions
  * with appropriate coordinates set to true/false (black/white)
  * according to passed string array.
  */
 public BarcodeImage(String[] strData)
 {
   imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   int row, col;
   if(checkSize(strData))
   {
      for(row = 0; row < strData.length; row++)
      {
         for(col = 0; col < strData[row].length(); col++) 
         {
            if(strData[row].charAt(col) == ' ')
            {
               setPixel(row, col, false);
            }
            else
            {
               setPixel(row, col, true);
            }
         }
      }
   }
 }
 
 /* Purpose: Creates a deep copy of the BarcodeImage object.
  * Preconditions: Must downcast the super.clone() object to BarcodeImage type
  * since it returns as the super class 'Object'.
  * Must also iterate through each value in the array when cloning
  * to ensure separate memory locations from original references.
  * Must include a fail exception.
  * Postconditions: Returns a true deep copy of BarcodeImage object.
  */
 public BarcodeImage clone()
 {
    try 
    {
       BarcodeImage copy = (BarcodeImage)super.clone();
       for(int i = 0; i < MAX_HEIGHT; i++)
       {
          copy.imageData[i] = imageData[i].clone();
       }
       return copy;
    }
    catch(CloneNotSupportedException e)
    {
       return null;
    }
 }
 
 //Methods
 /* Purpose: Gets a pixel value at given location/coordinate in BarcodeImage object.
  * Preconditions: Requires integer parameters of the row and column.
  * Arguments must be within final dimensions.
  * Postconditions: Returns the boolean value if valid, or returns false.
  */
 boolean getPixel(int row, int col)
 {
    if(row < MAX_HEIGHT && col < MAX_WIDTH)
       return imageData[row][col];
    else
       return false; 
 }
 
 /* Purpose: Sets a pixel value at given location/coordinate in BarcodeImage object
  * given boolean value.
  * Preconditions: Requires integer parameters of the row and column,
  * and boolean value of pixel at location.
  * Must validate dimension check with checkSize().
  * Postconditions: Sets the boolean value true/false (black/white element)
  * at passed in [row, column] location.
  */
 boolean setPixel(int row, int col, boolean value)
 {
     if(row > MAX_HEIGHT && col > MAX_WIDTH)
     {
        return false;
     }
     imageData[row][col] = value;
     return true;
 }
 
 /* Purpose: Verifies that the String array is within final dimensions.
  * Preconditions: Requires a String array argument.
  * Postconditions: Returns true or false if within valid final dimensions.
  */
 private boolean checkSize(String[] data)
 {      
    if(data.length < MAX_HEIGHT && data.length > 0 && data[0].length() < MAX_WIDTH && data[0].length() > 0)
    {
       return true;
    }
    return false;
 } 
 
 /* Purpose: Displays the BarcodeImage object.
  * Preconditions: Must be a BarcodeImage object.
  * Postconditions: Returns the values of each location/coordinate
  * to depict the same output as the String array object.
  */
 public void displayToConsole()
 {
    for(int row = 0; row < MAX_HEIGHT; row++)
    {
       for(int col = 0; col < MAX_WIDTH; col++)
       {
          if(imageData[row][col] != false)
          {
             System.out.print('*');
          }
          else
             System.out.print(' ');
       }
       System.out.println();
    }
 }
}

//***************************************************************
//phase 3
/* Purpose: Creates the DataMatrix structure but does not contain
 * any error correction or encoding. All 2D manipulation and
 * translation between image and text is done here.
 * Precondition: Must implement BarcodeIO interface to initialize
 * promised methods.
 */
class DataMatrix implements BarcodeIO
{
   //Variables
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   
   private String text = "";
   private int actualWidth;
   private int actualHeight;
  
   //Constructors
   /* Purpose: Creates default DataMatrix object.
    * Preconditions: None.
    * Postconditions: Constructs a DataMatrix object
    * with a new BarcodeImage object, no text, and zero dimensions.
    */
   public DataMatrix()
   {
      image = new BarcodeImage();
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }
   
   /* Purpose: Creates a DataMatrix object that takes in BarcodeImage object.
    * Preconditions: Requires a BarcodeImage object argument. Must call itself
    * to gain other DataMatrix default attributes. Must set
    * the image object to the BarcodeImage argument and invoke scan().
    * Postconditions: Constructs a DataMatrix object with a given BarcodeImage 
    * object and other default attributes.
    */
   public DataMatrix(BarcodeImage barcodeImage) //(BarcodeImage image)
   {
      this();
      image = barcodeImage;
      scan(image);
   }
   
   /* Purpose: Creates a DataMatrix object that takes in String object.
    * Preconditions: Requires a String object argument. Must call itself
    * to gain other DataMatrix default attributes. Must set
    * the String text attribute to the String argument and invoke readText().
    * Postconditions: Constructs a DataMatrix object with a given String object
    * and other default attributes.
    */
   public DataMatrix(String newText)
   {
      this();
      text = newText;
      readText(text);
   }
   
   //Methods
   /* Purpose: Evaluates clones, and shifts the BarcodeImage argument.
    * Preconditions: Requires a BarcodeImage object.
    * The text String attribute must be reset to default value.
    * Invokes cleanImage().
    * Gathers the with and height dimensions of the image.
    * Postconditions: Returns true if the new image was successfully scanned,
    * otherwise returns false.
    */
   public boolean scan(BarcodeImage barCodeImage)
   {
       this.text = ""; 
       
       if(barCodeImage == null)//Checks if object is null before cloning.
       { 
           return false;
       }
      this.image = (BarcodeImage)barCodeImage.clone(); 
      if(this.image == null) //Checks that cloned image is valid.
      {
          return false;
      }
      cleanImage();
      this.actualWidth = computeSignalWidth();
      this.actualHeight = computeSignalHeight();
      return true;
   }
   
   /* Purpose: Shifts the BarcodeImage to the lower left portion of the grid.
    * Preconditions: 
    * Postconditions: after shifting
    * to lower left portion of the grid sized to final dimensions.
    */
   private void cleanImage()
   {
      int row, col;
      int heightDisplacement = findHeightDisplacement();
      int widthDisplacement = findWidthDisplacement(); 
      
      for(row = BarcodeImage.MAX_HEIGHT-1; row >= heightDisplacement ; row--)
      {
         for(col = 0; col < BarcodeImage.MAX_WIDTH - widthDisplacement; col++)
         {
            image.setPixel(row, col, image.getPixel(row-heightDisplacement, col+widthDisplacement));//copies over the image pixel value from pixels of image's original location
            image.setPixel(row - heightDisplacement, col + widthDisplacement, false); //sets original pixel location to white elements
         }
      }
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   private int computeSignalWidth()
   {
      int col = 0;
      int width = 0;
      while(image.getPixel(BarcodeImage.MAX_HEIGHT-1, col) != false) //not a white element
      {
         width += 1;
         col++;
      }
      return width;
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   private int computeSignalHeight() 
   {
      int row = BarcodeImage.MAX_HEIGHT-1;
      int height = 0;
      while(image.getPixel(row, 0) != false) //not a white element
      {
         height += 1;
         row--;
      }
      return height;
   }
   
   /* Purpose: To access the private width attribute.
    * Preconditions: Must be a DataMatrix object.
    * Postconditions: Returns the actualWidth attribute.
    */
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   /* Purpose: To access the private height attribute.
    * Preconditions: Must be a DataMatrix Object.
    * Postconditions: Returns the actualHeight attribute.
    */
   public int getActualHeight()
   {
      return actualHeight;
   }
  
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   private int findHeightDisplacement()
   {
      int row, col;
      
      for(row = BarcodeImage.MAX_HEIGHT-1; row > 0; row--)
      {
         for(col = 0; col < BarcodeImage.MAX_WIDTH; col++)
         {
            if(image.getPixel(row,col) == true) //if black element
            {
               return (BarcodeImage.MAX_HEIGHT - 1) - row; //to find the difference
            }
         }
      }
      return -1;
   }

   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   private int findWidthDisplacement()
   {
      int row, col;

      for(row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
      {
         for(col = 0; col < BarcodeImage.MAX_WIDTH; col++)
         {
            if(image.getPixel(row,col) == true) //if black element
            {
               return col;
            }
         }
      }
      return -1;
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   public void displayRawImage()
   {
      for(int r = 0; r < BarcodeImage.MAX_HEIGHT; r++)
      {
         for(int c = 0; c < BarcodeImage.MAX_WIDTH; c++)
         {
            if(image.getPixel(r, c) != false)
            {
               System.out.print('*');
            }
            else
            {
               System.out.print(' ');
            }
         }
         System.out.println();
      }
   }
   
   /* Purpose: Resets the values of the image.
    * Preconditions: Must be a DataMatrix object.
    * Postconditions: Does not clear/reset any object attributes.
    * This only sets the displayed elements to false (white elements).
    */
   public void clearImage()
   {
      for(int row = 0; row < BarcodeImage.MAX_HEIGHT; row++)
      {
         for(int col = 0; col < BarcodeImage.MAX_WIDTH; col++)
         {
            image.setPixel(row, col,  false);
         }
      }
   }
  
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   public boolean readText(String text)
   {
      if(text != null)
      {
         this.text = text;
         return true;
      }
      else
         return false;
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   public boolean translateImageToText()
   {
      if(image != null)
      {
         for (int column = 1; column < getActualWidth()-1; column++)
         {
            text += readCharFromCol(column);
         }
         return true;
      }
      return false;
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   private char readCharFromCol(int column)
   {
      int value = 0;
      int power = 0;
      char character = ' ';
      for(int row = BarcodeImage.MAX_HEIGHT-2; row > BarcodeImage.MAX_HEIGHT-actualHeight; row--)
      {
         if(image.getPixel(row, column) == true)
         {
            value += Math.pow(2,power);
         }
         power++;
      }
      character = (char)value;
      return character;
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
   
   /* Purpose: Produces an image from String text data.
    * Preconditions: Requires a DataMatrix object with valid 
    * String text data. Requires the left, bottom, and 
    * alternating top spine.
    * Postconditions: Invokes helper method writeCharToCol().
    * Returns true if successful, otherwise false if text exceeds
    * limit of final width dimension.
    */
   public boolean generateImageFromText() 
   {
      if (text == null || text.length() > BarcodeImage.MAX_WIDTH)
      {
          return false;
      }
      clearImage(); // clear in case of old data
      actualWidth = text.length() + 2; //plus two because of border
      actualHeight = 10; 
      //forgot right alternating pattern!!
      
      //setting spine
      //top
      for(int i = 0; i < actualWidth; i += 2)
      {
         image.setPixel(BarcodeImage.MAX_HEIGHT - actualHeight, i, true);
      }
      //left
      for(int i = 0; i < actualHeight; i++)
      {
         image.setPixel(BarcodeImage.MAX_HEIGHT - actualHeight + i, 0, true);
      }
      //bottom
      for(int i = 0; i < actualWidth; i++)
      {
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, i, true);
      }

      //creating black elements in fields
      int ascii = 0;
      for(int column = 0; column < getActualWidth() - 2 ; column++)
      {
          ascii = (int)text.charAt(column); //casting the character found at column index to an integer value
          writeCharToCol(ascii, column + 1); //plus one because of the spine
      }
      return true;
   }
   
   /* Purpose: Helper function for generateImageFromText. Evaluates
    * the ascii number at a given column to calculate the binary value.
    * Iterates through the image to set each pixel to true if binary value
    * is 1, otherwise maintain false value if binary value is 0. 
    * Preconditions: Requires an ascii integer and the column integer.
    * Postconditions: Returns true or false values based on binary value.
    */
   private void writeCharToCol(int ascii, int column) 
   {
       for(int row = BarcodeImage.MAX_HEIGHT - 2; row > BarcodeImage.MAX_HEIGHT - getActualHeight(); row--)
       {

           if(ascii % 2 == 1)
           {
               image.setPixel(row, column, true);
           }
           ascii /= 2;
       }
   }
   
   /* Purpose:
    * Preconditions: 
    * Postconditions:
    */
   public void displayImageToConsole()
   {
      int topBorder = BarcodeImage.MAX_HEIGHT - getActualHeight();
      
      for(int column = 0 ; column < getActualWidth() + 2 ; column++)
      {
         System.out.print('-');
      }
      System.out.println();
      
      for (int row = topBorder; row < BarcodeImage.MAX_HEIGHT; row++)
      {
         System.out.print('|');
         for (int column = 0; column < getActualWidth(); column++)
         {
            if (image.getPixel(row, column) == true)
            {
               System.out.print('*');
            }
            else
            {
               System.out.print(' ');
            }
         }
         System.out.println('|');
      }
      
      /*
      //bottom border - not required
      for(int row = BarcodeImage.MAX_HEIGHT-1 ; row > BarcodeImage.MAX_HEIGHT-2; row--)
      {
         for(int column = 0; column < getActualWidth()+2; column++)
         {               
            System.out.print('-');
         }
         System.out.println();
      }*/
   }
}

/*-------------------Output-------------------------
 * Displaying original BarcodeImage Object
(Not cleaned)...
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
     * * * * * * * * * * * * * * * * * * * * *                   
     *                                       *                   
     ****** **** ****** ******* ** *** *****                     
     *     *    ******************************                   
     * **    * *        **  *    * * *   *                       
     *   *    *  *****    *   * *   *  **  ***                   
     *  **     * *** **   **  *    **  ***  *                    
     ***  * **   **  *   ****    *  *  ** * **                   
     *****  ***  *  * *   ** ** **  *   * *                      
     *****************************************                   
                                                                 
                                                                 
                                                                 
Displaying raw image (after cleanImage(), No border)...
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
* * * * * * * * * * * * * * * * * * * * *                        
*                                       *                        
****** **** ****** ******* ** *** *****                          
*     *    ******************************                        
* **    * *        **  *    * * *   *                            
*   *    *  *****    *   * *   *  **  ***                        
*  **     * *** **   **  *    **  ***  *                         
***  * **   **  *   ****    *  *  ** * **                        
*****  ***  *  * *   ** ** **  *   * *                           
*****************************************                        
Now displaying first message...
CSUMB CSIT online program is top notch.
-------------------------------------------
|* * * * * * * * * * * * * * * * * * * * *|
|*                                       *|
|****** **** ****** ******* ** *** *****  |
|*     *    ******************************|
|* **    * *        **  *    * * *   *    |
|*   *    *  *****    *   * *   *  **  ***|
|*  **     * *** **   **  *    **  ***  * |
|***  * **   **  *   ****    *  *  ** * **|
|*****  ***  *  * *   ** ** **  *   * *   |
|*****************************************|
Now displaying second original BarcodeImage Object
(Not cleaned, No border)...
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
                                                                 
* * * * * * * * * * * * * * * * * * *                            
*                                    *                           
**** *** **   ***** ****   *********                             
* ************ ************ **********                           
** *      *    *  * * *         * *                              
***   *  *           * **    *      **                           
* ** * *  *   * * * **  *   ***   ***                            
* *           **    *****  *   **   **                           
****  *  * *  * **  ** *   ** *  * *                             
**************************************                           
                                                                 
                                                                 
                                                                 
                                                                 
Now displaying second message...
You did it!  Great work.  Celebrate.
----------------------------------------
|* * * * * * * * * * * * * * * * * * * |
|*                                    *|
|**** *** **   ***** ****   *********  |
|* ************ ************ **********|
|** *      *    *  * * *         * *   |
|***   *  *           * **    *      **|
|* ** * *  *   * * * **  *   ***   *** |
|* *           **    *****  *   **   **|
|****  *  * *  * **  ** *   ** *  * *  |
|**************************************|
Now displaying own message...
What an insane assignment!
------------------------------
|* * * * * * * * * * * * * * |
|*                           |
|***** ** ****** **********  |
|* ************************* |
|**  *      *     **      *  |
|* *    * **  *     * ** *   |
|**  *  *  *  **     ******  |
|**     *  ** *   ** **  *   |
|** *  *  * ** * ***** **  * |
|****************************|
*/

