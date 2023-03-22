<template>
    <el-container style="width:100%">
        <el-header>
            <div class="left_box">
                <span>欢 迎</span>
            </div>
        </el-header>
        <el-container style="height: 100%;">
            <el-aside>
                <el-form-item label="菜单" style="margin-left: 12px;margin-top: 10px;"></el-form-item>
                <div class="selectTitle">
                    <span>菜单</span>
                </div>

                <el-form-item label="算法" style="margin-left: 20px;">
                    <el-select v-model="datasetSelect" class="filter-item" placeholder="选择算法" clearable
                        style="width: 200px;background-color: #ededc7;">
                        <el-option v-for="item in datasetOptions" :key="item.key" :label="item.label" :value="item.key" />
                    </el-select>
                </el-form-item>

                <div>
                    <div class="btn-group">
                        <el-button v-if="!isDrawing" @click="startDrawing">开始加点</el-button>
                        <el-button v-if="isDrawing" @click="stopDrawing">停止加点</el-button>
                        <el-button @click="clearMap">取消所有点</el-button>
                        <el-button @click="deleteLastPoint">删除上一个点</el-button>
                    </div>
                    <el-button style="background-color: #f4f2c7;width: 250px ;margin-left: 23px;margin-top: 10px;"
                        @click="toggleLines">{{ isDrawingLines ? '隐藏连线' : '绘制点的连线' }}</el-button>
                </div>

                <el-table ref="multipleTableRef" :data="tableData" style="width: 255px;margin-top: 10px;margin-left: 20px;">
                    <el-table-column type="selection" width="55" />
                    <el-table-column prop="alg" label="算法" width="100" />
                    <el-table-column prop="time" label="时间" sortable width="100" />
                </el-table>
            </el-aside>

            <el-main>
                <div id="map" style="width: 100%; height: 550px;margin-top: 30px;"></div>

            </el-main>
        </el-container>
    </el-container>
</template>

<script>
import { dataset } from '../utils/api'
import { toRaw } from '@vue/reactivity'
export default {
    data() {
        return {
            path: [],//返回的路线点的顺序
            datasetSelect: [],
            datasetOptions: [
                { label: "贪心算法", key: 1 },
                { label: "算法2", key: 2 },
                { label: "算法3", key: 3 },
                { label: "算法4", key: 4 },
                { label: "算法5", key: 5 },
                { label: "算法6", key: 6 },
                { label: "算法7", key: 7 },
            ],
            map: null,
            isDrawing: false,
            isDrawingLines: false,

            circles: [],
            lines: null,
            zoom: 12,
            pointData: {
                num: 0,
                points: [],
                x: [],
                y: [],
            },

            //表格的数据
            time_tanxin: 0,
            tableData: [],
            tableflag:true,
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
            });
        },
        startDrawing() {
            // 开始加点
            this.isDrawing = true;
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
            // 清空连线
            if (this.lines) {
                this.map.removeOverlay(this.lines);
                this.lines = null;
            }
        },
        toggleLines() {
            if (this.isDrawingLines) {
                // 隐藏连线
                this.isDrawingLines = false;
                this.map.removeOverlay(this.lines);
                this.lines = null;

            } else {
                // 绘制点的连线
                //首先判断path是否为空，
                //若tableflag = true,则path为空，则需发送请求，运行算法，并将表格的数据填充
                //若tableflag = false，则不为空，则之前已经运行过算法，表格数据不变，只需绘制路线
                if(this.tableflag) {
                    if (this.datasetSelect == []) {
                        this.$message({
                            message: '请选择算法',
                            type: 'warning'
                        });
                    }
                    else {
                        if (this.pointData.points.length < 2) {
                            return;
                        }
                        //贪心算法
                        if (this.datasetSelect == 1) {
                            dataset(this.pointData).then(res => {
                                if (res.state == 200) {
                                    const crystal = toRaw(res.data);
                                    this.path = crystal.path
                                    console.log(this.path)

                                    //画出路线
                                    let pointsArray = [];
                                    let point = [];
                                    let i = 0;
                                    const points = this.pointData.points
                                    for (i = 0; i < this.path.length; i++) {
                                        point = points[this.path[i]];
                                        console.log(point);
                                        pointsArray.push(new BMap.Point(point.lng, point.lat)); //添加每一个点
                                    }
                                    this.lines = new BMap.Polyline(pointsArray, {
                                        strokeColor: "red",
                                        strokeWeight: 2,
                                        strokeOpacity: 0.5,
                                    });
                                    this.map.addOverlay(this.lines);
                                    this.isDrawingLines = true;

                                    //添加表格数据并显示
                                    this.time_tanxin = crystal.time
                                    console.log(this.time_tanxin)
                                    this.tableData.push({ alg: "贪心算法", time: this.time_tanxin })

                                    //弹出成功消息
                                    this.$message({
                                        showClose: true,
                                        message: '成功',
                                        type: 'success'
                                    })

                                    //标记变量设为false
                                    this.tableflag = false;
                                } else {
                                    this.$message({
                                        showClose: true,
                                        //message: res.message,
                                        message: "错误",
                                        type: 'error'
                                    })
                                }
                            }).catch(err => {
                                console.log(err.response)
                            })
                        }
                    }
                }
                else{
                    //只绘制路线
                    let pointsArray = [];
                    let point = [];
                    let i = 0;
                    const points = this.pointData.points
                    for (i = 0; i < this.path.length; i++) {
                        point = points[this.path[i]];
                        console.log(point);
                        pointsArray.push(new BMap.Point(point.lng, point.lat)); //添加每一个点
                    }
                    this.lines = new BMap.Polyline(pointsArray, {
                        strokeColor: "red",
                        strokeWeight: 2,
                        strokeOpacity: 0.5,
                    });
                    this.map.addOverlay(this.lines);
                    this.isDrawingLines = true;
                } 

            }

        },
        deleteLastPoint() {
            if (this.pointData.num > 0) {
                // 移除最后一个圆
                const circle = this.circles.pop();
                if (circle) {
                    this.map.removeOverlay(circle);
                    // 移除最后一个点
                    this.points.pop();
                }
                // 清空连线
                if (this.lines) {
                    this.map.removeOverlay(this.lines);
                    this.lines = null;
                }
            }
        },
        getTotalLength() {
            if (this.lines) {
                const points = this.lines.getPath();
                let totalLength = 0;
                for (let i = 0; i < points.length - 1; i++) {
                    totalLength += this.map.getDistance(points[i], points[i + 1]);
                }
                return totalLength.toFixed(2);
            }
            return 0;
        }
    },
}

</script>

<style lang="less" scoped>
/* 头部布局*/
.el-header {
    color: #0b0435;
    background-color: #78cdd8;
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
    font-size: 25px;
    justify-content: space-between;
    margin: 0 auto;
    font-size: 35px;
}

/* 菜单 */
.selectTitle {
    color: #1b3366;
    display: flex;
    width: 250dp;
    height: 50px;
    font-size: 25px;
    justify-content: space-between;
    margin: 0 auto;
    margin-top: 10px;
}

.el-aside {
    background-color: #78cdd8;
    display: flex;
    flex-direction: column;
    width: "250px";
    height: 83vh;
}

.btn-group {
    margin-top: 10px;
}

.btn-group button {
    margin-left: 35px;
    width: 90px;
    background-color: #f4f2c7;
}

// .map {
//     width: 100%;
//     display: flex;
//     justify-content: center;
//     align-items: center;

//     .rightulliimg {
//         max-width: 100%;
//         max-height: 620px;

//     }
// }
</style>
