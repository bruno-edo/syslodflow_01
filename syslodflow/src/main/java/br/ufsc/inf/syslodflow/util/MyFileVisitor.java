package br.ufsc.inf.syslodflow.util;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;

public class MyFileVisitor extends SimpleFileVisitor<Path> {

	private List<Path> listFiles = new ArrayList<Path>();
	
	public FileVisitResult visitFile(Path path, BasicFileAttributes fileAttributes){
		listFiles.add(path);
		System.out.println("Nome do arquivo:" + path.getFileName());
		return FileVisitResult.CONTINUE;
	}
	public FileVisitResult preVisitDirectory(Path path, BasicFileAttributes fileAttributes){
		System.out.println("----------Nome do diretório:" + path + "----------");
		return FileVisitResult.CONTINUE;
	}
	public List<Path> getListFiles() {
		return listFiles;
	}
	
	

}
