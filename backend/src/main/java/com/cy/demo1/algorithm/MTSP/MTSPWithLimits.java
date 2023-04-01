package com.cy.demo1.algorithm.MTSP;

import java.util.*;

/**
 * MTSPWithLimits 类实现了多个无人机巡航的 mTSP 问题，使用贪心算法，目的是总用时最短（即走最长路径的无人机的路径应该尽可能短）
 * 同时增加了每个无人机的最大飞行距离限制，并尽可能最大化利用无人机，以减少无人机的使用数量。
 * 使用方法：
 * int[][] paths = MTSPWithLimits.solveMTSP(data, numberOfDrones, limitDistance, priorAllDrones);
 * eg.  0 号无人机需要经过 0 3 4 2 6 -> path[0] = {0,3,4,2,6}
 */
public class MTSPWithLimits {
    /*
     *    * solveMTSP 方法实现了解决多个无人机巡航的 mTSP 问题
     *    * @param data 数据对象，包括距离矩阵和出发点及禁飞区
     *    * @param numberOfDrones 无人机数量
     *    * @param limitDistance 每个无人机的最大限制飞行距离
     *    * @param priorAllDrones 是否优先启动所有无人机
     *    * @return 返回一个包含各无人机路径的二维数组
     */

    // 包含全部的节点及与禁飞区的交点、禁飞区本身的交点（initForbiddenZonesPoints）
    public static DataForMultipleDrones dataForMultipleDronesWithForbiddenZones;
    public static double[][] adjacencyMatrix;

    public static int[][] solveMTSP(DataForMultipleDrones dataForMultipleDrones, int numberOfDrones, double limitDistance, boolean prioritizeAllDrones) {
        int[][] paths = new int[numberOfDrones][];
        double[] pathDistances = new double[numberOfDrones];

        // 最大尝试次数
        int maxAttempts = 10000;
        int attempts = 0;

        // 更新 dataWithForbiddenZones
        initForbiddenZonesPoints(dataForMultipleDrones);

        adjacencyMatrix = calculateDistances(dataForMultipleDronesWithForbiddenZones);


        List<Integer>[][] forbiddenRounds = updateDistancesWithDijkstra(adjacencyMatrix, dataForMultipleDronesWithForbiddenZones);

        PriorityQueue<Edge> edgeQueue = new PriorityQueue<>((a, b) -> {
            if (a.distance < b.distance) return -1;
            else if (a.distance > b.distance) return 1;
            return 0;
        });

        for (int i = 1; i < dataForMultipleDrones.num; i++) {
            edgeQueue.offer(new Edge(0, i, adjacencyMatrix[0][i]));
        }

        int[] visitedCount = new int[dataForMultipleDrones.num];
        visitedCount[0] = numberOfDrones;
        List<List<Integer>> dronePaths = new ArrayList<>();
        for (int i = 0; i < numberOfDrones; i++) {
            dronePaths.add(new ArrayList<>(Collections.singletonList(0)));
        }

        while (!edgeQueue.isEmpty() && attempts < maxAttempts) {

            Edge edge = edgeQueue.poll();
            if (visitedCount[edge.to] == 0) {
                int droneIndex = findDroneWithShortestPath(adjacencyMatrix, dronePaths, pathDistances, edge.to, limitDistance, prioritizeAllDrones);
                if (droneIndex != -1) {

                    List<Integer> path = dronePaths.get(droneIndex);
                    int lastNode = path.get(path.size() - 1);
                    double currentDistance = adjacencyMatrix[lastNode][edge.to];
                    double returnDistance = adjacencyMatrix[edge.to][0];

                    if (pathDistances[droneIndex] + currentDistance + returnDistance <= limitDistance) {

                        // 如果存在中间节点，就加上
                        if (forbiddenRounds[lastNode][edge.to] != null) {
                            path.addAll(forbiddenRounds[lastNode][edge.to]);

                        }

                        path.add(edge.to);
                        System.out.println("ADD2: " + edge.to);
                        pathDistances[droneIndex] += currentDistance;

                        visitedCount[edge.to] = 1;

                        for (int i = 1; i < dataForMultipleDrones.num; i++) {
                            if (visitedCount[i] == 0) {
                                edgeQueue.offer(new Edge(edge.to, i, adjacencyMatrix[edge.to][i]));
                            }
                        }
                    }
                } else {
                    // 如果找不到适合的无人机，将边重新放回队列，以便在后续尝试中重新评估
                    edgeQueue.offer(edge);
                }
            }
            attempts++;
        }


        if (attempts >= maxAttempts) {
            return new int[][]{}; // 如果达到最大尝试次数，返回空数组
        }

        // Check if all nodes have been visited
        for (int i = 1; i < dataForMultipleDrones.num; i++) {
            if (visitedCount[i] == 0) {
                return new int[][]{}; // Return empty array if not all nodes can be visited
            }
        }

        for (int i = 0; i < numberOfDrones; i++) {

            List<Integer> pathList = dronePaths.get(i);
            // 如果存在中间节点，就加上
            double currentDistance = adjacencyMatrix[pathList.get(pathList.size() - 1)][0];
            if (forbiddenRounds[pathList.get(pathList.size() - 1)][0] != null) {
                pathList.addAll(forbiddenRounds[pathList.get(pathList.size() - 1)][0]);

            }
            pathList.add(0);
            paths[i] = pathList.stream().mapToInt(Integer::intValue).toArray();
            pathDistances[i] += currentDistance;

            System.out.println("DIS " + i + ": " + pathDistances[i]);
            System.out.println(Arrays.toString(paths[i]));
        }
        //
        if (paths.length == 0)
        {
            optimize(paths, adjacencyMatrix, forbiddenRounds, pathDistances, limitDistance);
        }
        return paths;
    }



    public static int[][] optimize(int[][] paths, double[][] distanceMatrix, List<Integer>[][] forbiddenRounds, double[] pathDistances, double maxRange) {
        boolean improvement = true;
        while (improvement) {
            improvement = false;
            for (int i = 0; i < paths.length; i++) {
                for (int j = 0; j < paths[i].length - 1; j++) {
                    for (int i2 = i; i2 < paths.length; i2++) {
                        int kStart = (i2 == i) ? j + 1 : 0;
                        for (int k = kStart; k < paths[i2].length - 1; k++) {
                            if (forbiddenRounds[paths[i][j]][paths[i2][k]] == null &&
                                    forbiddenRounds[paths[i][j + 1]][paths[i2][k + 1]] == null) {
                                double currentDistance = distanceMatrix[paths[i][j]][paths[i][j + 1]] + distanceMatrix[paths[i2][k]][paths[i2][k + 1]];
                                double newDistance = distanceMatrix[paths[i][j]][paths[i2][k]] + distanceMatrix[paths[i][j + 1]][paths[i2][k + 1]];
                                if (newDistance < currentDistance) {
                                    double newPathDistanceI = pathDistances[i] - distanceMatrix[paths[i][j]][paths[i][j + 1]] + distanceMatrix[paths[i][j]][paths[i2][k]];
                                    double newPathDistanceI2 = pathDistances[i2] - distanceMatrix[paths[i2][k]][paths[i2][k + 1]] + distanceMatrix[paths[i][j + 1]][paths[i2][k + 1]];
                                    if (newPathDistanceI <= maxRange && newPathDistanceI2 <= maxRange) {
                                        if (i == i2) {
                                            reverse(paths[i], j + 1, k);
                                        } else {
                                            swap(paths, i, j + 1, i2, k);
                                        }
                                        improvement = true;
                                        pathDistances[i] = newPathDistanceI;
                                        pathDistances[i2] = newPathDistanceI2;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return paths;
    }

    private static void reverse(int[] path, int i, int j) {
        while (i < j) {
            int temp = path[i];
            path[i] = path[j];
            path[j] = temp;
            i++;
            j--;
        }
    }

    private static void swap(int[][] paths, int i1, int j1, int i2, int j2) {
        int[] tempPath = new int[j2 - j1 + 1];
        System.arraycopy(paths[i2], j1, tempPath, 0, j2 - j1 + 1);
        System.arraycopy(paths[i1], j1, paths[i2], j1, j2 - j1 + 1);
        System.arraycopy(tempPath, 0, paths[i1], j1, j2 - j1 + 1);
    }


    public static void initForbiddenZonesPoints(DataForMultipleDrones dataForMultipleDrones) {

        //FIXME
//        dataWithForbiddenZones = data;

        dataForMultipleDronesWithForbiddenZones = new DataForMultipleDrones();

        ArrayList<Double> xList = new ArrayList<>();
        ArrayList<Double> yList = new ArrayList<>();
        // 加入全部节点
        for (double value : dataForMultipleDrones.x) {
            xList.add(value);
        }
        for (double value : dataForMultipleDrones.y) {
            yList.add(value);
        }
        if (dataForMultipleDrones.forbiddenZones != null) {
            // 将禁飞区节点加入
            for (ForbiddenZone zone : dataForMultipleDrones.forbiddenZones) {
                for (double value : zone.x) {
                    xList.add(value);
                    System.out.println(xList.size() - 1);
                }
                for (double value : zone.y) {
                    yList.add(value);
                }
            }
        }


        dataForMultipleDronesWithForbiddenZones.x = xList.stream().mapToDouble(Double::doubleValue).toArray();
        dataForMultipleDronesWithForbiddenZones.y = yList.stream().mapToDouble(Double::doubleValue).toArray();
        dataForMultipleDronesWithForbiddenZones.num = dataForMultipleDronesWithForbiddenZones.x.length;

        dataForMultipleDronesWithForbiddenZones.forbiddenZones = dataForMultipleDrones.forbiddenZones;

        // 此时，包含全部的节点及与禁飞区的交点、禁飞区本身的交点

    }


    // 计算距离，如果有交点，就把它们的距离设为最大值
    private static double[][] calculateDistances(DataForMultipleDrones dataForMultipleDrones) {

        double R = 6371e3; // 地球半径，单位为米
        double[][] distances = new double[dataForMultipleDrones.num][dataForMultipleDrones.num];
        for (int i = 0; i < dataForMultipleDrones.num - 1; i++) {
            for (int j = i + 1; j < dataForMultipleDrones.num; j++) {
                // TODO: 判断两个点是否为禁区节点且连线穿过禁区，如果是则距离无限大
                if (dataForMultipleDrones.isCrossingForbiddenZone(dataForMultipleDrones.x[i], dataForMultipleDrones.y[i], dataForMultipleDrones.x[j], dataForMultipleDrones.y[j])) {
                    distances[i][j] = Double.MAX_VALUE;
                    distances[j][i] = Double.MAX_VALUE;

                } else {
                    // 计算普通距离
                    // double distance = Math.sqrt(Math.pow(data.x[i] - data.x[j], 2) + Math.pow(data.y[i] - data.y[j], 2));
                    // 使用经纬度计算距离
                    double lat1Radians = Math.toRadians(dataForMultipleDrones.x[i]);
                    double lat2Radians = Math.toRadians(dataForMultipleDrones.x[j]);
                    double deltaLatRadians = Math.toRadians(dataForMultipleDrones.x[j] - dataForMultipleDrones.x[i]);
                    double deltaLonRadians = Math.toRadians(dataForMultipleDrones.y[j] - dataForMultipleDrones.y[i]);

                    double a = Math.sin(deltaLatRadians / 2) * Math.sin(deltaLatRadians / 2) +
                            Math.cos(lat1Radians) * Math.cos(lat2Radians) *
                                    Math.sin(deltaLonRadians / 2) * Math.sin(deltaLonRadians / 2);
                    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

                    distances[i][j] = R * c;
                    distances[j][i] = R * c;
                }

            }
        }
        return distances;
    }


    // 更新路径距离，并给出经过点
    // NOTE: 这里的 distances 应该是对应 dataWithForbiddenZones 的
    public static List<Integer>[][] updateDistancesWithDijkstra(double[][] distances, DataForMultipleDrones dataForMultipleDronesWithForbiddenZones) {
        int size = distances.length;
        List<Integer>[][] paths = new ArrayList[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = i + 1; j < size; j++) {
                if (distances[i][j] == Double.MAX_VALUE) {
                    // Calculate shortest path between nodes i and j using Dijkstra's algorithm
                    DijkstraResult result = dijkstra(dataForMultipleDronesWithForbiddenZones, distances, i, j);

                    // Update distances matrix
                    distances[i][j] = result.distance;
                    distances[j][i] = result.distance;

                    // Update paths matrix
                    paths[i][j] = result.path;
                    paths[j][i] = new ArrayList<>(result.path);
                    Collections.reverse(paths[j][i]);
                }
                if (distances[i][j] != Double.MAX_VALUE) {
                    System.out.println("Distance " + i + "->" + j + ": " + distances[i][j]);
                    // System.out.println("Distance " + j + "->" + i + ": " + distances[j][i]);
                }

            }
        }
        adjacencyMatrix = distances;
        return paths;
    }

    // 计算最短路径
    private static DijkstraResult dijkstra(DataForMultipleDrones dataForMultipleDrones, double[][] distances, int source, int target) {
        int size = dataForMultipleDrones.num;
        double[] minDistances = new double[size];
        Arrays.fill(minDistances, Double.MAX_VALUE);
        minDistances[source] = 0;

        int[] previous = new int[size];
        Arrays.fill(previous, -1);

        PriorityQueue<Integer> queue = new PriorityQueue<>(Comparator.comparingDouble(i -> minDistances[i]));
        queue.add(source);

        while (!queue.isEmpty()) {
            int current = queue.poll();

            if (current == target) {
                break;
            }

            for (int neighbor = 0; neighbor < size; neighbor++) {
                if (neighbor == current || distances[current][neighbor] == Double.MAX_VALUE) {
                    continue;
                }

                double newDistance = minDistances[current] + distances[current][neighbor];
                if (newDistance < minDistances[neighbor]) {
                    minDistances[neighbor] = newDistance;
                    previous[neighbor] = current;
                    queue.remove(neighbor);
                    queue.add(neighbor);
                }
            }
        }

        List<Integer> path = new ArrayList<>();
        for (int node = target; node != -1; node = previous[node]) {
            path.add(node);
        }
        Collections.reverse(path);
        // Remove source node and target node if the path is not empty
        if (!path.isEmpty()) {
            path.remove(0); // Remove source node
            if (path.size() > 0) {
                path.remove(path.size() - 1); // Remove target node
            }
        }

        return new DijkstraResult(minDistances[target], path);
    }


    private static int findDroneWithShortestPath(double[][] adjacencyMatrix, List<List<Integer>> dronePaths, double[] droneDistances, int destination, double limitDistance, boolean prioritizeAllDrones) {
        double minDistance = Double.MAX_VALUE;
        int minIndex = -1;
        int unassignedDroneIndex = -1;

        // 由于计算误差，应该计算加上一些限制
        limitDistance *= 0.9;

        double minAssignedDistance = Double.MAX_VALUE;
        int minAssignedIndex = -1;

        for (int i = 0; i < dronePaths.size(); i++) {
            List<Integer> path = dronePaths.get(i);
            // 如果有未分配任务的无人机且优先启动所有无人机
            if (prioritizeAllDrones)
            {
                if (path.size() == 1)
                {
                    unassignedDroneIndex = i;
                    break;
                }
                double assignedDistance = 0;
                for (int j = 0; j < path.size() - 2; j++)
                {
                    assignedDistance += adjacencyMatrix[path.get(j)][path.get(j+1)];
                }
                if (minAssignedDistance > assignedDistance)
                {
                    minAssignedDistance = assignedDistance;
                    minAssignedIndex = i;
                }

            }
            int lastNode = path.get(path.size() - 1);
            double currentDistance = adjacencyMatrix[lastNode][destination];
            double totalDistance = droneDistances[i] + currentDistance + adjacencyMatrix[destination][0];

            if (currentDistance < minDistance && totalDistance <= limitDistance) {
                minDistance = currentDistance;
                minIndex = i;
            }

        }
        // 如果是优先启动所有无人机，则优先选择无人机距离短的
        if (prioritizeAllDrones)
        {
            if (unassignedDroneIndex != -1) { // 当找到未分配任务的无人机时，优先使用未分配任务的无人机
                return unassignedDroneIndex;
            } else {
                List<Integer> path = dronePaths.get(minAssignedIndex);
                if (droneDistances[minAssignedIndex] + adjacencyMatrix[path.get(path.size() - 1)][destination] + adjacencyMatrix[destination][0] <= limitDistance)
                {
                    return minAssignedIndex;
                }
            }
        }

        return minIndex;
    }
}

class Edge {
    int from;
    int to;
    double distance;

    Edge(int from, int to, double distance) {
        this.from = from;
        this.to = to;
        this.distance = distance;
    }
}


class DijkstraResult {
    double distance;
    List<Integer> path;

    DijkstraResult(double distance, List<Integer> path) {
        this.distance = distance;
        this.path = path;
    }
}
