<template>
    <el-table :data="mTSPData" border style="width: 500px;margin-top: 5px;margin-left: 60px;"
     :row-style="tableRowClassName">
        <el-table-column prop="no" label="序号" width="80" />
        <el-table-column prop="alg" label="算法" sortable width="100" />
        <el-table-column prop="time" label="时间/ms" sortable width="110" />
        <el-table-column prop="distance_mtsp" label="距离/m" sortable width="100" />
    </el-table>
    <el-button style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;margin-top: 5px;height:30px;" @click="map">
        <router-link to="/">返回</router-link>

    </el-button>
</template>

<script>
export default {
    data() {
        return {
            tableData: [],
        }
    },
    map() {
        this.$router.push({ path: '/' });
    }
}
</script>