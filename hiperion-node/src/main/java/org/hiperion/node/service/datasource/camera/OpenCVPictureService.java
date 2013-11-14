package org.hiperion.node.service.datasource.camera;

import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.cpp.opencv_core;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.log4j.Logger;
import org.hiperion.common.exception.HiperionException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

/**
 * User: iobestar
 * Date: 21.06.13.
 * Time: 14:09
 */
public class OpenCVPictureService {

    private static final Logger LOGGER = Logger.getLogger(OpenCVPictureService.class);

    public enum ImageType{
        JPG,
        PNG;
    }

    private ConcurrentHashMap<Integer, OpenCVFrameGrabber> frameGrabberMap;
    private final ImageType imageType;

    public OpenCVPictureService(ImageType imageType){
        this.imageType = imageType;
        this.frameGrabberMap = new ConcurrentHashMap<Integer, OpenCVFrameGrabber>();
    }

    public synchronized byte[] takePicture(int deviceId, int width, int height) throws HiperionException {

        OpenCVFrameGrabber openCVFrameGrabber = frameGrabberMap.get(deviceId);
        if(null == openCVFrameGrabber){
            addCaptureDevice(deviceId);
            openCVFrameGrabber = frameGrabberMap.get(deviceId);
        }
        try {
            opencv_core.IplImage img = openCVFrameGrabber.grab();
            openCVFrameGrabber.flush();
            BufferedImage image = resizeImage(img.getBufferedImage(), width,height);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(image, imageType.name().toLowerCase(), baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (FrameGrabber.Exception e) {
            throw new HiperionException(e);
        } catch (IOException e) {
            throw new HiperionException(e);
        }
    }

    private BufferedImage resizeImage(BufferedImage image, int width, int height) throws IOException {
        return Thumbnails.of(image).size(width, height).asBufferedImage();
    }

    private void addCaptureDevice(int deviceId) throws HiperionException {
        try {
            OpenCVFrameGrabber openCVFrameGrabber = new OpenCVFrameGrabber(deviceId);
            openCVFrameGrabber.start();
            frameGrabberMap.putIfAbsent(deviceId, openCVFrameGrabber);
        } catch (FrameGrabber.Exception e) {
            throw new HiperionException(e);
        }
    }

    public ImageType getImageType() {
        return imageType;
    }

    public void dispose(){
        for(OpenCVFrameGrabber openCVFrameGrabber : frameGrabberMap.values()){
            try {
                openCVFrameGrabber.stop();
            } catch (FrameGrabber.Exception e) {
                LOGGER.error(e);
            }
        }
    }
}
