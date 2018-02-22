import Vue from 'vue'
import Router from 'vue-router'
import Readings from '@/components/Readings'
import Home from '@/components/Home'
import UserProfile from '@/components/UserProfile'

import store from '../store'

const theStore = store;

// https://stackoverflow.com/questions/42579601/how-to-access-async-store-data-in-vue-router-for-usage-in-beforeenter-hook
Vue.use(Router)

const router = new Router({
  routes: [
    {
      path: '/readings',
      name: 'Readings',
      component: Readings,
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
