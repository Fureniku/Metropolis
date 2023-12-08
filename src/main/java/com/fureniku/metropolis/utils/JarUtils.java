package com.fureniku.metropolis.utils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Handle some jarfile stuff for file generation
 */
public class JarUtils {

    private final Path JAR_PATH;
    private final String MODEL_PATH_BLOCK;
    private final String MODEL_PATH_ITEM;
    private final String TEXTURE_PATH_BLOCK;
    private final String TEXTURE_PATH_ITEM;

    private final String BLOCKSTATE_PATH;

    private String _blockStatePath = "/blockstates/";
    private String _modelPathBlock = "/models/block/";
    private String _modelPathItem = "/models/item/";
    private String _texturePathBlock = "/textures/block/";
    private String _texturePathItem = "/textures/item/";

    public JarUtils(Class modClass, String modid) throws URISyntaxException {
        JAR_PATH = getFilePath(modClass);
        BLOCKSTATE_PATH = "assets/" + modid + _blockStatePath;
        MODEL_PATH_BLOCK = "assets/" + modid + _modelPathBlock;
        MODEL_PATH_ITEM = "assets/" + modid + _modelPathItem;
        TEXTURE_PATH_BLOCK = "assets/" + modid + _texturePathBlock;
        TEXTURE_PATH_ITEM = "assets/" + modid + _texturePathItem;
    }

    public void printPaths() {
        Debug.Log("Jar path: " + JAR_PATH.toAbsolutePath());
        //Debug.Log("Blockstate path: " + BLOCKSTATE_PATH.toAbsolutePath());
        Debug.Log("Block Model path: " + JAR_PATH + MODEL_PATH_BLOCK);
        Debug.Log("Item Model path: " + JAR_PATH + MODEL_PATH_ITEM);
        Debug.Log("Block Texture path: " + JAR_PATH + TEXTURE_PATH_BLOCK);
        Debug.Log("Item Texture path: " + JAR_PATH + TEXTURE_PATH_ITEM);
    }

    private Path getFilePath(Class modClass) {

            // Try to get the JAR file path
        URL resourceUrl = modClass.getResource("");
        Debug.Log("Resource URL searchable: " + resourceUrl);
        Debug.Log("Paths get searchable: " + Paths.get(""));
            /*String jarPath = modClass.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            Path pathFromJar = Paths.get(jarPath);

            // Check if running from a JAR file
            if (pathFromJar.toString().endsWith(".jar")) {
                // If running from a JAR, you can adjust the path as needed
                Debug.Log("In jar");
                return Paths.get("path/inside/jar/resource.txt");
            } else {
                Debug.Log("Not in jar");
                // If not running from a JAR, use a direct path for development
                return Paths.get("path/in/development/resource.txt");
            }*/
        return Paths.get("");
    }

    private String readFileInJar(String fileName, Class modClass) throws IOException {
        StringBuilder sb = new StringBuilder();
        fileName = BLOCKSTATE_PATH + fileName;
        URL jarUrl = modClass.getProtectionDomain().getCodeSource().getLocation();
        JarURLConnection jarConnection = (JarURLConnection)jarUrl.openConnection();
        JarFile jar = jarConnection.getJarFile();
        JarEntry entry = jar.getJarEntry(fileName);

        if (entry != null) {
            InputStream inputStream = jar.getInputStream(entry);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            reader.close();
            inputStream.close();
        }
        jar.close();
        return sb.toString();
    }

    private static boolean compiledSystem = false;

    private static ArrayList<File> getJsonFiles(String dir, ArrayList<File> jsonList) {
        File directory = new File(dir);
        File[] files = directory.listFiles();

        //Above only works in dev environment; for compiled .jar we need to handle differently.
        if (files == null) {
            Debug.Log("Running in production environment; switching to jarfile extraction for internal paint JSONs");
            //return getCompiledJsonFiles(jsonList);
        }

        if (files.length > 0) {
            for (File file : files) {
                if (file != null) {
                    if (file.isFile()) {
                        jsonList.add(file);
                    } else if (file.isDirectory()) {
                        getJsonFiles(file.getAbsolutePath(), jsonList);
                    }
                }
            }
        }

        return jsonList;
    }

    /*private static ArrayList<File> getCompiledJsonFiles(ArrayList<File> foundFiles) {
        compiledSystem = true;
        try {
            URL jarUrl = FurenikusRoads.class.getProtectionDomain().getCodeSource().getLocation();
            JarURLConnection jarConnection = (JarURLConnection)jarUrl.openConnection();
            JarFile jar = jarConnection.getJarFile();
            Enumeration<JarEntry> entries = jar.entries();

            int entryCount = 0;

            while (entries.hasMoreElements()) {
                entryCount++;
                JarEntry entry = entries.nextElement();

                if (!entry.isDirectory() && entry.getName().startsWith("assets/" + FurenikusRoads.MODID + "/paints")) {
                    File file = new File(entry.getName());
                    if (file.getName().endsWith(".json")) {
                        foundFiles.add(file);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return foundFiles;
    }*/
}
