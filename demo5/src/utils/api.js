import request from "./request";

//发送数据集序号
export function dataset(pointData) {
  console.log('dataset_api')
  return request({
    // url: '/callAlgorithm/'+num+'/'+points,
    url: '/callAlgorithm/',
    method: 'post',
    params:{
      num:pointData.num,
      //xy:pointData.points,
      x:JSON.stringify(pointData.x),
      y:JSON.stringify(pointData.y),
    }
  })
}