import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)

/* Layout */
import Layout from '@/layout'

/**
 * Note: sub-menu only appear when route children.length >= 1
 * Detail see: https://panjiachen.github.io/vue-element-admin-site/guide/essentials/router-and-nav.html
 *
 * hidden: true                   if set true, item will not show in the sidebar(default is false)
 * alwaysShow: true               if set true, will always show the root menu
 *                                if not set alwaysShow, when item has more than one children route,
 *                                it will becomes nested mode, otherwise not show the root menu
 * redirect: noRedirect           if set noRedirect will no redirect in the breadcrumb
 * name:'router-name'             the name is used by <keep-alive> (must set!!!)
 * meta : {
    roles: ['admin','editor']    control the page roles (you can set multiple roles)
    title: 'title'               the name show in sidebar and breadcrumb (recommend set)
    icon: 'svg-name'/'el-icon-x' the icon show in the sidebar
    breadcrumb: false            if set false, the item will hidden in breadcrumb(default is true)
    activeMenu: '/example/list'  if set path, the sidebar will highlight the path you set
  }
 */

/**
 * constantRoutes
 * a base page that does not have permission requirements
 * all roles can be accessed
 */
export const constantRoutes = [
  {
    path: '/login',
    component: () => import('@/views/login/index'),
    hidden: true
  },

  {
    path: '/404',
    component: () => import('@/views/404'),
    hidden: true
  },

  {
    path: '/',
    component: Layout,
    redirect: '/dashboard',
    children: [{
      path: 'dashboard',
      name: '主页',
      component: () => import('@/views/dashboard/index'),
      meta: { title: '主页', icon: 'el-icon-s-tools' }
    }]
  },

  {
    path: '/vod',
    component: Layout,
    redirect: '/vod/teacher/list',
    name: 'vod',
    meta: { title: '讲师管理', icon: 'el-icon-user' },
    children: [
      {
        path: 'teacher/list',
        name: 'TeacherList',
        component: () => import('@/views/vod/teacher/list'),
        meta: { title: '讲师列表', icon: 'table' }
      },
      {
        path: 'teacher/create',
        name: 'TeacherCreate',
        component: () => import('@/views/vod/teacher/form'),
        meta: { title: '添加讲师', icon: 'tree' }
      },
      {
        path: 'teacher/edit/:id',
        name: 'TeacherEdit',
        component: () => import('@/views/vod/teacher/form'),
        meta: { title: '编辑讲师' },
        hidden: true //隐藏(隐藏路由)
      }
    ]
  },

  {
    path: '/subject',
    component: Layout,
    redirect: '/subject/list',
    name: '课程分类管理',
    alwaysShow: true,
    meta: { title: '课程分类管理', icon: 'form' },
    children: [
      {
        path: 'list',
        name: '课程分类列表',
        component: () => import('@/views/vod/subject/list'),
        meta: { title: '课程分类列表', icon: 'table' }
      }
    ]
  },
  
    // 课程管理
    {
      path: '/vodcourse',
      component: Layout,
      redirect: '/vodcourse/course/list',
      name: 'Vodcourse',
      meta: {
        title: '点播管理',
        icon: 'el-icon-bank-card'
      },
      alwaysShow: true,
      children: [
        {
          path: 'course/list',
          name: 'CourseList',
          component: () => import('@/views/vod/course/list'),
          meta: { title: '课程列表', icon: 'table'}
        },
        {
          path: 'course/info',
          name: 'CourseInfo',
          component: () => import('@/views/vod/course/form'),
          meta: { title: '发布课程' },
          hidden: true
        },
        {
          path: 'course/info/:id',
          name: 'CourseInfoEdit',
          component: () => import('@/views/vod/course/form'),
          meta: { title: '编辑课程' },
          hidden: true
        },
        {
          path: 'course/chapter/:id',
          name: 'CourseChapterEdit',
          component: () => import('@/views/vod/course/form'),
          meta: { title: '编辑大纲' },
          hidden: true
        },
        {
          path: 'course/chart/:id',
          name: 'CourseChart',
          component: () => import('@/views/vod/course/chart'),
          meta: { title: '课程统计' },
          hidden: true
        }
      ]
    },

    {
      path: '/order',
      component: Layout,
      redirect: '/order/orderInfo/list',
      name: 'Order',
      meta: { title: '订单管理', icon: 'el-icon-truck' },
      alwaysShow: true,
      children: [
        {
          path: 'orderInfo/list',
          name: 'OrderInfo',
          component: () => import('@/views/order/list'),
          meta: { title: '订单列表', icon: 'table'}
        }
      ]
    },

    {
      path: '/activity',
      component: Layout,
      redirect: '/couponInfo/list',
      name: 'Activity',
      meta: { title: '营销管理', icon: 'el-icon-football' },
      alwaysShow: true,
      children: [
        {
          path: 'couponInfo/list',
          name: 'CouponInfo',
          component: () => import('@/views/activity/couponInfo/list'),
          meta: { title: '优惠券列表', icon: 'table' }
        },
        {
          path: 'couponInfo/add',
          name: 'CouponInfoAdd',
          component: () => import('@/views/activity/couponInfo/form'),
          meta: { title: '添加' },
          hidden: true
        },
        {
          path: 'couponInfo/edit/:id',
          name: 'CouponInfoEdit',
          component: () => import('@/views/activity/couponInfo/form'),
          meta: { title: '编辑', noCache: true },
          hidden: true
        },
        {
          path: 'couponInfo/show/:id',
          name: 'CouponInfoShow',
          component: () => import('@/views/activity/couponInfo/show'),
          meta: { title: '详情', noCache: true },
          hidden: true
        }
      ]
    },

    {
      path: '/wechat',
      component: Layout,
      redirect: '/wechat/menu/list',
      name: 'Wechat',
      meta: {
        title: '公众号菜单管理',
        icon: 'el-icon-refrigerator'
      },
      alwaysShow: true,
      children: [
        {
          path: 'menu/list',
          name: 'Menu',
          component: () => import('@/views/wechat/menu/list'),
          meta: { title: '菜单列表', icon: 'table' }
        }
      ]
  },

  {
    path: '/live',
    component: Layout,
    redirect: '/live/liveCourse/list',
    name: 'Live',
    meta: {
      title: '直播管理',
      icon: 'el-icon-view'
    },
    alwaysShow: true,
    children: [
      {
        path: 'liveCourse/list',
        name: 'liveCourseList',
        component: () => import('@/views/live/liveCourse/list'),
        meta: { title: '直播列表', icon: 'table' }
      },
      {
        path: 'liveCourse/config/:id',
        name: 'liveCourseConfig',
        component: () => import('@/views/live/liveCourse/config'),
        meta: { title: '直播配置' },
        hidden: true
      },
      {
        path: 'liveVisitor/list/:id',
        name: 'liveVisitor',
        component: () => import('@/views/live/liveVisitor/list'),
        meta: { title: '观看记录' },
        hidden: true
      }
    ]
  },

  {
    path: '/form',
    component: Layout,
    children: [
      {
        path: 'index',
        name: 'Form',
        component: () => import('@/views/form/index'),
        meta: { title: 'Form', icon: 'form' }
      }
    ]
  },

  {
    path: '/nested',
    component: Layout,
    redirect: '/nested/menu1',
    name: 'Nested',
    meta: {
      title: 'Nested',
      icon: 'nested'
    },
    children: [
      {
        path: 'menu1',
        component: () => import('@/views/nested/menu1/index'), // Parent router-view
        name: 'Menu1',
        meta: { title: 'Menu1' },
        children: [
          {
            path: 'menu1-1',
            component: () => import('@/views/nested/menu1/menu1-1'),
            name: 'Menu1-1',
            meta: { title: 'Menu1-1' }
          },
          {
            path: 'menu1-2',
            component: () => import('@/views/nested/menu1/menu1-2'),
            name: 'Menu1-2',
            meta: { title: 'Menu1-2' },
            children: [
              {
                path: 'menu1-2-1',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-1'),
                name: 'Menu1-2-1',
                meta: { title: 'Menu1-2-1' }
              },
              {
                path: 'menu1-2-2',
                component: () => import('@/views/nested/menu1/menu1-2/menu1-2-2'),
                name: 'Menu1-2-2',
                meta: { title: 'Menu1-2-2' }
              }
            ]
          },
          {
            path: 'menu1-3',
            component: () => import('@/views/nested/menu1/menu1-3'),
            name: 'Menu1-3',
            meta: { title: 'Menu1-3' }
          }
        ]
      },
      {
        path: 'menu2',
        component: () => import('@/views/nested/menu2/index'),
        name: 'Menu2',
        meta: { title: 'menu2' }
      }
    ]
  },

  {
    path: 'external-link',
    component: Layout,
    children: [
      {
        path: 'https://panjiachen.github.io/vue-element-admin-site/#/',
        meta: { title: 'External Link', icon: 'link' }
      }
    ]
  },

  // 404 page must be placed at the end !!!
  { path: '*', redirect: '/404', hidden: true }
]

const createRouter = () => new Router({
  // mode: 'history', // require service support
  scrollBehavior: () => ({ y: 0 }),
  routes: constantRoutes
})

const router = createRouter()

// Detail see: https://github.com/vuejs/vue-router/issues/1234#issuecomment-357941465
export function resetRouter() {
  const newRouter = createRouter()
  router.matcher = newRouter.matcher // reset router
}

export default router
