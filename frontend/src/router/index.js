import { createRouter } from 'vue-router'
import { createWebHashHistory } from 'vue-router'
import Form from '../views/Form.vue'
import Map from '../views/Map.vue'
// import Home from '../views/Home.vue'
// import NotFound from '../views/NotFound.vue'
// import Show from '../views/Show.vue'


// const routes = [
//   {path:'/',component:Home},
//   {path: '/path(.*)',component:NotFound},
//   {path:'/Show',component:Show},
// ]

const routes = [
    {path:'/',component:Map},
    {path:'/:id_select/:alg_select',component:Map},
    {path:'/Form',component:Form},
]
const router = createRouter({
  history:createWebHashHistory(),
  routes:routes
})

export default router