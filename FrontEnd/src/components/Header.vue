<script setup>
    import { ref } from 'vue';
    import SetTable from "../components/SetTable.vue"
    import OverlayBackground from "../components/OverlayBackground.vue";
    import classesData from "../data/classes.json" 
    // import JoinBox from "../components/JoinBox.vue"
    import setsData from "../data/sets.json"
    import SeachClasses from './SeachClasses.vue';
    import ClassTable from './ClassTable.vue';

    const menuOpen = ref(false);
    const showNotifications = ref(false)
    const setTable = ref(false)
    const searchQuery = ref("")
    const Overlay_background = ref("false");
    const showSearch = ref(false)
    const classItems = ref([])
    const setItems = ref([])
    const searchItem = ref("")
    const newItem = ref(false);
    const classTable = ref(false)


    const toggleMenu = () => {
        menuOpen.value = !menuOpen.value;
    };

    const toggleNotifications = () => {
        showNotifications.value = !showNotifications.value
    }

    const showSetTable = () =>{
        setTable.value = !setTable.value;
        newItem.value = !newItem.value
    }
    const showClassTable = () =>{
        classTable.value = !classTable.value;
        newItem.value = !newItem;
    }
    const performSearch = (searchQuery, () => {
        if (searchQuery.value) {
            // Lọc danh sách class từ dữ liệu classesData dựa trên searchQuery
            classItems.value = classesData.filter(classData => 
                classData.name.toLowerCase().includes(searchQuery.value.toLowerCase())
            );
            setItems.value = setsData.filter(setData => 
                setData.name.toLowerCase().includes(searchQuery.value.toLowerCase())
            );
        }
        showSearch.value = true;
        Overlay_background.value = true;
        searchItem.value = searchQuery.value
        searchQuery.value = ""
    })


</script>

<template>
    <div class="container">
        <OverlayBackground :isVisible="menuOpen" @clickOverlay="menuOpen = false" />
        <header>
            <div class="nav-header">
                <button class="nav-toggle" @click="toggleMenu">☰</button>
            </div>
            <div v-if="menuOpen" class="nav-menu">
                <ul>
                    <li>
                        <img src="@/assets/icons/home-icon.png" alt="Home"  class="menu-icon"/>
                        <router-link to="/" class="menu-link">Home</router-link>
                        
                    </li>
                    <li>
                        <img src="@/assets/icons/folder.png" alt="folder"  class="menu-icon"/>
                        <router-link to="/classes" class="menu-link">Your Library</router-link>
                    </li>
                    <li>
                        <img src="@/assets/icons/statistics.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Statistics</a>
                    </li>
                    <li>
                        <img src="@/assets/notification.svg" alt="Home"  class="menu-icon"/>
                        <a href="#">Notifications</a>
                    </li>
                    <li>
                        <img src="@/assets/user.svg" alt="Home"  class="menu-icon"/>
                        <a href="#">Your profile</a>
                    </li>
                    <li class="divider"></li>
                    <li>
                        <img src="@/assets/icons/logout.png" alt="Home"  class="menu-icon"/>
                        <a href="#">Log out</a>
                    </li>
                </ul>
            </div>
            <input 
                type="text" 
                v-model="searchQuery"
                @keyup.enter="performSearch" 
                placeholder="Search for flashcards sets, classes" 
                class="search-bar"/>
            <div class="button-container">
                <button class="add-button" @click="newItem = !newItem"> 
                    <img src="../assets/plus.svg" alt="Add set">
                </button>
                
                <div class="user-icon">
                    U
                </div>
            </div>

            <div v-if="showSearch" class="search-class">
                <SeachClasses
                    :searchQuery="searchItem"
                    :sets="setItems"
                    :classes="classItems"
                    :Overlay_background="Overlay_background"
                    @close="Overlay_background = false"
                />
            </div>

            <div class="new-set-class-container" v-if="newItem">
                <p  @click="showSetTable" >
                    <img src="../assets/new-set.svg" alt="">
                    Flashcard set
                </p>
                <p @click="showClassTable">
                    <img src="../assets/class.svg" alt="">
                    Class
                </p>
            </div>
            <SetTable v-if="setTable" @close="setTable = false" />
            <ClassTable v-if="classTable" @close="classTable = false"></ClassTable>
        </header>
    </div>
</template>

<style scoped>
    .new-set-class-container{
        position: fixed;
        padding: 5px;
        top: 50px;
        right: 80px;
        width: 150px;
        background-color: #ffffff;
        border-radius: 5px;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.5);
    }
    .new-set-class-container p{
        height: 40px;
        padding: 5px;
        font-size: 18px;
        cursor: pointer;
        border-radius: 5px;
    }

    .new-set-class-container p img{
        width: 20px;
    }

    .new-set-class-container p:hover{
        background-color: #dcdcdc;
    }

    header {
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 5px;
        background-color: #DBF3F7;
        border-bottom: 1px solid #ccc;
        position: fixed;
        top: 0;
        left: 0;
        width: 100%;
        height: 50.67px;
        z-index: 9999; 
    }
        
    .nav-header {
        display: flex;
        align-items: center;
    }

    .nav-toggle {
        background: none;
        border: none;
        font-size: 24px;
        cursor: pointer;
        color: #333;
    }

    .nav-menu {
        position: absolute;
        top: 50px;
        left: 0;
        width: 250px;
        height: 100vh;
        background-color: #fff;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
        display: flex;
        justify-content: center;
        flex-direction: column;
        justify-content: flex-start;
        padding: 20px;
        z-index: 9999;
    }

    .menu-icon {
        width: 24px;  /* Chỉnh kích thước rộng */
        height: 24px; /* Chỉnh kích thước cao */
        margin-right: 10px;
    }  

    .nav-menu ul {
        list-style: none;
        padding: 0;
        margin: 0;
    }

    .nav-menu ul li {
        display: flex;
        margin: 10px 0;
        align-items: center;
    }

    .nav-menu ul li a {
        text-decoration: none;
        color: #333;
        font-size: 14px;
        padding: 10px;
        display: block;
        font-weight: 500;
    }

    .nav-menu ul li a:hover {
        background-color: rgba(128, 128, 128, 0.263);
        color: white;
        width: 100%;
    }
    .button-container {
        position: fixed;
        right: 80px;
        display: flex; /* Sử dụng flexbox cho container */
        align-items: center; /* Căn giữa dọc */
        /* margin-left: 10px; Khoảng cách bên trái */
    }

    .user-icon {
        position: fixed;
        right: 30px;
        font-size: 16px;
        background-color: #D9D9D9; /* Màu nền cho icon user */
        color: white; /* Màu chữ */
        width: 40px; /* Kích thước rộng của icon */
        height: 40px; /* Kích thước cao của icon */
        display: flex;
        align-items: center;
        justify-content: center;
        border-radius: 50%; /* Tạo hình tròn */
        cursor: pointer;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
    }

    .add-button {
        font-size: 20px;
        background-color: #BDEDF5;
        color: Black;
        border: none;
        padding: 0; /* Đặt padding thành 0 để giảm kích thước */
        border-radius: 4px;
        width: 40px;
        height: 40px;
        display: flex;
        align-items: center;
        justify-content: center;
        cursor: pointer;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.2);
        transition: box-shadow 0.3s ease;
    }

    .search-bar {
        position: fixed;
        width: 60%;
        left:20%;
        height: 40px;
        padding: 0 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.1);
    }

    .divider {
        border-top: 1px solid #ccc; /* Đường kẻ màu xám nhạt */
        margin: 30px 0 10px 0; /* Khoảng cách trên dưới */
        height: 1px; /* Chiều cao của đường phân cách */
    }
</style>
