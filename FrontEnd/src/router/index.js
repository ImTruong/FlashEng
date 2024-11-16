import { createRouter, createWebHistory } from "vue-router"
import HomeView from "@/view/HomeView.vue"
import YourLibrary from "@/view/YourLibrary.vue";
import Login from "@/view/Login.vue";
import SignUp from "@/view/SignUp.vue";
import User from "@/view/User.vue";
import Statistics from "@/view/Statistics.vue";
import FlashCard from "../view/FlashCardView.vue"
import Review from  "../view/Review.vue"
import Fillgame from '../view/FillingGame.vue'


const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes:[
        {
            path: "/library",
            name: "library",
            component: YourLibrary
        },
        {
            path: "/",
            name: "home",
            component: HomeView
        },
        {
            path: "/login",
            name: "Login",
            component: Login
        },
        {
            path: "/signup",
            name: "SignUp",
            component: SignUp
        },
        {
            path: "/profile",
            name: "User",
            component: User
        },
        {
            path: "/statistics",
            name: "Statistics",
            component: Statistics
        },
        {
            path: "/flashcard/:id",
            name: "flashcard",
            component: FlashCard
        },
        {
            path: "/review",
            name: "review",
            component: Review
        },
        {
            path: "/fillgame/:id",
            name: "fillgame",
            component: Fillgame
        },
    ]
})

export default router