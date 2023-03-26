/*
 * @Descripttion: 
 * @version: 
 * @Author: springhser
 * @Date: 2020-12-21 22:35:55
 * @LastEditors: springhser
 * @LastEditTime: 2021-06-13 21:01:01
 */
#ifndef TSP_HPP
#define TSP_HPP
#include <vector>
#include <unordered_map>
#include <unordered_set>
#include <set>
#include <string>
#include <list>
#include <array>
#include <iostream>
#include <algorithm>
#include <math.h>
#include <utility>
#include <functional>

#include "Point.hpp"
#include "Utils/HelpTool.hpp"

#define RECURSION_DEPTH 2

class ATest
{
public:
    virtual void test() = 0;
};

struct Node;
struct Edge;
struct Node: public Point2D
{
    Node(int index, double x = 0.0, double y = 0.0):Point2D(x, y), unq_idx_(index){}
    Node(int index, const Point2D& pt):Point2D(pt), unq_idx_(index){}
    //类的友元函数是定义在类外部，但有权访问类的所有私有（private）成员和保护（protected）成员。
    //尽管友元函数的原型有在类的定义中出现过，但是友元函数并不是成员函数。
    friend bool operator==(const Node& lhs, const Node& rhs)
    {
        return lhs.unq_idx_ == rhs.unq_idx_;
    }

    friend std::ostream& operator<<(std::ostream& os, const Node& n)
    {
        os << "[" << n.unq_idx_ << ": (" << n.x << " " << n.y <<")]" << std::endl;
        return os;
    }
    int unq_idx_;           // this index is an unique key of a node.
};

struct NodeWrapper
{
    NodeWrapper(int prev_idx = 0, int next_idx = 0):prev_idx_(prev_idx), next_idx_(next_idx){}
    int prev_idx_;
    int next_idx_; 
    friend std::ostream& operator<<(std::ostream& os, const NodeWrapper& n)
    {
        os << "[" << n.prev_idx_ << " : " << n.next_idx_ << "]" << std::endl;
        return os;
    }
};

using NodeList = std::vector<Node>;
using Matrix = std::vector<std::vector<double>>;
using TourMap = std::unordered_map<int, NodeWrapper>;

struct Edge
{
    Edge(int n1, int n2)
    {
        ASSERTM(n1 != n2, "The node index should not the same");
        // to ensure that the index first node in edge is smaller than the second 
        if(n1 > n2)
        {
            first = n2;
            second = n1;
        }
        else
        {
            first = n1;
            second = n2;
        }
    }

    friend bool operator==(const Edge& lhs, const Edge& rhs)
    {
        return lhs.first == rhs.first &&
               lhs.second == rhs.second;
    }

    friend std::ostream& operator<<(std::ostream& os, const Edge& e)
    {
        os << e.first << " *--* " << e.second << std::endl;
        return os;
    }

    int first;
    int second;
};

struct hash_edge
{
    std::size_t operator()(const Edge& e) const
    {
        std::size_t h1 = std::hash<int>{}(e.first);
        std::size_t h2 = std::hash<int>{}(e.second);
        //<<左移，>>右移，^按位异或运算符
        return (h1 ^ (h2<<1)) >> 1;
    }
};
using EdgeSet = std::unordered_set<Edge, hash_edge>;

class EdgeTest : public ATest
{
    void testConstruct()
    {
        TESTCASE("Test Edge Construct");
        Edge e(1, 2);
        PRINTN(e);
    
        // Edge e_fail(2, 2);
    }

    void testEqual()
    {
        TESTCASE("Test equal function");
        Edge e1(1, 2);
        Edge e2(2, 1);
        EQUAL(e1 == e2);
    }

    void testHash()
    {
        TESTCASE("Test Hash function");
        Edge e1(1, 2);
        Edge e2(2, 1);
        Edge e3(3, 0);
        std::size_t h1 = std::hash<int>{}(e1.first);
        std::size_t h2 = std::hash<int>{}(e1.second);
        PRINTN( ((h1 ^ (h2<<1)) >> 1));
        h1 = std::hash<int>{}(e3.first);
        h2 = std::hash<int>{}(e3.second);
        PRINTN( ((h1 ^ (h2<<1)) >> 1));

        EdgeSet e_set;
        e_set.insert(e1);
        e_set.insert(e2);
        e_set.insert(e3);

        for(const auto& e: e_set)
        {
            PRINT(e);
        }
    }
public:
    void test()
    {
        TESTMODULE("Test Edge");
        testConstruct();
        testEqual();
        testHash();
    }


};


struct Tour
{
    Tour(){}

    //初始化路线中的变量
    void initTour()
    {
        int node_size = getNodeSize();
        if( node_size <= 1)
        {
            return;
        }

        //疑惑？？？？？？？
        tour_map_[0] = NodeWrapper(node_size-1, 1);
        tour_edges_.insert(Edge(0,1));
        for(int i = 1; i < node_size-1; ++i)
        {
            tour_map_[i] = NodeWrapper(i-1, i+1);
            tour_edges_.insert(Edge(i,i+1));
        }
        tour_map_[node_size-1] = NodeWrapper(node_size-2,0);
        tour_edges_.insert(Edge(0, node_size-1));
        head_node_ = 0;
    }
    
    /**
     * @brief Determine whether it is a valid path
     * @return yes: true; no: false
     */
    bool isValidTour() const;
    bool relinkTour(const EdgeSet& R_set,
                    const EdgeSet& A_set);

    void reGenTourMap();

    //打印在目前路线中的所有边
    void printEdges()
    {
        for(const auto& e: tour_edges_)
        {
            PRINT(e);
        }
        PRINTN("")
    }
    
    //？？？？？？？
    //打印路线，即tour_map_中的所有边，存疑
    void printTourMap()
    {
        for(const auto& m: tour_map_)
        {
            PRINT(m.first << " - "<< m.second);
        }
        PRINTN("")
    }

    //打印路线
    void printTour()
    {
        PRINTN("Forward Traversal")
        int next_node = tour_map_[head_node_].next_idx_;
        int prev_node = tour_map_[head_node_].prev_idx_;
        PRINT(head_node_ << "-->")
        while(next_node != head_node_)
        {
            PRINT(next_node << "-->")
            next_node = tour_map_[next_node].next_idx_;
        }
        PRINTN(head_node_)
        
        PRINTN("Back Traversal")
        PRINT(head_node_ << "<--")
        while(prev_node != head_node_)
        {
            PRINT(prev_node << "<--")
            prev_node = tour_map_[prev_node].prev_idx_;
        }
         PRINTN(head_node_)

    }

    /**
     * @brief calculate the cost of the tour.This function
     *        maybe will not be used very frequently 
     * @warning It should esure the tour is a vaild path, 
     *          or the cost will be set to -1
     */
    void calTourCost() 
    {
        // if(!isValidTour())
        // {
        //     cost_ = -1;
        //     return;
        // }
        double t_cost = 0.0;
        int cur_node = head_node_;
        int next_node = tour_map_[cur_node].next_idx_;
        t_cost = t_cost+Node_Dist_Mat[cur_node][next_node];
        while(next_node != head_node_ )
        {
            cur_node = next_node;
            next_node = tour_map_[cur_node].next_idx_;
            t_cost = t_cost+Node_Dist_Mat[cur_node][next_node];
        }
        cost_ = t_cost;
    }
    
    /**
     * @brief get the cost of the tour.
     * @return the cost of the tour
     */
    double getTourCost() const
    {
        return cost_;
    }

    //判断两个结点是否在路线上是相连的
    bool isNodeConnected(const Node& n1, const Node& n2) const
    {
        return (tour_map_.at(n1.unq_idx_).prev_idx_ == tour_map_.at(n2.unq_idx_).next_idx_ ||
                tour_map_.at(n1.unq_idx_).next_idx_ == tour_map_.at(n2.unq_idx_).prev_idx_);
    }

    //判断某一条边是否在路线中
    bool isEdgeInTour(const Edge& e) const
    {
        // the comment below is an another method before to determine edge in tour,
        // but it also good to learn something about the different between [] and at in std::map 
        /*
        //https://stackoverflow.com/questions/42095642/error-passing-const-stdmapint-int-as-this-argument-discards-qualifiers
        operator[] hasn't a const qualifier in std::map, as you can see from the documentation, e.g. std::map::operator[] - cppreference.com:

        Returns a reference to the value that is mapped to a key equivalent to key, performing an insertion if such key does not already exist.

        Therefore you cannot use it directly on a const instance. Use at instead (ref std::map::at - cppreference.com) if you can afford C++11 features.

        Declarations for those member functions follow:

        T& operator[](const key_type& x);
        T& operator[](key_type&& x);
        T&       at(const key_type& x);
        const T& at(const key_type& x) const;
        */
        // return (this->tour_map_[e.first]->prev->unq_idx_ == this->tour_map_[e.second]->unq_idx_ ||
        //         this->tour_map_[e.first]->next->unq_idx_ == this->tour_map_[e.second]->unq_idx_);
        // return (tour_map_.at(e.first)->prev->unq_idx_ == tour_map_.at(e.second)->unq_idx_ ||
        //         tour_map_.at(e.first)->next->unq_idx_ == tour_map_.at(e.second)->unq_idx_);

        return tour_edges_.find(e) != tour_edges_.end();
    }

    //返回EdgeSet中边的总距离，EdgeSet相当于边的一个集合
    double getEdgeSetLength(const EdgeSet& edge_set)
    {
        double cost = 0.0;
        for(auto r : edge_set)
        {
            cost += getEdgeLength(r.first, r.second);
        }
        return cost;
    }

    // return the first node uique key
    int getFirstNode() const
    {
        return head_node_; 
    }

    //返回输入结点的序号、坐标（x、y），存疑
    Node getNodeByIdx(int unique_idx) const
    {
        return Node_List[unique_idx];
    }

    //返回路线中输入节点的下一个结点的序号、坐标（x、y）
    Node getNodeSucc(const Node& n) const 
    {
        return getNodeByIdx(tour_map_.at(n.unq_idx_).next_idx_);
    }

    //返回路线中输入序号所代表的节点的下一个结点的序号、坐标（x、y）
    Node getNodeSuccByIdx(int unq_idx) const 
    {
        return getNodeByIdx(tour_map_.at(unq_idx).next_idx_);
    }

    //返回路线中输入序号所代表的节点的下一个结点的序号
    int getNodeSuccIdxByIdx(int unq_idx) const
    {
        return tour_map_.at(unq_idx).next_idx_;
    }
    
    //返回路线中输入节点的上一个结点的序号、坐标（x、y）
    Node getNodePrev(const Node& n) const 
    {
        return getNodeByIdx(tour_map_.at(n.unq_idx_).prev_idx_);
    }
    
    //返回路线中输入序号所代表的节点的上一个结点的序号、坐标（x、y）
    Node getNodePrevByIdx(int unq_idx) const 
    {
        return getNodeByIdx(tour_map_.at(unq_idx).prev_idx_);
    }

    //返回路线中输入序号所代表的节点的上一个结点的序号
    int getNodePrevIdxByIdx(int unq_idx) const
    {
        return tour_map_.at(unq_idx).prev_idx_;
    }

    //返回路线中输入结点的前一个结点和后一个节点的序号、坐标（x、y）
    std::vector<Node> getAdjacent(const Node& n)
    {
        return {getNodePrev(n), getNodeSucc(n)};
    }
    
    //返回路线中输入序号所代表的结点的前一个结点和后一个结点的序号
    std::vector<int> getAdjacentIdxByIdx(int unique_idx)
    {
        return {getNodePrevIdxByIdx(unique_idx), getNodeSuccIdxByIdx(unique_idx)};
    }

    /**
     * @brief get the neighbor node of the cur_node.
     *        requirement:
     *        1. 
     *        2.
     *        3.
     * @return the neighbor node of the cur_node.
     */
    //如果遍历的结点既不是当前结点，且其与当前结点的连线没有在路线中，没有在R_set和A_set中
    //则在nnodes中加入当前遍历到的结点的序号
    bool getNeighborNode(int cur_idx, const EdgeSet& R_set, const EdgeSet& A_set, std::unordered_set<int>& nnodes)
    {
        for(auto& n: Node_List)
        {
            // not the adjacent
            if(n.unq_idx_ == cur_idx ||
               isEdgeInTour(Edge(n.unq_idx_, cur_idx)))
            {
                continue;
            }
            
            Edge t_e(n.unq_idx_, cur_idx);

            if(R_set.find(t_e) != R_set.end())
            {
                continue;
            }

            if(A_set.find(t_e) != A_set.end())
            {
                continue;
            }
            // if(Tour::getEdgeLength(n.unq_idx_, cur_idx) >= length)
            // {
            //     continue;
            // }

            //如果遍历的结点既不是当前结点，且其与当前结点的连线没有在路线中，没有在R_set和A_set中
            //则在nnodes中加入当前遍历到的结点的序号
            nnodes.insert(n.unq_idx_);
        }
        return !nnodes.empty();
    }

// havnt finished，该方法未完成
    bool isEqual(const Tour& tour) const
    {
        if(Tour::getNodeSize() <= 2 )
        {
            return true;
        }

        int first_node = 0;
        
        bool forward = true;

        int next_node1 = tour_map_.at(first_node).next_idx_;
        int next_node2 = tour.getNodeSuccIdxByIdx(first_node);

        //while()
        
        return false;
    }

    //把路线的边的集合中中在R_set集合中的边删除
    void removeEdges(const EdgeSet& R_set)
    {
        for(const auto& e: R_set)
        {
            if(tour_edges_.find(e) != tour_edges_.end())
            {
                tour_edges_.erase(e);
            }
        }
    }

    //在路线的边的集合中中加上A_set集合中的边
    void addEdges(const EdgeSet& A_set)
    {
        for(const auto& e: A_set)
        {
            tour_edges_.insert(e);   
        }
    }
    
    TourMap tour_map_; 

    EdgeSet tour_edges_;//路线中边的集合
    
    int head_node_;//初始结点
    
    double cost_;//整个路线的总距离


    /** 
     * static member
    */
    //Node_Dist_Mat计算所有点之间距离
    static bool initDistMat(const Points& point_list)
    {
        if(point_list.empty())
        {
            return false;
        }
        int p_size = point_list.size();
        Node_Dist_Mat = std::vector<std::vector<double>>(p_size,std::vector<double>(p_size, 0));
        for(int i = 0; i < p_size; ++i)
        {
            for(int j = 0; j < i; ++j)
            {
                
                Node_Dist_Mat[i][j] = Node_Dist_Mat[j][i] 
                                    = sqrt((point_list[i].x-point_list[j].x)*(point_list[i].x-point_list[j].x) +
                                           ((point_list[i].y-point_list[j].y)*(point_list[i].y-point_list[j].y)));
            }
        }
        return true;
    }

    //返回两点之间的距离
    static inline double getEdgeLength(int idx1, int idx2)
    {
        // Verify the validity of  input parameters here.
        ASSERTM(idx1 >= 0 && idx2 >= 0 && 
                idx1 < Node_List.size() && idx1 < Node_List.size(), 
                "Idx ERROR");
        return Node_Dist_Mat[idx1][idx2];
    }

    //返回点的个数
    static inline std::size_t getNodeSize()
    {
        return Node_List.size();
    }

    //初始化Node_List，即记录所有点的序号，坐标
    static bool initNodeList(const Points& point_list)
    {
        for(int i = 0; i < point_list.size(); ++i)
        {
            Node_List.emplace_back(Node(i, point_list[i]));
        }
        return true;
    }

    //打印Node_List，即打印所有点的序号，坐标
    static void printNodeList()
    {
        for(const auto& n : Node_List)
        {
            PRINT(n);
        }
        PRINTN("");
    } 

    //打印Node_Dist_Mat，即打印记录点之间的距离的二维数组
    static void printMatrix()
    {
        helptool::PrintContainer<double> pnum;
        int i = 1;
        for(auto& row : Node_Dist_Mat)
        {
            PRINT("Line " << i << ": ")
            
            pnum.printVecSingleLine(row, " ");
            PRINTN("");
            ++i;
        }
       
    }

    //清空Node_Dist_Mat和Node_List
    static void clearTour()
    {
        Node_Dist_Mat.clear();
        Node_List.clear();
    }

    static Matrix Node_Dist_Mat;//相当于二维数组，记录点之间的距离 
    static NodeList Node_List;//所有点的集合
};


class TourTest:public ATest
{

    void testStatic()
    {
        TESTCASE("Test Static");
        Points pts;
        pts.emplace_back(Point2D(1,2));
        pts.emplace_back(Point2D(3,2));
        pts.emplace_back(Point2D(4,2));
        pts.emplace_back(Point2D(5,2));
        Tour::clearTour();
        Tour::initDistMat(pts);
        Tour::initNodeList(pts);
        Tour::printMatrix();
        Tour::printNodeList();
        
        EQUAL(Tour::getEdgeLength(0,3) == 4.0);
        EQUAL(Tour::getNodeSize() == 4);
    }

    void testInitTour()
    {
        TESTCASE("Test Init Tour");
        Points pts;
        pts.emplace_back(Point2D(0,1));
        pts.emplace_back(Point2D(4,1));
        pts.emplace_back(Point2D(2,2));
        pts.emplace_back(Point2D(3,0));
        pts.emplace_back(Point2D(1,0));
        Tour::clearTour();
        Tour::initDistMat(pts);
        Tour::initNodeList(pts);

        Tour tour;
        tour.initTour();
        tour.printEdges();
        tour.printTourMap();

        EdgeSet R_set{Edge(0,1), Edge(2,3)};
        EdgeSet A_set{Edge(0,2), Edge(1,3)};

        tour.relinkTour(R_set, A_set);

        tour.printEdges();
        tour.printTourMap();

        tour.printTour();

        tour.calTourCost();
        double expect_cost = 2*(sqrt(2)+sqrt(5)+1);
        double real_cost = tour.getTourCost();
        PRINTN("cost: " << real_cost)
        EQUAL(expect_cost == real_cost);
        
    }

    void testGetInfo()
    {
        TESTCASE("Test Get Node Info")
        Points pts;
        pts.emplace_back(Point2D(0,1));
        pts.emplace_back(Point2D(4,1));
        pts.emplace_back(Point2D(2,2));
        pts.emplace_back(Point2D(3,0));
        pts.emplace_back(Point2D(1,0));
        Tour::clearTour();
        Tour::initDistMat(pts);
        Tour::initNodeList(pts);

        Tour tour;
        tour.initTour();
        tour.printEdges();
        tour.printTourMap();

        std::vector<int> adj = tour.getAdjacentIdxByIdx(0);
        PRINTN("Get Adjacent")
        for(auto i : adj)
        {
            PRINT(i << " ");
        }
        PRINTN("")
        EdgeSet R_set{Edge(0,1)};
        EdgeSet A_set{Edge(1,3)};
        std::unordered_set<int> res;
        EQUAL(tour.getNeighborNode(1, R_set, A_set, res))
        for(auto i : res)
        {
            PRINT(i << " ");
        }
        PRINTN("");
    }

    void testValid()
    {
        TESTCASE("Test valididy of tour")
        Points pts;
        pts.emplace_back(Point2D(0,1));
        pts.emplace_back(Point2D(4,1));
        pts.emplace_back(Point2D(2,2));
        pts.emplace_back(Point2D(3,0));
        pts.emplace_back(Point2D(1,0));
        Tour::clearTour();
        Tour::initDistMat(pts);
        Tour::initNodeList(pts);

        Tour tour;
        tour.initTour();

        EdgeSet R_set{Edge(0,1), Edge(2,3)};
        EdgeSet A_set{Edge(0,2), Edge(1,4)};
        tour.removeEdges(R_set);
        tour.addEdges(A_set);
        EQUAL(tour.isValidTour());
        
    }
public:
    void test()
    {
        TESTMODULE("Test Tour");
        testStatic();
        testInitTour();
        testGetInfo();
        testValid();
    }
};

// /*
// 思路：
// * 维护两个边的集合：remove集（待删除边集合，简称RSet）和add集（带加入的集合, 简称A_Set）。
// 算法简要流程：
// 1. 选择起点n1.
// 2. 选n1的前驱或者后继节点为n2，组成第一条待删除的边(n1,n2)进入R_Set.
// 3. 选n2的邻近节点n3
//     (
//     要求(n2,n3):
//         （1）不属于原路径上的边。
//         （2）不在R_Set中。
//         （3）长度小于(n1,n2)// 舍弃
//     )。
//     (n2,n3)进入A_Set。
// 4. 从n3的前驱或者后继中选择节点n4，(n3,n4)进入R_Set.
// 5. 若n4和n1连接，即(n1,n2) (n3,n4)-> (n1,n4)(n2,n3) ，
//    能形成一条路径，且使得dist(n1,n2)+ dist(n3,n4)> dist(n1,n4)+dist(n2,n3) ，
//    则得到一条新的路径。
// 6. 否则，(n1,n4)进入A_Set。从n4出发，按照3，4的步骤重新找待删除的边和待添加边。
//    这里，由于寻找越多，计算复杂度越大，通常当R_Set的大小超过5，退出搜索，
//    表明从n1出发找不到更合适的路径。
// */

class TSP
{
public:
    TSP(const Points& point_list);

    void initTour(const Points& point_list);
    
    void optTour();

    //输入n1的序号和当前路线，找到n1的前驱或后继节点n2，n1-n2边若不在set_R_，则将其加入到set_R_中
    //之后再找n3和n4
    bool doOpt(int n1_idx, Tour& temp_tour);
    
    //输入n2，n1，当前路线
    //找到n3，n3不等于n1,且n2-n3不在当前路线中、不在set_R_和set_A_中
    //若找到n3，则将n2-n3加入set_A_中
    //再继续找n4，调用函数doSelection2
    bool doSelection(int n2_idx, int origin_node_idx, Tour& temp_tour);
    
    //origin_node_idx也就是n1
    //输入n3,n1，当前路线
    //找n4结点
    //确保n4不等于n1，且n1-n4不在当前路线中、不在set_R_和set_A_中
    //找到n4后，将n3-n4加入set_R_中，n1-n4加入set_A_中
    //之后若dist(n1,n2)+ dist(n3,n4)> dist(n1,n4)+dist(n2,n3)，则重连线路（进行二交换）
    //否则在set_A_中删除n1-n4，继续找n5，调用doSelection(n4_idx, origin_node_idx, temp_tour)
    //输入n4，n1，当前路线
    //找到n5，n5不等于n1,且n4-n5不在当前路线中、不在set_R_和set_A_中
    //若找到n5，则将n4-n5加入set_A_中
    //再继续找n6，调用函数doSelection2(n5_idx, origin_node_idx, temp_tour)
    //确保n6不等于n1，且n1-n5不在当前路线中、不在set_R_和set_A_中
    //找到n6后，将n5-n6加入set_R_中，n1-n6加入set_A_中
    //之后若dist(n1,n2)+ dist(n3,n4)+ dist(n5,n6)> dist(n2,n3)+dist(n4,n5)+dist(n1,n6)，则重连线路（三交换）
    //否则返回false
    bool doSelection2(int n3_idx, int origin_node_idx, Tour& temp_tour);

    //判断边e是否在set_R中
    bool isEdgeInRSet(const EdgeSet& set_R, Edge& e)
    {
        return set_R.find(e) != set_R.end();
    }

    ////判断边e是否在set_A中
    bool isEdgeInASet(const EdgeSet& set_A, const Edge& e)
    {
        return set_A.find(e) != set_A.end();
    }

    //打印两点之间距离的二维数组和路线
    void printTour()
    {
        Tour::printMatrix();
        tour_.printTour();
    }

    //打印路线
    void printRes()
    {
        tour_.printTour();
    }

    //打印总距离
    void printCost()
    {
        tour_.calTourCost();
        PRINTN("Cost: " << tour_.getTourCost());
    }

    //打印set_R_中的边
    void printRSet()
    {
        PRINTN("R set{")
        for(auto& e: set_R_)
        {
            PRINT(e);
        }
        PRINTN("}");
    }

    //打印set_A_中的边
    void printASet()
    {
        PRINTN("A set{")
        for(auto& e: set_A_)
        {
            PRINT(e);
        }
        PRINTN("}");
    }
private:
    EdgeSet set_R_; // the set of edge that to be removed  
    EdgeSet set_A_; // the set of edge that to be added
    
    int recur_depth_;  // the recurve deep
    Tour tour_;
    Points point_list_;//所有点的集合，记录点的坐标
};

class TSPTest : public ATest
{
    
public:
    void test()
    {
        TESTMODULE("Test TSP")
        Points pts;
        pts.emplace_back(Point2D(0,1));
        pts.emplace_back(Point2D(4,1));
        pts.emplace_back(Point2D(2,2));
        pts.emplace_back(Point2D(3,0));
        pts.emplace_back(Point2D(1,0));

        TSP tsp(pts);
        tsp.optTour();
        tsp.printRes();
    }
};
#endif