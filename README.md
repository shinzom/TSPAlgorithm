# 后端修改算法

## 传入后端

### Controller

请求的 mapping 地址仍然不变，新增了一个 `ForbiddenZone[]` 使用 `@RequestBody` 传输。 

```java
@RequestMapping("callAlgorithm_mtsp")
public JsonResult<ResultForMultipleDrones> callAlgorithm_mtsp(
        @RequestParam("num") int num,
        @RequestParam("x") String x,
        @RequestParam("y") String y,
        @RequestParam("planeNum") int planeNum,
        @RequestParam("limit") double limit,
        @RequestParam("radioVal") boolean radioVal,
        @RequestBody ForbiddenZone[] forbiddenZones)
{...}
```

返回的是一个` JsonResult<ResultForMultipleDrones>`

`ResultForMultipleDrones` 的定义为：

```java
public class ResultForMultipleDrones {
	// 原来的数据
  public long time;
  public int[][] path;
  
	// 新增数据（所有的数据点，包含原来的与新增的），格式与原 Data 相同
  public int num;
  public double[] x;
  public double[] y;

}
```



## 一个样例前端写法

```js
import axios from 'axios';

// mTSP
export function mtsp(pointData, inputDroneNumber, inputDroneRange, forbiddenZones) {
  console.log('mtsp');
  return axios({
    url: '/callAlgorithm_mtsp/',
    method: 'post',
    params: {
      num: pointData.num,
      x: JSON.stringify(pointData.x),
      y: JSON.stringify(pointData.y),
      planeNum: inputDroneNumber,
      limit: inputDroneRange,
      radioVal: true, 
    },
    data: forbiddenZones, // 注意请求体写在这里
    headers: {
      'Content-Type': 'application/json',
    },
  });
}

```



一个样例的` forbiddenZones` 数据格式：

```js
const forbiddenZones = [
  {
    nodeNum: 4,
    x: [1.0, 2.0, 2.0, 1.0],
    y: [1.0, 1.0, 2.0, 2.0]
  },
  {
    nodeNum: 3,
    x: [4.0, 5.0, 4.0],
    y: [3.0, 3.0, 4.0]
  }
];
```

