<template>
    <div>
        <div id="map" style="width: 100%; height: 400px;"></div>
        <div class="btn-group">
            <button v-if="!isDrawing" @click="startDrawing">开始加点</button>
            <button v-if="isDrawing" @click="stopDrawing">停止加点</button>
            <button @click="clearMap">取消所有点</button>
            <button @click="toggleLines">{{ isDrawingLines ? '隐藏连线' : '绘制点的连线' }}</button>

        </div>
    </div>
</template>
  
<script>
export default {
    data() {
        return {
            map: null,
            isDrawing: false,
            isDrawingLines: false,
            points: [],
            circles: [],
            lines: null,
            zoom: 12,
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
                    const circle = new BMap.Circle(point, 100, {
                        strokeColor: "blue",
                        strokeWeight: 2,
                        fillColor: "blue",
                        fillOpacity: 0.2,
                    });
                    this.map.addOverlay(circle);
                    this.points.push({ lng: point.lng, lat: point.lat });
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
            this.points = [];
            this.circles = [];
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
                if (this.points.length < 2) {
                    return;
                }
                const points = this.points.map((p) => new BMap.Point(p.lng, p.lat));
                this.lines = new BMap.Polyline(points, {
                    strokeColor: "red",
                    strokeWeight: 2,
                    strokeOpacity: 0.5,
                });
                this.map.addOverlay(this.lines);
                this.isDrawingLines = true;
            }

        },
       
    },
};
</script>

<style>
.btn-group {
    margin-top: 10px;
}

.btn-group button {
    margin-right: 10px;
}
</style>
  