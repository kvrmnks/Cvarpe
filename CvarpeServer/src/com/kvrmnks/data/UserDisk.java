package com.kvrmnks.data;

import com.kvrmnks.exception.FileExistedException;
import com.kvrmnks.exception.NoFileException;
import com.kvrmnks.exception.NoUserException;

import java.io.*;
import java.util.StringTokenizer;

public class UserDisk implements Serializable{
    private MyFile root;
    private MyFile tmpFile;

    public UserDisk(){

    }

    public UserDisk(MyFile root) {
        this.root = root;
        this.tmpFile = root;
    }

    @Deprecated
    public static UserDisk getUserDiskByName(String name) throws NoUserException, NoFileException, IOException, ClassNotFoundException {
        UserDisk ret = new UserDisk();
        long id = DataBase.getRoot(name);
        String fileName = "_" + id + "_" + DataBase.getFile(id) + ".db";
        ObjectInputStream oos = new ObjectInputStream(new FileInputStream(new File(fileName)));
        ret.tmpFile = ret.root = (MyFile) oos.readObject();
        return ret;
    }

    @Deprecated
    public static UserDisk getUserDiskByFileId(long fileId) throws IOException, NoFileException, ClassNotFoundException {
        MyFile my = RealDisk.getFileById(fileId);
        UserDisk userDisk = new UserDisk();
        userDisk.root = my;
        userDisk.tmpFile = my;
        return userDisk;
    }

    public MyFile getRoot() {
        return root;
    }

    private void create(String pos, String name) throws NoFileException {
        StringTokenizer st = new StringTokenizer(pos, "/");
        if (st.hasMoreTokens()) {
            String str = st.nextToken();
            if (tmpFile.sonDirectory.containsKey(str)) {
                tmpFile = tmpFile.sonDirectory.get(str);
                StringTokenizer stx = new StringTokenizer(pos.replaceFirst(str, ""), "/");
                if (stx.hasMoreTokens())
                    create(pos.replaceFirst(str, ""), name);
                else {
                    return;
                }
            } else throw new NoFileException();
        }
    }


    public MyFile createFileDirectory(String pos, String name) throws NoFileException, FileExistedException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst("/" + str, ""), name);
        if (!tmpFile.sonDirectory.containsKey(name)) {
            MyFile mf = new MyFile();
            mf.setName(name);
            mf.setType(MyFile.TYPEFILEDERECTORY);
            tmpFile.sonDirectory.put(name, mf);
            return tmpFile;
        } else {
            throw new FileExistedException();
        }
    }

    public MyFile createFileDirectory(long id,String name) throws NoFileException, FileExistedException {
        MyFile myFile = root.getById(id);
        if(myFile == null)
            throw new NoFileException();
        tmpFile = myFile;
        if (!tmpFile.sonDirectory.containsKey(name)) {
            MyFile mf = new MyFile();
            mf.setName(name);
            mf.setType(MyFile.TYPEFILEDERECTORY);
            tmpFile.sonDirectory.put(name, mf);
            return tmpFile;
        } else {
            throw new FileExistedException();
        }
    }

    public MyFile createFile(String pos, String name) throws NoFileException, FileExistedException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst(str, ""), name);
        if (!tmpFile.sonFile.containsKey(name)) {
            MyFile mf = new MyFile();
            mf.setName(name);
            mf.setType(MyFile.TYPEFILE);
            tmpFile.sonFile.put(name, mf);
            return tmpFile;
        } else {
            throw new FileExistedException();
        }
    }

    public MyFile createFile(long id,String name) throws NoFileException, FileExistedException {
        tmpFile = root.getById(id);
        if(tmpFile == null)
            throw new NoFileException();
        if (!tmpFile.sonFile.containsKey(name)) {
            MyFile mf = new MyFile();
            mf.setName(name);
            mf.setType(MyFile.TYPEFILE);
            tmpFile.sonFile.put(name, mf);
            return tmpFile;
        } else {
            throw new FileExistedException();
        }
    }

    @Deprecated
    public MyFile deleteFile(String pos, String name) throws NoFileException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst("/" + str, ""), name);
        if (tmpFile.sonFile.containsKey(name)) {
            MyFile file = tmpFile.sonFile.get(name);
            tmpFile.sonFile.remove(name);
            return file;
        } else {
            throw new NoFileException();
        }
    }


    public MyFile deleteFile(long id,String name) throws NoFileException {
        tmpFile = root.getById(id);
        if(tmpFile == null) throw  new NoFileException();
        if (tmpFile.sonFile.containsKey(name)) {
            MyFile file = tmpFile.sonFile.get(name);
            tmpFile.sonFile.remove(name);
            DataBase.replaceMyFile(file.getId(),file);
            //root.mainTain();
            return file;
        } else {
            throw new NoFileException();
        }
    }

    @Deprecated
    public MyFile deleteFileDirectory(String pos, String name) throws NoFileException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst("/" + str, ""), name);
        if (tmpFile.sonDirectory.containsKey(name)) {
            MyFile file = tmpFile.sonDirectory.get(name);
            tmpFile.sonDirectory.remove(name);
            return file;
        } else {
            throw new NoFileException();
        }
    }


    public MyFile deleteFileDirectory(long id,String name) throws NoFileException {
        tmpFile = root.getById(id);
        if(tmpFile == null) throw new NoFileException();
        if (tmpFile.sonDirectory.containsKey(name)) {
            MyFile file = tmpFile.sonDirectory.get(name);
            tmpFile.sonDirectory.remove(name);
            return file;
        } else {
            throw new NoFileException();
        }
    }

    @Deprecated
    public MyFile renameFile(String pos, String name, String newName) throws NoFileException, FileExistedException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst(str, ""), name);
        if (tmpFile.sonFile.containsKey(name) && !tmpFile.sonFile.containsKey(newName)) {
            tmpFile.sonFile.put(newName, tmpFile.sonFile.get(name));
            tmpFile.sonFile.remove(name);
            return tmpFile;
        } else if (!tmpFile.sonFile.containsKey(name)) {
            throw new NoFileException();
        } else {
            throw new FileExistedException();
        }
    }

    public MyFile renameFile(long id,String name,String newName) throws NoFileException, FileExistedException {
        tmpFile = root.getById(id);
        if (tmpFile.sonFile.containsKey(name) && !tmpFile.sonFile.containsKey(newName)) {
           // tmpFile.sonFile.get(name).setName(newName);
            tmpFile.sonFile.put(newName, tmpFile.sonFile.get(name));
            tmpFile.sonFile.remove(name);
            return tmpFile;
        } else if (!tmpFile.sonFile.containsKey(name)) {
            throw new NoFileException();
        } else {
            throw new FileExistedException();
        }
    }

    @Deprecated
    public MyFile renameFileDirectory(String pos, String name, String newName) throws NoFileException, FileExistedException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst(str, ""), name);
        if (tmpFile.sonDirectory.containsKey(name) && !tmpFile.sonDirectory.containsKey(newName)) {
            tmpFile.sonDirectory.put(newName, tmpFile.sonDirectory.get(name));
            tmpFile.sonDirectory.remove(name);
            return tmpFile;
        } else if (!tmpFile.sonDirectory.containsKey(name)) {
            throw new NoFileException();
        } else {
            throw new FileExistedException();
        }
    }

    public MyFile renameFileDirectory(long id,String name,String newName) throws NoFileException, FileExistedException {
        tmpFile = root.getById(id);
        if (tmpFile.sonDirectory.containsKey(name) && !tmpFile.sonDirectory.containsKey(newName)) {
            tmpFile.sonDirectory.put(newName, tmpFile.sonDirectory.get(name));
            tmpFile.sonDirectory.remove(name);
            return tmpFile;
        } else if (!tmpFile.sonDirectory.containsKey(name)) {
            throw new NoFileException();
        } else {
            throw new FileExistedException();
        }
    }

    public long getFileId(String pos, String name) throws NoFileException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst(str, ""), name);
        if (tmpFile.sonFile.containsKey(name)) {
            return tmpFile.sonFile.get(name).getId();
        } else {
            throw new NoFileException();
        }
    }

    public long getFileId(long id,String name) throws NoFileException {


        //tmpFile = root.getById(id);
        tmpFile = DataBase.getMyFileById(id);
        if (tmpFile.sonFile.containsKey(name)) {
            return tmpFile.sonFile.get(name).getId();
        } else {
            throw new NoFileException();
        }
    }
    @Deprecated
    public long getFileDirectoryId(String pos, String name) throws NoFileException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken();
        create(pos.replaceFirst(str, ""), name);
        if (tmpFile.sonDirectory.containsKey(name)) {
            return tmpFile.sonDirectory.get(name).getId();
        } else {
            throw new NoFileException();
        }
    }

    public long getFileDirectoryId(long id,String name) throws NoFileException {
        tmpFile = root.getById(id);
        if (tmpFile.sonDirectory.containsKey(name)) {
            return tmpFile.sonDirectory.get(name).getId();
        } else {
            throw new NoFileException();
        }
    }

    @Deprecated
    public MyFile getStructure(String pos) throws NoFileException {
        tmpFile = root;
        StringTokenizer st = new StringTokenizer(pos, "/");
        String str = st.nextToken(), name = "";
        create(pos.replaceFirst(str, ""), name);
        MyFile mf = tmpFile;
        return mf;
    }

    public MyFile getStructure(long id) throws NoFileException {
        return root.getById(id);
    }
}
