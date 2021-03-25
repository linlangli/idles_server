package io.github.grooters.idles.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.RemoteAddCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.dircache.DirCache;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.transport.URIish;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;

import java.io.*;
import java.net.URISyntaxException;
import java.util.Objects;

public class FileIOer {

    public static void writeString(String fileName, String message){

        File file = new File(fileName);

        try {
            FileWriter writer = new FileWriter(file);
            writer.write(message);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String readString(String fileName){

        File file = new File(fileName);
        StringBuilder message = new StringBuilder();

        try {
            FileReader reader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(reader);
            String temp;

            while ((temp = bufferedReader.readLine()) != null){
                message.append(temp);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message.toString();
    }

    public static boolean delete(String fileName){

        File file = new File(fileName);

        if(file.exists())
            return file.delete();
        else
            return false;
    }

    public static File inputStreamToFile(InputStream inputStream, String path){
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            byte[] bytes = new byte[1024];
            File file = new File(path);

            if(file.exists()) {
                file.delete();
            }

            if(file.createNewFile()){
                Printer.print("FileIOer", "file.createNewFile()");
            }

            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            int length;
            while ((length = Objects.requireNonNull(bufferedInputStream).read(bytes)) != -1){
                bufferedOutputStream.write(bytes, 0, length);
                bufferedOutputStream.flush();
            }

            inputStream.close();
            bufferedInputStream.close();
            bufferedOutputStream.close();

            return file;
        } catch (IOException e) {

            e.printStackTrace();

        }
        return null;
    }

    public static void pushImageToCloud(File file){

        UsernamePasswordCredentialsProvider user = new UsernamePasswordCredentialsProvider("lilinlang", "L^3-github");

        try {

//            Git git = Git.init().setDirectory(file).call();
//
//            RemoteAddCommand remoteAddCommand = git.remoteAdd();
//
//            remoteAddCommand.setName("origin");
//
//            remoteAddCommand.setUri(new URIish("https://github.com/lilinlang/lilinlang.github.io.git"));
//
//            remoteAddCommand.call();
//
////            git.remoteSetUrl();
//
//            git.add().addFilepattern(".").call();
//
//            git.commit().setMessage("init").call();
//
//            git.push().setCredentialsProvider(user).call();

//            Git git=Git.cloneRepository()
//                    .setURI("https://github.com/lilinlang/lilinlang.github.io.git")
//                    .setDirectory(file)
//                    .call();

            Git git=Git.open(file);

            git.add().addFilepattern(".").call();

            git.commit().setMessage("init").call();

            git.push().setCredentialsProvider(user).call();


        } catch (GitAPIException | IOException e) {

            e.printStackTrace();

        }

    }

}
