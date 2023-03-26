import request from "./request";

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
    }
  })
}

//蚁群算法
export function aco(pointData) {
  console.log('aco')
  return request({
    url: '/callAlgorithm_aco/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
    }
  })
}

// mTSP
export function mtsp(pointData, inputDroneNumber, inputDroneRange) {
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
    }
  })
}
