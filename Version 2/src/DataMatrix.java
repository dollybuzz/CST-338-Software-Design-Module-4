/*
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
      text = "undefined";
      actualWidth = 0;
      actualHeight = 0;
   }
   
   public DataMatrix(BarcodeImage image)
   {
      this();
      scan(image);
   }
   
   public DataMatrix(String text)
   {
      this();
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
   
   public boolean scan(BarcodeImage image)
   {
      if(image != null)
      {
         this.image = (BarcodeImage)image.clone();
         this.cleanImage();
         actualWidth = computeSignalWidth();
         actualHeight = computeSignalHeight();
         return true;
      }
      else
         return false;
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
      int imageHeightLowerBound = findHeightLowerBound();
      int imageWidthLowerBound = findWidthLowerBound();
      boolean imagePixelValue = false;
      
      
      for(rowIndex = BarcodeImage.MAX_HEIGHT-1; rowIndex > 0 ; rowIndex--)
      {
         for(colIndex = 0; colIndex < BarcodeImage.MAX_WIDTH; colIndex++)
         {
            imagePixelValue = image.getPixel(rowIndex - imageHeightLowerBound, colIndex + imageWidthLowerBound); //stores original pixel value
            image.setPixel(rowIndex, colIndex, imagePixelValue);//copies over the image pixel value from pixels of image's original location
            image.setPixel(rowIndex - imageHeightLowerBound, colIndex + imageWidthLowerBound, false); //sets original pixel location to white elements
         }
      }
   }
   
   private int findHeightLowerBound()
   {
      int r, c;
      int height = 0;
      for(r = BarcodeImage.MAX_HEIGHT-1; r > 0; r--)
      {
         for(c = 0; c < BarcodeImage.MAX_WIDTH; c++)
         {
            if(image.getPixel(r, c) != false) //if it equals a black element
            {
               height = (BarcodeImage.MAX_HEIGHT-1) - r;
            }
         }
      }
      return height;   
   }
   
   private int findWidthLowerBound()
   {
      int r, c;
      int width = 0;
      for(r = 0; r < BarcodeImage.MAX_HEIGHT; r++)
      {
         for(c = 0; c < BarcodeImage.MAX_WIDTH; c++)
         {
            if(image.getPixel(r, c) != false) //if it equals a black element
            {
               width = c;
            }
         }
      }
      return width;
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
   
   public void displayImageToConsole()
   {
      //work on
   }
   
   
   public boolean generateImageFromText()
   {}
   
   public boolean translateImageToText()
   {}
   
   public void displayTextToConsole()
   {
      System.out.println(text);
   }
  
   
}*/
