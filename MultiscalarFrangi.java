import filters.connectivity.Connectivity;
import filters.connectivity.SmartRodriguesConnectivity;
import filters.vessel.FrangiFilter;
import filters.vessel.MultiscaleFrangiFilter;
import image.Image;

public class MultiscalarFrangi {

    public static void main(String[] args) throws Exception {

        String path = "C:\\Users\\oyatsumi\\Google Drive\\Synced Folder\\Projetos e Artigos\\Connectivity Filter\\Images\\DRIVE\\test\\images\\";

        Image img = new Image(path + "01_test.tif");
        img.convertToGray(1, 8);


        FrangiFilter f1 = new FrangiFilter(),
        f2 = new FrangiFilter(),
        f3 = new FrangiFilter(),
        f4 = new FrangiFilter(),
        f5 = new FrangiFilter(),
        f6 = new FrangiFilter(),
        f7 = new FrangiFilter(),
        f8 = new FrangiFilter(),
        f9 = new FrangiFilter(),
        f10 = new FrangiFilter();

        f1.setMinSigma(0); f1.setMaxSigma(1);
        f1.setSigmaStep(0.5f);

        f2.setMinSigma(1); f2.setMaxSigma(1);
        f2.setSigmaStep(0.25f);

        f3.setMinSigma(0); f3.setMaxSigma(3);
        f3.setSigmaStep(1);

        f4.setMinSigma(0); f4.setMaxSigma(1);
        f4.setBeta(2f, 3);
        f4.setSigmaStep(0.2f);

        f5.setMinSigma(0); f5.setMaxSigma(3);
        f5.setBeta(2f, 3f);
        f5.setSigmaStep(0.6f);



        Image f1i = img.clone().applyFilter(f1).showImage().clone();
        Image f2i = img.clone().applyFilter(f2).showImage().clone();
        Image f3i = img.clone().applyFilter(f3).showImage().clone();
        Image f4i = img.clone().applyFilter(f4).showImage().clone();
        Image f5i = img.clone().applyFilter(f5).showImage().clone();

        f1i.multiply(0.2f);
        f2i.multiply(0.2f);
        f3i.multiply(0.2f);
        f4i.multiply(0.2f);
        f5i.multiply(0.2f);

        f1i.add(f2i);
        f1i.add(f3i);
        f1i.add(f4i);
        f1i.add(f5i);

        f1i.showImage();


        MultiscaleFrangiFilter mff = new MultiscaleFrangiFilter();
        Image fi = img.clone().applyFilter(mff).threshold(70).showImage();

        Connectivity connFilter = new Connectivity();
        connFilter.setKernel(Image.MorphologyConstants.STRUCT_WIDER_CROSS);
        fi.clone().applyFilter(connFilter).showImage();


        SmartRodriguesConnectivity smartConn = new SmartRodriguesConnectivity();
        smartConn.setKernel(Image.MorphologyConstants.STRUCT_SUP);
        fi.clone().applyFilter(smartConn).threshold(1).erode(Image.MorphologyConstants.STRUCT_SUP, 1).showImage();


    }

}
