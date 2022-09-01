import filters.connectivity.Connectivity;
import filters.connectivity.SmartRodriguesConnectivity;
import filters.connectivity.ignorar_SmartRodriguesConnectivity2;
import filters.vessel.FrangiFilter;
import image.Image;
import morphology.Morphology;

import java.io.File;


public class Run {

    public static void main(String[] args) throws Exception {
        String path = "C:\\Users\\oyatsumi\\Google Drive\\Synced Folder\\Projetos e Artigos\\IWSSIP2020 (x-ray angiograms)\\X-RayAngiogramDataset-master\\OSIRIX Images\\IMG-0004-00001.jpg";
        Image img = new Image(path);
        img.convertToGray(1,8);

        Image mask = new Image("C:\\Users\\oyatsumi\\Google Drive\\Synced Folder\\Projetos e Artigos\\IWSSIP2020 (x-ray angiograms)\\X-RayAngiogramDataset-master\\mask.png");
        mask.convertToGray();
        mask.erode(Image.MorphologyConstants.STRUCT_PRIMARY, 14);




        //frangi
        FrangiFilter frangi = new FrangiFilter();
        frangi.setMinSigma(1f);
        frangi.setBeta(1.1f, 3.9f);
        frangi.setMaxSigma(2.7f);
        frangi.setSigmaStep(1.3f);


        img.applyFilter(frangi);


        img.clone().setTitle("Frangi Cinza").showImage(); //n�o precisa salvar essa, pode tirar os showImage dpeois se quiser, sem problemas.


        img.maskImage(mask);
        img.threshold(40);
        img.setTitle("(salvar) Thresholded Frangi").showImage();


        //conn
        Image con0 = img.clone();
        Connectivity connFilter = new Connectivity();
        con0.applyFilter(connFilter);
        con0.threshold(1)
                .setTitle("(salvar) Connectivity filter").showImage(); //salvar essa, simple conn


        //morphology depois da conn
        Image morpho = con0.clone();
        morpho.dilate(Image.MorphologyConstants.STRUCT_SUP, 2).erode(Image.MorphologyConstants.STRUCT_SUP, 2);
        morpho.setTitle("(salvar) Connectivity + morphology").showImage();


        //smart conn 1
        Image img2 = img.clone();

        SmartRodriguesConnectivity smartConn = new SmartRodriguesConnectivity();
        //smartConn.setKernel(Image.MorphologyConstants.STRUCT_SUP);
        img2.applyFilter(smartConn).clone().showImage();

        img2.clone().setTitle("(salvar) Smart connectivity filter + erosion - versao 1")
                .threshold(2).erode(Image.MorphologyConstants.STRUCT_SUP, 1)
                .maskImage(mask)
                .showImage(); //salvar essa smart conn



        //smart conn 2
        Image img3 = img.clone();

        SmartRodriguesConnectivity smartConn2 = new SmartRodriguesConnectivity();
        smartConn2.setMaxDistance(8);
        smartConn2.setMaxToleranceScore(450);
        //smartConn.setKernel(Image.MorphologyConstants.STRUCT_SUP);
        img3.applyFilter(smartConn2).clone().showImage();

        img3.clone().setTitle("(salvar) Smart connectivity filter + erosion - versao 2")
                .threshold(2).erode(Image.MorphologyConstants.STRUCT_SUP, 3)
                .maskImage(mask)
                .showImage(); //salvar essa smart conn


        System.out.println("terminado");
    }


    public static void main2(String[] args) throws Exception {
        String path = "C:\\Users\\oyatsumi\\Google Drive\\Synced Folder\\Projetos e Artigos\\Connectivity Filter\\Images\\IOSTAR\\imgs\\";

        File folder = new File(path);

        File[] files = folder.listFiles();



        //mask
        File maskFolder = new File(path + "..\\mask\\");
        File[] masks = maskFolder.listFiles();
        //


        for (int k=0; k<files.length; k++){

            Image img = new Image(files[k]);
            img.convertToGray(1,8);

            //mask
            Image mask = new Image(masks[k]);
            mask.convertToGray();
            mask.erode(Image.MorphologyConstants.STRUCT_PRIMARY, 4);
            //


            long start = System.currentTimeMillis();

            //
            //
            //

            FrangiFilter frangi = new FrangiFilter();
            frangi.setMinSigma(0.001f);
            frangi.setBeta(0.26f, 0.45f);
            frangi.setMaxSigma(2.95f);

            img.applyFilter(frangi);

            long end = System.currentTimeMillis();

            System.out.println("Frangi filter: " + (end - start)/1000F);

            /*
            //hessian features
            HessianFilter h6 = new HessianFilter(),
                    h11 = new HessianFilter();


            h6.setOperationType(HessianFilter.HessianOperationType.TYPE_FIRST_EIGENVALUE);
            h11.setOperationType(HessianFilter.HessianOperationType.TYPE_TRACE);

            Image   h6Image = img.clone().applyFilter(h6).invert().showImage(),
                    h11Image = img.clone().applyFilter(h11).invert().showImage();

            Image hNew = h6Image.clone().subtract(h11Image.invert()).threshold(30).maskImage(mask).showImage();
            //
            */



            img.dilate(Morphology.STRUCT_PRIMARY, 1);
            img.erode(Morphology.STRUCT_PRIMARY, 2);
            img.dilate(Morphology.STRUCT_PRIMARY, 1);

            //
            img.maskImage(mask);
            //

            img.threshold(100);
            img.setTitle("Frangi + threshold + morphology");
            img.showImage();



            end = System.currentTimeMillis();
            System.out.println("Frangi + threshold + morphology: " + (end - start)/1000F);



            Image con = img.clone();
            Image con0 = img.clone();

            //SupConnectivity connectivity = new SupConnectivity();
            //con.applyFilter(connectivity);


            start = System.currentTimeMillis();

            Connectivity connFilter = new Connectivity();
            connFilter.setKernel(Image.MorphologyConstants.STRUCT_SUP);
            con0.applyFilter(connFilter);

            end = System.currentTimeMillis();
            System.out.println("Connectivity filter: " + (end - start)/1000F);

            con0.clone().setTitle("Connectivity filter sem threshold").showImage();

            con0.setTitle("Connectivity filter");
            con0.threshold(1).showImage();



            start = System.currentTimeMillis();

            SmartRodriguesConnectivity smartConn = new SmartRodriguesConnectivity();
            //smartConn.setKernel(Image.MorphologyConstants.STRUCT_SUP);
            con.applyFilter(smartConn);

            end = System.currentTimeMillis();
            System.out.println("Smart connectivity filter: " + (end - start)/1000F);

            con.setTitle("Smart connectivity filter");
            con.showImage();


            con.clone().setTitle("Smart connectivity filter + erosion")
                    .threshold(5).erode(Image.MorphologyConstants.STRUCT_SUP, 1).showImage();

            end = System.currentTimeMillis();
            System.out.println("Smart connectivity filter + erosion: " + (end - start)/1000F);

            //img.threshold(1);

        }


    }

}
