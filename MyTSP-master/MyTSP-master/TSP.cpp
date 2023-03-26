/*
 * @Descripttion: 
 * @version: 
 * @Author: springhser
 * @Date: 2021-05-26 22:10:39
 * @LastEditors: springhser
 * @LastEditTime: 2021-06-13 21:00:19
 */
#include "TSP.hpp"
Matrix Tour::Node_Dist_Mat; 
NodeList Tour::Node_List;


/**
 * @brief Determine whether it is a valid path
 * @return yes: true; no: false
 */
bool Tour::isValidTour() const
{
    if(tour_edges_.size() != Node_List.size())
    {
        return false;
    }
    int cur_node = head_node_; 
    std::unordered_map<int, int> next_nodes;
    EdgeSet tour_edges_temp = tour_edges_;
    while(!tour_edges_temp.empty())
    {
        bool get_flag = false;
        for(auto& e: tour_edges_temp)
        {
            if(e.first == cur_node)
            {
                next_nodes[cur_node] = e.second;
                cur_node = e.second;
                tour_edges_temp.erase(e);
                get_flag = true;
                break;
            }
            if(e.second == cur_node)
            {
                next_nodes[cur_node] = e.first;
                cur_node = e.first;
                tour_edges_temp.erase(e);
                get_flag = true;
                break;
            }
        }
        if(!get_flag)
        {
            return false;
        }
        if(cur_node == head_node_)
        {
            break;
        }
    }
    
    if(next_nodes.size()!=Node_List.size())
    {
        return false;
    }
    
    return true;
}
//重连路线，把R_set中的边删除，把A_set中的边加入
bool Tour::relinkTour(const EdgeSet& R_set,
                const EdgeSet& A_set)
{
    if(R_set.size() != A_set.size())
    {
        return false;
    }
    EdgeSet tour_edges_temp = tour_edges_;
    for(const auto& e: R_set)
    {
        //c.erase( p) 从c中删除迭代器p指定的元素，p必须指向c中的一个真实元素，不能等于c.end()
        if(tour_edges_.find(e) != tour_edges_.end())
        {
            tour_edges_.erase(e);
        }
        else
        {
            return false;
        }
    }
    for(const auto& e: A_set)
    {
        if(tour_edges_.find(e) != tour_edges_.end())
        {
            return false;
        }
        else
        {
            tour_edges_.insert(e);
        }
    }
    if(isValidTour())
    {
        reGenTourMap();
    }
    else
    {
        //若不为有效的路线，则tour_edges_回到原始值
        tour_edges_ = tour_edges_temp;
        return false;
    }
    return true;
}
//重新构建新路线的tour_map_，tour_map_记录每个结点的前结点和后结点
void Tour::reGenTourMap()
{
    int cur_node = head_node_; 
    EdgeSet tour_edges_temp = tour_edges_;
    while(!tour_edges_temp.empty())
    {
        bool get_flag = false;
        for(auto& e: tour_edges_temp)
        {
            if(e.first == cur_node)
            {
                int pre_node = cur_node;
                tour_map_[cur_node].next_idx_ = e.second;
                cur_node = e.second;
                tour_map_[cur_node].prev_idx_ = pre_node;
                
                tour_edges_temp.erase(e);
                break;
            }
            if(e.second == cur_node)
            {
                int pre_node = cur_node;
                tour_map_[cur_node].next_idx_ = e.first;
                cur_node = e.first;
                tour_map_[cur_node].prev_idx_ = pre_node;
                tour_edges_temp.erase(e);
                break;
            }
        }
        if(cur_node == head_node_)
        {
            break;
        }
    }
}




TSP::TSP(const Points& point_list):recur_depth_(0)
{
    // initialise global variable,初始化路线的相关变量
    Tour::clearTour();
    Tour::initDistMat(point_list);
    Tour::initNodeList(point_list);
    // initialise member variable
    initTour(point_list);
}
void TSP::initTour(const Points& point_list)
{
    point_list_ = point_list;
    tour_.initTour();
}

void TSP::optTour()
{
    bool is_success = false;
    for(auto& n: Tour::Node_List)
    {
        Tour temp_tour;
        temp_tour = tour_;
        set_A_.clear();
        set_R_.clear();
        // select the first node n1;
        if(is_success = doOpt(n.unq_idx_, temp_tour))
        {
            tour_ = temp_tour;
        }
    }
}

//输入n1的序号和当前路线，找到n1的前驱或后继节点n2，n1-n2边若不在set_R_，则将其加入到set_R_中
//之后再找n3和n4
bool TSP::doOpt(int n1_idx, Tour& temp_tour)
{
    // get succ or prev of n1;
    std::vector<int> prv_succ= tour_.getAdjacentIdxByIdx(n1_idx);
    bool get_new_tour_flag = false;
    for(auto& n2_idx: prv_succ)
    {
        // at the first the add set and remove set should be cleared.
        set_A_.clear();
        set_R_.clear();
        recur_depth_ = 0;
        Edge e_r(n1_idx, n2_idx);
        if(set_R_.find(e_r) == set_R_.end())
        {
            set_R_.insert(e_r);
        }
        else
        {
            continue;
        }
        
        if(doSelection(n2_idx, n1_idx, temp_tour))
        {
            return true;
        }
    }
    return false;
}

//输入n2，n1，当前路线
//找到n3，n3不等于n1,且n2-n3不在当前路线中、不在set_R_和set_A_中
//若找到n3，则将n2-n3加入set_A_中
//再继续找n4，调用函数doSelection2
bool TSP::doSelection(int n2_idx, int origin_node_idx, Tour& temp_tour)
{
    //若已有5条边要被删除或添加，返回false
    if(set_R_.size() > RECURSION_DEPTH || set_A_.size() > RECURSION_DEPTH)
    {
        return false;
    }
    // get the neighbor node of n2.
    std::unordered_set<int> n3_idxes;
    if(!tour_.getNeighborNode(n2_idx, set_R_, set_A_, n3_idxes))
    {
        return false;
    } 
    for(auto n3_idx: n3_idxes)
    {
        if(origin_node_idx == n3_idx)
        {
            continue;
        }
        Edge e(n2_idx, n3_idx);
        
        if(tour_.isEdgeInTour(e))
        {
            continue;
        }
        
        if(isEdgeInRSet(set_R_, e))
        {
            continue;
        }
        if(isEdgeInASet(set_A_, e))
        {
            continue;
        }
        // add the new edge to set_A_
        set_A_.insert(e);
        //找到n3后，继续找n4
        if(doSelection2(n3_idx, origin_node_idx, temp_tour))
        {
            return true;
        }
        //若未找到n4，则在set_A_中删除n2-n3
        set_A_.erase(e);
    }
    return false;
}

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
bool TSP::doSelection2(int n3_idx, int origin_node_idx, Tour& temp_tour)
{
    //若已有5条边要被删除或添加，返回false
    if(set_R_.size() > RECURSION_DEPTH || set_A_.size() > RECURSION_DEPTH)
    {
        return false;
    }
    std::vector<int> prv_succ = tour_.getAdjacentIdxByIdx(n3_idx);
    for(auto& n4_idx : prv_succ)
    {
        if(origin_node_idx == n4_idx)
        {
            continue;
        }
        Edge e_final(origin_node_idx, n4_idx);
        if(tour_.isEdgeInTour(e_final))
        {
            continue;
        }
        
        if(isEdgeInRSet(set_R_, e_final))
        {
            continue;
        }
        if(isEdgeInASet(set_A_, e_final))
        {
            continue;
        }
        Edge e_r(n3_idx,n4_idx);
        set_R_.insert(e_r);
        set_A_.insert(e_final);
        // PRINTN("*******************************")
        // PRINTN("print middle result:"<<recur_depth_)
        // printRSet();
        // printASet();
        // PRINTN("*******************************")
        // PRINTN("")
        if(temp_tour.getEdgeSetLength(set_R_) > temp_tour.getEdgeSetLength(set_A_))
        {
            if(temp_tour.relinkTour(set_R_, set_A_))
            {
                recur_depth_ = 0;
                // PRINTN("Yes get it")
                return true;
            }
        }
        set_A_.erase(e_final);
        
        recur_depth_++;
        if(recur_depth_ > RECURSION_DEPTH)
        {
            // recur_depth_ = 0;
            return false;;
        }
        if(doSelection(n4_idx, origin_node_idx, temp_tour))
        {
            return true;
        }
        set_R_.erase(e_r);
    }
    return false;
}

