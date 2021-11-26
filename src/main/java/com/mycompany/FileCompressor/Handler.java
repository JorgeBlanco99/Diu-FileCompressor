/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.FileCompressor;

import java.util.List;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import javax.swing.SwingWorker;

/**
 *
 * @author jorge
 */

public class Handler extends SwingWorker<Void, Integer> {
    private  String absolutePath;
    private  List<String> files;
    private  int BUFFER_SIZE =10000;
    private LoadingFrame lf;
    
    public Handler(String absolutePath, List<String> files, LoadingFrame lf){
        this.absolutePath=absolutePath;
        this.files=files;
        this.lf=lf;
    }
    protected Void doInBackground() throws Exception {
        try{
            //Referenciar archivos a comprimir
            BufferedInputStream origin = null;
            //Referenciar archivo zip de salida
            FileOutputStream dest = new FileOutputStream(absolutePath+".zip");
            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));
            // Buffer de transferencia que  almacena los datos a comprimir
            byte[] data = new byte[BUFFER_SIZE];
            Iterator i = files.iterator();
            int a=0;
            int leng= files.size();
            int each= 100/files.size();
            
            lf.setProgressBarValue(0);
            while(i.hasNext()) {
                try{
                    Thread.sleep(100);
                }catch (InterruptedException e){
                    Thread.currentThread().interrupt(); 
                }
                a+=each;
                lf.setProgressBarValue(a);
                String filename = (String)i.next();
                FileInputStream fi = new FileInputStream(filename);
                origin = new BufferedInputStream(fi, BUFFER_SIZE);
                ZipEntry entry = new ZipEntry( filename );
                out.putNextEntry( entry );
                // Leemos datos desde el archivo origen y se envían al archivo destino
                int count;
                while((count = origin.read(data, 0, BUFFER_SIZE)) != -1)  {
                    out.write(data, 0, count);
                }
                // Cerrar  archivo origen
                origin.close();
            }
            a++;
            // Cerramos el zip
            out.close();
        }catch( Exception e ) {
            e.printStackTrace();
        }
        return null;
      
    }
    @Override
    public void done(){
        lf.dispose();
       
    }
}