package com.garguir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Logger;

public class FileBrowser {
    private static final Logger LOGGER = Logger.getLogger("FilesCopy");
    private static final String USER_DIR = System.getProperty("user.dir");
    private static final String PATH_ORIGIN_FILES = USER_DIR+"\\src\\main\\resources";
    private static final String PATH_NEW_FILES = USER_DIR+"\\specs\\";
    private static final String OK = "ok";
    private static final String REQUEST = "Request";
    private static final String RESPONSE = "Response";
    private static final String WITHOUT_EXTENSION = ".without_extension";
    private static final String LINE_ASTERISKS =   "***************************************************************************************************";
    private static final String LINE_EQUALS =      "===================================================================================================";
    private static final String LINE_UNDERLINEDS = "___________________________________________________________________________________________________";
    private static final String LINE_MIDDLE =      "---------------------------------------------------------------------------------------------------";
    static int nro = 0;
    File filesRoute;

    public FileBrowser(String route) {
        this.filesRoute = new File(route);
    }

    public void listFiles(String path) throws IOException {
        File file = new File(path);
        if (file.exists()) {
            File[] files = file.listFiles();

            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isFile()) {
                        createFiles(files[i]);
                    } else if (files[i].isDirectory()) {
                        createDir(files[i]);
                        listFiles(files[i].getAbsolutePath());
                    }
                }
            }
        } else {
            System.out.println("El directorio o la ruta no existen.");
        }
    }

    private static String getNewPath(File file){
        return PATH_NEW_FILES + file.getParent()
                .replace(PATH_ORIGIN_FILES, "")
                .replace("\\prp", "")
                .replace(" ", "_") + "\\";
    }

    private static void createDir(File file){
        if(!file.getName().equals("prp")) {
            File newDir = new File( getNewPath(file) + file.getName().replace(" ","_"));
            if (!newDir.exists()) {
                if (!newDir.mkdir()) {
                    LOGGER.warning("createDir >>>>>>>>>>>>>>>>>>>>>>>>>>> Error al crear Directorio " + newDir.getAbsolutePath());
                }
            }
        }
    }

    private static String createNewDir(File file){
        //nro++;
        String fileName = getFileNameWithoutExtension(file.getName());
        String newDirName = (fileName.length() > 0 && !onlySpaces(fileName))?fileName.replace(" ", "_"):Integer.toString(++nro);
        File newFileNameDir = new File(getNewPath(file) + newDirName);
        File newOkDir = new File(newFileNameDir.toString().replace("\\prp", "") + "\\" + OK);
        if(!newFileNameDir.exists()){
            if(!newFileNameDir.mkdir()){
                LOGGER.warning("createDir >>>>>>>>>>>>>>>>>>>>>>>>>>> Error al crear Directorio "+newFileNameDir.getAbsolutePath());
                return null;
            }else {
                if (!newOkDir.exists()) {
                    if(!newOkDir.mkdir()){
                        LOGGER.warning("createDir >>>>>>>>>>>>>>>>>>>>>>>>>>> Error al crear Directorio "+newOkDir.getAbsolutePath());
                        return null;
                    }
                }
            }
        }
        return newOkDir.getAbsolutePath();
    }

    private static String getFileExtension(String fileName){
        int x = fileName.lastIndexOf(".");
        if(x!=-1){
            return fileName.substring(x);
        }else{
            return WITHOUT_EXTENSION;
        }
    }

    private static String getFileNameWithoutExtension(String fileName){
        return fileName.replace(getFileExtension(fileName),"").replace(" ", "_");
    }

    private static boolean onlySpaces(String s){
        for(int i=0; i<s.length();i++){
            if(s.charAt(i) != ' '){
                return false;
            }
        }
        return true;
    }

    private void createFiles(File file) throws IOException {
        String newDestinyDir = createNewDir(file);
        String fileExtension = getFileExtension(file.getName());
        File newResponseFile = new File(newDestinyDir + "\\" + RESPONSE + fileExtension);
        File newRequestFile = new File(newDestinyDir + "\\" + REQUEST + fileExtension);
        newRequestFile.createNewFile();
        try {
            Files.copy(file.toPath(), newResponseFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            LOGGER.info("Copiando archivo a la nueva carpeta: " + newDestinyDir);
        } catch (IOException e) {
            LOGGER.warning("Error al escribir el archivo: "+e);
        }
    }
}
