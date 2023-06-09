import request from "./request";
import axios from "axios";

//获取id
export function getid(pointData) {
  console.log('getid')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/get_id/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
      
    }
  })
}

//贪心算法
export function tx(pointData) {
  console.log('tx')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/callAlgorithm_tx/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
      id:pointData.alg_id,
    }
  })
}

//动态规划算法
export function dp(pointData) {
  console.log('dp')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/callAlgorithm_dp/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
      id:pointData.alg_id,
    }
  })
}

//模拟退火算法
export function sa(pointData) {
  console.log('sa')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/callAlgorithm_sa/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
      id:pointData.alg_id,
    }
  })
}

//禁忌搜索算法
export function tabu(pointData) {
  console.log('tabu')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/callAlgorithm_tabu/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
      id:pointData.alg_id,
    }
  })
}

//蚁群算法
export function aco(pointData) {
  console.log('aco')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/callAlgorithm_aco/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
      id:pointData.alg_id,
    }
  })
}

// // mTSP
// export function mtsp(pointData, inputDroneNumber, inputDroneRange, forbiddenZones) {
//   console.log('mtsp');
//   return axios({
//     url: '/callAlgorithm_mtsp/',
//     method: 'post',
//     data: {
//       num: pointData.num,
//       x: JSON.stringify(pointData.x),
//       y: JSON.stringify(pointData.y),
//       planeNum: inputDroneNumber,
//       limit: inputDroneRange,
//       radioVal: true,
//       forbiddenZones: forbiddenZones
//     },
//     headers: {
//       'Content-Type': 'application/json',
//     },
//   });
// }


// mtsp
export function mtsp(pointData, inputDroneNumber, inputDroneRange, radioVal) {
  console.log('mtsp')
  return request({
    url: '/callAlgorithm_mtsp/',
    method: 'post',
    params:{
      num: pointData.num,
      x: JSON.stringify(pointData.x),
      y: JSON.stringify(pointData.y),
      planeNum: inputDroneNumber,
      limit: inputDroneRange,
      radioVal: radioVal,
    },
    data: [],
    headers: {
      'Content-Type': 'application/json',
    },
  })
}




//历史记录图表
export function show() {
  console.log('showTable')
  return request({
    url: '/show/',
    method: 'post',
  })
}

//复现
export function reshow(id_select,alg_select) {
  console.log('reshow')
  return request({
    url: '/get_data/',
    method: 'post',
    params:{
      id: id_select,
      al: alg_select,
    }
  })
}

//导出文件
export function kml(id_export,alg_export,fileName) {
  console.log('导出文件')
  return request({
    url: '/get_kml/',
    method: 'post',
    params:{
      id: id_export,
      al: alg_export,
      filename:fileName,
    }
  })
}

//禁飞区
export function nofly(pointData, inputDroneNumber, inputDroneRange,radioVal,noflyData) {
  console.log('nofly')
  return request({
    url: '/callAlgorithm_mtsp/',
    method: 'post',
    params:{
      num: pointData.num,
      x: JSON.stringify(pointData.x),
      y: JSON.stringify(pointData.y),
      planeNum: inputDroneNumber,
      limit: inputDroneRange,
      radioVal: radioVal,
    },
    data: noflyData, // 注意请求体写在这里
    headers: {
      'Content-Type': 'application/json',
    },
  })
}


export function downloadKML(newPoints, path) {
  const requestData = {
    newPoints: newPoints,
    path: path
  };

  // 使用fetch API 发送请求
  return fetch('http://localhost:8080/downloadKML', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestData)
  })
    .then(response => {
      if (!response.ok) {
        throw new Error('Network response was not ok');
      }
      return response.blob();
    })
    .then(blob => {
      // 创建一个临时URL对象以下载文件
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.setAttribute('download', 'paths.zip');
      document.body.appendChild(link);
      link.click();
    })
    .catch(error => {
      console.error('There was a problem with the fetch operation:', error);
    });
}
