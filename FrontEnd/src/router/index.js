import { createRouter, createWebHistory } from "vue-router";
import ClassesView from "@/view/YourLibrary.vue";
import HomeView from "@/view/HomeView.vue"
import YourLibrary from "@/view/YourLibrary.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: "/classes",
            name: "classes",
            component: YourLibrary
        },
        {
            path: "/",
            name: "home",
            component: HomeView
        }
    ]
})

export default router