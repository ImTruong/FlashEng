import { createRouter, createWebHistory } from "vue-router";
import ClassesView from "@/view/ClassesView.vue";
import HomeView from "@/view/HomeView.vue"

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: "/classes",
            name: "classes",
            component: ClassesView
        },
        {
            path: "/",
            name: "home",
            component: HomeView
        }
    ]
})

export default router