/*
 * FileName: MFile.java
 * 
 * Description: create MFile class
 * 
 * History:
 * 1.0 SHUYUFANG 2013-08-16 Create
 */


package ARH.framework.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.*;

import ARH.framework.exception.MException;

/**
 * This class defines common file operations
 * @author SHUYUFANG
 * @version 1.0
 */
public class MFile
{
    /**
     * Constructor
     */
    private MFile()
    {
    }
    
    /**
     * Check file directory is existed or not
     * @param fileName the path of the file
     * @return true if exist, or false 
     */
    public static boolean isFileDirExist(String fileName)
    {
        String parentPath = new File(fileName).getParent();
        return new File(parentPath).exists();
    }
    
    /**
     * Get parent path
     * @param path directory path
     * @return parent path
     */
    public static String getParentDir(String path)
    {
        return new File(path).getParent();
    }     
    /**
     * Make directory
     * @param dir the directory path to create
     * @return true if create success, otherwise false 
     */
    public static boolean makeDir(String dir)
    {
        File file = new File(dir);
        
        if (file.exists() == false)
        {
            return file.mkdirs();
        }
        
        return false;
    }
    
    /**
     * Copy file
     * @param src the path of source file
     * @param dest the path of destination file
     * @return true if copy success, otherwise false
     */
    public synchronized static boolean copyFile(String src, String dest) throws MException
    {
        boolean res = false;
        
        if (!MFile.isExisted(src))
        {
            return res;
        }
        
        if (!MFile.isFileDirExist(dest))
        {
            String dir = MFile.getParentDir(dest);
            if (!MFile.makeDir(dir))
            {
                return res;
            }
        }
        
        MFile.delFile(dest);
        
        try 
        {
            FileInputStream in = new FileInputStream(src);
            FileOutputStream out = new FileOutputStream(dest);
            
            FileChannel inChannel = in.getChannel();
            FileChannel outChannel = out.getChannel();
            
            int len = 1024;
            
            ByteBuffer byteBuffer=null;
            while (true)
            {
                if (inChannel.position() == inChannel.size())
                {
                    inChannel.close();
                    outChannel.close();
                    in.close();
                    out.close();
                    res = true;
                    break;
                }
                else if (inChannel.size() - inChannel.position() < len)
                {
                    len = (int) (inChannel.size() - inChannel.position());
                }
                    
                byteBuffer = ByteBuffer.allocateDirect(len);
                inChannel.read(byteBuffer);
                byteBuffer.flip();
                outChannel.write(byteBuffer);
                outChannel.force(false);
            }
            
        } 
        catch (IOException e)
        {
            res = false;
        }
        
        return res;
    }
    
    /**
     * The file is exist or not
     * @param filePath the path of the file
     * @return file existed or not
     */
    public static boolean isExisted(String filePath)
    {
        boolean res = false;
        
        File file = new File(filePath);
        res = file.exists();
        
        return res;
    }
    
    /**
     * Create new file
     * if file is already existed, return false
     * @param filePath the path of the file
     * @return true if create success,otherwise false
     */
    public static synchronized boolean genFile(String filePath) throws MException
    {
        boolean res = false;
        
        try
        {
            File file = new File(filePath);
            
            if (!file.exists())
            {
                if (!MFile.isFileDirExist(filePath))
                {
                    String parPath = MFile.getParentDir(filePath);
                    if (MFile.makeDir(parPath) == false)
                    {
                        res = false;
                    }
                    else
                    {
                        res = file.createNewFile();
                    }
                }
                else
                {
                    res = file.createNewFile();
                }
            }
            else
            {
                res = false;
            }
        }
        catch (IOException e)
        {
            throw new MException("Fail to create file \"" + filePath + "\".");
        }
        
        return res;
    }
    
    /**
     * Delete file
     * if file is not existed, do nothing
     * @param filePath the path of the file
     * @return true while the file not exist, false otherwise
     */
    public static synchronized boolean delFile(String filePath) throws MException
    {
        boolean res = false; 
        if (null == filePath || filePath.trim().equals(""))
        {
            return res;
        }
        
        File file = new File(filePath);
        if (file.exists())
        {
            res = file.delete();
        }
        
        return res;
    }
    
    /**
     * Write file.
     * If the file not exist, it would be created
     * @param filePath the path of the file
     * @param content the content to write to the file
     * @param append append content to the file or not
     * @return true if success,otherwise false 
     */
    protected static boolean writeFile(String filePath, String content, boolean append) throws MException
    {
        boolean res = false;
        
        if (!MFile.isFileDirExist(filePath))
        {
            String dir = MFile.getParentDir(filePath);
            MFile.makeDir(dir);
        }
        
        try
        {
            FileOutputStream out = new FileOutputStream(filePath, append);
            out.write(content.getBytes());
            out.flush();
            out.close();
            res = true;
        }
        catch (FileNotFoundException e)
        {
            throw new MException("The file \"" + filePath + "\" doesn't exist.");
        }
        catch (IOException e)
        {
            throw new MException("Fail to write file \"" + filePath + "\".");
        }
        
        return res;
    }
    
    /**
     * Append content to file
     * @param filePath the path of the file
     * @param content the content to write to the file
     * @return content true if success,otherwise false
     */
    public static boolean appendFile(String filePath, String content) throws MException
    {
        return MFile.writeFile(filePath, content, true);
    }
    
    /**
     * Append content to file
     * @param filePath the path of the file
     * @param content the content to write to the file
     * @return content true if success,otherwise false
     */
    public static boolean appendFileLn(String filePath, String content) throws MException
    {
        return MFile.writeFile(filePath, content + "\n", true);
    }
    
    /**
     * Call write(filePath, content, false)
     * @param filePath the path of the file
     * @param content the content to write to the file
     * @return result of write(filePath, content, false)
     */
    public static boolean writeFile(String filePath, String content) throws MException
    {
        return MFile.writeFile(filePath, content, false);
    }
    
    /**
     * Read file
     * @param filePath file path
     * @return null if file not exist, file content as String if file existed
     */
    public static String readFile(String filePath) throws MException
    {
        String content = null;
        
        if (!MFile.isExisted(filePath))
        {
            return content;
        }
        
        try
        {
            content = "";
            FileInputStream in = new FileInputStream(filePath);
            int len = 0;
            
            byte[] buffer = new byte[1024];
            while (true)
            {
                Arrays.fill(buffer, (byte) 0);
                
                len = in.read(buffer);
                if (len == -1)
                {
                    in.close();
                    break;
                }
                else if (len < 1024)
                {
                    String s = new String(buffer);
                    content += s.substring(0, len);
                    in.close();
                    break;
                }
                
                content += new String(buffer);
            }
        }
        catch (FileNotFoundException e)
        {
            throw new MException("The file \"" + filePath + "\" doesn't exist.");
        }
        catch (IOException e)
        {
            throw new MException("Fail to read file \"" + filePath + "\".");
        }
        
        return content;
    }
    
    /**
     * Read content
     * @param filePath path of the file
     * @return null if file not existed, file content as ArrayList<String> if file existed
     */
    public static ArrayList<String> readFileLines(String filePath) throws MException
    {
        ArrayList<String> contents;
        
        if (!MFile.isExisted(filePath))
        {
            return null;
        }
        
        try
        {
            contents = new ArrayList<String>();
            File file = new File(filePath);
            BufferedReader in = new BufferedReader(new FileReader(file));
            String line;
            
            while (null != (line = in.readLine()))
            {
                contents.add(line);
            }
            
            in.close();
        }
        catch (FileNotFoundException e)
        {
            throw new MException("The file \"" + filePath + "\" doesn't exist.");
        }
        catch (IOException e)
        {
            throw new MException("Fail to read file \"" + filePath + "\".");
        }
        
        return contents;
    }
}