package com.cy.demo1.algorithm;
import com.cy.demo1.data.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

//  模拟退火求解TSP

public class SA_TSP {
    // 城市坐标<[x,y]>
    List<double[]> locationList;
    // 距离矩阵
    double[][] dist;
    // 城市数量
    int cityNum;
    public final int MAX_GEN = 500000;//最大的迭代次数(提高这个值可以稳定地提高解质量，但是会增加求解时间)
    public int[] tempGh;//存放临时编码
    public int[] currGh;//存放当前编码
    public int[] bestGh;//最好的路径编码
    public int bestT;//最佳的迭代次数
    public double tempEvaluation;//临时解
    public double currEvaluation;//当前解
    public double bestEvaluation;//最优解

    public int t;//当前迭代
    public Random random;//随机函数对象
    public double T = 100;   //模拟退火温度
    public double a = 0.9;  //降温速度


    public int[] main(Data data) {
        List<double[]> list = new ArrayList<double[]>(data.num);
        for(int i = 0; i < data.num; i++) {
            double[] temp ={data.x[i], data.y[i]};
            list.add(temp);
        }
        this.locationList = list;
        solve();
        int path[] = new int[data.num + 1];
        for(int i = 0; i < data.num; i++) {
            path[i] = bestGh[i];
        }
        path[data.num] = bestGh[0];
        return path;
    }

    public void solve() {
        initVar();
        solver();
    }

    public void solver() {
        while (t <= MAX_GEN) {
            t++;
            tempGh = generateNewGh(currGh, tempGh);
            tempEvaluation = evaluate(tempGh);
            if (tempEvaluation < bestEvaluation) {
                // 如果临时解优于最优解
                bestEvaluation = tempEvaluation;
                bestGh = tempGh.clone();
                bestT = t;
            } else if (tempEvaluation < currEvaluation) {
                // 如果临时解优于当前解
                currEvaluation = tempEvaluation;
                currGh = tempGh.clone();
            } else {
                // 如果临时解比全局最优解和当前解都差
                double r = random.nextDouble();
                if (r <= Math.exp(-1 * (Math.abs(tempEvaluation - currEvaluation) / T))) {
                    // 如果满足模拟退火算法Metropolis准则，那么临时解替换当前解
                    currEvaluation = tempEvaluation;
                    currGh = tempGh.clone();
                }
                // 降温
                T = T * (1.0 - a);
            }
        }
        System.out.println("最佳迭代次数:" + bestT);
        System.out.println("最短路程为：" + bestEvaluation);
        int[] bestPath = new int[cityNum + 1];
        System.arraycopy(bestGh, 0, bestPath, 0, bestGh.length);
        bestPath[cityNum] = bestPath[0];
        System.out.println("最佳路径为：" + Arrays.toString(bestPath));
    }

    // 领域交换，生成新解(随机指定数组中的两个数，不包括首尾，然后让这两个数进行位置互换，达到生成一个新路线的作用)
    public int[] generateNewGh(int[] Gh, int[] tempGh) {
        int temp;
        //将Gh复制到tempGh
        tempGh = Gh.clone();

        int r1 = 0;
        int r2 = 0;
//        这段代码(r1==0||r2==0)是为了保证起点不受改变，如果有固定的起点的话，可以使用这几行代码
//        while (r1==r2||(r1==0||r2==0)){
//            r1 = random.nextInt(cityNum);
//            r2 = random.nextInt(cityNum);
//        }
        while (r1 == r2) {
            r1 = random.nextInt(cityNum);
            r2 = random.nextInt(cityNum);
        }
        // 交换
        temp = tempGh[r1];
        tempGh[r1] = tempGh[r2];
        tempGh[r2] = temp;
        return tempGh.clone();
    }

    // 初始化变量
    public void initVar() {
        cityNum = locationList.size();//城市数量为点的数量
        bestGh = new int[cityNum];//最好的路径编码
        currGh = new int[cityNum];//当前编码
        tempGh = new int[cityNum];//存放临时编码
        dist = new double[cityNum][cityNum];//距离矩阵
        random = new Random(System.currentTimeMillis());
        //初始化距离矩阵
        for (int i = 0; i < dist.length; i++) {
            for (int j = i; j < dist[i].length; j++) {
                if (i == j) {
                    //对角线为0
                    dist[i][j] = 0.0;
                } else {
                    //计算i到j的距离
                    dist[i][j] = getDistance(locationList.get(i), locationList.get(j));
                    dist[j][i] = dist[i][j];
                }
            }
        }
        //初始化参数
        bestT = 0;
        t = 0;
        random = new Random(System.currentTimeMillis());
        //随机创造初始解
        currGh[0] = 0;
        List<Integer> pathList = new ArrayList<>();
        pathList.add(0);
        int index = 1;
        while (index < cityNum) {
            int r1 = random.nextInt(cityNum);
            if (!pathList.contains(r1)) {
                currGh[index++] = r1;
                pathList.add(r1);
            }
        }
        System.out.println("初始解为：" + Arrays.toString(currGh));
        //复制当前路径编码给最优路径编码
        tempGh = currGh.clone();
        bestGh = currGh.clone();
        currEvaluation = evaluate(currGh);
        bestEvaluation = currEvaluation;
        tempEvaluation = currEvaluation;
        //System.out.println("随机破坏："+Arrays.toString(randomBreakAndRepair(currGh.clone())));
    }

    // 计算两点之间的距离（使用伪欧氏距离，可以减少计算量）
    public double getDistance(double[] place1, double[] place2) {
        // 伪欧氏距离在根号内除以了一个10
//        return Math.sqrt((Math.pow(place1[0] - place2[0], 2) + Math.pow(place1[1] - place2[1], 2)) / 10.0);
//        return Math.sqrt((Math.pow(place1[0] - place2[0], 2) + Math.pow(place1[1] - place2[1], 2)));
        double rij;
        Point point1 = new Point(place1[0], place1[1]);
        Point point2 = new Point(place2[0], place2[1]);
        rij = BaiduLocationUtils.getDistance(point1, point2);
        return rij;
    }

    // 评价函数
    public double evaluate(int[] path) {
        double pathLen = 0.0;
        for (int i = 1; i < path.length; i++) {
            // 起点到终点途径路程累加
            pathLen += dist[path[i - 1]][path[i]];
        }
        // 然后再加上返回起点的路程
        pathLen += dist[path[0]][path[path.length - 1]];
        return pathLen;
    }

    public double getDistance() {
        return bestEvaluation;
    }
}
