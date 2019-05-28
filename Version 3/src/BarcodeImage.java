//phase 2
public class BarcodeImage implements Cloneable
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