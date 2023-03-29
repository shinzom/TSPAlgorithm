<template style="background-color:#d7f8ff;">
    <div style="background-color:#d7f8ff;font-size: 25px;width: 100%;height:705px;">
        <div>
            <span style="color: #01040c;font-size: 35px;text-align: center;display:block;">单架无人机不同算法对比历史记录</span>
        </div>
        <el-table :data="tableData" border stripe :cell-style="columnStyle" max-height="600px"
            style="width: 1210px;margin-top: 15px;margin-left: 165px;">
            <el-table-column prop="no" label="序号" width="60" align="center" />
            <el-table-column prop="tx" label="贪心算法" width="210" align="center">
                <el-table-column prop="time_tx" label="时间/ms" sortable width="110" />
                <el-table-column prop="distance_tx" label="距离/m" sortable width="100" />
            </el-table-column>
            <el-table-column prop="dp" label="动态规划算法" width="210" align="center">
                <el-table-column prop="time_dp" label="时间/ms" sortable width="110" />
                <el-table-column prop="distance_dp" label="距离/m" sortable width="100" />
            </el-table-column>
            <el-table-column prop="sa" label="模拟退火算法" width="210" align="center">
                <el-table-column prop="time_sa" label="时间/ms" sortable width="110" />
                <el-table-column prop="distance_sa" label="距离/m" sortable width="100" />
            </el-table-column>
            <el-table-column prop="tabu" label="禁忌搜索算法" width="210" align="center">
                <el-table-column prop="time_tabu" label="时间/ms" sortable width="110" />
                <el-table-column prop="distance_tabu" label="距离/m" sortable width="100" />
            </el-table-column>
            <el-table-column prop="aco" label="蚁群算法" width="210" align="center">
                <el-table-column prop="time_aco" label="时间/ms" sortable width="110" />
                <el-table-column prop="distance_aco" label="距离/m" sortable width="100" />
            </el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="scope">
                    <el-button class="show_btn" size="small" @click="handleReshow(scope.row)">复现路线</el-button>
                </template>
            </el-table-column>
        </el-table>

        <el-dialog v-model="dialogFormVisible" title="选择算法">
            <el-form>
                <el-form-item label="请选择要复现路线的算法：" >
                    <!-- <el-select v-model="this.alg_select" placeholder="算法">
                        <el-option label="贪心算法" key=0 />
                        <el-option label="动态规划算法" key=1 />
                        <el-option label="模拟退火算法" key=2 />
                        <el-option label="禁忌搜索算法" key=3 />
                        <el-option label="蚁群算法" key=4 />
                    </el-select> -->
                    <el-select v-model="alg_select" class="filter-item" placeholder="选择算法" multiple style="width:300px">
                <el-option v-for="item in algorithmOptions" :key="item.key" :label="item.label" :value="item.key" />
            </el-select>
                </el-form-item>
            </el-form>
            <template #footer>
                <span class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary" @click="select">确定</el-button>
                </span>
            </template>
        </el-dialog>

        <el-button
            style="background-color: #a2d8ca;font-size: 25px;width: 250px ;height:40px;display:block;margin:0 auto;margin-top: 10px;"
            @click="map">返回</el-button>
    </div>
</template>

<script>
import { show } from '../utils/api'
import { toRaw } from '@vue/reactivity'
export default {
    data() {
        return {
            tableData: [],
            result: [[]],
            dialogFormVisible: false,
            algorithmOptions: [
                { label: "贪心算法", key: 0 },
                { label: "动态规划算法", key: 1 },
                { label: "模拟退火算法", key: 2},
                { label: "禁忌搜索算法", key: 3},
                { label: "蚁群算法", key: 4},

            ],
            alg_select: 0,
            id_select: 0,
        }
    },
    created() {
        console.log("456789");
        this.showTable();
    },
    methods: {
        //设置不同算法列的背景颜色
        columnStyle({ row, column, rowIndex, columnIndex }) {
            //row,  每一行上的数据
            //column, 每一列上的数据
            //rowIndex,  行数的下标从0开始
            //columnIndex   列数下标从0开始
            if (columnIndex == 1 || columnIndex == 2) {
                let styleObj = {};
                styleObj.background = "#d6e6c8";
                return styleObj;
            }
            else if (columnIndex == 3 || columnIndex == 4) {
                let styleObj = {};
                styleObj.background = "#def0e4";
                return styleObj;
            }
            else if (columnIndex == 5 || columnIndex == 6) {
                let styleObj = {};
                styleObj.background = "#def0ee";
                return styleObj;
            }
            else if (columnIndex == 7 || columnIndex == 8) {
                let styleObj = {};
                styleObj.background = "#dee6f0";
                return styleObj;
            }
            else if (columnIndex == 9 || columnIndex == 10) {
                let styleObj = {};
                styleObj.background = "#dedef0";
                return styleObj;
            }
        },

        //返回按钮，跳转到map界面
        map() {
            this.$router.push('/');
        },

        //发送请求，填充表格数据
        showTable() {
            console.log("11111");
            show().then(res => {
                if (res.state == 200) {
                    this.result = toRaw(res.data);
                    console.log(this.result);
                    let i = 0;
                    for (i = 0; i < this.result.length; i++) {
                        this.tableData.push({
                            id: this.result[i][0].result_id,
                            no: i + 1,
                            time_tx: this.result[i][0].time,
                            distance_tx: this.result[i][0].distance.toFixed(2),
                            time_dp: this.result[i][1].time,
                            distance_dp: this.result[i][1].distance.toFixed(2),
                            time_sa: this.result[i][2].time,
                            distance_sa: this.result[i][2].distance.toFixed(2),
                            time_tabu: this.result[i][3].time,
                            distance_tabu: this.result[i][3].distance.toFixed(2),
                            time_aco: this.result[i][4].time,
                            distance_aco: this.result[i][4].distance.toFixed(2),
                        });
                    }
                } else {
                    this.$message({
                        showClose: true,
                        message: "错误",
                        type: 'error'
                    })
                }
            }).catch(err => {
                console.log(err.response)
            })

        },

        //复现路线
        handleReshow(row) {
            this.dialogFormVisible = true;
            this.id_select = this.tableData[row.no-1].id;

        },

        select(){
            this.dialogFormVisible = false;
            console.log(this.alg_select);
            console.log(this.id_select);
            this.$router.push('/' + this.id_select + '/' + this.alg_select)
        },

    }
}
</script>

<style lang="less" scoped></style>