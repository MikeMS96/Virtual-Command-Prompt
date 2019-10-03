package com.metacube.vcp;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class for the virtual command prompt which supports cmd like
 * mkdir,ls,find,bk,cd,exit and tree
 * 
 * @author Mohit Sahu
 *
 */
public class VirtualCommandPrompt {
	private Directory root = new Directory("R:\\", null);
	private Directory currentDirectory = root;
	private Scanner scanner;
	private List<String> currentDirectoryNameFromRoot;

	public void start() {
		scanner = new Scanner(System.in);
		System.out
				.println("Welcome to Virtual Command Prompt\nCopyright (c) 2019. All rights reserved.");
		String rootDirectoryName = "R:\\";
		currentDirectoryNameFromRoot = new ArrayList<String>();
		do {
			System.out.print("\n" + rootDirectoryName);
			for (String directoryName : currentDirectoryNameFromRoot) {
				System.out.print(directoryName);
			}
			System.out.print(">");

			String[] cmd = scanner.nextLine().split("\\s+");

			switch (cmd[0]) {
			case "mkdir":
				if (cmd.length > 1) {
					createDirectory(cmd);
				} else {
					System.out.println("The syntax of the command is invalid.");
				}
				break;
			case "cd":
				if (cmd.length == 2) {
					moveToDirectory(cmd[1]);
				} else {
					System.out
							.println("The system cannot find the path specified.");
				}
				break;
			case "bk":
				moveToPreviousDirectory();
				break;
			case "ls":
				printAllDirectoriesInCurrentDirectory();
				break;
			case "find":
				if (cmd.length != 2) {
					System.out.println("The syntax of the command is invalid.");
				} else {
					List<String> pathList = getListOfPathWhichMatchesTheInput(
							currentDirectory, cmd[1], ".");
					if (pathList.size() != 0) {
						for (String path : pathList) {
							System.out.println(path);
						}
					} else {
						System.out.println("No directory found by this name");
					}
				}
				break;
			case "tree":
				System.out.println(".");
				printDirectoryTree(currentDirectory, 0);
				break;
			case "exit":
				scanner.close();
				return;
			default:
				System.out.println(cmd[0] + ": command not found");
			}
		} while (true);
	}

	/**
	 * Method to create a directory(ies)
	 * 
	 * @param cmd
	 *            name of directory(ies) in String array
	 */
	private void createDirectory(String[] nameOfDirectoryToBeCreated) {
		boolean flag;
		for (int loopVar = 1; loopVar < nameOfDirectoryToBeCreated.length; loopVar++) {
			flag = false;// To check if directory already exists ?
			// If directory to be created contains any special characters \ / :
			// ? < > * " | then it is not allowed?
			if (nameOfDirectoryToBeCreated[loopVar]
					.matches(".*[\\\\:?<>*|\"/]+.*")) {
				System.out.println("The directory name "
						+ nameOfDirectoryToBeCreated[loopVar]
						+ " is not allowed.");
			} else {
				for (Directory directory : currentDirectory.getSubDirectories()) {
					if (directory.getDirectoryName().equals(
							nameOfDirectoryToBeCreated[loopVar])) {
						System.out.println("A subdirectory "
								+ nameOfDirectoryToBeCreated[loopVar]
								+ " already exist.");
						flag = true;
						break;
					}
				}
			}

			if (!flag) {
				currentDirectory.getSubDirectories().add(
						new Directory(nameOfDirectoryToBeCreated[loopVar],
								currentDirectory));
			}
		}
	}

	/**
	 * Method to move to the directory from current directory, called by cd
	 * command
	 * 
	 * @param directoryName
	 *            directory name where we need to move from current directory
	 */
	private void moveToDirectory(String directoryName) {
		boolean directoryFound = false;
		for (Directory directory : currentDirectory.getSubDirectories()) {
			if (directory.getDirectoryName().equals(directoryName)) {
				directoryFound = true;
				currentDirectory = directory;
				if (currentDirectoryNameFromRoot.isEmpty()) {
					currentDirectoryNameFromRoot.add(directoryName);
				} else {
					currentDirectoryNameFromRoot.add("\\" + directoryName);
				}
				break;
			}
		}
		if (!directoryFound) {
			System.out.println("The system cannot find the path specified.");
		}
	}

	/**
	 * Method to move to the previous directory of the current directory, called
	 * by bk command
	 */
	private void moveToPreviousDirectory() {
		if (!currentDirectoryNameFromRoot.isEmpty()) {
			if (currentDirectoryNameFromRoot.size() != 1) {
				currentDirectoryNameFromRoot.remove("\\"
						+ currentDirectory.getDirectoryName());
			} else {
				currentDirectoryNameFromRoot.remove(currentDirectory
						.getDirectoryName());
			}
			currentDirectory = currentDirectory.getParentDirectory();
		}
	}

	/**
	 * Method to print all sub-directories in the current directory with their
	 * date and time of creation, prints number of directories at last, called
	 * by ls command
	 */
	private void printAllDirectoriesInCurrentDirectory() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(
				"dd/MM/YYYY hh:mm:ss a");
		for (Directory directory : currentDirectory.getSubDirectories()) {
			System.out.println(simpleDateFormat.format(directory.getDate())
					+ "  " + directory.getDirectoryName());
		}
		System.out.print("\t" + currentDirectory.getSubDirectories().size()
				+ " Folder(s)");
	}

	/**
	 * Method to print the directory structure from current directory i.e,
	 * directory and it's sub-directories in tree form
	 * 
	 * @param currentDirectory
	 * @param level
	 *            level of directory, it is 0 initially
	 */
	private void printDirectoryTree(Directory currentDirectory, int level) {
		List<Directory> subDirectories = currentDirectory.getSubDirectories();
		Directory lastDirectory = subDirectories.get(subDirectories.size() - 1);
		for (Directory directory : subDirectories) {
			for (int loopVar = 0; loopVar < level; loopVar++) {
				System.out.print("\u2502    ");
			}
			if (directory.equals(lastDirectory)) {
				System.out.println("\u2514\u2500\u2500"
						+ directory.getDirectoryName());
			} else {
				System.out.println("\u251c\u2500\u2500"
						+ directory.getDirectoryName());
			}
			if (directory.getSubDirectories().size() != 0) {
				printDirectoryTree(directory, level + 1);
			}

		}
	}

	/**
	 * Method to get the absolute path of all directories and its
	 * sub-directories which matches the nameOfDirectoy to be matched
	 * 
	 * @param currentDirectory
	 * @param nameOfDirectory
	 *            name by which we need to search
	 * @param previousPath
	 *            absolute path of the current directory
	 * @return List of String containing the paths
	 */
	private List<String> getListOfPathWhichMatchesTheInput(
			Directory currentDirectory, String nameOfDirectory,
			String previousPath) {
		List<String> paths = new ArrayList<String>();
		for (Directory directory : currentDirectory.getSubDirectories()) {
			if (directory.getDirectoryName().matches(nameOfDirectory + ".*")) {
				paths.add(previousPath + "\\" + directory.getDirectoryName());
			}
			if (directory.getSubDirectories().size() != 0) {
				paths.addAll(getListOfPathWhichMatchesTheInput(directory,
						nameOfDirectory,
						previousPath + "\\" + directory.getDirectoryName()));
			}
		}
		return paths;
	}
}
