import { createRouter, createWebHistory } from "vue-router"
import HomeView from "@/view/HomeView.vue"
import YourLibrary from "@/view/YourLibrary.vue"
import FlashCard from "../view/FlashCardView.vue"

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
        },
        {
            path: "/flashcard/:id",
            name: "flashcard",
            component: FlashCard
        }
    ]
})

export default router