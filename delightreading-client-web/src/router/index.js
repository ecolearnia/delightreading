import Vue from 'vue'
import Router from 'vue-router'
import HelloWorld from '@/components/HelloWorld'
import Goal from '@/components/Goal'
import Home from '@/components/Home'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'HelloWorld',
      component: HelloWorld
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
    }
  ]
})
