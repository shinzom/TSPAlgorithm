<template>
    <el-container style="width:100%;height:100%;">
        <el-header>
            <div class="left_box">
                <span>无 人 机 航 迹 规 划 算 法 研 究</span>
            </div>
        </el-header>
        <el-container style="height: 100%;">
            <el-aside>
                <el-form-item label="菜单" style="margin-left: 12px;"></el-form-item>
                <div class="selectTitle">
                    <span>菜单</span>
                </div>

                <div class="btn-group">
                    <el-button style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;height:30px;"
                        v-if="!isDrawing" @click="startDrawing">开始加点</el-button>
                    <el-button style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;height:30px;"
                        v-if="isDrawing" @click="stopDrawing">停止加点</el-button>
                    <el-button
                        style="background-color: #a2d8ca;width: 100px ;margin-left: 23px;margin-top: 5px;height:30px;"
                        @click="deleteLastPoint">删除上一个点</el-button>
                    <el-button
                        style="background-color: #a2d8ca;width: 100px ;margin-top: 5px;margin-left: 50px;height:30px;"
                        @click="clearMap">取消所有点</el-button>
                </div>

                <el-divider style="margin-top: 10px;margin-bottom: 3px;background-color: #1e9ee9;" />

                <el-collapse v-model="activeNames" class="collapse" @change="handleChange" style="margin-top: 10px;"
                    accordion>
                    <el-collapse-item title="单架无人机不同算法" name="1">
                        <el-button
                            style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;margin-top: 5px;height:30px;"
                            @click="toggleLines">{{ isDrawingLines ? '隐藏连线' : '绘制点的连线' }}</el-button>

                        <el-table ref="multipleTableRef" :data="tableData"
                            style="width: 290px;margin-top: 5px;margin-left: 5px;"
                            :default-sort="{ prop: 'distance', order: 'ascending' }"
                            @selection-change="handleSelectionChange">
                            <el-table-column type="selection" width="27" />
                            <el-table-column prop="alg" label="算法" width="80" />
                            <el-table-column prop="time" label="时间/ms" sortable width="104" />
                            <el-table-column prop="distance" label="距离/m" sortable width="97" />
                        </el-table>
                        <el-button
                            style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;margin-top: 5px;height:30px;"
                            @click="draw">绘制所选算法的路线</el-button>
                        <el-button
                            style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;margin-top: 5px;height:30px;"
                            @click="history">查看历史算法对比记录</el-button>
                    </el-collapse-item>

                    <el-collapse-item title="多架无人机协同算法" name="2">
                        <el-radio-group v-model="radioVal" style="margin-left: 75px;" text-color="green">
                            <el-radio :label=true size="large">优先启动全部无人机</el-radio>
                            <el-radio :label=false size="large">优先满足距离限制</el-radio>
                        </el-radio-group>
                        <el-form class="form_class" style="margin-top: 10px;">
                            <el-form-item label="无人机数量:" label-width="140px">
                                <el-input v-model="planeNum" placeholder="无人机数量" style="width:110px;"></el-input>
                            </el-form-item>
                            <el-form-item label="无人机续航:" label-width="140px">
                                <el-input v-model="limit" placeholder="距离限制" style="width:110px;"></el-input>
                            </el-form-item>
                        </el-form>
                        <el-button style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;height:30px;"
                            @click="synAlg">绘制路线</el-button>
                        <el-table :data="mTSPData" style="width: 180px;margin-top: 5px;margin-left: 60px;"
                            :default-sort="{ prop: 'distance_mtsp', order: 'ascending' }" :row-style="tableRowClassName">
                            <el-table-column prop="no" label="序号" width="80" />
                            <el-table-column prop="distance_mtsp" label="距离/m" sortable width="100" />
                        </el-table>
                    </el-collapse-item>

                    <el-collapse-item title="设置无人机禁飞区算法" name="3">
                        <el-radio-group v-model="noflyPriority" style="margin-left: 75px;" text-color="green">
                            <el-radio :label=true size="large">优先启动全部无人机</el-radio>
                            <el-radio :label=false size="large">优先满足距离限制</el-radio>
                        </el-radio-group>
                        <el-form class="form_class" style="margin-top: 10px;">
                            <el-form-item label="无人机数量:" label-width="140px">
                                <el-input v-model="planeNumNofly" placeholder="无人机数量" style="width:110px;"></el-input>
                            </el-form-item>
                            <el-form-item label="无人机续航:" label-width="140px">
                                <el-input v-model="limitNofly" placeholder="距离限制" style="width:110px;"></el-input>
                            </el-form-item>
                        </el-form>

                        <el-button
                            style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;height:30px;margin-top: 5px;"
                            v-if="!isDrawingNofly" @click="startDrawingNofly(e)">开始绘制禁飞区</el-button>
                        <el-button
                            style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;height:30px;margin-top: 5px;"
                            v-if="isDrawingNofly" @click="stopDrawingNofly">停止绘制禁飞区</el-button>
                        <el-button
                            style="background-color: #a2d8ca;width: 250px ;margin-left: 23px;height:30px;margin-top: 5px;"
                            @click="nofly">绘制路线</el-button>

                    </el-collapse-item>
                </el-collapse>
            </el-aside>

            <el-main>
                <div id="map" style="width: 100%; height: 618px;margin-top: 30px;"></div>

            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import { getid } from '../utils/api'
import { tx } from '../utils/api'
import { dp } from '../utils/api'
import { sa } from '../utils/api'
import { tabu } from '../utils/api'
import { aco } from '../utils/api'
import { mtsp } from '../utils/api'
import { reshow } from '../utils/api'
import { toRaw } from '@vue/reactivity'

export default {
    data() {
        return {
            //返回的路线点的顺序
            path_tx: [],
            path_dp: [],
            path_sa: [],
            path_tabu: [],
            path_aco: [],
            map: null,
            isDrawing: false,
            isDrawingLines: false,

            circles: [],
            lines: null,
            lines_tx: null,
            lines_dp: null,
            lines_sa: null,
            lines_tabu: null,
            lines_aco: null,
            zoom: 12,
            pointData: {
                num: 0,
                alg_id: 0,
                points: [],
                x: [],
                y: [],
            },

            //表格的数据

            id_get: false,
            time_tanxin: 0,
            time_dp: 0,
            time_sa: 0,
            time_tabu: 0,
            time_aco: 0,
            distance_tx: 0,
            distance_dp: 0,
            distance_sa: 0,
            distance_tabu: 0,
            distance_aco: 0,
            tableData: [],
            tableflag: true,

            multipleSelection: [],

            //多架无人机
            line: null,//画出的路线
            radioVal: true,//选择优先条件
            planeNum: 0,//无人机数量
            limit: 0,//距离限制
            mTSPData: [],//表格数据

            //禁飞区
            noflyPriority: true,//选择优先条件
            planeNumNofly: 0,//无人机数量
            limitNofly: 0,//距离限制
            isDrawingNofly: false,//是否绘制禁飞区
            noflyData: {
                num: 0,
                points: [],
                x: [],
                y: [],
            },
            //drawingManager: null,
            overlays: [],
            polygonPath:[],
            noflyNum: 0,
            totalCricles: [[]],
        };
    },

    created() {
        const script = document.createElement("script");
        script.type = "text/javascript";
        script.src = "http://api.map.baidu.com/api?v=2.0&ak=你的百度地图密钥&callback=initMap";
        script.onerror = () => {
            throw new Error("无法加载百度地图 API");
        };
        window.initMap = this.initMap;
        document.body.appendChild(script);
    },
    methods: {
        initMap() {
            // 创建地图容器
            this.map = new BMap.Map("map");
            // 设置地图中心点和缩放级别
            const point = new BMap.Point(116.404, 39.915);
            this.map.centerAndZoom(point, 15);
            this.map.enableScrollWheelZoom();
            // 监听地图的点击事件
            this.map.addEventListener("click", (e) => {
                if (this.isDrawing) {
                    // 创建圆
                    const point = e.point;
                    const circle = new BMap.Circle(point, 50, {
                        strokeColor: "black",
                        strokeWeight: 5,
                        fillColor: "purple",
                        fillOpacity: 0.2,
                    });
                    this.map.addOverlay(circle);
                    this.pointData.points.push({ lng: point.lng, lat: point.lat });
                    this.pointData.x.push(point.lng);
                    this.pointData.y.push(point.lat);
                    this.pointData.num++;
                    this.circles.push(circle);
                    console.log("点击点的经度：" + point.lng + "，纬度：" + point.lat);
                }
                //else if (this.isDrawingNofly) {
                // 创建圆
                // const pointNofly = e.point;
                // const circleNofly = new BMap.Circle(pointNofly, 50, {
                //     strokeColor: "red",
                //     strokeWeight: 5,
                //     fillColor: "blue",
                //     fillOpacity: 0.2,
                // });
                // this.map.addOverlay(circleNofly);
                // this.noflyData.points.push({ lng: point.lng, lat: point.lat });
                // this.noflytData.x.push(point.lng);
                // this.noflyData.y.push(point.lat);
                // this.noflyData.num++;
                // this.circlesNofly.push(circle);
                // console.log("禁飞区点的经度：" + point.lng + "，纬度：" + point.lat);

                //     let overlays = [];
                //     let overlaycomplete = function (e) {
                //         overlays.push(e.overlay);
                //     };
                //     let styleOptions = {
                //         strokeColor: "red", //边线颜色。
                //         fillColor: "red", //填充颜色。当参数为空时，圆形将没有填充效果。
                //         strokeWeight: 3, //边线的宽度，以像素为单位。
                //         strokeOpacity: 0.8, //边线透明度，取值范围0 - 1。
                //         fillOpacity: 0.6, //填充的透明度，取值范围0 - 1。
                //         strokeStyle: 'solid' //边线的样式，solid或dashed。
                //     }
                //     //实例化鼠标绘制工具
                //     this.drawingManager = new BMapLib.DrawingManager(this.map, {
                //         isOpen: false, //是否开启绘制模式
                //         // enableDrawingTool: true, //是否显示工具栏
                //         drawingToolOptions: {
                //             anchor: BMAP_ANCHOR_TOP_RIGHT, //位置
                //             offset: new BMap.Size(5, 5), //偏离值
                //         },
                //         circleOptions: styleOptions, //圆的样式
                //         polylineOptions: styleOptions, //线的样式
                //         polygonOptions: styleOptions, //多边形的样式
                //         rectangleOptions: styleOptions //矩形的样式
                //     });
                //     //添加鼠标绘制工具监听事件，用于获取绘制结果
                //     this.drawingManager.addEventListener('overlaycomplete', overlaycomplete);

                // }
            });

            if (this.$route.params.id_select) {
                console.log(this.$route.params.id_select)
                //const res = reshow(this.$route.params.id_select, this.$route.params.alg_select)
                reshow(this.$route.params.id_select, this.$route.params.alg_select).then(res => {
                    if (res.state == 200) {
                        const crystal = toRaw(res.data);
                        let x = crystal.x;
                        let y = crystal.y;
                        let path = crystal.path;
                        let pointsArray = [];

                        for (let i = 0; i < x.length; i++) {
                            const point1 = new BMap.Point(x[i], y[i]);
                            const circle1 = new BMap.Circle(point1, 50, {
                                strokeColor: "black",
                                strokeWeight: 5,
                                fillColor: "purple",
                                fillOpacity: 0.2,
                            });
                            this.map.addOverlay(circle1);
                        }


                        if (this.$route.params.alg_select == 1) {
                            for (let i = 0; i < path.length; i++) {
                                pointsArray.push(new BMap.Point(x[path[i] - 1], y[path[i] - 1]));
                            }
                        } else {
                            for (let i = 0; i < path.length; i++) {
                                pointsArray.push(new BMap.Point(x[path[i]], y[path[i]]));
                            }
                        }

                        this.lines = new BMap.Polyline(pointsArray, {
                            strokeColor: "red",
                            strokeWeight: 2,
                            strokeOpacity: 0.5,
                        });
                        this.map.addOverlay(this.lines);
                        this.isDrawingLines = true;


                        //弹出成功消息
                        this.$message({
                            showClose: true,
                            message: '复现成功',
                            type: 'success'
                        })

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
            }

        },
        startDrawing() {
            // 开始加点
            this.isDrawing = true;
            this.isDrawingNofly = false;
        },
        stopDrawing() {
            // 停止加点
            this.isDrawing = false;
        },
        clearMap() {
            // 移除所有圆
            this.circles.forEach((circle) => {
                this.map.removeOverlay(circle);
            });
            // 清空点数组和圆数组
            this.pointData.points = [];
            this.pointData.x = [];
            this.pointData.y = [];
            this.pointData.num = 0;
            this.circles = [];
            this.tableData = [];//清空表格数据
            this.tableflag = true;//标记变量改为true，需重新请求
            this.path_tx = [];
            this.path_dp = [];
            this.path_sa = [];
            this.path_tabu = [];
            this.path_aco = [];

            this.mTSPData = [];//多架无人机表格数据清空

            // 清空连线
            if (this.lines_tx) {
                this.map.removeOverlay(this.lines_tx);
                this.lines_tx = null;
            }
            if (this.lines_dp) {
                this.map.removeOverlay(this.lines_dp);
                this.lines_dp = null;
            }
            if (this.lines_sa) {
                this.map.removeOverlay(this.lines_sa);
                this.lines_sa = null;
            }
            if (this.lines_tabu) {
                this.map.removeOverlay(this.lines_tabu);
                this.lines_tabu = null;
            }
            if (this.lines_aco) {
                this.map.removeOverlay(this.lines_aco);
                this.lines_aco = null;
            }

            if (this.line) {
                this.map.removeOverlay(this.line);
                this.line = null;
            }
            this.map.clearOverlays();

            //将按钮设置为“绘制路线”
            this.isDrawingLines = false;
        },

        toggleLines() {
            if (this.isDrawingLines) {
                // 隐藏连线
                this.isDrawingLines = false;
                this.map.removeOverlay(this.lines_tx);
                this.map.removeOverlay(this.lines_dp);
                this.map.removeOverlay(this.lines_sa);
                this.map.removeOverlay(this.lines_tabu);
                this.map.removeOverlay(this.lines_aco);
                this.lines_dp = null;

            } else {
                // 绘制点的连线
                //首先判断path是否为空，
                //若tableflag = true,则path为空，则需发送请求，运行算法，并将表格的数据填充
                //若tableflag = false，则不为空，则之前已经运行过算法，表格数据不变，只需绘制路线
                if (this.tableflag) {
                    if (this.pointData.points.length < 2) {
                        return;
                    }

                    getid(this.pointData).then(res => {
                        if (res.state == 200) {
                            this.pointData.alg_id = res.data
                            console.log(this.pointData.alg_id)
                        }
                    }).then(res => {
                        if (this.pointData.alg_id != 0) {
                            //贪心算法
                            tx(this.pointData).then(res => {
                                if (res.state == 200) {
                                    const crystal = toRaw(res.data);
                                    this.path_tx = crystal.path
                                    console.log(this.path_tx)

                                    //画出路线
                                    let pointsArray = [];
                                    let point = [];
                                    let i = 0;
                                    const points = this.pointData.points
                                    for (i = 0; i < this.path_tx.length; i++) {
                                        point = points[this.path_tx[i]];
                                        pointsArray.push(new BMap.Point(point.lng, point.lat)); //添加每一个点
                                    }
                                    this.lines_tx = new BMap.Polyline(pointsArray, {
                                        strokeColor: "red",
                                        strokeWeight: 2,
                                        strokeOpacity: 0.5,
                                    });
                                    // this.map.addOverlay(this.lines_tx);
                                    // this.isDrawingLines = true;

                                    //添加表格数据并显示
                                    this.time_tanxin = crystal.time
                                    for (let i = 0; i < pointsArray.length - 1; i++) {
                                        this.distance_tx += this.map.getDistance(pointsArray[i], pointsArray[i + 1]);
                                    }
                                    this.tableData.push({ alg: "贪心", time: this.time_tanxin, distance: this.distance_tx.toFixed(2) });

                                    //弹出成功消息
                                    this.$message({
                                        showClose: true,
                                        message: '贪心算法成功',
                                        type: 'success'
                                    })


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


                            //动态规划算法
                            dp(this.pointData).then(res => {
                                if (res.state == 200) {
                                    const crystal = toRaw(res.data);
                                    this.path_dp = crystal.path
                                    console.log(this.path_dp)

                                    //画出路线
                                    let pointsArray_dp = [];
                                    let point_dp = [];
                                    let i_dp = 0;
                                    const points_dp = this.pointData.points
                                    for (i_dp = 0; i_dp < this.path_dp.length; i_dp++) {
                                        point_dp = points_dp[this.path_dp[i_dp] - 1];
                                        pointsArray_dp.push(new BMap.Point(point_dp.lng, point_dp.lat)); //添加每一个点
                                    }
                                    this.lines_dp = new BMap.Polyline(pointsArray_dp, {
                                        strokeColor: "blue",
                                        strokeWeight: 2,
                                        strokeOpacity: 0.5,
                                    });
                                    this.map.addOverlay(this.lines_dp);
                                    this.isDrawingLines = true;

                                    //添加表格数据并显示
                                    this.time_dp = crystal.time
                                    for (let i = 0; i < pointsArray_dp.length - 1; i++) {
                                        this.distance_dp += this.map.getDistance(pointsArray_dp[i], pointsArray_dp[i + 1]);
                                    }
                                    this.tableData.push({ alg: "动态规划", time: this.time_dp, distance: this.distance_dp.toFixed(2) })

                                    //弹出成功消息
                                    this.$message({
                                        showClose: true,
                                        message: '动态规划算法成功',
                                        type: 'success'
                                    })

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

                            //模拟退火算法
                            sa(this.pointData).then(res => {
                                if (res.state == 200) {
                                    const crystal = toRaw(res.data);
                                    this.path_sa = crystal.path
                                    console.log(this.path_sa)

                                    //画出路线
                                    let pointsArray_sa = [];
                                    let point_sa = [];
                                    let i_sa = 0;
                                    const points_sa = this.pointData.points
                                    for (i_sa = 0; i_sa < this.path_sa.length; i_sa++) {
                                        point_sa = points_sa[this.path_sa[i_sa]];
                                        pointsArray_sa.push(new BMap.Point(point_sa.lng, point_sa.lat)); //添加每一个点
                                    }
                                    this.lines_sa = new BMap.Polyline(pointsArray_sa, {
                                        strokeColor: "black",
                                        strokeWeight: 2,
                                        strokeOpacity: 0.5,
                                    });
                                    // this.map.addOverlay(this.lines_sa);
                                    // this.isDrawingLines = true;


                                    //添加表格数据并显示
                                    this.time_sa = crystal.time
                                    for (let i = 0; i < pointsArray_sa.length - 1; i++) {
                                        this.distance_sa += this.map.getDistance(pointsArray_sa[i], pointsArray_sa[i + 1]);
                                    }
                                    this.tableData.push({ alg: "模拟退火", time: this.time_sa, distance: this.distance_sa.toFixed(2) })

                                    //弹出成功消息
                                    this.$message({
                                        showClose: true,
                                        message: '模拟退火算法成功',
                                        type: 'success'
                                    })
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

                            //禁忌搜索算法
                            tabu(this.pointData).then(res => {
                                if (res.state == 200) {
                                    const crystal = toRaw(res.data);
                                    this.path_tabu = crystal.path
                                    console.log(this.path_tabu)

                                    //画出路线
                                    let pointsArray_tabu = [];
                                    let point_tabu = [];
                                    let i_tabu = 0;
                                    const points_tabu = this.pointData.points
                                    for (i_tabu = 0; i_tabu < this.path_tabu.length; i_tabu++) {
                                        point_tabu = points_tabu[this.path_tabu[i_tabu]];
                                        pointsArray_tabu.push(new BMap.Point(point_tabu.lng, point_tabu.lat)); //添加每一个点
                                    }
                                    this.lines_tabu = new BMap.Polyline(pointsArray_tabu, {
                                        strokeColor: "green",
                                        strokeWeight: 2,
                                        strokeOpacity: 0.5,
                                    });
                                    // this.map.addOverlay(this.lines_tabu);
                                    // this.isDrawingLines = true;


                                    //添加表格数据并显示
                                    this.time_tabu = crystal.time
                                    for (let i = 0; i < pointsArray_tabu.length - 1; i++) {
                                        this.distance_tabu += this.map.getDistance(pointsArray_tabu[i], pointsArray_tabu[i + 1]);
                                    }
                                    this.tableData.push({ alg: "禁忌搜索", time: this.time_tabu, distance: this.distance_tabu.toFixed(2) })

                                    //弹出成功消息
                                    this.$message({
                                        showClose: true,
                                        message: '模拟退火算法成功',
                                        type: 'success'
                                    })
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

                            //蚁群算法
                            aco(this.pointData).then(res => {
                                if (res.state == 200) {
                                    const crystal = toRaw(res.data);
                                    this.path_aco = crystal.path
                                    console.log(this.path_aco)

                                    //画出路线
                                    let pointsArray_aco = [];
                                    let point_aco = [];
                                    let i_aco = 0;
                                    const points_aco = this.pointData.points
                                    for (i_aco = 0; i_aco < this.path_aco.length; i_aco++) {
                                        point_aco = points_aco[this.path_aco[i_aco]];
                                        pointsArray_aco.push(new BMap.Point(point_aco.lng, point_aco.lat)); //添加每一个点
                                    }
                                    this.lines_aco = new BMap.Polyline(pointsArray_aco, {
                                        strokeColor: "orange",
                                        strokeWeight: 2,
                                        strokeOpacity: 0.5,
                                    });
                                    // this.map.addOverlay(this.lines_aco);
                                    // this.isDrawingLines = true;


                                    //添加表格数据并显示
                                    this.time_aco = crystal.time
                                    for (let i = 0; i < pointsArray_aco.length - 1; i++) {
                                        this.distance_aco += this.map.getDistance(pointsArray_aco[i], pointsArray_aco[i + 1]);
                                    }
                                    this.tableData.push({ alg: "蚁群", time: this.time_aco, distance: this.distance_aco.toFixed(2) })

                                    //弹出成功消息
                                    this.$message({
                                        showClose: true,
                                        message: '蚁群算法成功',
                                        type: 'success'
                                    })
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

                            //标记变量设为false
                            this.tableflag = false;
                            console.log(this.tableData);
                        }
                    })


                }
                else {
                    //只绘制路线
                    let pointsArray_dp = [];
                    let point_dp = [];
                    let i_dp = 0;
                    const points_dp = this.pointData.points
                    for (i_dp = 0; i_dp < this.path_dp.length; i_dp++) {
                        point_dp = points_dp[this.path_dp[i_dp] - 1];
                        pointsArray_dp.push(new BMap.Point(point_dp.lng, point_dp.lat)); //添加每一个点
                    }
                    this.lines_dp = new BMap.Polyline(pointsArray_dp, {
                        strokeColor: "blue",
                        strokeWeight: 2,
                        strokeOpacity: 0.5,
                    });
                    this.map.addOverlay(this.lines_dp);
                    console.log("绘制路线");
                    this.isDrawingLines = true;
                }

            }

        },

        //移除最后一个点
        deleteLastPoint() {
            if (this.pointData.num > 0) {
                // 移除最后一个圆
                const circle = this.circles.pop();
                if (circle) {
                    this.map.removeOverlay(circle);
                    // 移除最后一个点
                    this.pointData.points.pop();
                    this.pointData.x.pop();
                    this.pointData.y.pop();
                    this.pointData.num--;
                }

                this.tableData = [];//清空表格数据
                this.mTSPData = [];//多架无人机表格数据清空
                this.tableflag = true;//标记变量改为true，需重新请求
                // 清空连线
                if (this.lines_tx) {
                    this.map.removeOverlay(this.lines_tx);
                    this.lines_tx = null;
                }
                if (this.lines_dp) {
                    this.map.removeOverlay(this.lines_dp);
                    this.lines_dp = null;
                }
                if (this.lines_sa) {
                    this.map.removeOverlay(this.lines_sa);
                    this.lines_sa = null;
                }
                if (this.lines_tabu) {
                    this.map.removeOverlay(this.lines_tabu);
                    this.lines_tabu = null;
                }
                if (this.lines_aco) {
                    this.map.removeOverlay(this.lines_aco);
                    this.lines_aco = null;
                }


                this.map.clearOverlays();

                for (let i = 0; i < this.pointData.points.length; i++) {
                    const point = this.pointData.points[i];
                    const circle1 = new BMap.Circle(point, 50, {
                        strokeColor: "black",
                        strokeWeight: 5,
                        fillColor: "purple",
                        fillOpacity: 0.2,
                    });
                    this.map.addOverlay(circle1);
                }

                //将按钮设置为“绘制路线”
                this.isDrawingLines = false
            }
        },

        //将表格中选择的数据存到multipleSelection
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },

        //绘制所选算法的路线
        draw() {
            if (this.multipleSelection.length == 0) {
                this.map.removeOverlay(this.lines_tx);
                this.map.removeOverlay(this.lines_sa);
                this.map.removeOverlay(this.lines_tabu);
                this.map.removeOverlay(this.lines_aco);
                this.map.addOverlay(this.lines_dp);

                return;
            }
            this.map.removeOverlay(this.lines_tx);
            this.map.removeOverlay(this.lines_dp);
            this.map.removeOverlay(this.lines_sa);
            this.map.removeOverlay(this.lines_tabu);
            this.map.removeOverlay(this.lines_aco);

            let algSelect = "";
            for (let i = 0; i < this.multipleSelection.length; i++) {
                algSelect = this.multipleSelection[i].alg;
                if (algSelect == "贪心") {
                    this.map.addOverlay(this.lines_tx);
                }
                else if (algSelect == "动态规划") {
                    this.map.addOverlay(this.lines_dp);
                }
                else if (algSelect == "模拟退火") {
                    this.map.addOverlay(this.lines_sa);
                }
                else if (algSelect == "禁忌搜索") {
                    this.map.addOverlay(this.lines_tabu);
                }
                else if (algSelect == "蚁群") {
                    this.map.addOverlay(this.lines_aco);
                }
            }
            this.isDrawingLines = true;
        },

        //跳转到历史表单界面
        history() {
            this.$router.push('/Form');
            //router.push('/Form');
        },

        //多架无人机协同算法
        synAlg() {
            this.mTSPData = [];//多架无人机表格数据清空
            //清空连线
            this.map.clearOverlays();
            const circle1 = new BMap.Circle(this.pointData.points[0], 50, {
                strokeColor: "black",
                strokeWeight: 15,
                fillColor: "green",
                fillOpacity: 0.2,
            });
            this.map.addOverlay(circle1);
            for (let i = 1; i < this.pointData.points.length; i++) {
                const point = this.pointData.points[i];
                const circle1 = new BMap.Circle(point, 50, {
                    strokeColor: "black",
                    strokeWeight: 5,
                    fillColor: "purple",
                    fillOpacity: 0.2,
                });
                this.map.addOverlay(circle1);
            }

            mtsp(this.pointData, this.planeNum, this.limit, this.radioVal).then(res => {
                if (res.state == 200) {
                    const crystal = toRaw(res.data);
                    const paths = crystal.path;
                    if (paths.length === 0) {
                        this.$message({
                            showClose: true,
                            message: '当前无人机无法满足巡回条件！',
                            type: 'error',
                        });
                        return;
                    }

                    let n = 1;
                    paths.forEach((path, index) => {
                        console.log(path.length);
                        let pointsArray = [];
                        let point = [];
                        const points = this.pointData.points;

                        for (let i = 0; i < path.length; i++) {
                            point = points[path[i]];
                            pointsArray.push(new BMap.Point(point.lng, point.lat));
                        }
                        // 自定义颜色
                        const randomColor = `#${Math.floor(Math.random() * 16777215).toString(16)}`;
                        this.line = new BMap.Polyline(pointsArray, {
                            strokeColor: randomColor,
                            strokeWeight: 2,
                            strokeOpacity: 0.9,
                        });

                        let dis = 0
                        for (let i = 0; i < pointsArray.length - 1; i++) {
                            dis += this.map.getDistance(pointsArray[i], pointsArray[i + 1]);
                        }
                        this.mTSPData.push({ no: n, distance_mtsp: dis.toFixed(2), color_mtsp: randomColor });
                        n++;


                        // TODO: 我的画图直接加在这里了，想知道有什么办法可以让上面的按钮控制？
                        this.map.addOverlay(this.line);


                        // TODO: 显示各个无人机的飞行距离
                        // 也许可以在表格里显示对应的颜色
                    });
                    this.isDrawingLines = true;
                    this.$message({
                        showClose: true,
                        message: '多无人机路径规划成功！',
                        type: 'success',
                    });
                } else {
                    this.$message({
                        showClose: true,
                        message: '规划失败',
                        type: 'error',
                    });
                }
            }).catch(err => {
                if (err.response) {
                    console.log(err.response);
                } else {
                    console.log(err);
                }
            });
        },

        //多架无人机表格中每行数据的字体颜色与路线颜色一致
        tableRowClassName({ row, rowIndex }) {
            console.log(this.mTSPData)
            let styleObj = {};
            styleObj.color = row.color_mtsp;
            return styleObj;
        },

        //开始绘制禁飞区
        // startDrawingNofly() {
        //     this.polygonPath.editing = true;
        //     this.isDrawing = false;
        // },

        startDrawingNofly(e) {
            this.isDrawingNofly = true;
             this.isDrawing = false;
            var styleOptions = {
                strokeColor: '#5E87DB', // 边线颜色
                fillColor: '#5E87DB', // 填充颜色。当参数为空时，圆形没有填充颜色
                strokeWeight: 2, // 边线宽度，以像素为单位
                strokeOpacity: 1, // 边线透明度，取值范围0-1
                fillOpacity: 0.2 // 填充透明度，取值范围0-1
            };
            var labelOptions = {
                borderRadius: '2px',
                background: '#FFFBCC',
                border: '1px solid #E1E1E1',
                color: '#703A04',
                fontSize: '12px',
                letterSpacing: '0',
                padding: '5px'
            };

            // 实例化鼠标绘制工具
            var drawingManager = new BMapGLLib.DrawingManager(this.map, {
                // isOpen: true,        // 是否开启绘制模式
                enableCalculate: false, // 绘制是否进行测距测面
                enableSorption: true, // 是否开启边界吸附功能
                sorptiondistance: 20, // 边界吸附距离
                circleOptions: styleOptions, // 圆的样式
                polylineOptions: styleOptions, // 线的样式
                polygonOptions: styleOptions, // 多边形的样式
                rectangleOptions: styleOptions, // 矩形的样式
                labelOptions: labelOptions // label样式
            });

            var arr = document.getElementsByClassName('bmap-btn');
            for (var i = 0; i < arr.length; i++) {
                arr[i].style.backgroundPositionY = '0';
            }
            e.currentTarget.style.backgroundPositionY = '-52px';

            var drawingType = BMAP_DRAWING_POLYLINE;
            // 进行绘制
            if (drawingManager._isOpen && drawingManager.getDrawingMode() === drawingType) {
                drawingManager.close();
            } else {
                drawingManager.setDrawingMode(drawingType);
                drawingManager.open();
            }
            drawingManager.addEventListener('overlaycomplete', this.overlaycomplete);
        },
        //获坐标集
        overlaycomplete(e) {
            let self = this
            //清除底层图案
            //this.map.clearOverlays();
            //画多边形
            //this.clearData();
            this.polygonPath = e.overlay.getPath(); //获取多边形路径点
            //this.drawingManager.close();
            //增加多边形，
            //this.polygon = new BMap.Polygon(this.polygonPath, { strokeColor: 'red', strokeWeight: 1, strokeOpacity: 0.85 });

            this.map.addOverlay(this.polygon);
            //编辑多边形
            //this.polygon.enableEditing();

            console.log(this.polygonPath)
        },
        //清空坐标点
        clearData() {
            for (var i = 0; i < this.overlays.length; i++) {
                this.map.removeOverlay(overlays[i]);
            }
            this.overlays.length = 0;
        },



        //停止绘制禁飞区
        stopDrawingNofly() {
            this.isDrawingNofly = false;
        },

        //禁飞区算法路线绘制
        nofly() {

        }
    },
}

</script>

<style lang="less" scoped>
/* 头部布局*/
.el-header {
    color: #0b0435;
    background-color: #d7f8ff;
    display: flex;
    justify-content: space-between;
    width: 100%;
    height: 50px;
    margin: 0 auto;
    position: absolute;
}

/* 标题 */
.left_box {
    color: #01040c;
    display: flex;
    width: 250dp;
    height: 50px;
    justify-content: space-between;
    margin: 0 auto;
    font-size: 35px;
}

/* 菜单 */
.selectTitle {
    color: #1b3366;
    display: flex;
    width: 250dp;
    height: 45px;
    font-size: 25px;
    justify-content: space-between;
    margin: 0 auto;
    // margin-top: 10px;
}

.el-aside {
    background-color: #d7f8ff; //#78cdd8;
    display: flex;
    flex-direction: column;
    width: "250px";
    height: 95vh;
}

:deep .collapse .el-collapse-item__header {
    border-bottom: 2px solid #1e9ee9;
    background-color: #d7f8ff;
}

:deep .collapse .el-collapse-item__wrap {
    border-bottom: 2px solid #1e9ee9;
    background-color: #d7f8ff;
}
</style>