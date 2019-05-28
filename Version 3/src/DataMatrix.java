//phase 3
public class DataMatrix implements BarcodeIO
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
      text = "";
      actualWidth = 0;
      actualHeight = 0;
   }
   
   public DataMatrix(BarcodeImage barcodeImage) //(BarcodeImage image)
   {
      this();
      image = barcodeImage;
      scan(image);
   }
   
   public DataMatrix(String newText)
   {
      this();
      text = newText;
      readText(text);
   }
   
   public boolean scan(BarcodeImage barCodeImage)
   {
       this.text = ""; //resets any possible text from previous scan

       if(barCodeImage == null)//checks if object is null before cloning
       { 
           return false;
       }
      this.image = (BarcodeImage)barCodeImage.clone(); 
      if(this.image == null) //checks that image is valid after cloning
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
   
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
   
   public boolean generateImageFromText() //not producing right
   {
      if (text == null || text.length() > BarcodeImage.MAX_WIDTH)
      {
          return false;
      }
      clearImage(); // clear in case of old data
      actualWidth = text.length()+2;
      actualHeight = 10; 
      
      //setting spine and top alternating pattern
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
      for(int i = 0; i< actualWidth; i++)
      {
         image.setPixel(BarcodeImage.MAX_HEIGHT - 1, i, true);
      }
      
      //creating black elements in fields
      int ascii = 0;
      for(int column = 0; column < getActualWidth()-2 ; column++)//
      {
          ascii = (int)text.charAt(column);
          charToCol(ascii, column+1); //
      }

      return true;
   }
   
   private void charToCol(int ascii, int column) //not producing right
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
