package backend;

import javafx.stage.FileChooser;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PluginLoader{
    private Object pluginObject;
    private Method[] methods;
    public PluginLoader(File pluginPath){
        try{

            JarFile plugin = new JarFile(pluginPath);
            Enumeration<JarEntry> entries = plugin.entries();

            URL[] urls = { new URL("jar:file:" + pluginPath+"!/") };
            URLClassLoader classLoader = URLClassLoader.newInstance(urls);

            while (entries.hasMoreElements()) {
                JarEntry jar = entries.nextElement();

                if(!jar.getName().endsWith(".class") || jar.isDirectory()){
                    continue;
                }

                String cName = jar.getName().substring(0,jar.getName().length() - ".class".length());
                cName = cName.replace('/', '.');

                Class c = classLoader.loadClass(cName);
                if(c.getSimpleName().equals("Main")){
                    pluginObject = c.getMethod("getInstance").invoke(null);
                    methods = c.getMethods();
                }
            }
        } catch (Exception e){
            System.out.println(":D");
        }
    }

    public Object invokeMethod(String methodName, Object... args){
        for(Method m:methods){
            if(m.getName().equals(methodName)){
                try{
                    return m.invoke(pluginObject, args);
                } catch (Exception e) {
                    System.out.println(":D");
                }
            }
        }
        return null;
    }

}
