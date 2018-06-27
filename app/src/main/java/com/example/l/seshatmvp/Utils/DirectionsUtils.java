package com.example.l.seshatmvp.Utils;

import com.example.l.seshatmvp.model.Direction;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.Stack;

public class DirectionsUtils {

    public static Direction[] getDirections(String filepath, int version) {
        Stack<Direction> directions = new Stack<>();
        File file = new File(filepath);

        try {
            int foundedversions = 0;
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                String line = null;
                line = scan.nextLine();
                switch (line.charAt(0)) {
                    case 'I':
                        foundedversions++;
                        break;
                    case 'E':
                        if (foundedversions > version) {
                            Direction[] result = new Direction[directions.size()];
                            return directions.toArray(result);
                        } else {
                            directions.clear();
                        }
                        break;
                    case 'L':
                        directions.push(Direction.LEFT);
                        break;
                    case 'R':
                        directions.push(Direction.RIGHT);
                        break;
                    case 'U':
                        directions.push(Direction.UP);
                        break;
                    case 'D':
                        directions.push(Direction.DOWN);
                        break;
                    case 'S':
                        directions.push(Direction.SAME);
                        break;
                    case 'N':
                        directions.push(Direction.NOMATTER);
                        break;
                }
            }
            scan.close();
        } catch (FileNotFoundException e) {
            System.out.println(e.toString());
        }
        Direction[] result = new Direction[directions.size()];
        return directions.toArray(result);
    }
}
