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
         BarcodeImage bc = new BarcodeImage(sImageIn);
         bc.displayToConsole();
         DataMatrix dm = new DataMatrix(bc);
         dm.displayRawImage();
        
         // First secret message
         //dm.translateImageToText();
         //dm.displayTextToConsole();
         //dm.displayImageToConsole();
         
         // second secret message
         //bc = new BarcodeImage(sImageIn_2);
         //bc.displayToConsole();
         //dm.scan(bc);
         //dm.translateImageToText();
         //dm.displayTextToConsole();
         //dm.displayImageToConsole();
         
         // create your own message
         //dm.readText("What a great resume builder this is!");
         //dm.generateImageFromText();
         //dm.displayTextToConsole();
         //dm.displayImageToConsole();
      }  
}

//***************************************************************
//phase 1
interface BarcodeIO 
{
   public boolean scan(BarcodeImage bc);
   
 //  public boolean readText(String text);
   
 //  public boolean generateImageFromText();
   
   //public boolean translateImageToText();
   
  // public void displayTextToConsole();
   
  // public void displayImageToConsole();
}

//***************************************************************
//phase 2
class BarcodeImage implements Cloneable
{
 public static final int MAX_HEIGHT = 30;//dimensions of 2d data
 public static final int MAX_WIDTH = 65;
 private boolean[][] imageData; //where to store the image

 //constructors
 //initiates a 2d array with all white elements (false)
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
 
 public BarcodeImage(String[] strData)
 {
   imageData = new boolean[MAX_HEIGHT][MAX_WIDTH];
   int rowIndex, colIndex;
   int startLowerBound = MAX_HEIGHT - strData.length;
   if(checkSize(strData))
   {
      for(rowIndex = 0; rowIndex < strData.length; rowIndex++)
      {
         for(colIndex = 0; colIndex < strData[rowIndex].length(); colIndex++) 
         {
            if(strData[rowIndex].charAt(colIndex) == ' ')
            {
               setPixel(startLowerBound+rowIndex, colIndex, false);
            }
            else
            {
               setPixel(startLowerBound+rowIndex, colIndex, true);
            }
         }
      }
   }
 }
 
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
 
 boolean getPixel(int row, int col)
 {
    if(row < MAX_HEIGHT && col < MAX_WIDTH)
       return imageData[row][col];
    else
       return false; 
 }
 
 //assumed that checkSize is invoked first for dimension check
 boolean setPixel(int row, int col, boolean value)
 {
     if(row > MAX_HEIGHT && col > MAX_WIDTH)
     {
        return false;
     }
     imageData[row][col] = value;
     return true;
 }
 
 private boolean checkSize(String[] data)
 {      
    if(data.length < MAX_HEIGHT && data.length > 0 && data[0].length() < MAX_WIDTH && data[0].length() > 0)
    {
       return true;
    }
    return false;
 } 
 
 public void displayToConsole()
 {
    for(int i = 0; i < MAX_HEIGHT; i++)
    {
       for(int k = 0; k < MAX_WIDTH; k++)
       {
          if(imageData[i][k] != false)
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
class DataMatrix implements BarcodeIO
{
   public static final char BLACK_CHAR = '*';
   public static final char WHITE_CHAR = ' ';
   private BarcodeImage image;
   
   private String text = "";
   private int actualWidth;
   private int actualHeight;
  
   public DataMatrix()
   {
      image = new BarcodeImage();
      text = "undefined";
      actualWidth = 0;
      actualHeight = 0;
   }
   
   public DataMatrix(BarcodeImage barcodeImage)
   {
      image = barcodeImage;
      text = "undefined";
      scan(image);
   }
   
   public DataMatrix(String text)
   {
      readText(text);
   }
   
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
   
   public boolean scan(BarcodeImage barCodeImage)
   {
       if(barCodeImage == null)
       { 
           return false;
       }
      this.image = (BarcodeImage)barCodeImage.clone();
      if(this.image == null)
      {
          return false;
      }
      cleanImage();
      this.actualWidth = computeSignalWidth();
      this.actualHeight = computeSignalHeight();
      return true;
   }
   
   public int getActualWidth()
   {
      return actualWidth;
   }
   
   public int getActualHeight()
   {
      return actualHeight;
   }
   
   private int computeSignalWidth()
   {
      int c = 0;
      int width = 0;
      while(image.getPixel(BarcodeImage.MAX_HEIGHT-1, c) != false) //not a white element
      {
         width += 1;
         c++;
      }
      return width;
   }
   
   private int computeSignalHeight() 
   {
      int r = BarcodeImage.MAX_HEIGHT-1;
      int height = 0;
      while(image.getPixel(r, 0) != false) //not a white element
      {
         height += 1;
         r--;
      }
      return height;
   }
   
   private void cleanImage()
   {
      int rowIndex, colIndex;
      int heightDisplacement = findHeightDisplacement();
      int widthDisplacement = findWidthDisplacement(); 
      
      for(rowIndex = BarcodeImage.MAX_HEIGHT-1; rowIndex >= heightDisplacement ; rowIndex--)
      {
         for(colIndex = 0; colIndex < BarcodeImage.MAX_WIDTH - widthDisplacement; colIndex++)
         {
            image.setPixel(rowIndex, colIndex, image.getPixel(rowIndex-heightDisplacement, colIndex+widthDisplacement));//copies over the image pixel value from pixels of image's original location
            image.setPixel(rowIndex - heightDisplacement, colIndex + widthDisplacement, false); //sets original pixel location to white elements
         }
      }
   }
  
   private int findHeightDisplacement()
   {
      int rowIndex, colIndex;
      
      for(rowIndex = BarcodeImage.MAX_HEIGHT-1; rowIndex > 0; rowIndex--)
      {
         for(colIndex = 0; colIndex < BarcodeImage.MAX_WIDTH; colIndex++)
         {
            if(image.getPixel(rowIndex,colIndex) == true) //if black element
            {
               return (BarcodeImage.MAX_HEIGHT - 1) - rowIndex; //to find the difference
            }
         }
      }
      return -1;
   }

   private int findWidthDisplacement()
   {
      int rowIndex, colIndex;

      for(rowIndex = 0; rowIndex < BarcodeImage.MAX_HEIGHT; rowIndex++)
      {
         for(colIndex = 0; colIndex < BarcodeImage.MAX_WIDTH; colIndex++)
         {
            if(image.getPixel(rowIndex,colIndex) == true) //if black element
            {
               return colIndex;
            }
         }
      }
      return -1;
   }
   
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
   
   public void clearImage()
   {
      for(int r = 0; r < BarcodeImage.MAX_HEIGHT; r++)
      {
         for(int c = 0; c < BarcodeImage.MAX_WIDTH; c++)
         {
            image.setPixel(r, c,  false);
         }
      }
   }
   
}
