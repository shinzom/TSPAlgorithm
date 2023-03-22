import { createRouter } from 'vue-router'
import { createWebHashHistory } from 'vue-router'
import Home from '../views/Home.vue'
import NotFound from '../views/NotFound.vue'
import Show from '../views/Show.vue'
import map from '../views/map.vue'


const routes = [
  {path:'/',component:Home},
  {path: '/path(.*)',component:NotFound},
  {path:'/Show',component:Show},
  {path:'/map',component:map},
]

const router = createRouter({
  history:createWebHashHistory(),
  routes:routes
})

export default router