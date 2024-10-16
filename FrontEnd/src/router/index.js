import { createRouter, createWebHistory } from "vue-router";
import ClassesView from "@/view/ClassesView.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: "/",
            name: "classes",
            component: ClassesView
        }
    ]
})

export default router