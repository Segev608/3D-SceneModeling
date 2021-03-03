package Tests.UnitTests;

import org.junit.Test;
import renderer.ImageWriter;
import java.awt.Color;

public class ImageWriterTest {

    /**
     * test ImageWriter writePixel() and writeToImage()
     */
    @Test
    public void testImageWriter(){
        ImageWriter write = new ImageWriter("test", 800, 500, 1600, 1000);
        for(int i=0; i<1000; i++){
            for(int j=0; j<1600; j++){
                if(i%100 == 0 || j%100 == 0)
                    write.writePixel(j,i,new Color(255,255,255));
                else
                    write.writePixel(j, i, new Color(((i * 255) / 1000) % 255,((j * 255) / 1600) % 255, (((i + j) % 255))));
            }
        }
        write.writeToImage();
    }


}





