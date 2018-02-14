import Vue from 'vue'
import Router from 'vue-router'
import Initial from '@/components/Initial'
import Goal from '@/components/Goal'
import Home from '@/components/Home'
import UserProfile from '@/components/UserProfile'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'Initial',
      component: Initial
    },
    {
      path: '/goal',
      name: 'Goal',
      component: Goal
    },
    {
      path: '/home',
      name: 'Home',
      component: Home
    },
    {
      path: '/userprofile',
      name: 'UserProfile',
      component: UserProfile
    }
  ]
})
