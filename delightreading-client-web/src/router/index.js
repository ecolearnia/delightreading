import Vue from 'vue'
import Router from 'vue-router'
import Initial from '@/components/Initial'
import Goal from '@/components/Goal'
import Home from '@/components/Home'
import UserProfile from '@/components/UserProfile'

import Team from '@/components/content/Team'

import store from '../store'

const theStore = store;

// https://stackoverflow.com/questions/42579601/how-to-access-async-store-data-in-vue-router-for-usage-in-beforeenter-hook
Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/',
      name: 'Initial',
      component: Initial
    },
    {
      path: '/goal',
      name: 'Goal',
      component: Goal,
      meta: { requiresAuth: true }
    },
    {
      path: '/home',
      name: 'Home',
      component: Home,
      meta: { requiresAuth: true }
    },
    {
      path: '/userprofile',
      name: 'UserProfile',
      component: UserProfile,
      meta: { requiresAuth: true }
    },
    {
      path: '/team',
      name: 'Team',
      component: Team
    }
  ]
});

export const LOGIN_PAGE_PATH = "/";

router.beforeEach((to, from, next) => {
  if (to.matched.some(record => record.meta.requiresAuth)) {
    // this route requires auth, check if logged in
    // if not, redirect to login page.
    if (!theStore.getters.isAuthenticated) {
      next({
        path: LOGIN_PAGE_PATH,
        query: { redirect: to.fullPath }
      })
    } else {
      next();
    }
  } else {
    next(); // make sure to always call next()!
  }
});

export default router;
