package com.scottlandyard;

import javafx.scene.Node;
import javafx.scene.Parent;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class GameProcessing {
    public static Integer[] getArr(String file) throws IOException {
        List<Integer> listOfInts
                = new ArrayList<>();

        BufferedReader bf = new BufferedReader(
                new FileReader(file));

        String line = bf.readLine();

        while (line != null) {
            listOfInts.add(Integer.parseInt(line));
            line = bf.readLine();
        }

        bf.close();

        return listOfInts.toArray(new Integer[0]);
    }

    public static Integer[] getStartPos() {
        List<Integer> allStartPositions = new ArrayList<>();
        allStartPositions.add(197);
        allStartPositions.add(132);
        allStartPositions.add(94);
        allStartPositions.add(103);
        allStartPositions.add(26);
        allStartPositions.add(141);
        allStartPositions.add(112);
        allStartPositions.add(91);
        allStartPositions.add(155);
        allStartPositions.add(34);
        allStartPositions.add(29);
        allStartPositions.add(50);
        allStartPositions.add(53);
        allStartPositions.add(198);
        allStartPositions.add(174);
        allStartPositions.add(13);
        allStartPositions.add(138);
        allStartPositions.add(117);
        List<Integer> inGamePositions = new ArrayList<>();
        int b = 18;
        for (int i = 0; i < 5; i++) {
            double value = Math.random() * b;
            Double newValue = value;
            int a = newValue.intValue();
            inGamePositions.add(allStartPositions.get(a));
            allStartPositions.remove(a);
            b = b - 1;
        }
        return allStartPositions.toArray(new Integer[0]);
    }

    public static <T, E> T getKeyByValue(Map<T, E> map, E value) {
        for (Map.Entry<T, E> entry : map.entrySet()) {
            if (Objects.equals(value, entry.getValue())) {
                return entry.getKey();
            }
        }
        return null;
    }

    public static ArrayList<Node> getAllNodes(Parent root) {
        ArrayList<Node> nodes = new ArrayList<Node>();
        addAllDescendents(root, nodes);
        return nodes;
    }

    private static void addAllDescendents(Parent parent, ArrayList<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent)
                addAllDescendents((Parent)node, nodes);
        }
    }

    public static Deque<Integer> pathsSplitter() {
        String[] paths = readPaths();
        Deque<Integer> pathsDeque = new ArrayDeque<>();

        for (String path : paths) {
            String[] split = path.split(" -");
            pathsDeque.add(Integer.parseInt(split[0]));
            split = split[1].split("> ");
            pathsDeque.add(Integer.parseInt(split[0]));
            pathsDeque.add(Integer.parseInt(split[1]));
        }

        return pathsDeque;
    }

    private static String[] readPaths() {
        BufferedReader reader;
        List<String> paths = new ArrayList<>();

        try {
            File file = new File("src/main/resources/com/scottlandyard/files/paths.txt");
            reader = new BufferedReader(new FileReader(file));
            String line = reader.readLine();

            while (line != null) {
                paths.add(line);
                // read next line
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return paths.toArray(new String[0]);
    }
}
