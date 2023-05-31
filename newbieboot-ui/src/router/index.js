import Vue from 'vue'
import VueRouter from 'vue-router'
import ElementUI from "element-ui"

// 防止连续点击多次路由报错
let routerPush = VueRouter.prototype.push;
let routerReplace = VueRouter.prototype.replace;
// push
VueRouter.prototype.push = function push(location) {
  return routerPush.call(this, location).catch(err => err)
}
// replace
VueRouter.prototype.replace = function push(location) {
  return routerReplace.call(this, location).catch(err => err)
}

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Manage',
    component: () => import('../views/sys/Manage'),
    redirect: '/home',
    children: [
      // 注意path不能有/，因为会和父path拼接
      {
        path: 'home',
        name: 'Home',
        component: () => import('../views/Home'),
        meta: {
          isAuth: true,
          title: '首页',
          role: ['ROLE_ADMIN', 'ROLE_COMMON']
        }
      },
      {
        path: 'user',
        name: 'User',
        component: () => import('../views/sys/User'),
        meta: {
          isAuth: true,
          title: '用户管理',
          role: 'ROLE_ADMIN'
        }
      },
      {
        path: 'role',
        name: 'Role',
        component: () => import('../views/sys/Role'),
        meta: {
          isAuth: true,
          title: '角色管理',
          role: 'ROLE_ADMIN'
        }
      },
      {
        path: 'menu',
        name: 'Menu',
        component: () => import('../views/sys/Menu'),
        meta: {
          isAuth: true,
          title: '菜单管理',
          role: 'ROLE_ADMIN'
        }
      },
      {
        path: 'personInfo',
        name: 'PersonInfo',
        component: () => import('../views/sys/PersonInfo'),
        redirect: '/personInfo/baseInfo',
        meta: {
          isAuth: true,
          title: '个人信息',
          role: ['ROLE_ADMIN', 'ROLE_COMMON']
        },
        children: [
          {
            path: '/personInfo/baseInfo',
            name: 'BaseInfo',
            component: () => import('../views/sys/BaseInfo'),
            meta: {
              isAuth: true,
              title: '基本信息',
              role: ['ROLE_ADMIN', 'ROLE_COMMON']
            }
          },
          {
            path: '/personInfo/updatePwd',
            name: 'updatePwd',
            component: () => import('../views/sys/UpdatePwd'),
            meta: {
              isAuth: true,
              title: '修改密码',
              role: ['ROLE_ADMIN', 'ROLE_COMMON']
            }
          }
        ]
      }
    ]
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  // 画面从页面顶部开始展示
  document.body.scrollTop = 0;
  document.documentElement.scrollTop = 0;
  if (to.meta.isAuth) {
    const user = localStorage.getItem('user') ? JSON.parse(localStorage.getItem('user')) : null
    if (user === null) {
      ElementUI.Message({
        type: 'warning',
        message: '未登录，请先登录'
      });
      router.push('/login')
    } else {
      const role = user.role;
      if (to.meta.role.includes(role.roleKey)) {
        next()
      } else {
        ElementUI.Message({
          type: 'warning',
          message: '权限不足，无法访问'
        });
        router.push(from.path)
      }
    }
  } else {
    next()
  }
})

export default router
